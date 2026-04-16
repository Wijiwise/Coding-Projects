package Model;
import java.util.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
/**
 * Represents an explosive bomb that can be placed by the Hero to destroy enemies and breakable walls.
 * The bomb has a countdown timer and explodes after a set duration, affecting entities within its blast radius.
 * 
 * @author Luis Carlos / Nathan Ng
 * @version 2.0
 */
public class Bomb {
    //**time in milliseconds before the bomb explodes */
    private long timer; 
    //**X Coordinate of the Bomb */
    private int positionX;
    //**Y Coordinate of the Bomb */
    private int positionY;
    //**blast radius of the bomb */
    private int blastRadius; 
    //**Where the Bomb is place */
    private Map map; 
    //**Who placed the Bomb / can also get hit by the Bomb */
    private Hero hero;

    private boolean exploded = false;

    private Timeline countdown;

    // New Field
    private ArrayList<int[]> blastCoordinates = new ArrayList<>();


    //Constructor
    /**
     * Constructs a new {@code Bomb} at the specified position.
     * 
     * Each bomb has a fixed 3-second timer and a blast radius of 2.
     * 
     * 
     * @param x The x-coordinate where the bomb is placed.
     * @param y The y-coordinate where the bomb is placed.
     * @param map The game map where the bomb exists.
     * @param hero The hero who placed the bomb.
     */
    public Bomb(int x, int y, Map map, Hero hero){
        this.positionX = x;
        this.positionY = y;
        this.timer = 3000; // bomb explodes after 3 seconds
        this.map = map;
        this.hero = hero;
        this.blastRadius = hero.getBombBlastRadius();
        
    }
    //Methods:
    /**
     * Starts the bomb's countdown timer.
     *
     * @param enemies The list of active generic enemies in the level.
     * @param snakes The list of active snake enemies in the level.
     */
    public void countdown(ArrayList<Enemy> enemies, ArrayList<Snake> snakes){
        //decrease timer
        countdown = new Timeline(
            new KeyFrame(
                Duration.millis(timer), 
                e -> explode(enemies, snakes)
            )
        );
        countdown.play();
    }
    /**
     * Executes the bomb explosion, dealing damage to entities within the blast radius.
     *
     * When exploded, the bomb:
     * * Displays an explosion marker ('!') temporarily on the map
     * * Damages any enemies, snakes, or the hero in its radius
     * * Destroys breakable walls ('#')
     *
     * @param enemies The list of active generic enemies to check for damage.
     * @param snakes The list of active snake enemies to check for damage.
     */
    public void explode(ArrayList<Enemy> enemies, ArrayList<Snake> snakes){
        //Add logic to damage enemies, break walls, damage player
        map.setCell(positionX, positionY, '!');

        Timeline explosionDisplay = new Timeline(
            new KeyFrame(
                Duration.millis(1500), // Show the '!' for 1.5 seconds
                event -> {
                    // This code runs AFTER the visual timer expires
                    map.setCell(positionX, positionY, ' '); // Clear the explosion marker
                    applyBlastRadius(hero, map, enemies, snakes);
                }
            )

        );
        explosionDisplay.play();
        exploded = true;
    }

    /**
     * Applies the bomb’s blast effects to surrounding map cells.
     *
     * The explosion propagates outward in all four cardinal directions (up, down,
     * left, right) up to the blast radius, unless blocked by an unbreakable wall ('X').
     * 
     * 
     * Effects per target:
     * 
     *   '#'— Breakable wall destroyed (blast stops)
     *   'E' — Enemy killed
     *   'S' — Snake enemy killed
     *   'H' — Hero takes damage
     * 
     * 
     * @param hero The hero potentially affected by the blast.
     * @param map The game map to modify.
     * @param enemies The list of enemies that may be hit.
     * @param snakes The list of snake enemies that may be hit.
     */
    public void applyBlastRadius(Hero hero, Map map, ArrayList<Enemy> enemies, ArrayList<Snake> snakes) {
        // Directions: up, down, left, right
        int[][] directions = {
            {0, -1}, // up
            {0, 1},  // down
            {-1, 0}, // left
            {1, 0}   // right
        }; 

        // Clear and re-populate the list for rendering
        blastCoordinates.clear(); 
        // Add the center point
        blastCoordinates.add(new int[]{positionX, positionY});

        for (int[] dir : directions) {
            for (int i = 1; i <= blastRadius; i++) {
                int targetX = positionX + dir[0] * i;
                int targetY = positionY + dir[1] * i;

                if (!map.isValidPos(targetX, targetY))
                    break; // stop if out of bounds

                char cell = map.getCell(targetX, targetY);

                // Stop the blast if it hits an unbreakable wall
                if (cell == 'X') break;

                // Breakable wall
                if (cell == '#') {
                    map.setCell(targetX, targetY, ' ');
                    // Record the blast tile coordinate BEFORE applying effect, 
                    // but only if it's not a block that stops the blast
                    blastCoordinates.add(new int[]{targetX, targetY});
                    break; // explosion stops after breaking a wall
                }

                // Damage enemies
                if (cell == 'E') {
                    
                    for(Enemy e : enemies){
                        if(e.getX() == targetX && e.getY() == targetY && e.isAlive()){
                            e.Die(hero, map);
                        }
                    }
                    
                    continue;
                }

                if(cell == 'S'){
                    for (Snake snake : snakes) {
                    if (snake.getX() == targetX && snake.getY() == targetY && snake.isAlive()) {
                    snake.Die(hero, map);
                    }
                    }
                }

                // Damage player
                if (cell == 'H') {
                    hero.TakeDamage();

                    if (!hero.getIsAlive()) {
                    hero.Die();
                    }
                    continue;
                }
            }
        }
    }

    //Getters
    /**
     * Checks if the bomb has exploded.
     * @return true if the bomb has exploded, false otherwise
     */    
    public boolean hasExploded() { return exploded; }

    /**
     * Gets the X coordinate of the bomb.
     * @return The X coordinate of the bomb
     */
    public int getPositionX() { return positionX; }

    /**
     * Gets the Y coordinate of the bomb.
     * @return The Y coordinate of the bomb
     */
    public int getPositionY() { return positionY; }
    
    /**
     * Gets the Blast coordinates from the bombs explosion
     * @return The ArrayList coordinates of the bomb
    */
    public ArrayList<int[]> getBlastCoordinates(){
        return blastCoordinates;
    }

    /**
     * Gets the map where the bomb is placed.
     * @return The map of the bomb
     */
    public Map getMap() { return map; }
    }