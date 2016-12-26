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
 * Servlet implementation class CreateVoterServlet
 */
@WebServlet("/CreateVoterServlet")
public class CreateVoterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateVoterServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		response.sendRedirect("voter.html");
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html"); 
		PrintWriter writer = response.getWriter();
		
		String       user_name = null;
		String 		 password = null;
		String       repassword = null;
		String       first_name = null;
		String	     last_name = null;
		String	     address = null;
		String	     email = null;
		String		 age = null;
		long	     voter_id = 0;
		String       district = null;
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
		
/*		ssid = (String) httpSession.getAttribute( "ssid" );
		System.out.println("ssid" + ssid);
	      if( ssid == null ) {       // not logged in!
	    	  writer.println("<script type=text/javascript>");
	    	  writer.println("alert('Session Expired: Please log in again 2');");
	    	  writer.println("</script>");
	    	  return;
	      }*/

	      
//	      session = SessionManager.getSessionById( ssid );
//	      if( session == null ) {
//	    	  writer.println("<script type=text/javascript>");
//	    	  writer.println("alert('Session Expired: Please log in again 3');");
//	    	  writer.println("</script>");
//	    	  return;
//	      }
	      
	      //added by michael
	      if(session == null) {
	    	  try {
	                session = SessionManager.createSession();
	            }
	            catch ( Exception e ) {
	            	writer.println("<script type=text/javascript>");
					writer.println("alert('Error Creating Session 3');");
					writer.println("</script>");
	                return;
	            }
	      } // by michael
	      

	      logicLayer = session.getLogicLayer();
	      if( logicLayer == null ) {
	    	  writer.println("<script type=text/javascript>");
	    	  writer.println("alert('Session Expired: Please log in again 4');");
	    	  writer.println("</script>"); 
	    	  return;
	      }
	      

	      user_name = request.getParameter("user_name");
	      password = request.getParameter("password");
	      repassword = request.getParameter("repassword");
	      email = request.getParameter("email");
	      first_name = request.getParameter("first_name");
	      last_name = request.getParameter("last_name");
	      address = request.getParameter("address");
	      age = request.getParameter("age");
	      district = request.getParameter("district");
	      
	      
	      
	      if( user_name == null ) {
	    	  writer.println("<script type=text/javascript>");
	    	  writer.println("alert('Unspecified Username');");
	    	  writer.println("</script>"); 
	    	  return;
	      }

	      if( password == null ) {
	    	  writer.println("<script type=text/javascript>");
	    	  writer.println("alert('Unspecified Password');");
	    	  writer.println("</script>"); 
	    	  return;
	      }
	      
	      if( !password.equals(repassword)){
	    	  writer.println("<script type=text/javascript>");
	    	  writer.println("alert('Passwords don't match');");
	    	  writer.println("</script>"); 
	    	  return;
	      }

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

	      if( age == null ){
	    	  writer.println("<script type=text/javascript>");
	    	  writer.println("alert('Unspecified Age');");
	    	  writer.println("</script>"); 
	    	  return;
	      }

	      if( email == null ) {
	    	  writer.println("<script type=text/javascript>");
	    	  writer.println("alert('Unspecified Email');");
	    	  writer.println("</script>"); 
	    	  return;
	      }
	      if(district == null) {
	    	  writer.println("<script type=text/javascript>");
	    	  writer.println("alert('Unspecified District');");
	    	  writer.println("</script>"); 
	    	  return;
	      }
	      
	      try {
	          voter_id = logicLayer.createVoter( user_name, password, email, first_name, last_name, address, district, Integer.parseInt(age) );
	          writer.println("<script type=text/javascript>");
	    	  writer.println("alert('Voter account has been created successfully');");
	    	  writer.println("location='signin.html';");
	    	  writer.println("</script>");
	      } 
	      catch ( Exception e ) {
	    	  e.printStackTrace();
	    	  writer.println("<script type=text/javascript>");
	    	  writer.println("alert('Exception! Error creating voter.');");
	    	  writer.println("</script>"); 
	      }
	      
		doGet(request, response);
	}

}
