package servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.card.VirtualCard;
import services.card.VirtualCardService;
import services.users.UserService;

/**
 * Servlet implementation class RegisterSv
 */
@WebServlet("/CreateCard")
public class CreateCard extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String holder = request.getParameter("cardHolder");
		String digits = request.getParameter("cardDigits");
		int cvc = Integer.parseInt(request.getParameter("cardCvc"));
		String exp = request.getParameter("cardExp");
		int idVault = Integer.parseInt(request.getParameter("idVault"));

		String res = null;
		
		VirtualCardService vcs = new VirtualCardService();
		VirtualCard vc = new VirtualCard(0,holder,digits,cvc,exp,idVault);
		

		try {
			
			boolean success = vcs.addCard(vc);
			if (success == true) {
				res = "Card added successfully!";
			} else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				res = "Limit of cards reached 3 / 3";
				
			}
			
		} catch (SQLException e) {
			System.out.println(e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			res = "Internal error";
		}
		
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.getWriter().append(res);

	}

}
