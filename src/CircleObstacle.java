/* COLOUR SWITCH PROGRAM
 * Name: Angelina Wang
 * Class: ICS4UP
 * Program name: Colour Switch
*/

/*CIRCLE OBSTACLE CLASS
 * Creates circle object with 4 coloured segments rotating continuously about its centre
 */

//IMPORT STATEMENTS
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;

public class CircleObstacle extends Rectangle {

	// VARIABLE DECLARATION
	public static final double SPEED = 1.5; // amount added to angle each frame
	public double angle = 0; // starting angle
	public int size; // diameter of circle
	public Graphics2D g2; // for draw() method

	// declare quarter-circle arcs
	public static Arc2D yellow;
	public static Arc2D blue;
	public static Arc2D red;
	public static Arc2D green;
	public static Rectangle2D rect; // container for arcs

	// CONSTRUCTOR
	public CircleObstacle(int x, int y, int s) {
		super(x, y, s, s); // Rectangle constructor
	}

	// method to rotate object
	public void move() {
		angle = angle + SPEED; // angle changes by SPEED every frame
	}

	// display graphics, called by GamePanel draw()
	public void draw(Graphics g) {
		g2 = (Graphics2D) g.create(); // cast to Graphics2D
		rect = (Rectangle2D) this; // container for arc

		// initialize quarter-circle
		yellow = new Arc2D.Double(rect, 0, 82, Arc2D.OPEN); // 82 deg accounts for bold stroke when drawing
		blue = new Arc2D.Double(rect, 90, 82, Arc2D.OPEN);
		red = new Arc2D.Double(rect, 180, 82, Arc2D.OPEN);
		green = new Arc2D.Double(rect, 270, 82, Arc2D.OPEN);

		// for smoother graphic edges
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// rotate graphics
		g2.rotate(Math.toRadians(angle), rect.getX() + rect.getWidth() / 2, rect.getY() + rect.getHeight() / 2);

		/* alternative approach to rotating object (by individual starting angle): 
		 * 		yellow.setAngleStart(0+ angle); red.setAngleStart(90+ angle);
		 * 		blue.setAngleStart(180+ angle); green.setAngleStart(270+ angle);
		 */

		// draw shapes
		g2.setStroke(new BasicStroke(20)); //stroke thickness
		g2.setPaint(GamePanel.YELLOW); //colour
		g2.draw(yellow); //draw each segment
		g2.setPaint(GamePanel.BLUE);
		g2.draw(blue);
		g2.setPaint(GamePanel.RED);
		g2.draw(red);
		g2.setPaint(GamePanel.GREEN);
		g2.draw(green);

		// during painting, check if Graphics2D Stroke for obstacle collides with GameBall object
		checkCollision(g2);
		// protocol for any transformation of g2 (e.g. rotation) - none used here (but iss an alternative
		g2.dispose();
	}

	// method to check collision between ball and obstacles during painting
	public void checkCollision(Graphics2D g2) {
		// hit(Rectangle r, Shape s, boolean onStroke) - detects when s hits r during drawing
		if (!GamePanel.gameOver) { //if game is in play
			//if ball hits obstacle, and ball colour is the same as the obstacle segment hit
			if (g2.hit(GamePanel.ball, yellow, true) && !GameBall.ballColour.equals(GamePanel.YELLOW)) { 
				if (!GamePanel.mute)
					GamePanel.playSound("death.wav"); //play death sound effect if not muted
				GamePanel.gameOver = true; //game is over
			//repeat for all four segments
			} else if (g2.hit(GamePanel.ball, red, true) && !GameBall.ballColour.equals(GamePanel.RED)) { 
				if (!GamePanel.mute)
					GamePanel.playSound("death.wav");
				GamePanel.gameOver = true;
			} else if (g2.hit(GamePanel.ball, blue, true) && !GameBall.ballColour.equals(GamePanel.BLUE)) {
				if (!GamePanel.mute)
					GamePanel.playSound("death.wav");
				GamePanel.gameOver = true;
			} else if (g2.hit(GamePanel.ball, green, true) && !GameBall.ballColour.equals(GamePanel.GREEN)) {
				if (!GamePanel.mute)
					GamePanel.playSound("death.wav");
				GamePanel.gameOver = true;
			}
		}
	}
}