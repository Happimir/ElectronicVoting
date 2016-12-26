package edu.uga.cs.evote.entity;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.persistence.Persistable;

public interface Candidate extends Persistable
{
    /** Return the name of this candidate
     * @return the name of this candidate
     */
    public String getName();
    
    /** Set the new name of this candidate
     * @param name the new name of this candidate
     */
    public void setName( String name );
    
    /** Return the vote count for this candidate
     * @return the vote count for this candidate
     */
    public int getVoteCount();
    
    /** Set the vote count for this candidate
     * @param voteCount the new vote count for this candidate
     * @throws EVException in case the new vote is negative
    */
    public void setVoteCount( int voteCount ) throws EVException;
    
    /** Add one vote (increment by one) to the votes cast for this Candidate.
     */
    public void addVote();

    /** Return the Election for which this candidate is running.
     * @return the Election for this candidate
     * @throws EVException in case there is a problem with traversing a link to the requested object
     */
    public Election getElection() throws EVException;
    
    /** Set the Election for which this candidate is running.
     * @param election the new election for this candidate
     * @throws EVException in case there is a problem with setting a link to the requested object
     */
    public void setElection( Election election ) throws EVException;
    
    /** Return the political party of this candidate.
     * @return the PoliticalParty of this candidate
     * @throws EVException in case there is a problem with traversing a link to the requested object
     */
    public PoliticalParty getPoliticalParty() throws EVException;
    
    /** Set the political party of this candidate.
     * @param politicalParty the new PoiticalParty
     * @throws EVException in case there is a problem with setting a link to the requested object
     */
    public void setPoliticalParty( PoliticalParty politicalParty ) throws EVException;
}
