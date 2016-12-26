package edu.uga.cs.evote.persistence;

import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Ballot;
import edu.uga.cs.evote.entity.BallotItem;
import edu.uga.cs.evote.entity.Candidate;
import edu.uga.cs.evote.entity.Election;
import edu.uga.cs.evote.entity.ElectionsOfficer;
import edu.uga.cs.evote.entity.ElectoralDistrict;
import edu.uga.cs.evote.entity.Issue;
import edu.uga.cs.evote.entity.PoliticalParty;
import edu.uga.cs.evote.entity.VoteRecord;
import edu.uga.cs.evote.entity.Voter;


/**
 * This is the interface to the Persistence Layer subsystem of the eVote system.
 * <p>
 * Each entity class has three types of operations: restore, store, and delete.  The
 * store operation does an update, if the given argument has already been stored previously
 * (i.e., it is already persistent).
 * <p>
 * Note, that since BallotItem and User are abstract classes and no use cases in the eVote
 * system manipulate these objects no methods for handling them have been provided.  
 * Nevertheless, such methods could be provided, if needed.  For example, we could 
 * easily restore all User objects (in fact, a set of ElectionOfficers and Voters, and rely
 * on polymorphism to discover the actual type of each retrieved object.  Here, however,
 * we do not require these methods for BallotItem.
 * 
 * One association traversal returns a List of BallotItems.  In this case,
 * all of the returned objects are going to be wither Elections or Issues (no actual BallotItem objects 
 * will ever exist).  The application logic will have to distinguish among these objects based on
 * polymorphism or explicit type checks.
 * 
 * <p>
 * For each binary association connecting two entity classes <code><i>ClassA</i></code> and <code><i>ClassB</i></code>, 
 * there are four operations: 
 * <ul>
 * <li><code>store<i>ClassAClassB</i></code>, which is used to create a link between two instances of <code><i>ClassA</i></code> 
 *     and <code><i>ClassB</i></code>,</li>
 * <li><code>restore<i>ClassAClassB</i></code>, two overloaded versions are used to traverse the link from 
 *     <code><i>ClassA</i></code> to <code><i>ClassB</i></code> and from
 *     <code><i>ClassB</i></code> to <code><i>ClassA</i></code>, and</li>
 * <li><code>delete<i>ClassAClassB</i></code>, which is used to remove the link connecting two object instances.</li>
 * </ul>
 * <p>
 * In case there are two associations connecting the same two entity classes, the names of the association-related operations include
 * the name of the association between the classes.  Furthermore, depending on the multiplicity of the association endpoint,
 * the return value is either <code><i>ClassA</i></code> (<code><i>ClassB</i></code>), if the multiplicity is one or optional,
 * or an <code>List&lt;<i>ClassA</i>&gt;</code> (<code>List&lt;<i>ClassB</i>&gt;</code>), if the multiplicity is many.
 * <p>Note, that for any association endpoint with a multiplicity of one or optional, the entity class on the opposite end of
 * the association has get/set methods get or set the (single) linked object instance.
 */
public interface PersistenceLayer
{
    /** 
     * Restore all ElectionsOfficer objects that match attributes of the model ElectionsOfficer.
     * @param modelElectionsOfficer the model ElectionsOfficer; if null is provided, all ElectionsOfficer objects will be returned
     * @return a List of the located ElectionsOfficer objects
     * @throws EVException in case an error occurred during the restore operation 
     */
    public List<ElectionsOfficer> restoreElectionsOfficer( ElectionsOfficer modelElectionsOfficer ) throws EVException;
    
    /** 
     * Store a given ElectionsOfficer object in the persistent data store.  
     * If the ElectionsOfficer object to be stored is already persistent, the persistent 
     * object in the data store is updated.
     * @param electionsOfficer the ElectionsOfficer to be stored
     * @throws EVException in case an error occurred during the store operation 
     */
    public void storeElectionsOfficer( ElectionsOfficer electionsOfficer ) throws EVException;
    
    /** 
     * Delete a given ElectionsOfficer object from the persistent data store.
     * @param electionsOfficer the ElectionsOfficer to be deleted
     * @throws EVException in case an error occurred during the delete operation 
     */
    public void deleteElectionsOfficer( ElectionsOfficer electionsOfficer ) throws EVException; 
    
    /** 
     * Restore all Voter objects that match attributes of the model Voter.
     * @param modelVoter the model Voter; if null is provided, all Voter objects will be returned
     * @return a List of the located Voter objects
     * @throws EVException in case an error occurred during the restore operation 
     */
    public List<Voter> restoreVoter( Voter modelVoter ) throws EVException;
    
    /** 
     * Store a given Voter object in the persistent data store.
     * If the Voter object to be stored is already persistent, the persistent 
     * object in the data store is updated.
     * @param voter the Voter to be stored
     * @throws EVException in case an error occurred during the store operation 
     */
    public void storeVoter( Voter voter ) throws EVException;
    
    /** 
     * Delete a given Voter object from the persistent data store.
     * @param voter the Voter to be deleted
     * @throws EVException in case an error occurred during the delete operation 
     */
    public void deleteVoter( Voter voter ) throws EVException;      

    /** 
     * Restore all Ballot objects that match attributes of the model Ballot.
     * @param modelBallot the model Ballot; if null is provided, all Ballot objects will be returned
     * @return a List of the located Ballot objects
     * @throws EVException in case an error occurred during the restore operation 
     */
    public List<Ballot> restoreBallot( Ballot modelBallot ) throws EVException;
    
    /** 
     * Store a given Ballot object in the persistent data store.
     * If the Ballot object to be stored is already persistent, the persistent 
     * object in the data store is updated.
     * @param ballot the Ballot to be stored
     * @throws EVException in case an error occurred during the store operation 
     */
    public void storeBallot( Ballot ballot ) throws EVException;
    
    /** 
     * Delete a given Ballot object from the persistent data store.
     * @param ballot the Ballot to be deleted
     * @throws EVException in case an error occurred during the delete operation 
     */
    public void deleteBallot( Ballot ballot ) throws EVException;      

    /** 
     * Restore all Candidate objects that match attributes of the model Candidate.
     * @param modelCandidate the model Candidate; if null is provided, all Candidate objects will be returned
     * @return a List of the located Candidate objects
     * @throws EVException in case an error occurred during the restore operation 
     */
    public List<Candidate> restoreCandidate( Candidate modelCandidate ) throws EVException;
    
    /** 
     * Store a given Candidate object in the persistent data store.
     * If the Candidate object to be stored is already persistent, the persistent 
     * object in the data store is updated.
     * @param candidate the Candidate to be stored
     * @throws EVException in case an error occurred during the store operation 
     */
    public void storeCandidate( Candidate candidate ) throws EVException;
    
    /** 
     * Delete a given Candidate object from the persistent data store.
     * @param candidate the Candidate to be deleted
     * @throws EVException in case an error occurred during the delete operation 
     */
    public void deleteCandidate( Candidate candidate ) throws EVException;      

    /** 
     * Restore all Election objects that match attributes of the model Election.
     * @param modelElection the model Election; if null is provided, all Election objects will be returned
     * @return a List of the located Election objects
     * @throws EVException in case an error occurred during the restore operation 
     */
    public List<Election> restoreElection( Election modelElection ) throws EVException;
    
    /** 
     * Store a given Election object in the persistent data store.
     * If the Election object to be stored is already persistent, the persistent 
     * object in the data store is updated.
     * @param election the Election to be stored
     * @throws EVException in case an error occurred during the store operation 
     */
    public void storeElection( Election election ) throws EVException;
    
    /** 
     * Delete a given Election object from the persistent data store.
     * @param election the Election to be deleted
     * @throws EVException in case an error occurred during the delete operation 
     */
    public void deleteElection( Election election ) throws EVException;      

    /** 
     * Restore all ElectoralDistrict objects that match attributes of the model ElectoralDistrict.
     * @param modelElectoralDistrict the model ElectoralDistrict; if null is provided, all ElectoralDistrict objects will be returned
     * @return a List of the located ElectoralDistrict objects
     * @throws EVException in case an error occurred during the restore operation 
     */
    public List<ElectoralDistrict> restoreElectoralDistrict( ElectoralDistrict modelElectoralDistrict ) throws EVException;
    
    /** 
     * Store a given ElectoralDistrict object in the persistent data store.
     * If the ElectoralDistrict object to be stored is already persistent, the persistent 
     * object in the data store is updated.
     * @param electoralDistrict the ElectoralDistrict to be stored
     * @throws EVException in case an error occurred during the store operation 
     */
    public void storeElectoralDistrict( ElectoralDistrict electoralDistrict ) throws EVException;
    
    /** 
     * Delete a given ElectoralDistrict object from the persistent data store.
     * @param electoralDistrict the ElectoralDistrict to be deleted
     * @throws EVException in case an error occurred during the delete operation 
     */
    public void deleteElectoralDistrict( ElectoralDistrict electoralDistrict ) throws EVException;      

    /** 
     * Restore all Issue objects that match attributes of the model Issue.
     * @param modelIssue the model Issue; if null is provided, all Issue objects will be returned
     * @return a List of the located Issue objects
     * @throws EVException in case an error occurred during the restore operation 
     */
    public List<Issue> restoreIssue( Issue modelIssue ) throws EVException;
    
    /** 
     * Store a given Issue object in the persistent data store.
     * If the Issue object to be stored is already persistent, the persistent 
     * object in the data store is updated.
     * @param issue the Issue to be stored
     * @throws EVException in case an error occurred during the store operation 
     */
    public void storeIssue( Issue issue ) throws EVException;
    
    /** 
     * Delete a given Issue object from the persistent data store.
     * @param issue the Issue to be deleted
     * @throws EVException in case an error occurred during the delete operation 
     */
    public void deleteIssue( Issue issue ) throws EVException;      

    /** 
     * Restore all PoliticalParty objects that match attributes of the model PoliticalParty.
     * @param modelPoliticalParty the model PoliticalParty; if null is provided, all PoliticalParty objects will be returned
     * @return a List of the located PoliticalParty objects
     * @throws EVException in case an error occurred during the restore operation 
     */
    public List<PoliticalParty> restorePoliticalParty( PoliticalParty modelPoliticalParty ) throws EVException;
    
    /** 
     * Store a given PoliticalParty object in the persistent data store.
     * If the PoliticalParty object to be stored is already persistent, the persistent 
     * object in the data store is updated.
     * @param politicalParty the PoliticalParty to be stored
     * @throws EVException in case an error occurred during the store operation 
     */
    public void storePoliticalParty( PoliticalParty politicalParty ) throws EVException;
    
    /** 
     * Delete a given PoliticalParty object from the persistent data store.
     * @param politicalParty the PoliticalParty to be deleted
     * @throws EVException in case an error occurred during the delete operation 
     */
    public void deletePoliticalParty( PoliticalParty politicalParty ) throws EVException;      

    /** 
     * Restore all VoteRecord objects that match attributes of the model VoteRecord.
     * @param modelVoteRecord the model VoteRecord; if null is provided, all VoteRecord objects will be returned
     * @return a List of the located VoteRecord objects
     * @throws EVException in case an error occurred during the restore operation 
     */
    public List<VoteRecord> restoreVoteRecord( VoteRecord modelVoteRecord ) throws EVException;
    
    /** 
     * Store a given VoteRecord object in the persistent data store.
     * If the VoteRecord object to be stored is already persistent, the persistent 
     * object in the data store is updated.
     * @param voteRecord the VoteRecord to be stored
     * @throws EVException in case an error occurred during the store operation 
     */
    public void storeVoteRecord( VoteRecord voteRecord ) throws EVException;
    
    /** 
     * Delete a given VoteRecord object from the persistent data store.
     * @param voteRecord the VoteRecord to be deleted
     * @throws EVException in case an error occurred during the delete operation 
     */
    public void deleteVoteRecord( VoteRecord voteRecord ) throws EVException;      



    // Associations
    //
    // Ballot--includes-->BallotItem;   multiplicity:   1  -  *
    // Candidate--isCandidateIn-->Election;   multiplicity:   1..*  -  1
    // ElectoralDistrict--hasBallot-->Ballot;   multiplicity:   1  -  *
    // Candidate--isMemberOf-->PoliticalParty;   multiplicity:   1..*  -  1
    // Voter--belongsTo-->ElectoralDistrict;   multiplicity:   1..*  -  1
    // Voter--VoteRecord-->Ballot;  multiplicity:   *  -  *
    //          it is an association class, so no traversals


    // Ballot--includes-->BallotItem;   multiplicity: 1 - *
    //
    /** 
     * Store a link between a Ballot and a BallotItem included on the Ballot.
     * @param ballot the Ballot to be linked
     * @param ballotItem the BallotItem to be linked
     * @throws EVException in case an error occurred during the store operation 
     */
    public void storeBallotIncludesBallotItem( Ballot ballot, BallotItem ballotItem ) throws EVException;

    /** 
     * Return the Ballot which includes a given BallotItem.
     * @param ballotItem the BallotItem
     * @return the Ballot which includes the BallotItem
     * @throws EVException in case an error occurred during the restore operation 
     */
    public Ballot restoreBallotIncludesBallotItem( BallotItem ballotItem ) throws EVException;

    /** 
     * Return BallotItems included on a given Ballot.
     * @param ballot the Ballot
     * @return a List with all BallotItems included on the Ballot
     * @throws EVException in case an error occurred during the restore operation 
     */
    public List<BallotItem> restoreBallotIncludesBallotItem( Ballot ballot ) throws EVException;

    /** 
     * Delete a link between a Ballot and a BallotItem included on the Ballot.
     * @param ballot the Ballot
     * @param ballotItem the BallotItem
     * @throws EVException in case an error occurred during the delete operation 
     */
    public void deleteBallotIncludesBallotItem( Ballot ballot, BallotItem ballotItem ) throws EVException;

    // Candidate--isCandidateIn-->Election;   multiplicity: 1..* - 1
    //
    /** 
     * Store a link between a Election and a Candidate included on the Election.
     * @param election the Election to be linked
     * @param candidate the Candidate to be linked
     * @throws EVException in case an error occurred during the store operation 
     */
    public void storeCandidateIsCandidateInElection( Candidate candidate, Election election ) throws EVException;

    /** 
     * Return the Election in which a given Candidate runs.
     * @param candidate the Candidate
     * @return the Election in which the Candidate runs
     * @throws EVException in case an error occurred during the restore operation 
     */
    public Election restoreCandidateIsCandidateInElection( Candidate candidate ) throws EVException;

    /** 
     * Return Candidates running in a given Election.
     * @param election the Election
     * @return a List with all Candidates running in the Election
     * @throws EVException in case an error occurred during the restore operation 
     */
    public List<Candidate> restoreCandidateIsCandidateInElection( Election election ) throws EVException;

    /** 
     * Delete a link between a Election and a Candidate included on the Election.
     * @param election the Election
     * @param candidate the Candidate
     * @throws EVException in case an error occurred during the delete operation 
     */
    public void deleteCandidateIsCandidateInElection( Candidate candidate, Election election ) throws EVException;

    // ElectoralDistrict--hasBallot-->Ballot;   multiplicity:   1  -  *
    //
    /** 
     * Store a link between an ElectoralDistrict and a Ballot which is voted on by voters in the ElectoralDistrict.
     * @param electoralDistrict the ElectoralDistrict to be linked
     * @param ballot the Ballot to be linked
     * @throws EVException in case an error occurred during the store operation 
     */
    public void storeElectoralDistrictHasBallotBallot( ElectoralDistrict electoralDistrict, Ballot ballot ) throws EVException;

    /** 
     * Return the ElectoralDistrict which has a given Ballot.
     * @param ballot the Ballot
     * @return the ElectoralDistrict which includes the Ballot
     * @throws EVException in case an error occurred during the restore operation 
     */
    public ElectoralDistrict restoreElectoralDistrictHasBallotBallot( Ballot ballot ) throws EVException;

    /** 
     * Return alll Ballots which a given ElectoralDistrict has.
     * @param electoralDistrict the ElectoralDistrict
     * @return a List with all Ballots maintained by the ElectoralDistrict
     * @throws EVException in case an error occurred during the restore operation 
     */
    public List<Ballot> restoreElectoralDistrictHasBallotBallot( ElectoralDistrict electoralDistrict ) throws EVException;

    /** 
     * Delete a link between an ElectoralDistrict and a Ballot which is voted on by voters in the ElectoralDistrict.
     * @param electoralDistrict the ElectoralDistrict
     * @param ballot the Ballot
     * @throws EVException in case an error occurred during the delete operation 
     */
    public void deleteElectoralDistrictHasBallotBallot( ElectoralDistrict electoralDistrict, Ballot ballot ) throws EVException;

    // Candidate--isMemberOf-->PoliticalParty;   multiplicity:   1..*  -  1
    /** 
     * Store a link between a Candidate and a PoliticalParty to which the Candidate belongs.
     * @param candidate the Candidate to be linked
     * @param politicalParty the PoliticalParty to be linked
     * @throws EVException in case an error occurred during the store operation 
     */
    public void storeCandidateIsMemberOfPoliticalParty( Candidate candidate, PoliticalParty politicalParty ) throws EVException;

    /** 
     * Return the PoliticalParty of a given Candidate.
     * @param candidate the Candidate
     * @return the PoliticalParty of which the Candidate is a member
     * @throws EVException in case an error occurred during the restore operation 
     */
    public PoliticalParty restoreCandidateIsMemberOfPoliticalParty( Candidate candidate ) throws EVException;

    /** 
     * Return Candidates who are members of a given PoliticalParty.
     * @param politicalParty the PoliticalParty
     * @return a List with all Candidates who are members of the PoliticalParty
     * @throws EVException in case an error occurred during the restore operation 
     */
    public List<Candidate> restoreCandidateIsMemberOfPoliticalParty( PoliticalParty politicalParty ) throws EVException;

    /** 
     * Delete a link between a Candidate and a PoliticalParty to which the Candidate belongs.
     * @param candidate the Candidate 
     * @param politicalParty the PoliticalParty
     * @throws EVException in case an error occurred during the delete operation 
     */
    public void deleteCandidateIsMemberOfElection( Candidate candidate, PoliticalParty politicalParty ) throws EVException;

    /** 
     * Store a link between a Voter and a ElectoralDistrict to which the Voter belongs.
     * @param voter the Voter to be linked
     * @param electoralDistrict the ElectoralDistrict to be linked
     * @throws EVException in case an error occurred during the store operation 
     */
    public void storeVoterBelongsToElectoralDistrict( Voter voter, ElectoralDistrict electoralDistrict ) throws EVException;

    /** 
     * Return the ElectoralDistrict of a given Voter.
     * @param voter the Voter
     * @return the ElectoralDistrict to which the Voter belongs
     * @throws EVException in case an error occurred during the restore operation 
     */
    public ElectoralDistrict restoreVoterBelongsToElectoralDistrict( Voter voter ) throws EVException;

    /** 
     * Return Voters who belong to a given ElectoralDistrict.
     * @param electoralDistrict the ElectoralDistrict
     * @return a List with all Voters who are members of the ElectoralDistrict
     * @throws EVException in case an error occurred during the restore operation 
     */
    public List<Voter> restoreVoterBelongsToElectoralDistrict( ElectoralDistrict electoralDistrict ) throws EVException;

    /** 
     * Delete a link between a Voter and a ElectoralDistrict to which the Voter belongs.
     * @param voter the Voter 
     * @param electoralDistrict the ElectoralDistrict
     * @throws EVException in case an error occurred during the delete operation 
     */
    public void deleteVoterBelongsToElectoralDistrict( Voter voter, ElectoralDistrict electoralDistrict ) throws EVException;
    public int getBallotItemID(BallotItem bItem) throws EVException;
}
