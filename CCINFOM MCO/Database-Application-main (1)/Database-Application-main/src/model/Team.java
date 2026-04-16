package model;
import java.time.*;
import java.util.*;
public class Team {
    private int teamID;
    private int coachID;
    private ArrayList<Integer> playerIDs;
    private String teamName;
    private int numberOfPlayers;
    private boolean isRegistered;
    private LocalDate teamCreatedAt;
    
    public Team(int teamID, int coachID, String teamName, int numberOfPlayers, boolean isRegistered, LocalDate teamCreatedAt){
        this.teamID = teamID;
        this.coachID = coachID;
        this.teamName = teamName;
        this.numberOfPlayers = numberOfPlayers;
        this.isRegistered = isRegistered;
        this.playerIDs = new ArrayList<Integer>();
        this.teamCreatedAt = teamCreatedAt;
    }

    //GETTERS
    public int getTeamID(){ return teamID; }
    public int getCoachID(){ return coachID; }
    public ArrayList<Integer> getPlayerIDs(){ return playerIDs; }
    public String getTeamName(){ return teamName; }
    public int getNumberOfPlayers(){ return numberOfPlayers; }
    public boolean getIsRegistered(){ return isRegistered; }
    public LocalDate getTeamCreatedAt(){ return teamCreatedAt; }
    //SETTERS
    public void setCoachID(int coachID){ this.coachID = coachID; }
    public void setTeamName(String teamName){ this.teamName = teamName; }
    public void setNumberOfPlayers(int numberOfPlayers){ this.numberOfPlayers = numberOfPlayers; }
    public void setIsRegistered(boolean isRegistered){ this.isRegistered = isRegistered; }
    public void setTeamCreatedAt(LocalDate teamCreatedAt){ this.teamCreatedAt = teamCreatedAt;}
}
