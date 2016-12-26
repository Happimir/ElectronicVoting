package edu.uga.cs.evote.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uga.cs.evote.entity.Ballot;
import edu.uga.cs.evote.entity.BallotItem;
import edu.uga.cs.evote.entity.Election;
import edu.uga.cs.evote.entity.Issue;
import edu.uga.cs.evote.entity.VoteRecord;
import edu.uga.cs.evote.entity.Voter;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.object.impl.ObjectLayerImpl;
import edu.uga.cs.evote.persistence.PersistenceLayer;
import edu.uga.cs.evote.persistence.impl.PersistenceLayerImpl;

/**
 * Servlet implementation class ProveBallotServlet
 */
@WebServlet("/ProveBallotServlet")
public class ProveBallotServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ProveBallotServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter writer = response.getWriter();
		ObjectLayer layer = new ObjectLayerImpl();
		Connection conn;
		int ballotID = Integer.parseInt(request.getParameter("chooseBallotForProve").substring(7));
		try {			
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://uml.cs.uga.edu:3306/team11", "team11", "virtual");
			PersistenceLayer pLayer = new PersistenceLayerImpl(conn, layer);
			Ballot modelBallot = layer.createBallot();
			modelBallot.setId(ballotID);
			Ballot ballot = pLayer.restoreBallot(modelBallot).get(0);

			writer.println("<h2>VoteRecord for Ballot " + ballotID+": </h2><br>");
			writer.println("<p>Start Date: "+ballot.getOpenDate().toString()+"</p>");
			writer.println("<p>End Date: "+ballot.getCloseDate().toString()+"</p>");
			List<BallotItem> ballotItems = pLayer.restoreBallotIncludesBallotItem(ballot);
			for(int i = 0; i<ballotItems.size(); i++){
				if(ballotItems.get(i) instanceof Election){
					Election modelElection = (Election) ballotItems.get(i);
					List<Election> elections = pLayer.restoreElection(modelElection);
					Election election = elections.get(0);
					writer.println(election.getOffice());

					VoteRecord modelVoteRecord = layer.createVoteRecord();
					modelVoteRecord.setBallotItem(election);
					writer.println("<br>Voters: ");
					try{
						List<VoteRecord> voteRecords = pLayer.restoreVoteRecord(modelVoteRecord);
						for(int j = 0; j< voteRecords.size(); j++){
							writer.print(voteRecords.get(j).getVoter().getFirstName()+" "+ voteRecords.get(j).getVoter().getLastName()+" on " + voteRecords.get(j).getDate().toString() + ";");
						}
					}catch(Exception e){
						e.printStackTrace();
						writer.print("N/a");
						writer.println("<br><br>");
						continue;
					}

					writer.println("<br><br>");
				}else{
					Issue modelIssue = (Issue) ballotItems.get(i);
					List<Issue> issues = pLayer.restoreIssue(modelIssue);
					Issue issue = issues.get(0);
					writer.println(issue.getQuestion());

					VoteRecord modelVoteRecord = layer.createVoteRecord();
					modelVoteRecord.setBallotItem(issue);
					writer.println("<br>Voters: ");
					try{
						List<VoteRecord> voteRecords = pLayer.restoreVoteRecord(modelVoteRecord);
						for(int j = 0; j< voteRecords.size(); j++){
							writer.print(voteRecords.get(j).getVoter().getFirstName()+" "+ voteRecords.get(j).getVoter().getLastName() + ";");
						}
					}catch(Exception e){
						writer.print("N/a");
						writer.println("<br><br>");
						continue;
					}

					writer.println("<br><br>");
				}
			}





			writer.println("<br><br><a href=\"officer.html\">Go Back</a>");
			writer.println("<br><br><a href=\"officer.html\">Go Back</a>");
		} catch(Exception e){
			e.printStackTrace();
		}
	}

}
