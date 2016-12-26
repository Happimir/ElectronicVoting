package edu.uga.cs.evote.logic.impl;

import java.util.Date;
import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Ballot;
import edu.uga.cs.evote.entity.ElectoralDistrict;
import edu.uga.cs.evote.object.ObjectLayer;

public class CreateBallotCtrl {

ObjectLayer objectLayer = null;
	
	public CreateBallotCtrl(ObjectLayer layer) {
		this.objectLayer = layer;
	}
	
	public long createBallot(Date startDate, Date endDate, String districtName) throws EVException {
		
		Ballot ballot = null;
		Ballot modelBallot = null;
		List<Ballot> ballots = null;
		
		modelBallot = objectLayer.createBallot();
		modelBallot.setOpenDate(startDate);
		modelBallot.setCloseDate(endDate);
		modelBallot.setApproved(false);
		
		ElectoralDistrict district = null;
		ElectoralDistrict modelDistrict = objectLayer.createElectoralDistrict();
		modelDistrict.setName(districtName);
		List<ElectoralDistrict> districts = null;
		districts = objectLayer.findElectoralDistrict(modelDistrict);
		
		if(districts.size() > 0){
			district = districts.get(0);
		}
		else {
			throw new EVException( "Electoral District with given ID does not exist" );
		}
		
		modelBallot.setElectoralDistrict(district);
		
		ballots = objectLayer.findBallot(modelBallot);
		
		if(ballots.size() > 0){
			ballot = ballots.get(0);
		}
		
		if(ballot != null){
			throw new EVException( "A ballot with given parameters already exists." );
		}
		
		ballot = objectLayer.createBallot(startDate, endDate, false, district, false);
		objectLayer.storeBallot(ballot);
		
	
		
		
		return ballot.getId();
	}
	
}
