package edu.uga.cs.evote.entity;

import java.util.Date;
import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.persistence.Persistable;



public interface Ballot extends Persistable
{
	//TODO: Adding getters/setters for approved. 
	
	public boolean getApproved();
    
    public void setApproved(boolean approved);
	
    /** Return the opening date for this Ballot
     * @return the opening date for this Ballot
     */
    public Date getOpenDate();
    
    /** Set the new opening date for this Ballot
     * @param openDate the new opening date
     */
    public void setOpenDate( Date openDate );
    
    /** Return the closing date for this Ballot
     * @return the closing date for this Ballot
     */
    public Date getCloseDate();
    
    /** Set the new closing date for this Ballot
     * @param closeDate the new closing date
     */
    public void setCloseDate( Date closeDate );
    
    /** Return the ElectoralDistrict of this Ballot
     * @return the ElectoralDistrict of this Ballot
     * @throws EVException in case there is a problem with the retrieval of the requested object
     */
    public ElectoralDistrict getElectoralDistrict() throws EVException;
    
    /** Set the new ElectoralDistrict of this Ballot
     * @param electoralDistrict the new ElectoralDistrict
     * @throws EVException in case there is a problem with setting a link to the argument object
     */
    public void setElectoralDistrict( ElectoralDistrict electoralDistrict ) throws EVException;
    
    /** Return a list of BallotItems on this Ballot.
     * @return a list of BallotItems on this Ballot
     * @throws EVException in case there is a problem with the retrieval of the requested objects
     */
    public List<BallotItem> getBallotItems() throws EVException;
    
    /** Add a new BallotItem to the end of this Ballot.
     * @param ballotItem the new BallotItem
     * @throws EVException in case there is a problem with adding a link to the argument object
     */
    public void addBallotItem( BallotItem ballotItem ) throws EVException;
    
    /** Delete a BallotItem from this Ballot.
     * @param ballotItem the BallotItem to be removed
     * @throws EVException in case there is a problem with deleting a link to the argument object
     */
    public void deleteBallotItem( BallotItem ballotItem ) throws EVException;
    
    /** Return a list of VoteRecords for this Ballot.
     * @return a List of VoteRecords recorded for this Ballot
     * @throws EVException in case there is a problem with traversing links to the requested objects
     */
    //public List<VoteRecord> getVoterVoteRecords() throws EVException;

    public boolean getOpen() throws EVException;
    public void setOpen(boolean boo) throws EVException;
}
