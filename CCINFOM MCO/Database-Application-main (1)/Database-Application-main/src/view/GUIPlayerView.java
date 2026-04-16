package view;
import model.*;
import controller.*;
import util.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GUIPlayerView {
    private JFrame frame;
    private Container cp;

    private PlayerController controller;
    private Player player; 

    private CoachManagement coachModel;
    private Coach coach;
    private GameManagement gameModel;
    private Game game;



    public GUIPlayerView() {}

    public void start() {
        frame = GUIUtil.setupGUI(frame, cp, 720, 480);
    }

    public void setListeners(PlayerController controller) {
        this.controller = controller;
    }

    // FIX 1: Overloaded method to use stored player object (used by back button)
    public void profilePanel() {
        if (this.player != null) {
            profilePanel(this.player);
        }
    }

    public void profilePanel(Player player) {
        this.player = player; // <--- FIX 1: Store the player object
        cp = BackgroundPanel.create("assets/login/player/profile.png");
        cp.setLayout(null);
        frame.setContentPane(cp);
        
        // Data labels
        // FIX: Corrected getPLayerID() -> getPlayerID()
        JLabel id = GUIUtil.createText(String.valueOf(player.getPlayerID()),416,205,92,27);
        // FIX: Corrected getSex() -> getGender()
        JLabel sex = GUIUtil.createText(String.valueOf(player.getSex()),532,205,87,27); 
        JLabel fullname = GUIUtil.createText(player.getFullName(),105,261,514,27);
        JLabel birthday = GUIUtil.createText(player.getDateOfBirth(),106,318,243,27);
        JLabel height = GUIUtil.createText(String.valueOf(player.getHeight()) + " cm",368,318,125,27);
        JLabel weight = GUIUtil.createText(String.valueOf(player.getWeight()) + " kg",521,318,98,27);
        // FIX: Corrected getRStatus() -> getStatus()
        JLabel rStatus = GUIUtil.createText(player.getStatus() ? "Active" : "Inactive",659,205,92,27);
        

        JButton updateBtn = GUIUtil.createIButton(455,116,80,38);
        JButton deleteBtn = GUIUtil.createIButton(544,116,80,36);

        // FIX 1: Add action listeners for navigation
        updateBtn.addActionListener(e -> updatePanel());
        deleteBtn.addActionListener(e -> deletePanel());

        cp.add(id);
        cp.add(sex);
        cp.add(fullname);
        cp.add(birthday);
        cp.add(height);
        cp.add(weight);
        cp.add(rStatus);
        cp.add(updateBtn);
        cp.add(deleteBtn);

        cp.revalidate();
        cp.repaint();
    }
    
    public void updatePanel() {
        if (player == null) {
             // Handle case where player is not initialized (e.g., error handling)
            return; 
        }

        cp = BackgroundPanel.create("assets/admin/player/update_view.png");
        cp.setLayout(null);
        frame.setContentPane(cp);

        // FIX 1: Corrected getPLayerID() to getPlayerID()
        JLabel id = GUIUtil.createText(String.valueOf(player.getPlayerID()),410,199,92,27);
        JLabel firstname = GUIUtil.createText(player.getFirstName(),102,273,153,27);
        JLabel middlename = GUIUtil.createText(player.getMiddleName(),276,271,164,27);
        JLabel lastname = GUIUtil.createText(player.getLastName(),462,273,153,27);
        // Displaying date as stored in Player model (String)
        JLabel birthday = GUIUtil.createText(player.getDateOfBirth(),100,346,245,27);
        
        // FIX 2: Corrected getSex() to getGender()
        JLabel sex = GUIUtil.createText(String.valueOf(player.getSex()),526,199,89,27);
        JLabel height = GUIUtil.createText(String.valueOf(player.getHeight()) + " cm",310,346,125,27);
        JLabel weight = GUIUtil.createText(String.valueOf(player.getWeight()) + " kg",521,346,98,27);
        
        // FIX 3: Corrected getRStatus() to getStatus()
        JLabel rStatus = GUIUtil.createText(player.getStatus() ? "Active" : "Inactive",659,199,92,27);
    
        JButton backBtn = GUIUtil.createIButton(648,440,64,27);
        JButton saveBtn = GUIUtil.createIButton(570,440,64,27);
        
        // FIX 4: Removed editUsername as Player model does not have a username field
        // JButton editUsername = GUIUtil.createIButton(354,179,36,18); 
        
        JButton editFirstName = GUIUtil.createIButton(223,253,36,18);
        JButton editMiddleName = GUIUtil.createIButton(409,252,36,18);
        JButton editLastName = GUIUtil.createIButton(584,253,36,18);
        JButton editSex = GUIUtil.createIButton(584,179,36,18);
        JButton editBirthday = GUIUtil.createIButton(314,326,36,18);

        cp.add(id);
        cp.add(sex);
        cp.add(firstname);
        cp.add(middlename);
        cp.add(lastname);
        cp.add(birthday);
        cp.add(height);
        cp.add(weight);
        cp.add(rStatus);

        cp.add(backBtn);
        cp.add(saveBtn);
        // cp.add(editUsername); // Removed
        cp.add(editFirstName);
        cp.add(editMiddleName);
        cp.add(editLastName);
        cp.add(editSex);
        cp.add(editBirthday);

        // FIX 1: Changed to call the parameterless profilePanel() overload
        backBtn.addActionListener(e -> profilePanel()); 
        saveBtn.addActionListener(e -> controller.handleUpdate(player));
        
        // FIX 4: Removed editUsername listener
        // editUsername.addActionListener(e -> updateFieldPanel("username")); 
        
        editFirstName.addActionListener(e -> updateFieldPanel("first name"));
        editMiddleName.addActionListener(e -> updateFieldPanel("middle name"));
        editLastName.addActionListener(e -> updateFieldPanel("last name"));
        editSex.addActionListener(e -> updateFieldPanel("sex"));
        editBirthday.addActionListener(e -> updateFieldPanel("birthday"));

        cp.revalidate();
        cp.repaint(); 
    }

    public void updateFieldPanel(String type) {
        if (player == null) return;
        
        cp = BackgroundPanel.create("assets/admin/player/update_field.png");
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
            
            // Validation for required fields
            switch(type) {
                // FIX 5: Removed "username" from required field validation
                case "sex": case "first name": 
                case "last name": case "birthday":
                    if(input.isEmpty()) {
                        JOptionPane.showMessageDialog(frame, 
                            "Required Field! Please input update for " + type + ".");
                        return;
                    }
            }

            switch(type) {
                // FIX 5: Removed case "username"
                case "sex": 
                    // FIX 6: Changed setSex() to setGender() for consistency with getGender()
                    player.setSex(input.toUpperCase()); 
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
                    // FIX 7: Confirmed date parsing logic is correct (MM/DD/YYYY input -> YYYY-MM-DD model)
                    DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy"); 
                    DateTimeFormatter outputFormat = DateTimeFormatter.ISO_DATE; // YYYY-MM-DD
                    try {
                        LocalDate date = LocalDate.parse(input, inputFormat);
                        // Store the date in the YYYY-MM-DD format as required by Player.java
                        player.setDateOfBirth(date.format(outputFormat)); 
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

    // FIX 8: Corrected method name to proper Java convention: passUpdatedPlayer
    public void passUpdatedPlayer(Player player) {
        // Assuming Player.java has the public void update(Player other) method
        this.player.update(player); 
        profilePanel(); // Calls the parameterless overload
    }

    public void deletePanel() {
        if (player == null) return;
        
        int choice = JOptionPane.showConfirmDialog(
                        frame, 
                        "Do you want to proceed?", 
                        "Account Deletion", 
                        JOptionPane.YES_NO_OPTION
                        );

        if(choice == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(null, "Successful Deletion! Logging out."); 
            controller.handleDelete(player);
            controller.handleLogout();
        } else {
            JOptionPane.showMessageDialog(null, "Deletion cancelled.");
        }
    }

    public void displayMessage(String message) {
    JOptionPane.showMessageDialog(
        frame,     // parent component (your view)
        message, 
        "Information",
        JOptionPane.INFORMATION_MESSAGE
    );
}

    public void displayError(String message) {
    JOptionPane.showMessageDialog(
        frame, 
        message, 
        "Error",  
        JOptionPane.ERROR_MESSAGE
    );
}
    public void start(Player player) {
        this.player = player;
        profilePanel(player);
    }

    /**
 * Panel showing player's current teams
 */
public void myTeamsPanel(Player player, ArrayList<Team> teams) {
    cp = BackgroundPanel.create("assets/login/player/my_teams.png");
    cp.setLayout(null);
    frame.setContentPane(cp);
    
    JButton backBtn = GUIUtil.createIButton(648, 440, 64, 27);
    cp.add(backBtn);
    
    // Title
    JLabel title = GUIUtil.createText("My Teams", 280, 40, 200, 40);
    title.setFont(new Font("Arial", Font.BOLD, 24));
    cp.add(title);
    
    if (teams.isEmpty()) {
        JLabel noTeams = GUIUtil.createText("You are not on any teams yet.", 200, 200, 400, 30);
        noTeams.setFont(new Font("Arial", Font.PLAIN, 16));
        cp.add(noTeams);
    } else {
        // Create table for teams
        String[] columns = {"Team ID", "Team Name", "Coach ID", "Players", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        
        for (Team team : teams) {
            Object[] row = {
                team.getTeamID(),
                team.getTeamName(),
                team.getCoachID(),
                team.getNumberOfPlayers(),
                team.getIsRegistered() ? "Active" : "Inactive"
            };
            model.addRow(row);
        }
        
        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.getTableHeader().setReorderingAllowed(false);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(50, 100, 620, 300);
        cp.add(scrollPane);
    }
    
    backBtn.addActionListener(e -> profilePanel(player));
    
    cp.revalidate();
    cp.repaint();
}

/**
 * Panel showing player's registration requests
 */
public void myRequestsPanel(Player player, ArrayList<RegistrationRequest> requests) {
    cp = BackgroundPanel.create("assets/login/player/my_requests.png");
    cp.setLayout(null);
    frame.setContentPane(cp);
    
    JButton backBtn = GUIUtil.createIButton(648, 440, 64, 27);
    cp.add(backBtn);
    
    // Title
    JLabel title = GUIUtil.createText("My Registration Requests", 200, 40, 400, 40);
    title.setFont(new Font("Arial", Font.BOLD, 24));
    cp.add(title);
    
    if (requests.isEmpty()) {
        JLabel noRequests = GUIUtil.createText("No registration requests found.", 200, 200, 400, 30);
        noRequests.setFont(new Font("Arial", Font.PLAIN, 16));
        cp.add(noRequests);
    } else {
        // Create table for requests
        String[] columns = {"Request ID", "Team ID", "Status", "Request Date", "Reviewed Date"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        for (RegistrationRequest req : requests) {
            // Format dates
            String requestDate = req.getRequestDate() != null ? 
                req.getRequestDate().toString().substring(0, 10) : "N/A";
            String reviewedDate = req.getReviewedDate() != null ? 
                req.getReviewedDate().toString().substring(0, 10) : "Pending";
            
            Object[] row = {
                req.getRequestId(),
                req.getTeamId(),
                req.getStatus().toUpperCase(),
                requestDate,
                reviewedDate
            };
            model.addRow(row);
        }
        
        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.getTableHeader().setReorderingAllowed(false);
        
        // Add color coding for status
        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (column == 2) { // Status column
                    String status = (String) value;
                    if ("APPROVED".equals(status)) {
                        c.setForeground(new Color(0, 128, 0)); // Green
                    } else if ("REJECTED".equals(status)) {
                        c.setForeground(Color.RED);
                    } else if ("PENDING".equals(status)) {
                        c.setForeground(new Color(255, 165, 0)); // Orange
                    } else {
                        c.setForeground(Color.BLACK);
                    }
                } else {
                    c.setForeground(Color.BLACK);
                }
                
                return c;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(50, 100, 620, 300);
        cp.add(scrollPane);
        
        // Add click listener to show rejection reason
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                int row = table.rowAtPoint(evt.getPoint());
                if (row >= 0) {
                    RegistrationRequest req = requests.get(row);
                    if ("rejected".equals(req.getStatus()) && req.getRejectionReason() != null) {
                        JOptionPane.showMessageDialog(frame, 
                            "Rejection Reason:\n" + req.getRejectionReason(),
                            "Request Details", 
                            JOptionPane.INFORMATION_MESSAGE);
                    } else if ("approved".equals(req.getStatus())) {
                        JOptionPane.showMessageDialog(frame, 
                            "Request approved! You are now part of the team.",
                            "Request Details", 
                            JOptionPane.INFORMATION_MESSAGE);
                    } else if ("pending".equals(req.getStatus())) {
                        JOptionPane.showMessageDialog(frame, 
                            "Request is pending. Waiting for coach approval.",
                            "Request Details", 
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });
        
        // Add instruction label
        JLabel instruction = GUIUtil.createText("Click on a row to see details", 250, 410, 300, 20);
        instruction.setFont(new Font("Arial", Font.ITALIC, 12));
        cp.add(instruction);
    }
    
    backBtn.addActionListener(e -> profilePanel(player));
    
    cp.revalidate();
    cp.repaint();
}

/**
 * Panel showing team details before joining
 */
public void viewTeamDetailsPanel(Team team, TeamRequirements requirements, ArrayList<Player> players) {
    cp = GUIUtil.createColorPanel("#CD5C33");
    cp.setLayout(null);
    frame.setContentPane(cp);
    
    JButton backBtn = GUIUtil.createIButton(648, 440, 64, 27);
    JButton joinBtn = GUIUtil.createIButton(550, 440, 80, 27);
    
    // Title
    JLabel title = GUIUtil.createText("Team Details", 260, 20, 200, 40);
    title.setFont(new Font("Arial", Font.BOLD, 24));
    title.setForeground(Color.WHITE);
    cp.add(title);
    
    // Team Information
    JLabel teamName = GUIUtil.createText("Team: " + team.getTeamName(), 50, 70, 300, 30);
    teamName.setFont(new Font("Arial", Font.BOLD, 16));
    teamName.setForeground(Color.WHITE);
    cp.add(teamName);
    
    JLabel teamPlayers = GUIUtil.createText("Players: " + team.getNumberOfPlayers(), 50, 100, 300, 30);
    teamPlayers.setForeground(Color.WHITE);
    cp.add(teamPlayers);
    
    JLabel teamStatus = GUIUtil.createText("Status: " + (team.getIsRegistered() ? "Open for Registration" : "Closed"), 
                                          50, 125, 300, 30);
    teamStatus.setForeground(Color.WHITE);
    cp.add(teamStatus);
    
    // Requirements
    String reqText = "Requirements: ";
    if (requirements != null) {
        reqText = String.format("<html>Requirements:<br/>Age: %d-%d years | Height: %.0f-%.0fcm<br/>Max Players: %d | Registration: %s</html>",
            requirements.getMinAge(), requirements.getMaxAge(),
            requirements.getMinHeight(), requirements.getMaxHeight(),
            requirements.getMaxPlayers(),
            requirements.isRegistrationOpen() ? "Open" : "Closed");
    } else {
        reqText = "<html>Requirements: No specific requirements set<br/>Default: Any age, any height, max 15 players</html>";
    }
    
    JLabel reqLabel = GUIUtil.createText(reqText, 50, 155, 600, 60);
    reqLabel.setForeground(Color.WHITE);
    cp.add(reqLabel);
    
    // Current Players Table
    JLabel playersTitle = GUIUtil.createText("Current Roster:", 50, 220, 200, 30);
    playersTitle.setFont(new Font("Arial", Font.BOLD, 14));
    playersTitle.setForeground(Color.WHITE);
    cp.add(playersTitle);
    
    if (players.isEmpty()) {
        JLabel noPlayers = GUIUtil.createText("No players on this team yet.", 50, 260, 300, 30);
        noPlayers.setForeground(Color.WHITE);
        cp.add(noPlayers);
    } else {
        String[] columns = {"Player Name", "Height (cm)", "Weight (kg)"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        for (Player p : players) {
            Object[] row = {
                p.getFullName(),
                String.format("%.1f", p.getHeight()),
                String.format("%.1f", p.getWeight())
            };
            model.addRow(row);
        }
        
        JTable table = new JTable(model);
        table.setRowHeight(25);
        table.getTableHeader().setReorderingAllowed(false);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(50, 260, 620, 140);
        cp.add(scrollPane);
    }
    
    // Buttons
    JLabel backLabel = GUIUtil.createText("Back", 648, 440, 64, 27);
    backLabel.setForeground(Color.WHITE);
    backLabel.setHorizontalAlignment(SwingConstants.CENTER);
    cp.add(backLabel);
    
    JLabel joinLabel = GUIUtil.createText("Join Team", 550, 440, 80, 27);
    joinLabel.setForeground(Color.WHITE);
    joinLabel.setHorizontalAlignment(SwingConstants.CENTER);
    cp.add(joinLabel);
    
    cp.add(backBtn);
    cp.add(joinBtn);
    
    backBtn.addActionListener(e -> {
        // Go back to browse teams - need to get all teams
        controller.handleMenu("browse", this.player);
    });
    
    joinBtn.addActionListener(e -> {
        int confirm = JOptionPane.showConfirmDialog(frame,
            "Do you want to request to join " + team.getTeamName() + "?",
            "Confirm Registration",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            controller.requestJoinTeam(this.player, team.getTeamID());
        }
    });
    
    cp.revalidate();
    cp.repaint();
}

/**
 * Panel for browsing available teams
 * This method uses the new viewTeamDetailsPanel above
 */
public void browseTeamsPanel(Player player, ArrayList<Team> teams) {
    cp = BackgroundPanel.create("assets/login/player/browse_teams.png");
    cp.setLayout(null);
    frame.setContentPane(cp);
    
    JButton backBtn = GUIUtil.createIButton(648, 440, 64, 27);
    cp.add(backBtn);
    
    // Title
    JLabel title = GUIUtil.createText("Browse Teams", 260, 40, 200, 40);
    title.setFont(new Font("Arial", Font.BOLD, 24));
    cp.add(title);
    
    if (teams.isEmpty()) {
        JLabel noTeams = GUIUtil.createText("No teams available at the moment.", 200, 200, 400, 30);
        noTeams.setFont(new Font("Arial", Font.PLAIN, 16));
        cp.add(noTeams);
    } else {
        // Create table for available teams
        String[] columns = {"Team ID", "Team Name", "Players", "Status", "View Details"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Only "View Details" column is clickable
            }
        };
        
        for (Team team : teams) {
            Object[] row = {
                team.getTeamID(),
                team.getTeamName(),
                team.getNumberOfPlayers(),
                team.getIsRegistered() ? "Open" : "Closed",
                "View"
            };
            model.addRow(row);
        }
        
        JTable table = new JTable(model);
        table.setRowHeight(35);
        table.getTableHeader().setReorderingAllowed(false);
        
        // Add button column for "View Details"
        table.getColumn("View Details").setCellRenderer(new ButtonRenderer());
        table.getColumn("View Details").setCellEditor(new ButtonEditor(new JCheckBox(), teams));
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(50, 100, 620, 300);
        cp.add(scrollPane);
    }
    
    backBtn.addActionListener(e -> profilePanel(player));
    
    cp.revalidate();
    cp.repaint();
}

// ========== Helper Classes for Table Buttons ==========

/**
 * Button Renderer for table cells
 */
class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
    public ButtonRenderer() {
        setOpaque(true);
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        setText((value == null) ? "" : value.toString());
        return this;
    }
}

/**
 * Button Editor for table cells
 */
class ButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private String label;
    private boolean isPushed;
    private ArrayList<Team> teams;
    private JTable table;
    
    public ButtonEditor(JCheckBox checkBox, ArrayList<Team> teams) {
        super(checkBox);
        this.teams = teams;
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(e -> fireEditingStopped());
    }
    
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        this.table = table;
        label = (value == null) ? "" : value.toString();
        button.setText(label);
        isPushed = true;
        return button;
    }
    
    @Override
    public Object getCellEditorValue() {
        if (isPushed) {
            int row = table.getSelectedRow();
            if (row >= 0 && row < teams.size()) {
                Team team = teams.get(row);
                controller.viewTeamDetails(team.getTeamID());
            }
        }
        isPushed = false;
        return label;
    }
    
    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }
}
    public void show() { frame.setVisible(true); }
    public void hide() { frame.setVisible(false); }
    public void dispose() { if(frame != null) frame.dispose(); }
}