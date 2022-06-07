package services.card;

import java.sql.ResultSet;
import java.sql.SQLException;

import models.card.VirtualCard;
import services.sql.DbFunctions;
/**
 * Servicio de tarjetas de credito virtuales
 * @author rafa,kevin,heber
 *
 */
public class VirtualCardService {
	/**
	 * Añadir una tarjeta de credito
	 * @param vc
	 * @return
	 * @throws SQLException
	 */
	public boolean addCard(VirtualCard vc) throws SQLException {

		String getCards = "select count(cardID) as total from VirtualCards where idVault = ? ";

		ResultSet CardsRs = DbFunctions.Select(getCards, new Object[] { vc.getIdVault() });

		if (CardsRs.next()) {
			
		if (CardsRs.getInt("total") >= 3) {

			return false;
		}}
		
		String getMaxId = "select  max(cardID) as total from VirtualCards";

		ResultSet MaxIdRs = DbFunctions.Select(getMaxId, new Object[] { });

		MaxIdRs.next();

		int id = MaxIdRs.getInt("total");
		id++;

		String insertQuery = "?,?,?,?,?,?";
		DbFunctions.Insert("VirtualCards", insertQuery,
				new Object[] { id,vc.getCardHolder(), vc.getDigits(), vc.getCvc(), vc.getExpDate(),  vc.getIdVault() });

		return true;
	}
	
	/**
	 * Eliminar una tarjeta de credito
	 * @param id
	 * @param idVault
	 * @return
	 * @throws SQLException
	 */
	public boolean removeCard(String id,int idVault) throws SQLException {
		
		String validateQuery = "select cardID from VirtualCards where idVault = ? and cardDigits = ?";

		ResultSet validate = DbFunctions.Select(validateQuery, new Object[] { idVault, id });

		if ( validate.next() == false ) {
			return false;
		}
		String deleteQuery = " Digits = ? and idVault = ?";
		DbFunctions.Delete("virtualCards", deleteQuery, new Object[] { id, idVault });
		return true;
	}
	
}
