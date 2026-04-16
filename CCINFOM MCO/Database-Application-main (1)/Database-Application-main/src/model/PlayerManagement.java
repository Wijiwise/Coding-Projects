package model;

import java.util.ArrayList;

import util.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
/**
 * Handles all database operations related to Player entities.
 * <p>
 * This includes adding, deleting, updating, searching, and retrieving all players.
 * <br>
 * Each method uses JDBC to interact with the database through {@link util.DatabaseConnection}.
 * </p>
 */
public class PlayerManagement {
    /** 
     * Default constructor for PlayerManagement.
     * <p>Currently unused, but can be used to initialize resources in the future.</p>
     */
    public PlayerManagement(){
    }
    /**
     * Adds a new player record to the database.
     * 
     * @param player the Player object containing player details.
     * @return the auto-generated player ID from the database (if successful), or 0 if insertion failed.
     * 
     * <p><b>Notes:</b></p>
     * <ul>
     *   <li>Uses {@code Statement.RETURN_GENERATED_KEYS} to retrieve the generated primary key.</li>
     *   <li>Ensure {@code player.getDateofBirth()} returns a valid date in {@code yyyy-MM-dd} format, or {@link java.sql.Date#valueOf(String)} will throw an exception.</li>
     *   <li>Ensure the number of placeholders in the SQL query matches the number of values set in the statement.</li>
     * </ul>
     */
    public int addPlayer(Player player) {
		String sql = "INSERT INTO player (player_id, lastname, firstname, middlename, date_of_birth, sex, height, weight, rStatus) " 
			+ "VALUES (?, ?, ?, ?, ?, ?, ? , ?, ?)";

		try(Connection conn = DatabaseConnection.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			statement.setInt(1, player.getPlayerID());
			statement.setString(2, player.getLastName());
			statement.setString(3, player.getFirstName());
			statement.setString(4, player.getMiddleName());
			statement.setDate(5, Date.valueOf(player.getDateOfBirth()));
			statement.setString(6, String.valueOf(player.getSex()));
			statement.setDouble(7, player.getHeight());
			statement.setDouble(8, player.getWeight());
			statement.setBoolean(9, player.getStatus());

			int affectedRows = statement.executeUpdate();
			
			if(affectedRows > 0)
				System.out.println("Player added.");

			try(ResultSet generatedKey = statement.getGeneratedKeys()) {
				if(generatedKey.next()) {
					int newID = generatedKey.getInt(1);
					return newID;
				} else {
					throw new SQLException("Failed Creating Player");
				}
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}

		return 0;
	}
    /**
     * Deletes a player record from the database using its player ID.
     * 
     * @param playerID the unique identifier of the player to delete.
     * 
     * <p><b>Notes:</b></p>
     * <ul>
     *   <li>Deletion is permanent—ensure this method is only called intentionally.</li>
     *   <li>If no record matches the ID, prints "Failed to delete".</li>
     * </ul>
     */
	public void deletePlayer(int playerID) {
		String sql = "DELETE FROM player WHERE player_id = ?";
		try(Connection conn = DatabaseConnection.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql)) {
			statement.setInt(1, playerID);

			int rowsDeleted = statement.executeUpdate();
			if(rowsDeleted > 0) 
				System.out.println("Player deleted");
			else
				System.out.println("Failed to delete");
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
    /**
     * Updates an existing player's information in the database.
     *
     * @param oldPlayer the Player object containing the original player ID (used to locate the record).
     * @param newPlayer the Player object containing the updated information.
     * @return the updated Player object retrieved from the database, or {@code null} if not found.
     *
     * <p><b>Notes:</b></p>
     * <ul>
     *   <li>This method uses the player ID from {@code oldPlayer} as the lookup key.</li>
     *   <li>All other fields are updated with the values from {@code newPlayer}.</li>
     *   <li>Returns the fresh record after update via {@link #searchPlayer(int)}.</li>
     * </ul>
     */
    public Player updatePlayer(Player newPlayer) {
        String sql = "UPDATE player SET lastname = ?, firstname = ?, middlename = ?, date_of_birth = ?, "
            + "sex = ?, height = ?, weight = ?, rStatus = ? WHERE player_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement statement = conn.prepareStatement(sql)) {

        statement.setString(1, newPlayer.getLastName());
        statement.setString(2, newPlayer.getFirstName());
        statement.setString(3, newPlayer.getMiddleName());
        statement.setDate(5, Date.valueOf(newPlayer.getDateOfBirth()));
        statement.setString(5, String.valueOf(newPlayer.getSex()));
        statement.setDouble(6, newPlayer.getHeight());
        statement.setDouble(7, newPlayer.getWeight());
        statement.setBoolean(8, newPlayer.getStatus());
        statement.setInt(9, newPlayer.getPlayerID());

        int rowsUpdated = statement.executeUpdate();
        if (rowsUpdated > 0)
            System.out.println("Player updated successfully.");
        else
            System.out.println("No player found to update.");
        }catch (SQLException e) {
        e.printStackTrace();
    }

    return searchPlayer(newPlayer.getPlayerID());
}
	/**
     * Searches for a player in the database by their player ID.
     *
     * @param playerID the unique identifier of the player to search for.
     * @return a {@link Player} object if found, otherwise {@code null}.
     *
     * <p><b>Notes:</b></p>
     * <ul>
     *   <li>Uses a {@code PreparedStatement} to prevent SQL injection.</li>
     *   <li>The gender column ('sex') is assumed to store a single character (e.g., 'M' or 'F').</li>
     *   <li>All fields must match the database column names exactly.</li>
     * </ul>
     */
public Player searchPlayer(int playerID) {
    String sql = "SELECT * FROM player WHERE player_id = ?";
    Player player = null;

    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement statement = conn.prepareStatement(sql)) {

        statement.setInt(1, playerID);
        try (ResultSet rs = statement.executeQuery()) {
            if (rs.next()) {
                player = new Player(
                    rs.getInt("player_id"),
                    rs.getString("lastname"),
                    rs.getString("firstname"),
                    rs.getString("middlename"),
                    rs.getString("date_of_birth"),                
                    rs.getString("sex"),              
                    rs.getDouble("height"),
                    rs.getDouble("weight"),
                    rs.getBoolean("rStatus")
                );
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return player;
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
public ArrayList<Player> getAllPlayers() {
    ArrayList<Player> players = new ArrayList<>();
    String sql = "SELECT * FROM player";

    try (Connection conn = DatabaseConnection.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {

        while (rs.next()) {
            Player player = new Player(
                rs.getInt("player_id"),
                rs.getString("lastname"),
                rs.getString("firstname"),
                rs.getString("middlename"),
                rs.getString("date_of_birth"),
                rs.getString("sex"),
                rs.getDouble("height"),
                rs.getDouble("weight"),
                rs.getBoolean("rStatus")
            );
            players.add(player);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return players;
}

    public int generatePlayerID() {
        String sql = "SELECT MAX(player_id) AS max_id FROM player";
        int newID = 1;

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                newID = rs.getInt("max_id") + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return newID;
    }
    /* Delete Player from DB
	 *
	 * @param playerID The ID of the player to be deleted from DB
	 * */
	public void togglePlayer(int playerID) {
		String sql = "UPDATE player SET status = CASE WHEN status = true THEN false ELSE true END WHERE player_id = ?";

		try(Connection conn = DatabaseConnection.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql)) {
			statement.setInt(1, playerID);
			statement.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}

}
