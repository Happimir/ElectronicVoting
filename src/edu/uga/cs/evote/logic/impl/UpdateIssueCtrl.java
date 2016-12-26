package edu.uga.cs.evote.logic.impl;

import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Ballot;
import edu.uga.cs.evote.entity.Issue;
import edu.uga.cs.evote.object.ObjectLayer;

public class UpdateIssueCtrl {
	ObjectLayer objectLayer = null;
	public UpdateIssueCtrl(ObjectLayer layer) {
		this.objectLayer = layer;
	}


	public long updateIssue( String old_issue, String new_issue)
	throws EVException
	{ 
		Issue issue = null;
		Issue modelIssue = null;
		List<Issue> issues = null;
		
		Issue issue_check = null;
		Issue modelIssue_check = null;
		List<Issue> issues_check = null;
		
		Ballot nBallot = null;
		Ballot modelBallot = null;
		List<Ballot> ballots = null;
		
		// check if the userName already exists
		modelIssue = objectLayer.createIssue();
		modelIssue.setQuestion(old_issue);
		issues = objectLayer.findIssue(modelIssue);
		if( issues.size() > 0 )
		issue = issues.get( 0 );
		
		// check if the issue actually exists, and if so, throw an exception
		if( issue == null )
		throw new EVException( "issue with old name does not exist" );
		
		modelBallot = objectLayer.createBallot();
		int ballotItemID = (int) issue.getBallotItemId();
		ballots = objectLayer.findBallot(modelBallot);
		
		if(ballots.size() > 0){
			nBallot = ballots.get(0);
		}
		
		if(nBallot == null){
			throw new EVException("Ballot does not exist");
		}
		
		modelIssue_check = objectLayer.createIssue();
		modelIssue_check.setQuestion(new_issue);
		issues_check = objectLayer.findIssue(modelIssue_check);
		if( issues_check.size() > 0 )
		issue_check = issues_check.get( 0 );
		
		// check if the question actually exists, and if so, throw an exception
		if( issue_check != null )
		throw new EVException( "issue with new name already exists in database" );
		
		issue.setQuestion( new_issue );
		issue.setBallot(nBallot);
		objectLayer.storeIssue( issue );
		
		return issue.getId();
	}

}
