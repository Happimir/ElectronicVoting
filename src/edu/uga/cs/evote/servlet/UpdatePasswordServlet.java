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
 * Servlet implementation class UpdatePasswordServlet
 */
@WebServlet("/UpdatePasswordServlet")
public class UpdatePasswordServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdatePasswordServlet() {
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
		
		String	     username = null;
		String		 old_password = null;
		String		 new_password = null;
		String		 re_password = null;
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
	      username = (String) httpSession.getAttribute("user");
	      String userType = (String) httpSession.getAttribute("type");
	    
	      old_password = request.getParameter("old_password");
	      new_password = request.getParameter("new_password");
	      re_password = request.getParameter("re_password");
	      


	      if( old_password == null ) {
	    	  writer.println("<script type=text/javascript>");
	    	  writer.println("alert('Unspecified Old Password');");
	    	  writer.println("</script>"); 
	    	  return;
	      }

	      if( new_password == null ) {
	    	  writer.println("<script type=text/javascript>");
	    	  writer.println("alert('Unspecified New Password');");
	    	  writer.println("location='changePWD.html';");
	    	  writer.println("</script>"); 
	    	  return;
	      }

	      if( re_password == null ){
	    	  writer.println("<script type=text/javascript>");
	    	  writer.println("alert('Unspecified re_password');");
	    	  writer.println("location='changePWD.html';");
	    	  writer.println("</script>"); 
	    	  return;
	      }

	      if( !new_password.equals(re_password) ) {
	    	  writer.println("<script type=text/javascript>");
	    	  writer.println("alert('New password does not match to the re-entered password');");
	    	  writer.println("location='changePWD.html';");
	    	  writer.println("</script>"); 
	    	  return;
	      }
	      
	      try {
	          long user_id = logicLayer.updatePassword( userType, username, old_password, new_password);
	          writer.println("<script type=text/javascript>");
	    	  writer.println("alert('Successfully updated password');");
	    	  if(userType.equalsIgnoreCase("voter")){
	    		  writer.println("location='voter.html';");
	    	  }
	    	  else {
	    		  writer.println("location='officer.html';");
	    	  }
	    	  writer.println("</script>"); 
	    	  return;
	      } 
	      catch ( Exception e ) {
	    	 
	    	  e.printStackTrace();
	    	  writer.println("<script type=text/javascript>");
	    	  writer.println("alert('Exception! Error updating password');");
	    	  writer.println("</script>"); 
	      }
	}



}
