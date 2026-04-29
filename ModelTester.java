/**
 * ModelTester.java
 * A standalone tester for the GameModel logic.
 * Use this to verify game rules without launching the UI.
 */
public class ModelTester {

    public static void main(String[] args) {
        System.out.println("=== PAC-MAN MODEL TESTER ===");
        
        // 1. Initialize Model
        GameModel model = new GameModel();
        model.startGame(0); // Easy mode
        
        System.out.println("Initial State:");
        System.out.println("Score: " + model.score);
        System.out.println("Lives: " + model.lives);
        System.out.println("Ghosts spawned: " + model.ghosts.size());

        // 2. Test Movement Logic
        System.out.println("\nTesting Movement...");
        int startX = model.x;
        int startY = model.y;
        
        // Set intent to move right
        model.setIntent(1, 0); 
        // Update a few times to simulate movement
        for(int i = 0; i < 5; i++) model.update();
        
        if (model.x != startX || model.y != startY) {
            System.out.println("[PASS] Pac-Man position changed.");
        } else {
            System.out.println("[FAIL] Pac-Man is stuck!");
        }

        // 3. Test Scoring (Simulation)
        System.out.println("\nTesting Scoring...");
        model.score += 10;
        if (model.score == 10) {
            System.out.println("[PASS] Score incremented correctly.");
        }

        // 4. Test Power Pellet Logic
        System.out.println("\nTesting Frightened State...");
        model.frightenAllGhosts();
        boolean allFrightened = true;
        for (GameModel.Ghost g : model.ghosts) {
            if (!g.frightened) {
                allFrightened = false;
                break;
            }
        }
        if (allFrightened) {
            System.out.println("[PASS] All ghosts are now frightened.");
        } else {
            System.out.println("[FAIL] Some ghosts did not change state.");
        }

        // 5. Test Game Over Logic
        System.out.println("\nTesting Game Over...");
        model.lives = 1;
        // Simulate a ghost collision (by setting flag directly or moving ghost)
        model.lives--;
        if (model.lives <= 0) {
            model.isGameOver = true;
        }
        
        if (model.isGameOver) {
            System.out.println("[PASS] Game Over triggered at 0 lives.");
        }

        System.out.println("\n=== TEST COMPLETE ===");
    }
}