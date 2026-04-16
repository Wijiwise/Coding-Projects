
package model;

import util.*;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;

public class GameManagement {

    // constructor
    public GameManagement() {
    }

    // add game
    public int addGame(Game game) {
        String sql = "INSERT INTO game (game_id, tournament_id, competing_teamA_id, competing_teamB_id, winning_team_id, losing_team_id, score_ratio, game_status, start_date, end_date)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, game.getGameID());
            statement.setInt(2, game.getTournamentID());
            statement.setInt(3, game.getCompetingTeamA_ID());
            statement.setInt(4, game.getCompetingTeamB_ID());
            statement.setInt(5, game.getWinningTeamID());
            statement.setInt(6, game.getLosingTeamID());
            statement.setString(7, game.getScoreRatio());
            statement.setString(8, game.getGameStatus());
            statement.setDate(9, Date.valueOf(game.getStartDate().toStringDate()));
            statement.setDate(10, Date.valueOf(game.getEndDate().toStringDate()));

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0)
                System.out.println("Game added.");

            // For finding the new id to return
            try (ResultSet generatedKey = statement.getGeneratedKeys()) {
                if (generatedKey.next()) {
                    int newID = generatedKey.getInt(1);
                    return newID;
                } else {
                    throw new SQLException("Failed Creating Game");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;

    }

    // delete game
    public void deleteGame(int gameID) {
        String sql = "DELETE FROM game WHERE game_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, gameID);

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0)
                System.out.println("Game deleted");
            else
                System.out.println("Failed to delete");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    // update game
    public Game updateGame(Game gameReference) {
        String sql = "UPDATE game SET " +
                "tournament_id = ?, " +
                "competing_teamA_id = ?, " +
                "competing_teamB_id = ?, " +
                "winning_team_id = ?, " +
                "losing_team_id = ?, " +
                "score_ratio = ?, " +
                "game_status = ?, " +
                "start_date = ?, " +
                "end_date = ? " +

                "WHERE game_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, gameReference.getGameID());
            statement.setInt(2, gameReference.getTournamentID());
            statement.setInt(3, gameReference.getCompetingTeamA_ID());
            statement.setInt(4, gameReference.getCompetingTeamB_ID());
            statement.setInt(5, gameReference.getWinningTeamID());
            statement.setInt(6, gameReference.getLosingTeamID());
            statement.setString(7, gameReference.getScoreRatio());
            statement.setString(8, gameReference.getGameStatus());
            statement.setDate(9, Date.valueOf(gameReference.getStartDate().toStringDate()));
            statement.setDate(10, Date.valueOf(gameReference.getEndDate().toStringDate()));

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0)
                System.out.println("Game updated");
            else
                System.out.println("Failed to update");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return gameReference;
    }

    // get all game
    public ArrayList<Game> getGames() {
        ArrayList<Game> gameList = new ArrayList<>();

        String sql = "SELECT * FROM game";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Game game = new Game(
                        rs.getInt("game_id"),
                        rs.getInt("tournament_id"),
                        rs.getInt("competing_teamA_id"),
                        rs.getInt("competing_teamB_id"),
                        rs.getInt("winning_team_id"),
                        rs.getInt("losing_team_id"),
                        rs.getString("score_ratio"),
                        rs.getString("game_status"),
                        rs.getString("start_date"),
                        rs.getString("end_date")
                );
                gameList.add(game);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return gameList;
    }


    // get specific game
    public Game searchGameByID(int ID) {
        String sql = "SELECT * FROM game WHERE game_id = ?";
        Game game = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, ID);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                game = new Game(
                    rs.getInt("game_id"),
                    rs.getInt("tournament_id"),
                    rs.getInt("competing_teamA_id"),
                    rs.getInt("competing_teamB_id"),
                    rs.getInt("winning_team_id"),
                    rs.getInt("losing_team_id"),
                    rs.getString("score_ratio"),
                    rs.getString("game_status"),
                    rs.getString("start_date"),
                    rs.getString("end_date")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return game;
    }

    // validation
    public boolean tournamentExists(int tournamentID) {
        String sql = "SELECT 1 FROM tournament WHERE tournament_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, tournamentID);
            ResultSet rs = statement.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }







    boolean teamExists(int teamID) {return true;}
    boolean gameExists(int gameID) {return true;}
    boolean isTeamInTournament(int teamID, int tournamentID) {return true;}
    boolean isWinnerValid(Game game) {return true;}
    boolean isGameInBracket(int gameID) {return true;}

}
