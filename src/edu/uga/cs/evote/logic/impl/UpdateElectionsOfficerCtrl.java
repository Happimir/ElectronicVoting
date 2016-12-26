package edu.uga.cs.evote.logic.impl;

import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.ElectionsOfficer;
import edu.uga.cs.evote.entity.ElectoralDistrict;
import edu.uga.cs.evote.object.ObjectLayer;

public class UpdateElectionsOfficerCtrl {
	ObjectLayer objectLayer = null;
	public UpdateElectionsOfficerCtrl(ObjectLayer layer) {
		this.objectLayer = layer;
	}
	
	public long updateElectionsOfficer(String old_username, String first_name, String last_name, String email, String address) throws EVException{
		ElectionsOfficer modelOfficer = null;
		ElectionsOfficer officer = null;
		List<ElectionsOfficer> officers = null;


		
		// check if the userName already exists
		modelOfficer = objectLayer.createElectionsOfficer();
		modelOfficer.setUserName(old_username);
		officers = objectLayer.findElectionsOfficer(modelOfficer);
		if( officers.size() > 0 )
			officer = officers.get( 0 );

		// check if the person actually exists, and if so, throw an exception
		if( officer == null )
			throw new EVException( "Officer with old username does not exist" );


		officer.setFirstName(first_name);
		officer.setLastName(last_name);
		officer.setEmailAddress(email);
		officer.setAddress(address);
		objectLayer.storeElectionsOfficer( officer );

		return officer.getId();
	}

}
