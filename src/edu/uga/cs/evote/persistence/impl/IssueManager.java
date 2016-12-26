package edu.uga.cs.evote.persistence.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.PreparedStatement;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Issue;
import edu.uga.cs.evote.entity.Ballot;
import edu.uga.cs.evote.entity.BallotItem;
import edu.uga.cs.evote.entity.Election;
import edu.uga.cs.evote.object.ObjectLayer;

public class IssueManager {
	private ObjectLayer objectLayer = null;
	private Connection  conn = null;

	public IssueManager( Connection conn, ObjectLayer objectLayer )
	{
		this.conn = conn;
		this.objectLayer = objectLayer;

	}

	public void store( Issue issue ) 
			throws EVException
	{
		String storeBallotItemSql = "insert into ballotItem ( voteCount, ballotID ) values ( ?, ?)";              
		String updateBallotItemSql = "update ballotItem  set voteCount = ?, ballotID = ? where id = ?";              
		PreparedStatement    stmt1;
		int                  inscnt1;
		long                 ballotItemId;

		try {

			if( !issue.isPersistent() )
				stmt1 = (PreparedStatement) conn.prepareStatement( storeBallotItemSql );
			else
				stmt1 = (PreparedStatement) conn.prepareStatement( updateBallotItemSql );

			if( issue.getVoteCount()!= -1 ) 
				stmt1.setInt( 1, issue.getVoteCount() );
			else 
				throw new EVException( "IssueManager.save: can't save a Issue: Question undefined" );
			if( issue.getBallot().getId()!= -1 ) 
				stmt1.setInt( 2, (int)issue.getBallot().getId() );
			else 
				throw new EVException( "IssueManager.save: can't save a Issue: BallotItemId undefined" );

			if( issue.isPersistent() )
				stmt1.setLong( 3, issue.getBallotItemId() );

			inscnt1 = stmt1.executeUpdate();

			if( !issue.isPersistent() ) {
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
							ballotItemId = r.getLong( 1 );
							if( ballotItemId > 0 )
								issue.setBallotItemId( ballotItemId ); // set this issue's db id (proxy object)
						}
					}
				}
			}
			else {
				if( inscnt1 < 1 )
					throw new EVException( "IssueManager.save: failed to save a issue" ); 
			}
		}
		catch( SQLException e ) {
			e.printStackTrace();
			throw new EVException( "IssueManager.save: failed to save a Election: " + e );
		}

		String               insertElectionSql = "insert into issue ( question, voteCount, yesCount, noCount, bItemID ) values ( ?, ?, ?, ?, ?)";              
		String               updateElectionSql = "update issue set question = ?, voteCount = ?, yesCount = ?, noCount = ?, bItemID = ? where id = ?";              
		PreparedStatement    stmt;
		int                  inscnt;
		long                 issueId;

		try {

			if( !issue.isPersistent() )
				stmt = (PreparedStatement) conn.prepareStatement( insertElectionSql );
			else
				stmt = (PreparedStatement) conn.prepareStatement( updateElectionSql );

			if( issue.getQuestion()!= null ) 
				stmt.setString( 1, issue.getQuestion() );
			else 
				throw new EVException( "IssueManager.save: can't save a Issue: Question undefined" );
			if( issue.getVoteCount()!= -1 ) 
				stmt.setInt( 2, issue.getVoteCount() );
			else 
				throw new EVException( "IssueManager.save: can't save a Issue: VoteCount undefined" );
			if( issue.getYesCount()!= -1 ) 
				stmt.setInt( 3, issue.getYesCount() );
			else 
				throw new EVException( "IssueManager.save: can't save a Issue: YesCount undefined" );
			if( issue.getNoCount()!= -1 ) 
				stmt.setInt( 4, issue.getNoCount() );
			else 
				throw new EVException( "IssueManager.save: can't save a Issue: NoCount undefined" );
			if( issue.getBallotItemId()!= -1 ) 
				stmt.setInt( 5, (int)issue.getBallotItemId() );
			else 
				throw new EVException( "IssueManager.save: can't save a Issue: NoCount undefined" );

			if( issue.isPersistent() )
				stmt.setLong( 6, issue.getId() );

			inscnt = stmt.executeUpdate();

			if( !issue.isPersistent() ) {
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
							issueId = r.getLong( 1 );
							if( issueId > 0 )
								issue.setId( issueId ); // set this issue's db id (proxy object)
						}
					}
				}
			}
			else {
				if( inscnt < 1 )
					throw new EVException( "IssueManager.save: failed to save a issue" ); 
			}
		}
		catch( SQLException e ) {
			e.printStackTrace();
			throw new EVException( "IssueManager.save: failed to save a issue: " + e );
		}
	}

	public List<Issue> restore( Issue modelIssue ) 
			throws EVException
	{
		String       selectElectionSql = "select id, question, voteCount, yesCount, noCount, bItemID from issue";
		String		 selectBallotItemSql = "select ballotID from ballotItem";
		Statement    stmt = null;
		StringBuffer query = new StringBuffer( 100 );
		StringBuffer condition = new StringBuffer( 100 );
		StringBuffer condition1 = new StringBuffer( 100 );
		List<Issue> issues = new ArrayList<Issue>();

		condition.setLength( 0 );
		condition1.setLength( 0 );

		// form the query based on the given Election object instance
		query.append( selectElectionSql );


		if( modelIssue != null ) {
			if( modelIssue.getId() >= 0 ) // id is unique, so it is sufficient to get a issue
				query.append( " where id = " + modelIssue.getId() );
			else if( modelIssue.getQuestion() != null ) // userName is unique, so it is sufficient to get a issue
				query.append( " where question = '" + modelIssue.getQuestion() + "'" ); //used to where name
			else{
				if( modelIssue.getVoteCount() != 0 )
					condition.append(" where voteCount = '" + modelIssue.getVoteCount() + "'");
				if( modelIssue.getYesCount() != 0)
					condition.append(" where yesCount = '" + modelIssue.getYesCount() + "'");
				if( modelIssue.getNoCount() != 0)
					condition.append(" where noCount ='" + modelIssue.getNoCount() + "'");
				if( modelIssue.getBallotItemId() != 0){
					condition.append(" where bItemID ='" + modelIssue.getBallotItemId() + "'");
				}
				if( condition.length() > 0){
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
				int voteCount;
				int yesCount;
				int noCount;
				int bItemId;

				while( rs.next() ) {

					Statement	 stmt1 = null;
					StringBuffer query1= new StringBuffer( 100 );
					id = rs.getLong( 1 );
					name = rs.getString( 2 );
					voteCount = rs.getInt( 3 );
					yesCount = rs.getInt( 4 );
					noCount = rs.getInt( 5 );
					bItemId = rs.getInt( 6 );


					Issue issue = objectLayer.createIssue();
					issue.setId( id );
					issue.setQuestion(name);
					issue.setVoteCount(voteCount);
					issue.setYesCount(yesCount);
					issue.setBallotItemId(bItemId);
					/*if(stmt1.execute( query1.toString() ) ){
						ResultSet rs1 = stmt1.getResultSet();
						while( rs1. next()){
							int BallotID = rs1.getInt(1);
							Ballot modelBallot = null;
							modelBallot = objectLayer.createBallot();
							modelBallot.setId(BallotID);

							List<Ballot> ballot = objectLayer.findBallot(modelBallot);
							for(int i = 0; i< ballot.size();i++){
								System.out.println(ballot.get(i).getElectoralDistrict().getName());
							}
							Ballot issueBallot = ballot.get(0);
							issue.setBallot(issueBallot);
						}
					}*/
					


					issues.add( issue );

				}

				return issues;
			}
		}
		catch( SQLException e ) {      // just in case...
			throw new EVException( "IssueManager.restore: Could not restore persistent Election object; Root cause: " + e );
		}

		// if we get to this point, it's an error
		throw new EVException( "IssueManager.restore: Could not restore persistent Election objects" );
	}


	public void delete( Issue issue ) 
			throws EVException
	{



		String               deleteIssueSql = "delete from issue where id = ?";              
		PreparedStatement    stmt = null;
		long				 bItemID = issue.getBallotItemId();

		int                  inscnt;

		// form the query based on the given Election object instance
		if( !issue.isPersistent() ) // is the Election object persistent?  If not, nothing to actually delete
			return;

		try {

			//DELETE t1, t2 FROM t1, t2 WHERE t1.id = t2.id;
			//DELETE FROM t1, t2 USING t1, t2 WHERE t1.id = t2.id;
			stmt = (PreparedStatement) conn.prepareStatement( deleteIssueSql );

			stmt.setLong( 1, issue.getId() );

			inscnt = stmt.executeUpdate();

			if( inscnt == 0 ) {
				throw new EVException( "IssueManager.delete: failed to delete this Issue" );
			}
		}
		catch( SQLException e ) {
			throw new EVException( "IssueManager.delete: failed to delete this Issue: " + e.getMessage() );
		}

		String               deleteBallotItemSql = "delete from ballotItem where id = ?";              
		PreparedStatement    stmt1 = null;
		int                  inscnt1;

		// form the query based on the given Election object instance
		if( !issue.isPersistent() ) // is the Election object persistent?  If not, nothing to actually delete
			return;

		try {

			//DELETE t1, t2 FROM t1, t2 WHERE t1.id = t2.id;
			//DELETE FROM t1, t2 USING t1, t2 WHERE t1.id = t2.id;
			stmt1 = (PreparedStatement) conn.prepareStatement( deleteBallotItemSql );

			stmt1.setLong( 1, bItemID );

			inscnt1 = stmt1.executeUpdate();

			if( inscnt1 == 0 ) {
				throw new EVException( "IssueManager.delete: failed to delete this ballotItem" );
			}
		}
		catch( SQLException e ) {
			throw new EVException( "IssueManager.delete: failed to delete this BallotItem: " + e.getMessage() );
		}
	}
}

