package edu.uga.cs.evote.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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
import edu.uga.cs.evote.entity.Voter;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.object.impl.ObjectLayerImpl;
import edu.uga.cs.evote.persistence.PersistenceLayer;
import edu.uga.cs.evote.persistence.impl.PersistenceLayerImpl;

/**
 * Servlet implementation class DisplayBallotForVotingServlet
 */
@WebServlet("/DisplayBallotForVotingServlet")
public class DisplayBallotForVotingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DisplayBallotForVotingServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter writer = response.getWriter();
		ObjectLayer layer = new ObjectLayerImpl();
		Connection conn;

		List<Ballot> allBallots = new ArrayList<>();
		try {			
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://uml.cs.uga.edu:3306/team11", "team11", "virtual");
			PersistenceLayer pLayer = new PersistenceLayerImpl(conn, layer);
			
			HttpSession session = request.getSession(true);
			String username = (String) session.getAttribute("user");
			
			Voter modelVoter = layer.createVoter();
			modelVoter.setUserName(username);
			Voter voter = pLayer.restoreVoter(modelVoter).get(0);
			
			Ballot modelBallot = layer.createBallot();
			modelBallot.setOpen(true);
			modelBallot.setElectoralDistrict(voter.getElectoralDistrict());
			allBallots = pLayer.restoreBallot(modelBallot);
			
			writer.println("var select = document.getElementById('chooseBallotForVoting');");
			for(int i = 0; i < allBallots.size(); i++){
			writer.println("var option = document.createElement('option');");
			writer.println("option.text = \"Ballot" + allBallots.get(i).getId()+"\";");
			writer.println("select.add(option);");
			}
			writer.println("");
			
		} catch (SQLException | EVException e) {

			e.printStackTrace();
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
