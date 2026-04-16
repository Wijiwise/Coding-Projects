package model;

import util.*;
import java.util.*;

public class Game {

    private int gameID;
    private int tournamentID;
    private int competingTeamA_ID;
    private int competingTeamB_ID;
    private int winningTeamID;
    private int losingTeamID;
    private String scoreRatio;
    private String gameStatus;
    private CustomTimestamp startDate;
    private CustomTimestamp endDate;

    // constructor
    public Game (int gameID, int tournamentID, int competingTeamA_ID, int competingTeamB_ID, int winningTeamID, int losingTeamID,
                    String scoreRatio, String gameStatus, String startDate, String endDate) {
        this.gameID = gameID;
        this.tournamentID = tournamentID;
        this.competingTeamA_ID = competingTeamA_ID;
        this.competingTeamB_ID =competingTeamB_ID;
        this.winningTeamID = winningTeamID;
        this.losingTeamID = losingTeamID;
        this.scoreRatio = scoreRatio;
        this.gameStatus = gameStatus;

        String[] startToken = startDate.split("-");
        String[] endToken = endDate.split("-");
        this.startDate = new CustomTimestamp(
                Integer.parseInt(startToken[0]),
                Integer.parseInt(startToken[1]),
                Integer.parseInt(startToken[2])
        );
        this.endDate = new CustomTimestamp(
                Integer.parseInt(endToken[0]),
                Integer.parseInt(endToken[1]),
                Integer.parseInt(endToken[2])
        );
    }


    //Getters
    public int getGameID() { return gameID; }
    public int getTournamentID() { return tournamentID; }
    public int getCompetingTeamA_ID() {return competingTeamA_ID;}
    public int getCompetingTeamB_ID() {return competingTeamB_ID;}
    public int getWinningTeamID() { return winningTeamID; }
    public int getLosingTeamID() { return losingTeamID; }
    public String getScoreRatio() { return scoreRatio; }
    public String getGameStatus() { return gameStatus; }
    public CustomTimestamp getStartDate() { return startDate; }
    public CustomTimestamp getEndDate() { return endDate; }

    //Setters
    public void setGameID(int gameID) { this.gameID = gameID; }
    public void setTournamentID(int tournamentID) { this.tournamentID = tournamentID; }
    public void setCompetingTeamA_ID(int competingTeamA_ID) {this.competingTeamA_ID = competingTeamA_ID;}
    public void setCompetingTeamB_ID(int competingTeamB_ID) {this.competingTeamB_ID = competingTeamB_ID;}
    public void setWinningTeamID(int winningTeamID) { this.winningTeamID = winningTeamID; }
    public void setLosingTeamID(int losingTeamID) { this.losingTeamID = losingTeamID; }
    public void setScoreRatio(String scoreRatio) { this.scoreRatio = scoreRatio; }
    public void setGameStatus(String gameStatus) { this.gameStatus = gameStatus; }
    public void setStartDate(CustomTimestamp startDate) { this.startDate = startDate; }
    public void setEndDate(CustomTimestamp endDate) { this.endDate = endDate; }

}