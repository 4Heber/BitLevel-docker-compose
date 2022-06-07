package servlets;

import java.io.IOException;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import services.smtp.Mail;

/**
 * Servlet implementation class SendOTP
 */
@WebServlet("/SendOtp")
public class SendOtp extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String password = request.getParameter("password");
		String username = request.getParameter("user");
		
		String errorLog = "Uer not matching";
		
		try {
			boolean validate = 
				Mail.sendMailOTP(username, password);
			if (validate == true) {
				errorLog = "the OTP has been succesfully sent to your mail account";
				
			} else {
				
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				errorLog ="Username or password not matching";
			}
			
		} catch (Exception e) {
			
			System.out.print(e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			errorLog ="Internal server error";
			
		}
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.getWriter().append(errorLog);
	}

}
