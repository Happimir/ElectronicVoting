package edu.uga.cs.evote.entity.impl;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Ballot;
import edu.uga.cs.evote.entity.BallotItem;
import edu.uga.cs.evote.entity.VoteRecord;
import edu.uga.cs.evote.entity.Voter;
import edu.uga.cs.evote.persistence.impl.Persistent;

import java.util.Date;

/**
 * @author Michael Kovalsky
 * Date: 10/23/16.
 */
public class VoteRecordImpl extends Persistent implements VoteRecord {

    private Date date;

    private Voter voter;
    private BallotItem ballotItem;

    /**
     * Default Constructor
     */
    public VoteRecordImpl() {
        super(-1);
        this.date = null;
        this.voter = null;
        this.ballotItem = null;
    }
    

    /**
     * Parametrized Constructor
     *
     * @param date
     * @param ballot
     * @param voter
     */
    public VoteRecordImpl(Voter voter, BallotItem ballotItem, Date date ) {
        super(-1);
        this.date = date;
        this.voter = voter;
        this.ballotItem = ballotItem;
    }


    /** Return the date the vote has been cast.
     * @return the date of the cast vote
     */
    public Date getDate() {
        return this.date;
    }

    /** Set the date the vote has been cast.
     * @param date the new date of the cast vote
     */
    public void setDate( Date date ) {
        this.date = date;
    }

    /** Return the Voter who cast the vote.
     * @return the voter who cast the vote
     * @throws EVException in case there is a problem with the retrieval of the requested object
     */
    public Voter getVoter() throws EVException {
        return this.voter;
    }

    /** Set the Voter who cast the vote.
     * @param voter the new voter who cast the vote
     * @throws EVException in case there is a problem with setting a link to the argument object
     */
    public void setVoter( Voter voter ) throws EVException {
        this.voter = voter;
    }

    /** Return the Ballot on which the vote has been cast.
     * @return the ballot on which the vote has been cast
     * @throws EVException in case there is a problem with the retrieval of the requested object
     */
    public BallotItem getBallotItem() throws EVException {
        return this.ballotItem;
    }

    /** Set the new Ballot on which the vote has been cast.
     * @param ballot the new ballot on which the vote has been cast
     * @throws EVException in case there is a problem with setting a link to the argument object
     */
    public void setBallotItem( BallotItem ballotItem ) throws EVException {
        this.ballotItem = ballotItem;
    }
}
