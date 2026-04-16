package model;

import util.*;
import java.util.*;

public class GameBracket {

    /* draft */

	/* FIX BUGS PLS
    private int bracketID;
    private int tournamentID;
    private ArrayList<Integer> competingTeamIDs;
    private ArrayList<Integer> gameIDs;
    private int gamesRemainingCount;
    private CustomTimestamp createdAt;

    // empty constructor
    public GameBracket () {}

    // constructor with id
    public GameBracket (int bracketID, int tournamentID,
                        int gamesRemainingCount, CustomTimestamp createdAt) {
        this.bracketID = bracketID;
        this.tournamentID = tournamentID;
        this.competingTeamIDs = new ArrayList<Integer>();
        this.gameIDs = new ArrayList<Integer>();
        this.gamesRemainingCount = gamesRemainingCount;

        String[] joinPart = createdAt.split(" ");
        String[] joinTokens = joinPart[0].split("-");
        this.createdAt = new CustomTimestamp(
                Integer.parseInt(joinTokens[0]),
                Integer.parseInt(joinTokens[1]),
                Integer.parseInt(joinTokens[2])
        );
    }

    // getters
    public int getBracketID() { return bracketID; }
    public int getTournamentID() { return tournamentID; }
    public ArrayList<Integer> getCompetingTeamIDs() { return competingTeamIDs; }
    public ArrayList<Integer> getGameIDs() {return gameIDs;}
    public int getGamesRemainingCount() {return gamesRemainingCount;}
    public CustomTimestamp getCreatedAt() {return createdAt;}

    // setters
    public void setBracketID(int bracketID) {this.bracketID = bracketID;}
    public void setTournamentID(int tournamentID) {this.tournamentID = tournamentID;}
    public void setCompetingTeamIDs(ArrayList<Integer> competingTeamIDs) {this.competingTeamIDs = competingTeamIDs;}
    public void setGameIDs(ArrayList<Integer> gameIDs) {this.gameIDs = gameIDs;}
    public void setGamesRemainingCount(int gamesRemainingCount) {this.gamesRemainingCount = gamesRemainingCount;}
    public void setCreatedAt(CustomTimestamp createdAt) {this.createdAt = createdAt;}
	 */
}
