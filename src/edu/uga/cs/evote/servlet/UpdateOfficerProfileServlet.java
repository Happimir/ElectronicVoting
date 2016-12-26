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
 * Servlet implementation class UpdateOfficerServlet
 */
@WebServlet("/UpdateOfficerProfileServlet")
public class UpdateOfficerProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateOfficerProfileServlet() {
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
		response.setContentType("text/html"); 
		PrintWriter writer = response.getWriter();
		
		String       user_name = null;
		String       first_name = null;
		String	     last_name = null;
		String	     address = null;
		String	     email = null;
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
	    	  
	    	  writer.println("<script type=text/javascript>");
	    	  writer.println("alert('Session Expired: Please log in again 4');");
	    	  writer.println("</script>"); 
	    	  return;
	      }
	      String old_username = (String) httpSession.getAttribute("user");
	      user_name = request.getParameter("user_name");
	      email = request.getParameter("email");
	      first_name = request.getParameter("first_name");
	      last_name = request.getParameter("last_name");
	      address = request.getParameter("address");
	      
	      
	    


	      if( first_name == null ) {
	    	  writer.println("<script type=text/javascript>");
	    	  writer.println("alert('Unspecified First Name');");
	    	  writer.println("</script>"); 
	    	  return;
	      }

	      if( last_name == null ) {
	    	  writer.println("<script type=text/javascript>");
	    	  writer.println("alert('Unspecified Last Name');");
	    	  writer.println("</script>"); 
	    	  return;
	      }

	      if( address == null ){
	    	  writer.println("<script type=text/javascript>");
	    	  writer.println("alert('Unspecified Address');");
	    	  writer.println("</script>"); 
	    	  return;
	      }

	      if( email == null ) {
	    	  writer.println("<script type=text/javascript>");
	    	  writer.println("alert('Unspecified Email');");
	    	  writer.println("</script>"); 
	    	  return;
	      }
	      
	      try {
	          long officer_id = logicLayer.updateElectionsOfficer( old_username, first_name, last_name, email, address);
	          writer.println("<script type=text/javascript>");
	    	  writer.println("alert('Successfully updated profile');");
	    	  writer.println("location='officer.html';");
	    	  writer.println("</script>"); 
	    	  return;
	      } 
	      catch ( Exception e ) {
	    	 
	    	  e.printStackTrace();
	    	  writer.println("<script type=text/javascript>");
	    	  writer.println("alert('Exception! Error updating officer.');");
	    	  writer.println("</script>"); 
	      }
		doGet(request, response);
	}

}
