import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private GamePanel gamePanel;
    private InstructionPanel instructionPanel;

    public GameWindow(){
        setTitle("Delivery Dash");
        setSize(800,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        gamePanel = new GamePanel();
        instructionPanel = new InstructionPanel(this);

        mainPanel.add(instructionPanel, "Instructions");
        mainPanel.add(gamePanel, "Game");

        add(mainPanel);
        cardLayout.show(mainPanel,"Instructions");

        instructionPanel.playBackgroundMusic();
    }

    public void startGame() {
        cardLayout.show(mainPanel, "Game");
        instructionPanel.stopBackgroundMusic();
        SwingUtilities.invokeLater(() -> {
            gamePanel.startGame();
            gamePanel.requestFocusInWindow();
        });
    }

    public void showInstructionPanel() {
        cardLayout.show(mainPanel, "Instructions");
        instructionPanel.playBackgroundMusic();
    }
}
