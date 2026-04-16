package solver;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Queue;

public class DlockDetector {

    // ----- CORNER DEADLOCK DETECTION -----
    // Check if a box at (x,y) is in a corner deadlock
    public static boolean isCornerDLock(int x, int y, char[][] mapData, char[][] itemsData) {
        // Check if the position is a wall or a goal
        if (mapData[y][x] == '#' || mapData[y][x] == '.') {
            return false;
        }

        // Check for corner deadlock
        boolean wallAbove = (y > 0 && mapData[y - 1][x] == '#');
        boolean wallBelow = (y < mapData.length - 1 && mapData[y + 1][x] == '#');
        boolean wallLeft = (x > 0 && mapData[y][x - 1] == '#');
        boolean wallRight = (x < mapData[0].length - 1 && mapData[y][x + 1] == '#');

        // A corner deadlock occurs if there are walls on two adjacent sides
        return (wallAbove && wallLeft) || (wallAbove && wallRight) ||
               (wallBelow && wallLeft) || (wallBelow && wallRight);
    }

    // Check if any box is in a corner deadlock
    public static boolean hasCornerDLock(char[][] mapData, char[][] itemsData) {
        for (int y = 0; y < mapData.length; y++) {
            for (int x = 0; x < mapData[0].length; x++) {
                if (itemsData[y][x] == '$') {
                    if (isCornerDLock(x, y, mapData, itemsData)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // ----- DEAD SQUARES -----
    // Compute dead squares in the map
    // A dead square is a non-goal, non-wall square from which a box cannot be pushed to any goal.
    // Does reverse Breadth-First Search from all goals to find all reachable squares.
    public static boolean[][] computeDeadSquares(char[][] mapData) {
        int H = mapData.length;
        int W = mapData[0].length;
        boolean[][] reachable = new boolean[H][W];
        Queue<int[]> q = new ArrayDeque<>();

        // Start from all goals
        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                if (mapData[y][x] == '.') {
                    reachable[y][x] = true;
                    q.add(new int[]{x, y});
                }
            }
        }

        int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};

        // Reverse push propagation
        while (!q.isEmpty()) {
            int[] p = q.poll();
            int tx = p[0], ty = p[1];

            for (int[] d : dirs) {
                int dx = d[0], dy = d[1];
                // box source
                int sx = tx - dx;
                int sy = ty - dy;
                // player source (player must stand here)
                int ppx = sx - dx;
                int ppy = sy - dy;

                // Check bounds
                if (sx < 0 || sx >= W || sy < 0 || sy >= H) continue;
                if (ppx < 0 || ppx >= W || ppy < 0 || ppy >= H) continue;

                // Check walls
                if (mapData[sy][sx] == '#') continue;
                if (mapData[ppy][ppx] == '#') continue;

                // If not yet reachable, mark and enqueue
                if (!reachable[sy][sx]) {
                    reachable[sy][sx] = true;
                    q.add(new int[]{sx, sy});
                }
            }
        }

        // Mark dead squares (non-goal, non-wall, not reachable)
        boolean[][] dead = new boolean[H][W];
        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                if (mapData[y][x] == '#' || mapData[y][x] == '.') {
                    dead[y][x] = false;
                } else {
                    dead[y][x] = !reachable[y][x];
                }
            }
        }
        return dead;
    }

    // Check if any box is on a dead square
    public static boolean hasDeadSquareBox(char[][] mapData, char[][] itemsData, boolean[][] deadSquares) {
        for (int y = 0; y < itemsData.length; y++) {
            for (int x = 0; x < itemsData[0].length; x++) {
                if (itemsData[y][x] == '$') {
                    if (mapData[y][x] != '.' && deadSquares[y][x]) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // ----- 2x2 BLOCK DEADLOCK DETECTION -----
    // Check for any 2x2 block of boxes not all on goals
    public static boolean has2x2DLock(char[][] mapData, char[][] itemsData) {
        int height = mapData.length;
        int width = mapData[0].length;

        // Iterate through each possible top-left corner of a 2x2 block
        for (int i = 0; i < height - 1; i++) {
            for (int j = 0; j < width - 1; j++) {
                // Check if all four positions in the 2x2 block contain boxes
                if (itemsData[i][j] == '$' &&
                    itemsData[i][j+1] == '$' &&
                    itemsData[i+1][j] == '$' &&
                    itemsData[i+1][j+1] == '$') {

                    // Check if all four positions are on goals
                    boolean allOnGoals = 
                        (mapData[i][j] == '.') &&
                        (mapData[i][j+1] == '.') &&
                        (mapData[i+1][j] == '.') &&
                        (mapData[i+1][j+1] == '.');

                    if (!allOnGoals) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // ----- FREEZE DEADLOCK DETECTION -----
    // Check for freeze deadlock: two or more boxes aligned horizontally or vertically
    // with walls on both sides and not on goals.
    public static boolean hasFreezeDLock(char[][] mapData, char[][] itemsData) {
        int H = mapData.length, W = mapData[0].length;
        boolean[][] visited = new boolean[H][W];

        // Check horizontal and vertical lines of boxes
        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                if (visited[y][x] || itemsData[y][x] != '$' || mapData[y][x] == '.') continue;

                // Check horizontal line
                if (isWall(mapData, y - 1, x) || isWall(mapData, y + 1, x)) {
                    int left = x, right = x;
                    while (left > 0 && itemsData[y][left - 1] == '$' && mapData[y][left - 1] != '.') left--;
                    while (right < W - 1 && itemsData[y][right + 1] == '$' && mapData[y][right + 1] != '.') right++;

                    for (int i = left; i <= right; i++) visited[y][i] = true;

                    boolean leftBlocked = isWall(mapData, y, left - 1) || itemsData[y][left - 1] == '$';
                    boolean rightBlocked = isWall(mapData, y, right + 1) || itemsData[y][right + 1] == '$';
                    if (leftBlocked && rightBlocked) return true;
                }

                // Check vertical line
                if (isWall(mapData, y, x - 1) || isWall(mapData, y, x + 1)) {
                    int up = y, down = y;
                    while (up > 0 && itemsData[up - 1][x] == '$' && mapData[up - 1][x] != '.') up--;
                    while (down < H - 1 && itemsData[down + 1][x] == '$' && mapData[down + 1][x] != '.') down++;

                    for (int i = up; i <= down; i++) visited[i][x] = true;

                    boolean upBlocked = isWall(mapData, up - 1, x) || itemsData[up - 1][x] == '$';
                    boolean downBlocked = isWall(mapData, down + 1, x) || itemsData[down + 1][x] == '$';
                    if (upBlocked && downBlocked) return true;
                }
            }
        }
        return false;
    }

    // ----- EDGE-LINE DEADLOCK -----
    // Detects entire wall-adjacent lines (rows/columns) without goals.
    // If a box is pushed into such a line, it can never contribute to the solution.
    public static boolean[][] computeEdgeLineDeadlocks(char[][] mapData) {
        int H = mapData.length;
        int W = mapData[0].length;
        boolean[][] edgeDead = new boolean[H][W];

        // Check horizontal lines (against top/bottom walls)
        for (int y = 0; y < H; y++) {
            boolean hasGoal = false;
            for (int x = 0; x < W; x++) {
                if (mapData[y][x] == '.') {
                    hasGoal = true;
                    break;
                }
            }
            if (!hasGoal) {
                // Mark cells next to a wall line as dead
                for (int x = 0; x < W; x++) {
                    if (mapData[y][x] != '#' && mapData[y][x] != '.') {
                        // If adjacent to top or bottom wall
                        if ((y > 0 && mapData[y - 1][x] == '#') || (y < H - 1 && mapData[y + 1][x] == '#')) {
                            edgeDead[y][x] = true;
                        }
                    }
                }
            }
        }

        // Check vertical lines (against left/right walls)
        for (int x = 0; x < W; x++) {
            boolean hasGoal = false;
            for (int y = 0; y < H; y++) {
                if (mapData[y][x] == '.') {
                    hasGoal = true;
                    break;
                }
            }
            if (!hasGoal) {
                for (int y = 0; y < H; y++) {
                    if (mapData[y][x] != '#' && mapData[y][x] != '.') {
                        // If adjacent to left or right wall
                        if ((x > 0 && mapData[y][x - 1] == '#') || (x < W - 1 && mapData[y][x + 1] == '#')) {
                            edgeDead[y][x] = true;
                        }
                    }
                }
            }
        }

        return edgeDead;
    }    

    // Check if any box is on an edge-line deadlock position
    public static boolean hasEdgeLineDeadlockBox(char[][] mapData, char[][] itemsData, boolean[][] edgeDead) {
        for (int y = 0; y < itemsData.length; y++) {
            for (int x = 0; x < itemsData[0].length; x++) {
                if (itemsData[y][x] == '$' && edgeDead[y][x]) {
                    return true;
                }
            }
        }
        return false;
    }

    // ----- TUNNEL DEADLOCK -----
    // Marks 1-tile-wide corridors with no goals as deadlock zones.
    public static boolean[][] computeTunnelDeadlocks(char[][] mapData) {
        int H = mapData.length;
        int W = mapData[0].length;
        boolean[][] tunnelDead = new boolean[H][W];

        // Horizontal tunnels
        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                // Must be floor or box space, not goal or wall
                if (mapData[y][x] == '#' || mapData[y][x] == '.') continue;

                // Must have walls above and below (so it's a 1-tile-high tunnel)
                if ((y > 0 && mapData[y - 1][x] == '#') && (y < H - 1 && mapData[y + 1][x] == '#')) {
                    // Extend left and right to mark entire tunnel segment
                    boolean hasGoal = false;
                    int left = x;
                    while (left >= 0 && mapData[y][left] != '#') {
                        if (mapData[y][left] == '.') hasGoal = true;
                        left--;
                    }
                    int right = x;
                    while (right < W && mapData[y][right] != '#') {
                        if (mapData[y][right] == '.') hasGoal = true;
                        right++;
                    }

                    // If no goal found, mark entire segment as dead
                    if (!hasGoal) {
                        for (int i = left + 1; i < right; i++) {
                            if (mapData[y][i] != '#') tunnelDead[y][i] = true;
                        }
                    }
                }
            }
        }

        // Vertical tunnels
        for (int x = 0; x < W; x++) {
            for (int y = 0; y < H; y++) {
                if (mapData[y][x] == '#' || mapData[y][x] == '.') continue;

                // Must have walls left and right
                if ((x > 0 && mapData[y][x - 1] == '#') && (x < W - 1 && mapData[y][x + 1] == '#')) {
                    boolean hasGoal = false;
                    int top = y;
                    while (top >= 0 && mapData[top][x] != '#') {
                        if (mapData[top][x] == '.') hasGoal = true;
                        top--;
                    }
                    int bottom = y;
                    while (bottom < H && mapData[bottom][x] != '#') {
                        if (mapData[bottom][x] == '.') hasGoal = true;
                        bottom++;
                    }

                    if (!hasGoal) {
                        for (int j = top + 1; j < bottom; j++) {
                            if (mapData[j][x] != '#') tunnelDead[j][x] = true;
                        }
                    }
                }
            }
        }

        return tunnelDead;
    }

    // Check if any box is on a tunnel deadlock position
    public static boolean hasTunnelDeadlockBox(char[][] mapData, char[][] itemsData, boolean[][] tunnelDead) {
        for (int y = 0; y < itemsData.length; y++) {
            for (int x = 0; x < itemsData[0].length; x++) {
                if (itemsData[y][x] == '$' && tunnelDead[y][x]) {
                    return true;
                }
            }
        }
        return false;
    }

    // ----- Helper functions -----

    // Check if a position is a wall or out of bounds
    private static boolean isWall(char[][] map, int y, int x) {
        return y < 0 || y >= map.length || x < 0 || x >= map[0].length || map[y][x] == '#';
    }
    // ----- placeholder for future deadlock detection methods -----

    // Check for any type of deadlock
    public static boolean hasDeadlock(char[][] mapData, char[][] itemsData, boolean[][] deadSquares, boolean[][] edgeLineMap, boolean[][] tunnelMap) {
        // Fast local checks first
        if (hasCornerDLock(mapData, itemsData)) return true;
        if (has2x2DLock(mapData, itemsData)) return true;
        if (hasFreezeDLock(mapData, itemsData)) return true;

        // Static map-based checks
        if (hasDeadSquareBox(mapData, itemsData, deadSquares)) return true;
        if (hasEdgeLineDeadlockBox(mapData, itemsData, edgeLineMap)) return true;
        if (hasTunnelDeadlockBox(mapData, itemsData, tunnelMap)) return true;

        return false;
}
}


// Lists of deadlock detection methods tested and implemented:
// 1. Corner Deadlock Detection
// 2. Dead Squares
// 3. 2x2 Block Deadlock Detection
// 4. Freeze Deadlock Detection
// 5. --Corridor Deadlock Detection-- (dropped, time complexity issues)
// 6. --Cluster Deadlock Detection--  (dropped, time complexity issues)
// 7. Edge-Line Deadlock Detection
// 8. Tunnel Deadlock Detection
