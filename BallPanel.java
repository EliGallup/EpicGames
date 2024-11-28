/*
 * Concepts needed:
 *  - paintComponent @Override
 *  - gravity
 *  - OOP using Ball object
 *  - Color, clearRect, setColor, drawLine
 */
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

/* 
 IS-A:  AnimatedPanel
 HAS-A: 
    - floor
    - Collection of Balls

 DOES:
    - Knows about Balls and asks them to move & draw themselves
    - Deals with click events
*/
public class BallPanel extends AnimatedPanel {

    private List<Ball> balls = new ArrayList<>();
    private static final int FLOOR = Main.HEIGHT - 100;
    Ball specialBall = new Ball(10, 10, FLOOR, getRandColor());

    @Override
    public void updateAnimation() {
        moveBalls();
    }

    public BallPanel() {
        addEventHandlers();
        createBalls();
    }

    /*
     * Create the initial number of balls on our panel
     */
    private void createBalls() {
        for (int count = 0; count < 4; count++) {
            addBall();
        }
    }

    /*
     * This method will create a single ball and add it to our list.
     * Add the ball relatively high on the screen
     */
    public void addBall() {
        int size = (int) (Math.random() * 10) + 50;
        // Take into acount that if x does = 0 or WIDTH, will be or have
        // part of it off the screen...
        // fixed that problem by changing Main.WIDTH - size to Main.WIDTH - size*2.
        int x = (int) (Math.random() * (Main.WIDTH - size*2)) + size/2;
        int y = (int) (Math.random() * (Main.HEIGHT/ 2));
        Ball ball = new Ball(x, y, size, FLOOR, getRandColor());
        // we add it to our list
        balls.add(ball);
    }

    private void addEventHandlers() {
        // a mouse listener requires a full interface with lots of methods.
        // to get around having implement all, we use the MouseAdapter class
        // and override just the one method we're interested in.

        // using an inline/inner anonymous class

        //"this" in this case is the BallPanel inherited from AnimatedPanel. Is a Jpanel.
        // Can implement in 4 ways. 
        // We are going to use mouseadapter so it will implement itself.
        // Creating a mouselistener subclass.
        // This is called an anonymous inner class. (Can only create one)
        // Overriding mousePressed method. 
        // Better because we don't have to add files, or implement other methods.
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                onMouseClicked(me);
            }
        });
    }

    /**
     * The method that gets called when the user clicks the mouse.
     * This will add the piece, update the local board and UI.
     * 
     * @param me The MouseEvent data structure provided by the event.
     */
    private void onMouseClicked(MouseEvent me) {
        int x = me.getX();
        int y = me.getY();
        System.out.printf("Mouse Clicked at (%d, %d)\n", x, y);
        checkAndInflateBalls(x, y);
    }

    private void checkAndInflateBalls(int x, int y) {
        for (Ball ball: balls) {
            if (ball.inside(x, y)) {
                ball.inflate();
            }
        }
    }

    public void moveBalls() {
        // System.out.println("move balls");
        balls.forEach(Ball::move);
        // Call our special move method on our special ball.
        specialBall.specialmove();
    }

    // do NOT override paint(Graphics g).
    // Use paintComponent(Graphics g) because...
    // paint(Graphics g) will draw borders and children--too much!
    //
    // Question: What does this @Override do?
    // A: has no functionality but tells the user that the method is overriden from 
    // a class
    @Override
    public void paintComponent(Graphics g) {
        // Always call our superclass implementation first
        super.paintComponent(g);

        // BEFORE we work on the below TODOs, just draw stuff
        // using the Graphics object.

       
        this.setBackground(Color.WHITE);

        // set the pen color, and draw our floor

        g.setColor(Color.PINK);
        // floor is a static final variable that is set 100 pixels above "ground" of application.
        // g is a graphics object that swing gave us.
        g.drawLine(0, BallPanel.FLOOR, this.getWidth(), BallPanel.FLOOR);
        for (Ball ball: balls) {
            ball.draw(g);
        }
        specialBall.draw(g);
    }
}
