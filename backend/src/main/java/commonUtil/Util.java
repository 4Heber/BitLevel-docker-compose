package commonUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Util {

	// METHODS
	public static String getRandomDigits(int ammount) {
		// DEFINIMOS UN STRING QUE DEVUELVA UNDEFINED POR SI SE PRODUCE UNA EXCEPCIÓN

		// DEFINIMOS UN STRING VACÍO
		String random = "";

		// DEFINIMOS QUE CARÁCTERES PUEDE CONTENER EL STRING CREADO PREVIAMENTE
		String characs = "1234567890";

		for (int x = 1; x <= ammount; x = x + 1) {
			if (x == 4) {
				int randomIndex = (int) (characs.length() * Math.random());
				random += characs.charAt(randomIndex);
				random += "-";
			} else if (x == 8) {
				int randomIndex = (int) (characs.length() * Math.random());
				random += characs.charAt(randomIndex);
				random += "-";
			} else if (x == 12) {
				int randomIndex = (int) (characs.length() * Math.random());
				random += characs.charAt(randomIndex);
				random += "-";
			} else if (x != 4 && x != 8 && x != 12) {
				int randomIndex = (int) (characs.length() * Math.random());
				random += characs.charAt(randomIndex);

			}
		}

		// DEVOLVEMOS EL STRING OTP
		return random;

	}

	public static String getNewDate() {
		LocalDateTime myDateObj = LocalDateTime.now();

		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		String dates = myDateObj.format(myFormatObj);
		String newDate = "";
		for (int x = 0; x < dates.length(); x++) {

			if (x == 3) {

				newDate += dates.charAt(x);
				int date = Integer.parseInt(newDate);
				date = date + 3;
				newDate = String.valueOf(date);
			} else {
				newDate += dates.charAt(x);
			}
		}
		return newDate;

	}

	// GENERAR OTP (DONE)
	public static String genreRandomString() {

		// DEFINIMOS UN STRING QUE DEVUELVA UNDEFINED POR SI SE PRODUCE UNA EXCEPCIÓN
		String otp2 = "UNDEFINED";
		try {

			// DEFINIMOS UN STRING VACÍO
			String otp = "";

			// DEFINIMOS QUE CARÁCTERES PUEDE CONTENER EL STRING CREADO PREVIAMENTE
			String characs = "a1b2c3d4e5f6g7h8j9k0lpoiuytreASDFGJEWFwqz";

			// HACEMOS UN BUCLE QUE INTRODUCE EN EL STRING VACÍO "otp" 8 CARÁCTERES
			// ALEATORIOS DE LA CADENA DE CARÁCTERES "characs"
			for (int x = 0; x < 8; x = x + 1) {
				int randomIndex = (int) (characs.length() * Math.random());
				otp += characs.charAt(randomIndex);

			}

			// DEVOLVEMOS EL STRING OTP
			return otp;

		} catch (Exception e) {

			// INFORMAMOS DEL ERROR
			System.out.println(e);

			// DEVOLVEMOS EL STRING OTP2
			return otp2;

		}

	}
}
