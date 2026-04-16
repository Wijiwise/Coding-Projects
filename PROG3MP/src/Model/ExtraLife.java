package Model;

/**
 * Represents a power-up item in the game.
 * This is an extra life, this class will increase the hero's life by 1 when collected.
 * @author Luis Carlos / Nathan Ng
 * @version 1.0 
 */
public class ExtraLife extends PowerUp {
    /**
     * Constructs a new ExtraLife power-up at the specified map coordinates.
     *
     * This calls the constructor of the superclass PowerUp to set the initial position.
     *
     * @param x The initial x-coordinate of the power-up on the map.
     * @param y The initial y-coordinate of the power-up on the map.
     */
    public ExtraLife(int x, int y){
        super(x, y);
    }
    /**
     * Attempts to collect the Extra Life power-up by the Hero.
     *
     * If the hero's position matches the power-up's position:
     * * The power-up's status is set to collected.
     * * The hero's life count is increased by 1 via Hero.increaseLife(int).
     * * The method returns true.
     *
     * @param hero The hero attempting to collect this power-up.
     * @return true if the hero successfully collected the power-up, false otherwise.
     */
    @Override
    public boolean collect(Hero hero) {
        // Use the fields inherited from the base PowerUp class (positionX, positionY, isCollected)
        
        if (hero.getPositionX() == super.getPositionX() && hero.getPositionY() == super.getPositionY()) {
            
            super.setIsCollected(true);
            
            hero.increaseLife(1); 
            
            return true;
        }
        return false;
    }

}
