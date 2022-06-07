package models.users;





public class UserAdmin extends UserRegistered  {

	private String adminID;
	private String password;
	private String mail;
	private int attempts;
	private String mailOTP;
	private int mailed;
	private String imageProfile;
	private String creationDate;
	private Boolean admin;

	// CONSTRUCTOR VACÍO

	public UserAdmin() {

	}

	// CONSTRUCTOR COMPLETO

	public UserAdmin(String adminID, String password, String mail, int attempts, String mailOTP, int mailed,
			String imageProfile, String creationDate, Boolean admin) {

		this.adminID = adminID;
		this.password = password;
		this.mail = mail;
		this.attempts = attempts;
		this.mailOTP = mailOTP;
		this.mailed = mailed;
		this.imageProfile = imageProfile;
		this.creationDate = creationDate;
		this.admin = admin;

	}

	// METHODS

	
	
	
	// GETTERS AND SETTERS

	public String getAdminID() {
		return adminID;
	}

	public void setAdminID(String adminID) {
		this.adminID = adminID;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public int getAttempts() {
		return attempts;
	}

	public void setAttempts(int attempts) {
		this.attempts = attempts;
	}

	public String getMailOTP() {
		return mailOTP;
	}

	public void setMailOTP(String mailOTP) {
		this.mailOTP = mailOTP;
	}

	public int getMailed() {
		return mailed;
	}

	public void setMailed(int mailed) {
		this.mailed = mailed;
	}

	public String getImageProfile() {
		return imageProfile;
	}

	public void setImageProfile(String imageProfile) {
		this.imageProfile = imageProfile;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public Boolean getAdmin() {
		return admin;
	}

	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}

}
