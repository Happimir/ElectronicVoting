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
 * Servlet implementation class VoteBallotItemServlet
 */
@WebServlet("/VoteBallotItemServlet")
public class VoteBallotItemServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public VoteBallotItemServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

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

		LogicLayer   logicLayer = null;
		HttpSession  httpSession = null;
		Session      session = null;
		String       ssid = null;

		httpSession = request.getSession();
		if(httpSession == null){
			writer.println("<script type=text/javascript>");
			writer.println("alert('Session Expired: Please log in again 1');");
			writer.println("</script>");
			return;
		}

		ssid = (String) httpSession.getAttribute( "ssid" );
		System.out.println("ssid" + ssid);
		if( ssid == null ) {       // not logged in!
			writer.println("<script type=text/javascript>");
			writer.println("alert('Session Expired: Please log in again 2');");
			writer.println("</script>");
			return;
		}


		session = SessionManager.getSessionById( ssid );
		if( session == null ) {
			writer.println("<script type=text/javascript>");
			writer.println("alert('Session Expired: Please log in again 3');");
			writer.println("</script>");
			return;
		}



		logicLayer = session.getLogicLayer();
		if( logicLayer == null ) {
			System.out.println("null");
			writer.println("<script type=text/javascript>");
			writer.println("alert('Session Expired: Please log in again 4');");
			writer.println("</script>"); 
			return;
		}

		String ballot = request.getParameter("chooseBallotForVoting");
		String bItem = request.getParameter("chooseBallotItemForVoting");
		String vote = request.getParameter("chooseVoteOptionForVoting");
		String username = (String) httpSession.getAttribute("user");
		try{
			long ret = logicLayer.updateVotes(username, ballot, bItem, vote);
			if(ret == 1){
				writer.println("<script type=text/javascript>");
				writer.println("alert('Vote successfully submitted');");
				writer.println("location='voter.html';");
				writer.println("</script>"); 
			} else {
				writer.println("<script type=text/javascript>");
				writer.println("alert('Records show you have already voted on this election/issue');");
				writer.println("location='voter.html';");
				writer.println("</script>"); 
			}
			
		} catch(Exception e){
			 e.printStackTrace();
	    	  writer.println("<script type=text/javascript>");
	    	  writer.println("alert('Exception! Error updating voting.');");
	    	  writer.println("</script>");
		}

		doGet(request, response);
	}

}
