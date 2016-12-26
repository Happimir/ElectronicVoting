package edu.uga.cs.evote.entity;

import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.persistence.Persistable;

public interface PoliticalParty 
    extends Persistable
{
    /** Return the name of this political party.
     * @return the name of the political party
     */
    public String getName();
    
    /** Set the new name of this political party.
     * @param name the new name of the political party
     */
    public void setName( String name );
    
    /** Return a list of the candidates belonging to this political party.
     * @return a list of this political party's candidates
     * @throws EVException in case there is a problem with traversing links to the requested objects
     */
    public List<Candidate> getCandidates() throws EVException;
}
