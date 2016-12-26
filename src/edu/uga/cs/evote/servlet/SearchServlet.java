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

import edu.uga.cs.evote.entity.Ballot;
import edu.uga.cs.evote.entity.BallotItem;
import edu.uga.cs.evote.entity.Candidate;
import edu.uga.cs.evote.entity.Election;
import edu.uga.cs.evote.entity.ElectoralDistrict;
import edu.uga.cs.evote.entity.Issue;
import edu.uga.cs.evote.entity.PoliticalParty;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.object.impl.ObjectLayerImpl;
import edu.uga.cs.evote.persistence.PersistenceLayer;
import edu.uga.cs.evote.persistence.impl.PersistenceLayerImpl;

/**
 * Servlet implementation class SearchServlet
 */
@WebServlet("/SearchServlet")
public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SearchServlet() {
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
		String search = request.getParameter("searchKeyword");
		String searchType = request.getParameter("searchType");
		try {			
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://uml.cs.uga.edu:3306/team11", "team11", "virtual");
			PersistenceLayer pLayer = new PersistenceLayerImpl(conn, layer);
			if(searchType.equals("Electoral Districts")){
				List<ElectoralDistrict> allDistricts = new ArrayList<>();
				if(search == null || search.isEmpty()) allDistricts = pLayer.restoreElectoralDistrict(null);
				else{
					ElectoralDistrict modelDistrict = layer.createElectoralDistrict();
					modelDistrict.setName(search);
					allDistricts = pLayer.restoreElectoralDistrict(modelDistrict);
				}
				writer.println("<h2>Districts: </h2>" );
				if(allDistricts.size() <= 0){
					writer.println("<h2> No Results Found</h2>");
					writer.println("<a href=\"officer.html\">Go Back</a>");
					return;
				}
				for(int i = 0; i < allDistricts.size(); i++){
					writer.println("ID: " + allDistricts.get(i).getId() + ", Name: " + allDistricts.get(i).getName() + "<br>");
				}
			}
			else if(searchType.equals("Elections")){
				List<Election> elections = new ArrayList<>();
				if(search == null || search.isEmpty()) elections = pLayer.restoreElection(null);
				else{
					Election modelElection = layer.createElection();
					modelElection.setOffice(search);
					elections = pLayer.restoreElection(modelElection);
				}
				if(elections.size() <= 0){
					writer.println("<h2> No Results Found</h2>");
					writer.println("<a href=\"officer.html\">Go Back</a>");
					return;
				}
				writer.println("<h2>Elections: </h2>" );
				for(int i = 0; i < elections.size(); i++){
					writer.println("ID: " + elections.get(i).getId() +  "Office: " + elections.get(i).getOffice() + "<br><br>");
					writer.println("Candidates: <br>");
					for(int j = 0; j < pLayer.restoreCandidateIsCandidateInElection(elections.get(i)).size(); j++){
						if(elections.get(i).getIsPartisan())
							writer.println("ID: " + pLayer.restoreCandidateIsCandidateInElection(elections.get(i)).get(j).getId() + ", Name: " + pLayer.restoreCandidateIsCandidateInElection(elections.get(i)).get(j).getName() + " (" + pLayer.restoreCandidateIsCandidateInElection(elections.get(i)).get(j).getPoliticalParty().getName() + ")" + "<br>");
						else
							writer.println("ID: " + pLayer.restoreCandidateIsCandidateInElection(elections.get(i)).get(j).getId() + ", Name: " + pLayer.restoreCandidateIsCandidateInElection(elections.get(i)).get(j).getName() + "<br>");
					}
				}
			}
			else if(searchType.equals("Candidates")){
				List<Candidate> candidates = new ArrayList<>();
				if(search == null || search.isEmpty()) candidates = pLayer.restoreCandidate(null);
				else{
					Candidate modelCandidate = layer.createCandidate();
					modelCandidate.setName(search);
					candidates = pLayer.restoreCandidate(modelCandidate);
				}
				if(candidates.size() <= 0){
					writer.println("<h2> No Results Found</h2>");
					writer.println("<a href=\"officer.html\">Go Back</a>");
					return;
				}
				writer.println("<h2>Candidates: </h2>" );
				for(int i = 0; i < candidates.size(); i++){
					writer.println("ID: " + candidates.get(i).getId() + "<br>");
					writer.println("Name: " + candidates.get(i).getName() + "<br>");
					writer.println("Political Party: " + candidates.get(i).getPoliticalParty().getName() + "<br>");
					//if(candidates.get(i).getElection() == null) System.out.println("election of candidate is null");
					//writer.println("Election: " + candidates.get(i).getElection().getOffice() + "<br>");
				}
			}
			else if(searchType.equals("Political Parties")){
				List<PoliticalParty> parties = new ArrayList<>();
				if(search == null || search.isEmpty()) parties = pLayer.restorePoliticalParty(null);
				else{
					PoliticalParty modelParty = layer.createPoliticalParty();
					modelParty.setName(search);
					parties = pLayer.restorePoliticalParty(modelParty);
				}
				if(parties.size() <= 0){
					writer.println("<h2> No Results Found</h2>");
					writer.println("<a href=\"officer.html\">Go Back</a>");
					return;
				}
				writer.println("<h2>Political Parties: </h2>" );
				for(int i = 0; i < parties.size(); i++){
					writer.println("ID: " + parties.get(i).getId() + ", Name: " + parties.get(i).getName() + "<br>");
				}
			}
			else if(searchType.equals("Issues")){
				List<Issue> issues = new ArrayList<>();
				if(search == null || search.isEmpty()) issues = pLayer.restoreIssue(null);
				else{
					Issue modelIssue = layer.createIssue();
					modelIssue.setQuestion(search);
					issues = pLayer.restoreIssue(modelIssue);
				}
				if(issues.size() <= 0){
					writer.println("<h2> No Results Found</h2>");
					writer.println("<a href=\"officer.html\">Go Back</a>");
					return;
				}
				writer.println("<h2>Issues: </h2>" );
				for(int i = 0; i < issues.size(); i++){
					writer.println("ID: " + issues.get(i).getId() + ", Question: " + issues.get(i).getQuestion() + "<br>");
				}
			}
			else if(searchType.equals("Ballots")){
				List<Ballot> ballots = new ArrayList<>();
				ballots = pLayer.restoreBallot(null);
				writer.println("<h2>Ballots: </h2>" );
				for(int i = 0; i < ballots.size(); i++){
					writer.println("<h3> Ballot " + ballots.get(i).getId() + "</h3><br>");
					writer.println("Electoral District: " + ballots.get(i).getElectoralDistrict().getName() + "<br>");
					writer.println("Start Date: " + ballots.get(i).getOpenDate() + "<br>");
					writer.println("End Date: " + ballots.get(i).getCloseDate() + "<br><br>");
					
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
							}
						}else{
							writer.println("<br><br>");
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
			}
			writer.println("<a href=\"officer.html\">Go Back</a>");
		} catch(Exception e){
			e.printStackTrace();
		}


		//doGet(request, response);
	}
}
