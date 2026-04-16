package model;

import util.*;

public class Player
{
    
    private int PlayerID; 
    private String lastName;
    private String firstName;
    private String middleName;
    private CustomTimestamp birthday;
    private String gender; // FIX 1: Changed type from String to char
    private double height;
    private double weight;
    private boolean rStatus;
    
    //Constructor
    public Player(int PlayerID, String lastName, String firstName, String middleName,
                 // FIX 1: Changed gender parameter type to char
                String birthday, String gender, double height, double weight, boolean rStatus){
    
        this.PlayerID = PlayerID;
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.rStatus = rStatus;

        // Logic for converting the input String ("YYYY-MM-DD") to CustomTimestamp
        String[] dateTokens = birthday.split("-");
        this.birthday = new CustomTimestamp(
            Integer.parseInt(dateTokens[0]), 
            Integer.parseInt(dateTokens[1]), 
            Integer.parseInt(dateTokens[2])
        );
    }

    // FIX 2: Corrected update logic for char type (using '\u0000') and date getter (using getBirthday())
    public void update(Player other){
        if(other.getLastName() != null){this.lastName = other.getLastName();}
        if(other.getFirstName() != null){this.firstName = other.getFirstName();}
        // middleName can be null/empty, so no check needed here
        if(other.getMiddleName() != null){this.middleName= other.getMiddleName();}
        
        // Use the CustomTimestamp getter
        if(other.getBirthday() != null) { this.birthday = other.getBirthday(); } 
        
        // Use null character check for char type
        if(other.getSex() != null ){ this.gender = other.getSex(); } 
        
        this.rStatus = other.getStatus();
    }
    
    public String toHtmlString() {
        String info = "<html>I'm born on " + birthday.getDisplayDate() + " and I am a " + gender +" basketball player<br>#" + height + "cm #" + weight + "kg #";
        if(rStatus)
            info += "CurrentlyActive";
        else
            info += "Inactive";
        info += "</html>";

        return info;
    }

    //Getters
    public int getPlayerID(){return PlayerID;}
    public String getLastName() { return lastName; }
    public String getFirstName() { return firstName; }
    public String getMiddleName() { return middleName; }
    
    /** Returns the birthday as a String in "YYYY-MM-DD" format. Required by GUI. */
    public String getDateOfBirth() {return birthday.toStringDate();} 
    
    /** Returns the CustomTimestamp object. Used in the update method. */
    public CustomTimestamp getBirthday() { 
        return birthday; 
    } 
    
    public String getSex() { return gender; } 
    public double getHeight() { return height; }
    public double getWeight() { return weight; }
    public boolean getStatus() { return rStatus; }
    public String getFullName() {
        StringBuilder fullName = new StringBuilder();
        if (firstName != null && !firstName.isEmpty()) {
            fullName.append(firstName).append(" ");
        }
        if (middleName != null && !middleName.isEmpty()) {
            fullName.append(middleName).append(" ");
        }
        if (lastName != null && !lastName.isEmpty()) {
            fullName.append(lastName);
        }
        return fullName.toString().trim();
    }

    //Setters
    // ADDED: Needed by PlayerController to set the generated ID
    public void setPlayerID(int playerID) { this.PlayerID = playerID; } 
    public void setHeight(int height){this.height = height;}
    public void setWeight(int weight){this.weight = weight;}
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }
    
    /** Sets the birthday from a String date ("YYYY-MM-DD"). Required by GUI update logic. */
    public void setDateOfBirth(String dateOfBirth) { 
        // Logic to convert String date (YYYY-MM-DD) back into CustomTimestamp
        String[] dateTokens = dateOfBirth.split("-");
        this.birthday = new CustomTimestamp(
            Integer.parseInt(dateTokens[0]), 
            Integer.parseInt(dateTokens[1]), 
            Integer.parseInt(dateTokens[2])
        );
    } 
    
    /** Sets the birthday from a CustomTimestamp object. */
    public void setBirthday(CustomTimestamp birthday) { 
        this.birthday = birthday; 
    }
    
    public void setSex(String gender) { this.gender = gender; } 
}