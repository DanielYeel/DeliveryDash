import javax.swing.*;
import java.awt.*;

public class Character {
    private int x,y;
    private Image image;
    private Box carriedBox;
    private int windowWidth, windowHeight;
    private boolean movingUp, movingDown, movingLeft, movingRight;

    public Character(int x, int y, int windowWidth, int windowHeight) {
        this.x = x;
        this.y = y;
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        loadImage();

        movingUp = false;
        movingDown = false;
        movingLeft = false;
        movingRight = false;
    }


    private void loadImage(){
        ImageIcon ii = new ImageIcon("resources/Character.png");
        image = ii.getImage();
    }

    public void draw(Graphics g){
        g.drawImage(image, x, y, image.getWidth(null) * 6, image.getHeight(null) * 6, null);

        if (carriedBox != null){
            carriedBox.draw(g, x - 16, y + 50);
        }

    }


    public void moveUp() {
        if (y - 10 >= 0) {
            y -= 10;
        }
    }

    public void moveDown() {
        if (y + 10 + image.getHeight(null) * 6 <= windowHeight) {
            y += 10;
        }
    }

    public void moveLeft() {
        if (x - 10 >= 0) {
            x -= 10;
        }
    }

    public void moveRight() {
        if (x + 10 + image.getWidth(null) * 6 <= windowWidth) {
            x += 10;
        }
    }

    public void setMovingUp(boolean movingUp) {
        this.movingUp = movingUp;
    }

    public void setMovingDown(boolean movingDown) {
        this.movingDown = movingDown;
    }

    public void setMovingLeft(boolean movingLeft) {
        this.movingLeft = movingLeft;
    }

    public void setMovingRight(boolean movingRight) {
        this.movingRight = movingRight;
    }

    public void update() {
        if (movingUp) {
            moveUp();
        }
        if (movingDown) {
            moveDown();
        }
        if (movingLeft) {
            moveLeft();
        }
        if (movingRight) {
            moveRight();
        }
    }

    public void pickUpBox(Box box) {
        if (carriedBox == null) {
            carriedBox = box;
            box.setPosition(-300, -300);
        }
    }

    public void putDownBox(Hole blueHole, Hole pinkHole, GamePanel gamePanel) {
        if (carriedBox != null) {
            boolean placed = false;
            if (blueHole.isBoxInside(carriedBox) && "blue".equals(carriedBox.getType())) {
                gamePanel.increaseBoxesPlaced();
                placed = true;
            } else if (pinkHole.isBoxInside(carriedBox) && "pink".equals(carriedBox.getType())) {
                gamePanel.increaseBoxesPlaced();
                placed = true;
            }

            if (placed) {
                carriedBox.setPosition(blueHole.getX() + 10, blueHole.getY() - 30);
                gamePanel.getBoxes().remove(carriedBox);
            } else {
                carriedBox.setPosition(x, y + image.getHeight(null) * 3);
            }
            carriedBox = null;
        }
    }


    public boolean isCollidingWithBox(Box box) {
        int proximity = 70;
        Rectangle characterBounds = new Rectangle(x, y, image.getWidth(null) * 6, image.getHeight(null) * 6);
        Rectangle boxBounds = new Rectangle(box.getX(), box.getY(), box.getImage().getWidth(null) * 4, box.getImage().getHeight(null) * 4);
        return characterBounds.intersects(boxBounds) && characterBounds.getLocation().distance(boxBounds.getLocation()) < proximity;
    }

    public boolean isCarryingBox() {
        return carriedBox != null;
    }

    public Box getCarriedBox() {
        return carriedBox;
    }
}
