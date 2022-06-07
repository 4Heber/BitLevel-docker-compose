package services.users;

import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import commonUtil.Util;

import models.users.UserRegistered;

import services.smtp.Mail;
import services.sql.DbFunctions;

public class UserService {

	// CLEAN METHODS

	// REGISTRO (DONE)
	public boolean register(String username, String mail, String password) throws SQLException {

		// EJECUTAMOS LA SIGUIENTE QUERY PARA COMPROBAR QUE NO HAY OTRO USUARIO CON EL
		// MISMO USERNAME | MAIL
		String usernameCompare = "select username from userregistered where username = ? || mail = ? ";

		ResultSet accountRs = DbFunctions.Select(usernameCompare, new Object[] { username, mail });
		String account = "";

		while (accountRs.next()) {
			account = accountRs.getString("username");

		}

		// SI SE ENCUENTRAN RESULTADOS
		if (account != "") {

			// DEVOLVEMOS 0 PARA INFORMAR DE QUE YA EXISTE UN USUARIO CON ESOS DATOS
			return false;

			// SI NO SE ENCUNETRAN RESULTADOS
		} else {

			// CREAMOS UN OBJETO LocalDateTime para extraer la fecha actual
			LocalDateTime myDateObj = LocalDateTime.now();

			// DEFINIMOS EL PATTERN
			DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd");

			// ASIGNAMOS EL VALOR DE LA FECHA A UN STRING
			String dates = myDateObj.format(myFormatObj);

			// EXTRAEMOS EL ÚLTIMO idVault
			ResultSet incrementalRs = DbFunctions
					.Select("select idVault from userregistered order by idVault desc limit 1");
			int incrementals = 0;
			while (incrementalRs.next()) {
				incrementals = incrementalRs.getInt("idVault");
			}
			incrementals++;

			// INSERTAMOS EL USUARIO
			String insertQuery = "?,null,null,?,?,?,0,0,'undefined',0,0,'undefined',?,0,'User account','00-00-00'";
			DbFunctions.Insert("userregistered", insertQuery,
					new Object[] { incrementals, username, password, mail, dates });

			// DEVOLVEMOS 1 PARA INDICAR QUE LA OPERACIÓN SE HA REALIZADO CON ÉXITO
			return true;
		}
	}

	// INICIO DE SESIÓN (DONE)
	public int login(String accID, String password, String OTP) throws SQLException {

		// CREAMOS 2 CONSULTAS PARA DETERMINAR SI EL USUARIO SE LOGEA CON USERNAME O
		// MAIL

		String userCompare = "select idVault from userregistered where ( username = ? or mail = ? )and password = ? and mailOtp = ? ";

		ResultSet unameRs = DbFunctions.Select(userCompare, new Object[] { accID, accID, password, OTP });

		unameRs.next();

		// BUSCAMOS EL CORREO DEL USUARIO
		String tempQuery = "select mail from userregistered where ( username = ? or mail = ? ) and password = ? ";
		ResultSet mailRs = DbFunctions.Select(tempQuery, new Object[] { accID, accID, password });

		mailRs.next();
		String mailUser = mailRs.getString("mail");

		// REALIZAMOS UN UPDATE AL USUARIO DEL mailOTP
		String Query = " mailOTP = 'undefined' where ( username = ? or mail = ? ) and password = ? ";
		DbFunctions.Update("userregistered", Query, new Object[] { accID, accID, password });

		// ENVIAMOS UN MAIL AL USUARIO INFIRMANDO DEL NUEVO OTP
		Mail.sendMail(mailUser, "New login detected Login ", "Hello " + mailUser
				+ "! We have detected a new login into your account, please if it was not you make sure to change your password");

		// DEVOLVEMOS 1 INDICANDO QUE SE HA COMPLETADO LA OPERACIÓN COMPLETAMENTE
		return 1;

	}

	// RESETEAR CONTRASEÑA (DONE)
	public int resetPassword(String accID, String password, String newPassword) throws SQLException {

		// CREAMOS 2 CONSULTAS DE LAS CUALES uname Y umail DETERMINAN SI EL USUARIO SE
		// LOGEA CON
		// USERNAME O MAIL
		String userCompare = "select idVault from userregistered where (username = ? or mail = ?) and password = ? ";

		ResultSet unameRs = DbFunctions.Select(userCompare, new Object[] { accID, accID, password});

		unameRs.next();
		int idVault = unameRs.getInt("idVault");

		// REALIZAMOS UN UPDATE AL USUARIO DEL PASSWORD
		String updateQuery = " password = ? where idVault = ? ";
		DbFunctions.Update("userregistered", updateQuery, new Object[] { newPassword, idVault });

		// DEVOLVEMOS 1 INDICANDO QUE EL MAIL SE HA ACTUALIAZDO
		return 1;
	}

	// RESETEAR 2FA (FALTA)
	public boolean reset2fa(String mail, String password, String newMail) throws SQLException {

		// CREAMOS 3 CONSULTAS DE LAS CUALES uname Y umail DETERMINAN SI EL USUARIO SE
		// LOGEA CON
		// USERNAME O MAIL Y mailInUse DETERMINA SI EL MAIL YA ESTÁ EN USO
		String usernameCompare = "select idVault from userregistered where  mail = ?  and password = ? ";
		String mailFinder = "select idVault from userregistered where mail = ? ";

		ResultSet unameRs = DbFunctions.Select(usernameCompare, new Object[] { mail, password });
		ResultSet mailsInUseRs = DbFunctions.Select(mailFinder, new Object[] { newMail });

		int idVault = 0;

		int mailsInUse = 0;

		if (unameRs.next() == false) {
			return false;
		}
		idVault = unameRs.getInt("idVault");
		if (mailsInUseRs.next() == true) {
			return false;
		}

		

		// INSERTAMOS EL USUARIO
		String updateQuery = " mail = ? where mail = ?";
		DbFunctions.Update("userregistered", updateQuery, new Object[] { newMail, mail });

		Mail.sendMail(mail, "2FA RESET",
				"Hello " + mail + ",\n You have successfully changed your mail address to " + newMail
						+ ". \n If it was not you, please make sure to contact us by replying this mail");
		// DEVOLVEMOS 1 INDICANDO QUE EL MAIL SE HA ACTUALIAZDO
		return true;

	}

	public boolean resetImage(int idVault, String image) throws SQLException {

		// CREAMOS 2 CONSULTAS DE LAS CUALES uname Y umail DETERMINAN SI EL USUARIO SE
		// LOGEA CON
		// USERNAME O MAIL
		String userCompare = "select idVault from userregistered where idVault = ?";

		ResultSet unameRs = DbFunctions.Select(userCompare, new Object[] { idVault });

		if (unameRs.next() == false) {
			return false;
		}
		
		System.out.println("Url before decoding: " +image);
		
		String imageUrlDecoded = java.net.URLDecoder.decode(image, StandardCharsets.UTF_8);
		
		System.out.println("Url after decoding: " +imageUrlDecoded);
		
		// REALIZAMOS UN UPDATE AL USUARIO DEL PASSWORD
		String updateQuery = " imageProfile = ? where idVault = ? ";
		DbFunctions.Update("userregistered", updateQuery, new Object[] { image, idVault });

		// DEVOLVEMOS 1 INDICANDO QUE EL MAIL SE HA ACTUALIAZDO
		return true;
	}

	// OBTENER COOKIES (DONE) FALTA COMENTAR
	public String getCookies(String accID) throws SQLException {

		String queryUser = "select * from userregistered where username = ? or mail = ? ";
		ResultSet userRs = DbFunctions.Select(queryUser, new Object[] { accID, accID });

		String userToken = null;
		userRs.next();

		UserRegistered ur = new UserRegistered(userRs.getInt("idVault"), userRs.getString("username"),
				userRs.getString("tempToken"), userRs.getString("tokenDate"), userRs.getString("password"),
				userRs.getBoolean("premium"), userRs.getString("mail"), userRs.getInt("transactionsCount"),
				userRs.getBoolean("AccVerified"), userRs.getInt("attempts"), userRs.getString("mailOtp"),
				userRs.getInt("mailed"), userRs.getString("imageProfile"), userRs.getString("creationDate"),
				userRs.getString("description"), userRs.getString("dailyQuest")

		);

		userToken = ur.getToken();

		String jsonInicio = "{\r\n" + "     \"token\": [";
		String jsonFinal = "\n    }]\r\n" + "}";

		/*
		 * if (userToken==null) {
		 * 
		 * // OBTENEMOS LA FECHA ACTUAL LocalDateTime myDateObj = LocalDateTime.now();
		 * myDateObj = myDateObj.plusDays(1); DateTimeFormatter myFormatObj =
		 * DateTimeFormatter.ofPattern("yyyy-MM-dd"); String dates =
		 * myDateObj.format(myFormatObj);
		 * 
		 * String querySetToken = " tempToken = ? and tokenDate = ? where idVault = ? ";
		 * DbFunctions.Update("userregistered", querySetToken, new Object[] {
		 * genreRandomString(), dates, ur.getIdVault() });
		 * 
		 * 
		 * } else {
		 * 
		 * 
		 * DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		 * LocalDateTime tokenDate = (LocalDateTime)
		 * myFormatObj.parse(ur.getTokenDate()); LocalDateTime myDateObj =
		 * LocalDateTime.now();
		 * 
		 * if (tokenDate.isBefore(myDateObj)) {
		 * 
		 * // OBTENEMOS LA FECHA ACTUAL LocalDateTime myDateObj = LocalDateTime.now();
		 * myDateObj = myDateObj.plusDays(1); DateTimeFormatter myFormatObj =
		 * DateTimeFormatter.ofPattern("yyyy-MM-dd"); String dates =
		 * myDateObj.format(myFormatObj);
		 * 
		 * String otp = genreRandomString();
		 * 
		 * String querySetToken =
		 * " tempToken = ? and tokenDate = ? where username = ? ";
		 * 
		 * DbFunctions.Update("userregistered", querySetToken, new Object[] { otp,
		 * dates, accID });
		 * 
		 * }
		 */

		LocalDateTime myDateObj = LocalDateTime.now();
		myDateObj = myDateObj.plusDays(1);
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String dates = myDateObj.format(myFormatObj);

		userToken = Util.genreRandomString();

		String querySetToken = " tempToken = ? , tokenDate = ? where idVault = ? ";
		DbFunctions.Update("userregistered", querySetToken, new Object[] { userToken, dates, ur.getIdVault() });

		return (jsonInicio + "{\r\n" + "        \"token\": \"" + userToken + "\"" + jsonFinal);

	}

	// OBTENER JSON (DONE) FALTA COMENTAR
	public UserRegistered getUserByToken(String token) throws SQLException {

		/*
		 * String estructuraInicio = "{\r\n" + "   \"User\": {\r\n" +
		 * "      \"info\": {\n"; String estructuraFinal = "   }\r\n" + "}\n}"; String
		 * content = "";
		 */

		String queryUser = "select * from userregistered where tempToken = ? ";
		ResultSet userRs = DbFunctions.Select(queryUser, new Object[] { token });

		if (userRs.next() == false) {
			return null;
		}

		UserRegistered ur = new UserRegistered(userRs.getInt("idVault"), userRs.getString("username"),
				userRs.getString("tempToken"), userRs.getString("tokenDate"), userRs.getString("password"),
				userRs.getBoolean("premium"), userRs.getString("mail"), userRs.getInt("transactionsCount"),
				userRs.getBoolean("AccVerified"), userRs.getInt("attempts"), userRs.getString("mailOtp"),
				userRs.getInt("mailed"), userRs.getString("imageProfile")+ "", userRs.getString("creationDate"),
				userRs.getString("description"), userRs.getString("dailyQuest")

		);

		// String coins = getBalance(ur.getIdVault());

		// String fiat = getFiat(ur.getIdVault());

		// return estructuraInicio + content + coins + "]," + fiat + estructuraFinal;

		return ur;

	}

	// GENERAR TRANSACCION (DONE) FALTA COMENTAR
	/*
	 * public int createCard(String accID) throws SQLException { // CREAMOS 2
	 * CONSULTAS PARA DETERMINAR SI EL USUARIO SE LOGEA CON USERNAME O
	 * 
	 * String usernameCompare =
	 * "select username from userregistered where username = '" + accID + "'";
	 * String emailCompare = "select username from userregistered where mail = '" +
	 * accID + "' ";
	 * 
	 * // SI NO ENCONTRAMOS NINGÚN VALOR, DEVOLVEMOS 0 if (uname == "" && umail ==
	 * "") { System.out.println("FAILURE"); // DEVOLVEMOS 0 INDICANDO QUE NO SE
	 * ENCONTRÓ NINGUN USUARIO return 0; } else if (uname != "") { String
	 * premiumCompare = "select premium from userregistered where username = ? ";
	 * ResultSet premiumRs = DbFunctions.Select(premiumCompare, new Object[] { accID
	 * }); String premium = ""; while (premiumRs.next()) { premium =
	 * premiumRs.getString("premium"); }
	 * 
	 * if (premium.equals("1")) { String getIdVault =
	 * "select idVault from userregistered where username = ? "; ResultSet idVaultRs
	 * = DbFunctions.Select(getIdVault, new Object[] { accID }); String idVault =
	 * ""; while (idVaultRs.next()) { idVault = idVaultRs.getString("idVault"); }
	 * 
	 * String compareCard = "select cardID from VirtualCards where idVault = ? ";
	 * ResultSet cardRs = DbFunctions.Select(compareCard, new Object[] { idVault });
	 * 
	 * String card = ""; while (cardRs.next()) { card = cardRs.getString("cardID");
	 * }
	 * 
	 * if (card.equals("")) {
	 * 
	 * String date = getNewDate(); String digits = getRandomDigits(16);
	 * 
	 * String cvc = getRandomDigits(3); String getIdCard =
	 * "select max(id) as last from VirtualCard";
	 * System.out.println(digits.length());
	 * 
	 * ResultSet cardIdRs = DbFunctions.Select(getIdCard);
	 * 
	 * float balance = 0; int cardID = 0;
	 * 
	 * while (cardIdRs.next()) { cardID = cardIdRs.getInt("last"); }
	 * 
	 * String insertQuery = "?,?,?,?,?,?";
	 * 
	 * DbFunctions.Insert("VirtualCards", insertQuery, new Object[] { cardID,
	 * digits, cvc, date, balance, idVault });
	 * 
	 * return 1; } else { return 0; } } } else if (umail != "") { String
	 * premiumCompare = "select premium from userregistered where mail = ? ";
	 * ResultSet premiumRs = DbFunctions.Select(premiumCompare, new Object[] { accID
	 * }); String premium = ""; while (premiumRs.next()) { premium =
	 * premiumRs.getString("premium"); } if (premium.equals("1")) { String
	 * getIdVault = "select idVault from userregistered where mail = ? "; ResultSet
	 * idVaultRs = DbFunctions.Select(getIdVault, new Object[] { accID }); String
	 * idVault = ""; while (idVaultRs.next()) { idVault =
	 * idVaultRs.getString("idVault"); }
	 * 
	 * String compareCard = "select cardID from VirtualCards where idVault = ? ";
	 * ResultSet cardRs = DbFunctions.Select(compareCard, new Object[] { idVault });
	 * String card = ""; while (cardRs.next()) { card = cardRs.getString("cardID");
	 * } if (card.equals("")) {
	 * 
	 * String date = getNewDate(); String digits = getRandomDigits(16);
	 * 
	 * String cvc = getRandomDigits(3); String getIdCard =
	 * "select max(id) as cardMax from VirtualCard"; ResultSet cardIdRs =
	 * DbFunctions.Select(getIdCard); float balance = 0;
	 * 
	 * int cardId = 0; while (cardIdRs.next()) { cardId =
	 * cardIdRs.getInt("cardMax"); } String insertQuery = "?,?,?,?,?";
	 * DbFunctions.Insert("VirtualCards", insertQuery, new Object[] { cardId,
	 * digits, cvc, date, balance, idVault });
	 * 
	 * return 1; } else { return 0; } } } return 0; }
	 */

}