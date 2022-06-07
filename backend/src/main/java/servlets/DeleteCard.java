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

/**
 * Servlet implementation class DeleteCard
 */
@WebServlet("/DeleteCard")
public class DeleteCard extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String digits = request.getParameter("cardDigits");
		int idVault = Integer.parseInt(request.getParameter("idVault"));

		String res = null;
		
		VirtualCardService vcs = new VirtualCardService();
	
		

		try {
			
			boolean success = vcs.removeCard(digits, idVault);
			if (success == true) {
				res = "Card deleted successfully!";
			} else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				res = "Card not matching";
				
			}
			
		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			res = "Internal error";
		}
		
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.getWriter().append(res);

	}

}
