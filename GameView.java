import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameView extends JPanel {
    private GameModel model;

    // Screen states: 0 = welcome, 1 = difficulty, 2 = game
    private int screen = 0;

    // Difficulty button rects
    private Rectangle easyBtn = new Rectangle(150, 320, 300, 55);
    private Rectangle medBtn  = new Rectangle(150, 395, 300, 55);
    private Rectangle hardBtn = new Rectangle(150, 470, 300, 55);

    public GameView(GameModel model) {
        this.model = model;
        setPreferredSize(new Dimension(600, 680));
        setBackground(Color.BLACK);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (screen == 0) {
                    screen = 1;
                    repaint();
                } else if (screen == 1) {
                    if (easyBtn.contains(e.getPoint())) { model.startGame(0); screen = 2; }
                    else if (medBtn.contains(e.getPoint()))  { model.startGame(1); screen = 2; }
                    else if (hardBtn.contains(e.getPoint())) { model.startGame(2); screen = 2; }
                    repaint();
                }
            }
        });
    }

    public void resetToMenu() { screen = 0; }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, getWidth(), getHeight());

        if      (screen == 0) drawWelcomeScreen(g2);
        else if (screen == 1) drawDifficultyScreen(g2);
        else                  drawGame(g2);
    }

    // ── Welcome screen ────────────────────────────────────────────────
    private void drawWelcomeScreen(Graphics2D g2) {
        g2.setColor(new Color(10, 10, 40));
        g2.fillRect(0, 0, getWidth(), getHeight());

        g2.setColor(Color.YELLOW);
        g2.setFont(new Font("Arial Black", Font.BOLD, 52));
        drawCenteredString(g2, "PAC-MAN", 160);

        Color[] dotColors = {Color.RED, Color.PINK, Color.CYAN, Color.ORANGE};
        for (int i = 0; i < 4; i++) {
            g2.setColor(dotColors[i]);
            g2.fillOval(190 + i * 60, 175, 18, 18);
        }

        g2.setColor(Color.YELLOW);
        g2.setStroke(new BasicStroke(2));
        g2.drawLine(100, 210, 500, 210);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        drawCenteredString(g2, "Created by Ibrahin Abdi", 255);
        g2.setColor(new Color(180, 180, 180));
        g2.setFont(new Font("Arial", Font.ITALIC, 16));
        drawCenteredString(g2, "Final Project For Brendan Shea", 285);

        g2.setColor(new Color(30, 30, 70));
        g2.fillRoundRect(80, 320, 440, 190, 20, 20);
        g2.setColor(Color.YELLOW);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(80, 320, 440, 190, 20, 20);

        g2.setColor(Color.YELLOW);
        g2.fillOval(110, 345, 20, 20);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.drawString("Eat all the dots to win!", 145, 360);
        
        g2.setColor(Color.RED);
        g2.fillOval(110, 390, 20, 20);
        g2.setColor(Color.WHITE);
        g2.drawString("Avoid the ghosts or lose a life", 145, 405);

        // Power pellet tip (new!)
    

        g2.setColor(Color.CYAN);
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.drawString("Arrow keys to move   |   P to pause   |   R to restart", 95, 475);

        g2.setColor(Color.YELLOW);
        g2.setFont(new Font("Arial", Font.BOLD, 22));
        drawCenteredString(g2, "CLICK ANYWHERE TO START", 560);
    }

    // ── Difficulty screen ─────────────────────────────────────────────
    private void drawDifficultyScreen(Graphics2D g2) {
        g2.setColor(new Color(10, 10, 40));
        g2.fillRect(0, 0, getWidth(), getHeight());

        g2.setColor(Color.YELLOW);
        g2.setFont(new Font("Arial Black", Font.BOLD, 32));
        drawCenteredString(g2, "SELECT DIFFICULTY", 120);

        g2.setColor(new Color(180, 180, 180));
        g2.setFont(new Font("Arial", Font.PLAIN, 16));
        drawCenteredString(g2, "Choose how hard you want it to be", 165);

        drawDiffButton(g2, easyBtn, "EASY",
            new Color(50, 160, 50), new Color(80, 220, 80),
            "2 ghosts  •  Long fright time");

        drawDiffButton(g2, medBtn, "MEDIUM",
            new Color(160, 120, 0), new Color(220, 180, 0),
            "3 ghosts  •  Medium fright time");

        drawDiffButton(g2, hardBtn, "HARD",
            new Color(160, 30, 30), new Color(220, 50, 50),
            "4 ghosts  •  Short fright time  •  Faster");
    }

    private void drawDiffButton(Graphics2D g2, Rectangle r, String label,
                                 Color bg, Color border, String subtitle) {
        g2.setColor(new Color(0, 0, 0, 100));
        g2.fillRoundRect(r.x + 4, r.y + 4, r.width, r.height, 16, 16);
        g2.setColor(bg);
        g2.fillRoundRect(r.x, r.y, r.width, r.height, 16, 16);
        g2.setColor(border);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(r.x, r.y, r.width, r.height, 16, 16);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial Black", Font.BOLD, 24));
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(label, r.x + (r.width - fm.stringWidth(label)) / 2, r.y + 30);
        g2.setColor(new Color(220, 220, 220));
        g2.setFont(new Font("Arial", Font.PLAIN, 13));
        fm = g2.getFontMetrics();
        g2.drawString(subtitle, r.x + (r.width - fm.stringWidth(subtitle)) / 2, r.y + 48);
    }

    // ── Game screen ───────────────────────────────────────────────────
    private void drawGame(Graphics2D g2) {
        // Maze tiles
        for (int r = 0; r < 20; r++) {
            for (int c = 0; c < 20; c++) {
                int cell = model.maze[r][c];
                if (cell == 1) {
                    g2.setColor(new Color(0, 0, 200));
                    g2.fillRect(c * 30, r * 30, 30, 30);
                    g2.setColor(new Color(50, 50, 255));
                    g2.drawRect(c * 30, r * 30, 30, 30);

                } else if (cell == 0) {
                    // Small dot
                    g2.setColor(Color.WHITE);
                    g2.fillOval(c * 30 + 13, r * 30 + 13, 4, 4);

                } else if (cell == 3) {
                    // ── Power pellet — large, bright circle ──────────
                    // Soft glow ring
                    g2.setColor(new Color(255, 255, 180, 80));
                    g2.fillOval(c * 30 + 6, r * 30 + 6, 18, 18);
                    // Solid white pellet
                    g2.setColor(Color.WHITE);
                    g2.fillOval(c * 30 + 9, r * 30 + 9, 12, 12);
                }
            }
        }

        // Pac-Man
        int angle = 45;
        if      (model.curDX > 0) angle = 45;
        else if (model.curDX < 0) angle = 225;
        else if (model.curDY > 0) angle = 315;
        else if (model.curDY < 0) angle = 135;
        g2.setColor(Color.YELLOW);
        g2.fillArc(model.x, model.y, 26, 26, angle, 270);

        // Ghosts
        Color[] normalColors = {Color.RED, Color.PINK, Color.CYAN, Color.ORANGE};
        for (int i = 0; i < model.ghosts.size(); i++) {
            GameModel.Ghost gh = model.ghosts.get(i);
            Color ghostColor;

            if (gh.frightened) {
                // Flash white when fright is almost over (last 80 ticks)
                boolean flashing = gh.frightenTimer < 80 && (gh.frightenTimer / 8) % 2 == 0;
                ghostColor = flashing ? Color.WHITE : new Color(30, 30, 210);
            } else {
                ghostColor = normalColors[i % 4];
            }

            drawGhost(g2, gh.x, gh.y, ghostColor, gh.frightened);
        }

        // HUD bar
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 600, 600, 80);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 15));
        g2.drawString("Score: " + model.score,                  10,  625);
        g2.drawString("Best: "  + model.highScore,              10,  648);
        g2.drawString("Lives: " + model.lives,                 180,  625);
        g2.drawString("Time: "  + model.elapsedSeconds() + "s", 320, 625);

        String[] diffs = {"EASY", "MEDIUM", "HARD"};
        Color[]  dc    = {new Color(80,200,80), new Color(220,180,0), new Color(220,50,50)};
        g2.setColor(dc[model.difficulty]);
        g2.drawString(diffs[model.difficulty], 460, 625);

        if (model.isPaused) {
            g2.setColor(Color.CYAN);
            g2.setFont(new Font("Arial", Font.BOLD, 18));
            g2.drawString("PAUSED", 180, 648);
        }

        if (model.isGameOver) {
            g2.setColor(new Color(0, 0, 0, 170));
            g2.fillRect(100, 220, 400, 140);
            g2.setColor(Color.RED);
            g2.setFont(new Font("Arial Black", Font.BOLD, 36));
            drawCenteredString(g2, "GAME OVER", 280);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 18));
            drawCenteredString(g2, "Press R to play again", 320);
        }
    }

    // ── Ghost renderer ────────────────────────────────────────────────
    // When frightened=true the body is already coloured by the caller;
    // we just swap the eye colour to make frightened ghosts look scared.
    private void drawGhost(Graphics2D g2, int gx, int gy, Color c, boolean frightened) {
        // Dome
        g2.setColor(c);
        g2.fillArc(gx, gy, 26, 26, 0, 180);
        // Body
        g2.fillRect(gx, gy + 13, 26, 10);
        // Wavy skirt
        int[] wx = {gx, gx+5, gx+9, gx+13, gx+17, gx+21, gx+26};
        int[] wy = {gy+23, gy+19, gy+23, gy+19, gy+23, gy+19, gy+23};
        g2.fillPolygon(wx, wy, 7);

        if (frightened) {
            // Scared face: simple squiggly mouth, no pupils
            g2.setColor(new Color(200, 200, 255));
            g2.fillOval(gx + 5,  gy + 8, 6, 6);
            g2.fillOval(gx + 15, gy + 8, 6, 6);
            // Wavy scared mouth
            g2.setColor(new Color(200, 200, 255));
            g2.setStroke(new BasicStroke(2));
            int[] mx = {gx+6, gx+9, gx+13, gx+17, gx+20};
            int[] my = {gy+18, gy+16, gy+18, gy+16, gy+18};
            g2.drawPolyline(mx, my, 5);
            g2.setStroke(new BasicStroke(1));
        } else {
            // Normal eyes
            g2.setColor(Color.WHITE);
            g2.fillOval(gx + 5,  gy + 7, 8, 8);
            g2.fillOval(gx + 13, gy + 7, 8, 8);
            g2.setColor(new Color(0, 0, 180));
            g2.fillOval(gx + 7,  gy + 9, 4, 4);
            g2.fillOval(gx + 15, gy + 9, 4, 4);
        }
    }

    private void drawCenteredString(Graphics2D g2, String s, int y) {
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(s, (getWidth() - fm.stringWidth(s)) / 2, y);
    }
}