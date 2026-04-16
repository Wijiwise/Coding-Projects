package Model;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
/**
 * Represents a projectile fired by a Snake enemy.
 * The projectile moves in a straight line toward its target and damages the hero on contact.
 * 
 * @author Luis Carlos / Nathan Ng
 * @version 1.0
 */
public class Projectile {
   /** X-coordinate of the projectile's current position */
    private int positionX;
    
    /** Y-coordinate of the projectile's current position */
    private int positionY;
    
    /** Direction of movement on the x-axis (-1, 0, or 1) */
    private int directionX;
    
    /** Direction of movement on the y-axis (-1, 0, or 1) */
    private int directionY;
    
    /** Maximum distance the projectile can travel */
    private int range;
    
    /** Distance the projectile has traveled so far */
    private int distanceTraveled;
    
    /** Indicates whether the projectile is still active/moving */
    private boolean active;
    
    /** Reference to the game map */
    private Map map;
    
    /** Reference to the hero (target) */
    private Hero hero;
    /** JavaFX timer */
    private Timeline movement; 

    /**
     * Constructs a new Projectile at the specified position with a direction and range.
     * 
     * @param x The starting x-coordinate
     * @param y The starting y-coordinate
     * @param dx The direction on the x-axis (-1 for left, 0 for no movement, 1 for right)
     * @param dy The direction on the y-axis (-1 for up, 0 for no movement, 1 for down)
     * @param range The maximum distance the projectile can travel
     * @param map The game map reference
     * @param hero The hero character reference
     */

    public Projectile(int x, int y, int dx, int dy, int range, Map map, Hero hero) {
        this.positionX = x;
        this.positionY = y;
        this.directionX = dx;
        this.directionY = dy;
        this.range = range;
        this.distanceTraveled = 0;
        this.active = true;
        this.map = map;
        this.hero = hero;
    }
    /**
     * Launches the projectile, moving it step by step until it hits the hero, goes out of bounds, or reaches its range.
     */
    public void launch() {
      movement = new Timeline(
            new KeyFrame(
                Duration.millis(500),
                e ->{
                    if(active && distanceTraveled < range){
                        move();
                    } else {
                        active = false;
                        movement.stop();
                    }
                }
            )

      );
      movement.setCycleCount(Timeline.INDEFINITE); // Run until stopped
      movement.play();
    }
    /**
     * Moves the projectile one step in its direction, checking for collisions with walls or the hero.
     */
    private void move() {
        int newX = positionX + directionX;
        int newY = positionY + directionY;
        
        // Check collision with walls
        if (!map.isValidPos(newX, newY) || map.getCell(newX, newY) == 'X' || map.getCell(newX, newY) == '#') {
            active = false;
            if(movement != null) movement.stop();
            return;
        }
        
        // Check collision with hero
        if (newX == hero.getPositionX() && newY == hero.getPositionY()) {
            hero.TakeDamage();
            active = false;
            if(movement != null) movement.stop();
            return;
        }
        
        positionX = newX;
        positionY = newY;
        distanceTraveled++;
    }
    //GETTERS
    /**
     * Gets the current x-coordinate of the projectile.
     * 
     * @return The x-coordinate
     */
    public int getPositionX() { return positionX;}

    /**
     * Gets the current y-coordinate of the projectile.
     * 
     * @return The y-coordinate
     */
    public int getPositionY() { return positionY; }
    /**
     * Checks if the projectile is currently active and moving.
     * 
     * @return true if the projectile is active, false otherwise
     */
    public boolean isActive() {
        return active;
    }


}