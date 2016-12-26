package edu.uga.cs.evote.test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.mysql.jdbc.Connection;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Ballot;
import edu.uga.cs.evote.entity.Candidate;
import edu.uga.cs.evote.entity.Election;
import edu.uga.cs.evote.entity.ElectionsOfficer;
import edu.uga.cs.evote.entity.ElectoralDistrict;
import edu.uga.cs.evote.entity.Issue;
import edu.uga.cs.evote.entity.PoliticalParty;
import edu.uga.cs.evote.entity.VoteRecord;
import edu.uga.cs.evote.entity.Voter;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.object.impl.ObjectLayerImpl;
import edu.uga.cs.evote.persistence.PersistenceLayer;
import edu.uga.cs.evote.persistence.impl.DbUtils;
import edu.uga.cs.evote.persistence.impl.PersistenceLayerImpl;

public class EvoteTester {
	
	
	
    public static void main(String[] args) throws ParseException
    {
         Connection conn = null;
         ObjectLayer objectLayer = null;
         PersistenceLayer persistence = null;
         
       ElectionsOfficer mike;
       ElectionsOfficer zaid;
       Voter jugal;
       Voter hongji;
       PoliticalParty party1;
       PoliticalParty party2;
       Ballot ballot1;
       Ballot ballot2;
       Issue issue11;
       Issue issue12;
       Issue issue13;
       Issue issue21;
       Issue issue22;
       Issue issue23;
       Election election11;
       Election election12;
       Election election13;
       Election election21;
       Election election22;
       Election election23;
       ElectoralDistrict district;
       Candidate c11, c12, c13, c21, c22, c23, c31, c32, c33, c41, c42, c43, c51, c52, c53, c61, c62, c63 ;
       VoteRecord record1, record2, record3, record4;
       
       
         
         // get a database connection
         try {
             conn = DbUtils.connect();
         } 
         catch (Exception seq) {
             System.err.println( "WriteTest: Unable to obtain a database connection" );
         }
         
         if( conn == null ) {
             System.out.println( "WriteTest: failed to connect to the database" );
             return;
         }
         
         // obtain a reference to the ObjectModel module      
         objectLayer = new ObjectLayerImpl();
         // obtain a reference to Persistence module and connect it to the ObjectModel        
         persistence = new PersistenceLayerImpl( conn, objectLayer ); 
         // connect the ObjectModel module to the Persistence module
         objectLayer.setPersistence( persistence );   

         /*
         String startDateTime = "2015-08-04 08:30:00";
         LocalDateTime start = LocalDateTime.parse(startDateTime);
         String endDateTime = "2015-08-04 08:30:00";
         LocalDateTime end = LocalDateTime.parse(endDateTime);
         */
         
         
         String startDateString = "11/08/2016";
         DateFormat df = new SimpleDateFormat("MM/dd/yyyy"); 
         Date startDate = df.parse(startDateString);
         
         String endDateString = "10/08/2016";
         Date endDate = df.parse(endDateString);
         
         
         try {
             
             // creating 2 election officer
             mike = objectLayer.createElectionsOfficer( "mikekova", "mike123", "mike@uga.edu", "Mike", "Kovalsky", "133 Maple St., Big Town, AZ. 87888");
             persistence.storeElectionsOfficer( mike );

             zaid = objectLayer.createElectionsOfficer( "zaidrash", "zaid123", "zaid@uga.edu", "Zaid", "Rashid", "2000 Apple St., Small Town, CA. 25065");
             persistence.storeElectionsOfficer( zaid );
             
             
             // creating 2 voters
             jugal = objectLayer.createVoter( "jugapanc", "jugal123", "jugal@uga.edu", "Jugal", "Panchal", "201 Davis St., Large Town, GA. 25222",23);
             //persistence.storeVoter( jugal );
             
             hongji = objectLayer.createVoter( "hongwang", "hongji123", "hingji@uga.edu", "Hongji", "Wang", "1000 Lake St., Tiny Town, NC. 85465",27);
             //persistence.storeVoter( hongji );

             
             // creating a electoral district
             district = objectLayer.createElectoralDistrict("Crawford");
             persistence.storeElectoralDistrict(district);
             
             //assigning district and updating voter to reflect changes
             jugal.setElectoralDistrict(district);
             hongji.setElectoralDistrict(district);
             persistence.storeVoter(jugal);
             persistence.storeVoter(hongji);
             
             // creating 2 political parties
             party1 = objectLayer.createPoliticalParty("Demo");
             persistence.storePoliticalParty(party1);
             
             party2 = objectLayer.createPoliticalParty("Repub");
             persistence.storePoliticalParty(party2);
             
           
             // creating 2 ballots
             ballot1 = objectLayer.createBallot(startDate , endDate, true , district,false);
             persistence.storeBallot(ballot1);
             
             ballot2 = objectLayer.createBallot(startDate, endDate, true, district,false);
             persistence.storeBallot(ballot2);
             
             
             // creating 3 issues per ballot, so total of 6
             issue11 = objectLayer.createIssue("System Shutdown?", ballot1);
             persistence.storeIssue(issue11);
             
             issue12 = objectLayer.createIssue("AC Turn off?", ballot1);
             persistence.storeIssue(issue12);
             
             issue13 = objectLayer.createIssue("Alarm turned ON?", ballot1);
             persistence.storeIssue(issue13);
             
             issue21 = objectLayer.createIssue("Electricity Available?", ballot2);
             persistence.storeIssue(issue21);
             
             issue22 = objectLayer.createIssue("Water available?", ballot2);
             persistence.storeIssue(issue22);
             
             issue23 = objectLayer.createIssue("Internet Available?", ballot2);
             persistence.storeIssue(issue23);
             
             
             // creating 3 elections per ballot, total of 6 with 1 partisian on each ballot
             election11 = objectLayer.createElection("Office of supervision", true, ballot1);
             persistence.storeElection(election11);
             
             election12 = objectLayer.createElection("Class for Spring 2016", false, ballot1);
             persistence.storeElection(election12);
             
             election13 = objectLayer.createElection("Project to start first?", false, ballot1);
             persistence.storeElection(election13);
             
             election21 = objectLayer.createElection("Office of Secretary", true, ballot2);
             persistence.storeElection(election21);
             
             election22 = objectLayer.createElection("Math Teacher", false, ballot2);
             persistence.storeElection(election22);
             
             election23 = objectLayer.createElection("CS Department Head", false, ballot2);
             persistence.storeElection(election23);
             
            
             // creating 3 candidates per elections
             c11 = objectLayer.createCandidate("Sam", party1, election11);
             persistence.storeCandidate(c11);

             c12 = objectLayer.createCandidate("Lisa", party2, election11);
             persistence.storeCandidate(c12);

             c13 = objectLayer.createCandidate("Ruthford", party2, election11);
             persistence.storeCandidate(c13);

             c21 = objectLayer.createCandidate("Eddie", null, election12);
             persistence.storeCandidate(c21);

             c22 = objectLayer.createCandidate("Melissa", null, election12);
             persistence.storeCandidate(c22);

             c23 = objectLayer.createCandidate("Penny", null, election12);
             persistence.storeCandidate(c23);

             c31  = objectLayer.createCandidate("Sheldon", null, election13);
             persistence.storeCandidate(c31);

             c32 = objectLayer.createCandidate("Samir", null, election13);
             persistence.storeCandidate(c32);

             c33 = objectLayer.createCandidate("Sonia", null, election13);
             persistence.storeCandidate(c33);

             c41 = objectLayer.createCandidate("Boonie", party2, election21);
             persistence.storeCandidate(c41);

             c42 = objectLayer.createCandidate("Sandra", party2, election21);
             persistence.storeCandidate(c42);

             c43 = objectLayer.createCandidate("Gus", party1, election21);
             persistence.storeCandidate(c43);

             c51 = objectLayer.createCandidate("Robert", null, election22);
             persistence.storeCandidate(c51);
             
             c52 = objectLayer.createCandidate("Khaleed", null, election22);
             persistence.storeCandidate(c52);

             c53 = objectLayer.createCandidate("Don", null, election22);
             persistence.storeCandidate(c53);

             c61 = objectLayer.createCandidate("Fred", null, election23);
             persistence.storeCandidate(c61);

             c62 = objectLayer.createCandidate("Peter", null, election23);
             persistence.storeCandidate(c62);

             c63 = objectLayer.createCandidate("Hanna", null, election23);
             persistence.storeCandidate(c63);
             
             //Jugal vote for ballot 1. Vote record has been created and stored.
             issue11.addYesVote();
             persistence.storeIssue(issue11);
             issue12.addYesVote();
             persistence.storeIssue(issue12);
             issue13.addNoVote();
             persistence.storeIssue(issue13);          
             c11.addVote();
             persistence.storeCandidate(c11);
             persistence.storeElection(c11.getElection());
             c22.addVote();
             persistence.storeCandidate(c22);
             persistence.storeElection(c22.getElection());
             c31.addVote();
             persistence.storeCandidate(c31);
             persistence.storeElection(c31.getElection());
             record1 = objectLayer.createVoteRecord(jugal, election11, new Date());
             persistence.storeVoteRecord(record1);
             

           //Jugal vote for ballot 2. Vote record has been created and stored.  
             issue21.addYesVote();
             persistence.storeIssue(issue21);
             issue22.addNoVote();
             persistence.storeIssue(issue22);
             issue23.addYesVote();
             persistence.storeIssue(issue23);       
             c43.addVote();
             persistence.storeCandidate(c43);
             persistence.storeElection(c43.getElection());
             c52.addVote();
             persistence.storeCandidate(c52);
             persistence.storeElection(c52.getElection());
             c61.addVote();
             persistence.storeCandidate(c61);
             persistence.storeElection(c61.getElection());
             record2 = objectLayer.createVoteRecord(jugal, election22, new Date());
             persistence.storeVoteRecord(record2);
             
             //hongji vote for ballot 1. Vote record has been created and stored.
             issue11.addYesVote();
             persistence.storeIssue(issue11);
             issue12.addNoVote();
             persistence.storeIssue(issue12);
             issue13.addNoVote();
             persistence.storeIssue(issue13);          
             c12.addVote();
             persistence.storeCandidate(c12);
             persistence.storeElection(c12.getElection());
             c22.addVote();
             persistence.storeCandidate(c22);
             persistence.storeElection(c22.getElection());
             c33.addVote();
             persistence.storeCandidate(c33);
             persistence.storeElection(c33.getElection());
             record3 = objectLayer.createVoteRecord(hongji, issue11, new Date());
             persistence.storeVoteRecord(record3);
             

           //hongji vote for ballot 2. Vote record has been created and stored.  
             issue21.addNoVote();
             persistence.storeIssue(issue21);
             issue22.addNoVote();
             persistence.storeIssue(issue22);
             issue23.addYesVote();
             persistence.storeIssue(issue23);       
             c41.addVote();
             persistence.storeCandidate(c41);
             persistence.storeElection(c41.getElection());
             c52.addVote();
             persistence.storeCandidate(c52);
             persistence.storeElection(c52.getElection());
             c61.addVote();
             persistence.storeCandidate(c61);
             persistence.storeElection(c61.getElection());
             record4 = objectLayer.createVoteRecord(hongji, issue22, new Date());
             persistence.storeVoteRecord(record4);         
                 
             System.out.println( "Entity objects created and saved in the persistence module. Ready for deleting!" );
             System.out.println( "" );
             System.out.println( "Deleting...");
             persistence.deleteVoteRecord(record1);
             persistence.deleteVoteRecord(record2);
             persistence.deleteVoteRecord(record3);
             persistence.deleteVoteRecord(record4);
             System.out.println( "..." );
             System.out.println( "VoteRecords have been deleted successfully." );
            
             persistence.deleteIssue(issue11);
             persistence.deleteIssue(issue12);
             persistence.deleteIssue(issue13);
             persistence.deleteIssue(issue21);
             persistence.deleteIssue(issue22);
             persistence.deleteIssue(issue23);
             System.out.println( "..." );
             System.out.println( "Issues have been deleted successfully." );

             persistence.deleteCandidate(c11);
             persistence.deleteCandidate(c12);
             persistence.deleteCandidate(c13);
             persistence.deleteCandidate(c21);
             persistence.deleteCandidate(c22);
             persistence.deleteCandidate(c23);
             persistence.deleteCandidate(c31);
             persistence.deleteCandidate(c32);
             persistence.deleteCandidate(c33);
             persistence.deleteCandidate(c41);
             persistence.deleteCandidate(c42);
             persistence.deleteCandidate(c43);
             persistence.deleteCandidate(c51);
             persistence.deleteCandidate(c52);
             persistence.deleteCandidate(c53);
             persistence.deleteCandidate(c61);
             persistence.deleteCandidate(c62);
             persistence.deleteCandidate(c63);
             
             System.out.println( "..." );
             System.out.println( "Candidates have been deleted successfully." );
             
             persistence.deleteElection(election11);
             persistence.deleteElection(election12);
             persistence.deleteElection(election13);
             persistence.deleteElection(election21);
             persistence.deleteElection(election22);
             persistence.deleteElection(election23);
             System.out.println( "..." );
             System.out.println( "Elections have been deleted successfully." );
             
             persistence.deleteBallot(ballot1);
             persistence.deleteBallot(ballot2);
             System.out.println( "..." );
             System.out.println( "Ballots have been deleted successfully." );
             
             persistence.deleteVoter(jugal);
             persistence.deleteVoter(hongji);
             System.out.println( "..." );
             System.out.println( "Voters have been deleted successfully." );
             
             persistence.deletePoliticalParty(party1);
             persistence.deletePoliticalParty(party2);
             System.out.println( "..." );
             System.out.println( "Political Parties have been deleted successfully." );
             
             persistence.deleteElectoralDistrict(district);
             System.out.println( "..." );
             System.out.println( "District has been deleted successfully." );
             
             persistence.deleteElectionsOfficer(mike);
             persistence.deleteElectionsOfficer(zaid);
             System.out.println( "..." );
             System.out.println( "Elections Offiers have been deleted successfully." );
             System.out.println( "Complete, there is no data in the tables right now!");
             
         }
         catch( EVException ce) {
             System.err.println( "Exception: " + ce );
             ce.printStackTrace();
         }
         catch( Exception e ) {
             e.printStackTrace();
         }
         finally {
             // close the connection
             try {
                 conn.close();
             }
             catch( Exception e ) {
                 System.err.println( "Exception: " + e );
             }
         }
    }  


}
