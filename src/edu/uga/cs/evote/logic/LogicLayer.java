
package edu.uga.cs.evote.logic;


import java.util.Date;
import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Ballot;
import edu.uga.cs.evote.entity.Candidate;
import edu.uga.cs.evote.entity.Election;
import edu.uga.cs.evote.entity.PoliticalParty;
import edu.uga.cs.evote.entity.User;
import edu.uga.cs.evote.session.Session;




public interface LogicLayer
{
//    //public List<Club>         findAllClubs() throws EVException;
//    public List<User>         findAllUsers() throws EVException;
//    //public long               joinClub( User person, Club club ) throws EVException;
//    public long               joinClub( long personId, String clubName ) throws EVException;
    public long               createElectoralDistrict( String name) throws EVException;
//    public List<User>         findClubMembers( String clubName ) throws EVException;
    public void               logout( String ssid ) throws EVException;
    public String             login( Session session, boolean isVoter, String userName, String password ) throws EVException;
	public long createVoter(String user_name, String password, String email, String first_name, String last_name,
			String address, String district, int age) throws EVException;
	public long updateElectoralDistrict(String old_name, String new_name) throws EVException;
	
	//Added by Michael Kovalsky
	

	public long					updateElection(String old_name, String new_name, boolean old_partisan, boolean new_partisan, 
								List<Candidate> old_candidates, List<Candidate> new_candidates) throws EVException;
	public long					createCandidate(String name, String party, String election) throws EVException;
	public long 				createIssue(String issue, int ballotID) throws EVException;
	public long					updateIssue(String old_issue, String new_issue) throws EVException;
	public long					createPoliticalParty(String name) throws EVException;
	public long					createBallot(Date startDate, Date endDate, String district_name) throws EVException;
	public long					createElection(String electionName, boolean isPart, int ballotID) throws EVException;
	public long                 updateBallot(Date new_start_date, Date new_end_date, int ballotID) throws EVException;
	public long                 updateBallot(String open, int ballotID) throws EVException;
	public long                 updateElectionsOfficer(String old_username, String first_name, String last_name, String email,
			                                                            String address) throws EVException;
	public long                 updatePassword(String userType, String username, String old_password, String new_password) throws EVException;
	public long                 updateVoter(String old_username, String first_name, String last_name, String email, String address, String district) throws EVException;
	public long 				updateVotes(String ballot, String bItem, String vote, String vote2) throws EVException;
	
}
