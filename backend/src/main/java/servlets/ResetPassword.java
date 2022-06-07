package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import services.smtp.Mail;
import services.sql.DbConnection;
import services.sql.DbFunctions;
import services.users.UserService;

/**
 * Servlet implementation class ResetPassword
 */
@WebServlet("/ResetPassword")
public class ResetPassword extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String mail = request.getParameter("mail");
		String newPassword = request.getParameter("newPassword");
		String password = request.getParameter("password");
		

		String res = null;

		System.out.println(mail + "\n" + password + "\n" + newPassword);

			try {
				
				UserService us = new UserService();
				int validate = us.resetPassword(mail, password, newPassword);
				if (validate == 1) {
					res = "The password has been successfully modified!";
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
