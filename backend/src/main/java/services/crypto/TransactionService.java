package services.crypto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import models.crypto.Transaction;
import models.crypto.UserCrypto;
import services.sql.DbFunctions;
/**
 * Servicio de transacciones (compra,venta,envios...)
 * @author rpere
 *
 */
public class TransactionService {

	/**
	 * Crear una transaccion
	 * @param t
	 * @return
	 * @throws SQLException
	 */
	public int createTransaction(Transaction t) throws SQLException {
		return createTransaction(t, "");
	}
	/**
	 * Crear una transaccion
	 * @param t
	 * @param daily
	 * @return
	 * @throws SQLException
	 */
	public int createTransaction(Transaction t, String daily) throws SQLException {
		try{
		// BUSCAMOS EL VALOR DEL ATRIBUTO dailyQuest
		String getId = "select transactionID as maxId from transactions order by transactionID desc limit 1";
		ResultSet idRs = DbFunctions.Select(getId);

		int id = 1;
		if (idRs.next()) {
			id += idRs.getInt("maxId");

		}

		System.out.println("HOLA LA ID ES " + id);

		float Fee = getFee(t.getIdVault());
		if (!(daily.equals(""))) {
			if (Fee < 2) {
				t.setTransactionAmount(t.getTransactionAmount() / 99 + t.getTransactionAmount());
			} else {
				t.setTransactionAmount(t.getTransactionAmount() * 5 / 95 + t.getTransactionAmount());
			}
			t.setTransactionAmountFees("0");
		}

		// CREAMOS UN OBJETO LocalDateTime para extraer la fecha actual
		LocalDateTime myDateObj = LocalDateTime.now();

		// DEFINIMOS EL PATTERN
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		// ASIGNAMOS EL VALOR DE LA FECHA A UN STRING
		String dates = myDateObj.format(myFormatObj);
		String insertQuery = "?,?,?,?,?,?,?,?,?,?,?,?,?";
		DbFunctions.Insert("transactions", insertQuery,
				new Object[] { id, t.getIdVault(), dates, t.getTransactionAmount(), t.getTransactionAmountFees(),
						t.getCoinPrice(), t.getTransactionCoin(), t.getTransactionDestination(),
						t.getTransactionSource(), t.getTransactionDescription(), t.getBenefits(), Fee, t.getMethod() });
		}catch(SQLException e ) {
			System.out.println(e);
		}
		return 1;
	}

	/**
	 * Comprar una crypto
	 * @param price
	 * @param conf
	 * @param us
	 * @return
	 * @throws SQLException
	 */
	public int buyCrypto(float price, String conf, UserCrypto us) throws SQLException {
		int res = buyCrypto(price, conf, us, null);
		return res;
	}
	/**
	 * Comprar una crypto
	 * @param price
	 * @param conf
	 * @param us
	 * @param destino
	 * @return
	 * @throws SQLException
	 */
	public int buyCrypto(float price, String conf, UserCrypto us, String destino) throws SQLException {

		try {
			float totalI = us.getCryptoAmount() * price;
			totalI = totalI * -1;
			String total = totalI + "$";

			// BUSCAMOS SI EL USUARIO TIENE YA LA CRYPTO
			String tempQuery = "select cryptoAmount from usercryptos where idVault = ? and cryptoTag = ? ";
			ResultSet cryptoRs = DbFunctions.Select(tempQuery, new Object[] { us.getIdVault(), us.getCryptoTag() });

			String crypto = "";
			Float fee = getFee(us.getIdVault());
			while (cryptoRs.next()) {
				crypto = cryptoRs.getString("cryptoAmount");
			}

			// SI EL USUARIO NO TIENE COMPRADA ESA CRYPTOMONEDA
			if (crypto.equals("")) {
				// DEFINIMOS UN INDICE
				int index = 1;

				// BUSCAMOS EL ULTIMO ID DE CRYPTOUSER
				tempQuery = "select max(id) as idCount from usercryptos ";
				ResultSet indexRs = DbFunctions.Select(tempQuery);

				while (indexRs.next()) {
					index = indexRs.getInt("idCount");
					index++;
				}

				if (conf.equals("sell")) {
					float Fee = getFee(us.getIdVault());
					float totalBal = 0;
					if (Fee < 2) {
						totalBal =  us.getCryptoAmount() * price  / 99 + us.getCryptoAmount() * price ;
					} else {
						totalBal = us.getCryptoAmount() * price  * 5 / 95 +us.getCryptoAmount() * price ;
					}
					
					String insertQuery = "?,?,?,?";
					DbFunctions.Insert("usercryptos", insertQuery,
							new Object[] { index, us.getIdVault(), us.getCryptoTag(), totalBal });
					// RETORNAMOS 1 PARA INDICAR QUE EL PROCESO HA SIDO REALIZADO CON EXITO
					return 1;

				} else if (conf.equals("send")) {
					// CREATE TRANSACTION
					totalI = us.getCryptoAmount() * price;
					total = totalI + "$";
					Transaction t = new Transaction(0, us.getIdVault(), "", us.getCryptoAmount(), "0", price,
							us.getCryptoTag(), "me", destino, "Recived", total, "", "Unknown");
					createTransaction(t);
					// INSERTAMOS LA CRYPTO
					String insertQuery = "?,?,?,?";
					DbFunctions.Insert("usercryptos", insertQuery,
							new Object[] { index, us.getIdVault(), us.getCryptoTag(), us.getCryptoAmount() });
					// RETORNAMOS 1 PARA INDICAR QUE EL PROCESO HA SIDO REALIZADO CON EXITO
					return 1;

				} else if (us.getCryptoTag().equals("usd")) {
					us.setCryptoAmount(us.getCryptoAmount() - fee * us.getCryptoAmount() / 100);
					// CREATE TRANSACTION
					totalI = us.getCryptoAmount() * price;
					total = totalI + "$";
					Transaction t = new Transaction(0, us.getIdVault(), "", us.getCryptoAmount(),
							("-" + us.getCryptoAmount() * fee / 100), price, us.getCryptoTag(), "me", "me",
							"Deposit Fiat", total, "", "Credit Card");
					createTransaction(t);
					// INSERTAMOS LA CRYPTO
					String insertQuery = "?,?,?,?";
					DbFunctions.Insert("usercryptos", insertQuery,
							new Object[] { index, us.getIdVault(), us.getCryptoTag(), us.getCryptoAmount() });
					// RETORNAMOS 1 PARA INDICAR QUE EL PROCESO HA SIDO REALIZADO CON EXITO
					return 1;

				} else if (us.getCryptoTag().equals("btl")) {
					// CREATE TRANSACTION
					totalI = us.getCryptoAmount() * price;
					total = totalI + "$";
					Transaction t = new Transaction(0, us.getIdVault(), "", us.getCryptoAmount(), "0", price,
							us.getCryptoTag(), "BL", "me", "Daily Quest", total, "", "BL");
					String insertQuery = "?,?,?,?";
					DbFunctions.Insert("usercryptos", insertQuery,
							new Object[] { index, us.getIdVault(), us.getCryptoTag(), us.getCryptoAmount() });
					
					createTransaction(t, "yes");
					// RETORNAMOS 1 PARA INDICAR QUE EL PROCESO HA SIDO REALIZADO CON EXITO
					return 1;

				} else if (conf.equals("fiatBuy")) {
					us.setCryptoAmount(us.getCryptoAmount() - fee * us.getCryptoAmount() / 100);
					// CREATE TRANSACTION
					Transaction t = new Transaction(0, us.getIdVault(), "", us.getCryptoAmount(),
							("-" + us.getCryptoAmount() * fee / 100), price, us.getCryptoTag(), "me", "me", "Buy",
							total, "", "Fiat");
					createTransaction(t, "");
					// INSERTAMOS LA CRYPTO
					String insertQuery = "?,?,?,?";
					DbFunctions.Insert("usercryptos", insertQuery,
							new Object[] { index, us.getIdVault(), us.getCryptoTag(), us.getCryptoAmount() });
					// RETORNAMOS 1 PARA INDICAR QUE EL PROCESO HA SIDO REALIZADO CON EXITO
					return 1;
				}

				else {
					us.setCryptoAmount(us.getCryptoAmount() - fee * us.getCryptoAmount() / 100);
					// CREATE TRANSACTION
					Transaction t = new Transaction(0, us.getIdVault(), "", us.getCryptoAmount(),
							("-" + us.getCryptoAmount() * fee / 100), price, us.getCryptoTag(), "me", "me", "Buy",
							total, "", "Credit Card");
					createTransaction(t, "");
					// INSERTAMOS LA CRYPTO
					String insertQuery = "?,?,?,?";
					DbFunctions.Insert("usercryptos", insertQuery,
							new Object[] { index, us.getIdVault(), us.getCryptoTag(), us.getCryptoAmount() });
					// RETORNAMOS 1 PARA INDICAR QUE EL PROCESO HA SIDO REALIZADO CON EXITO
					return 1;
				}

				// SI EL USAURIO YA POSEE ESA CRYPTO
			} else {

				// SUMAMOS AL VALOR OBTENIDO EN LA SELECT PREVIA LA CANTIDAD DE COMPRA
				float cryptoAmount = Float.parseFloat(crypto) + us.getCryptoAmount();

				if (conf.equals("sell")) {
										float Fee = getFee(us.getIdVault());
					float totalBal = 0;
					if (Fee < 2) {
						totalBal =  us.getCryptoAmount() * price  / 99 + us.getCryptoAmount() * price ;
					} else {
						totalBal = us.getCryptoAmount() * price  * 5 / 95 +us.getCryptoAmount() * price ;
					}
					float Total = totalBal +Float.parseFloat(crypto);
 
					String Query = " cryptoAmount = ?  where idVault = ? and cryptoTag = 'usd' ";
					DbFunctions.Update("usercryptos", Query,
							new Object[] { Total, us.getIdVault() });
					// RETORNAMOS 1 PARA INDICAR QUE EL PROCESO HA SIDO REALIZADO CON EXITO
					return 1;

				} else if (conf.equals("send")) {
					// CREATE TRANSACTION
					totalI = us.getCryptoAmount() * price;
					total = totalI + "$";
					Transaction t = new Transaction(0, us.getIdVault(), "", us.getCryptoAmount(), "0", price,
							us.getCryptoTag(), "me", destino, "Recived", total, "", "Unknown");
					createTransaction(t);
					// REALIZAMOS UN UPDATE AL USUARIO DEL mailOTP
					String Query = " cryptoAmount = ?  where idVault = ? and cryptoTag = ? ";
					DbFunctions.Update("usercryptos", Query,
							new Object[] { cryptoAmount, us.getIdVault(), us.getCryptoTag() });
					// RETORNAMOS 1 PARA INDICAR QUE EL PROCESO HA SIDO REALIZADO CON EXITO
					return 1;

				} else if (us.getCryptoTag().equals("usd")) {
					us.setCryptoAmount(us.getCryptoAmount() - fee * us.getCryptoAmount() / 100);
					// CREATE TRANSACTION
					totalI = us.getCryptoAmount() * price;
					total = totalI + "$";
					Transaction t = new Transaction(0, us.getIdVault(), "", us.getCryptoAmount(),
							("-" + us.getCryptoAmount() * fee / 100), price, us.getCryptoTag(), "me", "me",
							"Deposit Fiat", total, "", "Credit Card");
					createTransaction(t);
					// REALIZAMOS UN UPDATE AL USUARIO DEL mailOTP
					String Query = " cryptoAmount = ?  where idVault = ? and cryptoTag = ? ";
					DbFunctions.Update("usercryptos", Query,
							new Object[] { cryptoAmount, us.getIdVault(), us.getCryptoTag() });
					// RETORNAMOS 1 PARA INDICAR QUE EL PROCESO HA SIDO REALIZADO CON EXITO
					return 1;

				} else if (us.getCryptoTag().equals("btl")) {
					// CREATE TRANSACTION
					totalI = us.getCryptoAmount() * price;
					total = totalI + "$";
					Transaction t = new Transaction(0, us.getIdVault(), "", us.getCryptoAmount(), "0", price,
							us.getCryptoTag(), "BL", "me", "Daily Quest", total, "", "BL");
					createTransaction(t, "yes");
					// REALIZAMOS UN UPDATE AL USUARIO DEL mailOTP
					String Query = " cryptoAmount = ?  where idVault = ? and cryptoTag = ? ";
					DbFunctions.Update("usercryptos", Query,
							new Object[] { cryptoAmount, us.getIdVault(), us.getCryptoTag() });
					// RETORNAMOS 1 PARA INDICAR QUE EL PROCESO HA SIDO REALIZADO CON EXITO
					return 1;
				} else if (conf.equals("fiatBuy")) {
					us.setCryptoAmount(us.getCryptoAmount() - fee * us.getCryptoAmount() / 100);
					// CREATE TRANSACTION
					Transaction t = new Transaction(0, us.getIdVault(), "", us.getCryptoAmount(),
							("-" + us.getCryptoAmount() * fee / 100), price, us.getCryptoTag(), "me", "me", "Buy",
							total, "", "Fiat");
					createTransaction(t, "");
					// REALIZAMOS UN UPDATE AL USUARIO DEL mailOTP
					String Query = " cryptoAmount = ?  where idVault = ? and cryptoTag = ? ";
					DbFunctions.Update("usercryptos", Query,
							new Object[] { cryptoAmount, us.getIdVault(), us.getCryptoTag() });
					// RETORNAMOS 1 PARA INDICAR QUE EL PROCESO HA SIDO REALIZADO CON EXITO
					return 1;

				} else {
					us.setCryptoAmount(us.getCryptoAmount() - fee * us.getCryptoAmount() / 100);
					// CREATE TRANSACTION
					Transaction t = new Transaction(0, us.getIdVault(), "", us.getCryptoAmount(),
							("-" + us.getCryptoAmount() * fee / 100), price, us.getCryptoTag(), "me", "me", "Buy",
							total, "", "Credit Card");
					createTransaction(t, "");
					// REALIZAMOS UN UPDATE AL USUARIO DEL mailOTP
					String Query = " cryptoAmount = ?  where idVault = ? and cryptoTag = ? ";
					DbFunctions.Update("usercryptos", Query,
							new Object[] { cryptoAmount, us.getIdVault(), us.getCryptoTag() });
					// RETORNAMOS 1 PARA INDICAR QUE EL PROCESO HA SIDO REALIZADO CON EXITO
					return 1;
				}

			}
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
			// RETORNAMOS 0 PARA INDICAR QUE EL PROCESO NO HA SIDO REALIZADO CON EXITO
			return 0;
		}
	}

	/**
	 * Comprar fiat
	 * @param idVault
	 * @param amount
	 * @return
	 * @throws SQLException
	 */
	public int buyFiat(int idVault, float amount) throws SQLException {
		try {
			// EMPLEAMOS LA FUNCION PREVIAMENTE DEFINIDA PARA COMPRAR EL FIAT PUES LO
			// TRATAMOS COMO USD
			float price = 1;
			UserCrypto uc = new UserCrypto(0, idVault, "usd", amount);
			buyCrypto(price, "", uc);

			// RETORNAMOS 1 PARA DETERMINAR QUE LA OPERACION SE HA REALIZADO CON EXITO
			return 1;
			// SI SE ENCUENTRA UNA EXCEPCION
		} catch (Exception e) {
			// RETORNAMOS 0 PARA DETERMINAR QUE LA OPERACION NO SE HA REALIZADO CON EXITO
			return 0;
		}

	}
	/**
	 * Comprar crypto con fiat
	 * @param price
	 * @param uc
	 * @return
	 * @throws SQLException
	 */
	public boolean buyCryptoByFiat(float price, UserCrypto uc) throws SQLException {
		// BUSCAMOS SI EL USUARIO POSEE ESTA CRYPTO
		String tempQuery = "select cryptoAmount from usercryptos where idVault = ? and cryptoTag = usd ";
		ResultSet cryptoRs = DbFunctions.Select(tempQuery, new Object[] { uc.getIdVault() });

		if (!(cryptoRs.next())) {
			return false;
		}
		if (uc.getCryptoAmount() * price > cryptoRs.getFloat("cryptoAmount")) {
			return false;
		}
		TransactionService ts = new TransactionService();
		ts.buyCrypto(price, "fiatBuy", uc);

		Float cryptoAmount = cryptoRs.getFloat("cryptoAmount") - price * uc.getCryptoAmount();

		// REALIZAMOS UN UPDATE AL USUARIO DEL crytoAmount
		String Query = " cryptoAmount = ? where idVault = ? and cryptoTag = ? ";
		DbFunctions.Update("usercryptos", Query, new Object[] { cryptoAmount, uc.getIdVault(), uc.getCryptoTag() });

		return true;
	}

	/**
	 * Vender crypto
	 * @param idVault
	 * @param cryptoTag
	 * @param amount
	 * @param value
	 * @return
	 * @throws SQLException
	 */
	public int sellCrypto(int idVault, String cryptoTag, float amount, float value) throws SQLException {
		try {
			
		// BUSCAMOS SI EL USUARIO POSEE ESTA CRYPTO
		String tempQuery = "select cryptoAmount from usercryptos where idVault = ? and cryptoTag = ? ";
		ResultSet cryptoRs = DbFunctions.Select(tempQuery, new Object[] { idVault, cryptoTag });

		float crypto = 0;

		while (cryptoRs.next()) {
			crypto = Float.parseFloat(cryptoRs.getString("cryptoAmount"));
		}

		// SI EL USUARIO NO POSEE LA CRYPTO
		if (crypto == 0) {
			// DEVOLVEMOS 0
			return 0;
			// SI EL USUARIO POSEE LA CRYPTO
		} else {
	
				// REALIZAMOS UN parseFloat al valor del amount de la crypto y le restamos la
				// cantidad que quiere vender el usuario
				float cryptoAmount = crypto - amount;

				// SI EL RESULTADO DE LA OPERACION ES MENOR QUE 0
				if (cryptoAmount < 0) {
					
					
					// RETORNAMOS 0 PARA INDICAR QUE NO POSEE LA CANTIDAD ADECUADA
					return 0;
				}
				// SI EL RESULTADO ES MAYOR O IGUAL A O
				else {
					System.out.println("NO");
					System.out.println("NO");
					System.out.println("NO");
					System.out.println("NO");
					System.out.println("NO");
					float result = amount * value;
					String total = result + "$";

					// REALIZAMOS UN UPDATE AL USUARIO DEL crytoAmount
					String Query = " cryptoAmount = ? where idVault = ? and cryptoTag = ? ";
					DbFunctions.Update("usercryptos", Query, new Object[] { cryptoAmount, idVault, cryptoTag });
					

					// BUY FIAT
					UserCrypto uc = new UserCrypto(0, idVault, "usd", amount);
					buyCrypto(uc.getCryptoAmount(), "sell", uc);

					float fee = getFee(uc.getIdVault());

					Transaction t = new Transaction(0, idVault, "", amount, ("-" + amount * fee / 100), value,
							cryptoTag, "me", "me", "Sell", total, "", "Crypto");
					createTransaction(t);

					// RETORNAMOS 1 PARA INDICAR QUE LA OPERACIÓN SE HA REALIZADO CON ÉXITO
					return 1;
				}}
				// SI SE ENCUENTRA UNA EXCEPCION
			} catch (SQLException e) {
				System.out.println(e);
				// RETORNAMOS 0
				return 0;
			}
		}
	

	/**
	 * Recompensas diarias
	 * @param idVault
	 * @return
	 * @throws SQLException
	 */
	public int dailyQuest(int idVault) throws SQLException {

		// BUSCAMOS EL VALOR DEL ATRIBUTO dailyQuest
		String getDate = "select dailyQuest from userregistered where idVault = ?";
		ResultSet dailyRs = DbFunctions.Select(getDate, new Object[] { idVault });

		String daily = "";
		while (dailyRs.next()) {
			daily = dailyRs.getString("dailyQuest");
		}

		// OBTENEMOS LA FECHA ACTUAL
		LocalDateTime myDateObj = LocalDateTime.now();

		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		String dates = myDateObj.format(myFormatObj);

		if (!(daily.equals(dates))) {

			// REALIZAMOS UN UPDATE AL USUARIO DEL dailyQuest
			String updateQuery = " dailyQuest = '" + dates + "' where idVault = ?";
			DbFunctions.Update("userregistered", updateQuery, new Object[] { idVault });

			// BUSCAMOS EL VALOR DEL ATRIBUTO premium
			String getPremium = "select premium from userregistered where idVault = ? ";
			ResultSet premiumRs = DbFunctions.Select(getPremium, new Object[] { idVault });

			String premium = "";
			while (premiumRs.next()) {
				premium = premiumRs.getString("premium");
			}

			if (premium.equals("1")) {
				UserCrypto uc = new UserCrypto(0, idVault, "btl", 100);
				buyCrypto((float) 0.1, "", uc);
				return 1;
			} else {
				UserCrypto uc = new UserCrypto(0, idVault, "btl", 50);
				buyCrypto((float) 0.1, "", uc);
				return 1;
			}
		}

		return 0;
	}
	
	/**
	 * Enviar crypto
	 * @param idVault
	 * @param amount
	 * @param cryptoTag
	 * @param price
	 * @param username
	 * @return
	 * @throws SQLException
	 */
	public boolean sendCrypto(int idVault, float amount, String cryptoTag, float price, String username)
			throws SQLException {
		// BUSCAMOS EL VALOR DEL ATRIBUTO dailyQuest

		String getCrypto = "select cryptoAmount from usercryptos where idVault = ? and cryptoTag = ? ";
		ResultSet Crypto = DbFunctions.Select(getCrypto, new Object[] { idVault, cryptoTag });

		String getUser = "select idVault from userregistered where username = ?  ";
		ResultSet User = DbFunctions.Select(getUser, new Object[] { username });

		String getSource = "select username from userregistered where idVault= ?  ";
		ResultSet Source = DbFunctions.Select(getSource, new Object[] { idVault });

		if (Crypto.next() == false || User.next() == false || Source.next() == false) {
			return false;
		}
		if (Crypto.getFloat("cryptoAmount") < amount) {
			return false;
		}
		float balance = Crypto.getFloat("cryptoAmount");
		balance = balance - amount;
		String source = Source.getString("username");
		String total = amount * price * -1 + "$";
		System.out.println("BALANCE: " + balance);
		String updateQuery = " cryptoAmount = ? where idVault = ? and cryptotag = ?";
		DbFunctions.Update("usercryptos", updateQuery, new Object[] { balance, idVault, cryptoTag });

		Transaction t = new Transaction(2, idVault, "", amount, "0", price, cryptoTag, username, "me", "Send", total,
				"", "Crypto");
		createTransaction(t);

		UserCrypto uc = new UserCrypto(0, User.getInt("idVault"), cryptoTag, amount);
		buyCrypto(1, "send", uc, source);
		return true;
	}

	public float getFee(int idVault) throws SQLException {
		// BUSCAMOS EL VALOR DEL ATRIBUTO dailyQuest
		String getCommission = "select premium from userregistered where idVault = ? ";
		ResultSet commissionRs = DbFunctions.Select(getCommission, new Object[] { idVault });

		int commission = 0;
		while (commissionRs.next()) {
			commission = commissionRs.getInt("premium");
		}

		System.out.println(commission);
		float Fee = 0;
		String FeeToParse = "";
		if (commission == 0) {
			// BUSCAMOS EL VALOR DEL ATRIBUTO dailyQuest
			String getFee = "select percent from Commissions where id != 'premium' ";
			ResultSet FeeRs = DbFunctions.Select(getFee);

			while (FeeRs.next()) {
				FeeToParse = FeeRs.getString("percent");
			}
		} else if (commission != 0) {
			// BUSCAMOS EL VALOR DEL ATRIBUTO dailyQuest
			String getFee = "select percent from Commissions where id = 'premium' ";
			ResultSet FeeRs = DbFunctions.Select(getFee);

			while (FeeRs.next()) {
				FeeToParse = FeeRs.getString("percent");
			}
		}

		String extractFee = "";
		for (int x = 0; x < FeeToParse.length(); x++) {
			if (!(FeeToParse.charAt(x) == '%')) {
				extractFee += FeeToParse.charAt(x);
			}
		}
		Fee = Float.parseFloat(extractFee);
		return Fee;
	}
}
