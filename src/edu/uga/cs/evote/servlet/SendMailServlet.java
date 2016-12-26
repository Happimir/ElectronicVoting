package edu.uga.cs.evote.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import edu.uga.cs.evote.EVException;
import edu.uga.cs.evote.entity.ElectionsOfficer;
import edu.uga.cs.evote.entity.Voter;
import edu.uga.cs.evote.object.ObjectLayer;
import edu.uga.cs.evote.object.impl.ObjectLayerImpl;
import edu.uga.cs.evote.persistence.PersistenceLayer;
import edu.uga.cs.evote.persistence.impl.PersistenceLayerImpl;

/**
 * Servlet implementation class SendMailServlet
 */
@WebServlet("/SendMailServlet")
public class SendMailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SendMailServlet() {
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
		
		response.setContentType("text/html;charset=UTF-8");
		ObjectLayer layer = new ObjectLayerImpl();
		Connection conn;
		
		PrintWriter out = response.getWriter();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://uml.cs.uga.edu:3306/team11", "team11", "virtual");
			PersistenceLayer pLayer = new PersistenceLayerImpl(conn, layer);
			

			String email = request.getParameter("email");
			String subject = "Password for eVote";
			String username = request.getParameter("username");
			String password = null;
			
			
			if(request.getParameter("userType").equals("Voter")){
				Voter modelVoter = layer.createVoter();
				List<Voter> voters = new ArrayList<>();
				modelVoter.setUserName(username);
				modelVoter.setEmailAddress(email);
				voters = pLayer.restoreVoter(modelVoter);
				
				if(voters.size() == 0){
					out.println("<script type=text/javascript>");
					out.println("alert('Please make sure you entered correct username or email!');");
					out.println("location='sendMail.html';");
					out.println("</script>");
					return;
				}else{
					password = voters.get(0).getPassword();
				}
			}
			else {

				ElectionsOfficer modelOfficer = layer.createElectionsOfficer();
				List<ElectionsOfficer> officers = new ArrayList<>();
				modelOfficer.setUserName(username);
				modelOfficer.setEmailAddress(email);
				officers = pLayer.restoreElectionsOfficer(modelOfficer);
				if(officers.size() == 0){
					out.println("<script type=text/javascript>");
					out.println("alert('Please make sure you entered correct username or email!');");
					out.println("location='sendMail.html';");
					out.println("</script>");
					return;
				}else{
				password = officers.get(0).getPassword();
				}
			}
			String message = "Dear "+ username + ",\nYour password is " + password + ".\nSincerely,\neVote teams";
			
			Properties properties = new Properties();
			properties.put("mail.smtp.user", "evote.team11@gmail.com");
	        properties.put("mail.smtp.host", "smtp.gmail.com");
	        properties.put("mail.smtp.port", "587");
	        properties.put("mail.smtp.auth", "true");
	        properties.put("mail.smtp.debug", "true");
	        properties.put("mail.smtp.starttls.enable", "true");
	 
	        // creates a new session with an authenticator
	        Authenticator auth = new Authenticator() {
	            public PasswordAuthentication getPasswordAuthentication() {
	                return new PasswordAuthentication("evote.team11@gmail.com", "team11password");
	            }
	        };
	 
	        Session session = Session.getInstance(properties, auth);
	 
	        // creates a new e-mail message
	        Message msg = new MimeMessage(session);
	 
	        msg.setFrom(new InternetAddress("eVote.team11@gmail.com"));
	        InternetAddress[] toAddresses = { new InternetAddress(email) };
	        msg.setRecipients(Message.RecipientType.TO, toAddresses);
	        msg.setSubject(subject);
	        msg.setSentDate(new Date());
	        msg.setText(message);
	 
	        // sends the e-mail
	        Transport transport = session.getTransport("smtp");
	        transport.connect("smtp.gmail.com","evote.team11@gmail.com","team11password");
	        transport.sendMessage(msg,msg.getAllRecipients());
			out.println("<script type=text/javascript>");
			out.println("alert('Your password has been sent to your email address, please check!');");
			out.println("location='signin.html';");
			out.println("</script>");
		
			
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (EVException e) {
			
			e.printStackTrace();
			
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			
			e.printStackTrace();
		} 
		
	     
	    
	    
	}

}
