package models.users;

public class UserNonRegistered extends User {

	private String accountName;
	private String token;
	private int idVault;
	
	// CONSTRUCTOR VACÍO
	public UserNonRegistered() {

	}

	// CONSTRUCTOR COMPLETO
	public UserNonRegistered(String accountName, String token, int idVault) {

		setAccountName(accountName);
		setToken(token);
		setIdVault(idVault);
		
	}
	
	// GETTERS Y SETTERS
	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getIdVault() {
		return idVault;
	}

	public void setIdVault(int idVault) {
		this.idVault = idVault;
	}

	
	
	
}
