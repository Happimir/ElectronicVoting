package edu.uga.cs.evote.persistence.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.PreparedStatement;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.ElectoralDistrict;
import edu.uga.cs.evote.entity.Voter;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.persistence.PersistenceLayer;

public class VoterManager {
	private ObjectLayer objectLayer = null;
	private Connection  conn = null;

	public VoterManager( Connection conn, ObjectLayer objectLayer )
	{
		this.conn = conn;
		this.objectLayer = objectLayer;

	}

	public void store( Voter voter ) 
			throws EVException
	{

		String               insertUserSql = "insert into voter( username, userpass, email, firstname, lastname, address, age, districtID ) values ( ?, ?, ?, ?, ?, ?, ?, ? )";              
		String               updateUserSql = "update voter  set username = ?, userpass = ?, email = ?, firstname = ?, lastname = ?, address = ?, age = ?, districtID = ? where id = ?";              
		PreparedStatement    stmt;
		int                  inscnt;
		String               voterId;

		try {

			if( !voter.isPersistent() )
				stmt = (PreparedStatement) conn.prepareStatement( insertUserSql );
			else
				stmt = (PreparedStatement) conn.prepareStatement( updateUserSql );

			if( voter.getUserName() != null ) // evoteuser is unique, so it is sufficient to get a voter
				stmt.setString( 1, voter.getUserName() );
			else 
				throw new EVException( "VoterManager.save: can't save a Voter: userName undefined" );

			if( voter.getPassword() != null )
				stmt.setString( 2, voter.getPassword() );
			else
				throw new EVException( "VoterManager.save: can't save a Voter: password undefined" );

			if( voter.getEmailAddress() != null )
				stmt.setString( 3,  voter.getEmailAddress() );
			else
				throw new EVException( "VoterManager.save: can't save a Voter: email undefined" );

			if( voter.getFirstName() != null )
				stmt.setString( 4, voter.getFirstName() );
			else
				throw new EVException( "VoterManager.save: can't save a Voter: first name undefined" );

			if( voter.getLastName() != null )
				stmt.setString( 5, voter.getLastName() );
			else
				throw new EVException( "VoterManager.save: can't save a Voter: last name undefined" );

			if( voter.getAddress() != null )
				stmt.setString( 6, voter.getAddress() );
			else
				throw new EVException( "VoterManager.save: can't save a Voter: address undefined" );

			if( voter.getAddress() != null )
				stmt.setInt( 7, voter.getAge() );
			else
				throw new EVException( "VoterManager.save: can't save a Voter: age undefined" );

			if( voter.getElectoralDistrict().getId() != -1)
				stmt.setInt(8, (int)voter.getElectoralDistrict().getId());
			else
				throw new EVException( "VoterManager.save: can't save a Voter: districtID undefined" );

			if( voter.isPersistent() )
				stmt.setInt( 9, (int)voter.getId() );

			inscnt = stmt.executeUpdate();

			if( !voter.isPersistent() ) {
				if( inscnt >= 1 ) {
					String sql = "select last_insert_id()";
					if( stmt.execute( sql ) ) { // statement returned a result

						// retrieve the result
						ResultSet r = stmt.getResultSet();

						// we will use only the first row!
						//
						while( r.next() ) {
							// retrieve the last insert auto_increment value
							//userId = Integer.toString(r.getInt( 1 ));
							voterId = r.getString(1);
							if( voterId != null )
								voter.setVoterId( voterId ); // set this person's db id (proxy object)
						}
					}
				}
				else
					throw new EVException( "VoterManager.save: failed to save a voter" );
			}
			else {
				if( inscnt < 1 )
					throw new EVException( "VoterManager.save: failed to save a voter" ); 
			}
		}

		catch( SQLException e ) {
			e.printStackTrace();
			throw new EVException( "VoterManager.save: failed to save a voter: " + e );
		}
	}

	public void storeVoterBelongsToElectoralDistrict(Voter voter, ElectoralDistrict electoralDistrict) throws EVException {

		//update voter's foreign key to electoral district's 
		String updateVoter = "update voter set username = ?, userpass = ?, email = ?, firstname = ?, lastname = ?,address = ?, age = ? districtID = ? where id = ?";
		PreparedStatement stmt = null;
		int 				inscnt;

		try {
			stmt = (PreparedStatement) conn.prepareStatement(updateVoter);

			if(voter.isPersistent() && electoralDistrict.isPersistent()) {
				if(voter.getUserName() != null) {
					stmt.setString(1, voter.getUserName());
				} else {
					throw new EVException("Voter name is null");
				}
				if(voter.getPassword() != null) {
					stmt.setString(2, voter.getPassword());
				} else {
					throw new EVException("voter password is null");
				}
				if(voter.getEmailAddress() != null) {
					stmt.setString(3, voter.getEmailAddress());
				} else {
					throw new EVException("voter email is null");
				}
				if(voter.getFirstName() != null) {
					stmt.setString(4, voter.getFirstName());
				} else {
					throw new EVException("voter has no first name");
				} 
				if(voter.getLastName() != null) {
					stmt.setString(5, voter.getLastName());
				} else {
					throw new EVException ("voter lastname is null");
				}
				if(voter.getAddress() != null) {
					stmt.setString(6, voter.getAddress());
				} else {
					throw new EVException("voter address is null");
				}
				if(voter.getAge() != 0) { 
					stmt.setInt(7, voter.getAge()); 
				} else {
					stmt.setInt(7, 0);
				}

				stmt.setInt(8, (int) electoralDistrict.getId());
				stmt.setInt(9, (int) voter.getId());

				inscnt = stmt.executeUpdate();

			} else {
				throw new EVException("Either voter or electoralDistrict is not persistent: storeVoterBelongsToElectoralDistrict");
			}
		} catch(SQLException e) {
			e.printStackTrace();
			throw new EVException("voterManager storeVoterBelongsToElectoralDistrict: fail to store voter + district " + e);
		}

	}

	public List<Voter> restore( Voter modelVoter ) 
			throws EVException
	{
		String       selectVoterSql = "select id, username, userpass, email, firstname, lastname, address, age, districtID from voter";
		Statement    stmt = null;
		StringBuffer query = new StringBuffer( 100 );
		StringBuffer condition = new StringBuffer( 100 );
		List<Voter> voters = new ArrayList<Voter>();

		condition.setLength( 0 );

		// form the query based on the given Voter object instance
		query.append( selectVoterSql );

		if( modelVoter != null ) {
			if( modelVoter.getId() >= 0 ) // id is unique, so it is sufficient to get a voter
				condition.append( " where id = " + modelVoter.getId() );

			if( modelVoter.getUserName() != null ){ // userName is unique, so it is sufficient to get a voter
				if( condition.length() > 0 )
					condition.append( " and" );
				else
					condition.append( " where");
				condition.append( " username = '" + modelVoter.getUserName() + "'" );
			}

			if( modelVoter.getPassword() != null ){
				if( condition.length() > 0 )
					condition.append( " and" );
				else
					condition.append( " where");
				condition.append( " userpass = '" + modelVoter.getPassword() + "'" );
			}

			if( modelVoter.getEmailAddress() != null ) {
				if( condition.length() > 0 )
					condition.append( " and" );
				else
					condition.append( " where");
				condition.append( " email = '" + modelVoter.getEmailAddress() + "'" );
			}

			if( modelVoter.getFirstName() != null ) {
				if( condition.length() > 0 )
					condition.append( " and" );
				else
					condition.append( " where");
				condition.append( " firstName = '" + modelVoter.getFirstName() + "'" );
			}

			if( modelVoter.getLastName() != null ) {
				if( condition.length() > 0 )
					condition.append( " and" );
				else
					condition.append( " where");
				condition.append( " lastName = '" + modelVoter.getLastName() + "'" );
			}

			if( modelVoter.getAddress() != null ) {
				if( condition.length() > 0 )
					condition.append( " and" );
				else
					condition.append( " where");
				condition.append( " address = '" + modelVoter.getAddress() + "'" );
			}        
			if( modelVoter.getAge() != 0) {
				if( condition.length() > 0)
					condition.append(" and");
				else
					condition.append( " where");
				condition.append(" age = '" + modelVoter.getAge() + "'");
			}
			if( modelVoter.getElectoralDistrict() != null) {
				if( condition.length() > 0)
					condition.append(" and");
				else
					condition.append( " where");
				condition.append(" districtID = '" + modelVoter.getElectoralDistrict().getId() + "'");
			}

			query.append(condition);
		}
		try {

			stmt = conn.createStatement();
			// retrieve the persistent Voter objects
			//
			if( stmt.execute( query.toString() ) ) { // statement returned a result
				ResultSet rs = stmt.getResultSet();
				String userName;
				String password;
				String email;
				String firstName;
				String lastName;
				String address;
				int districtID;
				int age;
				String voterID;
				Voter nextVoter = null;
				

				while( rs.next() ) {
					voterID = rs.getString( 1 );
					userName = rs.getString( 2 );
					password = rs.getString( 3 );
					email = rs.getString( 4 );
					firstName = rs.getString( 5 );
					lastName = rs.getString( 6 );
					address = rs.getString( 7 );
					age = rs.getInt( 8 );
					districtID = rs.getInt(9);
					
					ElectoralDistrict modelDistrict = objectLayer.createElectoralDistrict();
					modelDistrict.setId(districtID);


					nextVoter = objectLayer.createVoter( userName, password, email, firstName, lastName, address, age);
					nextVoter.setId( Long.parseLong(voterID) );
					nextVoter.setVoterId(voterID);
					PersistenceLayer pLayer = new PersistenceLayerImpl(conn, objectLayer);
					List<ElectoralDistrict> districts = pLayer.restoreElectoralDistrict(modelDistrict);
					
					nextVoter.setElectoralDistrict(districts.get(0));
					voters.add( nextVoter );

				}

				return voters;
			}
		}
		catch( Exception e ) {      // just in case...
			throw new EVException( "VoterManager.restore: Could not restore persistent Voter object; Root cause: " + e );
		}

		// if we get to this point, it's an error
		throw new EVException( "VoterManager.restore: Could not restore persistent Voter objects" );
	}

	public ElectoralDistrict restoreVoterBelongsToElectoralDistrict(Voter voter) throws EVException {

		String selectElectoralDistrictSql = "select d.id, d.name, " + 
				"from district d, voter v where d.id = v.districtID";
		Statement    stmt = null;
		StringBuffer query = new StringBuffer( 100 );
		StringBuffer condition = new StringBuffer( 100 );
		ElectoralDistrict newDistrict = null;

		condition.setLength( 0 );

		// form the query based on the given District object instance
		query.append( selectElectoralDistrictSql );

		if( voter != null ) {
			if( voter.getId() >= 0 ) // id is unique, so it is sufficient to get a district
				query.append( " and v.id = " + voter.getId() );
			else if( voter.getUserName() != null ) // userName is unique, so it is sufficient to get a person
				query.append( " and v.username = '" + voter.getUserName() + "'" );
			else {

				if( voter.getPassword() != null )
					condition.append( " and v.userPass = '" + voter.getPassword() + "'" );   

				if( voter.getEmailAddress() != null ) {
					condition.append( " and v.email = '" + voter.getEmailAddress() + "'" );
				}
				if( voter.getFirstName() != null ) {
					condition.append( " and v.firstname = '" + voter.getFirstName() + "'" );
				}
				if( voter.getLastName() != null ) {
					condition.append( " and v.lastname = '" + voter.getLastName() + "'" );
				}
				if( voter.getEmailAddress() != null ) {
					condition.append( " and v.address = '" + voter.getAddress() + "'" );
				}

				if( voter.getAge() != 0 ) {
					condition.append( " and v.age = '" + voter.getAge() + "'" );
				}

				if( condition.length() > 0 ) {
					query.append( condition );
				}
			}
		}


		try {

			stmt = conn.createStatement();

			// retrieve the Ballot object
			//
			if( stmt.execute( query.toString() ) ) { // statement returned a result

				Long   id;
				String name;

				ResultSet rs = stmt.getResultSet();

				//get it using the ballot type, if it is an election then we make an election and add it to the list.
				//if it is an issue, then we make an issue; thus we will need to include a SQL query to grab the type. 
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
			throw new EVException( "VoterManager.restoreVoterBelongsToElectoralDistrict: Could not restore persistent District objects; Root cause: " + e );
		}
	}


	public void delete( Voter voter ) 
			throws EVException
	{
		String               deleteVoterSql = "delete from voter where id = ?";              
		PreparedStatement    stmt = null;
		int                  inscnt;

		// form the query based on the given Voter object instance
		if( !voter.isPersistent() ) // is the Voter object persistent?  If not, nothing to actually delete
			return;

		try {

			//DELETE t1, t2 FROM t1, t2 WHERE t1.id = t2.id;
			//DELETE FROM t1, t2 USING t1, t2 WHERE t1.id = t2.id;
			stmt = (PreparedStatement) conn.prepareStatement( deleteVoterSql );

			stmt.setLong( 1, voter.getId() );
			System.out.println(stmt.toString());
			inscnt = stmt.executeUpdate();

			if( inscnt == 0 ) {
				throw new EVException( "VoterManager.delete: failed to delete this Voter" );
			}
		}
		catch( SQLException e ) {
			throw new EVException( "VoterManager.delete: failed to delete this Voter: " + e.getMessage() );
		}
	}
}
