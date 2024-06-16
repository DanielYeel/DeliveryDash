import javax.swing.*;
import java.awt.*;

public class Box {
    private int x, y;
    private Image image;
    private int windowWidth, windowHeight;
    private String type;

    public Box(int x, int y, String imagePath, int windowWidth, int windowHeight, String type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        loadImage();
    }

    public void loadImage(){
        if (type.equals("blue")) {
            image = new ImageIcon("resources/BlueBox.png").getImage();
        } else if (type.equals("pink")) {
            image = new ImageIcon("resources/PinkBox.png").getImage();
        }
    }

    public void draw(Graphics g) {
        g.drawImage(image, x, y, image.getWidth(null) * 4, image.getHeight(null) * 4, null);
    }

    public void draw(Graphics g, int characterX, int characterY) {
        g.drawImage(image, characterX, characterY, image.getWidth(null) * 4, image.getHeight(null) * 4, null);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setPosition(int x, int y) {
        if (x >= 0 && x + image.getWidth(null) * 4 <= windowWidth && y >= 0 && y + image.getHeight(null) * 4 <= windowHeight) {
            this.x = x;
            this.y = y;
        }
    }

    public Image getImage(){
        return image;
    }


    public String getType() {
        return type;
    }

}
