package model;

import util.*;

public class Tournament {
    private int tournamentID;
    private String tournamentName;
    private int seasonYear;
    private String tournamentType;
    private CustomTimestamp startDate;
    private CustomTimestamp endDate;

    // Empty constructor
    public Tournament() {}

    // Constructor for new tournament
    public Tournament(String tournamentName, int seasonYear, String tournamentType,
                      CustomTimestamp startDate, CustomTimestamp endDate) {
        this.tournamentName = tournamentName;
        this.seasonYear = seasonYear;
        this.tournamentType = tournamentType;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Constructor for existing tournament
    public Tournament(int tournamentID, String tournamentName, int seasonYear,
                      String tournamentType, String startDate, String endDate) {
        this.tournamentID = tournamentID;
        this.tournamentName = tournamentName;
        this.seasonYear = seasonYear;
        this.tournamentType = tournamentType;

        // Parse date strings (YYYY-MM-DD format from database)
        if (startDate != null && !startDate.isEmpty()) {
            String[] startTokens = startDate.split("-");
            this.startDate = new CustomTimestamp(
                    Integer.parseInt(startTokens[0]),
                    Integer.parseInt(startTokens[1]),
                    Integer.parseInt(startTokens[2])
            );
        }

        if (endDate != null && !endDate.isEmpty()) {
            String[] endTokens = endDate.split("-");
            this.endDate = new CustomTimestamp(
                    Integer.parseInt(endTokens[0]),
                    Integer.parseInt(endTokens[1]),
                    Integer.parseInt(endTokens[2])
            );
        }
    }

    // Update method
    public void update(Tournament other) {
        if (other.getTournamentName() != null) { this.tournamentName = other.getTournamentName(); }
        if (other.getTournamentType() != null) { this.tournamentType = other.getTournamentType(); }
        if (other.getStartDate() != null) { this.startDate = other.getStartDate(); }
        if (other.getEndDate() != null) { this.endDate = other.getEndDate(); }
        if (other.getSeasonYear() > 0) { this.seasonYear = other.getSeasonYear(); }
    }
	
	public String toHtmlString() {
		String info = "<html>#Season" + seasonYear + " #" + tournamentType +"<br>Tournament Status: " + startDate.toStringDate() + "-" + endDate.toStringDate();  

		return info;
	}

    // Getters
    public int getTournamentID() { return tournamentID; }
    public String getTournamentName() { return tournamentName; }
    public int getSeasonYear() { return seasonYear; }
    public String getTournamentType() { return tournamentType; }
    public CustomTimestamp getStartDate() { return startDate; }
    public CustomTimestamp getEndDate() { return endDate; }

    // Setters
    public void setTournamentID(int tournamentID) { this.tournamentID = tournamentID; }
    public void setTournamentName(String tournamentName) { this.tournamentName = tournamentName; }
    public void setSeasonYear(int seasonYear) { this.seasonYear = seasonYear; }
    public void setTournamentType(String tournamentType) { this.tournamentType = tournamentType; }
    public void setStartDate(CustomTimestamp startDate) { this.startDate = startDate; }
    public void setEndDate(CustomTimestamp endDate) { this.endDate = endDate; }
}