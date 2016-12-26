package edu.uga.cs.evote.servlet;

import java.io.IOException;
import java.io.PrintWriter;

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
 * Servlet implementation class LogoutServlet
 */
@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		HttpSession httpSession = null;
		String ssid = null;
		
		response.setContentType("text/html");
		PrintWriter writer = response.getWriter();
		
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

}
