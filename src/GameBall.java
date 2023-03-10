/* COLOUR SWITCH PROGRAM
 * Name: Angelina Wang
 * Class: ICS4UP
 * Program name: Colour Switch
*/

/*GAMEBALL CLASS
 * Creates user-controlled game ball (controlled by any keys pressed)
 */


//IMPORT STATMENTS
import java.awt.*;
import java.awt.event.*;

public class GameBall extends Rectangle {
	//VARIABLE DECLARATION	
	public static int yMvmt = 0;
	public final int SPEED = 11; //speed of ball (change in y coord. per frame)
	public static int size = 20; // size of ball

	public static final Color VERY_LIGHT_RED = new Color(255, 102, 102);
	public static final Color GOLD = new Color(255, 204, 51);
	public static final Color DARK_GREEN = new Color(62,178,153);
	public static final Color LIGHT_BLUE = new Color(51, 153, 255);
	public static Color ballColour = GOLD;
	public static int randomColour;
	public static Color[] colours = {VERY_LIGHT_RED, GOLD, DARK_GREEN, LIGHT_BLUE };

	// constructor
	public GameBall(int x, int y) {
		super(x, y, size, size);
	}

	// key events
	public void keyPressed(KeyEvent e) {
		setYDirection(SPEED);
	}

	public void keyReleased(KeyEvent e) {
	}
	
	//called in gravity() method of GamePanel to set the change in y per frame in GameBall move() method
	public void setYDirection(int yDirection) {
		yMvmt = yDirection;
	}

	// method to move ball, called in GamePanel move()
	public void move() {
		y = y - yMvmt;
	}
	
	//method to rescale the game ball for game over animation
	public void scaleTransform() {
		size = size + 35; //increases the sizee of the ball every frame
	}

	//called by Switch to change the balls colour accordingly
	public static void changeColour() {
		randomColour = 1; //dafault color is yellow
		while (ballColour.equals(colours[randomColour])) { //randomly generate an index that is not the current one
			randomColour = (int) (Math.random() * 3) + 0;
		}
		ballColour = colours[randomColour]; //assign new colour to ball

	}
	
	//draw ball for game over animation
	public void drawDeath(Graphics g) {
		Graphics2D g2 = (Graphics2D) g; 
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // smoother graphics
		g2.setStroke(new BasicStroke(5)); //stroke thickness
		g2.setPaint(Color.white); //color
		// ripple effect
		g2.drawOval(x - (size - 20) / 2, y - (size - 20) / 2, size, size); 
		g2.drawOval(x - (size - 40) / 2, y - (size - 40) / 2, size - 20, size - 20);
		g2.drawOval(x - (size - 80) / 2, y - (size - 80) / 2, size - 60, size - 60);
		g2.drawOval(x - (size - 120) / 2, y - (size - 120) / 2, size - 100, size - 100);

	}

	//draw method to draw ball
	public void draw(Graphics g, Color c) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // smoother graphics
		g2.setColor(c); //color
		g2.fillOval(x, y, size, size); //shape

	}

}