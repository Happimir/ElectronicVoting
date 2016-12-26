package edu.uga.cs.evote.logic.impl;

import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.ElectoralDistrict;
import edu.uga.cs.evote.entity.Voter;
import edu.uga.cs.evote.object.ObjectLayer;

public class UpdateVoterCtrl {
	ObjectLayer objectLayer = null;
	public UpdateVoterCtrl(ObjectLayer layer) {
		this.objectLayer = layer;
	}
	public long updateVoter(String old_username, String first_name, String last_name, String email,
			String address, String district) throws EVException {
		Voter modelVoter = null;
		Voter voter = null;
		List<Voter> voters = null;


		
		// check if the userName already exists
		modelVoter = objectLayer.createVoter();
		modelVoter.setUserName(old_username);
		voters = objectLayer.findVoter(modelVoter);
		if( voters.size() > 0 )
			voter = voters.get( 0 );

		// check if the person actually exists, and if so, throw an exception
		if( voter == null )
			throw new EVException( "Officer with old username does not exist" );
		
		ElectoralDistrict modelDistrict = objectLayer.createElectoralDistrict();
		modelDistrict.setName(district);
		ElectoralDistrict nDistrict = null;
		nDistrict = objectLayer.findElectoralDistrict(modelDistrict).get(0);

		voter.setFirstName(first_name);
		voter.setLastName(last_name);
		voter.setEmailAddress(email);
		voter.setAddress(address);
		voter.setElectoralDistrict(nDistrict);
		objectLayer.storeVoter( voter );

		return voter.getId();
	}

}
