package view;

import model.Player;
import java.util.ArrayList;

public interface PlayerView {
    // Menu operations
    void displayMenu();
    int getMenuChoice();
    
    // Player input operations
    Player getPlayerInput();
    Player getPlayerUpdateInput();
    
    // Search operations
    String getSearchCategory();
    String getSearchKey();
    
    // Display operations
    void displayPlayer(Player player);
    void displayPlayers(ArrayList<Player> players);
    void displayMessage(String message);
    void displayError(String error);
    
    // Confirmation operations
    boolean confirmAction(String action);
    
    // Cleanup
    void close();
}