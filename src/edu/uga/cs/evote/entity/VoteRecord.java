package edu.uga.cs.evote.entity;

import java.util.Date;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.persistence.Persistable;

public interface VoteRecord extends Persistable
{
    /** Return the date the vote has been cast.
     * @return the date of the cast vote
     */
    public Date getDate();
    
    /** Set the date the vote has been cast.
     * @param date the new date of the cast vote
     */
    public void setDate( Date date );
    
    /** Return the Voter who cast the vote.
     * @return the voter who cast the vote
     * @throws EVException in case there is a problem with the retrieval of the requested object
     */
    public Voter getVoter() throws EVException;
    
    /** Set the Voter who cast the vote.
     * @param voter the new voter who cast the vote
     * @throws EVException in case there is a problem with setting a link to the argument object
     */
    public void setVoter( Voter voter ) throws EVException;
    
    /** Return the Ballot on which the vote has been cast.
     * @return the ballot on which the vote has been cast
     * @throws EVException in case there is a problem with the retrieval of the requested object
     */
    public BallotItem getBallotItem() throws EVException;
    
    /** Set the new Ballot on which the vote has been cast.
     * @param ballot the new ballot on which the vote has been cast
     * @throws EVException in case there is a problem with setting a link to the argument object
     */
    public void setBallotItem( BallotItem ballotItem ) throws EVException; 

}
