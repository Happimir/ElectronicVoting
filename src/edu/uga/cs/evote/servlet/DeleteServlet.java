package edu.uga.cs.evote.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uga.cs.evote.entity.Ballot;
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
 * Servlet implementation class DeleteServlet
 */
@WebServlet("/DeleteServlet")
public class DeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteServlet() {
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
		String delete = request.getParameter("deleteName");
		String deleteType = request.getParameter("deleteType");
		try {			
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://uml.cs.uga.edu:3306/team11", "team11", "virtual");
			PersistenceLayer pLayer = new PersistenceLayerImpl(conn, layer);
			if(deleteType.equals("Electoral Districts")){
				if(delete == null || delete.isEmpty()){
					writer.println("<h2>Error: Empty Field for Name</h2>");
					writer.println("<a href=\"officer.html\">Go Back</a>");
					return;
				}
				else{
					ElectoralDistrict modelDistrict = layer.createElectoralDistrict();
					modelDistrict.setId(Long.parseLong(delete));
					pLayer.deleteElectoralDistrict(modelDistrict);
					response.sendRedirect("officer.html");
				}
			}
			else if(deleteType.equals("Elections")){
				if(delete == null || delete.isEmpty()){
					writer.println("<h2> No Results Found</h2>");
					writer.println("<a href=\"officer.html\">Go Back</a>");
					return;
				}
				else{
					Election modelElection = layer.createElection();
					modelElection.setId(Long.parseLong(delete));
					Election findElection = pLayer.restoreElection(modelElection).get(0);
					pLayer.deleteElection(findElection);
					response.sendRedirect("officer.html");
				}
			}
			else if(deleteType.equals("Candidates")){
				if(delete == null || delete.isEmpty()){
					writer.println("<h2> No Results Found</h2>");
					writer.println("<a href=\"officer.html\">Go Back</a>");
					return;
				}
				else{
					Candidate modelCandidate = layer.createCandidate();
					modelCandidate.setId(Long.parseLong(delete));
					pLayer.deleteCandidate(modelCandidate);
					response.sendRedirect("officer.html");
				}
			}
			else if(deleteType.equals("Political Parties")){
				if(delete == null || delete.isEmpty()){
					writer.println("<h2> No Results Found</h2>");
					writer.println("<a href=\"officer.html\">Go Back</a>");
					return;
				}
				else{
					PoliticalParty modelParty = layer.createPoliticalParty();
					modelParty.setId(Long.parseLong(delete));
					pLayer.deletePoliticalParty(modelParty);
					response.sendRedirect("officer.html");
				}
			}
			else if(deleteType.equals("Issues")){
				if(delete == null || delete.isEmpty()){
					writer.println("<h2> No Results Found</h2>");
					writer.println("<a href=\"officer.html\">Go Back</a>");
					return;
				}
				else{
					Issue modelIssue = layer.createIssue();
					modelIssue.setId(Long.parseLong(delete));
					Issue findIssue = pLayer.restoreIssue(modelIssue).get(0);
					pLayer.deleteIssue(findIssue);
					response.sendRedirect("officer.html");
				}
			}
			else if(deleteType.equals("Ballots")){
				if(delete == null || delete.isEmpty()){
					writer.println("<h2> No Results Found</h2>");
					writer.println("<a href=\"officer.html\">Go Back</a>");
					return;
				}
				else{
					Ballot modelBallot = layer.createBallot();
					modelBallot.setId(Long.parseLong(delete));
					pLayer.deleteBallot(modelBallot);
				}
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		doGet(request, response);
	}

}
