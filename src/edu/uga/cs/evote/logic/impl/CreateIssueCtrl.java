package edu.uga.cs.evote.logic.impl;

import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Ballot;
import edu.uga.cs.evote.entity.Issue;
import edu.uga.cs.evote.object.ObjectLayer;

public class CreateIssueCtrl {
	
	ObjectLayer objectLayer = null;
	public CreateIssueCtrl(ObjectLayer layer) {
		this.objectLayer = layer;
	}
	
	public long createIssue(String question, int ballotID) throws EVException{
		
		Issue issue = null;
		Issue modelIssue = null;
		List<Issue> issues = null;
		
		Ballot nBallot = null;
		Ballot modelBallot = null;
		List<Ballot> ballots = null;
		
		modelBallot = objectLayer.createBallot();
		modelBallot.setId(ballotID);
		ballots = objectLayer.findBallot(modelBallot);
		if(ballots.size() > 0){
			nBallot = ballots.get(0);
		}
		
		if(nBallot == null){
			throw new EVException("Ballot does not exist");
		}
		
		modelIssue = objectLayer.createIssue();
		modelIssue.setQuestion(question);
		issues = objectLayer.findIssue(modelIssue);
		
		if(issues.size() > 0) {
			issue = issues.get(0);
		}
		
		if(issue != null) {
			throw new EVException( "A district with this name already exists" );
		}
		//
		issue = objectLayer.createIssue(question, nBallot);
		System.out.println("Creating Issue with question: " + question);
		objectLayer.storeIssue(issue);
		
		return issue.getId();
	}
}
