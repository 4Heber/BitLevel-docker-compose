package models.card;

import java.sql.ResultSet;
import java.sql.SQLException;

import services.sql.DbFunctions;

/**
 * Clase VirtualCard
 * 
 * @author rafa,kevin,heber
 *
 */
public class VirtualCard {

	private int cardId;
	private String cardHolder;
	private String digits;
	private int cvc;
	private String expDate;
	private int idVault;

	/**
	 * Constructor vacio
	 */
	public VirtualCard() {

	}

	/**
	 * Constructor completo
	 * 
	 * @param cardId
	 * @param cardHolder
	 * @param digits
	 * @param cvc
	 * @param expDate
	 * @param idVault
	 */
	public VirtualCard(int cardId, String cardHolder, String digits, int cvc, String expDate, int idVault) {
		setCardId(cardId);
		setCardHolder(cardHolder);
		setDigits(digits);
		setCvc(cvc);
		setExpDate(expDate);
		setIdVault(idVault);
	}

	/**
	 * Funcion para convertir el valor de los atributos a Json para pasar al
	 * frontend
	 * 
	 * @param idVault
	 * @return
	 * @throws SQLException
	 */
	public String toJson(int idVault) throws SQLException {
		// DEFINIMOS LOS STRINGS PARA FORMAR LA ESTRUCTURA DE RETORNO
		String estructuraInicio = "\n\"Card\": [";
		String estructuraFinal = "\n ]\n}";
		String content = "";

		// CREAMOS 2 CONSULTAS PARA OBTENER EL CYPTO TAG Y EL AMMOUNT
		String getCard = "select * from virtualCards where idVault = ? ";

		ResultSet CardRs = DbFunctions.Select(getCard, new Object[] { idVault });

		while (CardRs.next()) {
			content += "{";
			content += "\"cardId\" : \"" + CardRs.getInt("cardID") + "\",\n";
			content += "\"cardHolder\" : \"" + CardRs.getString("cardHolder") + "\",\n";
			content += "\"digits\" : \"" + CardRs.getString("digits") + "\",\n";
			content += "\"cvc\" : \"" + CardRs.getInt("cvc") + "\",\n";
			content += "\"expDate\" : \"" + CardRs.getString("expDate") + "\",\n";
			content += "\"idVault\" : \"" + CardRs.getInt("idVault") + "\"\n";
			content += "},\n";

		}
		try {
			content = content.substring(0, content.length() - 2);

			// DEVOLVEMOS EL STRING CONCATENADO
			return (estructuraInicio + content + estructuraFinal);
		} catch (Exception e) {

			// DEVOLVEMOS EL STRING CONCATENADO
			return (estructuraInicio + content + estructuraFinal);
		}
	}

	/**
	 * Getters y setters
	 * 
	 * @return
	 */
	public int getCardId() {
		return cardId;
	}

	public void setCardId(int cardId) {
		this.cardId = cardId;
	}

	public String getCardHolder() {
		return cardHolder;
	}

	public void setCardHolder(String cardHolder) {
		this.cardHolder = cardHolder;
	}

	public String getDigits() {
		return digits;
	}

	public void setDigits(String digits) {
		this.digits = digits;
	}

	public int getCvc() {
		return cvc;
	}

	public void setCvc(int cvc) {
		this.cvc = cvc;
	}

	public String getExpDate() {
		return expDate;
	}

	public void setExpDate(String expDate) {
		this.expDate = expDate;
	}

	public int getIdVault() {
		return idVault;
	}

	public void setIdVault(int idVault) {
		this.idVault = idVault;
	}

}
