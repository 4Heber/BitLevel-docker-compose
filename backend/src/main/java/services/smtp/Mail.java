package services.smtp;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import commonUtil.Util;
import services.sql.DbFunctions;
/**
 * Clase de servicio de mail
 * @author rpere
 *
 */
public class Mail {

	private static String remitente = "cryptoappld@gmail.com";
	private static String password = "jdwqdjbwqi@|#@";
	/**
	 * Constructor completo
	 * @param remitente
	 * @param password
	 */
	public Mail(String remitente, String password) {
		setRemitente(remitente);
		setPassword(password);
	}

	/**
	 * Enviar mail
	 * @param destinatario
	 * @param subject
	 * @param text
	 * @return
	 */
	public static int sendMail(String destinatario, String subject, String text) {

		// Esto es lo que va delante de @gmail.com en tu cuenta de correo. Es el
		// remitente también.
		Properties props = System.getProperties();
		props.put("mail.smtp.host", "smtp.gmail.com"); // El servidor SMTP de Google
		props.put("mail.smtp.user", remitente); // Correo
		props.put("mail.smtp.clave", "jdwqdjbwqi@|#@"); // La clave de la cuenta
		props.put("mail.smtp.auth", "true"); // Usar autenticación mediante usuario y clave
		props.put("mail.smtp.starttls.required", "true");
		props.put("mail.smtp.ssl.protocols", "TLSv1.2");
		props.put("mail.smtp.starttls.enable", "true"); // Para conectar de manera segura al servidor SMTP
		props.put("mail.smtp.port", "587"); // El puerto SMTP seguro de Google

		Session session = Session.getDefaultInstance(props);

		session.getProperties().put("mail.smtp.ssl.trust", "smtp.gmail.com"); // PROPIEDADES PARA CORRECION DE ERRORES
																				// DE VERSION
		session.getProperties().put("mail.smtp.starttls.enable", "true"); // PROPIEDADES PARA CORRECION DE ERRORES DE
																			// VERSION

		MimeMessage message = new MimeMessage(session);

		try {
			message.setFrom(new InternetAddress(remitente));
			message.setRecipients(Message.RecipientType.TO, destinatario); // Se podrían añadir varios de la misma
																			// manera
			message.setSubject(subject);
			message.setText(text);
			Transport transport = session.getTransport("smtp");
			transport.connect("smtp.gmail.com", remitente, password);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
			System.out.println("Message sent successfully");
			return 1;
		} catch (MessagingException me) {
			me.printStackTrace(); // Si se produce un error
			return 0;
		}
	}

	/**
	 * Enviar mailOtp
	 * @param accID
	 * @param password
	 * @return
	 * @throws SQLException
	 */
	public static boolean sendMailOTP(String accID, String password) throws SQLException {

		// CREAMOS 2 CONSULTAS PARA DETERMINAR SI EL USUARIO SE LOGEA CON USERNAME O
		// MAIL

		String userCompare = "select idVault from userregistered where ( username = ? or mail = ? )and password = ? ";

		ResultSet unameRs = DbFunctions.Select(userCompare, new Object[] { accID, accID, password });

		unameRs.next();

		// BUSCAMOS EL CORREO DEL USUARIO
		String tempQuery = "select mail from userregistered where (username = ? or mail = ?) and password =  ? ";
		ResultSet mailUserRs = DbFunctions.Select(tempQuery, new Object[] { accID, accID, password });

		mailUserRs.next();
		String mailUser = mailUserRs.getString("mail");

		// GENERAMOS UN OTP DE 8 DÍGITOS
		String OTP;
		OTP = Util.genreRandomString();

		// REALIZAMOS UN UPDATE AL USUARIO DEL mailOTP
		String Query = " mailOTP = ? where (username = ? or mail = ?) and password = ? ";
		DbFunctions.Update("userregistered", Query, new Object[] { OTP, accID, accID, password });

		// ENVIAMOS UN MAIL AL USUARIO INFIRMANDO DEL NUEVO OTP
		Mail.sendMail(mailUser, "BitLevel Verification CODE", "This is your one time use otp code " + OTP);

		// DEVOLVEMOS 1 INDICANDO QUE SE HA COMPLETADO LA OPERACIÓN COMPLETAMENTE
		return true;
		// DEVOLVEMOS 0 INDICANDO QUE NO SE ENCONTRÓ NINGUN USUARIO

	}

	/**
	 * Enviar mailOtp
	 * @param accID
	 * @return
	 * @throws SQLException
	 */
	public static int sendMailOTP(String accID) throws SQLException {

		// CREAMOS 2 CONSULTAS PARA DETERMINAR SI EL USUARIO SE LOGEA CON USERNAME O
		// MAIL
		String userCompare = "select idVault from userregistered where username = ? or mail = ? ";

		ResultSet unameRs = DbFunctions.Select(userCompare, new Object[] { accID, accID });

		if (unameRs.next() == false) {
			return 0;
		}
		unameRs.next();
		int idVault = unameRs.getInt("idVault");

		// SI EL VALOR uname NO ES VACÍO, SIGNIFICA QUE SE HA LOGUEADO CON USERNAME
		System.out.println("SE HA INICIADO POR USER");

		// BUSCAMOS EL CORREO DEL USUARIO
		String tempQuery = "select mail from userregistered where idVault = ? ";
		ResultSet mailUserRs = DbFunctions.Select(tempQuery, new Object[] { idVault });

		String mailUser = mailUserRs.getString("mail");

		// GENERAMOS UN OTP DE 8 DÍGITOS
		String OTP;
		OTP = Util.genreRandomString();

		// REALIZAMOS UN UPDATE AL USUARIO DEL mailOTP
		String Query = " mailOTP = ? where idVault = ? ";
		DbFunctions.Update("userregistered", Query, new Object[] { OTP, idVault });

		// ENVIAMOS UN MAIL AL USUARIO INFIRMANDO DEL NUEVO OTP
		Mail.sendMail(mailUser, "BitLevel Verification CODE", "This is your one use otp code " + OTP);

		// DEVOLVEMOS 1 INDICANDO QUE SE HA COMPLETADO LA OPERACIÓN COMPLETAMENTE
		return 1;
	}

	/**
	 * GETTERS / SETTERS
	 * @return
	 */
	public String getRemitente() {
		return remitente;
	}

	public void setRemitente(String remitente) {
		Mail.remitente = remitente;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		Mail.password = password;
	}

}
