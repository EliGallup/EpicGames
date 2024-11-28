import java.awt.Graphics;
import java.awt.Image;

public class Character {
    private Image image;
    private int xsize;
    private int ysize;
    private int x = Main.WIDTH/2;

    public Character(Image image) {
        this.image = image;
        ysize = 50;
        xsize = 50;
    }

    public void setLocation(int x) {
        this.x = x;
    }

    public void draw(Graphics g, boolean direction) {
        if (direction) {
            g.drawImage(image, x, Main.HEIGHT - ysize*2, xsize, ysize, null);
        } else {
            g.drawImage(image, x + 50, Main.HEIGHT - ysize*2, -xsize , ysize, null);
        }
    }

    public int getX() {
        return x;
    }

}