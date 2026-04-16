package view;

import model.*;
import controller.*;
import util.*;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Arrays;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.DateTimeException;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;

public class GUIAdminView {
	private JFrame frame;
	private Container cp;

	private AdminController controller;

	public GUIAdminView() {}	
		
	public void start() {
		frame = GUIUtil.setupGUI(frame, cp, 720, 480);
		dashboardPanel();
	}

	public void setListeners(AdminController controller) {
		this.controller = controller;
	}

	public void dashboardPanel() {
		cp = BackgroundPanel.create("assets/admin/home.png");
		cp.setLayout(null);
		frame.setContentPane(cp);

		JButton enthusiastBtn = GUIUtil.createIButton(259,208,200,27);
		JButton playerBtn = GUIUtil.createIButton(259, 245, 200, 27);
		JButton tournamentBtn = GUIUtil.createIButton(259,313,200,27);
		JButton backBtn = GUIUtil.createIButton(648,440,64,27);
		

		cp.add(enthusiastBtn);
        cp.add(playerBtn);
		cp.add(tournamentBtn);
		cp.add(backBtn);

		enthusiastBtn.addActionListener(e -> controller.handleMenu("enthusiast"));
		playerBtn.addActionListener(e -> controller.handleMenu("player"));
		tournamentBtn.addActionListener((e ->controller.handleMenu("tournament")));
		backBtn.addActionListener(e -> controller.handleMenu("exit"));

		cp.revalidate();
		cp.repaint();
	}

	//ENTHUSIASTS DASHBOARD!!!!!!!

	public void enthusiastDashboardPanel() {
		cp = BackgroundPanel.create("assets/admin/enthusiasts_crud.png");
		cp.setLayout(null);
		frame.setContentPane(cp);

		JButton createBtn = GUIUtil.createIButton(235,198,246,27);
		JButton viewAllBtn = GUIUtil.createIButton(235,235,246,27);
		JButton updateBtn = GUIUtil.createIButton(235,269,246,27);
		JButton deleteBtn = GUIUtil.createIButton(235,305,246,27);
		JButton reportBtn = GUIUtil.createIButton(235,341,246,27);
		JButton backBtn = GUIUtil.createIButton(648,440,64,27);
		cp.add(createBtn);
		cp.add(viewAllBtn);
		cp.add(updateBtn);
		cp.add(deleteBtn);
		cp.add(reportBtn);
		cp.add(backBtn);

		createBtn.addActionListener(e -> controller.handleEnthusiast("create"));
		viewAllBtn.addActionListener(e -> controller.handleEnthusiast("viewAll"));
		updateBtn.addActionListener(e -> controller.handleEnthusiast("update"));
		deleteBtn.addActionListener(e -> controller.handleEnthusiast("delete"));
		reportBtn.addActionListener(e -> controller.handleEnthusiast("report"));
		backBtn.addActionListener(e -> controller.handleEnthusiast("exit"));

		cp.revalidate();
		cp.repaint();

	}

	public void createEnthusiastPanel() {
		cp = BackgroundPanel.create("assets/admin/enthusiast/create.png");
		cp.setLayout(null);
		frame.setContentPane(cp);
		JTextField usernameField = GUIUtil.createTextField(121,194,268,27);
		JTextField lastNameField = GUIUtil.createTextField(121,249,268,27);
		JTextField firstNameField = GUIUtil.createTextField(121,304,268,27);
		JTextField middleNameField = GUIUtil.createTextField(121,359,268,27);
		JTextField yearField = GUIUtil.createTextField(447,194,151,27);
		JTextField monthField = GUIUtil.createTextField(447,248,151,27);
		JTextField dayField = GUIUtil.createTextField(447,305,151,27);
		JButton sexF = GUIUtil.createIButton(439,359,40,27);
		JButton sexM = GUIUtil.createIButton(485,359,40,27);
		JButton sexOther = GUIUtil.createIButton(530,359,71,27);
		JButton submitBtn = GUIUtil.createIButton(575,440,60,27);
		JButton backBtn = GUIUtil.createIButton(648,440,64,27);
		cp.add(usernameField);
		cp.add(lastNameField);
		cp.add(firstNameField);
		cp.add(middleNameField);
		cp.add(yearField);
		cp.add(monthField);
		cp.add(dayField);
		cp.add(sexF);
		cp.add(sexM);
		cp.add(sexOther);
		cp.add(submitBtn);
		cp.add(backBtn);

		final String[] sex = { "none" };
		Border blackBorder = BorderFactory.createLineBorder(new Color(141,53,18), 2);
		Border noBorder = BorderFactory.createLineBorder(Color.BLACK, 0);

		sexF.addActionListener(e -> {
			sex[0] = "Female";
			sexF.setBorder(blackBorder);
			sexM.setBorder(noBorder);
			sexOther.setBorder(noBorder);
		});
		sexM.addActionListener(e -> {
			sex[0] = "Male";
			sexM.setBorder(blackBorder);
			sexF.setBorder(noBorder);
			sexOther.setBorder(noBorder);
		});
		sexOther.addActionListener(e -> {
			sex[0] = "Other";
			sexOther.setBorder(blackBorder);
			sexF.setBorder(noBorder);
			sexM.setBorder(noBorder);
		});
		submitBtn.addActionListener(e -> {
			try {
				String username = usernameField.getText().trim();
				String lastName = lastNameField.getText().trim();
				String firstName = firstNameField.getText().trim();
				String middleName = middleNameField.getText().trim();

				//err handling
				if(username.isEmpty() || lastName.isEmpty() || 
					firstName.isEmpty()) {
					JOptionPane.showMessageDialog(frame, 
						"Required Fields! Fill in all the required fields (middle name is optional).");
   				return; // stop further processing
				}
				if(sex[0].equals("none")) {
					JOptionPane.showMessageDialog(frame, 
						"Required Fields! No sex chosen.");
   				return; // stop further processing
				}
				int year = Integer.parseInt(yearField.getText().trim());
      		int month = Integer.parseInt(monthField.getText().trim());
   			int day = Integer.parseInt(dayField.getText().trim());

				//for validating date of birth
				LocalDate dateOfBirth = LocalDate.of(year, month, day);

				Enthusiast enthusiast = new Enthusiast(
					username, 
					lastName, firstName, middleName, 
					sex[0], 
					new CustomTimestamp(year, month, day)
				);

				if(middleName.isEmpty()) {
					enthusiast.setMiddleName(null);
				}

				controller.addEnthusiast(enthusiast);

				usernameField.setText("");
				lastNameField.setText("");
				firstNameField.setText("");
				middleNameField.setText("");
				yearField.setText("");
				monthField.setText("");
				dayField.setText("");
				sexOther.setBorder(noBorder);
				sexF.setBorder(noBorder);
				sexM.setBorder(noBorder);
			} catch (NumberFormatException nfe) {
				JOptionPane.showMessageDialog(frame,
					"Invalid Date. Please enter numerical values.");
			} catch (DateTimeException dte) {
				JOptionPane.showMessageDialog(frame,
					"Invalid Date. Please check the day, month and or year.");
			}
		});
		backBtn.addActionListener(e -> enthusiastDashboardPanel());

		cp.revalidate();
		cp.repaint();
	}

	public void viewAllEnthusiastPanel(LinkedHashMap<String, String> information) {
		cp = BackgroundPanel.create("assets/admin/enthusiast/view_all_bg.png");
		cp.setLayout(null); 
		frame.setContentPane(cp);

		JButton inactiveBtn = GUIUtil.createIButton(647,107,64,20);
		JButton activeBtn = GUIUtil.createIButton(647,133,64,20);
		JButton allBtn = GUIUtil.createIButton(647,159,64,20);
		JLabel overlayBg = new JLabel(new ImageIcon("assets/admin/enthusiast/view_all.png"));
		overlayBg.setBounds(0,0,720,480);	
		JButton backBtn = GUIUtil.createIButton(648,440,64,27);
		
		JPanel scrollingPanel = new JPanel(null);
		int index = 0;
		int gap = 60;
		int y = 121;
		for(Map.Entry<String, String> entry : information.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();	

			String[] keys = key.split("-");
			int enthusiastID = Integer.parseInt(keys[0]);
			String status = keys[1];

			JPanel user = new JPanel(null);
			user.setOpaque(false);

			String imagePath = "assets/admin/enthusiast/";
			if(status.equals("active")) {
				imagePath += "banner_active";
			} else {
				imagePath += "banner_archived";
			}
			imagePath += ".png";

			JLabel banner = new JLabel(new ImageIcon(imagePath));
			banner.setBounds(0,0,588,54);
			banner.setOpaque(false);

			JButton bannerBtn = GUIUtil.createIButton(0,0,588,54);
			JLabel userInfo = GUIUtil.createText(value,62,17,443,20);

			bannerBtn.addActionListener(e -> {
				controller.showEnthusiast(enthusiastID, status);
				System.out.println("enthusiast is " + status);
			});

			user.add(userInfo);
			user.add(banner);
			user.add(bannerBtn);

			user.setBounds(41,y+index*gap,589,55);
			scrollingPanel.add(user);
			index++;
		}

		cp.add(overlayBg);
		int totalHeight = y + information.size() * gap;
		scrollingPanel.setBounds(0,0,720,totalHeight);
		scrollingPanel.setOpaque(false);
		cp.add(scrollingPanel);

		cp.addMouseWheelListener(new MouseWheelListener() {
			int offset = 0;

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) 
			{
				int rotation = e.getWheelRotation(); //1 down -1 up
				offset -= rotation * 20; //so 20 is the offset like when scrolled ykykyk

				int maxOffset = 0;
				int minOffset = Math.min(0,cp.getHeight()-20 - totalHeight);
				offset = Math.max(minOffset, Math.min(maxOffset, offset));

				scrollingPanel.setLocation(0,offset);
				cp.repaint();
			}
		});

		inactiveBtn.addActionListener(e -> controller.handleEnthusiast("viewInactive"));
		activeBtn.addActionListener(e -> controller.handleEnthusiast("viewActive"));
		allBtn.addActionListener(e -> controller.handleEnthusiast("viewAll"));
		backBtn.addActionListener(e -> enthusiastDashboardPanel());

		cp.add(backBtn);
		cp.add(inactiveBtn);
		cp.add(activeBtn);
		cp.add(allBtn);
		cp.revalidate();
		cp.repaint();
	}

	public void viewEnthusiastPanel(Enthusiast enthusiast, String status, String backPath) {
		String imagePath = "assets/admin/enthusiast/" + status + ".png";
		cp = BackgroundPanel.create(imagePath);
		cp.setLayout(null);
		frame.setContentPane(cp);

		JButton reactivateBtn = new JButton();
		if(status.equals("inactive")) {
			reactivateBtn = GUIUtil.createIButton(490,440,145,27);
			cp.add(reactivateBtn);
		}
		JLabel username = GUIUtil.createText(enthusiast.getUsername(),100,199,286,27);
		JLabel id = GUIUtil.createText(String.valueOf(enthusiast.getID()),410,199,92,27);
		JLabel sex = GUIUtil.createText(enthusiast.getSex(),526,199,89,27);
		JLabel fullname = GUIUtil.createText(enthusiast.getFullName(),102,273,515,27);
		JLabel birthday = GUIUtil.createText(enthusiast.getDateOfBirth()
				.getDisplayDate(),100,346,245,27);
		JLabel joinedBy = GUIUtil.createText(enthusiast.getJoinDate()
				.getDisplayDate(),370,346,245,27); 
		JButton backBtn = GUIUtil.createIButton(648,440,64,27);
		cp.add(username);
		cp.add(id);
		cp.add(sex);
		cp.add(fullname);
		cp.add(birthday);
		cp.add(joinedBy);
		cp.add(backBtn);

		reactivateBtn.addActionListener(e -> {
			controller.toggleEnthusiast(enthusiast);
			controller.handleEnthusiast(backPath);
		});
		backBtn.addActionListener(e -> controller.handleEnthusiast(backPath));

		cp.revalidate();
		cp.repaint();
	}

	public void searchUpdateEnthusiastPanel() {
		cp = BackgroundPanel.create("assets/admin/enthusiast/search.png");
		cp.setLayout(null);
		frame.setContentPane(cp);

		JButton submitBtn = GUIUtil.createIButton(528,260,98,27);
		JTextField field = GUIUtil.createTextField(142,261,367,27);
		JButton backBtn = GUIUtil.createIButton(648,440,64,27);
		cp.add(submitBtn);
		cp.add(field);
		cp.add(backBtn);

		backBtn.addActionListener(e -> enthusiastDashboardPanel());
		submitBtn.addActionListener(e -> {
			String input = field.getText().trim();
			field.setText("");

			try {
				int id = Integer.parseInt(input);
				if(!controller.updateEnthusiastView(id)) {
					JOptionPane.showMessageDialog(frame,
						"ID does not exist.");
				}
			} catch(NumberFormatException nfe) {
				JOptionPane.showMessageDialog(frame,
					"Invalid ID number.");
			}
		});
		
		cp.revalidate();
		cp.repaint();
	}

	public void updateEnthusiastPanel(Enthusiast enthusiast) {
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

		backBtn.addActionListener(e -> enthusiastDashboardPanel());
		saveBtn.addActionListener(e -> controller.updateEnthusiast(enthusiast));
		editUsername.addActionListener(e -> 
				updateFieldEnthusiastPanel(enthusiast,"username"));
		editFirstName.addActionListener(e -> 
				updateFieldEnthusiastPanel(enthusiast,"first name"));
		editMiddleName.addActionListener(e -> 
				updateFieldEnthusiastPanel(enthusiast,"middle name"));
		editLastName.addActionListener(e -> 
				updateFieldEnthusiastPanel(enthusiast,"last name"));
		editSex.addActionListener(e -> 
				updateFieldEnthusiastPanel(enthusiast,"sex"));
		editBirthday.addActionListener(e -> 
				updateFieldEnthusiastPanel(enthusiast,"birthday"));

		cp.revalidate();
		cp.repaint();	

	}

	public void updateFieldEnthusiastPanel(Enthusiast enthusiast, String type) {
		cp = BackgroundPanel.create("assets/admin/enthusiast/update_field.png");
		cp.setLayout(null);
		frame.setContentPane(cp);

		JButton submitBtn = GUIUtil.createIButton(519,252,98,27);
		JTextField field = GUIUtil.createTextField(100,252,400,27);
		JButton backBtn = GUIUtil.createIButton(648,440,64,27);
		cp.add(submitBtn);
		cp.add(field);
		cp.add(backBtn);

		backBtn.addActionListener(e -> updateEnthusiastPanel(enthusiast));
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

			updateEnthusiastPanel(enthusiast);
		});
		
		cp.revalidate();
		cp.repaint();
		
	}

	public void searchDeleteEnthusiastPanel() {
		cp = BackgroundPanel.create("assets/admin/enthusiast/search.png");
		cp.setLayout(null);
		frame.setContentPane(cp);

		JButton submitBtn = GUIUtil.createIButton(528,260,98,27);
		JTextField field = GUIUtil.createTextField(142,261,367,27);
		JButton backBtn = GUIUtil.createIButton(648,440,64,27);
		cp.add(submitBtn);
		cp.add(field);
		cp.add(backBtn);

		backBtn.addActionListener(e -> enthusiastDashboardPanel());
		submitBtn.addActionListener(e -> {
			String input = field.getText().trim();
			field.setText("");

			try {
				int id = Integer.parseInt(input);
				if(!controller.deleteEnthusiastView(id)) {
					JOptionPane.showMessageDialog(frame,
						"ID does not exist.");
				}
			} catch(NumberFormatException nfe) {
				JOptionPane.showMessageDialog(frame,
					"Invalid ID number.");
				}
		});
		
		cp.revalidate();
		cp.repaint();
	}

	public void deleteEnthusiastPanel(Enthusiast enthusiast) {
		cp = BackgroundPanel.create("assets/admin/enthusiast/delete.png");
		cp.setLayout(null);
		frame.setContentPane(cp);

		JLabel username = GUIUtil.createText(enthusiast.getUsername(),100,199,286,27);
		JLabel id = GUIUtil.createText(String.valueOf(enthusiast.getID()),410,199,92,27);
		JLabel sex = GUIUtil.createText(enthusiast.getSex(),526,199,89,27);
		JLabel fullname = GUIUtil.createText(enthusiast.getFullName(),102,273,515,27);
		JLabel birthday = GUIUtil.createText(enthusiast.getDateOfBirth()
				.getDisplayDate(),100,346,245,27);
		JLabel joinedBy = GUIUtil.createText(enthusiast.getJoinDate()
				.getDisplayDate(),370,346,245,27); 
		JButton backBtn = GUIUtil.createIButton(648,440,64,27);
		JButton delBtn = GUIUtil.createIButton(521,440,113,27);
		cp.add(username);
		cp.add(id);
		cp.add(sex);
		cp.add(fullname);
		cp.add(birthday);
		cp.add(joinedBy);
		cp.add(backBtn);
		cp.add(delBtn);

		backBtn.addActionListener(e -> enthusiastDashboardPanel());
		delBtn.addActionListener(e -> {
			controller.toggleEnthusiast(enthusiast);
			enthusiastDashboardPanel();
		});

		cp.revalidate();
		cp.repaint();	
	}

	public void viewReportEnthusiastPanel() {
		cp = BackgroundPanel.create("assets/admin/enthusiast/report.png");
		cp.setLayout(null);
		frame.setContentPane(cp);
		
		JButton generateBtn = GUIUtil.createIButton(259,249,200,27);
		JButton generatePdfBtn = GUIUtil.createIButton(259,286,200,27);
		JButton backBtn = GUIUtil.createIButton(648,440,64,27);
		
		cp.add(generateBtn);
		cp.add(generatePdfBtn);
		cp.add(backBtn);

		generateBtn.addActionListener(e -> generateReport());
		backBtn.addActionListener(e -> enthusiastDashboardPanel());

		cp.revalidate();
		cp.repaint();
	}

	public void generateReport() {
		JFrame reportsFrame = new JFrame();
		Container reportsCp = new Container();
		reportsFrame = GUIUtil.setupGUI(reportsFrame, reportsCp, 1920,1080, "enthusiast reports", true);

		reportsCp = BackgroundPanel.create("assets/admin/enthusiast/report_bg.png");
		reportsCp.setLayout(null);
		reportsFrame.setContentPane(reportsCp);

		LocalDate today = LocalDate.now();
		String month = today.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
		String year = String.valueOf(today.getYear());
		String date = month + " " + year;

		JLabel creationDate = GUIUtil.createText(date,1350,219,443,21);

		creationDate.setHorizontalAlignment(SwingConstants.RIGHT);
		creationDate.setFont(creationDate.getFont().deriveFont(20f));
		creationDate.setForeground(new Color(213,98,51));

		/*
		String[][] data = {
			 {"a1", "b1", "c1", "a2", "b2", "c2", "a3", "b3", "c3"},
			 {"a4", "b4", "c4", "a5", "b5", "c5", "a6", "b6", "c6"},
			 {"a7", "b7", "c7", "a8", "b8", "c8", "a9", "b9", "c9"}
		};
		*/
		String[][] data = controller.generateEnthusiastReport();
		String[] columns = new String[data[0].length];
		Arrays.fill(columns, "");
	

		JTable table = new JTable(data, columns);
		table.setTableHeader(null);
		table.setOpaque(false);
		((DefaultTableCellRenderer)table.getDefaultRenderer(Object.class)).setOpaque(false);
		table.setShowGrid(false);

		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
		renderer.setOpaque(false);
		renderer.setHorizontalAlignment(SwingConstants.LEFT);


		for (int i = 0; i < table.getColumnCount(); i++) {
			 table.getColumnModel().getColumn(i).setCellRenderer(renderer);
		}


		JScrollPane scroll = new JScrollPane(table);
		scroll.setBounds(129,309,1663,690);
		scroll.setOpaque(false);
		scroll.getViewport().setOpaque(false);
		scroll.getVerticalScrollBar().setOpaque(false);
		scroll.getHorizontalScrollBar().setOpaque(false);
		scroll.setBorder(BorderFactory.createEmptyBorder());

		scroll.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
		scroll.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));


		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getColumnModel().getColumn(0).setPreferredWidth(97);
		table.getColumnModel().getColumn(1).setPreferredWidth(367);
		table.getColumnModel().getColumn(2).setPreferredWidth(120);
		table.getColumnModel().getColumn(3).setPreferredWidth(170);
		table.getColumnModel().getColumn(4).setPreferredWidth(170);
		table.getColumnModel().getColumn(5).setPreferredWidth(170);
		table.getColumnModel().getColumn(6).setPreferredWidth(190);
		table.getColumnModel().getColumn(7).setPreferredWidth(170);
		table.getColumnModel().getColumn(8).setPreferredWidth(199);
		table.setRowHeight(40);

		reportsCp.add(scroll);
		reportsCp.add(creationDate);
		reportsCp.revalidate();
		reportsCp.repaint();
	}
	
	//ENTHUSIASTS DASHBOARD!!!!!!!

	//PLAYERS DASHBOARD!!!!!!!
    /**
	 * CREATE PLAYER PANEL
	 * RETRIEVE PLAYER PANEL
	 * UPDATE PLAYER PANEL
	 * DELETE PLAYER PANEL
	 * VIEW ALL PLAYERS PANEL
	 * TRANSACTION
	 * REPORT
	 */
	
	public void playerDashboardPanel(){
		//to be implemented
		cp = BackgroundPanel.create("assets/admin/players_crud.png");
		cp.setLayout(null);
		frame.setContentPane(cp);
		//Buttons
		JButton backBtn = GUIUtil.createIButton(648,440,64,27);
		JButton createBtn = GUIUtil.createIButton(235,198,246,27);
		JButton viewAllBtn = GUIUtil.createIButton(235,235,246,27);
		JButton updateBtn = GUIUtil.createIButton(235,269,246,27);
		JButton deleteBtn = GUIUtil.createIButton(235,305,246,27);
		JButton reportBtn = GUIUtil.createIButton(235,341,246,27);

        cp.add(backBtn);
		cp.add(createBtn);
		cp.add(viewAllBtn);
		cp.add(updateBtn);
		cp.add(deleteBtn);
		cp.add(reportBtn);
		backBtn.addActionListener(e -> controller.handleMenu("exit"));
		createBtn.addActionListener(e -> controller.handlePlayer("create"));
		viewAllBtn.addActionListener(e -> controller.handlePlayer("viewAll"));
		updateBtn.addActionListener(e -> controller.handlePlayer("update"));
		deleteBtn.addActionListener(e -> controller.handlePlayer("delete"));
		reportBtn.addActionListener(e -> controller.handlePlayer("report"));

		cp.revalidate();
		cp.repaint();
	}
    
	public void createPlayerPanel(){

        cp = BackgroundPanel.create("assets/admin/player/pCreate.png");
		cp.setLayout(null);
		frame.setContentPane(cp);
		JTextField lastNameField = GUIUtil.createTextField(114,196,261,27);
		JTextField firstNameField = GUIUtil.createTextField(114,250,261,27);
		JTextField middleNameField = GUIUtil.createTextField(114,305,261,27);
		JTextField height = GUIUtil.createTextField(115,362,161,27);
		JTextField weight = GUIUtil.createTextField(115,416,161,27);
		JTextField yearField = GUIUtil.createTextField(445,198,161,27);
		JTextField monthField = GUIUtil.createTextField(445,253,161,27);
		JTextField dayField = GUIUtil.createTextField(445,309,161,27);
		JButton sexF = GUIUtil.createIButton(442,359,40,27);
		JButton sexM = GUIUtil.createIButton(485,359,40,27);
		JButton sexOther = GUIUtil.createIButton(530,359,71,27);
		JTextField rStatusField = GUIUtil.createTextField(445,410,139,27);
		JButton submitBtn = GUIUtil.createIButton(575,440,64,27);
		JButton backBtn = GUIUtil.createIButton(648,440,64,27);

		cp.add(lastNameField);
		cp.add(firstNameField);
		cp.add(middleNameField);
		cp.add(yearField);
		cp.add(monthField);
		cp.add(dayField);
		cp.add(sexF);
		cp.add(sexM);
		cp.add(sexOther);
		cp.add(height);
		cp.add(weight);
		cp.add(rStatusField);
		cp.add(submitBtn);
		cp.add(backBtn);
        
        final String[] sex = { "none" };
		Border blackBorder = BorderFactory.createLineBorder(new Color(141,53,18), 2);
		Border noBorder = BorderFactory.createLineBorder(Color.BLACK, 0);

		sexF.addActionListener(e -> {
			sex[0] = "Female";
			sexF.setBorder(blackBorder);
			sexM.setBorder(noBorder);
			sexOther.setBorder(noBorder);
		});
		sexM.addActionListener(e -> {
			sex[0] = "Male";
			sexM.setBorder(blackBorder);
			sexF.setBorder(noBorder);
			sexOther.setBorder(noBorder);
		});
		sexOther.addActionListener(e -> {
			sex[0] = "Other";
			sexOther.setBorder(blackBorder);
			sexF.setBorder(noBorder);
			sexM.setBorder(noBorder);
		});

		submitBtn.addActionListener(e -> {
			try {

				String lastName = lastNameField.getText().trim();
				String firstName = firstNameField.getText().trim();
				String middleName = middleNameField.getText().trim();
				
				double heightVal = Double.parseDouble(height.getText().trim());
				double weightVal = Double.parseDouble(weight.getText().trim());
				boolean	rStatus = Boolean.parseBoolean(rStatusField.getText().trim());

				if(lastName.isEmpty() || 
					firstName.isEmpty()) {
					JOptionPane.showMessageDialog(frame, 
						"Required Fields! Fill in all the required fields (middle name is optional).");
   				return; // stop further processing
				}

			    int year = Integer.parseInt(yearField.getText().trim());
      		    int month = Integer.parseInt(monthField.getText().trim());
   			    int day = Integer.parseInt(dayField.getText().trim());

				//for validating date of birth
                String DoB = String.format("%04d-%02d-%02d", year, month, day);
				Player player = new Player( controller.generatePlayerID(),
					lastName, firstName, middleName, 
				    DoB, sex[0], heightVal , weightVal,
				    rStatus 
			);

				if(middleName.isEmpty()) {
					player.setMiddleName(null);
				}

				controller.addPlayer(player);

				lastNameField.setText("");
				firstNameField.setText("");
				middleNameField.setText("");
				yearField.setText("");
				monthField.setText("");
				dayField.setText("");
				height.setText("");
				weight.setText("");
				rStatusField.setText("");
			} catch (NumberFormatException nfe) {
				JOptionPane.showMessageDialog(frame,
					"Invalid Date. Please enter numerical values.");
			} catch (DateTimeException dte) {
				JOptionPane.showMessageDialog(frame,
					"Invalid Date. Please check the day, month and or year.");
			}
		});
		backBtn.addActionListener(e -> playerDashboardPanel());

		cp.revalidate();
		cp.repaint();
	}
    
	public void searchPlayerPanel(Player player){
        cp = BackgroundPanel.create("assets/admin/player/pSearch.png");
		cp.setLayout(null);
		frame.setContentPane(cp);

		JButton backBtn = GUIUtil.createIButton(648,440,64,27);
		JTextField searchField = GUIUtil.createTextField(142,261,367,27);
		JButton submitBtn = GUIUtil.createIButton(528,260,98,27);

		cp.add(backBtn);
		cp.add(searchField);
		cp.add(submitBtn);
	}

	public void updatePlayerPanel(Player player){
        cp = BackgroundPanel.create("assets/admin/player/pUpdate.png");
        cp.setLayout(null);
		frame.setContentPane(cp);
        JLabel height = GUIUtil.createText(String.valueOf(player.getHeight()),100,199,286,27);
		JLabel weight = GUIUtil.createText(String.valueOf(player.getWeight()),215,199,286,27);
		JLabel id = GUIUtil.createText(String.valueOf(player.getPlayerID()),410,199,92,27);
		JLabel sex = GUIUtil.createText(player.getSex(),526,199,89,27);
		JLabel firstname = GUIUtil.createText(player.getFirstName(),102,273,153,27);
		JLabel middlename = GUIUtil.createText(player.getMiddleName(),276,271,164,27);
		JLabel lastname = GUIUtil.createText(player.getLastName(),462,273,153,27);
		JLabel birthday = GUIUtil.createText(player.getDateOfBirth(),100,346,245,27);

		JButton backBtn = GUIUtil.createIButton(648,440,64,27);
		JButton saveBtn = GUIUtil.createIButton(570,440,64,27);
		JButton editHeight = GUIUtil.createIButton(171,179,36,18);
		JButton editWeight = GUIUtil.createIButton(286,179,36,18);
		JButton editFirstName = GUIUtil.createIButton(223,253,36,18);
		JButton editMiddleName = GUIUtil.createIButton(409,252,36,18);
		JButton editLastName = GUIUtil.createIButton(584,253,36,18);
		JButton editSex = GUIUtil.createIButton(586,182,36,18);
		JButton editBirthday = GUIUtil.createIButton(314,326,36,18);

		cp.add(height);
		cp.add(weight);
		cp.add(id);
		cp.add(sex);
		cp.add(firstname);
		cp.add(middlename);
		cp.add(lastname);
		cp.add(birthday);
		
		cp.add(backBtn);
		cp.add(saveBtn);
		
		cp.add(editFirstName);
		cp.add(editMiddleName);
		cp.add(editLastName);
		cp.add(editSex);
		cp.add(editBirthday);

		backBtn.addActionListener(e -> playerDashboardPanel());
		saveBtn.addActionListener(e -> controller.updatePlayer(player));
		editHeight.addActionListener(e -> 
				updateFieldPlayerPanel(player,"height"));
		editWeight.addActionListener(e -> 
				updateFieldPlayerPanel(player,"Weight"));
		editFirstName.addActionListener(e -> 
				updateFieldPlayerPanel(player,"first name"));
		editMiddleName.addActionListener(e -> 
				updateFieldPlayerPanel(player,"middle name"));
		editLastName.addActionListener(e -> 
				updateFieldPlayerPanel(player,"last name"));
		editSex.addActionListener(e -> 
				updateFieldPlayerPanel(player,"sex"));
		editBirthday.addActionListener(e -> 
				updateFieldPlayerPanel(player,"birthday"));

		cp.revalidate();
		cp.repaint();	


		
	}

	public void updateFieldPlayerPanel(Player player, String type){
    cp = BackgroundPanel.create("assets/admin/player/pUpdate_field.png");
		cp.setLayout(null);
		frame.setContentPane(cp);

		JButton submitBtn = GUIUtil.createIButton(519,252,98,27);
		JTextField field = GUIUtil.createTextField(100,252,400,27);
		JButton backBtn = GUIUtil.createIButton(648,440,64,27);
		cp.add(submitBtn);
		cp.add(field);
		cp.add(backBtn);

		backBtn.addActionListener(e -> updatePlayerPanel(player));
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
				case "height": 
					player.setHeight(Integer.parseInt(input));
					break;
				case "sex": 
					player.setSex(input); 
					break;
				case "first name": 
					player.setFirstName(input); 
					break;
				case "middle name":
					if(input.isEmpty())
						player.setMiddleName(null);
					else
						player.setMiddleName(input);
					break;
				case "last name":
					player.setLastName(input);
					break;
				case "birthday":
					DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");	
					try {
						LocalDate date = LocalDate.parse(input, format);
						String[] dateParts = input.split("/");
						int month = Integer.parseInt(dateParts[0]);
						int day = Integer.parseInt(dateParts[1]);
						int year = Integer.parseInt(dateParts[2]);
						player.setBirthday(new CustomTimestamp(year, month, day));
					} catch(DateTimeParseException dtpe) {
						JOptionPane.showMessageDialog(frame, 
							"Invalid Date. Please input it as MM/DD/YYYY");
						return;
					}
					break;
			}

			updatePlayerPanel(player);
		});
		
		cp.revalidate();
		cp.repaint();
	}

	public void deletePlayerPanel(Player player){
    cp = BackgroundPanel.create("assets/admin/player/pDelete.png");
	cp.setLayout(null);
	frame.setContentPane(cp);

		JLabel id = GUIUtil.createText(String.valueOf(player.getPlayerID()),410,199,92,27);
		JLabel sex = GUIUtil.createText(player.getSex(),526,199,89,27);
		JLabel fullname = GUIUtil.createText(player.getFullName(),102,273,515,27);
		JLabel birthday = GUIUtil.createText(player.getDateOfBirth(), 90, 338, 255, 27);

		JButton backBtn = GUIUtil.createIButton(648,440,64,27);
		JButton delBtn = GUIUtil.createIButton(521,440,113,27);
		
		cp.add(id);
		cp.add(sex);
		cp.add(fullname);
		cp.add(birthday);
		
		cp.add(backBtn);
		cp.add(delBtn);

		backBtn.addActionListener(e -> playerDashboardPanel());
		delBtn.addActionListener(e -> {
			controller.togglePlayer(player);
			playerDashboardPanel();
		});

		cp.revalidate();
		cp.repaint();
	}

	public void viewAllPlayerPanel(ArrayList<Player> players){
        String imagePath = "assets/admin/player/" + "pView_all.png";
		cp = BackgroundPanel.create(imagePath);
		cp.setLayout(null);
		frame.setContentPane(cp);
        JButton backBtn = GUIUtil.createIButton(648,440,64,27);
        // Back button action - define what happens when clicked
        backBtn.addActionListener(e -> playerDashboardPanel());  

		// Create a JList to display players
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Player player : players) {
        listModel.addElement(player.getPlayerID() + " - " + player.getFullName());
        }
        JList<String> playerJList = new JList<>(listModel);

    // Position and add the JList with a scroll pane
    JScrollPane scrollPane = new JScrollPane(playerJList);
    scrollPane.setBounds(50, 60, 620, 350); // Adjust position/size as needed
    cp.add(scrollPane);
		cp.add(backBtn);
        
		cp.revalidate();
		cp.repaint();
	}

	public void viewPlayerPanel(Player player){
    String imagePath = "assets/admin/player/pView.png";
		cp = BackgroundPanel.create(imagePath);
		cp.setLayout(null);
		frame.setContentPane(cp);

		
		JLabel height = GUIUtil.createText(String.valueOf(player.getHeight()),100,199,286,27);
		JLabel weight = GUIUtil.createText(String.valueOf(player.getWeight()),220,199,286,27);
		JLabel id = GUIUtil.createText(String.valueOf(player.getPlayerID()),410,199,92,27);
		JLabel sex = GUIUtil.createText(player.getSex(),526,199,89,27);
		JLabel fullname = GUIUtil.createText(player.getFullName(),102,273,515,27);
		JLabel birthday = GUIUtil.createText(player.getDateOfBirth(), 100, 355, 270, 27);
		JButton backBtn = GUIUtil.createIButton(648,440,64,27);
		cp.add(height);
		cp.add(weight);
		cp.add(id);
		cp.add(sex);
		cp.add(fullname);
		cp.add(birthday);
		
		cp.add(backBtn);

		backBtn.addActionListener(e -> playerDashboardPanel());

		cp.revalidate();
		cp.repaint();
	}

	public void viewReportPlayerPanel(){
        cp = BackgroundPanel.create("assets/admin/player/pReport.png");
        cp.setLayout(null);
        frame.setContentPane(cp);

        JButton generateBtn = GUIUtil.createIButton(259, 249, 200, 27);
        JButton backBtn = GUIUtil.createIButton(648, 440, 64, 27);

        cp.add(generateBtn);
        cp.add(backBtn);

        backBtn.addActionListener(e -> playerDashboardPanel());

        cp.revalidate();
        cp.repaint();
	}

	public void searchUpdatePlayerPanel(){

		cp = BackgroundPanel.create("assets/admin/player/pSearch.png");
		cp.setLayout(null);
		frame.setContentPane(cp);

		JButton submitBtn = GUIUtil.createIButton(528,260,98,27);
		JTextField field = GUIUtil.createTextField(142,261,367,27);
		JButton backBtn = GUIUtil.createIButton(648,440,64,27);
		cp.add(submitBtn);
		cp.add(field);
		cp.add(backBtn);

		backBtn.addActionListener(e -> playerDashboardPanel());
		submitBtn.addActionListener(e -> {
			String input = field.getText().trim();
			field.setText("");

			try {
				int id = Integer.parseInt(input);
				if(!controller.updatePlayerView(id)) {
					JOptionPane.showMessageDialog(frame,
						"ID does not exist.");
				}
			} catch(NumberFormatException nfe) {
				JOptionPane.showMessageDialog(frame,
					"Invalid ID number.");
			}
		});
		
		cp.revalidate();
		cp.repaint();
	}

	public void searchDeletePlayerPanel(){
		cp = BackgroundPanel.create("assets/admin/player/pSearch.png");
		cp.setLayout(null);
		frame.setContentPane(cp);

		JButton submitBtn = GUIUtil.createIButton(528,260,98,27);
		JTextField field = GUIUtil.createTextField(142,261,367,27);
		JButton backBtn = GUIUtil.createIButton(648,440,64,27);
		cp.add(submitBtn);
		cp.add(field);
		cp.add(backBtn);

		backBtn.addActionListener(e -> playerDashboardPanel());
		submitBtn.addActionListener(e -> {
			String input = field.getText().trim();
			field.setText("");

			try {
				int id = Integer.parseInt(input);
				if(!controller.deletePlayerView(id)) {
					JOptionPane.showMessageDialog(frame,
						"ID does not exist.");
				}
			} catch(NumberFormatException nfe) {
				JOptionPane.showMessageDialog(frame,
					"Invalid ID number.");
				}
		});
		
		cp.revalidate();
		cp.repaint();
	}

	//TOURNAMENT DASHBOARD!!!!!!!

    // tournament dashboard
    public void tournamentDashboardPanel() {
        cp = GUIUtil.createColorPanel("#CD5C33");
        cp.setLayout(null);
        frame.setContentPane(cp);

        // Coordinates aligned with your previous 'Enthusiast' style menu
        JButton createBtn = GUIUtil.createIButton(235, 198, 246, 27);
        JButton viewAllBtn = GUIUtil.createIButton(235, 235, 246, 27);
        JButton updateBtn = GUIUtil.createIButton(235, 269, 246, 27);
        JButton deleteBtn = GUIUtil.createIButton(235, 305, 246, 27);
        JButton reportBtn = GUIUtil.createIButton(235, 341, 246, 27);
        JButton backBtn = GUIUtil.createIButton(648, 440, 64, 27);

        cp.add(createBtn);
        cp.add(viewAllBtn);
        cp.add(updateBtn);
        cp.add(deleteBtn);
        cp.add(reportBtn);
        cp.add(backBtn);

        JLabel title = GUIUtil.createText("TOURNAMENT", 180, 50, 500, 50);
        cp.add(title);
        title.setFont(new Font("Arial", Font.PLAIN, 50));


        cp.add(GUIUtil.createTextLabel("ADD", 235, 198, 246, 27));
        cp.add(GUIUtil.createTextLabel("VIEW", 235, 235, 246, 27));
        cp.add(GUIUtil.createTextLabel("UPDATE", 235, 269, 246, 27));
        cp.add(GUIUtil.createTextLabel("DELETE", 235, 305, 246, 27));
        cp.add(GUIUtil.createTextLabel("GENERATE REPORT", 235, 341, 246, 27));
        cp.add(GUIUtil.createTextLabel("BACK", 648, 440, 64, 27));


        createBtn.addActionListener(e -> controller.handleTournament("create"));
        viewAllBtn.addActionListener(e -> controller.handleTournament("viewAll"));
        updateBtn.addActionListener(e -> controller.handleTournament("update"));
        deleteBtn.addActionListener(e -> controller.handleTournament("delete"));
        reportBtn.addActionListener(e -> controller.handleTournament("report"));
        backBtn.addActionListener(e -> controller.handleTournament("exit"));

        cp.revalidate();
        cp.repaint();
    }

    // create tournament
    public void createTournamentPanel() {
        cp = GUIUtil.createColorPanel("#CD5C33");
        cp.setLayout(null);
        frame.setContentPane(cp);

        JTextField nameField = GUIUtil.createTextField(250, 80, 300, 27);
        JTextField seasonField = GUIUtil.createTextField(250, 120, 300, 27);
        JTextField typeField = GUIUtil.createTextField(250, 160, 300, 27);
        // start date
        JTextField startYearField = GUIUtil.createTextField(250, 200, 90, 27);
        JTextField startMonthField = GUIUtil.createTextField(250, 240, 90, 27);
        JTextField startDayField = GUIUtil.createTextField(250, 280, 90, 27);
        // end date
        JTextField endYearField = GUIUtil.createTextField(250, 320, 90, 27);
        JTextField endMonthField = GUIUtil.createTextField(250, 360, 90, 27);
        JTextField endDayField = GUIUtil.createTextField(250, 400, 90, 27);

        JButton submitBtn = GUIUtil.createIButton(250, 440, 150, 40);
        JButton backBtn = GUIUtil.createIButton(648, 440, 64, 27);

        cp.add(GUIUtil.createTextLabel("Tournament Name:", 10, 80, 300, 27));
        cp.add(GUIUtil.createTextLabel("Season Year:", 35, 120, 300, 27));
        cp.add(GUIUtil.createTextLabel("Tournament Type:", 15, 160, 300, 27));
        cp.add(GUIUtil.createTextLabel("Start Date (year):", 10, 200, 300, 27));
        cp.add(GUIUtil.createTextLabel("Start Date (month):", 10, 240, 300, 27));
        cp.add(GUIUtil.createTextLabel("Start Date (day):", 10, 280, 300, 27));
        cp.add(GUIUtil.createTextLabel("End Date (year):", 10, 320, 300, 27));
        cp.add(GUIUtil.createTextLabel("End Date (month):", 10, 360, 300, 27));
        cp.add(GUIUtil.createTextLabel("End Date (day):", 10, 400, 300, 27));
        cp.add(GUIUtil.createTextLabel("Submit", 250, 440, 150, 40));
        cp.add(GUIUtil.createTextLabel("Back", 648, 440, 64, 27));

        cp.add(nameField);
        cp.add(seasonField);
        cp.add(typeField);
        cp.add(startYearField);
        cp.add(startMonthField);
        cp.add(startDayField);
        cp.add(endYearField);
        cp.add(endMonthField);
        cp.add(endDayField);
        cp.add(submitBtn);
        cp.add(backBtn);

        submitBtn.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                String type = typeField.getText().trim();

                if (name.isEmpty() || type.isEmpty() ||
                        startYearField.getText().trim().isEmpty() ||
                        startMonthField.getText().trim().isEmpty() ||
                        startDayField.getText().trim().isEmpty() ||
                        endYearField.getText().trim().isEmpty() ||
                        endMonthField.getText().trim().isEmpty() ||
                        endDayField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "All fields are required!");
                    return;
                }

                int seasonYear = Integer.parseInt(seasonField.getText().trim());
                int startYear = Integer.parseInt(startYearField.getText().trim());
                int startMonth = Integer.parseInt(startMonthField.getText().trim());
                int startDay = Integer.parseInt(startDayField.getText().trim());

                int endYear = Integer.parseInt(endYearField.getText().trim());
                int endMonth = Integer.parseInt(endMonthField.getText().trim());
                int endDay = Integer.parseInt(endDayField.getText().trim());


                // Validate parts length
                CustomTimestamp startDate = new CustomTimestamp(startYear, startMonth, startDay);
                CustomTimestamp endDate = new CustomTimestamp(endYear, endMonth, endDay);

                Tournament tournament = new Tournament(name, seasonYear, type, startDate, endDate);

                // Clear fields
                nameField.setText("");
                seasonField.setText("");
                typeField.setText("");
                startYearField.setText("");
                startMonthField.setText("");
                startDayField.setText("");
                endYearField.setText("");
                endMonthField.setText("");
                endDayField.setText("");

            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(frame, "Invalid number format (Year or Date parts).");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid Date Format. Use YYYY-MM-DD.");
            }
        });

        backBtn.addActionListener(e -> tournamentDashboardPanel());

        cp.revalidate();
        cp.repaint();
    }

    // view all tournament
    public void viewAllTournamentPanel(LinkedHashMap<String, String> information) {
        cp = GUIUtil.createColorPanel("#CD5C33");
        cp.setLayout(null);
        frame.setContentPane(cp);

        JLabel overlayBg = new JLabel(new ImageIcon("assets/tournament/view_all_overlay.png"));
        overlayBg.setBounds(0, 0, 720, 480);

        JButton backBtn = GUIUtil.createIButton(648, 440, 64, 27);

        // Use GUIUtil's scrolling panel logic
        // x=0, yStart=-19, yGap=57, visibleCount=6 are standard from your Util example
        JPanel scrollingPanel = GUIUtil.showScrollingPanel(information, -19, -3, 57, 6,
                clicked -> {
                    String[] parts = clicked.split("/", 2);
                    String key = parts[0];
                    controller.showTournament(Integer.parseInt(key));
                });

        scrollingPanel.setBounds(0, 0, frame.getWidth(), frame.getHeight());

        cp.add(GUIUtil.createTextLabel("Back", 648, 440, 64, 27));
        cp.add(backBtn);
        cp.add(overlayBg);
        cp.add(scrollingPanel);
        cp.add(GUIUtil.createTextLabel("Tournament", 10, 50, 150, 40));

        backBtn.addActionListener(e -> tournamentDashboardPanel());

        cp.revalidate();
        cp.repaint();
    }

    // view individual tournament
    public void viewTournamentPanel(Tournament tournament) {
        cp = GUIUtil.createColorPanel("#CD5C33");
        cp.setLayout(null);
        frame.setContentPane(cp);

        JLabel id = GUIUtil.createText(String.valueOf(tournament.getTournamentID()), 410, 100, 92, 27);
        JLabel name = GUIUtil.createText(tournament.getTournamentName(), 100, 140, 286, 27);
        JLabel season = GUIUtil.createText(String.valueOf(tournament.getSeasonYear()), 100, 180, 100, 27);
        JLabel type = GUIUtil.createText(tournament.getTournamentType(), 100, 220, 200, 27);
        JLabel start = GUIUtil.createText(tournament.getStartDate().toStringDate(), 100, 260, 245, 27);
        JLabel end = GUIUtil.createText(tournament.getEndDate().toStringDate(), 370, 260, 245, 27);

        JButton backBtn = GUIUtil.createIButton(648, 440, 64, 27);

        cp.add(GUIUtil.createTextLabel("Tournament", 10, 50, 150, 40));
        cp.add(GUIUtil.createTextLabel("Back", 648, 440, 64, 27));
        cp.add(id);
        cp.add(name);
        cp.add(season);
        cp.add(type);
        cp.add(start);
        cp.add(end);
        cp.add(backBtn);

        backBtn.addActionListener(e -> tournamentDashboardPanel());

        cp.revalidate();
        cp.repaint();
    }

    // search for update
    public void searchUpdateTournamentPanel() {
        cp = GUIUtil.createColorPanel("#CD5C33");
        cp.setLayout(null);
        frame.setContentPane(cp);

        JButton submitBtn = GUIUtil.createIButton(528, 260, 98, 27);
        JTextField field = GUIUtil.createTextField(142, 261, 367, 27);
        JButton backBtn = GUIUtil.createIButton(648, 440, 64, 27);

        JLabel title = GUIUtil.createText("UPDATE", 180, 50, 500, 50);
        cp.add(title);
        title.setFont(new Font("Arial", Font.PLAIN, 50));
        cp.add(GUIUtil.createTextLabel("ID:", 100, 261, 50, 27));
        cp.add(GUIUtil.createTextLabel("SUBMIT", 528, 260, 98, 27));
        cp.add(GUIUtil.createTextLabel("Back", 648, 440, 64, 27));
        cp.add(submitBtn);
        cp.add(field);
        cp.add(backBtn);

        submitBtn.addActionListener(e -> {
            String input = field.getText().trim();
            field.setText("");
            try {
                int id = Integer.parseInt(input);
                if (!controller.updateTournamentView(id)) {
                    JOptionPane.showMessageDialog(frame, "ID does not exist.");
                }
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(frame, "Invalid ID number.");
            }
        });

        backBtn.addActionListener(e -> tournamentDashboardPanel());

        cp.revalidate();
        cp.repaint();
    }

    // update form
    public void updateTournamentPanel(Tournament tournament) {
        cp = GUIUtil.createColorPanel("#CD5C33");
        cp.setLayout(null);
        frame.setContentPane(cp);

        // Display current info
        JLabel name = GUIUtil.createText(tournament.getTournamentName(), 100, 80, 300, 27);
        JLabel season = GUIUtil.createText(String.valueOf(tournament.getSeasonYear()), 100, 120, 300, 27);
        JLabel type = GUIUtil.createText(tournament.getTournamentType(), 100, 160, 300, 27);
        JLabel start = GUIUtil.createText(tournament.getStartDate().toStringDate(), 100, 200, 300, 27);
        JLabel end = GUIUtil.createText(tournament.getEndDate().toStringDate(), 100, 240, 300, 27);

        GUIUtil.createWhiteLayer(cp, 550, 80, 36, 18);
        GUIUtil.createWhiteLayer(cp, 550, 120, 36, 18);
        GUIUtil.createWhiteLayer(cp, 550, 160, 36, 18);
        GUIUtil.createWhiteLayer(cp, 550, 200, 36, 18);
        GUIUtil.createWhiteLayer(cp, 550, 240, 36, 18);


        // Buttons
        JButton backBtn = GUIUtil.createIButton(648, 440, 64, 27);
        JButton saveBtn = GUIUtil.createIButton(570, 440, 64, 27);
        JButton editName = GUIUtil.createIButton(550, 80, 36, 18);
        JButton editSeason = GUIUtil.createIButton(550, 120, 36, 18);
        JButton editType = GUIUtil.createIButton(550, 160, 36, 18);
        JButton editStart = GUIUtil.createIButton(550, 200, 36, 18);
        JButton editEnd = GUIUtil.createIButton(550, 240, 36, 18);

        cp.add(GUIUtil.createTextLabel("Update", 570, 440, 64, 27));
        cp.add(GUIUtil.createTextLabel("Back", 648, 440, 64, 27));

        cp.add(name); cp.add(season); cp.add(type); cp.add(start); cp.add(end);
        cp.add(backBtn); cp.add(saveBtn);
        cp.add(editName); cp.add(editSeason); cp.add(editType); cp.add(editStart); cp.add(editEnd);

        backBtn.addActionListener(e -> tournamentDashboardPanel());
        saveBtn.addActionListener(e -> controller.updateTournament(tournament));

        // Using a helper for input dialogs to keep it simple since we aren't making a full panel for each field
        editName.addActionListener(e -> quickUpdate(tournament, "name", name));
        editSeason.addActionListener(e -> quickUpdate(tournament, "season", season));
        editType.addActionListener(e -> quickUpdate(tournament, "type", type));
        editStart.addActionListener(e -> quickUpdate(tournament, "start", start));
        editEnd.addActionListener(e -> quickUpdate(tournament, "end", end));

        cp.revalidate();
        cp.repaint();
    }

    // search id for delete
    public void searchDeleteTournamentPanel() {
        cp = GUIUtil.createColorPanel("#CD5C33");
        cp.setLayout(null);
        frame.setContentPane(cp);

        JButton submitBtn = GUIUtil.createIButton(528, 260, 98, 27);
        JTextField field = GUIUtil.createTextField(142, 261, 367, 27);
        JButton backBtn = GUIUtil.createIButton(648, 440, 64, 27);

        JLabel title = GUIUtil.createText("DELETE", 180, 50, 500, 50);
        cp.add(title);
        title.setFont(new Font("Arial", Font.PLAIN, 50));
        cp.add(GUIUtil.createTextLabel("ID:", 100, 261, 50, 27));
        cp.add(GUIUtil.createTextLabel("SUBMIT", 528, 260, 98, 27));
        cp.add(GUIUtil.createTextLabel("Back", 648, 440, 64, 27));
        cp.add(submitBtn);
        cp.add(field);
        cp.add(backBtn);

        submitBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(field.getText().trim());
                if (!controller.deleteTournamentView(id)) {
                    JOptionPane.showMessageDialog(frame, "ID does not exist.");
                }
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(frame, "Invalid ID number.");
            }
        });
        backBtn.addActionListener(e -> tournamentDashboardPanel());

        cp.revalidate();
        cp.repaint();
    }

    // delete confirmation
    public void deleteTournamentPanel(Tournament tournament) {
        cp = GUIUtil.createColorPanel("#CD5C33");
        cp.setLayout(null);
        frame.setContentPane(cp);

        JLabel name = GUIUtil.createText(tournament.getTournamentName(), 100, 199, 286, 27);
        JLabel id = GUIUtil.createText(String.valueOf(tournament.getTournamentID()), 410, 199, 92, 27);

        JButton backBtn = GUIUtil.createIButton(648, 440, 64, 27);
        JButton delBtn = GUIUtil.createIButton(521, 440, 113, 27);

        cp.add(GUIUtil.createTextLabel("Delete", 521, 440, 113, 27));
        cp.add(GUIUtil.createTextLabel("Back", 648, 440, 64, 27));

        cp.add(name);
        cp.add(id);
        cp.add(backBtn);
        cp.add(delBtn);

        backBtn.addActionListener(e -> tournamentDashboardPanel());
        delBtn.addActionListener(e -> {
            controller.deleteTournament(tournament);
            JOptionPane.showMessageDialog(frame, "Tournament deleted.");
            tournamentDashboardPanel();
        });

        cp.revalidate();
        cp.repaint();
    }


    // helper
    private void quickUpdate(Tournament tournament, String field, JLabel label) {
        String input = JOptionPane.showInputDialog(frame, "Enter new " + field + ":");
        if (input == null || input.trim().isEmpty()) return;

        try {
            switch (field) {
                case "name":
                    tournament.setTournamentName(input);
                    label.setText(input);
                    break;
                case "season":
                    tournament.setSeasonYear(Integer.parseInt(input));
                    label.setText(input);
                    break;
                case "type":
                    tournament.setTournamentType(input);
                    label.setText(input);
                    break;
                case "start":
                    String[] parts = input.split("-");
                    tournament.setStartDate(new CustomTimestamp(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2])));
                    label.setText(input);
                    break;
                case "end":
                    String[] eParts = input.split("-");
                    tournament.setEndDate(new CustomTimestamp(Integer.parseInt(eParts[0]), Integer.parseInt(eParts[1]), Integer.parseInt(eParts[2])));
                    label.setText(input);
                    break;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Invalid format!");
        }
    }

    // report
    public void viewReportTournamentPanel() {
        cp = GUIUtil.createColorPanel("#CD5C33");
        cp.setLayout(null);
        frame.setContentPane(cp);

        JButton generateBtn = GUIUtil.createIButton(259, 249, 200, 27);
        JButton generatePdfBtn = GUIUtil.createIButton(259, 286, 200, 27);
        JButton backBtn = GUIUtil.createIButton(648, 440, 64, 27);

        cp.add(generateBtn);
        cp.add(generatePdfBtn);
        cp.add(backBtn);

        backBtn.addActionListener(e -> tournamentDashboardPanel());

        cp.revalidate();
        cp.repaint();
    }


	public void show() { frame.setVisible(true); }
	public void hide() { frame.setVisible(false); }
	public void dispose() { if(frame != null) frame.dispose(); }
}