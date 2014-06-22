package model.mapilelu.potable_001;


/**
 * 
 * esta clase contiene las variables, los constructores y los getters y setters
 * 
 */

public class Main_model {
	
	//declaramos e inicializamos las variables 
	private String fountainName="";
	private Working isItWorking=Working.DOESWORK;
	private String notes="";
	private String latitude="";
	private String longitude="";
	//las opciones de nuestros RadioGroups
	public enum Working {DOESWORK, TAP, OTHER}
	
	public Main_model() {
		super();
		this.fountainName = fountainName;
		this.isItWorking = isItWorking;
		this.notes = notes;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	
	public Main_model(String fountainName, Working isItWorking, String notes,
			String latitude, String longitude) {
		super();
		this.fountainName = fountainName;
		this.isItWorking = isItWorking;
		this.notes = notes;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public Main_model(String fountainName, Working isItWorking, String notes,
			String latitude) {
		super();
		this.fountainName = fountainName;
		this.isItWorking = isItWorking;
		this.notes = notes;
		this.latitude = latitude;
		
	}
	
	public Main_model(String fountainName, Working isItWorking, String notes) {
		super();
		this.fountainName = fountainName;
		this.isItWorking = isItWorking;
		this.notes = notes;
	}
	
	public Main_model(String fountainName, Working isItWorking) {
		super();
		this.fountainName = fountainName;
		this.isItWorking = isItWorking;
	}

	public String getFountainName() {
		return fountainName;
	}
	public void setFountainName(String fountainName) {
		this.fountainName = fountainName;
	}
	public Working getIsItWorking() {
		return isItWorking;
	}
	public void setIsItWorking(Working isItWorking) {
		this.isItWorking = isItWorking;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	

}//public class Main_model
