import java.awt.Color;
import java.awt.Graphics;

public class Spark {
    private int x;
    private int y;
    private double vx;
    public double vy;
    private Color color;
    private int size;
    public static double GRAVITY = 3;

	public Spark(int x, int y, double vx, double vy, Color color) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy; 
        this.color = color;
        this.size = 10;
    }

    public boolean move() {
        y += vy;
        x += vx;
        vy += GRAVITY;
        // If sparks are below certain height and are on their way down (vy < o) than we shrink the balls 
        // to eventually fade out and disappear
        if (vy > 0 && y > 250) {
            this.size -= 1;
        }
        if (y > Main.HEIGHT + 50) {
            return true;
        }
        return false;
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval(x, y, size, size);
    }

}