package edu.uga.cs.evote.persistence.impl;

import java.util.List;
import java.sql.Connection;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Ballot;
import edu.uga.cs.evote.entity.BallotItem;
import edu.uga.cs.evote.entity.Candidate;
import edu.uga.cs.evote.entity.Election;
import edu.uga.cs.evote.entity.ElectionsOfficer;
import edu.uga.cs.evote.entity.ElectoralDistrict;
import edu.uga.cs.evote.entity.Issue;
import edu.uga.cs.evote.entity.PoliticalParty;
import edu.uga.cs.evote.entity.VoteRecord;
import edu.uga.cs.evote.entity.Voter;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.persistence.PersistenceLayer;

public class PersistenceLayerImpl implements PersistenceLayer {
	private CandidateManager candidateManager = null;
	private ElectionManager electionManager = null;
	private ElectionOfficerManager electionOfficerManager = null;
	private ElectoralDistrictManager electoralDistrictManager = null;
	private PoliticalPartyManager politicalPartyManager = null;
	private VoterManager voterManager = null;
	private BallotManager ballotManager = null;
	private IssueManager issueManager = null;
	private VoteRecordManager voteRecordManager = null;

	
	public PersistenceLayerImpl( Connection conn, ObjectLayer objectLayer){
		candidateManager = new CandidateManager(conn, objectLayer);
		electionManager = new ElectionManager(conn, objectLayer);
		electionOfficerManager = new ElectionOfficerManager(conn, objectLayer);
		electoralDistrictManager = new ElectoralDistrictManager(conn, objectLayer);
		politicalPartyManager = new PoliticalPartyManager(conn, objectLayer);
		voterManager = new VoterManager(conn, objectLayer);
		ballotManager = new BallotManager(conn, objectLayer);
		issueManager = new IssueManager(conn, objectLayer);
		voteRecordManager = new VoteRecordManager(conn, objectLayer);
	}
	@Override
	public List<ElectionsOfficer> restoreElectionsOfficer(ElectionsOfficer modelElectionsOfficer) throws EVException {
		return electionOfficerManager.restore(modelElectionsOfficer);
	}

	@Override
	public void storeElectionsOfficer(ElectionsOfficer electionsOfficer) throws EVException {
		electionOfficerManager.store(electionsOfficer);
	}

	@Override
	public void deleteElectionsOfficer(ElectionsOfficer electionsOfficer) throws EVException {
		electionOfficerManager.delete(electionsOfficer);
	}

	@Override
	public List<Voter> restoreVoter(Voter modelVoter) throws EVException {
		return voterManager.restore(modelVoter);
	}

	@Override
	public void storeVoter(Voter voter) throws EVException {
		voterManager.store(voter);
	}

	@Override
	public void deleteVoter(Voter voter) throws EVException {
		voterManager.delete(voter);
	}

	@Override
	public List<Ballot> restoreBallot(Ballot modelBallot) throws EVException {
		return ballotManager.restore(modelBallot);
	}

	@Override
	public void storeBallot(Ballot ballot) throws EVException {
		ballotManager.store(ballot);
	}

	@Override
	public void deleteBallot(Ballot ballot) throws EVException {
		ballotManager.delete(ballot);
	}

	@Override
	public List<Candidate> restoreCandidate(Candidate modelCandidate) throws EVException {
		return candidateManager.restore(modelCandidate);
	}

	@Override
	public void storeCandidate(Candidate candidate) throws EVException {
		candidateManager.store(candidate);
	}

	@Override
	public void deleteCandidate(Candidate candidate) throws EVException {
		candidateManager.delete(candidate);
	}

	@Override
	public List<Election> restoreElection(Election modelElection) throws EVException {
		return electionManager.restore(modelElection);
	}

	@Override
	public void storeElection(Election election) throws EVException {
		electionManager.store(election);
	}

	@Override
	public void deleteElection(Election election) throws EVException {
		electionManager.delete(election);
	}

	@Override
	public List<ElectoralDistrict> restoreElectoralDistrict(ElectoralDistrict modelElectoralDistrict)
			throws EVException {
		return electoralDistrictManager.restore(modelElectoralDistrict);
	}

	@Override
	public void storeElectoralDistrict(ElectoralDistrict electoralDistrict) throws EVException {
		electoralDistrictManager.store(electoralDistrict);
	}

	@Override
	public void deleteElectoralDistrict(ElectoralDistrict electoralDistrict) throws EVException {
		electoralDistrictManager.delete(electoralDistrict);
	}

	@Override
	public List<Issue> restoreIssue(Issue modelIssue) throws EVException {
		return issueManager.restore(modelIssue);
	}

	@Override
	public void storeIssue(Issue issue) throws EVException {
		issueManager.store(issue);
	}

	@Override
	public void deleteIssue(Issue issue) throws EVException {
		issueManager.delete(issue);
	}

	@Override
	public List<PoliticalParty> restorePoliticalParty(PoliticalParty modelPoliticalParty) throws EVException {
		return politicalPartyManager.restore(modelPoliticalParty);
	}

	@Override
	public void storePoliticalParty(PoliticalParty politicalParty) throws EVException {
		politicalPartyManager.store(politicalParty);
	}

	@Override
	public void deletePoliticalParty(PoliticalParty politicalParty) throws EVException {
		politicalPartyManager.delete(politicalParty);
	}

	@Override
	public List<VoteRecord> restoreVoteRecord(VoteRecord modelVoteRecord) throws EVException {
		return voteRecordManager.restore(modelVoteRecord);
	}

	@Override
	public void storeVoteRecord(VoteRecord voteRecord) throws EVException {
		voteRecordManager.store(voteRecord);
	}

	@Override
	public void deleteVoteRecord(VoteRecord voteRecord) throws EVException {
		voteRecordManager.delete(voteRecord);
	}

	@Override
	public void storeBallotIncludesBallotItem(Ballot ballot, BallotItem ballotItem) throws EVException {
		ballotManager.storeBallotIncludesBallotItem(ballot, ballotItem);
	}

	@Override
	public Ballot restoreBallotIncludesBallotItem(BallotItem ballotItem) throws EVException {
		return ballotManager.restoreBallotIncludesBallotItem(ballotItem);
	}

	@Override
	public List<BallotItem> restoreBallotIncludesBallotItem(Ballot ballot) throws EVException {
		return ballotManager.restoreBallotIncludesBallotItem(ballot);
	}
	@Override
	public void deleteBallotIncludesBallotItem(Ballot ballot, BallotItem ballotItem) throws EVException {
		// TODO Auto-generated method stub
		//wasted method

	}

	@Override
	public void storeCandidateIsCandidateInElection(Candidate candidate, Election election) throws EVException {
		candidateManager.storeCandidateIsCandidateInElection(candidate, election);
	}

	@Override
	public Election restoreCandidateIsCandidateInElection(Candidate candidate) throws EVException {
		return candidateManager.restoreCandidateIsCandidateInElection(candidate);
	}

	@Override
	public List<Candidate> restoreCandidateIsCandidateInElection(Election election) throws EVException {
		return candidateManager.restoreCandidateIsCandidateInElection(election);
	}

	@Override
	public void deleteCandidateIsCandidateInElection(Candidate candidate, Election election) throws EVException {
		// TODO Auto-generated method stub

	}

	@Override
	public void storeElectoralDistrictHasBallotBallot(ElectoralDistrict electoralDistrict, Ballot ballot)
			throws EVException {
		electoralDistrictManager.storeElectoralDistrictHasBallotBallot(electoralDistrict, ballot);
	}

	@Override
	public ElectoralDistrict restoreElectoralDistrictHasBallotBallot(Ballot ballot) throws EVException {
		return electoralDistrictManager.restoreElectoralDistrictHasBallotBallot(ballot);
	}

	@Override
	public List<Ballot> restoreElectoralDistrictHasBallotBallot(ElectoralDistrict electoralDistrict)
			throws EVException {
		return electoralDistrictManager.restoreElectoralDistrictHasBallotBallot(electoralDistrict);
	}

	@Override
	public void deleteElectoralDistrictHasBallotBallot(ElectoralDistrict electoralDistrict, Ballot ballot)
			throws EVException {
		// TODO Auto-generated method stub

	}

	@Override
	public void storeCandidateIsMemberOfPoliticalParty(Candidate candidate, PoliticalParty politicalParty)
			throws EVException {
		candidateManager.storeCandidateIsMememberOfPoliticalParty(candidate, politicalParty);

	}

	@Override
	public PoliticalParty restoreCandidateIsMemberOfPoliticalParty(Candidate candidate) throws EVException {
		return candidateManager.restoreCandidateIsMemberOfPoliticalParty(candidate);
	}

	@Override
	public List<Candidate> restoreCandidateIsMemberOfPoliticalParty(PoliticalParty politicalParty) throws EVException {
		return politicalPartyManager.restoreCandidateIsMemberOfPoliticalParty(politicalParty);
	}

	@Override
	public void deleteCandidateIsMemberOfElection(Candidate candidate, PoliticalParty politicalParty)
			throws EVException {
			//wasted method
		// TODO Auto-generated method stub

	}

	@Override
	public void storeVoterBelongsToElectoralDistrict(Voter voter, ElectoralDistrict electoralDistrict)
			throws EVException {
		voterManager.storeVoterBelongsToElectoralDistrict(voter, electoralDistrict);
	}

	@Override
	public ElectoralDistrict restoreVoterBelongsToElectoralDistrict(Voter voter) throws EVException {
		return voterManager.restoreVoterBelongsToElectoralDistrict(voter);
	}

	@Override
	public List<Voter> restoreVoterBelongsToElectoralDistrict(ElectoralDistrict electoralDistrict) throws EVException {
		return electoralDistrictManager.restoreVoterBelongsToElectoralDistrict(electoralDistrict);
	}

	@Override
	public void deleteVoterBelongsToElectoralDistrict(Voter voter, ElectoralDistrict electoralDistrict) throws EVException {
		// TODO Auto-generated method stub
		//wasted method

	}
	@Override
	public int getBallotItemID(BallotItem bItem) throws EVException {
		return ballotManager.restoreBallotItemID(bItem);
	}

}
