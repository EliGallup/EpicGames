import java.awt.*;
import java.awt.event.*;
import java.util.List;

import javax.swing.Timer;

import java.util.ArrayList;

public class SparksPanel extends AnimatedPanel {

	private List<Spark> sparks = new ArrayList<Spark>();
	private int delay = 100;
	
	@Override
	public void updateAnimation() {
		moveSparks();
	}
	
	public SparksPanel() {
		addEventHandlers();
		createSparks();
	}

	public void moreDelay() {
		delay /= 2;
		System.out.println(delay);
		createSparks();
	}

	public void doGravity(String input) {
		if (input.equalsIgnoreCase("more")) {
			Spark.GRAVITY += 1;
		} else if (input.equalsIgnoreCase("less")) {
			Spark.GRAVITY -= 1;
		}
	}

	private void createSparks() {
		// Anonymous Inner class for ActionListener.
		ActionListener taskPerformer = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				addSpark();
				moveSparks();
			}
		};
		new Timer(delay, taskPerformer).start();	
	}

	private void addSpark() {
		int x = Main.WIDTH/2;
        int y = Main.HEIGHT;
        double vx = (Math.random() * 7);
		if ((int) (Math.random()*2) > 0) {
			vx *= -1;
		}
        double vy = (Math.random()*-10) -50; 
		Spark spark = new Spark(x, y, vx, vy, getRandColor());
		sparks.add(spark);
	}

	private void addEventHandlers() {
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent me) {
				onMouseClicked(me);
			}
		});
	}

	private void onMouseClicked(MouseEvent me) {
		int x = me.getX();
		int y = me.getY();
		for (int i = 0; i < 5; i++) {
			double vx = (Math.random() * 5);
			if ((int) (Math.random()*2) > 0) {
				vx *= -1;
			}
			double vy = (Math.random() * -5) - 20;
			sparks.add(new Spark(x, y, vx, vy, getRandColor()));
		}
	}

	private void moveSparks() {
		for (int count = 0; count < sparks.size(); count++) {
			if (sparks.get(count).move()) {
				sparks.remove(count);
			}
		}
	}


	// do NOT override paint(Graphics g). 
    // Use paintComponent(Graphics g) because...
	// paint(Graphics g) will draw borders and children--too much!
	// paint(Graphics g) will draw borders and children--too much!
	@Override
	public void paintComponent(Graphics g){  
        super.paintComponent(g);

		this.setBackground(Color.WHITE);

		for (Spark spark: sparks) {
			spark.draw(g);
		}
	}	
}