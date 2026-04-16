package controller;

import model.*;
import view.*;
import java.util.ArrayList;

public class MainController {
	private GUIView view;
	//admin
	private GUIAdminView adminView;
	private AdminController adminController;
	//enthusiast
	private GUIEnthusiastView enthusiastView;
	private EnthusiastController enthusiastController;
	private EnthusiastManagement enthusiastModel;
	//player
	private GUIPlayerView playerView;
	private PlayerManagement playerModel;
	private PlayerController playerController;

	

	public MainController(GUIView view) {
		//gui inits
		this.view = view;

		this.adminView = new GUIAdminView(); 
		adminController = new AdminController(view, adminView);	

		this.enthusiastView = new GUIEnthusiastView();
		enthusiastController = new EnthusiastController(view, enthusiastView);
		enthusiastModel = new EnthusiastManagement();
		
		this.playerView = new GUIPlayerView();
		playerModel = new PlayerManagement();
		playerController = new PlayerController(view, playerView);

		
	}
	
	public void handleMenu(String option) {
		switch(option) {
			case "admin":
				view.adminLoginPanel();
				break;
			case "login":
				view.loginPanel();
				break;
			case "register":
				break;
		}
	}

	public boolean handleAdminLogin(String password) {
		if(password.equals("admin123")) {
			adminView.setListeners(adminController);
			adminView.start();
			
			//transfer to sub-view (admin profile)
			view.hide();
			adminView.show();
		} else {
			return false;
		}

		return true;
	}


	public String handleEnthusiastLogin(int ID) {
		Enthusiast enthusiast = enthusiastModel.searchEnthusiastByID(ID);
		if(enthusiast == null) return "ERR|DNE"; //does not exist
		if(!enthusiast.getStatus()) return "ERR|DA"; //deactivated
		enthusiastView.setListeners(enthusiastController);
		enthusiastView.start(enthusiast);
		enthusiastView.profilePanel();
		view.hide();
		enthusiastView.show();

		return null;
	}

	public void handleEnthusiastRegister(Enthusiast enthusiast) {
		enthusiastModel.addEnthusiast(enthusiast);
	}

	public boolean handlePlayerLogin(int ID) {
		Player player = playerModel.searchPlayer(ID);
		if(player == null) return false;

		playerView.setListeners(playerController);
		playerView.start(player);
		playerView.profilePanel();
		view.hide();
		playerView.show();

		return true;
	}

	public void handlePlayerRegister(Player player) {
		playerModel.addPlayer(player);
	}
}