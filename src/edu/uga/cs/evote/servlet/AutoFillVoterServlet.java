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
import javax.servlet.http.HttpSession;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.ElectionsOfficer;
import edu.uga.cs.evote.entity.ElectoralDistrict;
import edu.uga.cs.evote.entity.Voter;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.object.impl.ObjectLayerImpl;
import edu.uga.cs.evote.persistence.PersistenceLayer;
import edu.uga.cs.evote.persistence.impl.PersistenceLayerImpl;

/**
 * Servlet implementation class AutoFillVoterServlet
 */
@WebServlet("/AutoFillVoterServlet")
public class AutoFillVoterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AutoFillVoterServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter writer = response.getWriter();
		ObjectLayer layer = new ObjectLayerImpl();
		Connection conn;

		List<ElectoralDistrict> allDistricts = new ArrayList<>();
		try {			
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://uml.cs.uga.edu:3306/team11", "team11", "virtual");
			PersistenceLayer pLayer = new PersistenceLayerImpl(conn, layer);
			
			HttpSession session = request.getSession(true);
			String username = (String)session.getAttribute("user");
			Voter modelVoter = layer.createVoter();
			modelVoter.setUserName(username);
			Voter findVoter = pLayer.restoreVoter(modelVoter).get(0);
			
			writer.println("var username = document.getElementById('first_name');");
			writer.println("username.setAttribute(\"value\", \"" + findVoter.getFirstName()+"\")");
			
			writer.println("var username = document.getElementById('last_name');");
			writer.println("username.setAttribute(\"value\", \"" + findVoter.getLastName()+"\")");
			
			
			writer.println("var username = document.getElementById('email');");
			writer.println("username.setAttribute(\"value\", \"" + findVoter.getEmailAddress()+"\")");
			
			writer.println("var username = document.getElementById('address');");
			writer.println("username.setAttribute(\"value\", \"" + findVoter.getAddress()+"\")");
			
			
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
