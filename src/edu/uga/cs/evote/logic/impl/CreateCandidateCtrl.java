package edu.uga.cs.evote.logic.impl;

import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Candidate;
import edu.uga.cs.evote.entity.Election;
import edu.uga.cs.evote.entity.PoliticalParty;
import edu.uga.cs.evote.object.ObjectLayer;

public class CreateCandidateCtrl {

	ObjectLayer objectLayer = null;
	
	public CreateCandidateCtrl(ObjectLayer layer) {
		this.objectLayer = layer;
	}
	
	public long createCandidate(String name, String party, String election) throws EVException {
		PoliticalParty politicalParty = null;
		PoliticalParty modelPoliticalParty = null; 
		List<PoliticalParty> politicalParties = null;
		
		modelPoliticalParty = objectLayer.createPoliticalParty();
		
		modelPoliticalParty.setName(party);
		politicalParties = objectLayer.findPoliticalParty(modelPoliticalParty);
		
		if(politicalParties.size() > 0){
			politicalParty = politicalParties.get(0);
		}else{
			throw new EVException("Political Party does not exist");
		}
		
		
		Election nElection = null;
		Election modelElection = null;
		List<Election> elections = null;
		
		modelElection = objectLayer.createElection();
		
		modelElection.setOffice(election);
		elections = objectLayer.findElection(modelElection);
		
		if(elections.size()>0){
			nElection = elections.get(0);
		}else{
			throw new EVException("Election does not exist");
		}
		
		Candidate candidate = null;
		Candidate modelCandidate = null;
		List<Candidate> candidates = null;
		
		modelCandidate = objectLayer.createCandidate();
		modelCandidate.setName(name);
		modelCandidate.setPoliticalParty(politicalParty);
		modelCandidate.setElection(nElection);
		
		candidates = objectLayer.findCandidate(modelCandidate);
		
		if(candidates.size() > 0) {
			candidate = candidates.get(0);
		}
		
		if(candidate == null) {
			candidate = objectLayer.createCandidate(name, politicalParty, nElection);
			objectLayer.storeCandidate(candidate);
		}
		
		return candidate.getId();	
	}
	
}
