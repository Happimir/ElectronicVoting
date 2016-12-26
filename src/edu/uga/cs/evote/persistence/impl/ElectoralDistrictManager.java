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
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.entity.Ballot;
import edu.uga.cs.evote.entity.ElectoralDistrict;
import edu.uga.cs.evote.entity.Voter;


public class ElectoralDistrictManager {
	private ObjectLayer objectLayer = null;
    private Connection  conn = null;
    
    public ElectoralDistrictManager( Connection conn, ObjectLayer objectLayer )
    {
        this.conn = conn;
        this.objectLayer = objectLayer;
        
    }
    
    public void store( ElectoralDistrict electoralDistrict ) 
            throws EVException
    {
        String               insertElectoralDistrictSql = "insert into district ( name ) values ( ? )";              
        String               updateElectoralDistrictSql = "update district  set name = ? where id = ?";              
        PreparedStatement    stmt;
        int                  inscnt;
        long                 electoralDistrictId;
        
        try {
            
            if( !electoralDistrict.isPersistent() )
                stmt = (PreparedStatement) conn.prepareStatement( insertElectoralDistrictSql );
            else
                stmt = (PreparedStatement) conn.prepareStatement( updateElectoralDistrictSql );

            if( electoralDistrict.getName()!= null ) // name is unique, so it is sufficient to get a electoralDistrict
                stmt.setString( 1, electoralDistrict.getName() );
            else 
                throw new EVException( "ElectoralDistrictManager.save: can't save a ElectionOfficer: userName undefined" );

            
            if( electoralDistrict.isPersistent() )
                stmt.setLong( 2, electoralDistrict.getId() );

            inscnt = stmt.executeUpdate();

            if( !electoralDistrict.isPersistent() ) {
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
                            electoralDistrictId = r.getLong( 1 );
                            if( electoralDistrictId > 0 )
                                electoralDistrict.setId( electoralDistrictId ); // set this electoralDistrict's db id (proxy object)
                        }
                    }
                }
            }
            else {
                if( inscnt < 1 )
                    throw new EVException( "ElectoralDistrictManager.save: failed to save a ElectionOfficer" ); 
            }
        }
        catch( SQLException e ) {
            e.printStackTrace();
            throw new EVException( "ElectoralDistrictManager.save: failed to save a ElectionOfficer: " + e );
        }
    }
    
    public void storeElectoralDistrictHasBallotBallot(ElectoralDistrict electoralDistrict, Ballot ballot)
			throws EVException {
    	
    	PreparedStatement stmt = null;
    	int insct;
    	
    	String updateBallot = "update ballot set startDate = ?, endDate = ?, approved = ?, districtID = ? where id = ?";
    	
    	if(electoralDistrict.isPersistent() && ballot.isPersistent()) {
	    	try {
	    		stmt = (PreparedStatement) conn.prepareStatement(updateBallot);
	    		
	    		if(ballot.getOpenDate() != null) {
	    			stmt.setDate(1, (Date) ballot.getOpenDate());
	    		} else {
	    			throw new EVException("open date is null");
	    		}
	    		if(ballot.getCloseDate() != null) {
	    			stmt.setDate(2, (Date) ballot.getCloseDate());
	    		}
	    		
	    		stmt.setBoolean(3, ballot.getApproved());
	    		stmt.setInt(4, (int) electoralDistrict.getId()); 
	    		stmt.setInt(5, (int) ballot.getId());
	    		
	    		insct = stmt.executeUpdate();

	    	} catch(SQLException e) {
	    		e.printStackTrace();
	    		throw new EVException("Could not store electoral district has ballot");
	    	}
    	} else {
    		throw new EVException("either ballot or electoral district is not persistent: storeElectoralDistrictHasBallotBallot");
    	}
    	
    	
    }

    public List<ElectoralDistrict> restore( ElectoralDistrict modelElectoralDistrict ) 
            throws EVException
    {
        String       selectElectionOfficerSql = "select id, name from district";
        Statement    stmt = null;
        StringBuffer query = new StringBuffer( 100 );
        StringBuffer condition = new StringBuffer( 100 );
        List<ElectoralDistrict> electoralDistricts = new ArrayList<ElectoralDistrict>();

        condition.setLength( 0 );
        
        // form the query based on the given ElectionOfficer object instance
        query.append( selectElectionOfficerSql );
        
        if( modelElectoralDistrict != null ) {
            if( modelElectoralDistrict.getId() >= 0 ) // id is unique, so it is sufficient to get a electoralDistrict
                query.append( " where id = " + modelElectoralDistrict.getId() );
            else if( modelElectoralDistrict.getName() != null ) // name is unique, so it is sufficient to get a electoralDistrict
                query.append( " where name = '" + modelElectoralDistrict.getName() + "'" );
           
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

                    ElectoralDistrict electoralDistrict = objectLayer.createElectoralDistrict(name);
                    electoralDistrict.setId( id );
                    electoralDistrict.setName(name);

                    electoralDistricts.add( electoralDistrict );

                }
                
                return electoralDistricts;
            }
        }
        catch( Exception e ) {      // just in case...
            throw new EVException( "ElectoralDistrictManager.restore: Could not restore persistent ElectionOfficer object; Root cause: " + e );
        }
        
        // if we get to this point, it's an error
        throw new EVException( "ElectoralDistrictManager.restore: Could not restore persistent ElectionOfficer objects" );
    }
    
    public List<Ballot> restoreElectoralDistrictHasBallotBallot(ElectoralDistrict electoralDistrict)
			throws EVException {
    		String selectPersonSql = "select b.id, b.startDate, b.endDate, b.approved, b.districtID," +
    								 "ed.id, ed.name " +
    								 "from ballot b, district ed where b.districtID = ed.id";
    		Statement    stmt = null;
    		StringBuffer query = new StringBuffer( 100 );
    		StringBuffer condition = new StringBuffer( 100 );
    		List<Ballot>   ballotItems = new ArrayList<Ballot>();

    		condition.setLength( 0 );

    		// form the query based on the given Ballot object instance
    		query.append( selectPersonSql );

    		if( electoralDistrict != null ) {
    			if( electoralDistrict.getId() >= 0 ) // id is unique, so it is sufficient to get a ballot
    				query.append( " and ed.id = " + electoralDistrict.getId() );
    			if ( electoralDistrict.getName() != null)
    				query.append(" and e.name = " + electoralDistrict.getName());
    			}

    		try {

    			stmt = conn.createStatement();

    			// retrieve the persistent Candidate objects
    			//
    			if( stmt.execute( query.toString() ) ) { // statement returned a result
    				
    				Ballot newBallot;
    				long   id;
    				Date startDate;
    				Date endDate;
    				boolean approved;
    				
    				ResultSet rs = stmt.getResultSet();
    				
    				while( rs.next() ) {
    					//	get it using the ballot type, if it is an election then we make an election and add it to the list.
    					//	if it is an issue, then we make an issue; thus we will need to include a SQL query to grab the type. 
    					//add integer position for our issue and election table to represent order. 
    					id = rs.getLong( 1 );
    					startDate = rs.getDate( 2 );
    					endDate = rs.getDate( 3 );
    					approved = rs.getBoolean( 4 );
	
    					newBallot = objectLayer.createBallot();


    					newBallot.setId(id);
    					newBallot.setOpenDate(startDate);
    					newBallot.setCloseDate(endDate);
    					newBallot.setApproved(approved);

    					// set this to null for the "lazy" association traversal
    					newBallot.setElectoralDistrict(null);
    					
    					ballotItems.add( newBallot );
    				}
    			}
    			return ballotItems;
    		}
    		catch( Exception e ) {      // just in case...
    			throw new EVException( "BallotManager.restoreBallotIncludes: Could not restore persistent BallotItem objects; Root cause: " + e );
    		}
    	}
    
    public ElectoralDistrict restoreElectoralDistrictHasBallotBallot(Ballot ballot)
			throws EVException {
    		String selectPersonSql = "select ed.id, ed.name, " +
    								 "select b.id, b.startDate, b.endDate, b.approved, b.districtID" +
    								 "from ballot b, district ed where b.districtID = ed.id";
    		Statement    stmt = null;
    		StringBuffer query = new StringBuffer( 100 );
    		StringBuffer condition = new StringBuffer( 100 );
    		ElectoralDistrict newDistrict = null;
    		condition.setLength( 0 );

    		// form the query based on the given Ballot object instance
    		query.append( selectPersonSql );

    		if( ballot != null ) {
    			if( ballot.getId() >= 0 ) // id is unique, so it is sufficient to get a ballot
    				query.append( " and b.id = " + ballot.getId() );
    			else {
    				if(ballot.getOpenDate() != null && condition.length() == 0)
    					query.append("b.startDate = " + ballot.getOpenDate());
    				else
    					query.append("AND b.startDate = " + ballot.getOpenDate());
    				if(ballot.getCloseDate() != null && condition.length() == 0)
    					query.append("b.endDate = " + ballot.getCloseDate());
    				else
    					query.append("AND b.endDate = " + ballot.getCloseDate());
    				if(condition.length() == 0)
    					query.append("b.approved = " + ballot.getApproved());
    				else
    					query.append("AND b.approve = " + ballot.getApproved());
    				if( condition.length() > 0 ) {
                        query.append( condition );
                    }
    			}
    		}
    		try {

    			stmt = conn.createStatement();

    			// retrieve the persistent Candidate objects
    			//
    			if( stmt.execute( query.toString() ) ) { // statement returned a result
    				
    				long   id;
    				String name;

    				ResultSet rs = stmt.getResultSet();
    				
    					//	get it using the ballot type, if it is an election then we make an election and add it to the list.
    					//	if it is an issue, then we make an issue; thus we will need to include a SQL query to grab the type. 
    					//add integer position for our issue and election table to represent order. 
    					id = rs.getLong( 1 );
    					name = rs.getString( 2 );
	
    					newDistrict = objectLayer.createElectoralDistrict();


    					newDistrict.setId(id);
    					newDistrict.setName(name);
    					    					

    			}
    			return newDistrict;
    		}
    		catch( Exception e ) {      // just in case...
    			throw new EVException( "BallotManager.restoreBallotIncludes: Could not restore persistent BallotItem objects; Root cause: " + e );
    		}
    	}
    
	public List<Voter> restoreVoterBelongsToElectoralDistrict(ElectoralDistrict electoralDistrict) throws EVException {
		String selectPersonSql = "select v.id, u.username, u.userpass, u.email, u.firstname, u.lastname, u.address, v.age, " +
				"d.id" +
				"from voter v,  user u, district d where u.id = v.voterID and v.districtID = d.id";
		Statement    stmt = null;
		StringBuffer query = new StringBuffer( 100 );
		StringBuffer condition = new StringBuffer( 100 );
		List<Voter>   voters = new ArrayList<Voter>();

		condition.setLength( 0 );

		// form the query based on the given Person object instance
		query.append( selectPersonSql );

		if( electoralDistrict != null ) {
			if( electoralDistrict.getId() >= 0 ) // id is unique, so it is sufficient to get a person
				query.append( " and d.id = " + electoralDistrict.getId() );
			else if( electoralDistrict.getName() != null ) // userName is unique, so it is sufficient to get a person
				query.append( " and d.name = '" + electoralDistrict.getName() + "'" );
		}

		try {

			stmt = conn.createStatement();

			// retrieve the persistent Club objects
			//
			if( stmt.execute( query.toString() ) ) { // statement returned a result

				long   id;
				String username;
				String userpass;
				String email;
				String firstname;
				String lastname;
				String address;
				int   age;
				Voter   nextVoter = null;

				ResultSet rs = stmt.getResultSet();

				while( rs.next() ) {

					id = rs.getLong( 1 );
					username = rs.getString( 2 );
					userpass = rs.getString( 3 );
					email = rs.getString( 4 );
					firstname = rs.getString( 5 );
					lastname = rs.getString( 6 );
					address = rs.getString( 7 );
					age = rs.getInt( 8 );
					
					nextVoter = objectLayer.createVoter(); // create a proxy club object
					// and now set its retrieved attributes
					nextVoter.setId( id );
					nextVoter.setUserName( username );
					nextVoter.setPassword( userpass );
					nextVoter.setEmailAddress( email );
					nextVoter.setFirstName( firstname );
					nextVoter.setLastName(lastname);
					nextVoter.setAddress(address);
					nextVoter.setAge( age );
					// set this to null for the "lazy" association traversal
					nextVoter.setElectoralDistrict( null );

					voters.add( nextVoter );
				}

				return voters;
			}
		}
		catch( Exception e ) {      // just in case...
			throw new EVException( "ElctoralDistrictManager.restoreVoterBelongsToElectoralDistrict: Could not restore persistent Voter objects; Root cause: " + e );
		}

		throw new EVException( "ElctoralDistrictManager.restoreVoterBelongsToElectoralDistrict: Could not restore persistent Voter objects" );

            
    }
    
    
    public void delete( ElectoralDistrict electoralDistrict ) 
            throws EVException
    {
        String               deleteElectionDistrictSql = "delete from district where id = ?";              
        PreparedStatement    stmt = null;
        int                  inscnt;
        
        // form the query based on the given ElectionOfficer object instance
        if( !electoralDistrict.isPersistent() ) // is the ElectionOfficer object persistent?  If not, nothing to actually delete
            return;
        
        try {
            
            //DELETE t1, t2 FROM t1, t2 WHERE t1.id = t2.id;
            //DELETE FROM t1, t2 USING t1, t2 WHERE t1.id = t2.id;
            stmt = (PreparedStatement) conn.prepareStatement( deleteElectionDistrictSql );
            
            stmt.setLong( 1, electoralDistrict.getId() );
            
            inscnt = stmt.executeUpdate();
            
            if( inscnt == 0 ) {
                throw new EVException( "ElectoralDistrictManager.delete: failed to delete this ElectionOfficer" );
            }
        }
        catch( SQLException e ) {
            throw new EVException( "ElectoralDistrictManager.delete: failed to delete this ElectionOfficer: " + e.getMessage() );
        }
    }
}
