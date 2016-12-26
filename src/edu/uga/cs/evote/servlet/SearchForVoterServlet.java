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
import edu.uga.cs.evote.entity.BallotItem;
import edu.uga.cs.evote.entity.Candidate;
import edu.uga.cs.evote.entity.Election;
import edu.uga.cs.evote.entity.Issue;
import edu.uga.cs.evote.entity.Voter;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.object.impl.ObjectLayerImpl;
import edu.uga.cs.evote.persistence.PersistenceLayer;
import edu.uga.cs.evote.persistence.impl.PersistenceLayerImpl;

/**
 * Servlet implementation class SearchForVoterServlet
 */
@WebServlet("/SearchForVoterServlet")
public class SearchForVoterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SearchForVoterServlet() {
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
		try {			
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://uml.cs.uga.edu:3306/team11", "team11", "virtual");
			PersistenceLayer pLayer = new PersistenceLayerImpl(conn, layer);
			
			HttpSession session = request.getSession(true);
			String username = (String) session.getAttribute("user");
			Voter modelVoter = layer.createVoter();
			modelVoter.setUserName(username);
			Voter voter = pLayer.restoreVoter(modelVoter).get(0);
			
			Ballot modelBallot = layer.createBallot();
			modelBallot.setElectoralDistrict(voter.getElectoralDistrict());
			List<Ballot> ballots = new ArrayList<>();
			ballots = pLayer.restoreBallot(modelBallot);
			writer.println("<h2>Ballots: </h2>" );
			for(int i = 0; i < ballots.size(); i++){
				writer.println("<h3> Ballot " + ballots.get(i).getId() + "</h3><br>");
				writer.println("Electoral District: " + ballots.get(i).getElectoralDistrict().getName() + "<br>");
				writer.println("Start Date: " + ballots.get(i).getOpenDate() + "<br>");
				writer.println("End Date: " + ballots.get(i).getCloseDate() + "<br>");

				List<BallotItem> bItems = pLayer.restoreBallotIncludesBallotItem(ballots.get(i));
				for(int a = 0; a < bItems.size(); a++){
					long bItemID = bItems.get(a).getId();

					if( bItems.get(a) instanceof Election){
						Election modelElection = layer.createElection();
						modelElection.setId(bItemID);
						List<Election> elections = pLayer.restoreElection(modelElection);
						if(elections.size() > 0){
							for(int b = 0; b < elections.size(); b++){
								writer.println("Election: " + elections.get(b).getOffice() + "<br>");
								List<Candidate> candidates = pLayer.restoreCandidateIsCandidateInElection(elections.get(b));
								writer.println("Candidates: <br> ");
								for(int c = 0; c < candidates.size(); c++){
									writer.println(candidates.get(c).getName() + " (" + candidates.get(c).getPoliticalParty().getName() + ") <br>");
								}
								writer.println("<br>");
							}
							writer.println("<br>");
						}
					}else{
						writer.println("<br>");
						Issue modelIssue = layer.createIssue();
						modelIssue.setId(bItemID);
						List<Issue> issues = pLayer.restoreIssue(modelIssue);
						if(issues.size() > 0){
							writer.println("Issue: ");
							for(int d = 0; d < issues.size(); d++){
								writer.println(issues.get(d).getQuestion());
								writer.println("<br>");
							}
							writer.println("<br>");
						}
					}
				}
			}

			writer.println("<br><br><a href=\"voter.html\">Go Back</a>");
		} catch(Exception e){
			e.printStackTrace();
		}
	}

}
