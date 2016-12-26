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
import javax.servlet.http.HttpSession;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Voter;
import edu.uga.cs.evote.logic.LogicLayer;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.object.impl.ObjectLayerImpl;
import edu.uga.cs.evote.persistence.PersistenceLayer;
import edu.uga.cs.evote.persistence.impl.PersistenceLayerImpl;
import edu.uga.cs.evote.session.Session;
import edu.uga.cs.evote.session.SessionManager;

/**
 * Servlet implementation class DeleteVoterServlet
 */
@WebServlet("/DeleteVoterServlet")
public class DeleteVoterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteVoterServlet() {
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
		try {			
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://uml.cs.uga.edu:3306/team11", "team11", "virtual");
			PersistenceLayer pLayer = new PersistenceLayerImpl(conn, layer);
		
			HttpSession session = request.getSession(true);
			String username = (String)session.getAttribute("user");
			System.out.println("Username: " + username);
			Voter modelVoter = layer.createVoter();
			modelVoter.setUserName(username);
			Voter voter = pLayer.restoreVoter(modelVoter).get(0);
			pLayer.deleteVoter(voter);
		}catch(Exception e){
			e.printStackTrace();
		}
		HttpSession httpSession = null;
		String ssid = null;
		
		httpSession = request.getSession(false);
		if(httpSession != null) {
			ssid = (String) httpSession.getAttribute("ssid");
			if(ssid != null) {
				System.out.println("SSID Exits: " + ssid);
				Session session = SessionManager.getSessionById(ssid);
				if(session == null) {
					System.out.println("Null Session");
					writer.println("<script type=text/javascript>");
					writer.println("alert('Session is null');");
					writer.println("</script>");
		        	return;
				}
				LogicLayer logicLayer = session.getLogicLayer();
				try {
					logicLayer.logout(ssid);
					httpSession.removeAttribute("ssid");
					httpSession.invalidate();
					System.out.println("Invalidated http session");
					response.sendRedirect("index.html");
				} catch (EVException e) {
					e.printStackTrace();
				}
			} 
			else 
				System.out.println("ssid is null");
		}
		else 
			System.out.println("no httpSession");	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
