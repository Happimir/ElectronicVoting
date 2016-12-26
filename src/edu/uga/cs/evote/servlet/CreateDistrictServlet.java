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
 * Servlet implementation class CreateDistrictServlet
 */
@WebServlet("/CreateDistrictServlet")
public class CreateDistrictServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateDistrictServlet() {
        super();
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
		
		int electoralDistrict_id;
		String district_name = "";
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
		
		district_name = request.getParameter("district_name");
		
		if(district_name == null) {
			writer.println("<script type=text/javascript>");
			writer.println("alert('District Name Cannot Be Null');");
			writer.println("</script>");
        	return;
		}
		
		try {
			electoralDistrict_id = (int) logicLayer.createElectoralDistrict(district_name);
			writer.println("<script type=text/javascript>");
			writer.println("alert('Successfully created district');");
			writer.println("location='officer.html';");
			writer.println("</script>");

        	
		} catch(Exception e) {
			e.printStackTrace();
			return;
		}
		
		//doGet(request, response);
	}

}
