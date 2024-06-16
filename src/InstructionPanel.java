import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class InstructionPanel extends JPanel {
    private Clip backgroundMusic;
    private Image backgroundImage;

    public InstructionPanel(GameWindow gameWindow) {
        setLayout(null);

        loadImage();

        JLabel gameNameLabel = new JLabel("Delivery Dash");
        gameNameLabel.setFont(new Font("Arial", Font.BOLD, 75));
        gameNameLabel.setForeground(Color.WHITE);
        gameNameLabel.setBounds(220,100,500,100);
        add(gameNameLabel);

        JButton instructionsButton = new JButton("How to Play");
        instructionsButton.setBounds(250,400,300,80);
        instructionsButton.setFont(new Font("Arial", Font.BOLD, 40));
        instructionsButton.setBackground(Color.WHITE);
        instructionsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showInstructionsPopup();
            }
        });
        add(instructionsButton);

        JButton startButton = new JButton(new AbstractAction("Start") {
            public void actionPerformed(ActionEvent e){
                gameWindow.startGame();
            }
        });
        startButton.setBounds(250,300,300,80);
        startButton.setFont(new Font("Arial", Font.BOLD, 40));
        startButton.setBackground(Color.WHITE);
        add(startButton);

        initBackgroundMusic();
    }

    private void loadImage() {
        String imagePath = "resources/MainBackground.png";
        try {
            backgroundImage = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private void showInstructionsPopup(){
            JFrame popupFrame = new JFrame("Instructions");
            popupFrame.setSize(400,360);

            ImageIcon imageIcon = new ImageIcon("resources/HTP.png");
            JLabel imageLabel = new JLabel(imageIcon);
            popupFrame.add(imageLabel, BorderLayout.CENTER);

            JButton closeButton = new JButton("Close");
            closeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    popupFrame.dispose();
                }
            });
            popupFrame.add(closeButton, BorderLayout.SOUTH);

            popupFrame.setLocationRelativeTo(null);
            popupFrame.setVisible(true);
    }

    private void initBackgroundMusic() {
        try {
            File musicFile = new File("resources/StartMenu.wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(musicFile);
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioInputStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void playBackgroundMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
            backgroundMusic.start();
        }
    }

    public void stopBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
        }
    }

    public void removeNotify() {
        stopBackgroundMusic();
        super.removeNotify();
    }
}
