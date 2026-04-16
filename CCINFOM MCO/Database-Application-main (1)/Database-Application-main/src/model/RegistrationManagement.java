package model;

import util.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;

/**
 * Management class for Player-Team Registration
 * All validation logic is in Java (no stored procedures)
 */
public class RegistrationManagement {
    
    private PlayerManagement playerMgmt;
    private TeamManagement teamMgmt;
    private CoachManagement coachMgmt;
    
    public RegistrationManagement() {
        this.playerMgmt = new PlayerManagement();
        this.teamMgmt = new TeamManagement();
        this.coachMgmt = new CoachManagement();
    }
    
    /**
     * Result class to hold operation output
     */
    public static class RegistrationResult {
        public int code;
        public String message;
        
        public RegistrationResult(int code, String message) {
            this.code = code;
            this.message = message;
        }
        
        public boolean isSuccess() {
            return code == 0;
        }
        
        @Override
        public String toString() {
            return "Code: " + code + ", Message: " + message;
        }
    }
    
    /**
     * Register a player to a team (creates a pending request)
     * ALL VALIDATIONS ARE DONE IN JAVA
     */
    public RegistrationResult registerPlayerToTeam(int playerId, int teamId) {
                Connection conn = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction
            
            // Step 1: Verify Player exists and is active
            Player player = playerMgmt.searchPlayer(playerId);
            if (player == null || !player.getStatus()) {
                conn.rollback();
                return new RegistrationResult(1, "Player does not exist or is inactive");
            }
            
            // Step 2: Verify Team exists
            Team team = teamMgmt.searchTeam(teamId);
            if (team == null) {
                conn.rollback();
                return new RegistrationResult(2, "Team does not exist");
            }
            
            // Check team registry status
            if (!team.getIsRegistered()) {
                conn.rollback();
                return new RegistrationResult(3, "Team registration is closed");
            }
            
            // Step 3: Verify Coach exists and is active
            if (team.getCoachID() == 0) {
                conn.rollback();
                return new RegistrationResult(5, "Team does not have an assigned coach");
            }
            
            Coach coach = coachMgmt.searchCoach(team.getCoachID());
            if (coach == null || !coach.isInGameStatus()) {
                conn.rollback();
                return new RegistrationResult(4, "Coach does not exist or is inactive");
            }
            
            // Step 4: Check if player is already on this team
            if (isPlayerOnTeam(playerId, teamId, conn)) {
                conn.rollback();
                return new RegistrationResult(6, "Player is already on this team");
            }
            
            // Step 5: Check if player already has a pending request
            if (hasPendingRequest(playerId, teamId, conn)) {
                conn.rollback();
                return new RegistrationResult(7, "Player already has a pending registration request for this team");
            }
            
            // Step 6: Get team requirements (use defaults if not set)
            TeamRequirements requirements = getTeamRequirements(teamId);
            if (requirements == null) {
                requirements = new TeamRequirements(teamId, 15, 0, 100, 0.0, 300.0, true);
            }
            
            // Step 7: Verify team is not full
            if (team.getNumberOfPlayers() >= requirements.getMaxPlayers()) {
                conn.rollback();
                return new RegistrationResult(8, 
                    "Team is full. Maximum players: " + requirements.getMaxPlayers());
            }
            
            // Step 8: Verify registration is open
            if (!requirements.isRegistrationOpen()) {
                conn.rollback();
                return new RegistrationResult(9, "Team registration is currently closed");
            }
            
            // Step 9: Verify age requirements
            int playerAge = calculateAge(player.getDateOfBirth());
            if (playerAge < requirements.getMinAge() || playerAge > requirements.getMaxAge()) {
                conn.rollback();
                return new RegistrationResult(10, 
                    String.format("Player age (%d) does not meet requirements (%d-%d years)", 
                    playerAge, requirements.getMinAge(), requirements.getMaxAge()));
            }
            
            // Step 10: Verify height requirements
            if (player.getHeight() < requirements.getMinHeight() || 
                player.getHeight() > requirements.getMaxHeight()) {
                conn.rollback();
                return new RegistrationResult(11, 
                    String.format("Player height (%.1fcm) does not meet requirements (%.1f-%.1fcm)", 
                    player.getHeight(), requirements.getMinHeight(), requirements.getMaxHeight()));
            }
            
            // Step 11: Check if team is in tournament
            if (isTeamInTournament(teamId, conn)) {
                conn.rollback();
                return new RegistrationResult(12, 
                    "Team is currently participating in a tournament. Registration not allowed.");
            }
            
            // Step 12: Create registration request
            String sql = "INSERT INTO registration_request (player_id, team_id, status) VALUES (?, ?, 'pending')";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, playerId);
                stmt.setInt(2, teamId);
                stmt.executeUpdate();
            }
            
            conn.commit();
            return new RegistrationResult(0, "Registration request submitted successfully. Awaiting coach approval.");
            
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return new RegistrationResult(-1, "Database error: " + e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Coach approves a registration request
     */
    public RegistrationResult approveRegistration(int requestId, int coachId) {
                Connection conn = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            // Get request details
            RegistrationRequest request = getRequestById(requestId);
            if (request == null) {
                conn.rollback();
                return new RegistrationResult(-1, "Registration request not found");
            }
            
            // Get team
            Team team = teamMgmt.searchTeam(request.getTeamId());
            if (team == null) {
                conn.rollback();
                return new RegistrationResult(-1, "Team not found");
            }
            
            // Verify coach is authorized
            if (team.getCoachID() != coachId) {
                conn.rollback();
                return new RegistrationResult(1, "Unauthorized: You are not the coach of this team");
            }
            
            // Verify request is pending
            if (!"pending".equals(request.getStatus())) {
                conn.rollback();
                return new RegistrationResult(2, "Request is not pending. Current status: " + request.getStatus());
            }
            
            // Verify team still has space
            TeamRequirements requirements = getTeamRequirements(request.getTeamId());
            int maxPlayers = requirements != null ? requirements.getMaxPlayers() : 15;
            
            if (team.getNumberOfPlayers() >= maxPlayers) {
                conn.rollback();
                return new RegistrationResult(3, "Team is now full");
            }
            
            // Check if team is in tournament
            if (isTeamInTournament(request.getTeamId(), conn)) {
                conn.rollback();
                return new RegistrationResult(4, "Cannot approve registration while team is in tournament");
            }
            
            // Add player to team
            String sql1 = "INSERT INTO player_team (player_id, team_id, status) VALUES (?, ?, TRUE)";
            try (PreparedStatement stmt = conn.prepareStatement(sql1)) {
                stmt.setInt(1, request.getPlayerId());
                stmt.setInt(2, request.getTeamId());
                stmt.executeUpdate();
            }
            
            // Update team player count
            String sql2 = "UPDATE team SET number_of_players = number_of_players + 1 WHERE team_ID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql2)) {
                stmt.setInt(1, request.getTeamId());
                stmt.executeUpdate();
            }
            
            // Update player registry status
            String sql3 = "UPDATE player SET rStatus = TRUE WHERE player_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql3)) {
                stmt.setInt(1, request.getPlayerId());
                stmt.executeUpdate();
            }
            
            // Update registration request status
            String sql4 = "UPDATE registration_request SET status = 'approved', reviewed_date = CURRENT_TIMESTAMP WHERE request_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql4)) {
                stmt.setInt(1, requestId);
                stmt.executeUpdate();
            }
            
            conn.commit();
            return new RegistrationResult(0, "Player successfully added to team");
            
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return new RegistrationResult(-1, "Database error: " + e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Coach rejects a registration request
     */
    public RegistrationResult rejectRegistration(int requestId, int coachId, String reason) {
        Connection conn = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            RegistrationRequest request = getRequestById(requestId);
            if (request == null) {
                conn.rollback();
                return new RegistrationResult(-1, "Registration request not found");
            }
            
            Team team = teamMgmt.searchTeam(request.getTeamId());
            if (team.getCoachID() != coachId) {
                conn.rollback();
                return new RegistrationResult(1, "Unauthorized");
            }
            
            if (!"pending".equals(request.getStatus())) {
                conn.rollback();
                return new RegistrationResult(2, "Request is not pending");
            }
            
            String sql = "UPDATE registration_request SET status = 'rejected', reviewed_date = CURRENT_TIMESTAMP, rejection_reason = ? WHERE request_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, reason);
                stmt.setInt(2, requestId);
                stmt.executeUpdate();
            }
            
            conn.commit();
            return new RegistrationResult(0, "Registration request rejected");
            
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return new RegistrationResult(-1, "Database error: " + e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Remove a player from a team
     */
    public RegistrationResult removePlayerFromTeam(int playerId, int teamId, int coachId) {
             Connection conn = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            Team team = teamMgmt.searchTeam(teamId);
            if (team.getCoachID() != coachId) {
                conn.rollback();
                return new RegistrationResult(1, "Unauthorized: You are not the coach of this team");
            }
            
            if (!isPlayerOnTeam(playerId, teamId, conn)) {
                conn.rollback();
                return new RegistrationResult(2, "Player is not on this team");
            }
            
            if (isTeamInTournament(teamId, conn)) {
                conn.rollback();
                return new RegistrationResult(3, "Cannot remove player while team is in tournament");
            }
            
            // Remove player from team
            String sql1 = "UPDATE player_team SET status = FALSE WHERE player_id = ? AND team_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql1)) {
                stmt.setInt(1, playerId);
                stmt.setInt(2, teamId);
                stmt.executeUpdate();
            }
            
            // Update team count
            String sql2 = "UPDATE team SET number_of_players = number_of_players - 1 WHERE team_ID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql2)) {
                stmt.setInt(1, teamId);
                stmt.executeUpdate();
            }
            
            conn.commit();
            return new RegistrationResult(0, "Player removed from team successfully");
            
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return new RegistrationResult(-1, "Database error: " + e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    // ========== Helper Methods ==========
    
    private boolean isPlayerOnTeam(int playerId, int teamId, Connection conn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM player_team WHERE player_id = ? AND team_id = ? AND status = TRUE";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, playerId);
            stmt.setInt(2, teamId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;

    }
    
    private boolean hasPendingRequest(int playerId, int teamId, Connection conn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM registration_request WHERE player_id = ? AND team_id = ? AND status = 'pending'";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, playerId);
            stmt.setInt(2, teamId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }
    
    private boolean isTeamInTournament(int teamId, Connection conn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM bracket WHERE competing_team_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, teamId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }
    
    private int calculateAge (String dateOfBirth) {
        LocalDate birthDate = LocalDate.parse(dateOfBirth);
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
    
    // ========== CRUD Methods ==========
    
    public ArrayList<RegistrationRequest> getPendingRequestsForCoach(int coachId) {
        ArrayList<RegistrationRequest> requests = new ArrayList<>();
        String sql = "SELECT rr.* FROM registration_request rr " +
                    "INNER JOIN team t ON rr.team_id = t.team_ID " +
                    "WHERE t.coach_id = ? AND rr.status = 'pending' " +
                    "ORDER BY rr.request_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, coachId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                requests.add(new RegistrationRequest(
                    rs.getInt("request_id"),
                    rs.getInt("player_id"),
                    rs.getInt("team_id"),
                    rs.getTimestamp("request_date"),
                    rs.getString("status"),
                    rs.getTimestamp("reviewed_date"),
                    rs.getString("rejection_reason")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }
    
    public ArrayList<RegistrationRequest> getRequestsForPlayer(int playerId) {
        ArrayList<RegistrationRequest> requests = new ArrayList<>();
        String sql = "SELECT * FROM registration_request WHERE player_id = ? ORDER BY request_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, playerId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                requests.add(new RegistrationRequest(
                    rs.getInt("request_id"),
                    rs.getInt("player_id"),
                    rs.getInt("team_id"),
                    rs.getTimestamp("request_date"),
                    rs.getString("status"),
                    rs.getTimestamp("reviewed_date"),
                    rs.getString("rejection_reason")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }
    
    public ArrayList<Player> getTeamPlayers(int teamId) {
            ArrayList<Player> players = new ArrayList<>();
        String sql = "SELECT p.* FROM player p " +
                    "INNER JOIN player_team pt ON p.player_id = pt.player_id " +
                    "WHERE pt.team_id = ? AND pt.status = TRUE";
        
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, teamId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                players.add(new Player(
                    rs.getInt("player_id"),
                    rs.getString("lastname"),
                    rs.getString("firstname"),
                    rs.getString("middlename"),
                    rs.getString("date_of_birth"),
                    rs.getString("sex"),
                    rs.getDouble("height"),
                    rs.getDouble("weight"),
                    rs.getBoolean("rStatus")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return players;
    }
    
    public ArrayList<Team> getPlayerTeams(int playerId) {
            ArrayList<Team> teams = new ArrayList<>();
        String sql = "SELECT t.* FROM team t " +
                    "INNER JOIN player_team pt ON t.team_ID = pt.team_id " +
                    "WHERE pt.player_id = ? AND pt.status = TRUE";
        
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, playerId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                teams.add(new Team(
                    rs.getInt("team_ID"),
                    rs.getInt("coach_id"),
                    rs.getString("team_name"),
                    rs.getInt("number_of_players"),
                    rs.getBoolean("registry_status"),
                    rs.getDate("tCreated_at").toLocalDate()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return teams;
    }
    
    public void setTeamRequirements(TeamRequirements requirements) {
                String sql = "INSERT INTO team_requirements " +
                    "(team_id, max_players, min_age, max_age, min_height, max_height, registration_open) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE " +
                    "max_players = VALUES(max_players), min_age = VALUES(min_age), max_age = VALUES(max_age), " +
                    "min_height = VALUES(min_height), max_height = VALUES(max_height), registration_open = VALUES(registration_open)";
        
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, requirements.getTeamId());
            stmt.setInt(2, requirements.getMaxPlayers());
            stmt.setInt(3, requirements.getMinAge());
            stmt.setInt(4, requirements.getMaxAge());
            stmt.setDouble(5, requirements.getMinHeight());
            stmt.setDouble(6, requirements.getMaxHeight());
            stmt.setBoolean(7, requirements.isRegistrationOpen());
            
            stmt.executeUpdate();
            System.out.println("Team requirements updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public TeamRequirements getTeamRequirements(int teamId) {
           String sql = "SELECT * FROM team_requirements WHERE team_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, teamId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new TeamRequirements(
                    rs.getInt("team_id"),
                    rs.getInt("max_players"),
                    rs.getInt("min_age"),
                    rs.getInt("max_age"),
                    rs.getDouble("min_height"),
                    rs.getDouble("max_height"),
                    rs.getBoolean("registration_open")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public RegistrationRequest getRequestById(int requestId) {
        
        String sql = "SELECT * FROM registration_request WHERE request_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, requestId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new RegistrationRequest(
                    rs.getInt("request_id"),
                    rs.getInt("player_id"),
                    rs.getInt("team_id"),
                    rs.getTimestamp("request_date"),
                    rs.getString("status"),
                    rs.getTimestamp("reviewed_date"),
                    rs.getString("rejection_reason")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
