package controller;

import model.*;
import view.*;
import java.util.*;

public class EnthusiastController {
	private GUIView prevView;
	private GUIEnthusiastView view;
	private EnthusiastManagement model;
	private EngagementManagement engagementModel;

	public EnthusiastController(GUIView prevView, GUIEnthusiastView view) {
		this.prevView = prevView;
		this.view = view;

		model = new EnthusiastManagement();
		engagementModel = new EngagementManagement();
	} 

	public LinkedHashMap<String, String> handleFeed() {
		//key contains CATEGORY-NAME-ID using '-' as a delimiter
		LinkedHashMap<String, String> information = new LinkedHashMap<>();
		
		ArrayList<Object> rawFeed = engagementModel.getFeed();
		String key = "";
		String value = "";
		for(Object o : rawFeed) {
			if(o == null) { continue; }
			if(o instanceof Player) {
				Player p = (Player) o;
				key = "player-" + p.getLastName() + ", " + p.getFirstName() + " " + p.getMiddleName() + "-" + String.valueOf(p.getPlayerID());	
				value = p.toHtmlString();
			} else if(o instanceof Coach) {
				Coach c = (Coach) o;
				key = "coach-" + c.getLastName() + ", " + c.getFirstName() + " " + c.getMiddleName() + "-" + String.valueOf(c.getCoachID());	
				value = c.toHtmlString();
			} else if(o instanceof Tournament) {
				Tournament t = (Tournament) o;
				key = "tournament-" + t.getTournamentName() + "-" + String.valueOf(t.getTournamentID());	
				value = t.toHtmlString();
			}

			information.put(key,value);
		}

		return information;
	}

	public boolean handleEngagement(int targetID, int enthusiastID, String category, String type) {
		int engagementID = engagementModel.searchEngagement(enthusiastID, targetID, category, type);
		if(engagementID == -1) {
			engagementModel.addEngagement(new Engagement(category, type, enthusiastID, targetID));
			return true;
		} else {
			if(engagementModel.toggleEngagementStatus(engagementID))
				return true;
		}
		return false;
	}

	public LinkedHashMap<String, String> handleFollowing(Enthusiast enthusiast) {
		LinkedHashMap<String, String> information = new LinkedHashMap<>();

		for(Engagement e : engagementModel.getEngagement(enthusiast.getID(), "follow")) {
			if(e == null) { continue; }
			String value = "";

			switch(e.getTargetCategory()) {
				case "player":
					Player p = new PlayerManagement().searchPlayer(e.getTargetID()); 
					value = "[player] " + p.getLastName() + ", " + p.getFirstName() + " ";  		
					if(!p.getMiddleName().isEmpty() || p.getMiddleName() != null) {
						value += p.getMiddleName();
					}
					break;
				case "coach":
					Coach c = new CoachManagement().searchCoach(e.getTargetID()); 
					value = "[coach] " + c.getLastName() + ", " + c.getFirstName() + " ";  		
					if(!c.getMiddleName().isEmpty() || c.getMiddleName() != null) {
						value += c.getMiddleName();
					}	
					break;
				case "tournament":
					Tournament t = new TournamentManagement().searchTournamentByID(e.getTargetID()); 
					value = "[tournament] Season " + t.getSeasonYear() + " " + t.getTournamentType(); 
					break;
			}

			String key = String.valueOf(e.getID());
			information.put(key, value);
		}

		return information;
	}

	public LinkedHashMap<String, String> handleLikes(Enthusiast enthusiast) {
		LinkedHashMap<String, String> information = new LinkedHashMap<>();

		for(Engagement e : engagementModel.getEngagement(enthusiast.getID(), "like")) {
			if(e == null) { continue; }
			String value = "";

			switch(e.getTargetCategory()) {
				case "player":
					Player p = new PlayerManagement().searchPlayer(e.getTargetID()); 
					value = "[player] " + p.getLastName() + ", " + p.getFirstName() + " ";  		
					if(!p.getMiddleName().isEmpty() || p.getMiddleName() != null) {
						value += p.getMiddleName();
					}
					break;
				case "coach":
					Coach c = new CoachManagement().searchCoach(e.getTargetID()); 
					value = "[coach] " + c.getLastName() + ", " + c.getFirstName() + " ";  		
					if(!c.getMiddleName().isEmpty() || c.getMiddleName() != null) {
						value += c.getMiddleName();
					}	
					break;
				case "tournament":
					Tournament t = new TournamentManagement().searchTournamentByID(e.getTargetID()); 
					value = "[tournament] Season " + t.getSeasonYear() + " " + t.getTournamentType(); 
					break;
			}

			String key = String.valueOf(e.getID());
			information.put(key, value);
			System.out.println(e.getSimpleInfo());
		}

		return information;
	}

	public ArrayList<Engagement> getEngagements(int id) {
		ArrayList<Engagement> activeLikes = engagementModel.getEngagement(id, "like");
		ArrayList<Engagement> activeFollows = engagementModel.getEngagement(id, "follow");
		ArrayList<Engagement> engagements = new ArrayList<>();

		engagements.addAll(activeLikes);
		engagements.addAll(activeFollows);

		return engagements;
	}

	public void handleUpdate(Enthusiast updatedEnthusiast) {
		Enthusiast enthusiast = model.updateEnthusiast(updatedEnthusiast);
		view.passUpdatedEnthusiast(enthusiast);
	}

	public void handleToggle(int engagementId) {
		engagementModel.toggleEngagementStatus(engagementId);
	}

	public void handleDelete(Enthusiast enthusiast) {
		model.toggleEnthusiast(enthusiast.getID());
	}

	public void handleLogout() {
		view.dispose();
		prevView.menuPanel();
		prevView.show();
	}

}