package view;

import model.Player;
import java.util.ArrayList;
import java.util.Scanner;

// IMPORTANT: CLIPlayerView must implement PlayerView interface
public class CLIPlayerView implements PlayerView {
    private Scanner scanner;

    public CLIPlayerView() {
        scanner = new Scanner(System.in);
    }

    @Override
    public void displayMenu() {
        System.out.println("\n=== Player Management System ===");
        System.out.println("1. Add Player");
        System.out.println("2. Search Player");
        System.out.println("3. Update Player");
        System.out.println("4. Delete Player");
        System.out.println("5. Display All Players");
        System.out.println("6. Exit");
        System.out.print("Enter your choice: ");
    }

    @Override
    public int getMenuChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    @Override
    public Player getPlayerInput() {
        System.out.println("\n--- Enter Player Details ---");
        
        System.out.print("Player ID: ");
        int playerID = Integer.parseInt(scanner.nextLine());
        
        System.out.print("Last Name: ");
        String lastName = scanner.nextLine();
        
        System.out.print("First Name: ");
        String firstName = scanner.nextLine();
        
        System.out.print("Middle Name: ");
        String middleName = scanner.nextLine();
        
        System.out.print("Birthday (YYYY-MM-DD): ");
        String birthday = scanner.nextLine();
        
        System.out.print("Gender (M/F): ");
        char gender = scanner.nextLine().toUpperCase().charAt(0);
        
        System.out.print("Height (cm): ");
        double height = Double.parseDouble(scanner.nextLine());
        
        System.out.print("Weight (kg): ");
        double weight = Double.parseDouble(scanner.nextLine());
        
        System.out.print("Registration Status (true/false): ");
        boolean rStatus = Boolean.parseBoolean(scanner.nextLine());

       // return new Player(playerID, lastName, firstName, middleName, 
       //                  age, birthday, gender, height, weight, rStatus);
		return new Player(playerID, lastName, firstName, middleName,  birthday, String.valueOf(gender), height, weight, rStatus);
    }

    @Override
    public Player getPlayerUpdateInput() {
        System.out.println("\n--- Enter Updated Player Details (leave blank to skip) ---");
        
        System.out.print("Last Name: ");
        String lastName = scanner.nextLine();
        
        System.out.print("First Name: ");
        String firstName = scanner.nextLine();
        
        System.out.print("Middle Name: ");
        String middleName = scanner.nextLine();
        
        System.out.print("Birthday (YYYY-MM-DD): ");
        String birthday = scanner.nextLine();
        
        System.out.print("Gender (M/F): ");
        String genderInput = scanner.nextLine();
        String gender = genderInput.isEmpty() ? null : genderInput.toUpperCase();
        
        System.out.print("Registration Status (true/false): ");
        String statusInput = scanner.nextLine();
        boolean rStatus = statusInput.isEmpty() ? false : Boolean.parseBoolean(statusInput);

		/*
        return new Player(0, 
                        lastName.isEmpty() ? null : lastName,
                        firstName.isEmpty() ? null : firstName,
                        middleName.isEmpty() ? null : middleName,
                        0, 
                        birthday.isEmpty() ? null : birthday,
                        gender, 0, 0, rStatus);
								*/
		return new Player(0, lastName.isEmpty()? null : lastName,
                        firstName.isEmpty() ? null : firstName,
                        middleName.isEmpty() ? null : middleName,
                        birthday.isEmpty() ? null : birthday, gender, 0, 0, rStatus);
    }

    @Override
    public String getSearchCategory() {
        System.out.println("\nSearch by:");
        System.out.println("1. ID");
        System.out.println("2. Name");
        System.out.print("Enter choice: ");
        int choice = Integer.parseInt(scanner.nextLine());
        return (choice == 1) ? "id" : "name";
    }

    @Override
    public String getSearchKey() {
        System.out.print("Enter search term: ");
        return scanner.nextLine();
    }

    @Override
    public void displayPlayer(Player player) {
        if (player == null) {
            System.out.println("\nPlayer not found.");
            return;
        }
        System.out.println("\n--- Player Details ---");
        System.out.println("Player ID: " + player.getPlayerID());
        System.out.println("Name: " + player.getFirstName() + " " + 
                          player.getMiddleName() + " " + player.getLastName());
        //System.out.println("Age: " + player.getAge());
        System.out.println("Birthday: " + player.getDateOfBirth());
        System.out.println("Gender: " + player.getSex());
        System.out.println("Height: " + player.getHeight() + " cm");
        System.out.println("Weight: " + player.getWeight() + " kg");
        System.out.println("Status: " + (player.getStatus() ? "Active" : "Inactive"));
    }

    @Override
    public void displayPlayers(ArrayList<Player> players) {
        if (players.isEmpty()) {
            System.out.println("\nNo players in the system.");
            return;
        }
        System.out.println("\n=== All Players ===");
        for (Player player : players) {
            displayPlayer(player);
            System.out.println("---");
        }
    }

    @Override
    public void displayMessage(String message) {
        System.out.println("\n" + message);
    }

    @Override
    public void displayError(String error) {
        System.err.println("\nError: " + error);
    }

    @Override
    public boolean confirmAction(String action) {
        System.out.print("\nAre you sure you want to " + action + "? (yes/no): ");
        String response = scanner.nextLine().toLowerCase();
        return response.equals("yes") || response.equals("y");
    }

    @Override
    public void close() {
        scanner.close();
    }
}