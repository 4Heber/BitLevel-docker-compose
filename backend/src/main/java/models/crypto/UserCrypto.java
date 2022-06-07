package models.crypto;

import java.sql.ResultSet;
import java.sql.SQLException;

import services.sql.DbFunctions;
/**
 * Clase UserCrypto
 * @author rafa,kevin,heber
 *
 */
public class UserCrypto {

	private int id;
	private int idVault;
	private String cryptoTag;
	private float cryptoAmount;
	
	/**
	 * Constructor vacio
	 */
	public UserCrypto() {

	}
	
	/**
	 * Constructor completo
	 * @param id
	 * @param idVault
	 * @param cryptoTag
	 * @param cryptoAmount
	 */
	public UserCrypto(int id, int idVault, String cryptoTag, float cryptoAmount) {
		setId(id);
		setIdVault(idVault);
		setCryptoTag(cryptoTag);
		setCryptoAmount(cryptoAmount);
	}

	/**
	 * 	 * Funcion para convertir el valor de los atributos a Json para pasar al frontend el fiat
	 * @param idVault
	 * @return
	 * @throws SQLException
	 */
	public String FiatToJson(int idVault) throws SQLException {

		// DEFINIMOS LOS STRINGS PARA FORMAR LA ESTRUCTURA DE RETORNO
		String estructuraInicio = "\n\"Fiat\": [";
		String estructuraFinal = "\n ],";
		String content = "";
		String addTag = "";
		String addAmount = "";

		// CREAMOS 2 CONSULTAS PARA OBTENER EL CYPTO TAG Y EL AMMOUNT
		String tag = "select cryptoTag from usercryptos where idVault = ? and cryptoTag = 'usd'";
		String amount = "select cryptoAmount from usercryptos where idVault = ? and cryptoTag  = 'usd'";

		ResultSet addTagRs = DbFunctions.Select(tag, new Object[] { idVault });
		ResultSet addAmountRs = DbFunctions.Select(amount, new Object[] { idVault });

		while (addTagRs.next()) {
			addTag = addTagRs.getString("cryptoTag");
		}
		while (addAmountRs.next()) {
			addAmount = addAmountRs.getString("cryptoAmount");
		}

		// INSERTAMOS LOS VALORES
		content += "{\r\n" + "         \"tag\": \"" + addTag + "\",\r\n" + "         \"amount\": \"" + addAmount
				+ "\"\r\n" + "      }\n";

		// DEVOLVEMOS EL STRING CONCATENADO
		return (estructuraInicio + content + estructuraFinal);
	}

	/**
	 * Funcion para convertir el valor de los atributos a Json para pasar al frontend el balance
	 * @param idVault
	 * @return
	 * @throws SQLException
	 */
	public String BalanceToJson(int idVault) throws SQLException {
		// DEFINIMOS LOS STRINGS PARA FORMAR LA ESTRUCTURA DE RETORNO
				String estructuraInicio = "\n\"Crypto\": [";
		String tempQuery = "select cryptoTag,cryptoAmount from usercryptos where idVault = ? ";
		ResultSet getBalanceRs = DbFunctions.Select(tempQuery, new Object[] { idVault });

		String midle = "";

		while (getBalanceRs.next()) {
			if (!(getBalanceRs.getString("cryptoTag").equals("usd"))) {
				midle += "{";
				midle += "\n\"tag\": \"" + getBalanceRs.getString("cryptoTag") + "\",\n";
				midle += "\"amount\": \"" + getBalanceRs.getString("cryptoAmount") + "\"\n";
				midle += "},\n";
			}
			
		}

			// CONCATENAMOS LOS STRINGS
			String complete = estructuraInicio + midle.substring(0, midle.length() - 2) + "],";

			// DEVOLVEMOS EL STRING CONCATENADO
			return complete;
	}

	/**
	 * GETTERS / SETTERS
	 * @return
	 */
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdVault() {
		return idVault;
	}

	public void setIdVault(int idVault) {
		this.idVault = idVault;
	}

	public String getCryptoTag() {
		return cryptoTag;
	}

	public void setCryptoTag(String cryptoTag) {
		this.cryptoTag = cryptoTag;
	}

	public float getCryptoAmount() {
		return cryptoAmount;
	}

	public void setCryptoAmount(float cryptoAmount) {
		this.cryptoAmount = cryptoAmount;
	}

}
