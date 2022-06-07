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
@WebServlet("/ResetImage")
public class ResetImage extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int idVault = Integer.parseInt(request.getParameter("idVault"));
		String image = request.getParameter("image");

		String res = null;

		try {
			
			UserService us = new UserService();
			boolean validate = us.resetImage(idVault, image);
			if (validate == true) {
				res = "The image has been successfully modified!";
			} else {
				res = "Credentials not matching";
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			}

		} catch (Exception e) {
			System.out.println(e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}

		response.addHeader("Access-Control-Allow-Origin", "*");
		response.getWriter().append(res);
	}
}
