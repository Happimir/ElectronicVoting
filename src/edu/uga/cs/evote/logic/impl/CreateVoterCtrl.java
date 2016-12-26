package edu.uga.cs.evote.logic.impl;

import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.ElectoralDistrict;
import edu.uga.cs.evote.entity.Voter;
import edu.uga.cs.evote.object.ObjectLayer;

public class CreateVoterCtrl {
	 private ObjectLayer objectLayer = null;
	    
	    public CreateVoterCtrl( ObjectLayer objectModel )
	    {
	        this.objectLayer = objectModel;
	    }
	    
	    public long createVoter( String userName, String password, String email, String firstName, 
	                              String lastName, String address, String district2, int age )
	          throws EVException
	    { 
	        Voter voter = null;
	        Voter modelVoter = null;
	        List<Voter> voters = null;

	        // check if the userName already exists
	        modelVoter = objectLayer.createVoter();
	        modelVoter.setUserName( userName );
	        voters = objectLayer.findVoter( modelVoter );
	        if( voters.size() > 0 )
	            voter = voters.get( 0 );
	        
	        
	        // check if the person actually exists, and if so, throw an exception
	        if( voter != null )
	            throw new EVException( "A voter with this user name already exists" );
	        
	        
	        
	        voter = objectLayer.createVoter( userName, password, email, firstName, lastName, address, age);
	        
	        ElectoralDistrict district = null;
	        ElectoralDistrict modelDistrict = null;
	        List<ElectoralDistrict> districts = null;	        
	      
	        
	        modelDistrict = objectLayer.createElectoralDistrict();
	        modelDistrict.setName(district2);
	        
	        districts = objectLayer.findElectoralDistrict(modelDistrict);
	        if( districts.size() > 0 )
	            district = districts.get( 0 );
	        
	        if(district == null)
	            throw new EVException( "District does not exist" );
	        
	        voter.setElectoralDistrict(district);
	   
	        
	        objectLayer.storeVoter( voter );

	        return voter.getId();
	    }
}
