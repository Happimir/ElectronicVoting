package edu.uga.cs.evote.persistence.impl;

import edu.uga.cs.evote.persistence.Persistable;
import edu.uga.cs.evote.persistence.PersistenceLayer;

/**
 * @author Michael Kovalsky
 * Date: 10/20/16.
 */
public abstract class Persistent implements Persistable {

    private long id;
    
    private PersistenceLayer persistenceLayer;
    
    public void setPersistenceLayer(PersistenceLayer pLayer) {
    	this.persistenceLayer = pLayer;
    }
    
    public PersistenceLayer getPersistenceLayer() {
    	return this.persistenceLayer;
    }

    public Persistent() {
        this.id = -1;
    }

    public Persistent(long id) {
        this.id = id;
    }

    /**
     * Access the persistent identifier of an entity object instance. The value of -1 indicates that
     * the object has not been stored in the persistent data store, yet.
     *
     * @return persistent identifier of an entity object instance.
     */
    public long getId() {

        return this.id;
    }

    /**
     * Set the persistent identifier for this entity object.  This method is typically used by the persistence
     * subsystem when creating proxy objects for entity object already residing in the persistent data store.
     *
     * @param id the persistent object key
     */
    public void setId( long id ) {
        this.id = id;

    }

    /**
     * Check if this entity object has been stored in the the persistent data store (for the first time).
     * Note that the value is isPersistent() may be true, even though the entity object may need to be saved
     * in the persistent data store again, after an update to its state.
     *
     * @return true if this entity object has already been stored in the persistent data store.
     */
    public boolean isPersistent() {

        return id >= 0;
    }
}
