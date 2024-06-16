import javax.swing.*;
import java.awt.*;

public class Hole {
    private int x,y;
    private Image image;
    private String type;

    public Hole(int x, int y, String imagePath, String type){
        this.x=x;
        this.y=y;
        this.image = new ImageIcon(imagePath).getImage();
        this.type = type;
    }

    public void draw(Graphics g){
        g.drawImage(image,x,y, image.getWidth(null) * 9, image.getHeight(null) * 9,null);
    }

    public boolean isBoxInside(Box box) {
        Rectangle holeBounds = new Rectangle(x, y, image.getWidth(null) * 9, image.getHeight(null) * 9);
        Rectangle boxBounds = new Rectangle(box.getX(), box.getY(), box.getImage().getWidth(null) * 4, box.getImage().getHeight(null) * 4);
        holeBounds.grow(250,250);
        return holeBounds.intersects(boxBounds);
    }

    public String getType() {
        return type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return image.getWidth(null) * 9;
    }

    public int getHeight() {
        return image.getHeight(null) * 9;
    }
}
