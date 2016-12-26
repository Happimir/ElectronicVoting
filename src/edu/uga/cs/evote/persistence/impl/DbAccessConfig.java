package edu.uga.cs.evote.persistence.impl;


public abstract class DbAccessConfig
{
    /** The fully qualified name of the JDBC driver.
     */
    static final String DB_DRIVE_NAME  = "com.mysql.jdbc.Driver";
    
    /** The database name
     */
    static final String DB_NAME        = "team11";
    
    /** The database server name for the connection pool
     */
    static final String DB_SERVER_NAME = "localhost";

    /** The JDBC connection string/URL.
     */
    static final String DB_CONNECTION_URL = "jdbc:mysql://uml.cs.uga.edu:3306/team11";

    /** The database user name.
     */
    static  String DB_CONNECTION_USERNAME = "team11";

    /** The password for the database user.
     */
    static  String DB_CONNECTION_PWD = "virtual";

}
