package edu.uga.cs.evote.logic.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Ballot;
import edu.uga.cs.evote.entity.Candidate;
import edu.uga.cs.evote.entity.Election;
import edu.uga.cs.evote.entity.Issue;
import edu.uga.cs.evote.entity.VoteRecord;
import edu.uga.cs.evote.entity.Voter;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.persistence.PersistenceLayer;
import edu.uga.cs.evote.persistence.impl.PersistenceLayerImpl;

public class UpdateVotesCtrl {
	ObjectLayer objectLayer = null;
	public UpdateVotesCtrl(ObjectLayer layer) {
		this.objectLayer = layer;
	}
	
	public long updateVotes(String username, String ballot, String bItem, String vote) throws EVException{
		
		VoteRecord voteRecord = objectLayer.createVoteRecord();
		VoteRecord checkRecord = objectLayer.createVoteRecord();
		
		Voter modelVoter = objectLayer.createVoter();
		modelVoter.setUserName(username);
		Voter voter = objectLayer.findVoter(modelVoter).get(0);
		
		Ballot modelBallot = objectLayer.createBallot();
		modelBallot.setId(Integer.parseInt(ballot.substring(6)));
		Ballot findBallot = objectLayer.findBallot(modelBallot).get(0);
		
		Election modelElection = objectLayer.createElection();
		modelElection.setOffice(bItem);
		List<Election> elections = objectLayer.findElection(modelElection);
		if(elections.size() < 1){
			Issue modelIssue = objectLayer.createIssue();
			modelIssue.setQuestion(bItem);
			List<Issue> issues = objectLayer.findIssue(modelIssue);
			if(issues.size() < 1){
				throw new EVException("No Election or Issue found with that Name");
			} else {
				Issue issue = issues.get(0);
				int yesCount = issue.getYesCount();
				int voteCount = issue.getVoteCount();
				if(vote.equalsIgnoreCase("yes")){
					yesCount++;
				}
				checkRecord.setBallotItem(issue);
				checkRecord.setVoter(voter);
				List<VoteRecord> checker = objectLayer.findVoteRecord(checkRecord);
				if(checker.size() > 0){
					return -1;
				}
				voteCount++;
				issue.setYesCount(yesCount);
				issue.setVoteCount(voteCount);
				issue.setBallot(findBallot);
				objectLayer.storeIssue(issue);
				voteRecord.setBallotItem(issue);
				
			}
			
		} else{
			Election election = elections.get(0);
			Candidate modelCandidate = objectLayer.createCandidate();
			modelCandidate.setElection(election);
			int indexOfParty = vote.indexOf('(');
			if(indexOfParty != -1){
				vote = vote.substring(0, indexOfParty).trim();
			}
			modelCandidate.setName(vote);
			Candidate candidate = objectLayer.findCandidate(modelCandidate).get(0);
			
			checkRecord.setBallotItem(election);
			checkRecord.setVoter(voter);
			List<VoteRecord> checker = objectLayer.findVoteRecord(checkRecord);
			if(checker.size() > 0){
				return -1;
			}
			
			int voteCount = candidate.getVoteCount();
			voteCount++;
			candidate.setVoteCount(voteCount);
			objectLayer.storeCandidate(candidate);
			
			int electionVoteCount = election.getVoteCount();
			System.out.println(election.getBallotItemID());
			electionVoteCount++;
			election.setVoteCount(electionVoteCount);
			election.setBallot(findBallot);
			objectLayer.storeElection(election);
			voteRecord.setBallotItem(election);
			
		}
		voteRecord.setVoter(voter);
		//DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date date = new Date();
		voteRecord.setDate(date);
		
		objectLayer.storeVoteRecord(voteRecord);
		return 1;
	}
}
