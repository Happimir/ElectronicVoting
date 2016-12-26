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
import edu.uga.cs.evote.entity.Election;

import edu.uga.cs.evote.entity.PoliticalParty;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.persistence.PersistenceLayer;
import edu.uga.cs.evote.entity.PoliticalParty;

public class CandidateManager {
	private Connection  conn = null;
	private ObjectLayer objectLayer;

	public CandidateManager( Connection conn, ObjectLayer objectLayer )
	{
		this.conn = conn;        
		this.objectLayer = objectLayer;
	}

	public void store( Candidate candidate ) 
			throws EVException
	{
		String               insertElectionSql = "insert into candidate ( name, voteCount, electionID, partyID ) values ( ?, ?, ?, ?)";              
		String               updateElectionSql = "update candidate  set name = ?, voteCount = ?, electionID = ?, partyID = ? where id = ?";              
		PreparedStatement    stmt;
		int                  inscnt;
		long                 candidateId;

		try {

			if( !candidate.isPersistent() )
				stmt = (PreparedStatement) conn.prepareStatement( insertElectionSql );
			else
				stmt = (PreparedStatement) conn.prepareStatement( updateElectionSql );

			if( candidate.getName() != null ) 
				stmt.setString( 1, candidate.getName() );
			else 
				throw new EVException( "CandidateManager.save: can't save a candidate: name undefined" );

			if( candidate.getVoteCount() >= 0 ) 
				stmt.setInt( 2, candidate.getVoteCount() );
			else 
				throw new EVException( "CandidateManager.save: can't save a candidate: votecount undefined" );

			if( candidate.getElection()!= null ) 
				stmt.setInt( 3, (int )candidate.getElection().getId() );
			else 
				throw new EVException( "CandidateManager.save: can't save a candidate: electionID undefined" );

			if( candidate.getPoliticalParty()!= null ) 
				stmt.setInt( 4, (int )candidate.getPoliticalParty().getId() );
			else 
				stmt.setNull(4, java.sql.Types.INTEGER);

			if( candidate.isPersistent() )
				stmt.setLong( 5, candidate.getId() );
			
			
			inscnt = stmt.executeUpdate();

			if( !candidate.isPersistent() ) {
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
							candidateId = r.getLong( 1 );
							if( candidateId > 0 )
								candidate.setId( candidateId ); // set this candidate's db id (proxy object)
						}
					}
				}
			}
			else {
				if( inscnt < 1 )
					throw new EVException( "CandidateManager.save: failed to save a Candidate" ); 
			}
		}
		catch( SQLException e ) {
			e.printStackTrace();
			throw new EVException( "CandidateManager.save: failed to save a Candidate: " + e );
		}
	}

	public void storeCandidateIsCandidateInElection(Candidate candidate, Election election) throws EVException {

		PreparedStatement stmt = null;
		int 			  insct;

		String 			  updateCandidate = "update candidate set name = ?, voteCount = ?, electionID = ?, partyID = ? where id = ? ";

		try {
			if(candidate.isPersistent() && election.isPersistent()) {
				stmt = (PreparedStatement) conn.prepareStatement(updateCandidate);

				if(candidate.getName() != null) {
					stmt.setString(1, candidate.getName());
				} else {
					throw new EVException ("Candidate name is null, should not be");
				}

				if(candidate.getVoteCount() != 0) {
					stmt.setInt(2, candidate.getVoteCount());
				} else {
					stmt.setInt(2, 0);
				}

				//Setting the corresponding IDs now, I believe that are persistent already so no need for checks. 
				stmt.setInt(3, (int) election.getId());
				stmt.setInt(4, (int) candidate.getPoliticalParty().getId());
				stmt.setInt(5, (int) candidate.getId());

				insct = stmt.executeUpdate();
			} else {
				throw new EVException("Either election or candidate is not persistent: storeCandidateIsInElection");
			}
		} catch (SQLException e) {

		}

	}

	public void storeCandidateIsMememberOfPoliticalParty(Candidate candidate, PoliticalParty politicalParty) throws EVException {
		PreparedStatement stmt = null;
		int 			  insct;

		String 			  updateCandidate = "update candidate set name = ?, voteCount = ?, electionID = ?, partyID = ? where id = ? ";

		try {
			if(candidate.isPersistent() && politicalParty.isPersistent()) {
				stmt = (PreparedStatement) conn.prepareStatement(updateCandidate);

				if(candidate.getName() != null) {
					stmt.setString(1, candidate.getName());
				} else {
					throw new EVException ("Candidate name is null, should not be");
				}

				if(candidate.getVoteCount() != 0) {
					stmt.setInt(2, candidate.getVoteCount());
				} else {
					stmt.setInt(2, 0);
				}

				//Setting the corresponding IDs now, I believe that are persistent already so no need for checks. 
				stmt.setInt(3, (int) candidate.getElection().getId());
				stmt.setInt(4, (int) politicalParty.getId());
				stmt.setInt(5, (int) candidate.getId());

				insct = stmt.executeUpdate();
			} else {
				throw new EVException("either candidate or political party is not persistent: storeCandidaeMemberOfPoliticalParty");
			}
		} catch (SQLException e) {

		}

	}
	public List<Candidate> restore( Candidate modelCandidate ) 
			throws EVException
	{
		String       selectElectionSql = "select id, name, voteCount, electionID, partyID from candidate";
		Statement    stmt = null;
		StringBuffer query = new StringBuffer( 100 );
		StringBuffer condition = new StringBuffer( 100 );
		List<Candidate> candidates = new ArrayList<Candidate>();

		condition.setLength( 0 );

		// form the query based on the given Election object instance
		query.append( selectElectionSql );
		if( modelCandidate != null ) {

			if( modelCandidate.getId() >= 0 ) // id is unique, so it is sufficient to get a candidate
				condition.append( " where id = " + modelCandidate.getId() );
			if( modelCandidate.getName() != "" ){ // name is unique, so it is sufficient to get a candidate
				if( condition.length() > 0 )
					condition.append( " and" );
				else
					condition.append( " where");
				condition.append( " name = '" + modelCandidate.getName() + "'" );
			}

			if( modelCandidate.getVoteCount() != 0 ){
				if( condition.length() > 0 )
					condition.append( " and" );
				else
					condition.append( " where");
				condition.append( " voteCount = '"+modelCandidate.getVoteCount()+ "'");
			}

			if( modelCandidate.getElection() != null ){
				if( condition.length() > 0 )
					condition.append( " and" );
				else
					condition.append( " where");
				condition.append( " electionID = '"+modelCandidate.getElection().getId()+ "'");
			}

			if( modelCandidate.getPoliticalParty() != null ){
				if( condition.length() > 0 )
					condition.append( " and" );
				else
					condition.append( " where");
				condition.append( " partyID = '"+modelCandidate.getPoliticalParty().getId()+ "'");
			}


			query.append(condition);
		}

		try {

			stmt = conn.createStatement();
			System.out.println("Candidate Restore: " + query.toString());
			// retrieve the persistent Election objects
			//
			if( stmt.execute( query.toString() ) ) { // statement returned a result
				ResultSet rs = stmt.getResultSet();
				long   id;
				String name;
				int voteCount;
				int electionID;
				int partyID;

				while( rs.next() ) {

					id = rs.getLong( 1 );
					name = rs.getString( 2 );
					voteCount = rs.getInt( 3 );
					electionID = rs.getInt( 4 );
					partyID = rs.getInt( 5 );

					Candidate candidate = objectLayer.createCandidate(name, null, null);
					candidate.setId( id );
					//candidate.setName(name);
					candidate.setVoteCount(voteCount);

					PersistenceLayer pLayer = new PersistenceLayerImpl(conn, objectLayer);
					Election modelElection = objectLayer.createElection();
					modelElection.setId(electionID);
					Election findElection = null;
					findElection = pLayer.restoreElection(modelElection).get(0);


					PoliticalParty modelParty = objectLayer.createPoliticalParty();
					modelParty.setId(partyID);
					PoliticalParty findParty = null;
					findParty = pLayer.restorePoliticalParty(modelParty).get(0);



					candidate.setElection(findElection);
					candidate.setPoliticalParty(findParty);

					candidates.add( candidate );

				}

				return candidates;
			}
		}
		catch( Exception e ) {      // just in case...
			throw new EVException( "CandidateManager.restore: Could not restore persistent Candidate object; Root cause: " + e );
		}

		// if we get to this point, it's an error
		throw new EVException( "CandidateManager.restore: Could not restore persistent Election objects" );
	}
	public List<Candidate> restoreCandidateIsCandidateInElection(Election election) throws EVException {
		String selectPersonSql = "select c.id, c.name, c.voteCount, c.electionID, c.partyID," +
				"e.id, e.name, e.isPartisan, e.voteCount " +
				"from candidate c, election e where c.electionID = e.id";
		Statement    stmt = null;
		StringBuffer query = new StringBuffer( 100 );
		StringBuffer condition = new StringBuffer( 100 );
		List<Candidate>   candidateItems = new ArrayList<Candidate>();

		condition.setLength( 0 );

		// form the query based on the given Ballot object instance
		query.append( selectPersonSql );

		if( election != null ) {
			if( election.getId() >= 0 ) // id is unique, so it is sufficient to get a ballot
				query.append( " and e.id = '" + election.getId() +"'");
			if ( election.getOffice() != null)
				query.append(" and e.name = '" + election.getOffice() + "'");
			else {
				if( election.getVoteCount() !=  0 && condition.length() == 0 ) 
					condition.append( " e.voteCount = '" + election.getVoteCount() + "'" );
				else
					condition.append( " AND e.voteCount = '" + election.getVoteCount() + "'" );
				if(condition.length() > 0){
					//					query.append(" where ");
					query.append(condition);
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
				int voteCount;     
				int electionID;
				int partyID;
				ResultSet rs = stmt.getResultSet();

				while( rs.next() ) {
					Candidate newCandidate = null;
					//get it using the ballot type, if it is an election then we make an election and add it to the list.
					//if it is an issue, then we make an issue; thus we will need to include a SQL query to grab the type. 
					//add integer position for our issue and election table to represent order. 
					id = rs.getLong( 1 );
					name = rs.getString( 2 );
					voteCount = rs.getInt( 3 );
					electionID = rs.getInt( 4 );
					partyID = rs.getInt( 5 );

					newCandidate = objectLayer.createCandidate(name, null, null);

					newCandidate.setId(id);
					//					newCandidate.setName(name);
					newCandidate.setVoteCount(voteCount);	

					PersistenceLayer pLayer = new PersistenceLayerImpl(conn, objectLayer);
					Election modelElection = objectLayer.createElection();
					modelElection.setId(electionID);
					Election findElection = null;
					findElection = pLayer.restoreElection(modelElection).get(0);


					PoliticalParty modelParty = objectLayer.createPoliticalParty();
					modelParty.setId(partyID);
					PoliticalParty findParty = null;
					findParty = pLayer.restorePoliticalParty(modelParty).get(0);


					newCandidate.setElection(findElection);
					newCandidate.setPoliticalParty(findParty);

					// set this to null for the "lazy" association traversal
					//					newCandidate.setElection(null);
					//					newCandidate.setPoliticalParty(null);
					candidateItems.add( newCandidate );
				}
			}
			return candidateItems;
		}
		catch( SQLException e ) {      // just in case...
			throw new EVException( "CandidateManager.restoreCandidateisCandidiateInElection: Could not restore persistent Candidate objects; Root cause: " + e );
		}

	}

	public Election restoreCandidateIsCandidateInElection(Candidate candidate) throws EVException {
		String selectPersonSql = "select e.id, e.name, e.isPartisan, e.voteCount, e.ballotID, " +
				"c.id, c.name, c.voteCount, c.electionID, c.partyID" +
				"from candidate c, election e where c.electionID = e.id";
		Statement    stmt = null;
		StringBuffer query = new StringBuffer( 100 );
		StringBuffer condition = new StringBuffer( 100 );
		Election newElection = null;

		condition.setLength( 0 );

		// form the query based on the given Ballot object instance
		query.append( selectPersonSql );

		if( candidate != null ) {
			if( candidate.getId() >= 0 ) // id is unique, so it is sufficient to get a ballot
				query.append( " and e.id = " + candidate.getId() );
			if ( candidate.getName() != null)
				query.append(" and e.name = " + candidate.getName());
			else {
				if( candidate.getVoteCount() !=  0 && condition.length() == 0 ) 
					condition.append( " e.voteCount = '" + candidate.getVoteCount() + "'" );
				else
					condition.append( " AND e.voteCount = '" + candidate.getVoteCount() + "'" );
				if(condition.length() > 0){
					query.append(" where ");
					query.append(condition);
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
				boolean isPartisan;
				int voteCount;     

				ResultSet rs = stmt.getResultSet();

				//get it using the ballot type, if it is an election then we make an election and add it to the list.
				//if it is an issue, then we make an issue; thus we will need to include a SQL query to grab the type. 
				//add integer position for our issue and election table to represent order. 
				id = rs.getLong( 1 );
				name = rs.getString( 2 );
				isPartisan = rs.getBoolean( 3 );
				voteCount = rs.getInt( 4 );

				newElection = objectLayer.createElection();

				newElection.setId(id);
				newElection.setOffice(name);
				newElection.setIsPartisan(isPartisan);
				newElection.setVoteCount(voteCount);

				// set this to null for the "lazy" association traversal
				newElection.setBallot(null);

			}
			return newElection;
		}
		catch( Exception e ) {      // just in case...
			throw new EVException( "CandidateManager.restoreCandidateisCandidiateInElection: Could not restore persistent Candidate objects; Root cause: " + e );
		}

	}

	public PoliticalParty restoreCandidateIsMemberOfPoliticalParty(Candidate candidate) throws EVException {
		String selectPersonSql = "select p.id, p.name, " +
				"c.id, c.name, c.voteCount, c.electionID, c.partyID" +
				"from candidate c, party p where c.partyID = p.id";
		Statement    stmt = null;
		StringBuffer query = new StringBuffer( 100 );
		StringBuffer condition = new StringBuffer( 100 );
		PoliticalParty newPoliticalParty = null;

		condition.setLength( 0 );

		// form the query based on the given Ballot object instance
		query.append( selectPersonSql );

		if( candidate != null ) {
			if( candidate.getId() >= 0 ) // id is unique, so it is sufficient to get a ballot
				query.append( " and e.id = " + candidate.getId() );
			if ( candidate.getName() != null)
				query.append(" and e.name = " + candidate.getName());
			else {
				if( candidate.getVoteCount() !=  0 && condition.length() == 0 ) 
					condition.append( " e.voteCount = '" + candidate.getVoteCount() + "'" );
				else
					condition.append( " AND e.voteCount = '" + candidate.getVoteCount() + "'" );
				if(condition.length() > 0){
					query.append(" where ");
					query.append(condition);
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

				//get it using the ballot type, if it is an election then we make an election and add it to the list.
				//if it is an issue, then we make an issue; thus we will need to include a SQL query to grab the type. 
				//add integer position for our issue and election table to represent order. 
				id = rs.getLong( 1 );
				name = rs.getString( 2 );

				newPoliticalParty = objectLayer.createPoliticalParty();

				newPoliticalParty.setId(id);
				newPoliticalParty.setName(name);

			}
			return newPoliticalParty;
		}
		catch( Exception e ) {      // just in case...
			throw new EVException( "CandidateManager.restoreCandidateisCandidiateInElection: Could not restore persistent Candidate objects; Root cause: " + e );
		}

	}

	public void delete( Candidate candidate ) 
			throws EVException
	{
		String               deleteElectionSql = "delete from candidate where id = ?";              
		PreparedStatement    stmt = null;
		int                  inscnt;

		// form the query based on the given Election object instance
		if( !candidate.isPersistent() ) // is the Election object persistent?  If not, nothing to actually delete
			return;

		try {

			//DELETE t1, t2 FROM t1, t2 WHERE t1.id = t2.id;
			//DELETE FROM t1, t2 USING t1, t2 WHERE t1.id = t2.id;
			stmt = (PreparedStatement) conn.prepareStatement( deleteElectionSql );

			stmt.setLong( 1, candidate.getId() );

			inscnt = stmt.executeUpdate();

			if( inscnt == 0 ) {
				throw new EVException( "CandidateManager.delete: failed to delete this Candidate" );
			}
		}
		catch( SQLException e ) {
			throw new EVException( "CandidateManager.delete: failed to delete this Candidate: " + e.getMessage() );
		}
	}

	public void deleteCandidateIsCandidateInElection(Candidate candidate, Election election) throws EVException {
		String               deleteCandidateIsCandidateInElectionSql = "delete from candidate where id = ?";              
		PreparedStatement    stmt = null;
		int                  inscnt;

		if (!candidate.isPersistent())
			return;
		if (election != candidate.getElection())
			return;

		try {
			stmt = (PreparedStatement) conn.prepareStatement( deleteCandidateIsCandidateInElectionSql );          
			stmt.setLong( 1, candidate.getId() );
			inscnt = stmt.executeUpdate();

			if( inscnt == 1 ) {
				return;
			}
			else
				throw new EVException( "CandidateManager.deleteCandidateIsCandidateInElection: failed to delete a Candidate" );
		}
		catch( SQLException e ) {
			e.printStackTrace();
			throw new EVException( "CandidateManager.deleteCandidateIsCandidateInElection: failed to delete a Candidate: " + e );        }

	}
}
