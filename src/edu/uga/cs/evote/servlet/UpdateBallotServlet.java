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
 * Servlet implementation class UpdateBallotServlet
 */
@WebServlet("/UpdateBallotServlet")
public class UpdateBallotServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateBallotServlet() {
        super();
        // TODO Auto-generated constructor stub
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
		response.setContentType("text/html");
		PrintWriter writer = response.getWriter();
		int ballotID;
		int ballot_ID = -1;
		String new_start_date = "";
		String new_end_date = "";
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
		
		new_start_date = request.getParameter("new_start_date");
		new_end_date = request.getParameter("new_end_date");
		ballotID = Integer.parseInt(request.getParameter("chooseBallot").substring(7));
		if(new_start_date.equalsIgnoreCase(new_end_date)){
			writer.println("<script type=text/javascript>");
			writer.println("alert('District names are identical.');");
			writer.println("</script>");
		}
		
		if(new_start_date == null || new_end_date == null) {
			writer.println("<script type=text/javascript>");
			writer.println("alert('Old District Name or New District Name cannot be null');");
			writer.println("</script>");
        	return;
		}
		
		try {
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
			Date start = df.parse(new_start_date);
			Date end = df.parse(new_end_date);
			ballot_ID = (int) logicLayer.updateBallot(start, end, ballotID);
			writer.println("<script type=text/javascript>");
			writer.println("alert('Ballot has been updated');");
			writer.println("location='officer.html';");
			writer.println("</script>");
		} catch(Exception e) {
			e.printStackTrace();
			return;
		}
	}

}
