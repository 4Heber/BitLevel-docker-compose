package servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import services.crypto.TransactionService;

/**
 * Servlet implementation class LoginSv
 */
@WebServlet("/SendCrypto")
public class SendCrypto extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int idVault = Integer.parseInt(request.getParameter("idVault"));
		float price = Float.parseFloat(request.getParameter("priceCrypto"));
		float cryptoAmount = Float.parseFloat(request.getParameter("amount"));
		
		String cryptoTag = request.getParameter("crypto");
		String username = request.getParameter("username");

		String res = "";

		try {
			
			TransactionService t = new TransactionService();
			t.sendCrypto(idVault, cryptoAmount, cryptoTag, price, username);
			res = cryptoAmount + "$ has been successfully sent to " + username;
		} catch (SQLException e) {

			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.getWriter().append(res);
	}

}