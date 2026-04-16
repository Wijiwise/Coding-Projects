package view;

import model.*;
import java.util.ArrayList;

public interface EngagementView {
	String showEngagementPage();
	boolean showProfiles(String scrollType, ArrayList<Object> feedList, int currentIndex);
	void engageProfile(String type);
}
