package edu.uga.cs.evote.servlet;

import java.io.IOException;
import java.io.PrintWriter;

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
 * Servlet implementation class UpdateOpenServlet
 */
@WebServlet("/UpdateOpenServlet")
public class UpdateOpenServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UpdateOpenServlet() {
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
		response.setContentType("text/html");
		PrintWriter writer = response.getWriter();
		int ballot_id;
		String open = "";
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

		open = request.getParameter("openValue");
		int ballotID = Integer.parseInt(request.getParameter("chooseBallotForOpen").substring(7));

		try {
			ballot_id = (int) logicLayer.updateBallot(open, ballotID);
			if(ballot_id == -1){
				writer.println("<script type=text/javascript>");
				writer.println("alert('Error current date is not the open date for this ballot');");
				writer.println("location='officer.html';");
				writer.println("</script>");
			}
			else if(ballot_id == -2){
				writer.println("<script type=text/javascript>");
				writer.println("alert('Error current date is not the close date for this ballot');");
				writer.println("location='officer.html';");
				writer.println("</script>");
			}else{
				writer.println("<script type=text/javascript>");
				writer.println("alert('Successfully " + open + " the Ballot');");
				writer.println("location='officer.html';");
				writer.println("</script>");
			}
		} catch(Exception e) {
			e.printStackTrace();
			return;
		}
	}


}
