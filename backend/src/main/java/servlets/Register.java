package servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import services.users.UserService;

/**
 * Servlet implementation class RegisterSv
 */
@WebServlet("/Register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String mail = request.getParameter("mail");
		String password = request.getParameter("password");
		String username = request.getParameter("username");

		String res = null;
		
		UserService us = new UserService();

		try {
			
			boolean success = us.register(mail, username, password);
			if (success == true) {
				res = "User created";
			} else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				res = "User already exists";
				
			}
			
		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.getWriter().append(res);

	}

}
