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
 * Servlet implementation class LoginSv
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("IT WORKS");
		System.out.println("IT WORKS");
		System.out.println("IT WORKS");
		System.out.println("IT WORKS");
		System.out.println("IT WORKS");
		String password = request.getParameter("password");
		String username = request.getParameter("user");
		String OTP = request.getParameter("OTP");

		String cookies = "";

		UserService us = new UserService();

		try {
			
			int good = us.login(username, password, OTP);

			if (good == 1) {
				cookies = us.getCookies(username);
				System.out.println("Login Cookies : " + cookies);

			} else {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				cookies = "Username or password not matching";
			}

		} catch (SQLException e) {

			System.out.println(e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

		}

		response.addHeader("Access-Control-Allow-Origin", "*");
		response.getWriter().append(cookies);
	}

}
