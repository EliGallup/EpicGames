import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.awt.*;
import java.io.*;
import javax.swing.Timer;

import javax.imageio.ImageIO;

// Implements KeyListener to cover one of 4 different requirements for using interfaces.
public class CustomPanel extends AnimatedPanel implements KeyListener, LoadImage {

    private List<Image> images = new ArrayList<>();
    private List<Spike> spikes = new ArrayList<>();
    private List<Spike> specialspikes = new ArrayList<>();
    private Character character;
    private Image gameOver;
    private boolean direction;

    public CustomPanel() {
        createSpikes();
        createSpecialSpikes();
        Image person;
        person = loadImage("Basketball.jpg");
        Character character = new Character(person);
        this.character = character;
        this.addKeyListener(this);
        this.setFocusable(true);
    }

    public void updateAnimation() {
        if (contact()) {
            gameOver = loadImage("GameOver.jpg");
        } else {
            moveSpikes();
        }
    }

    public Image loadImage(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void createSpecialSpikes() {
        int delay = 10000; // every ten seconds add new special spike
        // Lambda expression added
        new Timer(delay, ((ActionEvent e) -> {addSpike(specialspikes);})).start();
    }

    private boolean contact() {
        for (Spike spike: spikes) {
            if ((spike.x > character.getX() - 10 && spike.x < character.getX() + 40) || (spike.x + 50 < character.getX() + 40 && spike.x + 50 > character.getX())) {
                if (spike.getY() > Main.HEIGHT -150) {
                    this.setFocusable(false);
                    return true;
                }
            }
        }
        for (Spike spike: specialspikes) {
            if ((spike.getX() > character.getX() - 10 && spike.getX() < character.getX() + 40) || (spike.x + 50 < character.getX() + 40 && spike.x + 50 > character.getX())) {
                if (spike.getY() > Main.HEIGHT -150) {
                    this.setFocusable(false);
                    return true;
                }
            }
        }
        return false;
    }

    private void addSpike(List<Spike> list) {
        int x = (int) (Math.random()*Main.WIDTH) - 50;
        int vy = (int) (Math.random()*3) + 10;
        int vx = (int) (Math.random()*3) + 10;
        Image image;
        image = loadImage("Basketball.jpg");
        images.add(image);
        Spike spike = new Spike(x, 0, 50, vx, vy, image);
        list.add(spike);
    }

    private void createSpikes() {
        for (int count = 0; count < 4; count++) {
            addSpike(spikes);
        }
    }

    private void moveSpikes() {
        for (int count = 0; count < spikes.size(); count++) {
            if (spikes.get(count).move()) {
                spikes.remove(count);
                addSpike(spikes);
            }
        }
        for (int count = 0; count < specialspikes.size(); count++) {
            specialspikes.get(count).specialmove();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

		this.setBackground(Color.BLACK);

        if (contact()) {
            g.drawImage(gameOver, 0, Main.HEIGHT/2, Main.WIDTH, 200, null, null);
        } else {
            this.setBackground(Color.WHITE);
            character.draw(g, direction);

            spikes.forEach(spike -> spike.draw(g));
            
            specialspikes.forEach(spike -> spike.draw(g));
        }
        
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case 37: character.setLocation(character.getX() - 15);
            direction = false;
                break;
            case 39: character.setLocation(character.getX() + 15);
            direction = true;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}