package controller;

import model.*;
import view.*;

import java.util.ArrayList;

import javax.swing.JOptionPane;

public class PlayerController {

    private GUIView prevView;          // Main menu screen
    private GUIPlayerView playerView;  // Player screen
    private PlayerManagement model;
    private RegistrationManagement registrationModel;
    private TeamManagement teamModel;

    public PlayerController(GUIView prevView, GUIPlayerView view) {
        this.prevView = prevView;
        this.playerView = view;

        this.model = new PlayerManagement();
        this.playerView.setListeners(this);   // Connect GUI to controller
        this.registrationModel = new RegistrationManagement();
        this.teamModel = new TeamManagement();
    }

    /* ===========================================================
        NAVIGATION / VIEW CONTROL
       =========================================================== */
    public void showPlayerProfile(Player p) {
        playerView.profilePanel(p);
        playerView.show();
    }

    public void handleBackToMenu() {
        playerView.hide();
        prevView.menuPanel();
        prevView.show();
    }

    public void handleLogout() {
        playerView.hide();
        prevView.menuPanel();
        prevView.show();
    }

    /**
     * Handle player menu actions
     */
    
    public void handleMenu(String option, Player player) {
        switch(option) {
            case "profile":
                playerView.profilePanel(player);
                break;
            case "teams":
                playerView.myTeamsPanel(player, getPlayerTeams(player.getPlayerID()));
                break;
            case "requests":
                playerView.myRequestsPanel(player, getPlayerRequests(player.getPlayerID()));
                break;
            case "browse":
                playerView.browseTeamsPanel(player, getAllAvailableTeams());
                break;
            case "logout":
                handleLogout();
                break;
        }
    }

    /* ===========================================================
        CRUD OPERATIONS – rewritten for GUI use
       =========================================================== */

    // Called when GUI saves player updates
    public void handleUpdate(Player updatedPlayer) {
        try {
            Player result = model.updatePlayer(updatedPlayer);
            if (result != null) {
                JOptionPane.showMessageDialog(null, "Player updated successfully!");
                playerView.profilePanel(result);     // Refresh
            } else {
                JOptionPane.showMessageDialog(null, "Database update failed.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error updating: " + e.getMessage());
        }
    }

    // Called by deletePanel()
    public void handleDelete(Player p) {
    try {
        model.deletePlayer(p.getPlayerID());   // void
        playerView.displayMessage("Player deleted successfully!");
    } catch (Exception e) {
        playerView.displayError("Error deleting player: " + e.getMessage());
    }
}

    /* ===========================================================
        SEARCH → show profile
       =========================================================== */
    public void handleSearchPlayer(int playerID) {
        try {
            Player p = model.searchPlayer(playerID);
            if (p != null) {
                showPlayerProfile(p);
            } else {
                JOptionPane.showMessageDialog(null, "Player not found.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Search error: " + e.getMessage());
        }
    }

    /* ===========================================================
        CREATE PLAYER – GUI version (uses a dialog)
       =========================================================== */
    public void handleCreatePlayer(Player newPlayer) {
        try {
            int newID = model.addPlayer(newPlayer);

            if (newID > 0) {
                newPlayer.setPlayerID(newID);
                JOptionPane.showMessageDialog(null, "Player created! ID = " + newID);

                showPlayerProfile(newPlayer);
            } else {
                JOptionPane.showMessageDialog(null, "Create failed.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    /**
     * Player requests to join a team
     */
    
    public void requestJoinTeam(Player player, int teamId) {
        RegistrationManagement.RegistrationResult result = 
            registrationModel.registerPlayerToTeam(player.getPlayerID(), teamId);
        
        if (result.isSuccess()) {
            playerView.displayMessage("Success: " + result.message);
            handleMenu("requests", player);
        } else {
            playerView.displayError("Registration Failed: " + result.message);
        }
    }
    
    
    // * Get all teams the player is currently on
    
    public ArrayList<Team> getPlayerTeams(int playerId) {
        return registrationModel.getPlayerTeams(playerId);
    }
    
    
    // * Get all registration requests for a player
    
    public ArrayList<RegistrationRequest> getPlayerRequests(int playerId) {
        return registrationModel.getRequestsForPlayer(playerId);
    }
    
    /**
     * Get all available teams for browsing
     */
    public ArrayList<Team> getAllAvailableTeams() {
        return teamModel.getAllTeams();
    }

    /**
     * View team details before requesting to join
     */
    public void viewTeamDetails(int teamId) {
        Team team = teamModel.searchTeam(teamId);
        TeamRequirements requirements = registrationModel.getTeamRequirements(teamId);
        ArrayList<Player> teamPlayers = registrationModel.getTeamPlayers(teamId);
        
        playerView.viewTeamDetailsPanel(team, requirements, teamPlayers);
    }

}
