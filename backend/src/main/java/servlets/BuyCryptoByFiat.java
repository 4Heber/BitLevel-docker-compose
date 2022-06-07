package servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.crypto.UserCrypto;
import services.crypto.TransactionService;

/**
 * Servlet implementation class BuyCryptoByFiat
 */
@WebServlet("/BuyCryptoByFiat")
public class BuyCryptoByFiat extends HttpServlet {
	private static final long serialVersionUID = 1L;
 
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		int idVault = Integer.parseInt(request.getParameter("idVault"));
		
		
		int priceToFloat = Integer.parseInt(request.getParameter("priceCrypto"));
		float price = (float)priceToFloat;
		float cryptoAmount = Float.parseFloat(request.getParameter("amount"));
		String cryptoTag = request.getParameter("crypto");

		UserCrypto uc = new UserCrypto(0, idVault, cryptoTag, cryptoAmount);
		TransactionService ts = new TransactionService();
		String res = "";

		try {

			int checker = ts.buyCrypto(price, "fiatBuy", uc);
			if (checker == 0) {
				res = "You dont have enough balance";
			} else {
				res = cryptoAmount + cryptoTag + " has been successfully added into your account";
			}

		} catch (SQLException e) {

			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.getWriter().append(res);
	}
}
