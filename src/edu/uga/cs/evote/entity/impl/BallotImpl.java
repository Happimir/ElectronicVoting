package edu.uga.cs.evote.entity.impl;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Ballot;
import edu.uga.cs.evote.entity.BallotItem;
import edu.uga.cs.evote.entity.ElectoralDistrict;
import edu.uga.cs.evote.entity.VoteRecord;
import edu.uga.cs.evote.persistence.PersistenceLayer;
import edu.uga.cs.evote.persistence.impl.PersistenceLayerImpl;
import edu.uga.cs.evote.persistence.impl.Persistent;

import java.util.Date;
import java.util.List;

/**
 * @author Michael Kovalsky
 */
public class BallotImpl extends Persistent implements Ballot {

    //Simple data type block
    private Date openDate;
    private Date closeDate;
    private boolean approved;
    private boolean isOpen;
    //Object data type block
    private ElectoralDistrict electoralDistrict;

    //collection data type block
    private List<BallotItem> ballotItems;
    private List<VoteRecord> voteRecords;
    
  
    
    /**
     * default public constructor
     */
    public BallotImpl() {
    	super(-1);
    	this.openDate = null;
    	this.closeDate = null;
    	this.approved = false;
    	this.electoralDistrict = null;
    	this.ballotItems = null;
    	this.voteRecords = null;
    	this.isOpen = false;
    }
    
    public BallotImpl(Date openDate, Date closeDate, boolean approved, 
    		ElectoralDistrict electoralDistrict, boolean isOpen) {
    	
    	super(-1);
    	this.openDate = openDate;
    	this.closeDate = closeDate;
    	this.approved = approved;
    	this.electoralDistrict = electoralDistrict;
    	this.ballotItems = null;
    	this.voteRecords = null;
    	this.isOpen= isOpen;
    	
    }
    
    
    public boolean getApproved() {
    	return this.approved;
    }
    
    public void setApproved(boolean approved) {
    	this.approved = approved;
    }

    public Date getOpenDate() {
        return this.openDate;
    }

    /** Set the new opening date for this Ballot
     * @param openDate the new opening date
     */
    public void setOpenDate( Date openDate ) {
        this.openDate = openDate;

    }

    /** Return the closing date for this Ballot
     * @return the closing date for this Ballot
     */
    public Date getCloseDate() {
        return this.closeDate;

    }

    /** Set the new closing date for this Ballot
     * @param closeDate the new closing date
     */
    public void setCloseDate( Date closeDate ) {
        this.closeDate = closeDate;
    }

    /** Return the ElectoralDistrict of this Ballot
     * @return the ElectoralDistrict of this Ballot
     * @throws EVException in case there is a problem with the retrieval of the requested object
     */
    public ElectoralDistrict getElectoralDistrict() throws EVException {
        return this.electoralDistrict;
    }

    /** Set the new ElectoralDistrict of this Ballot
     * @param electoralDistrict the new ElectoralDistrict
     * @throws EVException in case there is a problem with setting a link to the argument object
     */
    public void setElectoralDistrict( ElectoralDistrict electoralDistrict ) throws EVException {
        this.electoralDistrict = electoralDistrict;
    }

    /** Return a list of BallotItems on this Ballot.
     * @return a list of BallotItems on this Ballot
     * @throws EVException in case there is a problem with the retrieval of the requested objects
     */
    public List<BallotItem> getBallotItems() throws EVException {
    	if(this.ballotItems == null) {
    		if(isPersistent()) {
    			ballotItems = getPersistenceLayer().restoreBallotIncludesBallotItem(this);
    			
    		}
    		else {
    			throw new EVException ("Ballot is not persistent");
    		}
    	}
        return this.ballotItems;
    }

    /** Add a new BallotItem to the end of this Ballot.
     * @param ballotItem the new BallotItem
     * @throws EVException in case there is a problem with adding a link to the argument object
     */
    public void addBallotItem( BallotItem ballotItem ) throws EVException {
        this.ballotItems.add(ballotItem);
    }

    /** Delete a BallotItem from this Ballot.
     * @param ballotItem the BallotItem to be removed
     * @throws EVException in case there is a problem with deleting a link to the argument object
     */
    public void deleteBallotItem( BallotItem ballotItem ) throws EVException {
        this.ballotItems.remove(ballotItem);
    }

    /** Return a list of VoteRecords for this Ballot.
     * @return a List of VoteRecords recorded for this Ballot
     * @throws EVException in case there is a problem with traversing links to the requested objects
     */
    /*public List<VoteRecord> getVoterVoteRecords() throws EVException {
    	if(voteRecords == null) {
    		if(isPersistent()) {
    			//Put code to traverse from ballotItem and ballot in the issue/elections manager.
    			VoteRecord voteRecord = new VoteRecordImpl();
    			voteRecord.setBallotItem(this);
    			voteRecords = getPersistenceLayer().restoreVoteRecord(voteRecord);	
    		}
    		else {
    			throw new EVException("Ballot is not persistent");
    		}
    	}
        return this.voteRecords;
    }*/
    public boolean getOpen(){
    	return this.isOpen;
    }

	@Override
	public void setOpen(boolean boo) throws EVException {
		this.isOpen = boo;
		
	}
}
