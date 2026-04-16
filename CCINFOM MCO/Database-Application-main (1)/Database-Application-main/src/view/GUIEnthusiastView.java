package view;

import model.*;
import controller.*;
import util.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.DateTimeException;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUIEnthusiastView {
	private JFrame frame;
	private Container cp;

	private Enthusiast enthusiast; //1 set enthusiast for this GUI
	private EnthusiastController controller;

	public GUIEnthusiastView() {}	
		
	public void start(Enthusiast enthusiast) {
		frame = GUIUtil.setupGUI(frame, cp, 720, 480);
		this.enthusiast = enthusiast;
	}

	public void setListeners(EnthusiastController controller) {
		this.controller = controller;
	}

	public void profilePanel() {
		cp = BackgroundPanel.create("assets/login/enthusiast/profile.png");
		cp.setLayout(null);
		frame.setContentPane(cp);
		
		JLabel username = GUIUtil.createText(enthusiast.getUsername(),105,205,286,27);
		JLabel id = GUIUtil.createText(String.valueOf(enthusiast.getID()),416,205,92,27);
		JLabel sex = GUIUtil.createText(enthusiast.getSex(),532,205,87,27);
		JLabel fullname = GUIUtil.createText(enthusiast.getFullName(),105,261,514,27);
		JLabel birthday = GUIUtil.createText(enthusiast.getDateOfBirth()
				.getDisplayDate(),106,318,243,27);
		JLabel joinedBy = GUIUtil.createText(enthusiast.getJoinDate()
				.getDisplayDate(),375,318,245,27); 

		JButton engageBtn = GUIUtil.createIButton(423,0,97,53);
		JButton profileBtn = GUIUtil.createIButton(521,0,93,53);
		JButton logoutBtn = GUIUtil.createIButton(614,0,93,53);

		JButton updateBtn = GUIUtil.createIButton(455,116,80,38);
		JButton deleteBtn = GUIUtil.createIButton(544,116,80,36);
		JButton followingBtn = GUIUtil.createIButton(101,378,257,27);
		JButton likesBtn = GUIUtil.createIButton(368,378,257,27);

		cp.add(username);
		cp.add(id);
		cp.add(sex);
		cp.add(fullname);
		cp.add(birthday);
		cp.add(joinedBy);

		cp.add(engageBtn);
		cp.add(profileBtn);
		cp.add(logoutBtn);
		cp.add(updateBtn);
		cp.add(deleteBtn);
		cp.add(followingBtn);
		cp.add(likesBtn);

		engageBtn.addActionListener(e -> engagePanel(false, ""));
		profileBtn.addActionListener(e -> profilePanel());
		logoutBtn.addActionListener(e -> controller.handleLogout());

		updateBtn.addActionListener(e -> updatePanel());
		deleteBtn.addActionListener(e -> deletePanel());
		followingBtn.addActionListener(e -> followingPanel());
		likesBtn.addActionListener(e -> likesPanel());

		cp.revalidate();
		cp.repaint();
	}

	public void engagePanel(boolean search, String keyword) {
		cp = BackgroundPanel.create("assets/login/enthusiast/engage_bg.png");
		cp.setLayout(null);
		frame.setContentPane(cp);

		//get information from controller
		LinkedHashMap<String, String> feed = controller.handleFeed(); 	
		ArrayList<Engagement> engagement = controller.getEngagements(enthusiast.getID());

		JLabel overlayBg = new JLabel(new ImageIcon("assets/login/enthusiast/engage.png"));
		overlayBg.setBounds(0, 0, 720, 480);

		JButton submitBtn = GUIUtil.createIButton(598,66,88,27);
		JTextField field = GUIUtil.createTextField(63,67,514,27);

		JButton engageBtn = GUIUtil.createIButton(423,0,97,53);
		JButton profileBtn = GUIUtil.createIButton(521,0,93,53);
		JButton logoutBtn = GUIUtil.createIButton(614,0,93,53);
		
		cp.add(field);
		cp.add(submitBtn);
		cp.add(engageBtn);
		cp.add(profileBtn);
		cp.add(logoutBtn);
		
		/*
		 * The idea is that the value contains the objects simple information
		 * in string form. The key should contain whatever information required
		 * by controller to update/ toggle the follow or like made by the enthusiast 
		 * mainly
		 * 	enthusiastID (given)
		 * 	targetID 
		 * 	category (given)
		 * 	type (given)
		 * */

		JPanel feedPanel = new JPanel(null);	
		int index = 0;
		int gap = 156;
		int y = 121; 
		for(Map.Entry<String, String> entry : feed.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue(); 

			if(search && !keyword.isEmpty()) {
				if(!value.toLowerCase().contains(keyword.toLowerCase()) &&
						!key.toLowerCase().contains(keyword.toLowerCase())) { continue;}
			}

			String[] keyParts = key.split("-");	
			String category = keyParts[0];
			String userStr = keyParts[1];
			int id = Integer.parseInt(keyParts[2]);
			
			//creates the post
			JPanel post = new JPanel(null);
			post.setOpaque(false);
	
			JLabel postBg = new JLabel(new ImageIcon("assets/login/enthusiast/banner.png"));
			postBg.setBounds(0,0,593,148);
			postBg.setOpaque(false);

			boolean liked = false;
			String imagePath = "assets/login/enthusiast/";
			for(Engagement e : engagement) {
				if(e.getTargetID() == id && e.getType().equals("like")
						&& e.getTargetCategory().equalsIgnoreCase(category)) {
					liked = true;
					break;
				}
			}
			imagePath += liked ? "liked.png" : "like.png";

			JLabel likeBtnBg = new JLabel(new ImageIcon(imagePath));
			JButton likeBtn = GUIUtil.createIButton(505,26,27,25);
			likeBtnBg.setBounds(505,26,60,60);
			likeBtnBg.setOpaque(false);

			boolean followed = false;
			imagePath = "assets/login/enthusiast/";
			for(Engagement e : engagement) {
				if(e.getTargetID() == id && e.getType().equals("follow")
						&& e.getTargetCategory().equalsIgnoreCase(category)) {
					followed = true;
					break;
				}
			}
			imagePath += followed ? "followed.png" : "follow.png";

			JLabel followBtnBg = new JLabel(new ImageIcon(imagePath));
			JButton followBtn = GUIUtil.createIButton(540,26,33,25);
			followBtnBg.setBounds(540,26,60,60);
			followBtnBg.setOpaque(false);

			JLabel user = GUIUtil.createText(userStr,78,28,408,20);
			JLabel info = GUIUtil.createText(value,29,73,534,62);

			likeBtn.addActionListener(e -> {
				if(controller.handleEngagement(id, enthusiast.getID(), category, "like")) {
					likeBtnBg.setIcon(new ImageIcon("assets/login/enthusiast/liked.png"));
				} else {
					likeBtnBg.setIcon(new ImageIcon("assets/login/enthusiast/like.png"));
				}
			});
			followBtn.addActionListener(e -> {
				if(controller.handleEngagement(id, enthusiast.getID(), category, "follow")) {
					followBtnBg.setIcon(new ImageIcon("assets/login/enthusiast/followed.png"));
				} else {
					followBtnBg.setIcon(new ImageIcon("assets/login/enthusiast/follow.png"));
				}
			});

			//add to post
			post.add(user);
			post.add(info);
			post.add(likeBtn);
			post.add(followBtn);
			post.add(likeBtnBg);
			post.add(followBtnBg);
			post.add(postBg);

			post.setBounds(63,y+index*gap,593,148);
			feedPanel.add(post);
			index++;
		}

		submitBtn.addActionListener(e -> {
			String input = field.getText().trim();
			field.setText("");
			engagePanel(true, input);
		});
			
		cp.add(overlayBg);

		int totalHeight = y+feed.size() * gap;
		feedPanel.setBounds(0,0,720,totalHeight);
		feedPanel.setOpaque(false);
		cp.add(feedPanel);

		cp.addMouseWheelListener(new MouseWheelListener() {
			int offset = 0;

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) 
			{
				int rotation = e.getWheelRotation(); //1 down -1 up
				offset -= rotation * 30; //so 20 is the offset like when scrolled ykykyk

				int maxOffset = 0;
				int minOffset = 370 - totalHeight;
				offset = Math.max(minOffset, Math.min(maxOffset, offset));

				feedPanel.setLocation(0,offset);
				cp.repaint();
			}
		});

		profileBtn.addActionListener(e -> profilePanel());
		logoutBtn.addActionListener(e -> controller.handleLogout());

		cp.revalidate();
		cp.repaint();	
	}

	public void likesPanel() {
		cp = BackgroundPanel.create("assets/login/enthusiast/likes_bg.png");
		cp.setLayout(null);
		frame.setContentPane(cp);

		//get information from controller
		LinkedHashMap<String, String> information = controller.handleLikes(enthusiast);

		JLabel overlayBg = new JLabel(new ImageIcon("assets/login/enthusiast/likes.png"));
		JButton backBtn = GUIUtil.createIButton(648,440,64,27);
		JButton engageBtn = GUIUtil.createIButton(423,0,97,53);
		JButton profileBtn = GUIUtil.createIButton(521,0,93,53);
		JButton logoutBtn = GUIUtil.createIButton(614,0,93,53);
		JPanel scrollingPanel = GUIUtil.showScrollingPanel(information, -19, -3, 57, 6,
			clicked -> {
				String[] parts = clicked.split("/",2);
				String key = parts[0];
				String value = parts[1];

				int choice = JOptionPane.showConfirmDialog(
						frame,
						"Do you want to unlike?",
						"Unfollow",
						JOptionPane.YES_NO_OPTION
						);
				if(choice == JOptionPane.YES_OPTION) {
					controller.handleToggle(Integer.parseInt(key));						
					JOptionPane.showMessageDialog(null, "Unliked.");
					profilePanel();
				}
			});
			overlayBg.setBounds(0, 0, 720, 480);
		scrollingPanel.setBounds(0, 0, frame.getWidth(), frame.getHeight());
		
		cp.add(overlayBg);
		cp.add(backBtn);
		cp.add(engageBtn);
		cp.add(profileBtn);
		cp.add(logoutBtn);
		cp.add(scrollingPanel);

		engageBtn.addActionListener(e -> engagePanel(false, ""));
		backBtn.addActionListener(e -> profilePanel());
		profileBtn.addActionListener(e -> profilePanel());
		logoutBtn.addActionListener(e -> controller.handleLogout());


		cp.revalidate();
		cp.repaint();	
	}



	public void followingPanel() {
		cp = BackgroundPanel.create("assets/login/enthusiast/following_bg.png");
		cp.setLayout(null);
		frame.setContentPane(cp);

		//get information from controller
		LinkedHashMap<String, String> information = controller.handleFollowing(enthusiast);

		JLabel overlayBg = new JLabel(new ImageIcon("assets/login/enthusiast/following.png"));
		JButton backBtn = GUIUtil.createIButton(648,440,64,27);
		JButton engageBtn = GUIUtil.createIButton(423,0,97,53);
		JButton profileBtn = GUIUtil.createIButton(521,0,93,53);
		JButton logoutBtn = GUIUtil.createIButton(614,0,93,53);
		JPanel scrollingPanel = GUIUtil.showScrollingPanel(information, -19, -3, 57, 6,
			clicked -> {
				String[] parts = clicked.split("/",2);
				String key = parts[0];
				String value = parts[1];

				int choice = JOptionPane.showConfirmDialog(
						frame,
						"Do you want to unfollow?",
						"Unfollow",
						JOptionPane.YES_NO_OPTION
						);
				if(choice == JOptionPane.YES_OPTION) {
					controller.handleToggle(Integer.parseInt(key));						
					JOptionPane.showMessageDialog(null, "Unfollowed.");
					profilePanel();
				}
			});
			overlayBg.setBounds(0, 0, 720, 480);
		scrollingPanel.setBounds(0, 0, frame.getWidth(), frame.getHeight());
		
		cp.add(overlayBg);
		cp.add(backBtn);
		cp.add(engageBtn);
		cp.add(profileBtn);
		cp.add(logoutBtn);
		cp.add(scrollingPanel);

		engageBtn.addActionListener(e -> engagePanel(false, ""));
		backBtn.addActionListener(e -> profilePanel());
		profileBtn.addActionListener(e -> profilePanel());
		logoutBtn.addActionListener(e -> controller.handleLogout());

		cp.revalidate();
		cp.repaint();	
	}

	public void updatePanel() {
		cp = BackgroundPanel.create("assets/admin/enthusiast/update_view.png");
		cp.setLayout(null);
		frame.setContentPane(cp);

		JLabel username = GUIUtil.createText(enthusiast.getUsername(),100,199,286,27);
		JLabel id = GUIUtil.createText(String.valueOf(enthusiast.getID()),410,199,92,27);
		JLabel sex = GUIUtil.createText(enthusiast.getSex(),526,199,89,27);
		JLabel firstname = GUIUtil.createText(enthusiast.getFirstName(),102,273,153,27);
		JLabel middlename = GUIUtil.createText(enthusiast.getMiddleName(),276,271,164,27);
		JLabel lastname = GUIUtil.createText(enthusiast.getLastName(),462,273,153,27);
		JLabel birthday = GUIUtil.createText(enthusiast.getDateOfBirth()
				.getDisplayDate(),100,346,245,27);
		JLabel joinedBy = GUIUtil.createText(enthusiast.getJoinDate()
				.getDisplayDate(),370,346,245,27); 
		JButton backBtn = GUIUtil.createIButton(648,440,64,27);
		JButton saveBtn = GUIUtil.createIButton(570,440,64,27);
		JButton editUsername = GUIUtil.createIButton(354,179,36,18);
		JButton editFirstName = GUIUtil.createIButton(223,253,36,18);
		JButton editMiddleName = GUIUtil.createIButton(409,252,36,18);
		JButton editLastName = GUIUtil.createIButton(584,253,36,18);
		JButton editSex = GUIUtil.createIButton(584,179,36,18);
		JButton editBirthday = GUIUtil.createIButton(314,326,36,18);

		cp.add(username);
		cp.add(id);
		cp.add(sex);
		cp.add(firstname);
		cp.add(middlename);
		cp.add(lastname);
		cp.add(birthday);
		cp.add(joinedBy);
		cp.add(backBtn);
		cp.add(saveBtn);
		cp.add(editUsername);
		cp.add(editFirstName);
		cp.add(editMiddleName);
		cp.add(editLastName);
		cp.add(editSex);
		cp.add(editBirthday);

		backBtn.addActionListener(e -> profilePanel());
		saveBtn.addActionListener(e -> controller.handleUpdate(enthusiast));
		editUsername.addActionListener(e -> 
				updateFieldPanel("username"));
		editFirstName.addActionListener(e -> 
				updateFieldPanel("first name"));
		editMiddleName.addActionListener(e -> 
				updateFieldPanel("middle name"));
		editLastName.addActionListener(e -> 
				updateFieldPanel("last name"));
		editSex.addActionListener(e -> 
				updateFieldPanel("sex"));
		editBirthday.addActionListener(e -> 
				updateFieldPanel("birthday"));

		cp.revalidate();
		cp.repaint();	
	}

	public void updateFieldPanel(String type) {
		cp = BackgroundPanel.create("assets/admin/enthusiast/update_field.png");
		cp.setLayout(null);
		frame.setContentPane(cp);

		JButton submitBtn = GUIUtil.createIButton(519,252,98,27);
		JTextField field = GUIUtil.createTextField(100,252,400,27);
		JButton backBtn = GUIUtil.createIButton(648,440,64,27);
		cp.add(submitBtn);
		cp.add(field);
		cp.add(backBtn);

		backBtn.addActionListener(e -> updatePanel());
		submitBtn.addActionListener(e -> {
			String input = field.getText().trim();
			//check for invalid inputs
			switch(type) {
				case "username": case "sex": case "first name": 
				case "last name": case "birthday":
					if(input.isEmpty()) {
						JOptionPane.showMessageDialog(frame, 
							"Required Field! Please input update for " + type + ".");
						return;
					}
			}

			if(type.equals("sex") && 
				!(input.equals("Male") || 
				input.equals("Female") || 
				input.equals("Other"))) {
					JOptionPane.showMessageDialog(frame, 
					"Sex must be Male, Female or Other.");
				return;
			}

			switch(type) {
				case "username": 
					enthusiast.setUsername(input);
					break;
				case "sex": 
					enthusiast.setSex(input); 
					break;
				case "first name": 
					enthusiast.setFirstName(input); 
					break;
				case "middle name":
					if(input.isEmpty())
						enthusiast.setMiddleName(null);
					else
						enthusiast.setMiddleName(input);
					break;
				case "last name":
					enthusiast.setLastName(input);
					break;
				case "birthday":
					DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");	
					try {
						LocalDate date = LocalDate.parse(input, format);
						String[] dateParts = input.split("/");
						int month = Integer.parseInt(dateParts[0]);
						int day = Integer.parseInt(dateParts[1]);
						int year = Integer.parseInt(dateParts[2]);
						enthusiast.setDateOfBirth(new CustomTimestamp(year, month, day));
					} catch(DateTimeParseException dtpe) {
						JOptionPane.showMessageDialog(frame, 
							"Invalid Date. Please input it as MM/DD/YYYY");
						return;
					}
					break;
			}

			updatePanel();
		});
		
		cp.revalidate();
		cp.repaint();
		
	}

	public void passUpdatedEnthusiast(Enthusiast enthusiast) {
		this.enthusiast.update(enthusiast);
		profilePanel();
	}

	public void deletePanel() {
		int choice = JOptionPane.showConfirmDialog(
				frame, 
				"Do you want to proceed?", 
				"Account Deletion", 
				JOptionPane.YES_NO_OPTION
				);	

		if(choice == JOptionPane.YES_OPTION) {
			JOptionPane.showMessageDialog(null, "Successful Deletion! Logging out."); 
			controller.handleDelete(enthusiast);
			controller.handleLogout();
		} else {
			JOptionPane.showMessageDialog(null, "Deletion cancelled.");
		}
	}

	public void show() { frame.setVisible(true); }
	public void hide() { frame.setVisible(false); }
	public void dispose() { if(frame != null) frame.dispose(); }
}