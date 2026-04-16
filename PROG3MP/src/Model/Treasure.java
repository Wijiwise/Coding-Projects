package Model;
import java.util.*;
/**
 * Represents a collectible treasure that the hero must collect to complete a level.
 * The treasure can only be collected after all enemies have been defeated.
 * 
 * @author Luis Carlos / Nathan Ng
 * @version 1.0
 */
public class Treasure {
    /** X-coordinate of the treasure's position */
    private int positionX;
    
    /** Y-coordinate of the treasure's position */
    private int positionY;
    
    /** Indicates whether the treasure has been collected */
    private boolean isCollected;

    /**
     * Constructs a new Treasure at the specified position.
     * 
     * @param x The x-coordinate of the treasure
     * @param y The y-coordinate of the treasure
     */
    public Treasure(int x, int y) {
        this.positionX = x;
        this.positionY = y;
        this.isCollected = false;
    }

    /**
     * Attempts to collect the treasure if the hero is at the same position
     * and all enemies have been defeated.
     * Awards the hero 500 points upon successful collection.
     * 
     * @param hero The hero attempting to collect the treasure
     * @param enemies The array list of enemies
     * @param snakes The array list of snakes
     * @return true if the treasure was collected, false otherwise
     */
    public boolean collect(Hero hero, ArrayList<Enemy> enemies,ArrayList<Snake> snakes ) {
            for (Enemy e : enemies) {
                if (e.isAlive()) {
                    return false;
                }
            }
            for (Snake s : snakes) {
                if (s.isAlive()) {
                    return false;
                }
            }
        
        if (hero.getPositionX() == positionX && hero.getPositionY() == positionY) {
            isCollected = true;
            hero.addScore(500);
            // Trigger level completion logic here
            return isCollected;
        }
        // Optionally notify player that enemies remain
        return isCollected;
    }
    //Getters
    /**
     * Gets the x-coordinate of the treasure's position.
     * @return The x-coordinate of the treasure
     * 
     */
    public int getPositionX() { return positionX; }

    /**
     * Gets the y-coordinate of the treasure's position.
     * @return The y-coordinate of the treasure
     */
    public int getPositionY() { return positionY; }
    /**
     * Gets whether the treasure has been collected.
     * @return return true if collected, false otherwise
     */
    public boolean getIsCollected() { return isCollected; }
}