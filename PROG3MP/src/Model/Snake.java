package Model;
/**
 * Represents a Snake enemy that extends the basic Enemy class.
 * In addition to normal enemy behavior, snakes can spit projectiles at the hero.
 * 
 * @author Luis Carlos / Nathan Ng
 * @version 1.0
 */
public class Snake extends Enemy {
    private Projectile shot;
    //Constructor\
    /**
     * Inherits the Contructor of Enemy
     * @param name : Tells the constructor the name of the instance
     */
    public Snake(String name) {
        super(name);
    }

    /**Spit Move: spits a projectile in the direction of the hero...
     * @param hero The hero to target with the spit attack.
     * @param map The game map to manage projectile movement.
     * @return The new Projectile created, or null if a projectile is already active.
     */
    public Projectile Spit(Hero hero, Map map){ 
        if(shot != null && shot.isActive()){
            return null; // Return null if a projectile is already active
        }
        int dx = Integer.compare(hero.getPositionX(), this.getX());
        int dy = Integer.compare(hero.getPositionY(), this.getY());
        
        // Creates the new projectile
        shot = new Projectile(this.getX(), this.getY(), dx, dy, 3, map, hero);
        shot.launch();
        
        return shot; 
    }

    /**
     * Marks the snake as dead, updates the map, and grants score to the hero.
     * This method overrides the Enemy's Die method.
     * @param hero The hero who defeated the snake.
     * @param map The game map to update.
     */
    @Override
    public void Die(Hero hero, Map map) {
        
        // 1. **CRITICAL:** Explicitly set the internal state to dead.
        // This ensures isAlive() returns false immediately for cleanup.
        this.setAlive(false); 
        
        // 2. Clear the snake's position on the map ('S' to ' ')
        map.setCell(this.getX(), this.getY(), ' '); 
        
        // 3. Grant the score (Using 100 points, consistent with your base Enemy score)
        hero.addScore(100); 

        // NOTE: We do not call super.Die() to prevent any state conflicts or double-clearing/scoring.
    }

    /**
     * Overrides Enemy.Move: moves the snake in a random direction.
     * CRITICAL: Snake objects must use 'S' for map cell representation.
     * @param map The game map to check for valid movement
     */
    @Override
    public void Move(Map map){
        int direction = (int)(Math.random() * 4); //0 = up, 1 = down, 2 = left, 3 = right
        int newX = this.getX();
        int newY = this.getY();

        switch(direction){
            case 0:
                newY--;
                break;
            case 1:
                newY++;
                break;
            case 2:
                newX--;
                break;
            case 3:
                newX++;
                break;
        }

        // Only move if position is walkable
        if (map.isValidPos(newX, newY) && map.isWalkable(newX, newY)) {
            map.setCell(this.getX(), this.getY(), ' '); // Clear old position
            this.setPosition(newX, newY);
            map.setCell(newX, newY, 'S'); // <-- Now correctly places 'S'
        }
    }
}