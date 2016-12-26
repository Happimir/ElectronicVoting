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
import edu.uga.cs.evote.entity.Election;
import edu.uga.cs.evote.entity.Issue;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.object.impl.ObjectLayerImpl;
import edu.uga.cs.evote.persistence.PersistenceLayer;
import edu.uga.cs.evote.persistence.impl.PersistenceLayerImpl;

/**
 * Servlet implementation class DisplayIssueServlet
 */
@WebServlet("/DisplayIssueServlet")
public class DisplayIssueServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DisplayIssueServlet() {
        super();
        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter writer = response.getWriter();
		ObjectLayer layer = new ObjectLayerImpl();
		Connection conn;

		List<Issue> allIssues = new ArrayList<>();
		try {			
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://uml.cs.uga.edu:3306/team11", "team11", "virtual");
			PersistenceLayer pLayer = new PersistenceLayerImpl(conn, layer);
			allIssues = pLayer.restoreIssue(null);
			
			writer.println("var select = document.getElementById('chooseIssue');");
			for(int i = 0; i < allIssues.size(); i++){
			writer.println("var option = document.createElement('option');");
			writer.println("option.text = \"" + allIssues.get(i).getQuestion()+"\";");
			
			writer.println("select.add(option);");
			}
			writer.println("");
			
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
		doGet(request, response);
	}

}
