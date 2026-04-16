package Model;

/**
 * Represents a power-up item in the game.
 *  The Normal PowerUp class will increase the bomb's blast radius by 1 when collected.
 *  PowerUps last until hero is hit.
 *  this can be obtained at anytime during the game
 *  
 * @author Luis Carlos / Nathan Ng
 * @version 1.0
 */
public class PowerUp {
    /** Indicates whether this power-up item has been collected by the hero. */
    private boolean isCollected;
    /** The x-coordinate of the power-up's position on the map. */
    private int positionX;
    /** The y-coordinate of the power-up's position on the map. */
    private int positionY;
    
    /**
     * Constructs a new PowerUp (Blast Radius Power-Up) at the specified coordinates.
     *
     * The initial collection status is set to false.
     *
     * @param x The initial x-coordinate where the power-up is placed.
     * @param y The initial y-coordinate where the power-up is placed.
     */
    public PowerUp(int x, int y){
        this.positionX = x;
        this.positionY = y;
        this.isCollected = false;
    }

    /**
     * Attempts to collect this power-up item by the Hero.
     *
     * If the hero's position matches the power-up's position:
     * * The power-up's status is set to collected.
     * * The hero's bomb blast radius is increased by 1 via Hero.increaseBombBlastRadius(int).
     * * The method returns true.
     *
     * @param hero The hero attempting to collect this power-up.
     * @return true if the hero successfully collected the power-up, false otherwise.
     */
    public boolean collect(Hero hero) {
        
        if (hero.getPositionX() == positionX && hero.getPositionY() == positionY) {
            
            // 💡 FIX 2: Correctly set the flag in the base class.
            this.isCollected = true;
            
            // 💡 FIX 3: Uncomment/confirm the Blast Radius increase for the 'P' PowerUp
            hero.increaseBombBlastRadius(1);
            
            return true;
        }
        // Optionally notify player that enemies remain
        return false;
    }

    //Getters
    /**
     * Gets the current x-coordinate of the power-up.
     * @return The x-coordinate.
     */
    public int getPositionX(){return positionX;}
    
    /**
     * Gets the current y-coordinate of the power-up.
     * @return The y-coordinate.
     */
    public int getPositionY(){return positionY;}
    
    /**
     * Checks the collection status of the power-up.
     * @return {@code true} if the power-up has been collected, {@code false} otherwise.
     */
    public boolean getCollected(){return isCollected;}

    // Setters
    
    /**
     * Sets a new x-coordinate for the power-up.
     * @param x The new x-coordinate value.
     */
    public void setX(int x){this.positionX = x;}
    
    /**
     * Sets a new y-coordinate for the power-up.
     *
     * @param y The new y-coordinate value.
     */
    public void setY(int y){this.positionY = y;} 
    
    /**
     * Sets the collection status of the power-up.
     * @param isCollected {@code true} to mark as collected, {@code false} otherwise.
     */
    public void setIsCollected(boolean isCollected){this.isCollected = isCollected;}
}