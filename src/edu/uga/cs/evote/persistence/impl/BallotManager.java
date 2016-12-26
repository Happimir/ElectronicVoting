package edu.uga.cs.evote.persistence.impl;

import java.sql.Connection;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.PreparedStatement;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Ballot;
import edu.uga.cs.evote.entity.BallotItem;
import edu.uga.cs.evote.entity.Election;
import edu.uga.cs.evote.entity.ElectoralDistrict;
import edu.uga.cs.evote.entity.Issue;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.persistence.PersistenceLayer;

public class BallotManager {
	private ObjectLayer objectLayer = null;
	private Connection  conn = null;

	public BallotManager( Connection conn, ObjectLayer objectLayer )
	{
		this.conn = conn;
		this.objectLayer = objectLayer;

	}

	public void store( Ballot ballot ) 
			throws EVException
	{
		String               insertBallotSql = "insert into ballot ( startDate, endDate, approved, open, districtID ) values ( ?, ?, ?, ?, ?)";              
		String               updateBallotSql = "update ballot  set startDate = ?, endDate = ?, approved = ?, open = ?, districtID = ? where id = ?";              
		PreparedStatement    stmt;
		int                  inscnt;
		long                 ballotId;

		try {

			if( !ballot.isPersistent() )
				stmt = (PreparedStatement) conn.prepareStatement( insertBallotSql );
			else
				stmt = (PreparedStatement) conn.prepareStatement( updateBallotSql );

			if( ballot.getOpenDate()!= null ){ 
				java.util.Date jDate = ballot.getOpenDate();
				java.sql.Date sqlStartDate = new java.sql.Date(jDate.getTime());
				stmt.setDate( 1, sqlStartDate );
			}
			else
				throw new EVException( "BallotManager.save: can't save a Ballot: openDate undefined");
			if( ballot.getCloseDate()!= null ) {
				java.util.Date jDate = ballot.getCloseDate();
				java.sql.Date sqlCloseDate = new java.sql.Date(jDate.getTime());
				stmt.setDate( 2, sqlCloseDate );
			}
			else 
				throw new EVException("BallotManager.save: can't save a Ballot: openDate undefined");
			stmt.setBoolean( 3, ballot.getApproved() );
			stmt.setBoolean( 4, ballot.getOpen() );
			if( ballot.getElectoralDistrict() != null)
				stmt.setInt(5, (int)ballot.getElectoralDistrict().getId());
			else 
				throw new EVException( "BallotManager.save: can't save a Ballot: Electoral District undefined" );


			if( ballot.isPersistent() )
				stmt.setLong( 6, ballot.getId() );

			inscnt = stmt.executeUpdate();

			if( !ballot.isPersistent() ) {
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
							ballotId = r.getLong( 1 );
							if( ballotId > 0 )
								ballot.setId( ballotId ); // set this ballot's db id (proxy object)
						}
					}
				}
			}
			else {
				if( inscnt < 1 )
					throw new EVException( "BallotManager.save: failed to save a Ballot" ); 
			}
		}
		catch( SQLException e ) {
			e.printStackTrace();
			throw new EVException( "BallotManager.save: failed to save a Ballot: " + e );
		}
	}

	public List<Ballot> restore( Ballot modelBallot ) 
			throws EVException
	{

		String       selectBallotSql = "select id, startDate, endDate, approved, open, districtID from ballot";
		Statement    stmt = null;
		StringBuffer query = new StringBuffer( 100 );
		StringBuffer condition = new StringBuffer( 100 );
		List<Ballot> ballots = new ArrayList<Ballot>();

		condition.setLength( 0 );

		// form the query based on the given Ballot object instance
		query.append( selectBallotSql );

		if( modelBallot != null ) {
			if( modelBallot.getId() >= 0 ) // id is unique, so it is sufficient to get a ballot
				condition.append( " where id = " + modelBallot.getId() );
			if( modelBallot.getOpenDate() != null ) {
				if( condition.length() > 0 )
					condition.append( " and" );
				else
					condition.append( " where");
				condition.append( " startDate = '" + modelBallot.getOpenDate() + "'" );
			}
			if( modelBallot.getCloseDate() != null ) {
				if( condition.length() > 0 )
					condition.append( " and" );
				else
					condition.append( " where");
				condition.append( " endDate = '" + modelBallot.getCloseDate() + "'" );
			}
			if( modelBallot.getElectoralDistrict() != null ) {
				if( condition.length() > 0 )
					condition.append( " and" );
				else
					condition.append( " where");
				condition.append( " districtID = '" + modelBallot.getElectoralDistrict().getId() + "'" );
			}
			if( modelBallot.getOpen() != false ) {
				if( condition.length() > 0 )
					condition.append( " and" );
				else
					condition.append( " where");
				condition.append( " open = '" + modelBallot.getOpen() + "'" );
			}
			if(modelBallot.getApproved() != false){
				if( condition.length() > 0 )
					condition.append( " and" );
				else
					condition.append( " where");
				condition.append( " approved = '" + modelBallot.getApproved() + "'" );
			}


			query.append(condition);

		}

		try {
			stmt = conn.createStatement();

			// retrieve the persistent Ballot objects
			//
			if( stmt.execute( query.toString() ) ) { // statement returned a result
				ResultSet rs = stmt.getResultSet();
				long   id;
				Date startDate;
				Date endDate;  
				boolean approved;
				boolean open;
				int districtID;
				while( rs.next() ) {

					id = rs.getLong( 1 );
					startDate = rs.getDate( 2 );
					endDate = rs.getDate( 3 );
					approved = rs.getBoolean( 4 );
					open = rs.getBoolean( 5 );
					districtID = rs.getInt(6);


					Ballot ballot = objectLayer.createBallot();
					ballot.setId( id );
					ballot.setOpenDate(startDate);
					ballot.setCloseDate(endDate);
					ballot.setApproved(approved);
					ballot.setOpen(open);
					ElectoralDistrict modelDistrict = objectLayer.createElectoralDistrict();
					modelDistrict.setId(districtID);

					PersistenceLayer pLayer = new PersistenceLayerImpl(conn, objectLayer);
					List<ElectoralDistrict> districts = pLayer.restoreElectoralDistrict(modelDistrict);

					ballot.setElectoralDistrict(districts.get(0));

					ballots.add( ballot );

				}
				return ballots;
			}
		}
		catch( SQLException e ) {      // just in case...
			throw new EVException( "BallotManager.restore: Could not restore persistent Ballot object; Root cause: " + e );
		}

		// if we get to this point, it's an error
		throw new EVException( "BallotManager.restore: Could not restore persistent Ballot objects" );
	}

	public void storeBallotIncludesBallotItem(Ballot ballot, BallotItem ballotItem) throws EVException {


		PreparedStatement    stmt = null;
		int                  inscnt;
		long                 ballotId;


		try {
			if(ballotItem instanceof Election) {
				String updateSqlElection = "UPDATE election set name = ?, isPartisan = ?, voteCount = ?, ballotID = ? where id = ?";
				stmt = (PreparedStatement) conn.prepareStatement( updateSqlElection );

				if(((Election) ballotItem).getOffice() != null) {
					stmt.setString(1, ((Election) ballotItem).getOffice());
				}else {
					throw new EVException("Election office name is null");
				}
				if(((Election) ballotItem).getIsPartisan() != false) {
					stmt.setBoolean(2, true);
				} else {
					stmt.setBoolean(2, false);
				}
				if(ballotItem.getVoteCount() != 0) {
					stmt.setInt(3, ballotItem.getVoteCount());
				} else {
					stmt.setInt(3,0);
				}

				stmt.setInt(4, (int)ballot.getId());
				stmt.setInt(5, (int) ballotItem.getId());
			}
			if(ballotItem instanceof Issue) {
				String updateSqlIssue = "UPDATE issue set question = ?, voteCount = ?, yesCount = ?, noCount = ?, ballotID = ? where id = ? ";
				stmt = (PreparedStatement) conn.prepareStatement( updateSqlIssue );

				if(((Issue) ballotItem).getQuestion() != null) {
					stmt.setString(1, ((Issue) ballotItem).getQuestion());
				} else {
					throw new EVException("Issue question is null");
				} 
				if(ballotItem.getVoteCount() != 0) {
					stmt.setInt(2, ballotItem.getVoteCount());
				} else {
					stmt.setInt(2, 0);
				} 
				if(((Issue) ballotItem).getYesCount() != 0) {
					stmt.setInt(3, ((Issue) ballotItem).getYesCount());
				} else {
					stmt.setInt(3, 0);
				} 
				if(((Issue) ballotItem).getNoCount() != 0) {
					stmt.setInt(4, ((Issue) ballotItem).getNoCount());
				} else {
					stmt.setInt(4, 0);
				}

				stmt.setInt(5, (int)ballot.getId());
				stmt.setInt(6, (int)ballotItem.getId());
			}


			inscnt = stmt.executeUpdate();	
		} catch (SQLException e) {
			e.printStackTrace();
			throw new EVException( "ballotManager StoreBallotIncludesBallotItem: failed to store ballot + ballotItem: " + e );
		}
	}

	public List<BallotItem> restoreBallotIncludesBallotItem( Ballot ballot ) 
			throws EVException
	{
		String selectPersonSql = "select issue.id, issue.question, issue.voteCount, issue.yesCount, issue.bItemID," +
				" bi.id, bi.voteCount, bi.ballotID, " +
				" b.id, b.startDate, b.endDate, b.approved, b.open, b.districtID" +
				" from ballot b, ballotItem bi, issue issue where bi.ballotID = b.id AND issue.bItemID = bi.id";
		Statement    stmt = null;
		StringBuffer query = new StringBuffer( 100 );
		StringBuffer condition = new StringBuffer( 100 );
		List<BallotItem>   ballotItems = new ArrayList<BallotItem>();

		condition.setLength( 0 );

		// form the query based on the given Ballot object instance
		query.append( selectPersonSql );

		if( ballot != null ) {
			if( ballot.getId() >= 0 ) // id is unique, so it is sufficient to get a ballot
				query.append( " and b.id = " + ballot.getId() );
			else {
				if( ballot.getOpenDate() != null ) 
					condition.append( " b.startDate = '" + ballot.getOpenDate() + "'" );
				if( ballot.getCloseDate() != null && condition.length() == 0) 
					condition.append( " b.endDate = '" + ballot.getCloseDate() + "'" );
				else
					condition.append( " AND b.endDate = '" + ballot.getCloseDate() + "'" );
				if( ballot.getElectoralDistrict() != null && condition.length() == 0) 
					condition.append( " b.districtID = '" + ballot.getElectoralDistrict().getId() + "'" );
				else
					condition.append( " AND b.districtID = '" + ballot.getElectoralDistrict().getId() + "'" );
				if(condition.length() > 0){
					query.append(" where ");
					query.append(condition);
				}


			}
		}

		try {

			stmt = conn.createStatement();

			// retrieve the persistent Ballot objects
			//

			if( stmt.execute( query.toString() ) ) { // statement returned a result

				long   id;
				String question;
				int voteCount;     
				int yesCount;

				ResultSet rs = stmt.getResultSet();

				while( rs.next() ) {
					//get it using the ballot type, if it is an election then we make an election and add it to the list.
					//if it is an issue, then we make an issue; thus we will need to include a SQL query to grab the type. 
					//add integer position for our issue and election table to represent order. 
					id = rs.getLong( 1 );
					question = rs.getString( 2 );
					voteCount = rs.getInt( 3 );
					yesCount = rs.getInt( 4 );

					Issue newIssue = objectLayer.createIssue();

					newIssue.setId(id);
					newIssue.setQuestion(question);
					newIssue.setVoteCount(voteCount);
					newIssue.setYesCount(yesCount);

					// set this to null for the "lazy" association traversal
					newIssue.setBallot(null);
					ballotItems.add( newIssue );
				}
			}

		}
		catch( Exception e ) {      // just in case...
			throw new EVException( "BallotManager.restoreBallotIncludes: Could not restore persistent BallotItem objects; Root cause: " + e );
		}

		selectPersonSql = "select e.id, e.name, e.isPartisan, e.voteCount, e.bItemID," +
				" bi.id, bi.voteCount, bi.ballotID, " +
				" b.id, b.startDate, b.endDate, b.approved, b.open, b.districtID" +
				" from ballot b, ballotItem bi, election e where bi.ballotID = b.id AND e.bItemID = bi.id";
		stmt = null;
		query = new StringBuffer( 100 );
		condition = new StringBuffer( 100 );


		condition.setLength( 0 );

		// 	form the query based on the given Ballot object instance
		query.append( selectPersonSql );

		if( ballot != null ) {
			if( ballot.getId() >= 0 ) // id is unique, so it is sufficient to get a ballot
				query.append( " and b.id = " + ballot.getId() );
			else {
				if( ballot.getOpenDate() != null ) 
					condition.append( " b.startDate = '" + ballot.getOpenDate() + "'" );
				if( ballot.getCloseDate() != null && condition.length() == 0) 
					condition.append( " b.endDate = '" + ballot.getCloseDate() + "'" );
				else
					condition.append( " AND b.endDate = '" + ballot.getCloseDate() + "'" );
				if( ballot.getElectoralDistrict() != null && condition.length() == 0) 
					condition.append( " b.districtID = '" + ballot.getElectoralDistrict().getId() + "'" );
				else
					condition.append( " AND b.districtID = '" + ballot.getElectoralDistrict().getId() + "'" );
				if(condition.length() > 0){
					query.append(" where ");
					query.append(condition);
				}
			}
		}

		try {

			stmt = conn.createStatement();

			// retrieve the persistent Ballot objects
			//
			if( stmt.execute( query.toString() ) ) { // statement returned a result

				Election newElection = null;
				long   id;
				String name;
				boolean isPartisan;
				int voteCount;     

				ResultSet rs = stmt.getResultSet();

				while( rs.next() ) {
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
					ballotItems.add( newElection );
				}
				return ballotItems;
			}
		}
		catch( Exception e ) {      // just in case...
			throw new EVException( "BallotManager.restoreBallotIncludes: Could not restore persistent BallotItem objects; Root cause: " + e );
		}

		throw new EVException( "BallotManager.restoreBallotIncludes: Could not restore persistent BallotItem objects" );
	}

	public Ballot restoreBallotIncludesBallotItem(BallotItem ballotItem) throws EVException {

		String selectBallotSql = "select b.id, b.startDate, b.endDate, b.approved, b.districtID, " + 
				"bi.id, bi.voteCount, bi.ballotID" +
				"from ballotItem bi, ballot b where bi.ballotID = b.id";
		Statement    stmt = null;
		StringBuffer query = new StringBuffer( 100 );
		StringBuffer condition = new StringBuffer( 100 );
		Ballot newBallot = null;

		condition.setLength( 0 );

		// form the query based on the given Ballot object instance
		query.append( selectBallotSql );

		if( ballotItem.getId() >= 0 ) // id is unique, so it is sufficient to get a ballot
			query.append( " and bi.id = " + ballotItem.getId() );
		else {
			if( ballotItem.getVoteCount() !=  0 && condition.length() == 0 ) 
				condition.append( " bi.voteCount = '" + ballotItem.getVoteCount() + "'" );
			else
				condition.append( " AND bi.voteCount = '" + ballotItem.getVoteCount() + "'" );
			if(condition.length() > 0){
				query.append(" where ");
				query.append(condition);
			}
		}


		try {

			stmt = conn.createStatement();

			// retrieve the Ballot object
			//
			if( stmt.execute( query.toString() ) ) { // statement returned a result

				long   id;
				Date startDate;
				Date endDate;
				boolean approved;  

				ResultSet rs = stmt.getResultSet();

				//get it using the ballot type, if it is an election then we make an election and add it to the list.
				//if it is an issue, then we make an issue; thus we will need to include a SQL query to grab the type. 
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

			}
			return newBallot;
		}
		catch( Exception e ) {      // just in case...
			throw new EVException( "BallotManager.restoreBallotIncludes: Could not restore persistent BallotItem objects; Root cause: " + e );
		}
	}


	public void delete( Ballot ballot ) 
			throws EVException
	{
		String               deleteBallotSql = "delete from ballot where id = ?";              
		PreparedStatement    stmt = null;
		int                  inscnt;

		// form the query based on the given Ballot object instance
		if( !ballot.isPersistent() ) // is the Ballot object persistent?  If not, nothing to actually delete
			return;

		try {

			//DELETE t1, t2 FROM t1, t2 WHERE t1.id = t2.id;
			//DELETE FROM t1, t2 USING t1, t2 WHERE t1.id = t2.id;
			stmt = (PreparedStatement) conn.prepareStatement( deleteBallotSql );

			stmt.setLong( 1, ballot.getId() );

			inscnt = stmt.executeUpdate();

			if( inscnt == 0 ) {
				throw new EVException( "BallotManager.delete: failed to delete this Ballot" );
			}
		}
		catch( SQLException e ) {
			throw new EVException( "BallotManager.delete: failed to delete this Ballot: " + e.getMessage() );
		}
	}

	public int restoreBallotItemID(BallotItem ballotItem) throws EVException {
		String selectBallotSql = "select bi.id" +
				" from ballotItem bi";
		Statement    stmt = null;
		StringBuffer query = new StringBuffer( 100 );
		StringBuffer condition = new StringBuffer( 100 );
		long   id = -1;
		condition.setLength( 0 );

		// form the query based on the given Ballot object instance
		query.append( selectBallotSql );

		if( ballotItem.getId() >= 0 ) // id is unique, so it is sufficient to get a ballot
			query.append( " where bi.id = " + ballotItem.getId() );
		else {
			if( ballotItem.getVoteCount() !=  0 && condition.length() == 0 ) 
				condition.append( " bi.voteCount = '" + ballotItem.getVoteCount() + "'" );
			else
				condition.append( " AND bi.voteCount = '" + ballotItem.getVoteCount() + "'" );
			if(condition.length() > 0){
				query.append(" where ");
				query.append(condition);
			}
		}


		try {

			stmt = conn.createStatement();

			// retrieve the Ballot object
			//
			if( stmt.execute( query.toString() ) ) { // statement returned a result

				Date startDate;
				Date endDate;
				boolean approved;  

				ResultSet rs = stmt.getResultSet();

				//get it using the ballot type, if it is an election then we make an election and add it to the list.
				//if it is an issue, then we make an issue; thus we will need to include a SQL query to grab the type. 
				//add integer position for our issue and election table to represent order. 
				while(rs.next()){
					id = rs.getLong( 1 );
				}

			}
			return (int)id;
		}
		catch( Exception e ) {      // just in case...
			throw new EVException( "BallotManager.restoreBallotIncludes: Could not restore persistent BallotItem objects; Root cause: " + e );
		}
	}

}
