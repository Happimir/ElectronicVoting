package edu.uga.cs.evote.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.Ballot;
import edu.uga.cs.evote.entity.Election;
import edu.uga.cs.evote.entity.PoliticalParty;
import edu.uga.cs.evote.logic.LogicLayer;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.object.impl.ObjectLayerImpl;
import edu.uga.cs.evote.session.Session;
import edu.uga.cs.evote.session.SessionManager;

/**
 * Servlet implementation class CreateElectionServlet
 */
@WebServlet("/CreateElectionServlet")
public class CreateElectionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateElectionServlet() {
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
		
		String electionName;
		int ballotID = Integer.parseInt(request.getParameter("chooseBallotForElection").substring(7));
		
		
		List<String> candidateNames = new ArrayList<String>();
		List<String> partyNames = new ArrayList<String>();
		
		electionName = request.getParameter("electionname");
		
		String isPartisan = request.getParameter("isPartisan");
		
		int i = 1;
		while(true) {
			System.out.print("here");
			if(request.getParameter("candidate"+i) == null || request.getParameter("candidate"+i).isEmpty()){
				break;
			}
			try{
				candidateNames.add(request.getParameter("candidate"+i));
			}catch(Exception e){
				e.printStackTrace();
			}
			/*if(request.getParameter("candidate"+i) != null || !request.getParameter("candidate"+i).isEmpty()) {
				candidateNames.add(request.getParameter("candidate"+i));
			}*/
			
			if(isPartisan.equalsIgnoreCase("true")) {
				try{
					partyNames.add(request.getParameter("party"+i));
				} catch(Exception e) {
					e.printStackTrace();
					break;
				}
			}else {
				System.out.println("Not Partisan Election");
			}
			
			i++;
		}
		
		LogicLayer logicLayer = null;
		
		String ssid;
		int election_id;
		int candidate_id;
		int party_id;
		
		HttpSession httpSession;
		Session session;
		
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
		
		if(electionName == null) {
			writer.println("<script type=text/javascript>");
			writer.println("alert('Election Name Cannot Be Null');");
			writer.println("</script>");
        	return;
		}
		
		if(candidateNames.size() == 0) {
			writer.println("<script type=text/javascript>");
			writer.println("alert('Candidates must be specified');");
			writer.println("</script>");
        	return;
		}
		
//Commenting for now, will probably have to add some check to see if it is partisan or not. 		
//		if(partyNames.size() == 0) {
//			writer.println("<script type=text/javascript>");
//			writer.println("alert('Election Name Cannot Be Null');");
//			writer.println("</script>");
//        	return;
//		}
		
		PoliticalParty party = null;
		Election election = null;
		try {																		//ballot
			boolean isPart = false;
			if(isPartisan.equalsIgnoreCase("true")) {
				isPart = true;
			}
			

			election_id = (int) logicLayer.createElection(electionName, isPart, ballotID);
			if(isPart){
				for(String pName : partyNames) {
					party_id = (int) logicLayer.createPoliticalParty(pName);
				}
				
				int index = 0;
				for(String cName : candidateNames) {
					System.out.println(cName);
					candidate_id = (int) logicLayer.createCandidate(cName, partyNames.get(index), electionName);
					index++;
					
				}
			} else {
				for(String cName : candidateNames) {
					System.out.println(cName);
					candidate_id = (int) logicLayer.createCandidate(cName, null, electionName);
					
				}
			}
			writer.println("<script type=text/javascript>");
			writer.println("alert('Election has been created successfully!');");
			writer.println("location='officer.html';");
			writer.println("</script>");
			
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
	}

}
