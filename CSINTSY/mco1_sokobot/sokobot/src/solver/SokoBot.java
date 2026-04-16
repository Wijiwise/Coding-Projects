package solver;
import java.util.*;

public class SokoBot {
  // Helper class to represent a state
  static class State {
    static char[][] sharedMapData;  // Shared across all states (never changes)
    char[][] itemsData;             // Dynamic objects (player, boxes)
    int playerX;                    // Player X position
    int playerY;                    // Player Y position
    String moves;                   // Path to this state
    int cost;                       // Cost to reach this state from start
    int heuristic;                  // Estimated cost to reach goal
    
    // Constructor
    public State(char[][] itemsData, int playerX, int playerY, String moves, int cost) {
      this.itemsData = itemsData;
      this.playerX = playerX;
      this.playerY = playerY;
      this.moves = moves;
      this.cost = cost;
      this.heuristic = 0;
    }
  }
  
  // Main Method to solve the puzzle
  public String solveSokobanPuzzle(int width, int height, char[][] mapData, char[][] itemsData) {
    try {
      // Set shared map data once (all states will reference this)
      State.sharedMapData = mapData;
      
      // 1. Find all goal positions
      List<int[]> goalPositions = findGoalPositions(height, width, mapData);
      
      // 2. Create the initial State
      State initialState = initialState(width, height, itemsData, "", 0);
      
      // 3. Perform A* Search
      String solution = aStarSearch(initialState, goalPositions);
      return solution;
      
    } catch (Exception e) {
      e.printStackTrace();
      return "";
    }
  }
  
  // Find all goal positions in the map
  private List<int[]> findGoalPositions(int height, int width, char[][] mapData) {
    List<int[]> goalPositions = new ArrayList<>();
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        if (mapData[i][j] == '.' || mapData[i][j] == '*') {
          goalPositions.add(new int[]{j, i});
        }
      }
    }
    return goalPositions;
  }
  
  // Method to Create the initial state of the Puzzle
  public static State initialState(int width, int height, char[][] itemsData, String moves, int cost) {
    // Initialize player positions
    int playerX = -1;
    int playerY = -1;
    
    // Find the player position
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        if (itemsData[i][j] == '@') {
          playerX = j;
          playerY = i;
        }
      }
    }
    
    // Returns the initial state of the puzzle
    return new State(itemsData, playerX, playerY, moves, cost);
  }
  
  // Method to check if the current state is the goal state
  public static boolean isGoalState(State state, List<int[]> goalPositions) {
    for (int[] pos : goalPositions) {
      if (state.itemsData[pos[1]][pos[0]] != '*') {
        return false;
      }
    }
    return true;
  }
  
  // Method to copy states (only copies itemsData now, not mapData)
  public static State copyState(State state) {
    // Only clone itemsData (mapData is shared and never changes)
    char[][] newItemsData = new char[state.itemsData.length][];
    for (int i = 0; i < state.itemsData.length; i++) {
      newItemsData[i] = state.itemsData[i].clone();
    }
    
    // Return new state (uses shared mapData)
    return new State(newItemsData, state.playerX, state.playerY, state.moves, state.cost);
  }
  //Deadlock dectection method to avoid  
  public static boolean isDeadlock(int x, int y, State state){

    if(state.sharedMapData[y][x]=='.') return false; // Box on goal = not deadlock
    //Check for corner Deadlocks
    boolean upWall = (state.sharedMapData[y-1][x] == '#');
    boolean downwall = (state.sharedMapData[y+1][x] == '#');
    boolean leftWall = (state.sharedMapData[y][x-1] == '#');
    boolean rightWall = (state.sharedMapData[y][x+1] == '#');

    return (upWall && leftWall) || (upWall && rightWall) || (downwall && leftWall) || (downwall && rightWall);
  }
  // Method to make a move and return new state
  public static State makeMove(State current, int newX, int newY, String direction) {
    // Create a copy of the current state
    State newState = copyState(current);
    
    // Check what's at the new position where player wants to move
    char itemAtNewPos = newState.itemsData[newY][newX];
    
    // If there's a box at the new position, we need to push it
    if (itemAtNewPos == '$' || itemAtNewPos == '*') {
      // Calculate where the box will be pushed to
      int boxNewY = newY + (newY - current.playerY);
      int boxNewX = newX + (newX - current.playerX);
      
      // Remove the box from its current position
      newState.itemsData[newY][newX] = ' ';
      
      // Place the box at its new position
      // Check if the new position is a goal spot
      if (State.sharedMapData[boxNewY][boxNewX] == '.') {
        newState.itemsData[boxNewY][boxNewX] = '*'; // Box on goal
      } else {
        newState.itemsData[boxNewY][boxNewX] = '$'; // Box on empty floor
      }

      if(isDeadlock(boxNewX, boxNewY, newState)){
        return null; // Deadlock detected, discard this state
      }
    }
    
    // Move the player to the new position
    newState.playerX = newX;
    newState.playerY = newY;
    
    // Update the moves string and cost
    newState.moves += direction;
    newState.cost = current.cost + 1;
    
    return newState;
  }
  
  // Method to check if move is valid
  public static boolean isValidMove(State state, int newX, int newY) {
    // Check if new position is in bounds
    if (newX < 0 || newX >= State.sharedMapData[0].length 
        || newY < 0 || newY >= State.sharedMapData.length) {
      return false;
    }
    
    // Check if new position is a wall
    if (State.sharedMapData[newY][newX] == '#') {
      return false;
    }
    
    // Check if there's a box at new position
    char itemAtNewPos = state.itemsData[newY][newX];
    if (itemAtNewPos == '$' || itemAtNewPos == '*') {
      // Calculate where the box would be pushed
      int boxPushY = newY + (newY - state.playerY);
      int boxPushX = newX + (newX - state.playerX);
      
      // Check if box push destination is valid
      if (boxPushY < 0 || boxPushY >= State.sharedMapData.length || 
          boxPushX < 0 || boxPushX >= State.sharedMapData[0].length) {
        return false; // Box would go out of bounds
      }
      
      if (State.sharedMapData[boxPushY][boxPushX] == '#') {
        return false; // Box would hit wall
      }
      
      char itemAtBoxDest = state.itemsData[boxPushY][boxPushX];
      if (itemAtBoxDest == '$' || itemAtBoxDest == '*') {
        return false; // Box would hit another box
      }
    }
    
    return true;
  }
  
  // Method to generate possible moves from the current state
  public static List<State> generateMoves(State state) {
    List<State> nextStates = new ArrayList<>();
    int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    String[] moves = {"u", "d", "l", "r"};
    
    for (int i = 0; i < 4; i++) {
      // Calculate new player position
      int newY = state.playerY + directions[i][0]; // row change
      int newX = state.playerX + directions[i][1]; // column change
      
      if (isValidMove(state, newX, newY)) {
        State newState = makeMove(state, newX, newY, moves[i]);
        if (newState != null) {
          nextStates.add(newState);
        }
      }
    }
    return nextStates;
  }
  
  // Method to calculate heuristic
  private int calculateHeuristic(State state, List<int[]> goalPositions) {
    int heuristic = 0;
    
    // Find all box positions
    List<int[]> boxPositions = new ArrayList<>();
    for (int i = 0; i < state.itemsData.length; i++) {
      for (int j = 0; j < state.itemsData[i].length; j++) {
        if (state.itemsData[i][j] == '$' || state.itemsData[i][j] == '*') {
          boxPositions.add(new int[]{j, i});
        }
      }
    }
    
    // Calculate minimum Manhattan distance for each box to any goal
    for (int[] box : boxPositions) {
      int minDistance = Integer.MAX_VALUE;
      for (int[] goal : goalPositions) {
        int distance = Math.abs(box[0] - goal[0]) + Math.abs(box[1] - goal[1]);
        minDistance = Math.min(minDistance, distance);
      }
      if (minDistance != Integer.MAX_VALUE) {
        heuristic += minDistance;
      }
       //Penalty for boxes not on goals
      if(state.itemsData[box[1]][box[0]] == '$'){
          int x = box[0], y = box[1];
          if(isDeadlock(x, y, state)){
            heuristic += 1000; // Large penalty for deadlock positions
          } else {
            heuristic += 10; // Small penalty for boxes not on goals
          }
      }
    }
    return heuristic;
  }
  
  // Create unique string representation of state
  private String statetoString(State state) {
    StringBuilder sb = new StringBuilder();
    sb.append(state.playerX).append(",").append(state.playerY).append("|");
    
    List<String> boxPositions = new ArrayList<>();
    for (int i = 0; i < state.itemsData.length; i++) {
      for (int j = 0; j < state.itemsData[i].length; j++) {
        if (state.itemsData[i][j] == '$' || state.itemsData[i][j] == '*') {
          boxPositions.add(j + "," + i);
        }
      }
    }
    Collections.sort(boxPositions);
    for (String pos : boxPositions) {
      sb.append(pos).append(";");
    }
    
    return sb.toString();
  }
  
  // Method to perform A* Search
  private String aStarSearch(State initialState, List<int[]> goalPos) {
    // Priority queue ordered by f(n) = cost + heuristic
    PriorityQueue<State> theSet = new PriorityQueue<>((a, b) -> {
      int fA = a.cost + a.heuristic;
      int fB = b.cost + b.heuristic;
      return Integer.compare(fA, fB);
    });
    
    // Set to track already-explored states
    HashSet<String> explored = new HashSet<>();
    
    // Calculate heuristic for initial state
    initialState.heuristic = calculateHeuristic(initialState, goalPos);
    
    theSet.offer(initialState);
    
    // Main A* Loop
    while (!theSet.isEmpty()) {
      State current = theSet.poll();
      
      // Check visited immediately after polling
      String currentKey = statetoString(current);
      if (explored.contains(currentKey)) {
        continue;
      }
      explored.add(currentKey);
      
      // Check goal
      if (isGoalState(current, goalPos)) {
        return current.moves;
      }
      
      // Generate successors
      List<State> successors = generateMoves(current);
      for (State next : successors) {
        String nextKey = statetoString(next);
        if (!explored.contains(nextKey)) {
          next.heuristic = calculateHeuristic(next, goalPos);
          theSet.offer(next);
        }
      }
    }
    
    // No solution found
    return "";
  }
}