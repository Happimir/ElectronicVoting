package edu.uga.cs.evote.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.uga.cs.evote.entity.Ballot;
import edu.uga.cs.evote.entity.Candidate;
import edu.uga.cs.evote.entity.Election;
import edu.uga.cs.evote.entity.ElectoralDistrict;
import edu.uga.cs.evote.entity.Issue;
import edu.uga.cs.evote.entity.Voter;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.object.impl.ObjectLayerImpl;
import edu.uga.cs.evote.persistence.PersistenceLayer;
import edu.uga.cs.evote.persistence.impl.PersistenceLayerImpl;

/**
 * Servlet implementation class DisplayResultsVoterServlet
 */
@WebServlet("/DisplayResultsVoterServlet")
public class DisplayResultsVoterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DisplayResultsVoterServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter writer = response.getWriter();
		ObjectLayer layer = new ObjectLayerImpl();
		Connection conn;
		HttpSession httpSession = request.getSession(true);
		try {			
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://uml.cs.uga.edu:3306/team11", "team11", "virtual");
			PersistenceLayer pLayer = new PersistenceLayerImpl(conn, layer);
			String username = (String) httpSession.getAttribute("user");

			Voter modelVoter = layer.createVoter();
			modelVoter.setUserName(username);
			Voter voter = pLayer.restoreVoter(modelVoter).get(0);

			List<Election> elections = new ArrayList<>();

			ElectoralDistrict district = voter.getElectoralDistrict();
			Ballot modelBallot = layer.createBallot();
			modelBallot.setApproved(true);
			modelBallot.setElectoralDistrict(district);
			List<Ballot> ballots = pLayer.restoreBallot(modelBallot);
			for(int a = 0; a < ballots.size(); a++){
				writer.println("<h1>Ballot " + ballots.get(a).getId() + "</h1><br><br>");
				Election modelElection = layer.createElection();
				modelElection.setBallot(ballots.get(a));
				elections = pLayer.restoreElection(modelElection);
				if(elections.size() <= 0){

				} else {
					writer.println("<h2>Elections: </h2>" );
					for(int i = 0; i < elections.size(); i++){
						writer.println("Office: " + elections.get(i).getOffice() + "<br><br>");
						writer.println("Candidates: <br>");
						List<Candidate> candidates = pLayer.restoreCandidateIsCandidateInElection(elections.get(i));
						for(int j = 0; j < candidates.size(); j++){
							if(elections.get(i).getIsPartisan())
								writer.println("Name: " + candidates.get(j).getName() + " (" + candidates.get(j).getPoliticalParty().getName() + ") " + "Votes: "+ candidates.get(j).getVoteCount() + "<br>");
							else
								writer.println("Name: " + candidates.get(j).getName() + " Votes: "+ candidates.get(j).getVoteCount() + "<br>");
						}
						Candidate maxCandidate = candidates.get(0);
						for(int k = 1; k < candidates.size(); k++){
							if(candidates.get(k).getVoteCount() > maxCandidate.getVoteCount()){
								maxCandidate = candidates.get(k);
							}
						}
						writer.println("<br><br><h3>WINNER: " + maxCandidate.getName() + "</h3><br><br>");
					}
				}
				Issue modelIssue = layer.createIssue();
				modelIssue.setBallot(ballots.get(a));
				List<Issue> issues = pLayer.restoreIssue(modelIssue);
				if(issues.size() > 0){
					for(int i = 0; i < issues.size(); i++){
						writer.println("Question: " + issues.get(i).getQuestion() + "<br>");
						writer.println("Yes Count: " + issues.get(i).getYesCount() + ", " + issues.get(i).getNoCount() + "<br><br>");
					}
				}
				if(elections.size() <= 0 && issues.size() <= 0){
					writer.println("<h3> Results not published yet for ballot " + ballots.get(a).getId() +  "</h3>");
				}
			}
			doGet(request, response);
		} catch (Exception e){
			e.printStackTrace();
		}

	}
}
