package edu.uga.cs.evote.logic.impl;

import java.util.Date;
import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Ballot;
import edu.uga.cs.evote.entity.Candidate;
import edu.uga.cs.evote.entity.Election;
import edu.uga.cs.evote.entity.PoliticalParty;
import edu.uga.cs.evote.logic.LogicLayer;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.session.Session;
import edu.uga.cs.evote.session.SessionManager;

public class LogicLayerImpl implements LogicLayer {
	
	private ObjectLayer objectLayer = null;
	
	public LogicLayerImpl(ObjectLayer layer) {
		this.objectLayer = layer;
	}

	public void logout( String ssid ) throws EVException
    {
        SessionManager.logout( ssid );
    }

    public String login( Session session, boolean isVote, String userName, String password )
            throws EVException
    {
        LoginCtrl ctrlVerifyPerson = new LoginCtrl( objectLayer );
        return ctrlVerifyPerson.login( session, isVote, userName, password );
    }

	@Override
	public long createVoter(String user_name, String password, String email, String first_name, String last_name,
			String address, String district, int age) throws EVException {
		
		CreateVoterCtrl ctrlCreateVoter = new CreateVoterCtrl( objectLayer );
		
		return ctrlCreateVoter.createVoter( user_name, password, email, first_name,
                last_name, address, district, age );
	}

	
	@Override
	public long createElectoralDistrict(String name) throws EVException {
		
		CreateElectoralDistrictCtrl ctrlDistrict = new CreateElectoralDistrictCtrl(objectLayer);
		
		return ctrlDistrict.createDistrict(name);
	}
	public long updateElectoralDistrict(String old_name, String new_name) throws EVException {
		
		UpdateElectoralDistrictCtrl ctrlUpdateDistrict = new UpdateElectoralDistrictCtrl(objectLayer);
		
		return ctrlUpdateDistrict.updateDistrict(old_name, new_name);

	}

	//Added by Michael Kovalsky
	@Override
	public long createElection(String name, boolean isPartisan, int ballotID) throws EVException {
		CreateElectionCtrl ctrlCreateElection = new CreateElectionCtrl(objectLayer);
		return ctrlCreateElection.createElection(name, isPartisan, ballotID);
	}

	@Override
	public long updateElection(String old_name, String new_name, boolean old_partisan, boolean new_partisan,
			List<Candidate> old_candidates, List<Candidate> new_candidates) {
		return 0;
	}

	@Override
	public long createCandidate(String name, String party, String election) throws EVException {
		
		CreateCandidateCtrl createCandidateCtrl = new CreateCandidateCtrl(objectLayer);
		return createCandidateCtrl.createCandidate(name, party, election);

	}

	@Override
	public long createIssue(String issue, int ballotID) throws EVException {
		
		CreateIssueCtrl createIssueCtrl = new CreateIssueCtrl(objectLayer);
		return createIssueCtrl.createIssue(issue, ballotID);

	}

	@Override
	public long updateIssue(String old_issue, String new_issue) throws EVException {
		UpdateIssueCtrl updateIssueCtrl = new UpdateIssueCtrl(objectLayer);
		return updateIssueCtrl.updateIssue(old_issue, new_issue);
	}

	@Override
	public long createPoliticalParty(String name) throws EVException {
		CreatePartyCtrl createPartyCtrl = new CreatePartyCtrl(objectLayer);
		return createPartyCtrl.createPoliticalParty(name);
	}

	@Override
	public long createBallot(Date startDate, Date endDate, String districtName) throws EVException {
		
		CreateBallotCtrl createBallotCtrl = new CreateBallotCtrl(objectLayer);
		return createBallotCtrl.createBallot(startDate, endDate, districtName);
	}

	@Override
	public long updateBallot(Date new_start_date, Date new_end_date, int ballotID) throws EVException {
		UpdateBallotCtrl updateBallotCtrl = new UpdateBallotCtrl(objectLayer);
		return updateBallotCtrl.updateBallot(new_start_date, new_end_date, ballotID);
	}

	@Override
	public long updateBallot(String open, int ballotID) throws EVException {
		UpdateBallotCtrl updateBallotCtrl = new UpdateBallotCtrl(objectLayer);
		return updateBallotCtrl.updateBallot(open, ballotID);
	}

	@Override
	public long updateElectionsOfficer(String old_username, String first_name, String last_name, String email,
			String address) throws EVException {
		UpdateElectionsOfficerCtrl updateElectionsOfficerCtrl = new UpdateElectionsOfficerCtrl(objectLayer);
		return updateElectionsOfficerCtrl.updateElectionsOfficer(old_username, first_name, last_name, email, address);
	}

	@Override
	public long updatePassword(String userType, String username, String old_password, String new_password) throws EVException {
		UpdatePasswordCtrl updatePasswordCtrl = new UpdatePasswordCtrl(objectLayer);
		return updatePasswordCtrl.updatePassword(userType, username, old_password, new_password);
	}

	@Override
	public long updateVoter(String old_username, String first_name, String last_name, String email, String address, String district) throws EVException {
		UpdateVoterCtrl updateVoterCtrl = new UpdateVoterCtrl(objectLayer);
		return updateVoterCtrl.updateVoter(old_username, first_name, last_name, email, address, district);
	}

	@Override
	public long updateVotes(String username, String ballot, String bItem, String vote) throws EVException {
		UpdateVotesCtrl updateVotesCtrl = new UpdateVotesCtrl(objectLayer);
		return updateVotesCtrl.updateVotes(username, ballot, bItem, vote);
	}
	
	



}
