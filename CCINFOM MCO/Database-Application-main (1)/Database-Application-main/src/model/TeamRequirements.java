package model;

/**
 * Model class for Team Requirements
 */

public class TeamRequirements {
    private int teamId;
    private int maxPlayers;
    private int minAge;
    private int maxAge;
    private double minHeight;
    private double maxHeight;
    private boolean registrationOpen;
    
    public TeamRequirements(int teamId, int maxPlayers, int minAge, int maxAge,
                        double minHeight, double maxHeight, boolean registrationOpen) {
        this.teamId = teamId;
        this.maxPlayers = maxPlayers;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
        this.registrationOpen = registrationOpen;
    }
    
    public int getTeamId() { return teamId; }
    public int getMaxPlayers() { return maxPlayers; }
    public int getMinAge() { return minAge; }
    public int getMaxAge() { return maxAge; }
    public double getMinHeight() { return minHeight; }
    public double getMaxHeight() { return maxHeight; }
    public boolean isRegistrationOpen() { return registrationOpen; }
    
    public void setMaxPlayers(int maxPlayers) { this.maxPlayers = maxPlayers; }
    public void setMinAge(int minAge) { this.minAge = minAge; }
    public void setMaxAge(int maxAge) { this.maxAge = maxAge; }
    public void setMinHeight(double minHeight) { this.minHeight = minHeight; }
    public void setMaxHeight(double maxHeight) { this.maxHeight = maxHeight; }
    public void setRegistrationOpen(boolean registrationOpen) { this.registrationOpen = registrationOpen; }
    
}
