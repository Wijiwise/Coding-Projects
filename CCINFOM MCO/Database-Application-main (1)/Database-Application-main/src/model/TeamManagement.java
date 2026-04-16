package model;

import java.util.ArrayList;

import util.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;

public class TeamManagement {
    public TeamManagement(){}
    //CREATE TEAM
    public int addTeam(Team team) {
        String sql = "INSERT INTO team (team_ID, coach_ID, team_name, num_of_player, is_registered, tCreated_at) " 
            + "VALUES (?, ?, ?, ?, ?, ?)";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, team.getTeamID());
            statement.setInt(2, team.getCoachID());
            statement.setString(3, team.getTeamName());
            statement.setInt(4, team.getNumberOfPlayers());
            statement.setBoolean(5, team.getIsRegistered());
            statement.setDate(6, Date.valueOf(team.getTeamCreatedAt()));

            int affectedRows = statement.executeUpdate();
            
            if(affectedRows > 0)
                System.out.println("Team added.");

            try(ResultSet generatedKey = statement.getGeneratedKeys()) {
                if(generatedKey.next()) {
                    int newID = generatedKey.getInt(1);
                    return newID;
                } else {
                    throw new SQLException("Failed Creating Team");
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    //DELETE TEAM
    /**
     * Deletes a Team record from the database using its Team ID.
     * 
     * @param teamID the unique identifier of the Team to delete.
     * 
     * <p><b>Notes:</b></p>
     * <ul>
     *   <li>Deletion is permanent—ensure this method is only called intentionally.</li>
     *   <li>If no record matches the ID, prints "Failed to delete".</li>
     * </ul>
     */
	public void deleteTeam(int teamID) {
		String sql = "DELETE FROM team WHERE team_id = ?";
		try(Connection conn = DatabaseConnection.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql)) {
			statement.setInt(1, teamID);

			int rowsDeleted = statement.executeUpdate();
			if(rowsDeleted > 0) 
				System.out.println("Team deleted");
			else
				System.out.println("Failed to delete");
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
    //SEARCH TEAM
    /**
     * Searches for a Team in the database by their Team ID.
     *
     * @param teamID the unique identifier of the Team to search for.
     * @return a {@link Team} object if found, otherwise {@code null}.
     *
     * <p><b>Notes:</b></p>
     * <ul>
     *   <li>Uses a {@code PreparedStatement} to prevent SQL injection.</li>
     *   <li>The gender column ('sex') is assumed to store a single character (e.g., 'M' or 'F').</li>
     *   <li>All fields must match the database column names exactly.</li>
     * </ul>
     */
public Team searchTeam(int teamID) {
    String sql = "SELECT * FROM team WHERE team_id = ?";
    Team team = null;

    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement statement = conn.prepareStatement(sql)) {

        statement.setInt(1, teamID);
        try (ResultSet rs = statement.executeQuery()) {
            if (rs.next()) {
                team = new Team(
                    rs.getInt("team_id"),
                    rs.getInt("coach_id"),
                    rs.getString("team_name"),
                    rs.getInt("num_of_players"),
                    rs.getBoolean("is_registered"),                            
                    rs.getDate("tCreated_at").toLocalDate()
                );
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return team;
}
    //UPDATE TEAM
        /**
     * Updates an existing Team's information in the database.
     *
     * @param oldTeam the Team object containing the original Team ID (used to locate the record).
     * @param newTeam the Team object containing the updated information.
     * @return the updated Team object retrieved from the database, or {@code null} if not found.
     *
     * <p><b>Notes:</b></p>
     * <ul>
     *   <li>This method uses the Team ID from {@code oldTeam} as the lookup key.</li>
     *   <li>All other fields are updated with the values from {@code newTeam}.</li>
     *   <li>Returns the fresh record after update via {@link #searchTeam(int)}.</li>
     * </ul>
     */
    public Team updateTeam(Team oldTeam, Team newTeam) {
        String sql = "UPDATE Team SET team_ID = ?, coach_ID = ?, team_name = ?, num_of_players = ?, "
            + "is_registered = ?, tCreated_at = ? WHERE team_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, newTeam.getTeamID());
            statement.setInt(2, newTeam.getCoachID());
            statement.setString(3, newTeam.getTeamName());
            statement.setInt(4, newTeam.getNumberOfPlayers());
            statement.setBoolean(5, newTeam.getIsRegistered());
            statement.setDate(6, Date.valueOf(newTeam.getTeamCreatedAt()));


        int rowsUpdated = statement.executeUpdate();
        if (rowsUpdated > 0)
            System.out.println("Team updated successfully.");
        else
            System.out.println("No Team found to update.");
        }catch (SQLException e) {
        e.printStackTrace();
    }

    return searchTeam(oldTeam.getTeamID());
}

    /**
     * Retrieves all players from the database.
     *
     * @return an {@link ArrayList} containing all {@link Player} objects stored in the database.
     *
     * <p><b>Notes:</b></p>
     * <ul>
     *   <li>Each player is instantiated using the {@link Player} constructor.</li>
     *   <li>Make sure the column names in the SQL query match your database schema.</li>
     *   <li>Returns an empty list if no players are found.</li>
     * </ul>
     */
    public ArrayList<Team> getAllTeams() {
        ArrayList<Team> teams = new ArrayList<>();
        String sql = "SELECT * FROM team";

        try (Connection conn = DatabaseConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Team team = new Team(
                    rs.getInt("team_id"),
                    rs.getInt("coach_id"),
                    rs.getString("team_name"),
                    rs.getInt("num_of_players"),
                    rs.getBoolean("is_registered"),                            
                    rs.getDate("tCreated_at").toLocalDate()
            );
                teams.add(team);
            }
        } catch (SQLException e) {
        e.printStackTrace();
    }

        return teams;
    }

    public String getTeamName(int playerID){
        return null;
    }

    public String getCoachName(int playerID){
        return null;
    } 

    public int getTotalGames(int playerID){
        return 0;
    }

    public boolean getStatus(int playerID){
        return false;
    }
}
