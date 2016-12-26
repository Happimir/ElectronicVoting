package edu.uga.cs.evote.entity;

import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.persistence.Persistable;

public interface ElectoralDistrict 
    extends Persistable
{
    /** Return the name of this electoral district.
     * @return the name of this electoral district
     */
    public String getName();
    
    /** Set the new name of this electoral district.
     * @param name the new name of this electoral district
     */
    public void setName( String name );
    
    /** Return a list of the voters in this electoral district.
     * @return a list of the voters in this electoral district
     * @throws EVException in case there is a problem with traversing links to the requested objects
     */
    public List<Voter> getVoters() throws EVException;
        
    /** Return a list of the ballots created for this electoral district.
     * @return a list of the ballots created for this electoral district
     * @throws EVException in case there is a problem with traversing links to the requested objects
     */
    public List<Ballot> getBallots() throws EVException;
    
    /** Add a new ballot for this electoral district.
     * @param ballot the new ballot for this electoral district
     * @throws EVException in case there is a problem with creating a link to the requested objects
    */
    public void addBallot( Ballot ballot ) throws EVException;
    
    /** Remove a ballot from this electoral district.
     * @param ballot the ballot to be removed from this electoral district
     * @throws EVException in case there is a problem with deleting a link to the requested objects
     */
    public void deleteBallot( Ballot ballot ) throws EVException;
}
