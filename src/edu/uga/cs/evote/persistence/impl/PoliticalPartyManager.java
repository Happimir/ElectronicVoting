package edu.uga.cs.evote.persistence.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.PreparedStatement;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Candidate;
import edu.uga.cs.evote.entity.PoliticalParty;
import edu.uga.cs.evote.object.ObjectLayer;

public class PoliticalPartyManager {
	private ObjectLayer objectLayer = null;
    private Connection  conn = null;
    
    public PoliticalPartyManager( Connection conn, ObjectLayer objectLayer )
    {
        this.conn = conn;
        this.objectLayer = objectLayer;
        
    }
    
    public void store( PoliticalParty party ) 
            throws EVException
    {
        String               insertPartySql = "insert into party (name) values (?)";              
        String               updatePartySql = "update party  set name = ? where id = ?";              
        PreparedStatement    stmt;
        int                  inscnt;
        long                 partyId;
        
        try {
            
            if( !party.isPersistent() )
                stmt = (PreparedStatement) conn.prepareStatement( insertPartySql );
            else
                stmt = (PreparedStatement) conn.prepareStatement( updatePartySql );

            if( party.getName()!= null ) // evoteuser is unique, so it is sufficient to get a party
                stmt.setString( 1, party.getName() );
            else 
                throw new EVException( "PartyManager.save: can't save a ElectionOfficer: userName undefined" );

            
            if( party.isPersistent() )
                stmt.setLong( 2, party.getId() );

            inscnt = stmt.executeUpdate();

            if( !party.isPersistent() ) {
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
                            partyId = r.getLong( 1 );
                            if( partyId > 0 )
                                party.setId( partyId ); // set this party's db id (proxy object)
                        }
                    }
                }
            }
            else {
                if( inscnt < 1 )
                    throw new EVException( "PartyManager.save: failed to save a ElectionOfficer" ); 
            }
        }
        catch( SQLException e ) {
            e.printStackTrace();
            throw new EVException( "PartyManager.save: failed to save a ElectionOfficer: " + e );
        }
    }

    public List<PoliticalParty> restore( PoliticalParty modelPoliticalParty ) 
            throws EVException
    {
        String       selectPoliticalPartySql = "select id, name from party";
        Statement    stmt = null;
        StringBuffer query = new StringBuffer( 100 );
        StringBuffer condition = new StringBuffer( 100 );
        List<PoliticalParty> partys = new ArrayList<PoliticalParty>();

        condition.setLength( 0 );
        
        // form the query based on the given ElectionOfficer object instance
        query.append( selectPoliticalPartySql );
        
        if( modelPoliticalParty != null ) {
            if( modelPoliticalParty.getId() >= 0 ) // id is unique, so it is sufficient to get a party
                query.append( " where id = " + modelPoliticalParty.getId() );
            else if( modelPoliticalParty.getName() != null ) // name is unique, so it is sufficient to get a party
                query.append( " where name = '" + modelPoliticalParty.getName() + "'" );
           
            }
        
        try {

            stmt = conn.createStatement();

            // retrieve the persistent ElectionOfficer objects
            //
            
            if( stmt.execute( query.toString() ) ) { // statement returned a result
                ResultSet rs = stmt.getResultSet();
                long   id;
                String name;
                
                while( rs.next() ) {
                    id = rs.getLong( 1 );
                    name = rs.getString( 2 );
                    
                    PoliticalParty party = objectLayer.createPoliticalParty(name);
                    party.setId( id );
                    party.setName(name);

                    partys.add( party );

                }
                
                return partys;
            }
        }
        catch( Exception e ) {      // just in case...
            throw new EVException( "PartyManager.restore: Could not restore persistent PoliticalParty object; Root cause: " + e );
        }
        
        // if we get to this point, it's an error
        throw new EVException( "PartyManager.restore: Could not restore persistent PoliticalParty objects" );
    }
    
    public List<Candidate> restoreCandidateIsMemberOfPoliticalParty(PoliticalParty politicalParty) throws EVException {
		String selectPersonSql = "select c.id, c.name, c.voteCount, c.electionID, c.partyID," +
								 "p.id, p.name " +
                				 "from candidate c, party p where c.partyID = p.id";
		Statement    stmt = null;
		StringBuffer query = new StringBuffer( 100 );
		StringBuffer condition = new StringBuffer( 100 );
		List<Candidate> candidateItems = new ArrayList<Candidate>();
		
		condition.setLength( 0 );

		// form the query based on the given Ballot object instance
		query.append( selectPersonSql );

		if( politicalParty != null ) {
			if( politicalParty.getId() >= 0 ) // id is unique, so it is sufficient to get a ballot
				query.append( " and e.id = " + politicalParty.getId() );
			if ( politicalParty.getName() != null)
				query.append(" and e.name = " + politicalParty.getName());
		}

		try {

			stmt = conn.createStatement();

			// retrieve the persistent Candidate objects
			//
			if( stmt.execute( query.toString() ) ) { // statement returned a result
         
				long   id;
				String name; 
				int voteCount;

				ResultSet rs = stmt.getResultSet();
				
				while( rs.next() ){
					Candidate newCandidate = null;
					//get it using the ballot type, if it is an election then we make an election and add it to the list.
					//if it is an issue, then we make an issue; thus we will need to include a SQL query to grab the type. 
					//add integer position for our issue and election table to represent order. 
					id = rs.getLong( 1 );
					name = rs.getString( 2 );
					voteCount = rs.getInt( 3 );
					
					newCandidate = objectLayer.createCandidate();
   
					newCandidate.setId(id);
					newCandidate.setName(name);
					newCandidate.setVoteCount(voteCount);
					
					newCandidate.setElection(null);
					newCandidate.setPoliticalParty(null);
					
					candidateItems.add(newCandidate);
				}
			}
			return candidateItems;
		}
		catch( Exception e ) {      // just in case...
			throw new EVException( "CandidateManager.restoreCandidateisCandidiateInElection: Could not restore persistent Candidate objects; Root cause: " + e );
		}
		
	}
    
    public void delete( PoliticalParty party ) 
            throws EVException
    {
        String               deleteElectionOfficerSql = "delete from party where id = ?";              
        PreparedStatement    stmt = null;
        int                  inscnt;
        
        // form the query based on the given ElectionOfficer object instance
        if( !party.isPersistent() ) // is the ElectionOfficer object persistent?  If not, nothing to actually delete
            return;
        
        try {
            
            //DELETE t1, t2 FROM t1, t2 WHERE t1.id = t2.id;
            //DELETE FROM t1, t2 USING t1, t2 WHERE t1.id = t2.id;
            stmt = (PreparedStatement) conn.prepareStatement( deleteElectionOfficerSql );
            
            stmt.setLong( 1, party.getId() );
            
            inscnt = stmt.executeUpdate();
            
            if( inscnt == 0 ) {
                throw new EVException( "PartyManager.delete: failed to delete this ElectionOfficer" );
            }
        }
        catch( SQLException e ) {
            throw new EVException( "PartyManager.delete: failed to delete this ElectionOfficer: " + e.getMessage() );
        }
    }
}

