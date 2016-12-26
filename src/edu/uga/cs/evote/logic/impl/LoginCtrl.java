package edu.uga.cs.evote.logic.impl;

import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.ElectionsOfficer;
import edu.uga.cs.evote.entity.User;
import edu.uga.cs.evote.entity.Voter;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.session.Session;
import edu.uga.cs.evote.session.SessionManager;



public class LoginCtrl
{ 
    private ObjectLayer objectLayer = null;
    
    public LoginCtrl( ObjectLayer objectLayer )
    {
        this.objectLayer = objectLayer;
    }
    
    public String login( Session session, boolean isVoter, String userName, String password )
            throws EVException
    {
        String ssid = null;
        if(isVoter){
        
	        Voter modelVoter = objectLayer.createVoter();
	        modelVoter.setUserName( userName );
	        modelVoter.setPassword( password );
	        List<Voter> voters = objectLayer.findVoter(modelVoter);
	        if( voters.size() > 0 ) {
	            User user = voters.get( 0 );
	            session.setUser( user);
	            ssid = SessionManager.storeSession( session );
	            System.out.println("Voter Login Successful");
	        }
	        else
	            throw new EVException( "SessionManager.login: Invalid User Name or Password" );
        }
        else{
        	ElectionsOfficer modelOfficer = objectLayer.createElectionsOfficer();
	        modelOfficer.setUserName( userName );
	        modelOfficer.setPassword( password );
	        List<ElectionsOfficer> officers = objectLayer.findElectionsOfficer(modelOfficer);
	        if( officers.size() > 0 ) {
	            User user = officers.get( 0 );
	            session.setUser( user );
	            ssid = SessionManager.storeSession( session );
	            System.out.println("Election Officer Login Successful");
	        }
	        else
	            throw new EVException( "SessionManager.login: Invalid User Name or Password" );
        }
        
        return ssid;
    }
}
