package models.crypto;

import java.sql.ResultSet;
import java.sql.SQLException;

import services.sql.DbFunctions;
/**
 * Clase Transacciones
 * @author rafa,kevin,heber
 *
 */
public class Transaction {

	private int transactionID;
	private int idVault;
	private String transactionDate;
	private float transactionAmount;
	private String transactionAmountFees;
	private float coinPrice;
	private String transactionCoin;
	private String transactionDestination;
	private String transactionSource;
	private String transactionDescription;
	private String benefits;
	private String fees;
	private String method;
	
	/**
	 * Constructor vacio
	 */
	public Transaction() {
	}
	/**
	 * Constructor completo
	 * @param transactionID
	 * @param idVault
	 * @param transactionDate
	 * @param transactionAmount
	 * @param transactionAmountFees
	 * @param coinPrice
	 * @param transactionCoin
	 * @param transactionDestination
	 * @param transactionSource
	 * @param transactionDescription
	 * @param benefits
	 * @param fees
	 * @param method
	 */
	public Transaction(int transactionID, int idVault, String transactionDate, float transactionAmount,	String transactionAmountFees, float coinPrice,
			String transactionCoin, String transactionDestination, String transactionSource,
			String transactionDescription, String benefits, String fees,String method) {

		setTransactionID(transactionID);
		setIdVault(idVault);
		setTransactionDate(transactionDate);
		setTransactionAmount(transactionAmount);
		setTransactionAmountFees(transactionAmountFees);
		setCoinPrice(coinPrice);
		setTransactionCoin(transactionCoin);
		setTransactionDestination(transactionDestination);
		setTransactionSource(transactionSource);
		setTransactionDescription(transactionDescription);
		setBenefits(benefits);
		setFees(fees);
		setMethod(method);
	}

	/**
	 * Funcion para convertir el valor de los atributos a Json para pasar al frontend
	 * @param idVault
	 * @return
	 * @throws SQLException
	 */
	public String ToJson(int idVault) throws SQLException {

		// DECLARAMOS LA ESTRUCTURA DE INICIO
		String estructuraInicio = "\n\"Transaction\": [";

		String transactions = "";

		String tempQuery = "select * from transactions where idVault = ? ";
		ResultSet transaction = DbFunctions.Select(tempQuery, new Object[] { idVault });

		while (transaction.next()) {
			transactions += "{";
			transactions += "\n\"transactionID\": \"" + transaction.getInt("transactionID") + "\",\n";
			transactions += "\"transactionDate\": \"" + transaction.getString("transactionDate") + "\",\n";
			transactions += "\"transactionAmount\": \"" + transaction.getFloat("transactionAmount") + "\",\n";
			transactions += "\"transactionAmountFees\": \"" + transaction.getFloat("transactionAmountFees") + "\",\n";
			transactions += "\"coinPrice\": \"" + transaction.getFloat("coinPrice") + "\",\n";
			transactions += "\"transactionCoin\": \"" + transaction.getString("transactionCoin") + "\",\n";
			transactions += "\"transactionSource\": \"" + transaction.getString("transactionSource") + "\",\n";
			transactions += "\"transactionDestination\": \"" + transaction.getString("transactionDestination") + "\",\n";
			transactions += "\"transactionDescription\": \"" + transaction.getString("transactionDescription") + "\",\n";
			transactions += "\"transactionBenefit\": \"" + transaction.getString("benefits") + "\",\n";
			transactions += "\"transactionMethod\": \"" + transaction.getString("method") + "\",\n";
			transactions += "\"transactionFee\": \"" + transaction.getString("fees") + "\"\n";
			transactions += "},\n";
		}
		if (transactions.equals("")) {
			return "";
		}
		transactions = transactions.substring(0, transactions.length() - 2);
		transactions += "],\n";

		// CONCATENAMOS LOS STRINGS
		String complete = estructuraInicio + transactions;

		// DEVOLVEMOS EL STRING CONCATENADO
		return complete;

	}
	/**
	 * Getters y setters
	 * @return
	 */
	public int getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(int transactionID) {
		this.transactionID = transactionID;
	}

	public int getIdVault() {
		return idVault;
	}

	public void setIdVault(int idVault) {
		this.idVault = idVault;
	}

	public String getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}

	public float getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(float transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public float getCoinPrice() {
		return coinPrice;
	}

	public void setCoinPrice(float coinPrice) {
		this.coinPrice = coinPrice;
	}

	public String getTransactionCoin() {
		return transactionCoin;
	}

	public void setTransactionCoin(String transactionCoin) {
		this.transactionCoin = transactionCoin;
	}

	public String getTransactionDestination() {
		return transactionDestination;
	}

	public void setTransactionDestination(String transactionDestination) {
		this.transactionDestination = transactionDestination;
	}

	public String getTransactionSource() {
		return transactionSource;
	}

	public void setTransactionSource(String transactionSource) {
		this.transactionSource = transactionSource;
	}

	public String getTransactionDescription() {
		return transactionDescription;
	}

	public void setTransactionDescription(String transactionDescription) {
		this.transactionDescription = transactionDescription;
	}

	public String getBenefits() {
		return benefits;
	}

	public void setBenefits(String benefits) {
		this.benefits = benefits;
	}

	public String getFees() {
		return fees;
	}

	public void setFees(String fees) {
		this.fees = fees;
	}

	public String getTransactionAmountFees() {
		return transactionAmountFees;
	}

	public void setTransactionAmountFees(String transactionAmountFees) {
		this.transactionAmountFees = transactionAmountFees;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
	
	
	

}
