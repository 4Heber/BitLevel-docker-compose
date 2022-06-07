package models.crypto;

public class Commission {

	private String id;
	private String percent;

	public Commission(String id, String percent) {

		setId(id);
		setPercent(percent);

	}

	// GETTERS | SETTERS

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPercent() {
		return percent;
	}

	public void setPercent(String percent) {
		this.percent = percent;
	}

}
