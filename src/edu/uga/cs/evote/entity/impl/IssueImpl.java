package edu.uga.cs.evote.entity.impl;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Ballot;
import edu.uga.cs.evote.entity.Issue;

/**
 * @author michaelkovalsky
 * Date: on 10/23/16.
 */
public class IssueImpl extends BallotItemImpl implements Issue {

    private String question;
    private int yesCount;
    private long ballotItemID;

    /**
     * Default Constructor
     */
    public IssueImpl() {
    	super();
    	this.question = "";
    	this.yesCount = 0;
    }
    
    public IssueImpl(String question, Ballot ballot) {
    	super(0, ballot);
    	this.question = question;
    	this.yesCount = 0;
    }
    
    /** Return the question of this issue.
     * @return the question of this issue
     */
    public String getQuestion() {
        return this.question;
    }

    /** Set the new question of this issue.
     * @param question the new question of this issue
     */
    public void setQuestion( String question ) {
        this.question = question;
    }

    /** Return the number of yes votes for this issue.
     * @return the number of yes votes for this issue
     */
    public int getYesCount() {
        return this.yesCount;
    }

    /** Set the new number of yes votes for this issue.
     * @param yesCount the new number of yes votes for this issue
     * @throws EVException in case the new yes vote count is negative
     */
    public void setYesCount( int yesCount ) throws EVException {
        this.yesCount = yesCount;
    }

    /** Return the number of no votes for this issue.
     * @return the number of no votes for this issue
     */
    public int getNoCount() {
        return getVoteCount() - this.yesCount;
    }

    // noCount is a derived attribute, so it is read-only
    //public void setNoCount( int noCount );

    /** Add one YES vote (increment by one) to the yes votes cast for this Issue.
     */
    public void addYesVote() {
        this.yesCount++;
        addVote();
    }

    /** Add one NO vote (increment by one) to the yes votes cast for this Issue.
     */
    public void addNoVote() {
        addVote();
    }

	@Override
	public void setBallotItemId(long ballotItemId) {
		this.ballotItemID = ballotItemId;
	}

	@Override
	public long getBallotItemId() {
		return this.ballotItemID;
		
	}
}
