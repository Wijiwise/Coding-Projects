package model;

import view.LoginBuilder;
import util.*;

/*
 *	Make a builder for enthusiast :D
 * */
public class Enthusiast {
	private int ID;
	private String username;
	private String lastName;
	private String firstName;
	private String middleName;
	private String sex;
	private boolean status;
	private CustomTimestamp dateOfBirth;	
	private CustomTimestamp joinDate;
	//temporary until IDs are implemented for other classes

	public Enthusiast() {
		//empty constructor
	};

	/*
	 * Constructor for new enthusiast
	 * When using constructor, take note of below
	 *
	 * NEED TO BE SET:
	 * ID number
	 * join date
	 * */
	public Enthusiast(String username, String lastName, String firstName, String middleName, String sex, CustomTimestamp dateOfBirth) {
		this.username = username;
		this.lastName = lastName;
		this.firstName = firstName;
		this.middleName = middleName;
		this.sex = sex;
		this.dateOfBirth = dateOfBirth;
	}

	public Enthusiast(int ID, String username, String lastName, String firstName, String middleName, String sex, String dateOfBirth, String joinDate, boolean status) {
		this.ID = ID;
		this.username = username;
		this.lastName = lastName;
		this.firstName = firstName;
		this.middleName = middleName;
		this.sex = sex;
		this.status = status;

		String[] dateTokens = dateOfBirth.split("-");
		String[] joinPart = joinDate.split(" ");
		String[] joinTokens = joinPart[0].split("-");
		this.dateOfBirth = new CustomTimestamp(
			Integer.parseInt(dateTokens[0]), 
			Integer.parseInt(dateTokens[1]), 
			Integer.parseInt(dateTokens[2])
		);
		this.joinDate = new CustomTimestamp(
			Integer.parseInt(joinTokens[0]), 
			Integer.parseInt(joinTokens[1]), 
			Integer.parseInt(joinTokens[2])
		);
	}

	public void update(Enthusiast other) {
		if(other.getUsername() != null) { this.username = other.getUsername(); }
		if(other.getLastName() != null) { this.lastName = other.getLastName(); }
		if(other.getFirstName() != null) { this.firstName = other.getFirstName(); }
		if(other.getDateOfBirth() != null) { this.dateOfBirth = other.getDateOfBirth(); }
		if(other.getSex() != null) { this.sex = other.getSex(); }

		this.middleName = other.getMiddleName();
	}

	public String getSimpleInfo() {
		return "[ID " + String.valueOf(ID) + "] " + lastName + ", " + firstName 
			+ " \"" + username + "\"";
	}
	public String getFullName() {
		String name = lastName + ", " + firstName;
		if(middleName != null) name += " " + middleName;
		return name;
	}

	public int getID() { return ID; }
	public String getUsername() { return username; }
	public String getLastName() { return lastName; }
	public String getFirstName() { return firstName; }
	public String getMiddleName() { return middleName; }
	public String getSex() { return sex; }
	public boolean getStatus() { return status; }
	public CustomTimestamp getDateOfBirth() { return dateOfBirth; }
	public CustomTimestamp getJoinDate() { return joinDate; }

	public void setID(int ID) { this.ID = ID; }
	public void setUsername(String username) { this.username = username; }
	public void setLastName(String lastName) { this.lastName = lastName; }
	public void setFirstName(String firstName) { this.firstName = firstName; }
	public void setMiddleName(String middleName) { this.middleName = middleName; }
	public void setSex(String sex) { this.sex = sex; }
	public void setDateOfBirth(CustomTimestamp dateOfBirth) { this.dateOfBirth = dateOfBirth; }
}