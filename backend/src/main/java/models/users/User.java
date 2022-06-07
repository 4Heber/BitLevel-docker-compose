package models.users;





public abstract class User {

	private String username;
	private String token;
	private int idVault;

	public User() {

	}

	public User(String username, String token, int idVault) {

		this.username = username;
		this.token = token;
		this.idVault = idVault;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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