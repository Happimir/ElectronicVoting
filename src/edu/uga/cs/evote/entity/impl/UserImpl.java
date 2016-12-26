package edu.uga.cs.evote.entity.impl;

import edu.uga.cs.evote.entity.User;
import edu.uga.cs.evote.persistence.impl.Persistent;

/**
 * @author michael kovalsky
 * Date: 10/23/16.
 */
public abstract class UserImpl extends Persistent implements User {

    private String firstName;
    private String lastName;
    private String userName;
    private String userPass;
    private String email;
    private String address;

    /**
     * Default Constructor
     */
    public UserImpl() {
        super(-1);
        this.firstName = null;
        this.lastName = null;
        this.userName = null;
        this.userPass = null;
        this.email = null;
        this.address = null;
    }

    /**
     * Parametrized Constructor
     *
     * @param firstName String
     * @param lastName String
     * @param userName String
     * @param password String
     * @param email String
     * @param address String
     */
    public UserImpl(String userName, String userPass, String email, String firstName, String lastName, String address) {
        super(-1);
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.userPass = userPass;
        this.email = email;
        this.address = address;
    }

    /** Return the user's first name.
     * @return the user's first name
     */
    public String getFirstName() {

        return this.firstName;
    }

    /** Set the user's first name.
     * @param firstName the new first name of this user
     */
    public void setFirstName( String firstName ) {

        this.firstName = firstName;
    }

    /** Return the user's last name.
     * @return the user's last name
     */
    public String getLastName() {

        return this.lastName;
    }

    /** Set the user's first name.
     * @param lastName the new last name of this user
     */
    public void setLastName( String lastName ) {

        this.lastName = lastName;
    }

    /** Return the user's user name (login name).
     * @return the user's user name (login name)
     */
    public String getUserName() {

        return this.userName;
    }

    /** Set the user's user name (login name).
     * @param userName the new user (login name)
     */
    public void   setUserName( String userName ) {

        this.userName = userName;
    }

    /** Return the user's password.
     * @return the user's password
     */
    public String getPassword() {
        return this.userPass;
    }

    /** Set the user's password.
     * @param password the new password
     */
    public void setPassword( String password ) {
        this.userPass = password;
    }

    /** Return the user's email address.
     * @return the user's email address
     */
    public String getEmailAddress() {
        return this.email;
    }

    /** Set the user's email address.
     * @param emailAddress the new email address
     */
    public void  setEmailAddress( String emailAddress ) {
        this.email = emailAddress;
    }

    /** Return the user's address.
     * @return the user's address
     */
    public String getAddress() {
        return this.address;
    }

    /** Set the user's address.
     * @param address the new address
     */
    public void setAddress( String address ) {
        this.address = address;
    }

}
