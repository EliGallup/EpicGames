import java.awt.event.InputEvent;
import javax.swing.*;

class Main extends JFrame {
    class Main extends JFrame {
        // To eliminate a warning shown in Eclipse, add this constant
        private static final long serialVersionUID = 1L;
    
        // This is declared volatile so that loops that use
        // it are not optimized strangely. We need to access this
        // variable from different threads, meaning that it can
        // change at any second!! It is volatile.
        private static volatile boolean done = false;
        
        public static final int WIDTH = 800;
        public static final int HEIGHT = 800;
    
        // Question: What's the benefit of these constants?
        // A: Easy to keep track of which panel is at what index and also insures that the indexes of the panels
        // don't change.
        private static final int BALL_PANEL = 0;
        private static final int SPARK_PANEL = 1;
        private static final int SPIKE_RUN = 2;
    
        // Our application may have many animated panels
        // But only one panel will be currently visible at a time
        private AnimatedPanel[] panels;
        private int currentPanel = -1;
        
        // This is used to establish the animation speed
        public static int delay = 1000/30;
        
        public static void main(String[] args) throws InterruptedException {
            Main theGUI = new Main();
            
            // Starts the UI Thread and creates the UI in that thread.
            // Uses a Lambda Expression to call the createFrame method.
            // Use theGUI as the semaphore object
            SwingUtilities.invokeLater( () -> theGUI.createFrame(theGUI));
    
            // Have the main thread wait for the GUI Thread to be done
            // creating the frame and all panels.
            // Q: What happens if this wait is deleted? Why does that happen?
            // A: This thread would start trying to draw animations possibly before the JFrame 
            // is created, causing the program to crash. Needs to wait for the signal to continue
            // from the Event dispatch thread.
            synchronized (theGUI ) {
                theGUI.wait();
            }
            
            // Have the main thread continually trigger animations.
            // When an animation stops, trigger another one.
            // The method startAnimation is a synchronous call that will continually
            // repeat the following until animation is stopped:
            // 	    1) update panel (i.e. the animation)
            //      2) paint
            //      3) wait
            // The GUI thread will trigger the animation to stop.
            while (true) {
                System.out.println("Start Animation");
                theGUI.startAnimation();
                System.out.println("Animation stopped");
            }
    
            // Question: How does our application end when the above while-loop
            // has no exit? It loops while(true)!
            // A: 
        }
        
        /**
         * Create the main JFrame and all animation JPanels.
         * 
         * @param semaphore The object to notify when complete
         */
        public void createFrame(Object semaphore) {
            // NOTE : This method will be similar to the Calculator createFrame
            // method. Leverage that code to complete these TODOs.
            
            this.setTitle("Epic Games - Elijah Gallup");
    
            this.setSize(WIDTH, HEIGHT);
    
            // Allows the application to properly close when the
            // user clicks on the Red-X. It tells the all threads
            // to terminate. This will end the main thread.
            this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    
            this.setLayout(null);
    
            // The JFrame has a menu attached to it
            addMenuBar();
    
            // we will have 4 possible panels that we may display
            this.panels = new AnimatedPanel[3];
    
            // Question: How is it that we can put all these different Panels into the
            // same array?
            // A: Its an array of panel objects, and since all these different panels extend Animated Panel it is
            // allowed to be added to the list of Animated Panels.
            panels[BALL_PANEL] = new BallPanel();
            panels[SPARK_PANEL] = new SparksPanel();
            panels[SPIKE_RUN] = new CustomPanel();
            
            // All Panels need to have their bounds set correctly or else they
            // won't be sized correctly and won't be visible.
            for (AnimatedPanel panel : panels) {
                panel.setBounds(0, 0, Main.WIDTH, Main.HEIGHT);
                add(panel);
                panel.setVisible(false);
            }
            // Set the current panel and make it visible 
            this.currentPanel = BALL_PANEL;
            panels[currentPanel].setVisible(true);
    
            // Set this JFrame to be visible
            this.setVisible(true);
    
            System.out.println("All done creating our frame");
            // tell the main thread that we are done creating our dialogs.
            // This allows the main thread to stop wait()'ing.
            synchronized (semaphore) {
                semaphore.notify();
            }
        }
        
        /**
         * This is run on the Main Thread.
         * Like a drummer, it keeps the pace, telling the Panel when 
         * to update itself--when to repaint.
         * This is how "the drummer" updates the objects (Balls, Sparks, Panels).
         */
        public void startAnimation() {
            // We set done to false and allow the UI thread to change the value
            // to true when menu options are selected.
            Main.done = false;
            try {			
                while (!Main.done) {
                    //updateAnimation is an API we added.
                    //updates animation by drawing one frame at a time.
                    //This runs in our main thread. 
                    panels[currentPanel].updateAnimation();
                    // This informs the UI Thread to repaint this component
                    // This runs on the Swing Thread instead of main thread
                    // but the following code will run immediately and go to
                    // sleep; all the while the Swing Thread will keep running
                    // and repaint.
    
                    // Uses lambda expression
                    // InvokeLater takes a runnable, an interface. This means we need to take an interface
                    // function as a parameter, not an int, String, etc.
                    // Thus, a lambda interface. When using lambda, remembers the object
                    // the "this". 
                    // This is how the animations run on the UI Thread.
                    SwingUtilities.invokeLater(() -> repaint()); 
                    // This causes our main thread to wait... to sleep... for a bit.
                    // This is for the user, we use a delay variable to set a timer, before
                    // updateAnimation runs again. It will run at a pace that can be processed by the user.
                    Thread.sleep(Main.delay);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        /**
         * Stops the animation of the current panel and shows the new panel.
         * This is run on the GUI thread
         * 
         * @param index Should be one of Main.BALL_PANEL, BANNER_PANEL, etc.
         */
        private void showPanel(int index) {
            System.out.printf("Show Panel. Thread is: %s\n", Thread.currentThread().getName());
            
            // stop the current animation
            Main.done = true;
            
            // hide the current panel
            panels[currentPanel].setVisible(false);
            
            // show the correct panel
            currentPanel = index;
            panels[currentPanel].setVisible(true);
            
            // The animation will start on the main thread.
            // Do nothing in the UI thread
        }
        
        /**
         * Add some menu options to control the experience.
         */
        private void addMenuBar() {
            
            JMenuBar bar = new JMenuBar();
            // Add the menu bar to the JFrame
            this.setJMenuBar(bar);
            
            JMenu menu = createAnimationMenu();
            bar.add(menu);
            
            // Add more top-level menu options for the specific animation panel
            menu = new JMenu("Balls");
            menu.setMnemonic('B');
            JMenuItem item = new JMenuItem("Add", 'A');
            item.addActionListener(e -> ((BallPanel)panels[BALL_PANEL]).addBall());
            menu.add(item);
            item = new JMenuItem("More Gravity", 'G');
            // item.addActionListener( ... what goes here? ...)
            // Answer these ALSO in your NOTEBOOK:
            // Q: How does the action listener work?
            // A: When the specific menu item is pressed, it triggers event
            // (e in the lambda expression) that allows us to do what we want when the 
            // menu item is clicked, in this cass add a ball object to the ball panel.
            // Q: What is the argument to addActionListener
            // A: The actionEvent, passed as parameter "e" in lambda expression
            // Q: Provide some details on the information available to the
            //    program when the menu item is selected?
            // A: Information available is the addball() method for BallPanel object in the list AnimatedPanel.
            
            menu.add(item);
            item = new JMenuItem("Less Gravity", 'L');
            menu.add(item);
            bar.add(menu);
            
            menu = new JMenu("Sparks");
            menu.setMnemonic('K');
            item = new JMenuItem("More Speed", 'I');
            item.addActionListener(e -> ((SparksPanel)panels[SPARK_PANEL]).moreDelay());
            menu.add(item);
            item  = new JMenuItem("More Gravity", 'M');
            item.addActionListener(e -> ((SparksPanel)panels[SPARK_PANEL]).doGravity("More"));
            menu.add(item);
            item  = new JMenuItem("Less Gravity", 'L');
            item.addActionListener(e -> ((SparksPanel)panels[SPARK_PANEL]).doGravity("Less"));
            menu.add(item);
            bar.add(menu);
    
            menu = new JMenu("Spike Run");
            menu.setMnemonic('M');
            menu.add(item);
            bar.add(menu);
    
        }
        
        /**
         * Create the top-level menu that controls Animation
         * 
         * @return The JMenu object with all the JMenuItems in it.
         */
        private JMenu createAnimationMenu() {
            // The Animation menu will display the specific animation
            // such as the ability to increase/decrease the animation speed.
            JMenu menu = new JMenu("Animation");
            menu.setMnemonic('A');
            // Q: What is the second parameter in the JMenuItem constructor?
            // A: Mnemonic, way to navigate menu with keyboard.
            JMenuItem item = new JMenuItem("Show Balls", 'B');
            // Q: What is the 'e -> showPanel(BALL_PANEL)?
            // Q: What is the 'e'?
            // A: Lambda expression for ActionListener. When ActionEvent is called, changes
            // current panel to AnimatedPanel List value at index BALL_PANEL, which is the Ball Panel.
            // 'e' is a parameter for the ActionEvent in the ActionPerformed method in the functional
            // interface ActionListener.
            item.addActionListener(e -> showPanel(BALL_PANEL));
            menu.add(item);
            item = new JMenuItem("Show Sparks", 'K');
            item.addActionListener(e -> showPanel(SPARK_PANEL));
            menu.add(item);
            item = new JMenuItem("Show Spikes", 'M');
            item.addActionListener(e -> showPanel(SPIKE_RUN));
            menu.add(item);
            item = new JMenuItem("Slower animation", 'S');
            // Q: Why have the Math.min here? What does it accomplish?
            // A: Makes sure that the maximum delay time is 500. Math.min compares 2 numbers and returns the smaller of the two.
            item.addActionListener(e -> Main.delay = Math.min(500, Main.delay+10));
            // Q: What is an accelerator? How does it impact the user experience/functionality?
            // A: Acclerator is a different way of getting to menu item. Makes user experience faster and efficient if user desires
            // 3 ways to get to menu
            // 1. can click on them
            // 2. mnemonics through keyboard to find menu item
            // 3. Accelerators (e.g ctrl + or ctrl -) keyboard shortcuts
            item.setAccelerator(KeyStroke.getKeyStroke('-', InputEvent.CTRL_DOWN_MASK));
            menu.add(item);
            item = new JMenuItem("Faster animation", 'F');
            item.addActionListener(e -> Main.delay = Math.max(10, Main.delay-10));
            item.setAccelerator(KeyStroke.getKeyStroke('=', InputEvent.CTRL_DOWN_MASK));
            menu.add(item);
            
            return menu;
        }
    }    
}
