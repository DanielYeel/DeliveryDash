import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GamePanel extends JPanel {
    private Character character;
    private List<Box> boxes;
    private Hole blueHole, pinkHole;
    private final int PANEL_WIDTH = 800;
    private final int PANEL_HEIGHT = 600;
    private final int NUM_BOXES = 18;
    private int boxesPlaced;
    private Clip backgroundMusic;
    private TimerThread timerThread;
    private long startTimeMillis;
    private boolean gameRunning;

    public GamePanel() {
        setFocusable(true);
        setBackground(Color.DARK_GRAY);
        this.character = new Character(100, 100, PANEL_WIDTH, PANEL_HEIGHT);

        this.blueHole = new Hole(100, 60, "resources/BlueHole.png", "blue");
        this.pinkHole = new Hole(600, 60, "resources/PinkHole.png", "pink");

        boxes = new ArrayList<>();
        this.gameRunning = false;

        this.addKeyListener(new KeyboardListener(character, this, boxes, blueHole, pinkHole));

        Thread gameThread = new Thread(() -> {
            while (true) {
                character.update();
                repaint();
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        initBackgroundMusic();
        gameThread.start();
    }


    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        blueHole.draw(g);
        pinkHole.draw(g);
        character.draw(g);

        for (Box box : boxes) {
            if (!character.isCarryingBox() || character.getCarriedBox() != box) {
                box.draw(g);
            }
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 30));

        if (gameRunning) {
            long currentTimeMillis = System.currentTimeMillis();
            long elapsedTimeSeconds = (currentTimeMillis - startTimeMillis) / 1000;
            int timeLeft = 100 - (int) elapsedTimeSeconds;

            if (timeLeft <= 0) {
                timeLeft = 0;
                endGame(false);
            }

            g.drawString("Time Left: " + timeLeft + " seconds", 10, 560);
            g.drawString("Boxes: " + boxesPlaced + "/" + NUM_BOXES, 10, 530);
        }
    }

    public void startGame(){
        if (!gameRunning){
            gameRunning = true;
            boxesPlaced = 0;
            boxes.clear();
            spawnBoxes();
            startTimeMillis = System.currentTimeMillis();
            timerThread = new TimerThread();
            timerThread.start();
            playBackgroundMusic();
            requestFocusInWindow();
        }
    }

    public void endGame(boolean timeUp) {
        gameRunning = false;
        stopBackgroundMusic();

        if (timerThread != null && timerThread.isAlive()) {
            timerThread.interrupt();
        }

        if (timeUp) {
            JOptionPane.showMessageDialog(this, "Time's up! You did not place all boxes.");
        } else {
            if (boxesPlaced == NUM_BOXES) {
                JOptionPane.showMessageDialog(this, "Congratulations! You placed all boxes in time!");
            } else {
                JOptionPane.showMessageDialog(this, "You did not place all boxes in time. Game over.");
            }
        }

        SwingUtilities.invokeLater(() -> {
            GameWindow gameWindow = (GameWindow) SwingUtilities.getWindowAncestor(this);
            gameWindow.showInstructionPanel();
        });

    }

    private void spawnBoxes() {
        Random rand = new Random();
        for (int i = 0; i < NUM_BOXES; i++) {
            int x,y;
            String type = (i < NUM_BOXES / 2) ? "blue" : "pink";
            boolean validPosition;
            do {
                validPosition = true;
                x = rand.nextInt(PANEL_WIDTH - 100);
                y= rand.nextInt(PANEL_HEIGHT / 2) + PANEL_HEIGHT / 3;

                if (new Rectangle(x, y, 40, 40).intersects(new Rectangle(blueHole.getX(), blueHole.getY(), blueHole.getWidth(), blueHole.getHeight())) ||
                        new Rectangle(x, y, 40, 40).intersects(new Rectangle(pinkHole.getX(), pinkHole.getY(), pinkHole.getWidth(), pinkHole.getHeight()))) {
                    validPosition = false;
                }
            } while (!validPosition);

            boxes.add(new Box(x, y, "resources/" + type.substring(0, 1).toUpperCase() + type.substring(1) + "Box.png", PANEL_WIDTH, PANEL_HEIGHT, type));

        }
    }

    public List<Box> getBoxes() {
        return boxes;
    }

    public void increaseBoxesPlaced(){
        boxesPlaced++;
        repaint();

        if (boxesPlaced == NUM_BOXES) {
            endGame(false);
        }
    }

    private void initBackgroundMusic(){
        try {
            File musicFile = new File("resources/GameStart.wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(musicFile);
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioInputStream);
            backgroundMusic.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    backgroundMusic.setFramePosition(0);
                    if (gameRunning) {
                        backgroundMusic.start();
                    }
                }
            });
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e){
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

    private class TimerThread extends Thread {
        public void run(){
            try {
                while (gameRunning) {
                    Thread.sleep(1000);
                    SwingUtilities.invokeLater(() -> {
                        repaint();
                    });
                }
            } catch (InterruptedException e){

            }
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(PANEL_WIDTH, PANEL_HEIGHT);
    }

}
