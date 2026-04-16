package model;

import util.*;

public class Engagement {
	private int ID;
	private String targetCategory;
	private String type;
	private int enthusiastID;
	private int targetID;
	private CustomTimestamp createdAt;
	//temporary until IDs are implemented for other classes

	public Engagement() {
		//empty constructor
	};

	public Engagement(int ID, String targetCategory, String type, int enthusiastID, int targetID, String createdAt) {
		this.ID = ID;
		this.targetCategory = targetCategory;
		this.type = type;
		this.enthusiastID = enthusiastID;
		this.targetID = targetID;
		
		String[] joinPart = createdAt.split(" ");
		String[] joinTokens = joinPart[0].split("-");
		this.createdAt = new CustomTimestamp(
			Integer.parseInt(joinTokens[0]), 
			Integer.parseInt(joinTokens[1]), 
			Integer.parseInt(joinTokens[2])
		);
	}

	public Engagement(String targetCategory, String type, int enthusiastID, int targetID) {
		this.targetCategory = targetCategory;
		this.type = type;
		this.enthusiastID = enthusiastID;
		this.targetID = targetID;
	}

	public String getSimpleInfo() {
		return targetCategory + " [ID " + String.valueOf(targetID) + "] since " + createdAt.toStringDate();
	}

	public int getID() { return ID; }
	public String getTargetCategory() { return targetCategory; }
	public String getType() { return type; }
	public int getEnthusiastID() { return enthusiastID; }
	public int getTargetID() { return targetID; }
	public CustomTimestamp getCreatedAt() { return createdAt; }

	public void setID(int ID) { this.ID = ID; }
	public void setTargetCategory(String targetCategory) { this.targetCategory = targetCategory; }
	public void setType(String type) { this.type = type; }
	public void setEnthusiastID(int enthusiastID) { this.enthusiastID = enthusiastID; }
	public void setTargetID(int targetID) { this.targetID = targetID; }
}