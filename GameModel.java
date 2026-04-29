import java.util.ArrayList;
import java.util.Random;

public class GameModel {
    public final int TILE_SIZE = 30;
    public final int GRID_SIZE = 20;
    public int score = 0, highScore = 0, lives = 3;
    public boolean isGameOver = false, isPaused = false;
    public boolean gameStarted = false;

    // Difficulty: 0=easy, 1=medium, 2=hard
    public int difficulty = 0;

    // Timer fields
    private long startTime  = 0;
    private long pausedAt   = 0;
    private long pausedTotal = 0;

    public int x = 30, y = 30, curDX = 0, curDY = 0, nextDX = 0, nextDY = 0;
    public ArrayList<Ghost> ghosts = new ArrayList<>();

    // Flag to safely reset AFTER the ghost loop finishes
    private boolean needsReset = false;

    // ── Maze ─────────────────────────────────────────────────────────
    // Cell values:
    //   0 = dot (small)
    //   1 = wall
    //   2 = eaten / empty floor
    //   3 = power pellet (big) — placed randomly each new game
    public int[][] maze = {
        {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
        {1,0,0,0,0,1,0,0,0,0,0,0,0,0,1,0,0,0,0,1},
        {1,0,1,1,0,1,0,1,1,1,1,1,1,0,1,0,1,1,0,1},
        {1,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,1},
        {1,1,1,1,0,1,1,1,0,1,1,0,1,1,1,0,1,1,1,1},
        {2,2,2,1,0,1,0,0,0,0,0,0,0,0,1,0,1,2,2,2},
        {1,1,1,1,0,1,0,1,1,0,0,1,1,0,1,0,1,1,1,1},
        {0,0,0,0,0,0,0,1,2,2,2,2,1,0,0,0,0,0,0,0},
        {1,1,1,1,0,1,0,1,1,1,1,1,1,0,1,0,1,1,1,1},
        {2,2,2,1,0,1,0,0,0,0,0,0,0,0,1,0,1,2,2,2},
        {1,1,1,1,0,1,0,1,1,1,1,1,1,0,1,0,1,1,1,1},
        {1,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,1},
        {1,0,1,1,0,1,1,1,0,1,1,0,1,1,1,0,1,1,0,1},
        {1,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,1},
        {1,1,0,1,0,1,0,1,1,1,1,1,1,0,1,0,1,0,1,1},
        {1,0,0,0,0,1,0,0,0,0,0,0,0,0,1,0,0,0,0,1},
        {1,0,1,1,1,1,1,1,1,0,0,1,1,1,1,1,1,1,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
        {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2}
    };

    private int[][] originalMaze;

    // Spread-out spawn points — all far from Pac-Man start at (1,1)
    private int[][] spawnPoints = {
        { 9 * 30,  9 * 30},   // center
        {17 * 30,  1 * 30},   // top-right
        { 1 * 30, 17 * 30},   // bottom-left
        {17 * 30, 17 * 30}    // bottom-right
    };

    public GameModel() {
        originalMaze = new int[GRID_SIZE][GRID_SIZE];
        for (int r = 0; r < GRID_SIZE; r++)
            originalMaze[r] = maze[r].clone();
        resetGame(true);
    }

    public void startGame(int diff) {
        difficulty = diff;
        gameStarted = true;
        resetGame(true);
    }

    // ── Power-pellet placement ────────────────────────────────────────
    // Collects every dot cell (value 0), shuffles them, and upgrades
    // roughly 1-in-40 of them to value 3.  Pac-Man's starting tile is
    // excluded so you never immediately eat a pellet on spawn.
    private void placePowerPellets() {
        ArrayList<int[]> dotCells = new ArrayList<>();
        for (int r = 0; r < GRID_SIZE; r++)
            for (int c = 0; c < GRID_SIZE; c++)
                if (maze[r][c] == 0 && !(r == 1 && c == 1))
                    dotCells.add(new int[]{r, c});

        int pelletCount = Math.max(2, dotCells.size() / 40); // ≈ 3 pellets

        Random rand = new Random();
        for (int i = dotCells.size() - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            int[] tmp = dotCells.get(i);
            dotCells.set(i, dotCells.get(j));
            dotCells.set(j, tmp);
        }
        for (int i = 0; i < Math.min(pelletCount, dotCells.size()); i++) {
            int[] pos = dotCells.get(i);
            maze[pos[0]][pos[1]] = 3;
        }
    }

    // ── Frighten all ghosts ───────────────────────────────────────────
    // Resets each ghost's fright timer — even if they were already
    // frightened — so stacking pellets extends the effect.
    public void frightenAllGhosts() {
        for (Ghost g : ghosts) {
            g.frightened    = true;
            g.frightenTimer = g.frightenDuration;
        }
    }

    public void resetGame(boolean fullReset) {
        x = 30; y = 30;
        curDX = 0; curDY = 0;
        nextDX = 0; nextDY = 0;
        needsReset = false;

        // Restore dots on full reset, then scatter fresh power pellets
        if (fullReset) {
            for (int r = 0; r < GRID_SIZE; r++)
                maze[r] = originalMaze[r].clone();
            placePowerPellets(); // must come after clone so we start from clean 0s
        }

        ghosts.clear();
        int ghostCount = (difficulty == 0) ? 2 : (difficulty == 1) ? 3 : 4;
        for (int i = 0; i < ghostCount; i++) {
            int[] sp = spawnPoints[i % spawnPoints.length];
            ghosts.add(new Ghost(sp[0], sp[1]));
        }

        if (fullReset) {
            score      = 0;
            lives      = 3;
            isGameOver = false;
            startTime  = System.currentTimeMillis();
            pausedTotal = 0;
            pausedAt    = 0;
        }
    }

    public void setPaused(boolean paused) {
        if (paused && !isPaused) {
            pausedAt = System.currentTimeMillis();
        } else if (!paused && isPaused) {
            pausedTotal += System.currentTimeMillis() - pausedAt;
        }
        isPaused = paused;
    }

    public long elapsedSeconds() {
        if (isGameOver) return 0;
        long now = isPaused ? pausedAt : System.currentTimeMillis();
        return (now - startTime - pausedTotal) / 1000;
    }

    public void update() {
        if (!gameStarted || isPaused || isGameOver) return;

        // ── Tunnel wrap for Pac-Man ───────────────────────────────────
        if (x < 0) x = (GRID_SIZE - 1) * TILE_SIZE;
        if (x > (GRID_SIZE - 1) * TILE_SIZE) x = 0;

        // ── Pac-Man movement ──────────────────────────────────────────
        if (canMove(x + nextDX, y + nextDY)) { curDX = nextDX; curDY = nextDY; }
        if (canMove(x + curDX,  y + curDY))  { x += curDX;  y += curDY; }

        // ── Eating ───────────────────────────────────────────────────
        int gridX = (x + 15) / 30, gridY = (y + 15) / 30;
        if (gridY >= 0 && gridY < GRID_SIZE && gridX >= 0 && gridX < GRID_SIZE) {
            if (maze[gridY][gridX] == 0) {
                // Regular dot
                maze[gridY][gridX] = 2;
                score += 10;
                if (score > highScore) highScore = score;

            } else if (maze[gridY][gridX] == 3) {
                // Power pellet
                maze[gridY][gridX] = 2;
                score += 50;
                if (score > highScore) highScore = score;
                frightenAllGhosts();
            }
        }

        // ── Ghost loop — NEVER call resetGame() inside here ───────────
        for (Ghost g : ghosts) {
            g.move();

            if (Math.abs(x - g.x) < 20 && Math.abs(y - g.y) < 20) {
                if (g.frightened) {
                    // Pac-Man eats the ghost → teleport it home, resume hunt
                    g.eatAndRespawn();
                    score += 200;
                    if (score > highScore) highScore = score;
                } else {
                    // Normal ghost hits Pac-Man → lose a life
                    lives--;
                    if (lives <= 0) {
                        isGameOver = true;
                    } else {
                        needsReset = true; // flagged — handled after the loop
                    }
                }
            }
        }

        // ── Safe to reset now — outside the ghost loop ────────────────
        if (needsReset && !isGameOver) {
            resetGame(false);
        }
    }

    public boolean canMove(int nx, int ny) {
        int r1 = ny / 30,        c1 = nx / 30;
        int r2 = (ny + 25) / 30, c2 = (nx + 25) / 30;
        // Out-of-bounds → tunnel edge, allow it
        if (r1 < 0 || r2 >= GRID_SIZE || c1 < 0 || c2 >= GRID_SIZE) return true;
        // Only value 1 is a wall; 0, 2, 3 are all walkable
        return maze[r1][c1] != 1 && maze[r2][c2] != 1
            && maze[r1][c2] != 1 && maze[r2][c1] != 1;
    }

    public void setIntent(int dx, int dy) { nextDX = dx * 2; nextDY = dy * 2; }

    // ═════════════════════════════════════════════════════════════════
    //  Ghost inner class
    // ═════════════════════════════════════════════════════════════════
    class Ghost {
        int x, y, dx, dy;
        Random rand = new Random();
        int speed;
        int stuckCounter = 0;

        // Each ghost remembers its own spawn so eatAndRespawn() is exact
        int spawnX, spawnY;

        // Chase probability (0–100): Easy=25 %, Medium=60 %, Hard=90 %
        int chaseChance;

        // Fright — duration differs by difficulty so Hard is scarier:
        //   Easy   = 400 ticks ≈ 6.4 s  (timer at 16 ms/tick)
        //   Medium = 250 ticks ≈ 4.0 s
        //   Hard   = 150 ticks ≈ 2.4 s
        boolean frightened       = false;
        int     frightenTimer    = 0;
        int     frightenDuration;

        Ghost(int startX, int startY) {
            this.x      = startX;
            this.y      = startY;
            this.spawnX = startX;
            this.spawnY = startY;

            if (difficulty == 0)      { speed = 1; chaseChance = 25; frightenDuration = 400; }
            else if (difficulty == 1) { speed = 1; chaseChance = 60; frightenDuration = 250; }
            else                      { speed = 2; chaseChance = 90; frightenDuration = 150; }

            pickNewDirection();
        }

        // Teleport home, clear fright, pick a new direction.
        // Only touches this ghost's own fields — safe inside the loop.
        void eatAndRespawn() {
            x = spawnX;
            y = spawnY;
            frightened    = false;
            frightenTimer = 0;
            pickNewDirection();
        }

        // ── Chase: pick the open direction minimising Manhattan distance ─
        void chaseDirection() {
            int px = GameModel.this.x;
            int py = GameModel.this.y;

            int[][] dirs = {{speed,0},{-speed,0},{0,speed},{0,-speed}};
            int    bestDist = Integer.MAX_VALUE;
            int[]  bestDir  = null;

            // First pass: avoid exact reversal (prevents oscillation)
            for (int[] d : dirs) {
                if (d[0] == -dx && d[1] == -dy && (dx != 0 || dy != 0)) continue;
                int nx = x + d[0], ny = y + d[1];
                if (canMove(nx, ny)) {
                    int dist = Math.abs(nx - px) + Math.abs(ny - py);
                    if (dist < bestDist) { bestDist = dist; bestDir = d; }
                }
            }

            // Second pass (dead-end only): allow reversal
            if (bestDir == null) {
                for (int[] d : dirs) {
                    int nx = x + d[0], ny = y + d[1];
                    if (canMove(nx, ny)) {
                        int dist = Math.abs(nx - px) + Math.abs(ny - py);
                        if (dist < bestDist) { bestDist = dist; bestDir = d; }
                    }
                }
            }

            if (bestDir != null) { dx = bestDir[0]; dy = bestDir[1]; }
        }

        // ── Wander: random direction (Fisher-Yates shuffled) ─────────
        void pickNewDirection() {
            int[][] dirs = {{speed,0},{-speed,0},{0,speed},{0,-speed}};
            for (int i = 3; i > 0; i--) {
                int j = rand.nextInt(i + 1);
                int[] tmp = dirs[i]; dirs[i] = dirs[j]; dirs[j] = tmp;
            }
            for (int[] d : dirs) {
                if (canMove(x + d[0], y + d[1])) { dx = d[0]; dy = d[1]; return; }
            }
        }

        void move() {
            // ── Tunnel wrap (mirrors Pac-Man's wrap exactly) ──────────
            if (x < 0) x = (GRID_SIZE - 1) * TILE_SIZE;
            if (x > (GRID_SIZE - 1) * TILE_SIZE) x = 0;

            // ── Fright countdown ──────────────────────────────────────
            if (frightened) {
                frightenTimer--;
                if (frightenTimer <= 0) {
                    frightened    = false;
                    frightenTimer = 0;
                }
            }

            // ── Direction decision at tile grid boundaries ────────────
            if (x % 30 == 0 && y % 30 == 0) {
                if (frightened) {
                    // Scatter randomly every boundary when frightened
                    pickNewDirection();
                } else {
                    if (rand.nextInt(100) < chaseChance) {
                        chaseDirection();
                    } else if (rand.nextInt(10) < 4) {
                        pickNewDirection();
                    }
                }
            }

            // ── Move ──────────────────────────────────────────────────
            if (canMove(x + dx, y + dy)) {
                x += dx;
                y += dy;
                stuckCounter = 0;
            } else {
                stuckCounter++;
                if (stuckCounter > 2) {
                    if (!frightened && rand.nextInt(100) < chaseChance) {
                        chaseDirection();
                    } else {
                        pickNewDirection();
                    }
                    stuckCounter = 0;
                }
            }
        }
    }
}