package model;

import util.*;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;

public class TournamentManagement {
    public TournamentManagement() {}

    // add tournament
    public int addTournament(Tournament tournament) {
        String sql = "INSERT INTO tournament (tournament_name, season_year, tournament_type, start_date, end_date) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Plug the required ? values
            statement.setString(1, tournament.getTournamentName());
            statement.setInt(2, tournament.getSeasonYear());
            statement.setString(3, tournament.getTournamentType());
            statement.setDate(4, Date.valueOf(tournament.getStartDate().toStringDate()));
            statement.setDate(5, Date.valueOf(tournament.getEndDate().toStringDate()));

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0)
                System.out.println("Tournament added.");

            // For finding the new id to return
            try (ResultSet generatedKey = statement.getGeneratedKeys()) {
                if (generatedKey.next()) {
                    int newID = generatedKey.getInt(1);
                    return newID;
                } else {
                    throw new SQLException("Failed Creating Tournament");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    // delete tournament
    public void deleteTournament(int tournamentID) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // First delete related records in engagement_tournament table
            String deleteEngagementSQL = "DELETE FROM engagement_tournament WHERE tournament_id = ?";
            try (PreparedStatement engagementStmt = conn.prepareStatement(deleteEngagementSQL)) {
                engagementStmt.setInt(1, tournamentID);
                engagementStmt.executeUpdate();
            }

            // Then delete the tournament
            String deleteTournamentSQL = "DELETE FROM tournament WHERE tournament_id = ?";
            try (PreparedStatement tournamentStmt = conn.prepareStatement(deleteTournamentSQL)) {
                tournamentStmt.setInt(1, tournamentID);
                int rowsDeleted = tournamentStmt.executeUpdate();

                if (rowsDeleted > 0) {
                    conn.commit(); // Commit transaction
                    System.out.println("Tournament and related records deleted successfully");
                } else {
                    conn.rollback(); // Rollback if tournament not found
                    System.out.println("Tournament not found");
                }
            }
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback on error
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Reset auto-commit
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // update tournament
    public Tournament updateTournament(Tournament tournamentReference) {
        String sql = "UPDATE tournament SET " +
                "tournament_name = ?, " +
                "season_year = ?, " +
                "tournament_type = ?, " +
                "start_date = ?, " +
                "end_date = ? " +
                "WHERE tournament_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, tournamentReference.getTournamentName());
            statement.setInt(2, tournamentReference.getSeasonYear());
            statement.setString(3, tournamentReference.getTournamentType());
            statement.setDate(4, Date.valueOf(tournamentReference.getStartDate().toStringDate()));
            statement.setDate(5, Date.valueOf(tournamentReference.getEndDate().toStringDate()));
            statement.setInt(6, tournamentReference.getTournamentID());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0)
                System.out.println("Tournament updated");
            else
                System.out.println("Failed to update");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tournamentReference;
    }

    // get all tournament
    public ArrayList<Tournament> getTournaments() {
        ArrayList<Tournament> tournamentList = new ArrayList<>();

        String sql = "SELECT * FROM tournament";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Tournament tournament = new Tournament(
                        rs.getInt("tournament_id"),
                        rs.getString("tournament_name"),
                        rs.getInt("season_year"),
                        rs.getString("tournament_type"),
                        rs.getString("start_date"),
                        rs.getString("end_date")
                );
                tournamentList.add(tournament);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tournamentList;
    }

    // get a specific tournament by using id
    public Tournament searchTournamentByID(int ID) {
        String sql = "SELECT * FROM tournament WHERE tournament_id = ?";
        Tournament tournament = null;

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, ID);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                tournament = new Tournament(
                        rs.getInt("tournament_id"),
                        rs.getString("tournament_name"),
                        rs.getInt("season_year"),
                        rs.getString("tournament_type"),
                        rs.getString("start_date"),
                        rs.getString("end_date")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tournament;
    }
}