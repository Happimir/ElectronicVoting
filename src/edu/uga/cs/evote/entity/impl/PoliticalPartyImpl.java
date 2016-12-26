package edu.uga.cs.evote.entity.impl;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Candidate;
import edu.uga.cs.evote.entity.PoliticalParty;
import edu.uga.cs.evote.persistence.impl.Persistent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author michael kovalsky
 * Date: 10/23/16.
 */
public class PoliticalPartyImpl extends Persistent implements PoliticalParty {

    private String name;
    private List<Candidate> candidates = new ArrayList<>();
    
    /**
     * Default constructor
     */
    public PoliticalPartyImpl() {
    	super(-1);
    	this.name = "";
    	this.candidates = null;
    }
    
    public PoliticalPartyImpl(String name) {
    	super(-1);
    	this.name = name;
    	this.candidates = null;
    }

    /** Return the name of this political party.
     * @return the name of the political party
     */
    public String getName() {
        return this.name;
    }

    /** Set the new name of this political party.
     * @param name the new name of the political party
     */
    public void setName( String name ) {
        this.name = name;
    }

    /** Return a list of the candidates belonging to this political party.
     * @return a list of this political party's candidates
     * @throws EVException in case there is a problem with traversing links to the requested objects
     */
    public List<Candidate> getCandidates() throws EVException {
    	if(candidates == null) {
    		if(isPersistent()) {
//    			Candidate candidate = new CandidateImpl();
//    			candidate.setPoliticalParty( this );
    			candidates = getPersistenceLayer().restoreCandidateIsMemberOfPoliticalParty(this);
    		} else {
    			throw new EVException("The candidate is not persistent");
    		}
    	}
        return this.candidates;
    }
}
