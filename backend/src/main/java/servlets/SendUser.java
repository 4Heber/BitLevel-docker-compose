package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.card.VirtualCard;
import models.crypto.Transaction;
import models.crypto.UserCrypto;
import models.users.UserRegistered;
import services.users.UserService;

/**
 * Servlet implementation class loadView
 */
@WebServlet("/loadView")
public class SendUser extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String token = request.getParameter("token");
		String userData = "";

		Transaction t = new Transaction();
		UserService us = new UserService();
		UserCrypto uc = new UserCrypto();
		VirtualCard vc = new VirtualCard();
		
		try {
			UserRegistered ur = us.getUserByToken(token);
 
			String userJson = ur.toJson();
			String transactionJson = t.ToJson(ur.getIdVault());
			String userCrypto = uc.BalanceToJson(ur.getIdVault());
			String userFiat = uc.FiatToJson(ur.getIdVault());
			String userCardJson = vc.toJson(ur.getIdVault());

			userData = userJson + userFiat + userCrypto + transactionJson + userCardJson ;
		} catch (Exception e) {
			System.out.println(e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			userData = "please login again";
		}

		response.addHeader("Access-Control-Allow-Origin", "*");
		response.getWriter().append(userData);
	}
}
