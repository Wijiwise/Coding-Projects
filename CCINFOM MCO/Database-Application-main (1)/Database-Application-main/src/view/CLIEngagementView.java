package view;

import java.util.Scanner;
import java.util.ArrayList;
import model.*;

public class CLIEngagementView implements EngagementView {
	Scanner input = new Scanner(System.in);

	private int getInput(int startRange, int endRange) {
		int userInput = -1;
		String prefix = "input # ";

		do {
			try {
				System.out.print(prefix);
				userInput = input.nextInt();	
				if(userInput < startRange || userInput > endRange) {
					System.out.println(prefix + "Out of range!");
				}
			} catch(Exception E) {
				System.out.println(prefix + "Please enter a valid input!");
			}
			input.nextLine();
		} while(userInput < startRange || userInput > endRange);

		return userInput;
	}

	@Override
	public String showEngagementPage() {
		System.out.println("_______________________");	
		System.out.println("BasketGRAM");
		System.out.println("1. Scroll Up");
		System.out.println("2. Scroll Down");
		System.out.println("3. Like");
		System.out.println("4. Follow");
		System.out.println("5. Profile");
		System.out.println("6. Logout");

		switch(getInput(1,6)) {
			case 1: return "scroll up";
			case 2: return "scroll down";
			case 3: return "like";
			case 4: return "follow";
			case 5: return "profile";
			case 6: return "exit";
		}

		return null;
	}

	//just for viewing the profiles (from all core records involved)
	@Override
	public boolean showProfiles(String scrollType, ArrayList<Object> feedList, int currentIndex) {
		Object profile = feedList.get(currentIndex);
		System.out.println("_______________________");	
		System.out.println("FEED");
		if(profile != null) {
			if(profile instanceof Player) {
				System.out.println("Player: ");
			} else if(profile instanceof Coach) {
				System.out.println("Coach: ");
			} else if(profile instanceof Tournament) {
				System.out.println("Tournament: ");
			}
			return true;
		}
		return false;
	}

	//for interacting
	@Override
	public void engageProfile(String type) {

	}
}
