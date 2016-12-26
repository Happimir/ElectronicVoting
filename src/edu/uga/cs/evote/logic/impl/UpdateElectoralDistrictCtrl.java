package edu.uga.cs.evote.logic.impl;

import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.ElectoralDistrict;
import edu.uga.cs.evote.object.ObjectLayer;

public class UpdateElectoralDistrictCtrl {
	ObjectLayer objectLayer = null;
	public UpdateElectoralDistrictCtrl(ObjectLayer layer) {
		this.objectLayer = layer;
	}


	public long updateDistrict( String old_name, String new_name)
	throws EVException
	{ 
		ElectoralDistrict district = null;
		ElectoralDistrict modelDistrict = null;
		List<ElectoralDistrict> districts = null;
		
		ElectoralDistrict district_check = null;
		ElectoralDistrict modelDistrict_check = null;
		List<ElectoralDistrict> districts_check = null;
		
		// check if the userName already exists
		modelDistrict = objectLayer.createElectoralDistrict();
		modelDistrict.setName(old_name);
		districts = objectLayer.findElectoralDistrict(modelDistrict);
		if( districts.size() > 0 )
		district = districts.get( 0 );
		
		// check if the person actually exists, and if so, throw an exception
		if( district == null )
		throw new EVException( "District with old name does not exist" );
		
	
		modelDistrict_check = objectLayer.createElectoralDistrict();
		modelDistrict_check.setName(new_name);
		districts_check = objectLayer.findElectoralDistrict(modelDistrict_check);
		if( districts_check.size() > 0 )
		district_check = districts_check.get( 0 );
		
		// check if the person actually exists, and if so, throw an exception
		if( district_check != null )
		throw new EVException( "District with new name already exists in database" );
		
		district.setName(new_name);
		objectLayer.storeElectoralDistrict( district );
		
		return district.getId();
	}
}

