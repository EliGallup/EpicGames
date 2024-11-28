import java.awt.Graphics;
import java.awt.Image;

public class Spike {
    public int x;
    private int y;
    private int size;
    private int yVelocity;
    private int xVelocity;
    private Image image;

    public Spike(){};

    public Spike(int x, int y, int size, int vx, int vy, Image image) {
        this.x = x;
        this.y = y;
        this.size = size;
        xVelocity = vx;
        yVelocity = vy;
        this.image = image;
    }

    public boolean move() {
        y += yVelocity;
        if (y + size > Main.HEIGHT) {
            return true;
        }
        return false;
    }

    public void specialmove() {
        y += yVelocity;
        x += xVelocity;
        if (y >= Main.HEIGHT - 100 || y <= 0) {
            yVelocity *= -1;
        } 
        if (x >= Main.WIDTH - 50|| x <= 0) {
            xVelocity *= -1;
        }
    }

    public void draw(Graphics g) {
        g.drawImage(image, x, y, size, size, null);
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }
}
