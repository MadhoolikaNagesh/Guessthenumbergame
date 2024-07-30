public class GameLogic {
    private int targetNumber;
    private int attempts;
    private final int maxAttempts = 3;
    private boolean gameWon;

    public GameLogic() {
        resetGame();
    }

    public void resetGame() {
        targetNumber = (int) (Math.random() * 100) + 1;
        attempts = 0;
        gameWon = false;
    }

    public String checkGuess(int guess) {
        attempts++;
        if (guess == targetNumber) {
            gameWon = true;
            return "Correct! You've guessed the number in " + attempts + " attempts.";
        } else if (guess < targetNumber) {
            return "Too low! " ;
        } else {
            return "Too high!  " ;
        }
    }

    public boolean isGameWon() {
        return gameWon;
    }

    public boolean isGameOver() {
        return attempts >= maxAttempts;
    }

    public int getAttempts() {
        return attempts;
    }

    public int getTargetNumber() {
        return targetNumber;
    }
}
