package edu.uga.cs.evote.persistence.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.PreparedStatement;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Election;
import edu.uga.cs.evote.object.ObjectLayer;

public class ElectionManager {
	private ObjectLayer objectLayer = null;
    private Connection  conn = null;
    
    public ElectionManager( Connection conn, ObjectLayer objectLayer )
    {
        this.conn = conn;
        this.objectLayer = objectLayer;
        
    }
    
    public void store( Election election ) 
            throws EVException
    {
    	String				insertBallotItemSql = "insert into ballotItem(voteCount, ballotID) values ( ?, ? )";
    	String				updateBallotItemSql = "update ballotItem set voteCount = ?, ballotID = ? where id = ?";
    	PreparedStatement    stmt1;
        int                  inscnt1;
        long                 BallotItemId;
        
        try {
            
            if( !election.isPersistent() )
                stmt1 = (PreparedStatement) conn.prepareStatement( insertBallotItemSql );
            else
                stmt1 = (PreparedStatement) conn.prepareStatement( updateBallotItemSql );

            if( election.getVoteCount()!= -1 ) 
                stmt1.setInt( 1, election.getVoteCount() );
            else 
                throw new EVException( "ElectionManager.save: can't save a BallotItem: voteCount undefined" );
            if(election.getBallot() != null)
        	   stmt1.setInt(2, (int)election.getBallot().getId());

            
            if( election.isPersistent() )
                stmt1.setLong( 3, election.getBallotItemID() );

            inscnt1 = stmt1.executeUpdate();

            if( !election.isPersistent() ) {
                // in case this this object is stored for the first time,
                // we need to establish its persistent identifier (primary key)
                if( inscnt1 == 1 ) {
                    String sql = "select last_insert_id()";
                    if( stmt1.execute( sql ) ) { // statement returned a result
                        // retrieve the result
                        ResultSet r = stmt1.getResultSet();
                        // we will use only the first row!
                        while( r.next() ) {
                            // retrieve the last insert auto_increment value
                            BallotItemId = r.getLong( 1 );
                            if( BallotItemId > 0 )
                                election.setBallotItemID( BallotItemId ); // set this election's db id (proxy object)
                        }
                    }
                }
            }
            else {
                if( inscnt1 < 1 )
                    throw new EVException( "ElectionManager.save: failed to save a BallotItem" ); 
            }
        }
        catch( SQLException e ) {
            e.printStackTrace();
            throw new EVException( "ElectionManager.save: failed to save a Election: " + e );
        }
    	
    	
        String               insertElectionSql = "insert into election ( name, isPartisan, voteCount, bItemID ) values ( ?, ?, ?, ?)";              
        String               updateElectionSql = "update election  set name = ?, isPartisan = ?, voteCount = ?, bItemID = ? where id = ?";              
        PreparedStatement    stmt;
        int                  inscnt;
        long                 electionId;
        
        try {
            
        	if( !election.isPersistent() )
				stmt = (PreparedStatement) conn.prepareStatement( insertElectionSql );
			else
				stmt = (PreparedStatement) conn.prepareStatement( updateElectionSql );

			if( election.getOffice()!= null ) 
				stmt.setString( 1, election.getOffice() );
			else 
				throw new EVException( "ElectionManager.save: can't save a election: Office undefined" );
			stmt.setBoolean( 2, election.getIsPartisan() );
			if( election.getVoteCount()!= -1 ) 
				stmt.setInt( 3, election.getVoteCount() );
			else 
				throw new EVException( "ElectionManager.save: can't save a election: VoteCount undefined" );
			if( election.getBallotItemID()!= -1 ) 
				stmt.setInt( 4, (int)election.getBallotItemID() );
			else 
				throw new EVException( "ElectionManager.save: can't save a election: VoteCount undefined" );

			if( election.isPersistent() )
                stmt.setLong( 5, election.getId() );

            inscnt = stmt.executeUpdate();

            if( !election.isPersistent() ) {
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
                            electionId = r.getLong( 1 );
                            if( electionId > 0 )
                                election.setId( electionId ); // set this election's db id (proxy object)
                        }
                    }
                }
            }
            else {
                if( inscnt < 1 )
                    throw new EVException( "ElectionManager.save: failed to save a Election" ); 
            }
        }
        catch( SQLException e ) {
            e.printStackTrace();
            throw new EVException( "ElectionManager.save: failed to save a Election: " + e );
        }
    }

    public List<Election> restore( Election modelElection ) 
            throws EVException
    {
        String       selectElectionSql = "select id, name, isPartisan, voteCount, bItemID from election";
        Statement    stmt = null;
        StringBuffer query = new StringBuffer( 100 );
        StringBuffer condition = new StringBuffer( 100 );
        List<Election> elections = new ArrayList<Election>();

        condition.setLength( 0 );
        
        // form the query based on the given Election object instance
        query.append( selectElectionSql );
        
        if( modelElection != null ) {
            if( modelElection.getId() >= 0 ) // id is unique, so it is sufficient to get a election
                query.append( " where id = " + modelElection.getId() );
            else if( modelElection.getOffice() != null ) // name is unique, so it is sufficient to get a election
                query.append( " where name = '" + modelElection.getOffice() + "'" );
            else {
            	if(modelElection.getVoteCount() != 0){
            		condition.append(" voteCount = '" + modelElection.getVoteCount() + "'");
            	}
            	if(condition.length() > 0){
            		query.append(" where ");
            		query.append(condition);
            	}
            	
            }
            }
        try {

            stmt = conn.createStatement();

            // retrieve the persistent Election objects
            //
            if( stmt.execute( query.toString() ) ) { // statement returned a result
                ResultSet rs = stmt.getResultSet();
                long   id;
                String name;
                boolean isPartisan;
                int voteCount;
                int bItemID;
                
                while( rs.next() ) {

                    id = rs.getLong( 1 );
                    name = rs.getString( 2 );
                    isPartisan = rs.getBoolean( 3 );
                    voteCount = rs.getInt( 4 );
                    bItemID = rs.getInt( 5 );
                    
                    Election election = objectLayer.createElection();
                    election.setId( id );
                    election.setOffice(name);
                    election.setIsPartisan(isPartisan);
                    election.setVoteCount(voteCount);
                    election.setBallotItemID(bItemID);
                    
                    election.setBallot(null);

                    elections.add( election );

                }
                
                return elections;
            }
        }
        catch( Exception e ) {      // just in case...
            throw new EVException( "ElectionManager.restore: Could not restore persistent Election object; Root cause: " + e );
        }
        
        // if we get to this point, it's an error
        throw new EVException( "ElectionManager.restore: Could not restore persistent Election objects" );
    }
    
    
    public void delete( Election election ) 
            throws EVException
    {
    
        
        String               deleteElectionSql = "delete from election where id = ?";              
        PreparedStatement    stmt = null;
        int                  inscnt;
        
        // form the query based on the given Election object instance
        if( !election.isPersistent() ) // is the Election object persistent?  If not, nothing to actually delete
            return;
        
        try {
            
            //DELETE t1, t2 FROM t1, t2 WHERE t1.id = t2.id;
            //DELETE FROM t1, t2 USING t1, t2 WHERE t1.id = t2.id;
            stmt = (PreparedStatement) conn.prepareStatement( deleteElectionSql );
            
            stmt.setLong( 1, election.getId() );
            
            inscnt = stmt.executeUpdate();
            
            if( inscnt == 0 ) {
                throw new EVException( "ElectionManager.delete: failed to delete this Election" );
            }
        }
        catch( SQLException e ) {
            throw new EVException( "ElectionManager.delete: failed to delete this Election: " + e.getMessage() );
        }
        
    	String               deleteBallotItemSql = "delete from ballotItem where id = ?";              
        PreparedStatement    stmt1 = null;
        int                  inscnt1;
        
        // form the query based on the given Election object instance
        if( !election.isPersistent() ) // is the Election object persistent?  If not, nothing to actually delete
            return;
        
        try {
            
            //DELETE t1, t2 FROM t1, t2 WHERE t1.id = t2.id;
            //DELETE FROM t1, t2 USING t1, t2 WHERE t1.id = t2.id;
            stmt1 = (PreparedStatement) conn.prepareStatement( deleteBallotItemSql );
            
            stmt1.setLong( 1, election.getBallotItemID() );
            
            inscnt1 = stmt1.executeUpdate();
            
            if( inscnt1 == 0 ) {
                throw new EVException( "ElectionManager.delete: failed to delete this BallotItem" );
            }
        }
        catch( SQLException e ) {
            throw new EVException( "ElectionManager.delete: failed to delete this BallotItem: " + e.getMessage() );
        }
    }
}
