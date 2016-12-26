package edu.uga.cs.evote.logic.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Ballot;
import edu.uga.cs.evote.entity.Ballot;
import edu.uga.cs.evote.object.ObjectLayer;

public class UpdateBallotCtrl {
	ObjectLayer objectLayer = null;
	public UpdateBallotCtrl(ObjectLayer layer) {
		this.objectLayer = layer;
	}


	public long updateBallot( Date new_start_date, Date new_end_date, int ballotID)
	throws EVException
	{ 
		Ballot ballot = null;
		Ballot modelBallot = null;
		List<Ballot> ballots = null;
		
		
		// check if the userName already exists
		modelBallot = objectLayer.createBallot();
		modelBallot.setId(ballotID);
		ballots = objectLayer.findBallot(modelBallot);
		if( ballots.size() > 0 )
		ballot = ballots.get( 0 );
		
		// check if the person actually exists, and if so, throw an exception
		if( ballot == null )
		throw new EVException( "Ballot not found" );
		
		System.out.println("District of Ballot: " + ballot.getElectoralDistrict().getName());
		ballot.setOpenDate(new_start_date);
		ballot.setCloseDate(new_end_date);
		objectLayer.storeBallot( ballot );
		
		return ballot.getId();
	}


	public long updateBallot(String open, int ballotID) throws EVException {
		Ballot ballot = null;
		Ballot modelBallot = null;
		List<Ballot> ballots = null;
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date date = new Date();
		boolean isOpen = false;
		// check if the userName already exists
		modelBallot = objectLayer.createBallot();
		modelBallot.setId(ballotID);
		ballots = objectLayer.findBallot(modelBallot);
		if( ballots.size() > 0 )
		ballot = ballots.get( 0 );
		if(open.equalsIgnoreCase("open")){
			if(dateFormat.format(ballot.getOpenDate()).compareTo(dateFormat.format(date)) == 0){
			isOpen = true;
			}
			else{
				return -1;
			}
		}
		else if(open.equalsIgnoreCase("close")){

			if(dateFormat.format(ballot.getCloseDate()).compareTo(dateFormat.format(date)) == 0){
				isOpen = false;
				}
			else{
				return -2;
			}
		}
		// check if the person actually exists, and if so, throw an exception
		if( ballot == null )
		throw new EVException( "Ballot not found" );
		ballot.setOpen(isOpen);
		objectLayer.storeBallot( ballot );
		
		return ballot.getId();
	}
}
