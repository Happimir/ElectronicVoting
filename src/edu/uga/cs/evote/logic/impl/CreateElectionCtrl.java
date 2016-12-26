package edu.uga.cs.evote.logic.impl;

import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Ballot;
import edu.uga.cs.evote.entity.Candidate;
import edu.uga.cs.evote.entity.Election;
import edu.uga.cs.evote.object.ObjectLayer;

public class CreateElectionCtrl {

	ObjectLayer objectLayer = null;
	
	public CreateElectionCtrl(ObjectLayer layer) {
		this.objectLayer = layer;
	}
	
	public long createElection(String name, boolean isPartisan, int ballotID) throws EVException {
		Ballot nBallot = null;
		Ballot modelBallot = null;
		List<Ballot> ballots = null;
		
		modelBallot = objectLayer.createBallot();
		modelBallot.setId(ballotID);
		ballots = objectLayer.findBallot(modelBallot);
		if(ballots.size() > 0){
			nBallot = ballots.get(0);
		}
		
		if(nBallot == null){
			throw new EVException("Ballot does not exist");
		}
		
		
		Election election = null;
		Election modelElection = null;
		List<Election> elections = null;
		
		modelElection = objectLayer.createElection();
		modelElection.setOffice(name);
		modelElection.setIsPartisan(isPartisan);
		elections = objectLayer.findElection(modelElection);
		if(elections.size() > 0) {
			election = elections.get(0);
		}
		
		if(election != null) {
			throw new EVException( "A election with this name already exists" );
		}
		
		election = objectLayer.createElection(name, isPartisan, nBallot);

		objectLayer.storeElection(election);
		
		return election.getId();
		
	}
	
}
