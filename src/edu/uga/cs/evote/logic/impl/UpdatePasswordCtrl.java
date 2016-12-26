package edu.uga.cs.evote.logic.impl;

import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.ElectionsOfficer;
import edu.uga.cs.evote.entity.ElectoralDistrict;
import edu.uga.cs.evote.entity.Voter;
import edu.uga.cs.evote.object.ObjectLayer;

public class UpdatePasswordCtrl {
	ObjectLayer objectLayer = null;
	public UpdatePasswordCtrl(ObjectLayer layer) {
		this.objectLayer = layer;
	}


	public long updatePassword(String userType, String username, String old_password, String new_password)
			throws EVException
	{ 
		if(userType.equals("voter")){
			Voter voter = null;
			Voter modelVoter = null;
			List<Voter> voters = null;

			// check if the password already exists
			modelVoter = objectLayer.createVoter();
			modelVoter.setUserName(username);
			voters = objectLayer.findVoter(modelVoter);
			if( voters.size() > 0 )
				voter = voters.get( 0 );

			// check if the person actually exists, and if so, throw an exception
			if( voter == null )
				throw new EVException( "Voter with this username does not exist" );

			voter.setPassword(new_password);
			objectLayer.storeVoter( voter );

			return voter.getId();
		}else{
			ElectionsOfficer officer = null;
			ElectionsOfficer modelOfficer = null;
			List<ElectionsOfficer> officers = null;

			// check if the password already exists
			modelOfficer = objectLayer.createElectionsOfficer();
			modelOfficer.setUserName(username);
			officers = objectLayer.findElectionsOfficer(modelOfficer);
			if( officers.size() > 0 )
				officer = officers.get( 0 );

			// check if the person actually exists, and if so, throw an exception
			if( officer == null )
				throw new EVException( "Officer with this username does not exist" );

			officer.setPassword(new_password);
			objectLayer.storeElectionsOfficer( officer );

			return officer.getId();		
		}
	}
}
