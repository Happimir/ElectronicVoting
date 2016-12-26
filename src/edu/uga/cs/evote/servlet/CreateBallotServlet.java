package edu.uga.cs.evote.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.uga.cs.evote.logic.LogicLayer;
import edu.uga.cs.evote.session.Session;
import edu.uga.cs.evote.session.SessionManager;

/**
 * Servlet implementation class CreateBallotServlet
 */
@WebServlet("/CreateBallotServlet")
public class CreateBallotServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

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
		// TODO Auto-generated method stub
response.setContentType("text/html");
		
		PrintWriter writer = response.getWriter();
		
		int ballot_id;
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String district_name = request.getParameter("chooseDistrict");
		String ssid;
		
		HttpSession httpSession;
		Session session;
		
		LogicLayer logicLayer = null;
		
		httpSession = request.getSession();
		if(httpSession == null) {
			writer.println("<script type=text/javascript>");
			writer.println("alert('Session expired or illegal, please log in 1');");
			writer.println("</script>");
        	return;
		}
		
		ssid = (String) httpSession.getAttribute("ssid");
		if(ssid == null) {
			writer.println("<script type=text/javascript>");
			writer.println("alert('Session expired or illegal, please log in 2');");
			writer.println("</script>");
        	return;
		}
		
		session = SessionManager.getSessionById(ssid);
		if(session == null) {
			writer.println("<script type=text/javascript>");
			writer.println("alert('Session expired or illegal, please log in 3');");
			writer.println("</script>");
        	return;
		}
		
		logicLayer = session.getLogicLayer();
		if(logicLayer == null) {
			writer.println("<script type=text/javascript>");
			writer.println("alert('Session expired or illegal, please log in 4');");
			writer.println("</script>");
        	return;
		}
		
		try {
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
			Date start = df.parse(startDate);
			Date end = df.parse(endDate);
			ballot_id = (int) logicLayer.createBallot(start, end, district_name);
			writer.println("<script type=text/javascript>");
			writer.println("alert('Ballot has been created successfully!');");
			writer.println("location='officer.html';");
			writer.println("</script>");
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
	
	}

}
