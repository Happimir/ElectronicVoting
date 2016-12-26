package edu.uga.cs.evote.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Ballot;
import edu.uga.cs.evote.entity.BallotItem;
import edu.uga.cs.evote.entity.Election;
import edu.uga.cs.evote.entity.Issue;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.object.impl.ObjectLayerImpl;
import edu.uga.cs.evote.persistence.PersistenceLayer;
import edu.uga.cs.evote.persistence.impl.PersistenceLayerImpl;

/**
 * Servlet implementation class DisplayBallotItemForVotingServlet
 */
@WebServlet("/DisplayBallotItemForVotingServlet")
public class DisplayBallotItemForVotingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DisplayBallotItemForVotingServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter writer = response.getWriter();
		ObjectLayer layer = new ObjectLayerImpl();
		Connection conn;

		List<Ballot> allBallots = new ArrayList<>();
		try {			
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://uml.cs.uga.edu:3306/team11", "team11", "virtual");
			PersistenceLayer pLayer = new PersistenceLayerImpl(conn, layer);
			allBallots = pLayer.restoreBallot(null);

			writer.println("var ballotItemsByBallot = {");
			for(int i = 0; i < allBallots.size(); i++){
				writer.print("Ballot"+ allBallots.get(i).getId()+": [");
				List<BallotItem> allBallotItems = new ArrayList<>();
				allBallotItems = pLayer.restoreBallotIncludesBallotItem(allBallots.get(i));
				if(allBallotItems.size() != 0){
					for(int j=0; j < allBallotItems.size(); j++){
						long bItemID = allBallotItems.get(j).getId();
						if(allBallotItems.get(j) instanceof Election){
							Election modelElection = layer.createElection();
							modelElection.setId(bItemID);
							List<Election> elections = pLayer.restoreElection(modelElection);
							Election election = elections.get(0);
							writer.print("\""+ election.getOffice()+"\"");
							if(j != allBallotItems.size()-1){
								writer.print(",");
							}
							else{
								writer.print("]");
							}
						}else{
							Issue modelIssue = layer.createIssue();
							modelIssue.setId(bItemID);
							List<Issue> issues = pLayer.restoreIssue(modelIssue);
							Issue issue = issues.get(0);
							writer.print("\""+ issue.getQuestion()+"\"");
							if(j != allBallotItems.size()-1){
								writer.print(",");
							}
							else{
								writer.print("]");
							}
						}

					}
				}else{
					writer.print("]");
				}

				if(i != allBallots.size() -1 ){
					writer.print(",");
				}else{
					writer.print("}");
				}
			}
			writer.println("\nfunction changeBallot(value) {");
			writer.println("if (value.length != 0)");
			writer.println("{");
			writer.println("var BallotItemOption = \"\";");
			writer.println("for (ballotItemId in ballotItemsByBallot[value]){");
			writer.println("BallotItemOption += \"<option>\"+ ballotItemsByBallot[value][ballotItemId]+\"</option>\";}");
			writer.println("document.getElementById(\"chooseBallotItemForVoting\").innerHTML = BallotItemOption;}}");


		} catch (SQLException | EVException e) {

			e.printStackTrace();
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
