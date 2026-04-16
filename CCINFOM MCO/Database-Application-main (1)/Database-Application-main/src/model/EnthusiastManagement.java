package model;

import util.*;
import view.LoginBuilder; 
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;

public class EnthusiastManagement {
	public EnthusiastManagement() {}

	/* Add Enthusiast into DB
	 *
	 * @param enthusiast The built enthusiast to add
	 * @return An int to get the auto incremented ID 
	 * */
	public int addEnthusiast(Enthusiast enthusiast) {
		String sql = "INSERT INTO enthusiast (username, lastname, firstname, middlename, sex, date_of_birth, status) " 
			+ "VALUES (?, ?, ?, ?, ?, ?, ?)";

		try(Connection conn = DatabaseConnection.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			//plug the required ? values 
			statement.setString(1, enthusiast.getUsername());
			statement.setString(2, enthusiast.getLastName());
			statement.setString(3, enthusiast.getFirstName());
			statement.setString(4, enthusiast.getMiddleName());
			statement.setString(5, enthusiast.getSex());
			statement.setDate(6, Date.valueOf(enthusiast
						.getDateOfBirth()
						.toStringDate()));
			statement.setBoolean(7, true);

			int affectedRows = statement.executeUpdate();
			
			if(affectedRows > 0)
				System.out.println("Enthusiast added.");

			//for finding the new id to return
			try(ResultSet generatedKey = statement.getGeneratedKeys()) {
				if(generatedKey.next()) {
					int newID = generatedKey.getInt(1);
					return newID;
				} else {
					throw new SQLException("Failed Creating Enthusiast");
				}
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}

		return 0;
	}

	/* Delete Enthusiast from DB
	 *
	 * @param enthusiastID The ID of the enthusiast to be deleted from DB
	 * */
	public void toggleEnthusiast(int enthusiastID) {
		String sql = "UPDATE enthusiast SET status = CASE WHEN status = true THEN false ELSE true END WHERE enthusiast_id = ?";

		try(Connection conn = DatabaseConnection.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql)) {
			statement.setInt(1, enthusiastID);
			statement.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}

	/* Update Enthusiast in DB
	 *
	 * @param oldEnthusiast The reference to the original Enthusiast
	 * @param updatedEnthusiast The reference to the updated Enthusiast
	 * @return The newly updated enthusiast 
	 * */
	public Enthusiast updateEnthusiast(Enthusiast enthusiastReference) {
		String sql = "UPDATE enthusiast SET " +
			"username = ?, " +
			"lastname = ?, " + 
			"firstname = ?, " + 
			"middlename = ?, " + 
			"sex = ?, " + 
			"date_of_birth = ? " +
			"WHERE enthusiast_id = ?";

		try(Connection conn = DatabaseConnection.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql)) {
			statement.setString(1, enthusiastReference.getUsername());
			statement.setString(2, enthusiastReference.getLastName());
			statement.setString(3, enthusiastReference.getFirstName());
			statement.setString(4, enthusiastReference.getMiddleName());
			statement.setString(5, enthusiastReference.getSex());
			statement.setDate(6, Date.valueOf(enthusiastReference
						.getDateOfBirth()
						.toStringDate()));
			statement.setInt(7, enthusiastReference.getID());

			int rowsAffected = statement.executeUpdate();
			if(rowsAffected > 0)
				System.out.println("Enthusiast updated");
			else
				System.out.println("Failed to update");
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return enthusiastReference;
	}
	
	/* Gets Enthusiasts from DB
	 *
	 * @return ArrayList of enthusiasts found
	 * */
	public ArrayList<Enthusiast> getEnthusiasts() {
		ArrayList<Enthusiast> enthusiastList = new ArrayList<>();

		String sql = "SELECT * FROM enthusiast";
	
		try(Connection conn = DatabaseConnection.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql)) {
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				Enthusiast enthusiast = new Enthusiast(
					rs.getInt("enthusiast_id"),
					rs.getString("username"),
					rs.getString("lastname"),
					rs.getString("firstname"),
					rs.getString("middlename"),
					rs.getString("sex"),
					rs.getString("date_of_birth"),
					rs.getString("created_at"),
					rs.getBoolean("status")
				);
				enthusiastList.add(enthusiast);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}

		return enthusiastList;
	}

		
	/* Gets Enthusiasts from DB
	 *
	 * @return ArrayList of enthusiasts found
	 * */
	public ArrayList<Enthusiast> getActiveEnthusiasts() {
		ArrayList<Enthusiast> enthusiastList = new ArrayList<>();

		String sql = "SELECT * FROM enthusiast WHERE status = true";
	
		try(Connection conn = DatabaseConnection.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql)) {
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				Enthusiast enthusiast = new Enthusiast(
					rs.getInt("enthusiast_id"),
					rs.getString("username"),
					rs.getString("lastname"),
					rs.getString("firstname"),
					rs.getString("middlename"),
					rs.getString("sex"),
					rs.getString("date_of_birth"),
					rs.getString("created_at"),
					rs.getBoolean("status")
				);
				enthusiastList.add(enthusiast);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}

		return enthusiastList;
	}

	
	/* Get an Enthusiast from DB using the primary key, ID
	 *
	 * @return The found Enthusiast or null depending if its found or not 
	 * */
	public Enthusiast searchEnthusiastByID(int ID) {
		String sql = "SELECT * FROM enthusiast WHERE enthusiast_id = ?";
		Enthusiast enthusiast = null;
	
		try(Connection conn = DatabaseConnection.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql)) {
			statement.setInt(1, ID);

			ResultSet rs = statement.executeQuery();
			if(rs.next()) {
				enthusiast = new Enthusiast(
					rs.getInt("enthusiast_id"),
					rs.getString("username"),
					rs.getString("lastname"),
					rs.getString("firstname"),
					rs.getString("middlename"),
					rs.getString("sex"),
					rs.getString("date_of_birth"),
					rs.getString("created_at"),
					rs.getBoolean("status")
				);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}

		return enthusiast;
	}
}