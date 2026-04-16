package Model;
/**
 * Represents a basic enemy character in the game that moves randomly and attacks the hero.
 * Enemies can be destroyed by bombs and grant points to the hero when defeated.
 * 
 * @author Luis Carlos / Nathan Ng
 * @version 2.0
 */
public class Enemy {
    /** The name of the enemy */
    private String hName;
    /** Tells whether enemy is still alive */
    private boolean isAlive;
    /** X coordinate of the enemy */
    private int positionX;
    /** Y coordinate of the enemy */
    private int positionY;

    //Constructor
    /**
     * Constructs a new Enemy with the specified name.
     * The enemy starts in an alive state.
     * 
     * @param name The name of the enemy
     */
    public Enemy(String name){
        this.hName = name;
        this.isAlive = true;
    }

    //Methods:
    /**Move Method: moves the enemy in a random direction
     * Enemy can only move in walkable positions on the map
     * @param map The game map to check for valid movement
    */
    public void Move(Map map){
        int direction = (int)(Math.random() * 4); //0 = up, 1 = down, 2 = left, 3 = right
        int newX = positionX;
        int newY = positionY;
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
            map.setCell(positionX, positionY, ' ');
            positionX = newX;
            positionY = newY;
            map.setCell(positionX, positionY, 'E');
        }
    }

    /**Attack Method: if the enemy is adjacent to the hero, it will attack and reduce the hero's lives by the enemy's damage value
     * @param hero The hero to attack
     * @return true if the attack was successful, false otherwise
    */
    public boolean Attack(Hero hero){
        int dx = Math.abs(hero.getPositionX() - this.positionX);
        int dy = Math.abs(hero.getPositionY() - this.positionY);

        if((dx==1 && dy ==0) || (dx==0 && dy==1)){
            hero.TakeDamage();
            return true;
        }
        return false;
    }
    /**Die Method: sets isAlive to false and removes the enemy from the game, ADD points to hero's score
     * @param hero The hero who defeated the enemy
     * @param map The game map to update
    */
    public void Die(Hero hero, Map map){
            isAlive = false;
            map.setCell(positionX, positionY, ' ');
            hero.addScore(100);
    }



    /**
     * Sets the alive status of the enemy.
     * This is needed because the 'isAlive' field is private.
     * @param status The new status (true for alive, false for dead).
     */
    public void setAlive(boolean status) {
        this.isAlive = status;
    }

    //Getters
    /**
     * Gets the name of the enemy.
     * @return The name of the enemy
     */
    public String getName() {
        return hName;
    }
    /**
     * Gets the X coordinate of the enemy.
     * @return The X coordinate of the enemy
     */
    public int getX(){
        return positionX;
    }
    /**
     * Gets the Y coordinate of the enemy.
     * @return The Y coordinate of the enemy
     */
    public int getY(){
        return positionY;
    }
    /**
     * Sets the position of the enemy.
     * @param x The new X coordinate
     * @param y The new Y coordinate
     */
    public void setPosition(int x, int y) {
    this.positionX = x;
    this.positionY = y;
    }
    /**
     * Checks if the enemy is alive.
     * @return true if the enemy is alive, false otherwise
     * This method is used by MainController for movement and cleanup.
     */
    public boolean isAlive() {
        return this.isAlive;
    }
}

