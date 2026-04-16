package Control;
import javafx.animation.AnimationTimer;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
//import javafx.scene.input.KeyEvent; // New Import for Input
import java.util.ArrayList;        // New Import for Lists
import java.util.HashSet;          // New Import for Input Set
import java.util.Set;              // New Import for Input Set
import Model.*;
import View.*;

public class MainController {
    private Stage stage;
    private MainView view;
    private Map gameMap;
    private int level = 1;
    /** MODEL FIELDS*/
    private Hero player;
    private Treasure treasure;
    private ArrayList<Enemy> enemies;
    private ArrayList<Snake> snakes;

    private ArrayList<Bomb> bombs;
    private ArrayList<Projectile> projectiles;
    private ArrayList<PowerUp> powerUps;

    private AnimationTimer gameTimer;


    // ============== CONTROL FIELDS ==============
    private Set<String> activeKeys = new HashSet<>();
    private long lastEnemyMoveTime = 0;
    private final long ENEMY_MOVE_DELAY = 1_000_000_000; // 1 second in nanoseconds

    public MainController(Stage stage){
        this.stage = stage;
        this.view = new MainView();
    }

    public void startGame() {
        loadLevel(level);
        //Set up Input Handling
        setupInputHandlers();
        gameTimer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            // Check if enough time has passed for enemy movement
            if (now - lastEnemyMoveTime >= ENEMY_MOVE_DELAY) {
                handleEnemyTurn();
                lastEnemyMoveTime = now;
            }

            handleHeroInput(); // Handle hero movement/bomb placement
            
            // Core game cycle methods
            maintainBombVisuals();
            view.clearCanvas();
            view.renderMap(gameMap);
            view.renderHero(player); 
            view.renderEnemies(enemies);
            view.renderSnakes(snakes);
            view.renderBombs(bombs);
            view.renderProjectiles(projectiles); 
            view.renderPowerUps(powerUps);
            view.updateInfo(player.getLives(), player.getScore(), level);
            checkGameState(); // <--- Check winning/losing condition here
            cleanupEntities(); // Remove dead/inactive entities
        }
    };
    stage.show();
    gameTimer.start(); // <-- Start the loop

    
    stage.setScene(view.getScene());
    stage.setTitle("Strip Mining..."); // etc.
    }

    private void setupInputHandlers(){
        //Add pressed Keys to the set
        view.getScene().setOnKeyPressed(e -> {
            activeKeys.add(e.getCode().toString());
        });
        // Remove released keys from the set
        view.getScene().setOnKeyReleased(e -> {
            activeKeys.remove(e.getCode().toString());
        });
    }
    /**
     * Initializes all Model objects by scanning the map grid.
     */
    private void loadLevel(int level) {

        // 1. Save Player State if moving to the next level
        int currentScore = (player != null) ? player.getScore() : 0;
        int currentLives = (player != null) ? player.getLives() : 3;

        //Initialize Model components
        enemies = new ArrayList<>();
        snakes = new ArrayList<>();
        bombs = new ArrayList<>();
        projectiles = new ArrayList<>();
        powerUps = new ArrayList<>();

        //load map from file
        gameMap = new Map();
        gameMap.loadFile("Maps/level" + level + ".txt");

        char grid[][] = gameMap.getGrid();
        for(int y = 0; y < gameMap.getHeight(); y++){
            for(int x = 0; x < gameMap.getWidth(); x++){
                
                switch(grid[y][x]){
                    case 'H': 
                        player = new Hero("Player");
                        player.setPosition(x,y);
                        player.setScore(currentScore);
                        player.setLives(currentLives);
                        break;
                    case 'E':
                        Enemy enemy = new Enemy("Enemy");
                        enemy.setPosition(x,y);
                        enemies.add(enemy);
                        break;
                    case 'S':
                        Snake snake = new Snake("Snake");
                        snake.setPosition(x,y);
                        snakes.add(snake);
                        break;
                    case 'T':
                        treasure = new Treasure(x,y);
                        break;

                    case 'P':
                        PowerUp powerUp = new PowerUp(x,y);
                        powerUps.add(powerUp);
                        gameMap.setCell(x, y, ' ');
                        break;
                    case 'L':
                        ExtraLife exL = new ExtraLife(x,y);
                        powerUps.add(exL);
                        gameMap.setCell(x, y, ' ');
                        break;
                    default:
                        break;
                }
            }
        }
        //Render Initial Map
        view.renderMap(gameMap);
    }
    
    /**
     * Updates the game state, including player and enemy movements.
     */
    /*
    private void update(long now) {
        // Handle Player Movement
        handleHeroInput();
        //Enemy Movement
        if(now - lastEnemyMoveTime > ENEMY_MOVE_DELAY){
            handleEnemyTurn();
            lastEnemyMoveTime = now;
        }
        //Ensure bomb visuals are maintained against entity movement.
        maintainBombVisuals();

        //Check the WinLoss Conditions
        checkGameState();
        //Remove inactive entities
        cleanupEntities();
        
        // 5. Render/Update View
        // NOTE: You will need to add new methods to MainView for rendering entities
        view.clearCanvas(); // A new necessary method to clear the canvas before redrawing
        view.renderMap(gameMap);
        view.renderHero(player); 
        view.renderEnemies(enemies);
        view.renderSnakes(snakes);
        view.renderBombs(bombs);
        view.renderProjectiles(projectiles); 
        view.updateInfo(player.getLives(), player.getScore(), level);
        view.renderPowerUps(powerUps); 
    } */
 //HELPER METHODS
    private void handleHeroInput() {
        if (!player.getIsAlive()) return;

        // Process a single move command per frame (prioritizing movement keys)
        if (activeKeys.contains("W")) {
            player.Move('w', gameMap, enemies, snakes);
            activeKeys.remove("W"); // <--- CONSUME THE INPUT

        } else if (activeKeys.contains("S")) {
            player.Move('s', gameMap, enemies, snakes);
            activeKeys.remove("S"); // <--- CONSUME THE INPUT

        } else if (activeKeys.contains("A")) {
            player.Move('a', gameMap, enemies, snakes);
            activeKeys.remove("A"); // <--- CONSUME THE INPUT

        } else if (activeKeys.contains("D")) {
            player.Move('d', gameMap, enemies, snakes);
            activeKeys.remove("D"); // <--- CONSUME THE INPUT
            
        } else if (activeKeys.contains("F")) {
            // Hero places a bomb
            Bomb newBomb = player.PlaceBomb(gameMap, enemies, snakes);
            if (newBomb != null) {
                bombs.add(newBomb);
            }
            // Remove the 'F' key immediately after use to prevent placing multiple bombs per frame
            activeKeys.remove("F"); 
        }
        
        // After moving, check if the hero collected the treasure
        treasure.collect(player, enemies, snakes);

        for (PowerUp pu : powerUps) {
        if (!pu.getCollected()) {
                pu.collect(player); // The collect method handles collision and applies the effect
            }
        }
    }

    private void handleEnemyTurn(){
        // Process all normal enemies
        for(Enemy enemy : enemies){
            if(enemy.isAlive()){
                enemy.Move(gameMap);
                enemy.Attack(player);
            }
        }

        // Process all snake enemies separately
        for(Snake snake : snakes){
            if(snake.isAlive()){
                snake.Move(gameMap);
                
                // Snake's attack logic (Spit)
                Projectile newProjectile = snake.Spit(player, gameMap);
                if (newProjectile != null) {
                    projectiles.add(newProjectile);
                }
            }
        }
    }

    private void checkGameState(){
        // Check for game over
        if(!player.getIsAlive()){
            stage.setTitle("Game Over - You Lost!");
            gameTimer.stop();
            view.renderEndScreen("GAME OVER", Color.RED);
        }
        // Check for level completion
        else if(treasure.getIsCollected()){
            level++;
            if(level <= 5){
                loadLevel(level);
                stage.setTitle("Level " + level);
            } else{
                stage.setTitle("Congratulations - You Won the Game!");
                gameTimer.stop();
                view.renderEndScreen("VICTORY", Color.GOLD);
            }

        }
    }
    private void cleanupEntities(){
        // Remove inactive enemies
        enemies.removeIf(enemy -> !enemy.isAlive());
        snakes.removeIf(snake -> !snake.isAlive());
        // Remove exploded bombs
        bombs.removeIf(bomb -> bomb.hasExploded());
        // Remove inactive projectiles
        projectiles.removeIf(projectile -> !projectile.isActive());
        powerUps.removeIf(pu -> pu.getCollected());
    }

    /**
     * Iterates through all active bombs and ensures their 'B' symbol 
     * remains on the map, overriding any concurrent entity movement.
     */
    private void maintainBombVisuals() {
        for (Bomb bomb : bombs) {
            // Only set 'B' if the bomb hasn't fully exploded (hasExploded == false).
            // Check also ensures we don't overwrite the '!' explosion marker.
            if (!bomb.hasExploded() && bomb.getMap().getCell(bomb.getPositionX(), bomb.getPositionY()) != '!') {
                bomb.getMap().setCell(bomb.getPositionX(), bomb.getPositionY(), 'B');
            }
        }
    }
}