package services.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase de metodos para consultas SQL
 * 
 * @author rpere
 *
 */
public class DbFunctions {

	private static String username = "root";

	private static String password = "root";

	private static String url = "jdbc:mysql://localhost:3306/bitlevel?useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";;

	/**
	 * QUERY Select
	 * 
	 * @param query
	 * @return
	 * @throws SQLException
	 */
	public static ResultSet Select(String query) throws SQLException {

		return Select(query, null);
	}

	/**
	 * QUERY Select
	 * 
	 * @param query
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public static ResultSet Select(String query, Object[] params) throws SQLException {

		System.out.print(url);
		DbConnection dbInfo = new DbConnection(url, username, password);

		// CONECTION TO SQL
		Connection con = DbConnection.startConection(dbInfo);

		System.out.println("Executing query (" + (params != null ? params.length : 0) + " params): " + query);

		// DEFINE STATEMENT
		PreparedStatement st = con.prepareStatement(query);

		if (params != null) {
			for (int x = 0; x < params.length; x++) {
				// System.out.println(params[x]);
				st.setObject(x + 1, params[x]);
			}
		}

		// QUERY
		ResultSet rs = st.executeQuery();

		// RETURN VALUE
		return rs;

	}

	/**
	 * QUERY Insert
	 * 
	 * @param table
	 * @param insert_Value
	 * @throws SQLException
	 */
	public static void Insert(String table, String insert_Value) throws SQLException {
		Insert(table, insert_Value, null);
	}

	/**
	 * QUERY Insert
	 * 
	 * @param table
	 * @param insert_Value
	 * @param params
	 * @throws SQLException
	 */
	public static void Insert(String table, String insert_Value, Object[] params) throws SQLException {

		DbConnection dbInfo = new DbConnection(url, username, password);

		String query = "INSERT INTO " + table + " VALUES (" + insert_Value + ")";

		// CONECTION TO SQL
		Connection con = DbConnection.startConection(dbInfo);

		// DEFINE STATEMENT
		PreparedStatement st = con.prepareStatement(query);

		if (params != null) {
			for (int x = 0; x < params.length; x++) {
				System.out.println(params[x]);
				st.setObject(x + 1, params[x]);
			}
		}

		// QUERY
		st.executeUpdate();

		// CLOSE CONNECTION
		DbConnection.closeConection(st, con);

	}

	/**
	 * QUERY Update
	 * 
	 * @param table
	 * @param insert_Value
	 * @throws SQLException
	 */
	public static void Update(String table, String insert_Value) throws SQLException {

		Update(table, insert_Value, null);
	}

	/**
	 * QUERY Update
	 * 
	 * @param table
	 * @param insert_Value
	 * @param params
	 * @throws SQLException
	 */
	public static void Update(String table, String insert_Value, Object[] params) throws SQLException {

		DbConnection dbInfo = new DbConnection(url, username, password);
		String query = "UPDATE " + table + " SET" + insert_Value;
		// CONECTION TO SQL
		Connection con = DbConnection.startConection(dbInfo);

		// DEFINE STATEMENT
		PreparedStatement st = con.prepareStatement(query);

		if (params != null) {
			for (int x = 0; x < params.length; x++) {
				System.out.println(params[x]);
				st.setObject(x + 1, params[x]);
			}
		}
		System.out.println("Update query : " + query);

		// QUERY
		st.executeUpdate();

		// CLOSE CONNECTION
		DbConnection.closeConection(st, con);

	}

	/**
	 * QUERY Delete
	 * 
	 * @param table
	 * @param delete_Value
	 * @param params
	 * @throws SQLException
	 */
	public static void Delete(String table, String delete_Value, Object[] params) throws SQLException {

		DbConnection dbInfo = new DbConnection(url, username, password);
		String query = "DELETE FROM " + table + " WHERE" + delete_Value;

		// CONECTION TO SQL
		Connection con = DbConnection.startConection(dbInfo);

		// DEFINE STATEMENT
		PreparedStatement st = con.prepareStatement(query);

		if (params != null) {
			for (int x = 0; x < params.length; x++) {

				st.setObject(x + 1, params[x]);
			}
		}
		System.out.println("Delete query : " + query);

		// QUERY
		st.executeUpdate();

		// CLOSE CONNECTION
		DbConnection.closeConection(st, con);

	}

}
