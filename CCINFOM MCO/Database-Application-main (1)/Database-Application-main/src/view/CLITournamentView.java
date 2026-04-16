package view;

import java.util.Scanner;
import model.Tournament;
import model.TournamentManagement;

public class CLITournamentView {
/*
    private TournamentManagement tournamentManagement;
    private Scanner scanner;

    public CLITournamentView(TournamentManagement tournamentManagement) {
        this.tournamentManagement = tournamentManagement;
        this.scanner = new Scanner(System.in);
    }

    public void showMenu() {
        int choice;
        do {
            System.out.println("\n=== TOURNAMENT MANAGEMENT ===");
            System.out.println("1. Add Tournament");
            System.out.println("2. View All Tournaments");
            System.out.println("3. Search Tournament");
            System.out.println("4. Update Tournament");
            System.out.println("5. Delete Tournament");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");

            choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> addTournament();
                case 2 -> viewAllTournaments();
                case 3 -> searchTournament();
                case 4 -> updateTournament();
                case 5 -> deleteTournament();
                case 0 -> System.out.println("Exiting Tournament Menu...");
                default -> System.out.println("Invalid choice!");
            }

        } while (choice != 0);
    }

    // add tournament
    private void addTournament() {
        System.out.print("Tournament ID: ");
        int id = Integer.parseInt(scanner.nextLine());

        System.out.print("Tournament Name: ");
        String name = scanner.nextLine();

        System.out.print("Season Year: ");
        int year = Integer.parseInt(scanner.nextLine());

        System.out.print("Tournament Type: ");
        String type = scanner.nextLine();

        System.out.print("Enter Start Date (YYYY-MM-DD) ");
        String[] startParts = scanner.nextLine().split(" ");
        Timestamp startDate = new Timestamp(
                Integer.parseInt(startParts[0]),
                Integer.parseInt(startParts[1]),
                Integer.parseInt(startParts[2]));

        System.out.print("Enter End Date (YYYY-MM-DD): ");
        String[] endParts = scanner.nextLine().split(" ");
        Timestamp endDate = new Timestamp(
                Integer.parseInt(endParts[0]),
                Integer.parseInt(endParts[1]),
                Integer.parseInt(endParts[2]));

        System.out.print("Enter Team Bracket: ");
        String bracket = scanner.nextLine();

        System.out.print("Enter Fan Favorite Team: ");
        String favorite = scanner.nextLine();

        Tournament newTournament = new Tournament(id, name, year, type, startDate, endDate, bracket, favorite);
        tournamentManagement.addTournament(newTournament);
        System.out.println("Tournament added successfully!");
    }

    // view all tournament
    private void viewAllTournaments() {
        System.out.println("\n---- All Tournaments ----");
        for (Tournament t : tournamentManagement.getAllTournaments()) {
            System.out.println(t);
            System.out.println("------------------------------");
        }
    }

    // search specific tournament
    private void searchTournament() {
        System.out.print("Search by (id/name): ");
        String category = scanner.nextLine();
        System.out.print("Enter search key: ");
        String key = scanner.nextLine();

        Tournament found = tournamentManagement.searchTournament(category, key);
        if (found != null)
            System.out.println(found);
        else
            System.out.println("Tournament not found.");
    }

    // update tournament
    private void updateTournament() {
        System.out.print("Enter ID of tournament to update: ");
        String id = scanner.nextLine();
        Tournament tournament = tournamentManagement.searchTournament("id", id);

        if (tournament == null) {
            System.out.println("Tournament not found.");
            return;
        }

        int choice;
        do {
            System.out.println("\n===== UPDATE TOURNAMENT =====");
            System.out.println("Current Tournament Info:");
            System.out.println(tournament);
            System.out.println("------------------------------");
            System.out.println("1. Update Name");
            System.out.println("2. Update Type");
            System.out.println("3. Update Start Date");
            System.out.println("4. Update End Date");
            System.out.println("5. Update Team Bracket");
            System.out.println("6. Update Fan Favorite Team");
            System.out.println("0. Done / Exit");
            System.out.print("Enter choice: ");
            choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter new Tournament Name: ");
                    tournament.setTournamentName(scanner.nextLine());
                    System.out.println("Name updated!");
                }
                case 2 -> {
                    System.out.print("Enter new Tournament Type: ");
                    tournament.setTournamentType(scanner.nextLine());
                    System.out.println("Tournament Type updated!");
                }
                case 3 -> {
                    System.out.print("Enter new Start Date (YYYY MM DD): ");
                    String[] startParts = scanner.nextLine().split(" ");
                    tournament.setStartDate(new model.Timestamp(
                            Integer.parseInt(startParts[0]),
                            Integer.parseInt(startParts[1]),
                            Integer.parseInt(startParts[2])));
                    System.out.println("Start Date updated!");
                }
                case 4 -> {
                    System.out.print("Enter new End Date (YYYY MM DD): ");
                    String[] endParts = scanner.nextLine().split(" ");
                    tournament.setEndDate(new model.Timestamp(
                            Integer.parseInt(endParts[0]),
                            Integer.parseInt(endParts[1]),
                            Integer.parseInt(endParts[2])));
                    System.out.println("End Date updated!");
                }
                case 5 -> {
                    System.out.print("Enter new Team Bracket: ");
                    tournament.setTeamBracket(scanner.nextLine());
                    System.out.println("Team Bracket updated!");
                }
                case 6 -> {
                    System.out.print("Enter new Fan Favorite Team: ");
                    tournament.setFanFavoriteTeam(scanner.nextLine());
                    System.out.println("Fan Favorite updated!");
                }
                case 0 -> System.out.println("Update complete! Returning to menu...");
                default -> System.out.println("Invalid choice!");
            }

        } while (choice != 0);
    }

    // delete tournament
    private void deleteTournament() {
        System.out.print("Enter ID of tournament to delete: ");
        String id = scanner.nextLine();
        Tournament tournament = tournamentManagement.searchTournament("id", id);

        if (tournament == null) {
            System.out.println("Tournament not found.");
        } else {
            tournamentManagement.deleteTournament(tournament);
            System.out.println("Tournament deleted successfully!");
        }
    }
*/
}