package Model;
import java.util.*;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Represents the player-controlled hero character in the game.
 * The hero can move, place bombs, take damage, collect points, and manage power-up effects.
 * It uses JavaFX {@code IntegerProperty} for lives and score to facilitate UI binding.
 * * @author Luis Carlos/ Nathan Ng
 * @version 3.0
 */
public class Hero {
    
    /** Name of the Hero */
    private String hName;
    
    /** Number of lives the Hero currently has, stored as a bindable JavaFX property. */
    private IntegerProperty nLives = new SimpleIntegerProperty(3); 
    
    /** Current score of the Hero, stored as a bindable JavaFX property. */
    private IntegerProperty nscore = new SimpleIntegerProperty(0); 
    
    /** X coordinate of the Hero on the map. */
    private int positionX;
    
    /** Y coordinate of the Hero on the map. */
    private int positionY;
    
    /** Tells whether the Hero is still alive. */
    private boolean isAlive;
    
    /** The current blast radius of the bombs placed by the Hero. */
    private int bombBlastRadius = 2;
    
    /** The base, unpowered blast radius for bombs (used for resetting after damage). */
    private final int BASE_RADIUS = 2;

    //Constructor 
    /**
     * Constructs a new Hero with a specified name.
     *
     * Initializes lives to 3, score to 0, sets the hero as alive, and sets the
     * base bomb blast radius.
     *
     * @param name The name of the hero character.
     */
    public Hero(String name){
        this.hName = name;
        this.nLives.set(3);
        this.nscore.set(0);
        this.isAlive = true;
    }
    
    /**
     * Moves the hero according to player input and handles map interactions.
     * 
     * The hero can move up ('w'), down ('s'), left ('a'), or right ('d') if the
     * destination cell is walkable and within bounds. The 'f' key is used to
     * {@link #PlaceBomb(Map, ArrayList, ArrayList) place a bomb}.
     * Movement onto the 'T' (Treasure) cell is blocked if any enemy is still alive.
     * 
     * @param input The player's input character ('w', 'a', 's', 'd', or 'f'). // Added missing tag
     * @param map The game map that defines valid movement and object positions.
     * @param enemies A list of all active enemies in the current level.
     * @param snakes A list of all active snake enemies in the current level.
     */
    public void Move(char input, Map map, ArrayList<Enemy> enemies, ArrayList<Snake> snakes ){
        int newX = positionX;
        int newY = positionY;
        switch(input){
            case 'w': newY--; break;//UP
            case 's': newY++; break;//DOWN
            case 'a': newX--; break;//LEFT
            case 'd': newX++; break;//RIGHT
            case 'f': PlaceBomb(map, enemies, snakes); break;//PLACE BOMB
            default:
            break;
        }
        if(map.isValidPos(newX, newY) && map.isWalkable(newX, newY)){
            char destinationCell = map.getCell(newX, newY);
            
            // Check if trying to move onto treasure with enemies still alive
            if (destinationCell == 'T') {
                boolean enemiesRemain = false;
                
                // Check if any enemies are still alive
                for (Enemy enemy : enemies) {
                    if (enemy.isAlive()) {
                        enemiesRemain = true;
                        break;
                    }
                }
                
                // Check if any snakes are still alive
                if (!enemiesRemain) {
                    for (Snake snake : snakes) {
                        if (snake.isAlive()) {
                            enemiesRemain = true;
                            break;
                        }
                    }
                }
                
                // Block movement if enemies remain
                if (enemiesRemain) {
                    return;
                }
            }
            //Clear old postion
            map.setCell(positionX, positionY, ' ');

            //Update to new postion
            positionX = newX;
            positionY = newY;
            map.setCell(positionX, positionY, 'H');
        }
    }
    
    /**
     * Places a new {@link Bomb} at the hero’s current position, if a bomb does not
     * already exist at the location.
     * 
     * The bomb is initialized with the hero's current blast radius and starts its countdown.
     * The bomb is visualized as 'B' on the map.
     * 
     * @param map The map where the bomb will be placed. // Added missing tag
     * @param enemies The list of enemies affected by the bomb.
     * @param snakes The list of snakes affected by the bomb.
     * @return The newly created {@code Bomb} object, or {@code null} if a bomb is already present.
     */
    public Bomb PlaceBomb(Map map, ArrayList<Enemy> enemies, ArrayList<Snake> snakes) {
        char cell = map.getCell(positionX, positionY);

        if (cell == 'B') {
            return null;
        }

        // Place new bomb
        Bomb bomb = new Bomb(positionX, positionY, map, this); 
        map.setCell(positionX, positionY, 'B'); // visually mark bomb on map
        bomb.countdown(enemies, snakes);

        return bomb;
    }

    /**
     * Reduces the hero's lives by 1 when they take damage.
     * 
     * If the hero's lives drop to 0 or below, the {@link #Die()} method is called.
     * This method also implicitly resets the bomb blast radius to the base value
     * after damage is taken, as per typical power-up rules (though the reset logic 
     * is often handled externally or within this method).
     *
     */
    public void TakeDamage(){
        if(nLives.get() > 0) {
            // Decrement lives
            nLives.set(nLives.get() - 1);
            // Assuming this is where the power-up effect is lost:
            resetBombBlastRadius(); 
        }

        // Check for immediate death
        if (nLives.get() <= 0) {
            // Ensure lives are exactly 0 (not negative)
            nLives.set(0); 
            Die();
        }
    }
    
    /**
     * Sets the hero's {@code isAlive} status to {@code false} and triggers end-game logic.
     * 
     * This method should only be called if the hero's life count is 0 or less.
     * 
     */
    public void Die(){
        if(nLives.get() <= 0){
            this.isAlive = false;
            //end game logic
        }
    }
    //====================
    //POWER UP MANAGEMENT
    //====================
    
    /**
     * Increases the hero's bomb blast radius by the specified amount.
     * This is typically called when a Blast Radius Power-Up is collected.
     * @param amount The value to add to the current bomb blast radius. // Added missing tag
     */
    public void increaseBombBlastRadius(int amount) {
        this.bombBlastRadius += amount;
    }
    
    /**
     * Resets the bomb blast radius to the {@link #BASE_RADIUS}.
     * This is typically called when the hero takes damage.
     */
    public void resetBombBlastRadius() {
        this.bombBlastRadius = BASE_RADIUS;
    }
    
    /**
     * Gets the hero's current bomb blast radius.
     * This value is used by the {@link Bomb} constructor when placing a new bomb.
     * @return The current bomb blast radius (e.g., 2 for base, 3 or more if powered up).
     */
    public int getBombBlastRadius() {
        return this.bombBlastRadius;
    }

    /**
     * Increases the hero's life count by the specified amount.
     * This is typically called when an {@code ExtraLife} power-up is collected.
     * @param amount The number of lives to add. 
     */
    public void increaseLife(int amount){
        nLives.set(nLives.get() + amount);
    }

    // =======================
    // GETTERS AND UTILITIES
    // =======================

    /**
     * Gets the current x-coordinate of the hero.
     * @return The x-coordinate.
     */
    public int getPositionX() { return positionX; }
    
    /**
     * Gets the current y-coordinate of the hero.
     * @return The y-coordinate.
     */
    public int getPositionY() { return positionY; }
    
    /**
     * Gets the name of the hero.
     * @return The hero's name.
     */
    public String getName() { return hName; }
    
    /**
     * Checks if the hero is currently alive.
     * @return {@code true} if the hero's life count is greater than 0, {@code false} otherwise.
     */
    public boolean getIsAlive() { return isAlive; }
    
    /**
     * Gets the JavaFX {@code IntegerProperty} for the hero's lives.
     * This is used for binding the lives count to a UI element.
     * @return The bindable property for lives.
     */
    public IntegerProperty LivesProperty(){ return nLives;}
    
    /**
     * Gets the JavaFX {@code IntegerProperty} for the hero's score.
     * This is used for binding the score count to a UI element.
     * @return The bindable property for score.
     */
    public IntegerProperty ScoreProperty(){ return nscore;}
    
    /**
     * Gets the current score of the hero.
     * @return The current score value.
     */
    public int getScore() { return nscore.get();}
    
    /**
     * Gets the current number of lives the hero has.
     * @return The current lives count.
     */
    public int getLives() { return nLives.get();}
    
    /**
     * Adds a specified value to the current score.
     * @param value The value to add to the current score.
     */
    public void addScore(int value) { nscore.set(nscore.get() + value); }
    
    /**
     * Sets the hero's position to the specified coordinates.
     * @param x The new X coordinate.
     * @param y The new Y coordinate.
     */
    public void setPosition(int x, int y) {
        this.positionX = x;
        this.positionY = y;
    }

    /**
     * Sets the hero's score to a specific value.
     * @param score The new score value.
     */
    public void setScore(int score){nscore.set(score);}

    /**
     * Sets the hero's lives to a specific value.
     * @param lives The new lives count.
     */
    public void setLives(int lives){nLives.set(lives);}
}