import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class GameFrame extends JFrame {
    private GameLogic gameLogic;
    private JTextField guessField;
    private JButton guessButton;
    private JLabel feedbackLabel;
    private JLabel attemptsLabel;
    private Clip backgroundMusic;

    public GameFrame() {
        setTitle("Guess the Number Game");
        setSize(600, 400); // Initial smaller window size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout()); // Use GridBagLayout for flexible component placement

        gameLogic = new GameLogic();

        guessField = new JTextField(10);
        guessButton = new JButton("Guess");
        feedbackLabel = new JLabel("Enter a number between 1 and 100:");
        attemptsLabel = new JLabel("Attempts left: 3");

        // Set fonts and colors for the labels
        feedbackLabel.setFont(new Font("Arial", Font.BOLD, 20));
        feedbackLabel.setForeground(Color.BLUE);
        attemptsLabel.setFont(new Font("Arial", Font.BOLD, 18));
        attemptsLabel.setForeground(Color.RED);

        // Set colors for the input panel and button
        guessButton.setBackground(Color.GREEN);
        guessButton.setForeground(Color.BLACK);
        guessButton.setPreferredSize(new Dimension(100, 40)); // Increase button size

        guessField.setPreferredSize(new Dimension(200, 40)); // Increase text box size

        // Use GridBagConstraints for flexible component placement
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(feedbackLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(guessField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        add(guessButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(attemptsLabel, gbc);

        guessButton.addActionListener(new GuessButtonListener());

        // Add a component listener to adjust layout on resize
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                adjustLayout();
            }
        });

        // Play background music
        playBackgroundMusic("m1.wav");
    }

    private void adjustLayout() {
        // Adjust component sizes based on the new window size
        Dimension size = getSize();
        int fontSize = size.width / 30;
        feedbackLabel.setFont(new Font("Arial", Font.BOLD, fontSize));
        attemptsLabel.setFont(new Font("Arial", Font.BOLD, fontSize - 2));

        int buttonWidth = size.width / 6;
        int buttonHeight = size.height / 12;
        guessButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));

        int textFieldWidth = size.width / 3;
        int textFieldHeight = size.height / 12;
        guessField.setPreferredSize(new Dimension(textFieldWidth, textFieldHeight));

        revalidate();
        repaint();
    }

    private void playBackgroundMusic(String filePath) {
        try {
            File audioFile = new File(getClass().getResource(filePath).toURI());
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioStream);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY); // Play music continuously
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private class GuessButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int guess = Integer.parseInt(guessField.getText());
                String feedback = gameLogic.checkGuess(guess);
                feedbackLabel.setText(feedback);
                attemptsLabel.setText("Attempts left: " + (3 - gameLogic.getAttempts()));

                if (gameLogic.isGameWon() || gameLogic.isGameOver()) {
                    String message = gameLogic.isGameWon() ? "You've won!" : "Game over! The correct number was " + gameLogic.getTargetNumber();
                    int response = JOptionPane.showConfirmDialog(
                            GameFrame.this,
                            message + " Would you like to play again?",
                            "Game Over",
                            JOptionPane.YES_NO_OPTION
                    );
                    if (response == JOptionPane.YES_OPTION) {
                        gameLogic.resetGame();
                        feedbackLabel.setText("Enter a number between 1 and 100:");
                        attemptsLabel.setText("Attempts left: 5");
                        guessField.setText("");
                    } else {
                        System.exit(0);
                    }
                }
            } catch (NumberFormatException ex) {
                feedbackLabel.setText("Please enter a valid number.");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameFrame gameFrame = new GameFrame();
            gameFrame.setVisible(true);
        });
    }
}
