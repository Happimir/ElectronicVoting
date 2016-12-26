package edu.uga.cs.evote.entity;

import java.util.List;

import edu.uga.cs.evote.EVException;

/** This class represents information about a registered voter in the eVote system.
 * A voter is a user who additionally has an age and a voter id.
 *
 */
public interface Voter 
    extends User
{
    /** Return the voter id for this voter.
     * @return the String representing the id of the voter
     */
    public String getVoterId();
    
    /** Set the new voter id for this voter.
     * @param voterId the new voter id of this voter
     */
    public void setVoterId( String voterId );
    
    /** Return the age of this voter.
     * @return the age of this voter
     */
    public int getAge();
    
    /** Set the new age for this voter
     * @param age the new age of this voter
     */
    public void setAge( int age );
    
    /** Return the ElectoralDistrict of this voter
     * @return the ElectoralDistrict of this voter
     * @throws EVException in case there is a problem with traversing a link to the requested object
     */
    public ElectoralDistrict getElectoralDistrict() throws EVException;
    
    /** Set the new ElectoralDistrict of this voter
     * @param electoralDistrict the new ElectoralDistrict
     * @throws EVException in case there is a problem with setting a link to the requested object
     */
    public void setElectoralDistrict( ElectoralDistrict electoralDistrict ) throws EVException;
    
    /** Return a list of VoteRecords for this Voter.
     * @return a List of VoteRecords recorded for this Voter
     * @throws EVException in case there is a problem with traversing links to the requested objects
     */
    public List<VoteRecord> getBallotVoteRecords() throws EVException;
    
}
