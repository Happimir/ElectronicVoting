package edu.uga.cs.evote.persistence.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.PreparedStatement;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Election;
import edu.uga.cs.evote.entity.Issue;
import edu.uga.cs.evote.entity.VoteRecord;
import edu.uga.cs.evote.object.ObjectLayer;

public class VoteRecordManager {
	private ObjectLayer objectLayer = null;
    private Connection  conn = null;
    
    public VoteRecordManager( Connection conn, ObjectLayer objectLayer )
    {
        this.conn = conn;
        this.objectLayer = objectLayer;
        
    }
    
    public void store( VoteRecord voteRecord ) 
            throws EVException
    {
        String               insertElectionSql = "insert into voteRecord (voterID, ballotItemID, voteDate ) values ( ?, ?, ?)";              
        String               updateElectionSql = "update voteRecord  set voterID = ?, ballotItemID = ?, voteDate = ? where id = ?";              
        PreparedStatement    stmt;
        int                  inscnt;
        long                 voteRecordId;
        
        
        
        try {
            
            if( !voteRecord.isPersistent() )
                stmt = (PreparedStatement) conn.prepareStatement( insertElectionSql );
            else
                stmt = (PreparedStatement) conn.prepareStatement( updateElectionSql );

            if( voteRecord.getVoter()!= null ) 
                stmt.setInt( 1, Integer.parseInt( voteRecord.getVoter().getVoterId() ));
            else 
                throw new EVException( "VoteRecordManager.save: can't save a Vote Record: voter undefined" );
            if( voteRecord.getBallotItem()!= null ) {
            	if(voteRecord.getBallotItem() instanceof Issue){
                	Issue modelIssue = objectLayer.createIssue();
                	modelIssue.setId(voteRecord.getBallotItem().getId());
                	Issue issue = objectLayer.findIssue(modelIssue).get(0);
                	stmt.setInt( 2, (int)issue.getBallotItemId() );
                }
            	else{
            		Election modelElection = objectLayer.createElection();
            		modelElection.setId(voteRecord.getBallotItem().getId());
            		Election election = objectLayer.findElection(modelElection).get(0);
            		stmt.setInt( 2,  (int)election.getBallotItemID());
            	}
            }        
            else 
                throw new EVException( "VoteRecordManager.save: can't save a Vote Record: ballot undefined" );
            if( voteRecord.getDate()!= null ) {
            	java.sql.Date sqlDate = new java.sql.Date(voteRecord.getDate().getTime());
                stmt.setDate( 3, sqlDate );
            }
            else 
                throw new EVException( "VoteRecordManager.save: can't save a Vote Record: date undefined" );
            
            if( voteRecord.isPersistent() )
                stmt.setLong( 4, voteRecord.getId() );
            
            System.out.println(stmt.toString());
            inscnt = stmt.executeUpdate();
 
            if( !voteRecord.isPersistent() ) {
                // in case this this object is stored for the first time,
                // we need to establish its persistent identifier (primary key)
                if( inscnt == 1 ) {
                    String sql = "select last_insert_id()";
                    if( stmt.execute( sql ) ) { // statement returned a result
                        // retrieve the result
                        ResultSet r = stmt.getResultSet();
                        // we will use only the first row!
                        while( r.next() ) {
                            // retrieve the last insert auto_increment value
                            voteRecordId = r.getLong( 1 );
                            if( voteRecordId > 0 )
                                voteRecord.setId( voteRecordId ); // set this voteRecord's db id (proxy object)
                        }
                    }
                }
            }
            else {
                if( inscnt < 1 )
                    throw new EVException( "VoterRecordManager.save: failed to save a VoteRecord" ); 
            }
        }
        catch( SQLException e ) {
            e.printStackTrace();
            throw new EVException( "VoterRecordManager.save: failed to save a VoteRecord: " + e );
        }
    }

    public List<VoteRecord> restore( VoteRecord modelVoteRecord ) 
            throws EVException
    {
    	Issue modelIssue = objectLayer.createIssue();
    	modelIssue.setId(modelVoteRecord.getBallotItem().getId());
    	Issue issue = objectLayer.findIssue(modelIssue).get(0);
    	
        String       selectElectionSql = "select voterID, ballotItemID, voteDate from voteRecord";
        Statement    stmt = null;
        StringBuffer query = new StringBuffer( 100 );
        StringBuffer condition = new StringBuffer( 100 );
        List<VoteRecord> voteRecords = new ArrayList<VoteRecord>();

        condition.setLength( 0 );
        
        // form the query based on the given Election object instance
        query.append( selectElectionSql );
        
        if( modelVoteRecord != null ) {
            if( modelVoteRecord.getId() >= 0 ) // id is unique, so it is sufficient to get a voteRecord
                query.append( " where id = " + modelVoteRecord.getId() );
            else{
            	if( modelVoteRecord.getVoter() != null )
            		condition.append("voterID = '" + modelVoteRecord.getVoter().getId() + "'");
            	if( modelVoteRecord.getBallotItem() != null){
            		if( condition.length() > 0 )
    					condition.append( " and" );
    				else
    					condition.append( " where");
            		condition.append(" ballotItemID = '" + issue.getBallotItemId() + "'");
            	}
            	if( modelVoteRecord.getDate() != null){
                	java.sql.Date sqlDate = new java.sql.Date(modelVoteRecord.getDate().getTime());
                	if( condition.length() > 0 )
    					condition.append( " and" );
    				else
    					condition.append( " where");
            		condition.append(" Date ='" + sqlDate + "'");
            	}
            	if( condition.length() > 0){
            		query.append(" where ");
            		query.append(condition);
            	}
            }
        }
        System.out.println(query.toString());
        try {
            stmt = conn.createStatement();
            System.out.println("Query for Vote Record: " + query.toString());
            // retrieve the persistent Election objects
            //
            if( stmt.execute( query.toString() ) ) { // statement returned a result
                ResultSet rs = stmt.getResultSet();
                long   id;
                Date date;
                
                while( rs.next() ) {

                    id = rs.getLong( 1 );
                    date = rs.getDate( 3 );
                    
                    VoteRecord voteRecord = objectLayer.createVoteRecord();
                    voteRecord.setId( id );
                    voteRecord.setDate(date);

                    voteRecords.add( voteRecord );

                }
                
                return voteRecords;
            }
        }
        catch( Exception e ) {      // just in case...
            throw new EVException( "IssueManager.restore: Could not restore persistent Voter Record object; Root cause: " + e );
        }
        
        // if we get to this point, it's an error
        throw new EVException( "IssueManager.restore: Could not restore persistent Voter Record objects" );
    }
    
    
    public void delete( VoteRecord voteRecord ) 
            throws EVException
    {
        String               deleteElectionSql = "delete from voteRecord where id = ?";              
        PreparedStatement    stmt = null;
        int                  inscnt;
        
        // form the query based on the given Election object instance
        if( !voteRecord.isPersistent() ) // is the Election object persistent?  If not, nothing to actually delete
            return;
        
        try {
            
            //DELETE t1, t2 FROM t1, t2 WHERE t1.id = t2.id;
            //DELETE FROM t1, t2 USING t1, t2 WHERE t1.id = t2.id;
            stmt = (PreparedStatement) conn.prepareStatement( deleteElectionSql );
            
            stmt.setLong( 1, voteRecord.getId() );
            
            inscnt = stmt.executeUpdate();
            
            if( inscnt == 0 ) {
                throw new EVException( "IssueManager.delete: failed to delete this Issue" );
            }
        }
        catch( SQLException e ) {
            throw new EVException( "IssueManager.delete: failed to delete this Issue: " + e.getMessage() );
        }
    }
}

