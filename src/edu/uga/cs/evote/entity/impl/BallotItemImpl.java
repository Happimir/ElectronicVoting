package edu.uga.cs.evote.entity.impl;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Ballot;
import edu.uga.cs.evote.entity.BallotItem;
import edu.uga.cs.evote.persistence.impl.Persistent;

/**
 * @author Michael Kovalsky
 */
public abstract class BallotItemImpl extends Persistent implements BallotItem {

    private int voteCount;
    private Ballot ballot;
    
    /**
     * Default constructor
     */
    public BallotItemImpl() {
    	super(-1);
    	this.voteCount = 0;
    	this.ballot = null;
    }

    /**
     * Parametrized constructor 
     * 
     * @param voteCount
     * @param ballot
     */
    public BallotItemImpl(int voteCount, Ballot ballot) {
    	super(-1);
    	this.voteCount = voteCount;
    	this.ballot = ballot;
    }
    
    /** Return the vote count of this BallotItem.
     * @return the vote count of this BallotItem
     */
    public int getVoteCount() {
        return this.voteCount;
    }

    /** Set the new vote count of this BallotItem.
     * @param voteCount the new vote count;  it must be non-negative
     * @throws EVException in case the new vote is negative
     */
    public void setVoteCount( int voteCount ) throws EVException {
        this.voteCount = voteCount;
    }

    /** Add one vote (increment by one) to the vote count of this BallotItem.
     */
    public void addVote() {
        voteCount++;
    }

    /** Return the Ballot on which this BallotItem is listed.
     * @return the Ballot of this BallotItem
     * @throws EVException in case there is a problem with traversing a link to the requested object
     */
    public Ballot getBallot() throws EVException {
        return this.ballot;
    }

    /** Set the new Ballot on which this BallotItem is listed.
     * @param ballot the new Ballot
     * @throws EVException in case there is a problem with setting a link to the requested object
     */
    public void setBallot( Ballot ballot ) throws EVException {
        this.ballot = ballot;
    }
}
