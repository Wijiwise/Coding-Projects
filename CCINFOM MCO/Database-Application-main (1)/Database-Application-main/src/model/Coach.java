package model;

import util.*;

public class Coach {
    private int coachID;
    private String firstName;
    private String middleName;
    private String lastName;
    private CustomTimestamp birthday;
    private String gender;
    private int startYear;
    private Integer endYear;
    private boolean inGameStatus;

    public Coach(String lastName, String firstName, String middleName, String gender, CustomTimestamp birthday, int startYear, Integer endYear) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.gender = gender;
        this.birthday = birthday;
        this.startYear = startYear;
        this.endYear = endYear;
        this.inGameStatus = (endYear == null);
    }

    public Coach(int coachID, String lastName, String firstName, String middleName,
                 String birthday, String gender, int startYear, int endYear,
                 boolean inGameStatus) {
        this.coachID = coachID;
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.gender = gender;
        this.startYear = startYear;
        this.endYear = endYear;
        this.inGameStatus = inGameStatus;
		  
		String[] dateTokens = birthday.split("-");
		this.birthday = new CustomTimestamp(
			Integer.parseInt(dateTokens[0]), 
			Integer.parseInt(dateTokens[1]), 
			Integer.parseInt(dateTokens[2])
		);
    }

    public void update(Coach other) {
        if(other.getFirstName() != null) { this.firstName = other.getFirstName(); }
        if(other.getMiddleName() != null) { this.middleName = other.getMiddleName(); }
        if(other.getLastName() != null) { this.lastName = other.getLastName(); }
        if(other.getBirthday() != null) { this.birthday = other.getBirthday(); }
        if(other.getGender() != null) { this.gender = other.getGender(); }
        this.inGameStatus = other.isInGameStatus();
    }
	
	public String toHtmlString() {
		String info = "<html>I’m born on " + birthday.getDisplayDate() + " and I am a " + gender + " coach. #";
		if(inGameStatus) {
			info += "ActiveCoach";
		} else {
			info += "Inactive";
		}
		info += "<br>Coaching Experience: " + startYear + " to ";
		if(endYear == 0) { info += "Current"; }
		else { info += endYear; }
		info += "</html>";

		return info;
	}

    public int getCoachID() { return coachID; }
    public String getFirstName() { return firstName; }
    public String getMiddleName() { return middleName; }
    public String getLastName() { return lastName; }
    public String getFullName() { return firstName + " " + middleName + " " + lastName; }
    public CustomTimestamp getBirthday() { return birthday; }
    public String getGender() { return gender; }
    public int getStartYear() { return startYear; }
    public Integer getEndYear() {
        return endYear;
    }
    public boolean isInGameStatus() { return inGameStatus; }

    public String getInfo() {
        return("[ID " + String.valueOf(coachID) + "] " + lastName + ", " + firstName);
    }

    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setBirthday(CustomTimestamp birthday) { this.birthday = birthday; }
    public void setGender(String gender) { this.gender = gender; }
    public void setStartYear(int year) { this.startYear = year; }
    public void setEndYear(Integer year) {
        this.endYear = year;
    }
    public void setInGameStatus(boolean inGameStatus) {
        this.inGameStatus = inGameStatus;
    }
}