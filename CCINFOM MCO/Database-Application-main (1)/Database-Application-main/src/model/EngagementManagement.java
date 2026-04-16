package model;

import util.*;
import view.LoginBuilder; 
import java.util.ArrayList;
import java.util.Set;
import java.lang.String;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;

public class EngagementManagement {
	public EngagementManagement() {}

	/* Add Engagement to DB 
	 * 
	 * @param engagement The built engagement to add
	 * */
	public void addEngagement(Engagement engagement) {
		//add if condition depending on the type
		String sql = "INSERT INTO engagement (engagement_type, enthusiast_id, status) VALUES(?, ?, ?)";

		try(Connection conn = DatabaseConnection.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			statement.setString(1, engagement.getType());
			statement.setInt(2, engagement.getEnthusiastID());
			statement.setBoolean(3, true);

			if(statement.executeUpdate() == 0)
				throw new SQLException("Engagement creation failed. No ID Found.");

			int engagementId = -1;
			try(ResultSet generatedKeys = statement.getGeneratedKeys()) {
				if(generatedKeys.next()) {
					engagementId = generatedKeys.getInt(1);
				} else {
					throw new SQLException("Engagement creation failed. No ID Found.");
				}
			}
			
			String category = engagement.getTargetCategory().toLowerCase();
			String subtypeSql = "";
			switch(category) {
				case "player":
					subtypeSql = "INSERT INTO engagement_player (engagement_id, player_id) VALUES(?, ?)";
					break;
				case "coach":
					subtypeSql = "INSERT INTO engagement_coach (engagement_id, coach_id) VALUES(?, ?)";
					break;
				case "tournament":
					subtypeSql = "INSERT INTO engagement_tournament (engagement_id, tournament_id) VALUES(?, ?)";
					break;
				default:
					throw new SQLException("Invalid category " + category);
			}

			try(PreparedStatement subtypeStatement = conn.prepareStatement(subtypeSql)) {
				subtypeStatement.setInt(1, engagementId);
				subtypeStatement.setInt(2, engagement.getTargetID());
				subtypeStatement.executeUpdate();
			}

			//debugging
			System.out.println("You have " + engagement.getType() + " this " + engagement.getTargetCategory());

		} catch(SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean toggleEngagementStatus(int engagementID) {
		String sql = "UPDATE engagement SET status = CASE WHEN status = true THEN false ELSE true END WHERE engagement_id = ?";
		String checkSql = "SELECT status FROM engagement WHERE engagement_id = ?";
		
		try(Connection conn = DatabaseConnection.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql);
			PreparedStatement checkStatement = conn.prepareStatement(checkSql)) {

			statement.setInt(1, engagementID);
			statement.executeUpdate();

			checkStatement.setInt(1, engagementID);
			ResultSet rs = checkStatement.executeQuery();
			if(rs.next()) {
				int newStatus = rs.getInt("status");
				return newStatus == 1;
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	/* Checks if engagement exists already
	 *
	 * @return engagement id
	 */
	public int searchEngagement(int enthusiastID, int targetID, String category, String type) {
		String playerSql = 
			"SELECT e.engagement_id " +		
			"FROM engagement e " +
			"JOIN engagement_player ep ON e.engagement_id = ep.engagement_id " +
			"WHERE e.enthusiast_id = ? " +
				"AND ep.player_id = ? " +
				"AND e.engagement_type = ? ";
		String coachSql = 
			"SELECT e.engagement_id " +		
			"FROM engagement e " +
			"JOIN engagement_coach ec ON e.engagement_id = ec.engagement_id " +
			"WHERE e.enthusiast_id = ? " +
				"AND ec.coach_id = ? " + 
				"AND e.engagement_type = ? ";
		String tournamentSql = 
			"SELECT e.engagement_id " +		
			"FROM engagement e " +
			"JOIN engagement_tournament et ON e.engagement_id = et.engagement_id " +
			"WHERE e.enthusiast_id = ? " +
				"AND et.tournament_id = ? " +
				"AND e.engagement_type = ? ";

		String sql = "";
		switch(category) {
			case "player":
				sql += playerSql; 
				break;
			case "coach":
				sql += coachSql;
				break;
			case "tournament":
				sql += tournamentSql;
				break;
			default:
				return -1; //invalid category
		}

		try(Connection conn = DatabaseConnection.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			statement.setInt(1, enthusiastID);
			statement.setInt(2, targetID);
			statement.setString(3, type);
			ResultSet rs = statement.executeQuery();

			if(rs.next()) {
				return rs.getInt("engagement_id"); 
			} else {
				return -1;
			}
				
		} catch(SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}
	public ArrayList<Engagement> getEngagement(int enthusiastID, String type) {
		ArrayList<Engagement> engagementList = new ArrayList<>();

		String sql = 
      	"SELECT g.engagement_id, g.engagement_type, " +
			"	g.enthusiast_id, g.status, g.created_at, " +
      	"	ep.player_id, ec.coach_id, et.tournament_id " +
      	"FROM engagement g " +
			"LEFT JOIN engagement_player ep ON g.engagement_id = ep.engagement_id " +
      	"LEFT JOIN engagement_coach ec ON g.engagement_id = ec.engagement_id " +
      	"LEFT JOIN engagement_tournament et ON g.engagement_id = et.engagement_id " +
      	"WHERE g.enthusiast_id = ? " +
   		"	AND g.engagement_type = ?" +
		  	"	AND g.status = true";
		
		try(Connection conn = DatabaseConnection.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql)) {
			statement.setInt(1, enthusiastID);
			statement.setString(2, type);

			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				int targetId = -1;
				String category = "";

				if(rs.getObject("player_id") != null) {
					targetId = rs.getInt("player_id");
					category = "player";
				} else if(rs.getObject("coach_id") != null) {
					targetId = rs.getInt("coach_id");
					category = "coach";
				} else if(rs.getObject("tournament_id") != null) {
					targetId = rs.getInt("tournament_id");
					category = "tournament";
				}
				
				Engagement engagement = new Engagement(
					rs.getInt("engagement_id"),
					category,
					rs.getString("engagement_type"),
					rs.getInt("enthusiast_id"),
					targetId,
					rs.getString("created_at")
				);

				engagementList.add(engagement);
			} 		
		} catch(SQLException e) {
			e.printStackTrace();
		}

		return engagementList;
	}

	//THE TARGET
	//THE TOTAL LIKES
	//THE TOTAL FOLLOWS
	public ArrayList<Object> getFeed() {	
		ArrayList<Object> feed = new ArrayList<>();

		//select everyone!! muhehehee
		//and then that table will be used
		//to order them by MOST recent!
		String sql = 
			"CREATE TABLE IF NOT EXISTS all_ids AS " +
         "SELECT player_id AS id, 'player' AS source, created_at " +
			"FROM player " +
         "UNION ALL " +
         "SELECT coach_id AS id, 'coach' AS source, created_at " +
         "FROM coach " +
         "UNION ALL " +
         "SELECT tournament_id AS id, 'tournament' AS source, created_at " +
         "FROM tournament " +
         "ORDER BY created_at DESC ";

		//get the stuff and stuff
		//it in the ykyk the array list
		//thats the feed for enthusiast
		try(Connection conn = DatabaseConnection.getConnection();
			Statement statement = conn.createStatement()) {
			statement.executeUpdate(sql);
			System.out.println("Successfully created table");

			ResultSet rs = statement.executeQuery("SELECT * FROM all_ids"); 
			while(rs.next()) {
				String source = rs.getString("source");
				int id = rs.getInt("id");
				
				/*waiting for groupmate crud*/
				Object o;
				switch(source) {
					case "player":
						o = new PlayerManagement().searchPlayer(id); 
						feed.add(o);
						break;
					case "coach":
						o = new CoachManagement().searchCoach(id);
						feed.add(o);
						break;
					case "tournament":
						o = new TournamentManagement().searchTournamentByID(id);
						feed.add(o);
						break;
				}
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}

		return feed;
	}

	//report functions
	public int getTotalEngagement(int ID) {
		String sql =
			"SELECT COUNT(*) AS total_engagement " + 
			"FROM engagement e " + 
			"WHERE e.enthusiast_id = ? " +
			"AND e.status = true";

		try(Connection conn = DatabaseConnection.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			statement.setInt(1, ID);
			ResultSet rs = statement.executeQuery();

			if(rs.next())
				return rs.getInt("total_engagement");
			else
				return 0;
		} catch(SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}


	public int getTotalPlayerEngagement(int ID) {
		String sql =
			"SELECT COUNT(*) AS total_player_engagement " +
			"FROM engagement e " + 
			"JOIN engagement_player ep ON e.engagement_id = ep.engagement_id " + 
			"WHERE e.enthusiast_id = ? " +
			"AND e.status = true";

		try(Connection conn = DatabaseConnection.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			statement.setInt(1, ID);
			ResultSet rs = statement.executeQuery();

			if(rs.next())
				return rs.getInt("total_player_engagement");
			else
				return 0;
		} catch(SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}	

	public int getTotalCoachEngagement(int ID) {
		String sql =
			"SELECT COUNT(*) AS total_coach_engagement " +
			"FROM engagement e " + 
			"JOIN engagement_coach ep ON e.engagement_id = ep.engagement_id " + 
			"WHERE e.enthusiast_id = ?";
		
		try(Connection conn = DatabaseConnection.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			statement.setInt(1, ID);
			ResultSet rs = statement.executeQuery();

			if(rs.next())
				return rs.getInt("total_coach_engagement");
			else
				return 0;
		} catch(SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}	

	public int getTotalTournamentEngagement(int ID) {
		String sql =
			"SELECT COUNT(*) AS total_tournament_engagement " +
			"FROM engagement e " + 
			"JOIN engagement_tournament ep ON e.engagement_id = ep.engagement_id " + 
			"WHERE e.enthusiast_id = ?";

		try(Connection conn = DatabaseConnection.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			statement.setInt(1, ID);
			ResultSet rs = statement.executeQuery();

			if(rs.next())
				return rs.getInt("total_tournament_engagement");
			else
				return 0;
		} catch(SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}	

	public int getAvgEngagements(int ID) {
		String sql =
			"SELECT AVG(cnt) AS avg_engagements " +
			"FROM ( " +
				"SELECT COUNT(*) AS cnt " + 
				"FROM engagement e " +
				"JOIN engagement_player ep ON e.engagement_id = ep.engagement_id " +
				"WHERE e.enthusiast_id = ? " +

				 "UNION ALL " +

				 "SELECT COUNT(*) " +
				 "FROM engagement e " +
				 "JOIN engagement_coach ec ON e.engagement_id = ec.engagement_id " +
				 "WHERE e.enthusiast_id = ? " +
				 "UNION ALL " +
				 "SELECT COUNT(*) " +
				 "FROM engagement e " +
				 "JOIN engagement_tournament et ON e.engagement_id = et.engagement_id " +
				 "WHERE e.enthusiast_id = ? " +
			") AS sub";

		try(Connection conn = DatabaseConnection.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			statement.setInt(1, ID);
			statement.setInt(2, ID);
			statement.setInt(3, ID);
			ResultSet rs = statement.executeQuery();

			if(rs.next())
				return rs.getInt("avg_engagements");
			else
				return 0;
		} catch(SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

	public String getLastEngagement(int ID) {
		String sql = 
			"SELECT MAX(e.created_at) AS last_engagement " +
			"FROM engagement e " + 
			"WHERE e.enthusiast_id = ? "; 

		try(Connection conn = DatabaseConnection.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			statement.setInt(1, ID);
			ResultSet rs = statement.executeQuery();

			if(rs.next())
				return rs.getString("last_engagement") == null ? "N/A" : rs.getString("last_engagement");
			else
				return "N/A";
		} catch(SQLException e) {
			e.printStackTrace();
			return "N/A";
		}
	}
}