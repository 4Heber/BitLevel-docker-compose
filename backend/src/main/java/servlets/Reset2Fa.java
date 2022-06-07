package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import services.smtp.Mail;
import services.users.UserService;

/**
 * Servlet implementation class ResetPassword
 */
@WebServlet("/Reset2Fa")
public class Reset2Fa extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String mail = request.getParameter("mail");
		String newMail = request.getParameter("newMail");
		String password = request.getParameter("password");
		System.out.println(newMail);
		System.out.println(password);
		System.out.println(mail);


		String res = null;

		UserService us = new UserService();
		try {
			
			boolean validate = us.reset2fa(mail, password, newMail);
			if (validate == true) {
				res = "Email successfully updated";
			} else {
				res = "Credentials not matching";
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

			}

		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

		}
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.getWriter().append(res);
	}
}