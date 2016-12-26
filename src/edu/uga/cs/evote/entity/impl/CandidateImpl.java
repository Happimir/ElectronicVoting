package edu.uga.cs.evote.entity.impl;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Candidate;
import edu.uga.cs.evote.entity.Election;
import edu.uga.cs.evote.entity.PoliticalParty;
import edu.uga.cs.evote.persistence.impl.Persistent;

/**
 * @author michael kovalsky
 * Date: 10/23/16.
 */
public class CandidateImpl extends Persistent implements Candidate {

    private String name;
    private int voteCount;

    private Election election;
    private PoliticalParty politicalParty;
   
    
    /**
     * Default Constructor
     */
    public CandidateImpl() {
    	super(-1);
    	this.name = "";
    	this.voteCount = 0;
    	this.election = null;
    	this.politicalParty = null;
    }
    
    
    public CandidateImpl(String name, PoliticalParty politicalParty , Election election) {
    	super(-1);
    	this.name = name;
    	this.voteCount = 0;
    	this.election = election;
    	this.politicalParty = politicalParty;
    }

    /** Return the name of this candidate
     * @return the name of this candidate
     */
    public String getName() {
        return this.name;
    }

    /** Set the new name of this candidate
     * @param name the new name of this candidate
     */
    public void setName( String name ) {
        this.name = name;
    }

    /** Return the vote count for this candidate
     * @return the vote count for this candidate
     */
    public int getVoteCount() {
        return this.voteCount;
    }

    /** Set the vote count for this candidate
     * @param voteCount the new vote count for this candidate
     * @throws EVException in case the new vote is negative
     */
    public void setVoteCount( int voteCount ) throws EVException {
        this.voteCount = voteCount;
    }

    /** Add one vote (increment by one) to the votes cast for this Candidate.
     */
    public void addVote() {
        this.voteCount++;
        this.election.addVote();
    }

    /** Return the Election for which this candidate is running.
     * @return the Election for this candidate
     * @throws EVException in case there is a problem with traversing a link to the requested object
     */
    public Election getElection() throws EVException {
        return this.election;
    }

    /** Set the Election for which this candidate is running.
     * @param election the new election for this candidate
     * @throws EVException in case there is a problem with setting a link to the requested object
     */
    public void setElection( Election nElection ) throws EVException {
        this.election = nElection;
        nElection.addCandidate(this);
    }

    /** Return the political party of this candidate.
     * @return the PoliticalParty of this candidate
     * @throws EVException in case there is a problem with traversing a link to the requested object
     */
    public PoliticalParty getPoliticalParty() throws EVException {
        return this.politicalParty;
    }

    /** Set the political party of this candidate.
     * @param politicalParty the new PoiticalParty
     * @throws EVException in case there is a problem with setting a link to the requested object
     */
    public void setPoliticalParty( PoliticalParty politicalParty ) throws EVException {
        this.politicalParty = politicalParty;
    }
}
