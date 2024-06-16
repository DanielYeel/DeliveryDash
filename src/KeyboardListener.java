import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

public class KeyboardListener implements KeyListener {
    private Character character;
    private GamePanel gamePanel;
    private List<Box> boxes;
    private Hole blueHole,pinkHole;



    public KeyboardListener(Character character, GamePanel gamePanel, List<Box> boxes, Hole blueHole, Hole pinkHole) {
        this.character = character;
        this.gamePanel = gamePanel;
        this.boxes = boxes;
        this.blueHole = blueHole;
        this.pinkHole = pinkHole;
    }


    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_UP:
                character.setMovingUp(true);
                break;
            case KeyEvent.VK_DOWN:
                character.setMovingDown(true);
                break;
            case KeyEvent.VK_LEFT:
                character.setMovingLeft(true);
                break;
            case KeyEvent.VK_RIGHT:
                character.setMovingRight(true);
                break;
            case KeyEvent.VK_F:
                togglePickUpBox();
                break;
        }
        gamePanel.repaint();
    }


    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_UP:
                character.setMovingUp(false);
                break;
            case KeyEvent.VK_DOWN:
                character.setMovingDown(false);
                break;
            case KeyEvent.VK_LEFT:
                character.setMovingLeft(false);
                break;
            case KeyEvent.VK_RIGHT:
                character.setMovingRight(false);
                break;
        }
        gamePanel.repaint();
    }

    private void togglePickUpBox() {
        if (character.isCarryingBox()) {
            character.putDownBox(blueHole, pinkHole, gamePanel);
        } else {
            for (Box box : boxes) {
                if (character.isCollidingWithBox(box)) {
                    character.pickUpBox(box);
                    break;
                }
            }
        }
    }

}
