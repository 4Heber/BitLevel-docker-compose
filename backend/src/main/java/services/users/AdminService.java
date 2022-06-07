package services.users;

import java.sql.ResultSet;
import java.sql.SQLException;

import services.smtp.Mail;
import services.sql.DbFunctions;



public class AdminService {

	// MOSTRAR USUARIOS EN "PELIGRO"
	public static int[] getMailicious() throws SQLException {

		String getAmmount = "select count(idVault) as number from userregistered ";

		ResultSet amountRs = DbFunctions.Select(getAmmount);

		int amount = 0;
		while (amountRs.next()) {
			amount = amountRs.getInt("number");
		}

		int[] tempaccs = new int[amount];
		int position = 0;
		for (int x = 1; x <= amount; x++) {
			String getSecurity = "select attempts from userregistered where idVault = " + x;

			ResultSet securityRs = DbFunctions.Select(getSecurity);

			int security = 0;
			while (securityRs.next()) {
				security = securityRs.getInt("attemps");
			}
			System.out.println(security);
			if (security >= 3) {

				tempaccs[position] = x;
				position++;

			}
		}
		int diferencia = 0;
		for (int z = 0; z < tempaccs.length; z++) {
			if (tempaccs[z] == 0) {
				diferencia++;

			}
		}
		int[] warnings = new int[diferencia];
		int difPosition = 0;
		for (int y = 0; y < tempaccs.length; y++) {
			if (tempaccs[y] == 0) {
				difPosition++;

			} else {
				warnings[y - difPosition] = tempaccs[y];
				difPosition = 0;

			}
		}
		if (warnings.length == 0) {
			System.out.println("ALL THE ACCOUNTS ARE SECURED");
		} else {
			System.out.println("THOSE ACCOUNTS NEED TO BE SECURED");
			for (int c = 0; c < warnings.length; c++) {
				System.out.println(warnings[c]);

			}
		}
		return warnings;

	}

	// AVISAR A USUARIOS EN "PELIGRO"
	public static int mailMalicious() throws SQLException {

		String text = "Hello, we have detected unsual activity on your account, you should change your password!";
		String subject = "CRITICAL SECURITY ON YOUR ACCOUNT";

		int[] accs = getMailicious();

		for (int x = 0; x < accs.length; x++) {

			// BUSCAMOS EL CORREO DEL USUARIO
			String tempQuery = "select mail from userregistered where idVault = ? ";
			ResultSet mailUserRs = DbFunctions.Select(tempQuery, new Object[] { accs[x] });
			String mailUser = "";
			while (mailUserRs.next()) {
				mailUser = mailUserRs.getString("mail");
			}
			Mail.sendMail(mailUser, subject, text);

		}
		return 1;
	}

}
