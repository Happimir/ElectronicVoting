package edu.uga.cs.evote.entity.impl;

import edu.uga.cs.evote.entity.ElectionsOfficer;

/**
 * This class represents an Administrator user.  It has no additional attributes; only the ones inherited from User.
 *
 * @author Michael Kovalsky
 * Date: 10/23/16.
 */
public class ElectionsOfficerImpl extends UserImpl implements ElectionsOfficer {

	

    /**
     * Default Constructor
     */
    public ElectionsOfficerImpl() {
        super();
    }

    /**
     * Parametrized Constructor
     *
     * @param userName String
     * @param userPass String
     * @param email String
     * @param firstName String
     * @param lastName String
     * @param address String
     */
    public ElectionsOfficerImpl(String userName, String userPass, String email, String firstName, String lastName, String address) {
        super(userName, userPass, email, firstName, lastName, address);
    }

}
