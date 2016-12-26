package edu.uga.cs.evote.logic.impl;

import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.ElectoralDistrict;
import edu.uga.cs.evote.entity.Voter;
import edu.uga.cs.evote.object.ObjectLayer;

public class CreateElectoralDistrictCtrl {

	ObjectLayer objectLayer = null;
	public CreateElectoralDistrictCtrl(ObjectLayer layer) {
		this.objectLayer = layer;
	}
	
	public long createDistrict( String name)
	throws EVException
	{ 
	ElectoralDistrict district = null;
	ElectoralDistrict modelDistrict = null;
	List<ElectoralDistrict> districts = null;
	
	// check if the userName already exists
	modelDistrict = objectLayer.createElectoralDistrict();
	modelDistrict.setName(name);
	districts = objectLayer.findElectoralDistrict(modelDistrict);
	if( districts.size() > 0 )
	district = districts.get( 0 );
	
	// check if the person actually exists, and if so, throw an exception
	if( district != null )
	throw new EVException( "A district with this name already exists" );
	
	district = objectLayer.createElectoralDistrict(name);
	System.out.println("district name: " + district.getName());
	objectLayer.storeElectoralDistrict( district );
	
	return district.getId();
	}
}
