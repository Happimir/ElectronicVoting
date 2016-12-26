package edu.uga.cs.evote.entity;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.persistence.Persistable;

public interface BallotItem 
    extends Persistable
{
    /** Return the vote count of this BallotItem.
     * @return the vote count of this BallotItem
     */
    public int getVoteCount();
    
    /** Set the new vote count of this BallotItem.
     * @param voteCount the new vote count;  it must be non-negative
     * @throws EVException in case the new vote is negative
     */
    public void setVoteCount( int voteCount ) throws EVException;

    /** Add one vote (increment by one) to the vote count of this BallotItem.
     */
    public void addVote();
    
    /** Return the Ballot on which this BallotItem is listed.
     * @return the Ballot of this BallotItem
     * @throws EVException in case there is a problem with traversing a link to the requested object
     */
    public Ballot getBallot() throws EVException;
    
    /** Set the new Ballot on which this BallotItem is listed.
     * @param ballot the new Ballot 
     * @throws EVException in case there is a problem with setting a link to the requested object
     */
    public void setBallot( Ballot ballot ) throws EVException;
}
