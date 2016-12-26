package edu.uga.cs.evote.object;

import java.util.Date;
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
import edu.uga.cs.evote.persistence.PersistenceLayer;





/**
 * This is the interface to the Object Layer subsystem of the eVote system. The interface provides operations
 * realizing the life-cycle of both <i>entity objects</i> and <i>links</i> interconnecting them.
 * <p>
 * For each entity <code><i>Class</i></code>, there are four operations: <code>create<i>Class</i></code>, 
 * <code>find<i>Class</i></code>, <code>store<i>Class</i></code>, and <code>delete<i>Class</i></code>.
 * <p>
 * The <code>create<i>Class</i></code> operations are factory method-type operations to create 
 * new entity instances. There are two versions of this operation: one with arguments, which give 
 * the initial attribute values, and one without (argument-less). 
 * The one with arguments is used to create a new object instance which has not been persisted yet.  
 * Such an instance must be stored in the persistent data store before the use case terminates.
 * The version with no arguments creates an uninitialized object and should be used as the
 * first step when restoring the persistent object from the persistent data store (suitable
 * setter methods should be used to set the attribute values, as retrieved from the persistent
 * data store. 
 * <p>
 * Furthermore, the <code>create<i>Class</i></code> operations for classes participating in associations with 
 * other classes, and where the multiplicity on the other end of the association is one, have parameters
 * to provide the linked objects on the other end of the association.  Note, that a class participating in
 * an association with a multiplicity of one with another class cannot exist without a link to one other
 * object on the other end.  Therefore, such a link must be established at the time an object is created.
 * <p>
 * The <code>find<i>Class</i></code> operations locate objects satisfying given search criteria and return a List
 * with the located object instances.  Each of these operations accepts a single <code>model<i>Class</i></code> argument.
 * If the argument provided on a call to one of these methods is null, it indicates that all objects
 * of a given class should be returned (the search criteria are empty, and so all objects satisfy the
 * search criteria).  On the other hand, a given <code>model<i>Class</i></code> argument may have all, or just some 
 * of the class attributes provided.  In this case, the <code>find<i>Class</i></code> operation will return only the
 * matching objects, i.e., the objects with the exact same values for all of the provided
 * attribute values in the model object.  More specifically: 
 * <ul>
 *   <li>if the <code>model<i>Class</i></code> argument is null, all objects of the given class are returned;</li>
 *   <li>if the persistent identifier attribute is provided, only one object is returned;</li>
 *   <li>if some (or even all) of the attributes are provided, only objects with the same attribute values are returned.</li>
 * </ul>
 * <p>
 * The <code>store<i>Class</i></code> operations store the argument object instance in the persistent data store.  If an object
 * being stored is already persistent (<code>isPersistent()</code> returns <code>true</code>), the object is updated.  
 * Otherwise, the object is stored in the persistent data store for the first time and its persistent object identifier is set.
 * <p>
 * The <code>delete<i>Class</i></code> operations remove the argument object instance.  While the argument (proxy) object may
 * still exist in the JVM that created the proxy, but the persistent object will be deleted and the subsequent
 * findClas operation call with the same persistent identifier should return an empty List. 
 */
public interface ObjectLayer
{ 
	
    /**
     * Create a new ElectionsOfficer object, given the set of initial attribute values.
     * @param firstName the first name
     * @param lastName the last name
     * @param userName the user name (login name)
     * @param password the password
     * @param emailAddress the email address
     * @param address the address
     * @return a new ElectionsOfficer object instance with the given attribute values
     * @throws EVException in case either firstName, lastName, or userName is null
     */
    public ElectionsOfficer createElectionsOfficer( String firstName, String lastName, String userName, 
                                                    String password, String emailAddress, String address ) throws EVException;

    /**
     * Create a new ElectionsOfficer object with undefined attribute values.
     * @return a new ElectionsOfficer object instance
     */
    public ElectionsOfficer createElectionsOfficer(); 
    
    /**
     * Return a List of ElectionsOfficer objects satisfying the search criteria given in the modelElectionsOfficer object.
     * @param modelElectionsOfficer a model ElectionsOfficer object specifying the search criteria
     * @return a List of the located ElectionsOfficer objects
     * @throws EVException in case there is a problem with the retrieval of the requested objects
     */
    public List<ElectionsOfficer> findElectionsOfficer( ElectionsOfficer modelElectionsOfficer ) throws EVException;
    
    /**
     * Store a given ElectionsOfficer object in persistent data store.
     * @param electionsOfficer the object to be persisted
     * @throws EVException in case there was an error while persisting the object
     */
    public void storeElectionsOfficer( ElectionsOfficer electionsOfficer ) throws EVException;
    
    /**
     * Delete this ElectionsOfficer object.
     * @param electionsOfficer the object to be deleted.
     * @throws EVException in case there is a problem with the deletion of the object
     */
    public void deleteElectionsOfficer( ElectionsOfficer electionsOfficer ) throws EVException; 
    
    /**
     * Create a new Voter object, given the set of initial attribute values.
     * @param firstName the first name
     * @param lastName the last name
     * @param userName the user (login) name
     * @param password the password
     * @param emailAddress the email address
     * @param address the Voter's address
     * @param age the Voter's age
     * @return a new Voter object instance with the given attribute values
     * @throws EVException in case any of the String parameters is null or if age is not positive
     */
    public Voter createVoter( String firstName, String lastName, String userName, String password, 
            String emailAddress, String address, int age ) throws EVException;

    /**
     * Create a new Voter object with undefined attribute values.
     * @return a new Voter object instance
     */
    public Voter createVoter(); 
    
    /**
     * Return a List of Voter objects satisfying the search criteria given in the modelVoter object.
     * @param modelVoter a model Voter object specifying the search criteria
     * @return a List of the located Voter objects
     * @throws EVException in case there is a problem with the retrieval of the requested objects
     */
    public List<Voter> findVoter( Voter modelVoter ) throws EVException;
    
    /**
     * Store a given Voter object in persistent data store.
     * @param voter the object to be persisted
     * @throws EVException in case there was an error while persisting the object
     */
    public void storeVoter( Voter voter ) throws EVException;
    
    /**
     * Delete this Voter object.
     * @param voter the object to be deleted.
     * @throws EVException in case there is a problem with the deletion of the object
     */
    public void deleteVoter( Voter voter ) throws EVException;       
 
    /**
     * Create a new PoliticalParty object, given the set of initial attribute values.
     * @param name the name of the political party
     * @return a new PoliticalParty object instance with the given attribute values
     * @throws EVException in case name is null
     */
    public PoliticalParty createPoliticalParty( String name ) throws EVException;

    /**
     * Create a new PoliticalParty object with undefined attribute values.
     * @return a new PoliticalParty object instance
     */
    public PoliticalParty createPoliticalParty();

    /**
     * Return a List of PoliticalParty objects satisfying the search criteria given in the modelPoliticalParty object.
     * @param modelPoliticalParty a model PoliticalParty object specifying the search criteria
     * @return a List of the located PoliticalParty objects
     * @throws EVException in case there is a problem with the retrieval of the requested objects
     */
    public List<PoliticalParty> findPoliticalParty( PoliticalParty modelPoliticalParty ) throws EVException;
    
    /**
     * Store a given PoliticalParty object in persistent data store.
     * @param politicalParty the object to be persisted
     * @throws EVException in case there was an error while persisting the object
     */
    public void storePoliticalParty( PoliticalParty politicalParty ) throws EVException;
    
    /**
     * Delete this PoliticalParty object.
     * @param politicalParty the object to be deleted.
     * @throws EVException in case there is a problem with the deletion of the object
     */
    public void deletePoliticalParty( PoliticalParty politicalParty ) throws EVException;    

    /**
     * Create a new ElectoralDistrict object, given the set of initial attribute values.
     * @param name the name of the political party
     * @return a new ElectoralDistrict object instance with the given attribute values
     * @throws EVException in case name is null
     */
    public ElectoralDistrict createElectoralDistrict( String name ) throws EVException;

    /**
     * Create a new ElectoralDistrict object with undefined attribute values.
     * @return a new ElectoralDistrict object instance
     */
    public ElectoralDistrict createElectoralDistrict();

    /**
     * Return a List of ElectoralDistrict objects satisfying the search criteria given in the modelElectoralDistrict object.
     * @param modelElectoralDistrict a model ElectoralDistrict object specifying the search criteria
     * @return a List of the located ElectoralDistrict objects
     * @throws EVException in case there is a problem with the retrieval of the requested objects
     */
    public List<ElectoralDistrict> findElectoralDistrict( ElectoralDistrict modelElectoralDistrict ) throws EVException;
    
    /**
     * Store a given ElectoralDistrict object in persistent data store.
     * @param electoralDistrict the object to be persisted
     * @throws EVException in case there was an error while persisting the object
     */
    public void storeElectoralDistrict( ElectoralDistrict electoralDistrict ) throws EVException;
    
    /**
     * Delete this ElectoralDistrict object.
     * @param electoralDistrict the object to be deleted.
     * @throws EVException in case there is a problem with the deletion of the object
     */
    public void deleteElectoralDistrict( ElectoralDistrict electoralDistrict ) throws EVException;    

    /**
     * Create a new Ballot object, given the set of initial attribute values.
     * @param openDate the date when the ballot should be open for voting
     * @param closeDate the date when the ballot should be closed for voting
     * @param approved indication if the ballot has been approved
     * @param electoralDistrict the electoral district of this ballot
     * @return a new Ballot object instance with the given attribute values
     * @throws EVException in case any of the arguments are null or if the electoralDistrict is not persistent
     */
    public Ballot createBallot( Date openDate, Date closeDate, boolean approved, ElectoralDistrict electoralDistrict, boolean isOpen ) throws EVException;

    /**
     * Create a new Ballot object with undefined attribute values.
     * @return a new Ballot object instance
     */
    public Ballot createBallot();

    /**
     * Return a List of Ballot objects satisfying the search criteria given in the modelBallot object.
     * @param modelBallot a model Ballot object specifying the search criteria
     * @return a List of the located Ballot objects
     * @throws EVException in case there is a problem with the retrieval of the requested objects
     */
    public List<Ballot> findBallot( Ballot modelBallot ) throws EVException;
    
    /**
     * Store a given Ballot object in persistent data store.
     * @param ballot the object to be persisted
     * @throws EVException in case there was an error while persisting the object
     */
    public void storeBallot( Ballot ballot ) throws EVException;
    
    /**
     * Delete this Ballot object.
     * @param ballot the object to be deleted.
     * @throws EVException in case there is a problem with the deletion of the object
     */
    public void deleteBallot( Ballot ballot ) throws EVException;    

    /**
     * Create a new Candidate object, given the set of initial attribute values.
     * @param name the name of the candidate
     * @param politicalParty the political party the candidate belongs to; it may be null for non partisan elections
     * @param election the election for which this candidate is running
     * @return a new Candidate object instance with the given attribute values
     * @throws EVException in case either the name or the politicalParty are null
     */
    public Candidate createCandidate( String name, PoliticalParty politicalParty, Election election ) throws EVException;

    /**
     * Create a new Candidate object with undefined attribute values.
     * @return a new Candidate object instance
     */
    public Candidate createCandidate();

    /**
     * Return a List of Candidate objects satisfying the search criteria given in the modelCandidate object.
     * @param modelCandidate a model Candidate object specifying the search criteria
     * @return a List of the located Candidate objects
     * @throws EVException in case there is a problem with the retrieval of the requested objects
     */
    public List<Candidate> findCandidate( Candidate modelCandidate ) throws EVException;
    
    /**
     * Store a given Candidate object in persistent data store.
     * @param candidate the object to be persisted
     * @throws EVException in case there was an error while persisting the object
     */
    public void storeCandidate( Candidate candidate ) throws EVException;
    
    /**
     * Delete this Candidate object.
     * @param candidate the object to be deleted.
     * @throws EVException in case there is a problem with the deletion of the object
     */
    public void deleteCandidate( Candidate candidate ) throws EVException; 

    /**
     * Create a new Issue object, given the set of initial attribute values.
     * @param question the question of the this issue
     * @return a new Issue object instance with the given attribute value
     * @throws EVException in case question is null
     */
    public Issue createIssue( String question, Ballot ballot ) throws EVException;

    /**
     * Create a new Issue object with undefined attribute values.
     * @return a new Issue object instance
     */
    public Issue createIssue();

    /**
     * Return a List of Issue objects satisfying the search criteria given in the modelIssue object.
     * @param modelIssue a model Issue object specifying the search criteria
     * @return a List of the located Issue objects
     * @throws EVException in case there is a problem with the retrieval of the requested objects
     */
    public List<Issue> findIssue( Issue modelIssue ) throws EVException;
    
    /**
     * Store a given Issue object in persistent data store.
     * @param issue the object to be persisted
     * @throws EVException in case there was an error while persisting the object
     */
    public void storeIssue( Issue issue ) throws EVException;
    
    /**
     * Delete this Issue object.
     * @param issue the object to be deleted.
     * @throws EVException in case there is a problem with the deletion of the object
     */
    public void deleteIssue( Issue issue ) throws EVException;    

    /**
     * Create a new Election object, given the set of initial attribute values.
     * @param office the office this Election is for
     * @param isPartisan indication if this Election is partisan
     * @return a new Election object instance with the given attribute value
     * @throws EVException in case question is null
     */
    public Election createElection( String office, boolean isPartisan, Ballot ballot) throws EVException;

    /**
     * Create a new Election object with undefined attribute values.
     * @return a new Election object instance
     */
    public Election createElection();

    /**
     * Return a List of Election objects satisfying the search criteria given in the modelElection object.
     * @param modelElection a model Election object specifying the search criteria
     * @return a List of the located Election objects
     * @throws EVException in case there is a problem with the retrieval of the requested objects
     */
    public List<Election> findElection( Election modelElection ) throws EVException;
    
    /**
     * Store a given Election object in persistent data store.
     * @param election the object to be persisted
     * @throws EVException in case there was an error while persisting the object
     */
    public void storeElection( Election election ) throws EVException;
    
    /**
     * Delete this Election object.
     * @param election the object to be deleted.
     * @throws EVException in case there is a problem with the deletion of the object
     */
    public void deleteElection( Election election ) throws EVException; 
    
    /**
     * Create a new VoteRecord object, given the set of initial attribute values.
     * @param voter the Voter who cast a vote
     * @param ballot the Ballot for which a vote has been cast
     * @param date the Date when the vote has been cast
     * @return a new VoteRecord object instance with the given attribute value
     * @throws EVException in case either of the arguments is null
     */
    public VoteRecord createVoteRecord( Voter voter, BallotItem ballotItem, Date date ) throws EVException;

    /**
     * Create a new VoteRecord object with undefined attribute values.
     * @return a new VoteRecord object instance
     */
    public VoteRecord createVoteRecord();

    /**
     * Return a List of VoteRecord objects satisfying the search criteria given in the modelVoteRecord object.
     * @param modelVoteRecord a model VoteRecord object specifying the search criteria
     * @return a List of the located VoteRecord objects
     * @throws EVException in case there is a problem with the retrieval of the requested objects
     */
    public List<VoteRecord> findVoteRecord( VoteRecord modelVoteRecord ) throws EVException;
    
    /**
     * Store a given VoteRecord object in persistent data store.
     * @param voteRecord the object to be persisted
     * @throws EVException in case there was an error while persisting the object
     */
    public void storeVoteRecord( VoteRecord voteRecord ) throws EVException;
    
    /**
     * Delete this VoteRecord object.
     * @param voteRecord the object to be deleted.
     * @throws EVException in case there is a problem with the deletion of the object
     */
    public void deleteVoteRecord( VoteRecord voteRecord ) throws EVException;

	public void setPersistence(PersistenceLayer persistence);
 
}
