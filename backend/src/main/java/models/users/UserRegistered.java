package models.users;




/**
 * Clase UserRegistered
 * @author rafa,kevin,heber
 *
 */
public class UserRegistered extends User {

	private String tokenDate;
	private String password;
	private boolean premium;
	private String mail;
	private float credit;
	private int transactionCount;
	private boolean accountVerfication;
	private int attempts;
	private String mailOTP;
	private int mailed;
	private String imageProfile;
	private String creationDate;
	private String description;
	private String dailyQuest;



	/**
	 * Constructor vacio
	 */
	public UserRegistered() {

	}

	/**
	 * Constructor completo
	 * @param idVault
	 * @param username
	 * @param token
	 * @param tokenDate
	 * @param password
	 * @param premium
	 * @param mail
	 * @param transactionCount
	 * @param accountVerification
	 * @param attempts
	 * @param mailOTP
	 * @param mailed
	 * @param imageProfile
	 * @param creationDate
	 * @param description
	 * @param dailyQuest
	 */
	public UserRegistered(int idVault,String username,String token, String tokenDate, String password, boolean premium, String mail,
			int transactionCount , boolean accountVerification, int attempts, String mailOTP, int mailed,
			String imageProfile, String creationDate, String description, String dailyQuest) {
		
		
		setIdVault(idVault);
		setUsername(username);
		setToken(token);
		setTokenDate(tokenDate);
		setPassword(password);
		setPremium(premium);
		setMail(mail);
		setTransactionCount(transactionCount);
		setAccountVerfication(accountVerification);
		setAttempts(attempts);
		setMailOTP(mailOTP);
		setMailed(mailed);
		setImageProfile(imageProfile);
		setCreationDate(creationDate);
		setDescription(description);
		setDailyQuest(dailyQuest);

	}
	
	/**
	 * Funcion para convertir el valor de los atributos a Json para pasar al frontend el usuario

	 * @return
	 */
	public String toJson() {
		
		String content = "{";
		content += "\"idVault\": \"" + this.getIdVault() + "\",\n";
		content += "\"username\": \"" + this.getUsername() + "\",\n";
		content += "\"mail\": \"" + this.getMail() + "\",\n";
		content += "\"attempts\": \"" + this.getAttempts() + "\",\n";
		content += "\"creationDate\": \"" + this.getCreationDate() + "\",\n";
		content += "\"image\": \"" + this.getImageProfile() + "\",\n";
		content += "\"premium\": \"" +this.isPremium() + "\",\n";
		
		return content;
	}
	
	/**
	 * GETTERS / SETTERS
	 * @return
	 */
	public String getTokenDate() {
		return tokenDate;
	}

	public void setTokenDate(String tokenDate) {
		this.tokenDate = tokenDate;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isPremium() {
		return premium;
	}

	public void setPremium(boolean premium) {
		this.premium = premium;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public float getCredit() {
		return credit;
	}

	public void setCredit(float credit) {
		this.credit = credit;
	}

	public int getTransactionCount() {
		return transactionCount;
	}

	public void setTransactionCount(int transactionCount) {
		this.transactionCount = transactionCount;
	}

	public boolean isAccountVerfication() {
		return accountVerfication;
	}

	public void setAccountVerfication(boolean accountVerfication) {
		this.accountVerfication = accountVerfication;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDailyQuest() {
		return dailyQuest;
	}

	public void setDailyQuest(String dailyQuest) {
		this.dailyQuest = dailyQuest;
	}
	
			
			
	// GETTERS AND SETTERS

	

	
}
