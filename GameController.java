import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class GameController extends JFrame {
    private GameModel model = new GameModel();
    private GameView  view;

    public GameController() {
        view = new GameView(model);
        add(view);
        pack();
        setTitle("Pac-Man - Ibrahim Abdi");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_P)     model.setPaused(!model.isPaused);
                if (key == KeyEvent.VK_R) {
                    model.resetGame(true);
                    view.resetToMenu();
                }
                if (key == KeyEvent.VK_UP)    model.setIntent( 0, -1);
                if (key == KeyEvent.VK_DOWN)  model.setIntent( 0,  1);
                if (key == KeyEvent.VK_LEFT)  model.setIntent(-1,  0);
                if (key == KeyEvent.VK_RIGHT) model.setIntent( 1,  0);
            }
        });

        new Timer(16, e -> {
            model.update();
            view.repaint();
        }).start();

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameController::new);
    }
}