package model;

import util.*;

import java.sql.*;
import java.util.ArrayList;


public class CoachManagement {
    public CoachManagement() {
    }

    public int addCoach(Coach coach) {
        String sql = "INSERT INTO coach (lastname, firstname, middlename, sex, date_of_birth, start_year, end_year, status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, coach.getLastName());
            statement.setString(2, coach.getFirstName());
            statement.setString(3, coach.getMiddleName());
            statement.setString(4, String.valueOf(coach.getGender()));
            statement.setDate(5, Date.valueOf(coach.getBirthday().toStringDate()));
            statement.setInt(6, coach.getStartYear());
            if(coach.getEndYear() == null)
                statement.setNull(7, Types.INTEGER);
            else
                statement.setInt(7, coach.getEndYear());
            statement.setBoolean(8, coach.isInGameStatus());

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0)
                System.out.println("Coach added.");

            try (ResultSet generatedKey = statement.getGeneratedKeys()) {
                if (generatedKey.next()) {
                    int newID = generatedKey.getInt(1);
                    return newID;
                } else
                    throw new SQLException("Failed creating coach.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public void deleteCoach(int coach_id) {
        String sql = "DELETE FROM coach WHERE coach_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, coach_id);

            int rowsDeleted = statement.executeUpdate();

            if (rowsDeleted > 0)
                System.out.println("Coach deleted.");
            else
                System.out.println("Failed to delete coach.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Coach updateCoach(Coach coach) {
        String sql = "UPDATE coach SET " +
                "lastname = ?, " +
                "firstname = ?, " +
                "middlename = ?, " +
                "date_of_birth = ?, " +
                "sex = ?, " +
                "start_year = ?, " +
                "end_year = ? " +
                "WHERE coach_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, coach.getLastName());
            statement.setString(2, coach.getFirstName());
            statement.setString(3, coach.getMiddleName());
            statement.setDate(4, Date.valueOf(coach.getBirthday().toStringDate()));
            statement.setString(5, coach.getGender());
            statement.setInt(6, coach.getStartYear());
            if(coach.getEndYear() == null) {
                statement.setNull(7, Types.INTEGER);
                updateCoachToCurrentStatus(coach);
            }
            else {
                statement.setInt(7, coach.getEndYear());
                updateCoachToPastStatus(coach);
            }
            statement.setInt(8, coach.getCoachID());

            int updatedRows = statement.executeUpdate();

            if (updatedRows > 0)
                System.out.println("Coach successfully updated.");
            else
                System.out.println("Update failed.");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return coach;
    }

    public void updateCoachToPastStatus(Coach coach) {
        String sql = "UPDATE coach SET " +
                    "status = ? " +
                    "WHERE coach_id = ? AND end_year IS NULL";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setBoolean(1, false);
            statement.setInt(2, coach.getCoachID());

            int updatedRows = statement.executeUpdate();
            if (updatedRows > 0)
                System.out.println("Status successfully updated.");
            else
                System.out.println("Status update failed.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateCoachToCurrentStatus(Coach coach) {
        String sql = "UPDATE coach SET " +
                "status = ? " +
                "WHERE coach_id = ? AND end_year IS NOT NULL";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setBoolean(1, true);
            statement.setInt(2, coach.getCoachID());

            int updatedRows = statement.executeUpdate();
            if (updatedRows > 0)
                System.out.println("Status successfully updated.");
            else
                System.out.println("Status update failed.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Coach searchCoach(int coach_id) {
        String sql = "SELECT * FROM coach WHERE coach_id = ?";
        Coach coach = null;

        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, coach_id);

            try(ResultSet rs = statement.executeQuery()) {
                if(rs.next()) {
                    coach = new Coach(
                            rs.getInt("coach_id"),
                            rs.getString("lastname"),
                            rs.getString("firstname"),
                            rs.getString("middlename"),
                            rs.getString("date_of_birth"),
                            rs.getString("sex"),
                            rs.getInt("start_year"),
                            rs.getInt("end_year"),
                            rs.getBoolean("status")
                    );
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return coach;
    }

    public ArrayList<Coach> getAllCoaches() {
        ArrayList<Coach> coaches = new ArrayList<>();
        String sql = "SELECT * FROM coach";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement statement = conn.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                Coach coach = new Coach(
                        rs.getInt("coach_id"),
                        rs.getString("lastname"),
                        rs.getString("firstname"),
                        rs.getString("middlename"),
                        rs.getString("date_of_birth"),
                        rs.getString("sex"),
                        rs.getInt("start_year"),
                        rs.getInt("end_year"),
                        rs.getBoolean("status")
                );
                coaches.add(coach);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return coaches;
    }

    public void toggleCoach(int ID) {
        String sql = "UPDATE coach SET status = CASE WHEN status = true THEN false ELSE true END WHERE coach_id = ?";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, ID);
            statement.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
}