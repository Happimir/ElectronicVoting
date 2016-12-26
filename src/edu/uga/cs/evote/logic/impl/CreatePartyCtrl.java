package edu.uga.cs.evote.logic.impl;

import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.PoliticalParty;
import edu.uga.cs.evote.object.ObjectLayer;

public class CreatePartyCtrl {

	ObjectLayer objectLayer = null;
	
	public CreatePartyCtrl(ObjectLayer layer) {
		this.objectLayer = layer;
	}
	
	public long createPoliticalParty(String name) throws EVException {
		
		PoliticalParty party = null;
		PoliticalParty modelParty = null;
		List<PoliticalParty> parties = null;
		
		modelParty = objectLayer.createPoliticalParty();
		modelParty.setName(name);
		parties = objectLayer.findPoliticalParty(modelParty);
		
		if(parties.size() > 0) {
			party = parties.get(0);
		}
		
		if(party == null) {
			party = objectLayer.createPoliticalParty(name);
			objectLayer.storePoliticalParty(party);
		}
		
		return party.getId();
		
	}
}
