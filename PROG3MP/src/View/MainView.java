package View;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane; // Used for a cleaner layout (Info bar + Canvas)
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.control.Label;
import java.util.ArrayList;
import Model.*;


public class MainView{
    private final int TILE_SIZE = 32;
    private Pane centerPane;
    private BorderPane root;
    private Canvas canvas;
    private GraphicsContext gc;
    private Scene scene;
    //Info labels
    private Label livesLabel;
    private Label scoreLabel;
    private Label levelLabel;
    //Sprite Images
    private Image UnbWallImg = new Image(getClass().getResourceAsStream("/Sprites/unbreakableWall.jpg"));
    private Image heroImg = new Image(getClass().getResourceAsStream("/Sprites/player.png"));
    private Image breakWallImg = new Image(getClass().getResourceAsStream("/Sprites/breakableWall.jpg"));
    private Image treasureImg = new Image(getClass().getResourceAsStream("/Sprites/Treasure.png"));
    private Image enemyImg = new Image(getClass().getResourceAsStream("/Sprites/enemy.png"));
    private Image snakeImg = new Image(getClass().getResourceAsStream("/Sprites/snake.png"));
    private Image bombImg = new Image(getClass().getResourceAsStream("/Sprites/bomb.png"));
    private Image explosionImg = new Image(getClass().getResourceAsStream("/Sprites/explosion.jpg"));
    private Image projectileImg = new Image(getClass().getResourceAsStream("/Sprites/projectile.jpg"));
    private Image dirtImg = new Image(getClass().getResourceAsStream("/Sprites/dirt.jpg"));
    private Image powerUp1Img = new Image(getClass().getResourceAsStream("/Sprites/PowerUp.png"));
    private Image extraLifeImg = new Image(getClass().getResourceAsStream("/Sprites/ExtraLife.png"));


    public MainView(){
        //Set canvas dimensions
        canvas = new Canvas(640, 480);
        gc = canvas.getGraphicsContext2D();

        livesLabel = new Label("Lives: 3");
        scoreLabel = new Label("Score: 0");
        levelLabel = new Label("Level: 1");

        Font infoFont = Font.font("Arial", FontWeight.BOLD, 14);
        livesLabel.setFont(infoFont);
        scoreLabel.setFont(infoFont);
        levelLabel.setFont(infoFont);
        HBox infoBar = new HBox(50, livesLabel, scoreLabel, levelLabel);
        infoBar.setStyle("-fx-background-color: #333; -fx-padding: 10;");
        livesLabel.setTextFill(Color.WHITE);
        scoreLabel.setTextFill(Color.WHITE);
        levelLabel.setTextFill(Color.WHITE);

        centerPane =  new Pane(canvas);
        root = new BorderPane();
        root.setCenter(centerPane);
        root.setTop(infoBar);

        scene = new Scene(root);
    }

    public Scene getScene() { return scene; }

    /**
     * Clears the canvas entirely before redrawing.
     */
    public void clearCanvas() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    /**
     * Renders the static and breakable map elements.
     */
    public void renderMap(Map map) {
        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x < map.getWidth(); x++) {
                char cell = map.getCell(x, y);
                double drawX = x * TILE_SIZE;
                double drawY = y * TILE_SIZE;
                
                // Draw floor tile first
                gc.drawImage(dirtImg, drawX, drawY, TILE_SIZE, TILE_SIZE);

                // Draw map elements on top of the floor
                if (cell == 'X') {
                    gc.drawImage(UnbWallImg, drawX, drawY, TILE_SIZE, TILE_SIZE);
                } else if (cell == '#') {
                    gc.drawImage(breakWallImg, drawX, drawY, TILE_SIZE, TILE_SIZE);
                } else if (cell == 'T') {
                    // NOTE: Treasure only remains 'T' in map grid until collected.
                    gc.drawImage(treasureImg, drawX, drawY, TILE_SIZE, TILE_SIZE);
                } else if(cell == 'P'){ //Powerup
                    gc.drawImage(powerUp1Img,drawX,drawY,TILE_SIZE, TILE_SIZE);
                } else if(cell == 'L'){ //ExtraLife
                    gc.drawImage(extraLifeImg,drawX, drawY,TILE_SIZE, TILE_SIZE);
                }
                
                // Note: 'H', 'E', 'S', 'B', '!', are handled by separate render methods
            }
        }
    }

    /**
     * Renders the hero at their current position.
     */
    public void renderHero(Model.Hero hero) {
        if (hero.getIsAlive()) {
            gc.drawImage(heroImg, hero.getPositionX() * TILE_SIZE, hero.getPositionY() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }
    }
    
    /**
     * Renders all active enemies (including Snakes).
     */
    public void renderEnemies(ArrayList<Enemy> enemies) {
        for (Enemy enemy : enemies) {
            if (enemy.isAlive()) {
                double drawX = enemy.getX() * TILE_SIZE;
                double drawY = enemy.getY() * TILE_SIZE;
                gc.drawImage(enemyImg, drawX, drawY, TILE_SIZE, TILE_SIZE);
        }
    }
}

    public void renderSnakes(ArrayList<Snake> snakes){
        for (Snake snake : snakes) {
            if (snake.isAlive()) {
                double drawX = snake.getX() * TILE_SIZE;
                double drawY = snake.getY() * TILE_SIZE;
                gc.drawImage(snakeImg, drawX, drawY, TILE_SIZE, TILE_SIZE);
            }
        }

    }
    
    /**
     * Renders all active bombs.
     */
    public void renderBombs(ArrayList<Bomb> bombs) {
        for (Bomb bomb : bombs) {
            // Check the map character to determine if it's a bomb ('B') or an explosion ('!')
            char cell = bomb.getMap().getCell(bomb.getPositionX(), bomb.getPositionY());
            double drawX = bomb.getPositionX() * TILE_SIZE;
            double drawY = bomb.getPositionY() * TILE_SIZE;
            
            if (cell == 'B') {
                gc.drawImage(bombImg, drawX, drawY, TILE_SIZE, TILE_SIZE);
            } else if (cell == '!') {
                // Draw the FULL BLAST RADIUS
                for (int[] coord : bomb.getBlastCoordinates()) {
                double bDrawX = coord[0] * TILE_SIZE;
                double bDrawY = coord[1] * TILE_SIZE;
                gc.drawImage(explosionImg, bDrawX, bDrawY, TILE_SIZE, TILE_SIZE);
            }
            // If the cell is ' ' or anything else, the bomb is done and will be cleaned up by the Controller.
        }
    }
}
    
    /**
     * Renders active projectiles (This relies on the Controller accurately tracking them).
     * NOTE: Since Projectile doesn't store itself in the Map grid, the Controller must pass the list.
     */
    public void renderProjectiles(ArrayList<Projectile> projectiles) {
        for (Projectile p : projectiles) {
            if (p.isActive()) {
                double drawX = p.getPositionX() * TILE_SIZE;
                double drawY = p.getPositionY() * TILE_SIZE;
                gc.drawImage(projectileImg, drawX, drawY, TILE_SIZE, TILE_SIZE);
            }
        }
    }

    /**
     * Renders all active, uncollected power-ups.
     * Checks the instance type to draw the correct sprite (ExtraLife vs regular PowerUp).
     * @param powerUps The list of active power-ups from the Controller.
     */
    public void renderPowerUps(ArrayList<PowerUp> powerUps) {
        for (PowerUp pu : powerUps) {
            // Only draw it if it hasn't been collected yet
            if (!pu.getCollected()) {
                double drawX = pu.getPositionX() * TILE_SIZE;
                double drawY = pu.getPositionY() * TILE_SIZE;

                // Use 'instanceof' to check the specific subclass type
                if (pu instanceof ExtraLife) {
                    gc.drawImage(extraLifeImg, drawX, drawY, TILE_SIZE, TILE_SIZE);
                } else {
                    // Default/Base PowerUp (assumed to be the Blast Radius PowerUp)
                    gc.drawImage(powerUp1Img, drawX, drawY, TILE_SIZE, TILE_SIZE);
                }
            }
        }
    }
    /**
     * Updates the score, lives, and level information displayed at the top of the window.
     */
    public void updateInfo(int lives, int score, int level) {
        livesLabel.setText("Lives: " + lives);
        scoreLabel.setText("Score: " + score);
        levelLabel.setText("Level: " + level);
    }

    /**
     * Renders a large text overlay for game end state (Game Over or Victory).
     * @param message The message to display (e.g., "GAME OVER")
     * @param color The color of the text.
     */
    public void renderEndScreen(String message, Color color) {
        // Clear the canvas first
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        
        // Set text properties
        gc.setFill(color);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        
        // Center the text (You may need to adjust canvas width/height access)
        double textWidth = gc.getFont().getSize() * message.length() / 2; // rough centering
        double centerX = (canvas.getWidth() / 2) - textWidth; 
        double centerY = canvas.getHeight() / 2;
        
        gc.fillText(message, centerX, centerY);
    }
}