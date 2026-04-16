package Model;
import java.io.*;

/**
 * Represents the game map/level, loaded from a text file.
 * Manages the grid layout, validates positions, and handles cell updates.
 * 
 * Map symbols:
 * - 'X' = Unbreakable wall
 * - '#' = Breakable wall
 * - ' ' = Empty space (walkable)
 * - 'H' = Hero starting position
 * - 'E' = Enemy starting position
 * - 'S' = Snake starting position
 * - 'T' = Treasure location
 * - 'P' = Power-up Location
 * - 'B' = Bomb location
 * 
 * @author Luis Carlos / Nathan Ng
 * @version 3.0
 */
public class Map {
    /** The height of the map (number of rows) */
    private int height;
    /** The width of the map (number of columns) */
    private int width;
    /** 2D representation of the map */
    private char[][] grid;

    /**Constructor to create an empty map */
    public Map(){
        this.height = 0;
        this.width = 0;
        this.grid = new char[0][0];
    
    }

    //Methods:
    /**FileLoad Method: loads a map from a text file
    Text file format: first line = height, second line = width, rest = map layout
    Breakable walls = '#', unbreakable walls = 'X', empty space = ' ', hero start = 'H', enemy start = 'E', treasure = 'T', snake = 'S'
    @param filename The name of the map file to load
    */
    public void loadFile(String filename) {
    try {
        // Ensure filename starts with '/' for classpath lookup
        if (!filename.startsWith("/")) {
            filename = "/" + filename;
        }

        InputStream in = getClass().getResourceAsStream(filename);
        if (in == null) {
            this.height = 0; // prevent null grid access
            return;
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        
        this.height = Integer.parseInt(br.readLine().trim());
        this.width = Integer.parseInt(br.readLine().trim());
        this.grid = new char[height][width];

        for (int i = 0; i < height; i++) {
            String line = br.readLine();
            if (line == null) break;
            for (int j = 0; j < width && j < line.length(); j++) {
                grid[i][j] = line.charAt(j);
            }
        }

        br.close();
        

    } catch (Exception e) {
        this.height = 0;
    }
}

    /**isValidPos : makes sure that the position the player moves to is within the map
     * @param x The x-coordinate to check
     * @param y The y-coordinate to check
     * @return true if the position is within bounds, false otherwise
    */
    public boolean isValidPos(int x, int y){
        return x>=0 && x< width && y>=0 && y<height; 
    }
    /**isWalkable : prevents enemies and heros from walking through walls 
      * @param x The x-coordinate to check
      * @param y The y-coordinate to check
      * @return true if the position is walkable, false otherwise
     */
    public boolean isWalkable(int x, int y) {
    if (!isValidPos(x, y)) return false;
    char cell = grid[y][x];
    return cell == ' ' || cell == 'H' || cell == 'E' || cell == 'T';
    }
    //Methods to update the map
    /**GetCell : reads the character at the curren position 
     * <p>
     * Walkable cells include:
     * <ul>
     *   <li>Empty space (' ')</li>
     *   <li>The hero's current position ('H')</li>
     *   <li>Enemy positions ('E')</li>
     *   <li>Treasure tiles ('T')</li>
     * </ul>
     * @param x The x-coordinate of the cell
     * @param y The y-coordinate of the cell
     * @return The character at the specified position, or 'X' if out of bounds
    */
    public char getCell(int x, int y){
       if(isValidPos(x,y)){
        return grid[y][x]; //Returns the character at that postion
       }
       return 'X';  //Returns X if out of bounds
    }
    /**SetCell : changes the character at x,y 
     * @param x The x-coordinate of the cell to update
     * @param y The y-coordinate of the cell to update
     * @param value The new character to set at the specified position
    */
    public void setCell(int x, int y, char value){
        if(isValidPos(x,y)){
            grid[y][x] = value;
        }
    }

    //Getters
    /**
     * Gets the entire map grid.
     * Gets the height and width of the map.
     * 
     * @return The 2D character array representing the map
     */
    public char[][] getGrid() {
        return grid;
    }
    /**
     * Gets the height of the map.
     * @return The height of the map
     */
    public int getHeight() {
        return height;
    }
    /**
     * Gets the width of the map.
     * @return The width of the map
     */
    public int getWidth() {
        return width;
    }
}
