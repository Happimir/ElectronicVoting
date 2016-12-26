package edu.uga.cs.evote.entity.impl;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.ElectoralDistrict;
import edu.uga.cs.evote.entity.VoteRecord;
import edu.uga.cs.evote.entity.Voter;
import edu.uga.cs.evote.persistence.impl.Persistent;

import java.util.List;

/**
 * @author Michael Kovalsky
 * Date: 10/23/16.
 */
                             //UserImpl
public class VoterImpl extends UserImpl implements Voter {

    //Voter related attributes
    private Integer age;
    private String voterId;

    private ElectoralDistrict electoralDistrict;
    private List<VoteRecord> voteRecords;

    /**
     * Default Constructor
     */
    public VoterImpl() {
        super();
        
        //Voter attributes
        this.age = 0;
        this.voterId = null;
        this.electoralDistrict = null;
        this.voteRecords = null;
    }

    /**
     * Parametrized Constructor
     *
     * first six belong to user's super
     * @param first firstName
     * @param last  lastname
     * @param userName userName
     * @param password password
     * @param email email
     * @param address address
     * @param age age
     * @param voterId voterId
     * @param electoralDistrict electoralDistrict
     * @param voteRecords voteRecords
     */
    public VoterImpl(String userName, String userPass, String email, String firstName, String lastName, String address, int age) {
        super(userName, userPass, email,  firstName,  lastName, address);

        //voter attributes
        this.age = age;
        this.voterId = null;
        this.electoralDistrict = null;
        this.voteRecords = null;
    }

   

    /*-----------------------------------------Voter Methods------------------------------------------ */

    /** Return the voter id for this voter.
     * @return the String representing the id of the voter
     */
    public String getVoterId() {
        return this.voterId;
    }

    /** Set the new voter id for this voter.
     * @param voterId the new voter id of this voter
     */
    public void setVoterId( String voterId )
    {
        this.voterId = voterId;
    }

    /** Return the age of this voter.
     * @return the age of this voter
     */
    public int getAge() {
        return this.age;
    }

    /** Set the new age for this voter
     * @param age the new age of this voter
     */
    public void setAge( int age ) {
        this.age = age;
    }

    /** Return the ElectoralDistrict of this voter
     * @return the ElectoralDistrict of this voter
     * @throws EVException in case there is a problem with traversing a link to the requested object
     */
    public ElectoralDistrict getElectoralDistrict() throws EVException {
        return this.electoralDistrict;
    }

    /** Set the new ElectoralDistrict of this voter
     * @param electoralDistrict the new ElectoralDistrict
     * @throws EVException in case there is a problem with setting a link to the requested object
     */
    public void setElectoralDistrict( ElectoralDistrict electoralDistrict ) throws EVException {
        this.electoralDistrict = electoralDistrict;
    }

    /** Return a list of VoteRecords for this Voter.
     * @return a List of VoteRecords recorded for this Voter
     * @throws EVException in case there is a problem with traversing links to the requested objects
     */
    public List<VoteRecord> getBallotVoteRecords() throws EVException {
        if(this.voteRecords == null) {
            if( isPersistent()) {
                    VoteRecord voteRecord = new VoteRecordImpl();
                    voteRecord.setVoter(this);
                    voteRecords = getPersistenceLayer().restoreVoteRecord(voteRecord);
            }
            else {
            	throw new EVException("Voter is not persistent");
            }
        }
        return this.voteRecords;
    }

}
