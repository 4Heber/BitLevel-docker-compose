package services.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Clase de conexion a base de datos
 * 
 * @author rpere
 *
 */
public class DbConnection {

	// DEFINITION OF URL
	private String url;

	// DEFINITION OF USERNAME
	private String username;

	// DEFINITION OF PASSWORD
	private String password;

	/**
	 * Constructor completo
	 * 
	 * @param url
	 * @param username
	 * @param password
	 */
	public DbConnection(String url, String username, String password) {

		setUrl(url);
		setPassword(password);
		setUsername(username);

	}

//METHODS

	/**
	 * crear conexion
	 * 
	 * @param url1
	 * @param username1
	 * @param password1
	 * @return
	 */
	public DbConnection setConnection(String url1, String username1, String password1) {

		DbConnection values = new DbConnection(url1, username1, password1);

		return values;

	}

	/**
	 * Empezar conexion
	 * 
	 * @param db
	 * @return
	 * @throws SQLException
	 */
	public static Connection startConection(DbConnection db) throws SQLException {

		Connection con = DriverManager.getConnection(db.url, db.username, db.password);

		return con;

	}

	/**
	 * Cerrar conexion
	 * 
	 * @param st
	 * @param con
	 * @throws SQLException
	 */
	public static void closeConection(Statement st, Connection con) throws SQLException {

		st.close();
		con.close();

	}

	/**
	 * GETTERS / SETTERS
	 * 
	 * @return
	 */
	// URL
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	// USERNAME
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	// PASSWORD
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
