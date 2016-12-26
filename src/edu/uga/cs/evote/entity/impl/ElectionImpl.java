package edu.uga.cs.evote.entity.impl;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Ballot;
import edu.uga.cs.evote.entity.Candidate;
import edu.uga.cs.evote.entity.Election;

import java.util.ArrayList;
import java.util.List;

/**
 * @author michael kovalsky
 * Date: 10/23/16.
 */
public class ElectionImpl extends BallotItemImpl implements Election {

    private String office;
    private boolean isPartisan;
    private long ballotItemID;

    private List<Candidate> candidates = new ArrayList<>();
    
    /**
     * Default Constructor
     */
    public ElectionImpl() {
    	super();
    	this.office = "";
    	this.isPartisan = false;
    	this.candidates = new ArrayList<>();
    	this.ballotItemID = -1;
    }
    
    public ElectionImpl(String office, boolean isPartisan, Ballot ballot) {
    	super(0, ballot);
    	this.office = office;
    	this.ballotItemID = 0;
    	this.isPartisan = isPartisan;
    }

    /** Return the office for which this election is held.
     * @return the office of this election
     */
    public String getOffice() {
        return this.office;
    }

    /** Set the new office for which this election is held.
     * @param office the new office for this election
     */
    public void setOffice( String office ) {
        this.office = office;
    }

    /** Return true if this is a partisan election and false otherwise.
     * @return partisan status of this election
     */
    public boolean getIsPartisan() {
        return this.isPartisan;
    }

    /** Set the new partisan status of this election.
     * @param isPartisan the new partisan status
     */
    public void setIsPartisan( boolean isPartisan ) {
        this.isPartisan = isPartisan;
    }

    /** Return a list of the candidates for this election.
     * @return the list of the candidates
     * @throws EVException in case there is a problem with traversing a link to the requested objects
     */
    public List<Candidate> getCandidates() throws EVException {
    	if(candidates == null) {
    		if(isPersistent()) {
    			candidates = getPersistenceLayer().restoreCandidateIsCandidateInElection(this);
    			
    		}
    		else {
    			throw new EVException("Election is not persistent");
    		}
    	}
        return this.candidates;
    }

    /** Add a candidate for this election.
     * @param candidate the candidate to be added
     * @throws EVException in case there is a problem with setting a link to the requested object
     */
    public void addCandidate( Candidate candidate ) throws EVException {
        this.candidates.add(candidate);
    }

    /** Remove the candidate from the candidates for this election.
     * @param candidate to be removed
     * @throws EVException in case there is a problem with removing the link to the requested object
     */
    public void deleteCandidate( Candidate candidate ) throws EVException {
        this.candidates.remove(candidate);
    }

	@Override
	public long getBallotItemID() {
		return this.ballotItemID;
	}

	@Override
	public void setBallotItemID(long id) {
		this.ballotItemID = id;
	}
}
