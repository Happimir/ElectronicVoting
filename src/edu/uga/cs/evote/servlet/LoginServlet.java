package edu.uga.cs.evote.servlet;


import java.io.IOException;
import java.io.PrintWriter;


import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.logic.LogicLayer;
import edu.uga.cs.evote.session.Session;
import edu.uga.cs.evote.session.SessionManager;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet implements Servlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginServlet() {
		super();
	}



	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//response.getWriter().append("Served at: ").append(request.getContextPath());

		PrintWriter writer = response.getWriter();
		response.setContentType("text/html");

		HttpSession    httpSession = null;
		String         username = null;
		String         password = null;
		String         ssid = null;
		Session        session = null;
		LogicLayer     logicLayer = null;

		username = request.getParameter("username");
		password = request.getParameter("password");
		boolean isVoter = false;		
		if(request.getParameter("userType").equals("Voter")){
			isVoter = true;
		}
		else {

			isVoter = false;
		}

		httpSession = request.getSession();
		ssid = (String) httpSession.getAttribute( "ssid" );
		if( ssid != null ) {
			System.out.println( "Already have ssid: " + ssid );
			session = SessionManager.getSessionById( ssid );
			System.out.println(session + " is session");
			System.out.println( "Connection: " + session.getConnection() );
		}
		else
			System.out.println( "ssid is null" );

		// Prepare the HTTP response:
		// - Use the charset of template for the output
		// - Use text/html MIME-type
		//

		if( session == null ) {
			try {
				session = SessionManager.createSession();
			}
			catch ( Exception e ) {
				writer.println("<script type=text/javascript>");
				writer.println("alert('Error Creating Session');");
				writer.println("</script>");
				return;
			}
		}
		
		logicLayer = session.getLogicLayer();
		if( username == null || password == null ) {
			writer.println("<script type=text/javascript>");
			writer.println("alert('Error could not find username or password');");
			writer.println("</script>");
		}

		try {          
			ssid = logicLayer.login( session, isVoter, username, password );
			httpSession.setAttribute("user", username);
			if(isVoter) httpSession.setAttribute("type", "voter");
			else	    httpSession.setAttribute("type", "officer");
			System.out.println( "Obtained ssid: " + ssid );
			httpSession.setAttribute( "ssid", ssid );
			System.out.println( "Connection: " + session.getConnection() );


		} 
		catch ( Exception e ) {
			e.printStackTrace();
			writer.println("<script type=text/javascript>");
			writer.println("alert('Error: Invalid username or password');");
			writer.println("location='signin.html';");
			writer.println("</script>");
			return;
		}
		//	        try {
		//				SessionManager.logout(ssid);
		//			} catch (EVException e) {
		//				e.printStackTrace();
		//			}

		if(isVoter == true) {
			response.sendRedirect("voter.html");
		} else {
			response.sendRedirect("officer.html");
		}

	}

}
