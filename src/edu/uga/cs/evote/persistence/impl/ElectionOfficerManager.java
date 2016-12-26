package edu.uga.cs.evote.persistence.impl;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.PreparedStatement;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.entity.ElectionsOfficer;
import edu.uga.cs.evote.entity.Election;

class ElectionOfficerManager
{
	private ObjectLayer objectLayer = null;
    private Connection  conn = null;
    
    public ElectionOfficerManager( Connection conn, ObjectLayer objectLayer )
    {
        this.conn = conn;
        this.objectLayer = objectLayer;
        
    }
    
    public void store( ElectionsOfficer electionOfficer ) 
            throws EVException
    {
        String               insertElectionOfficerSql = "insert into officer ( username, userpass, email, firstname, lastname, address) values ( ?, ?, ?, ?, ?, ?)";              
        String               updateElectionOfficerSql = "update officer  set username = ?, userpass = ?, email = ?, firstname = ?, lastname = ?, address = ? where id = ?";              
        PreparedStatement    stmt;
        int                  inscnt;
        long                 electionOfficerId;
        
        try {
            
            if( !electionOfficer.isPersistent() )
                stmt = (PreparedStatement) conn.prepareStatement( insertElectionOfficerSql );
            else
                stmt = (PreparedStatement) conn.prepareStatement( updateElectionOfficerSql );

            if( electionOfficer.getUserName() != null ) // evoteuser is unique, so it is sufficient to get a electionOfficer
                stmt.setString( 1, electionOfficer.getUserName() );
            else 
                throw new EVException( "ElectionOfficerManager.save: can't save a ElectionOfficer: userName undefined" );

            if( electionOfficer.getPassword() != null )
                stmt.setString( 2, electionOfficer.getPassword() );
            else
                throw new EVException( "ElectionOfficerManager.save: can't save a ElectionOfficer: password undefined" );

            if( electionOfficer.getEmailAddress() != null )
                stmt.setString( 3,  electionOfficer.getEmailAddress() );
            else
                throw new EVException( "ElectionOfficerManager.save: can't save a ElectionOfficer: email undefined" );

            if( electionOfficer.getFirstName() != null )
                stmt.setString( 4, electionOfficer.getFirstName() );
            else
                throw new EVException( "ElectionOfficerManager.save: can't save a ElectionOfficer: first name undefined" );

            if( electionOfficer.getLastName() != null )
                stmt.setString( 5, electionOfficer.getLastName() );
            else
                throw new EVException( "ElectionOfficerManager.save: can't save a ElectionOfficer: last name undefined" );

            if( electionOfficer.getAddress() != null )
                stmt.setString( 6, electionOfficer.getAddress() );
            else
                stmt.setNull(6, java.sql.Types.VARCHAR);
            
            if( electionOfficer.isPersistent() )
                stmt.setLong( 7, electionOfficer.getId() );

            inscnt = stmt.executeUpdate();

            if( !electionOfficer.isPersistent() ) {
                // in case this this object is stored for the first time,
                // we need to establish its persistent identifier (primary key)
                if( inscnt == 1 ) {
                    String sql = "select last_insert_id()";
                    if( stmt.execute( sql ) ) { // statement returned a result
                        // retrieve the result
                        ResultSet r = stmt.getResultSet();
                        // we will use only the first row!
                        while( r.next() ) {
                            // retrieve the last insert auto_increment value
                            electionOfficerId = r.getLong( 1 );
                            if( electionOfficerId > 0 )
                                electionOfficer.setId( electionOfficerId ); // set this electionOfficer's db id (proxy object)
                        }
                    }
                }
            }
            else {
                if( inscnt < 1 )
                    throw new EVException( "ElectionOfficerManager.save: failed to save a ElectionOfficer" ); 
            }
        }
        catch( SQLException e ) {
            e.printStackTrace();
            throw new EVException( "ElectionOfficerManager.save: failed to save a ElectionOfficer: " + e );
        }
    }

    public List<ElectionsOfficer> restore( ElectionsOfficer modelElectionOfficer ) 
            throws EVException
    {
        String       selectElectionOfficerSql = "select id, username, userpass, email, firstname, lastname, address from officer";
        Statement    stmt = null;
        StringBuffer query = new StringBuffer( 100 );
        StringBuffer condition = new StringBuffer( 100 );
        List<ElectionsOfficer> electionOfficers = new ArrayList<ElectionsOfficer>();

        condition.setLength( 0 );
        
        // form the query based on the given ElectionOfficer object instance
        query.append( selectElectionOfficerSql );
        
        if( modelElectionOfficer != null ) {
            if( modelElectionOfficer.getId() >= 0 ) // id is unique, so it is sufficient to get a electionOfficer
                condition.append( " where id = " + modelElectionOfficer.getId() );
         
            if( modelElectionOfficer.getUserName() != null ){ // userName is unique, so it is sufficient to get a electionOfficer
            	if( condition.length() > 0 )
                    condition.append( " and" );
                else
                    condition.append( " where" );
                condition.append( " username = '" + modelElectionOfficer.getUserName() + "'" );
            }
           if( modelElectionOfficer.getPassword() != null ){
        	   if( condition.length() > 0 )
                   condition.append( " and" );
               else
                   condition.append( " where" );
        	   condition.append( " userpass = '" + modelElectionOfficer.getPassword() + "'" );
           }

                if( modelElectionOfficer.getEmailAddress() != null ) {
                    if( condition.length() > 0 )
                        condition.append( " and" );
                    else
                        condition.append( " where" );
                    condition.append( " email = '" + modelElectionOfficer.getEmailAddress() + "'" );
                }

                if( modelElectionOfficer.getFirstName() != null ) {
                    if( condition.length() > 0 )
                        condition.append( " and" );
                    else
                        condition.append( " where" );
                    condition.append( " firstName = '" + modelElectionOfficer.getFirstName() + "'" );
                }

                if( modelElectionOfficer.getLastName() != null ) {
                    if( condition.length() > 0 )
                        condition.append( " and" );
                    else
                        condition.append( " where" );
                    condition.append( " lastName = '" + modelElectionOfficer.getLastName() + "'" );
                }

                if( modelElectionOfficer.getAddress() != null ) {
                    if( condition.length() > 0 )
                        condition.append( " and" );
                    else
                        condition.append( " where" );
                    condition.append( " address = '" + modelElectionOfficer.getAddress() + "'" );
                }        

            query.append(condition);
        }
        
        try {

            stmt = conn.createStatement();

            // retrieve the persistent ElectionOfficer objects
            //
            if( stmt.execute( query.toString() ) ) { // statement returned a result
                ResultSet rs = stmt.getResultSet();
                long   id;
                String userName;
                String password;
                String email;
                String firstName;
                String lastName;
                String address;
                
                while( rs.next() ) {

                    id = rs.getLong( 1 );
                    userName = rs.getString( 2 );
                    password = rs.getString( 3 );
                    email = rs.getString( 4 );
                    firstName = rs.getString( 5 );
                    lastName = rs.getString( 6 );
                    address = rs.getString( 7 );

                    ElectionsOfficer electionOfficer = objectLayer.createElectionsOfficer( userName, password, email, firstName, lastName, address);
                    electionOfficer.setId( id );

                    electionOfficers.add( electionOfficer );

                }
                
                return electionOfficers;
            }
        }
        catch( Exception e ) {      // just in case...
            throw new EVException( "ElectionOfficerManager.restore: Could not restore persistent ElectionOfficer object; Root cause: " + e );
        }
        
        // if we get to this point, it's an error
        throw new EVException( "ElectionOfficerManager.restore: Could not restore persistent ElectionOfficer objects" );
    }
    
    public void delete( ElectionsOfficer electionOfficer ) 
            throws EVException
    {
        String               deleteElectionOfficerSql = "delete from officer where id = ?";              
        PreparedStatement    stmt = null;
        int                  inscnt;
        
        // form the query based on the given ElectionOfficer object instance
        if( !electionOfficer.isPersistent() ) // is the ElectionOfficer object persistent?  If not, nothing to actually delete
            return;
        
        try {
            
            //DELETE t1, t2 FROM t1, t2 WHERE t1.id = t2.id;
            //DELETE FROM t1, t2 USING t1, t2 WHERE t1.id = t2.id;
            stmt = (PreparedStatement) conn.prepareStatement( deleteElectionOfficerSql );
            
            stmt.setLong( 1, electionOfficer.getId() );
            
            inscnt = stmt.executeUpdate();
            
            if( inscnt == 0 ) {
                throw new EVException( "ElectionOfficerManager.delete: failed to delete this ElectionOfficer" );
            }
        }
        catch( SQLException e ) {
            throw new EVException( "ElectionOfficerManager.delete: failed to delete this ElectionOfficer: " + e.getMessage() );
        }
    }
}