package edu.uga.cs.evote.entity.impl;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Ballot;
import edu.uga.cs.evote.entity.ElectoralDistrict;
import edu.uga.cs.evote.entity.Voter;
import edu.uga.cs.evote.persistence.impl.Persistent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author michael kovalsky
 * Date: 10/23/16.
 */
public class ElectoralDistrictImpl extends Persistent implements ElectoralDistrict {

    private String name;

    private List<Voter> voters = new ArrayList<>();
    private List<Ballot> ballots = new ArrayList<>();

    public ElectoralDistrictImpl() {
        super(-1);
        this.name = null;
        this.voters = null;
        this.ballots = null;
    }

    public ElectoralDistrictImpl(String name) {
        super(-1);
        this.name = name;
        this.voters = null;
        this.ballots = null;
    }

    /** Return the name of this electoral district.
     * @return the name of this electoral district
     */
    public String getName() {

        return this.name;
    }

    /** Set the new name of this electoral district.
     * @param name the new name of this electoral district
     */
    public void setName( String name ) {

        this.name = name;
    }

    /** Return a list of the voters in this electoral district.
     * @return a list of the voters in this electoral district
     * @throws EVException in case there is a problem with traversing links to the requested objects
     */
    public List<Voter> getVoters() throws EVException {
    	if(voters == null) {
    		if(isPersistent()) {
//    			Voter voter = new VoterImpl();
//    			voter.setElectoralDistrict(this);
    			voters = getPersistenceLayer().restoreVoterBelongsToElectoralDistrict(this);
    		}
    		else {
    			throw new EVException("Electoral District is not persistent");
    		}
    	}
        return this.voters;
       
    }
    
    /**
     * Custom added method to add voters to electoral district. 
     * @param voter
     */
    public void addVoters(Voter voter) {
    	this.voters.add(voter);
    }

    /** Return a list of the ballots created for this electoral district.
     * @return a list of the ballots created for this electoral district
     * @throws EVException in case there is a problem with traversing links to the requested objects
     */
    public List<Ballot> getBallots() throws EVException {
    	if(ballots == null) {
    		if(isPersistent()) {
    			ballots = getPersistenceLayer().restoreElectoralDistrictHasBallotBallot(this);
    		}
    		else {
    			throw new EVException("Electoral District is not persistent");
    		}
    	}
        return this.ballots;
    }

    /** Add a new ballot for this electoral district.
     * @param ballot the new ballot for this electoral district
     * @throws EVException in case there is a problem with creating a link to the requested objects
     */
    public void addBallot( Ballot ballot ) throws EVException {
        this.ballots.add(ballot);
    }

    /** Remove a ballot from this electoral district.
     * @param ballot the ballot to be removed from this electoral district
     * @throws EVException in case there is a problem with deleting a link to the requested objects
     */
    public void deleteBallot( Ballot ballot ) throws EVException {
        this.ballots.remove(ballot);
    }
}
