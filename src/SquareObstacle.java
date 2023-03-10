/* COLOUR SWITCH PROGRAM
 * Name: Angelina Wang
 * Class: ICS4UP
 * Program name: Colour Switch
*/

/*SQUARE OBSTACLE CLASS
 * Creates square object with 4 coloured edges rotating continuously about a skewed centre
 */

//IMPORT STATEMENTS
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class SquareObstacle extends Rectangle {

	// VARIABLE DECLARATION
	public static final int SPEED = -1; // rotate oppostie direction of other obstacles
	public int angle = 0; // starting angle
	public int size; // length of square side
	public Graphics2D g2; // for draw() method

	// declare square edges
	public static Rectangle yellow;
	public static Rectangle blue;
	public static Rectangle red;
	public static Rectangle green;
	public static Rectangle2D rect; // container for square

	// constructor
	public SquareObstacle(int x, int y, int s) {
		super(x, y, s, s);
	}

	// method to rotate object
	public void move() {
		angle = angle + SPEED;
	}

	// display graphics, called by GamePanel draw()
	public void draw(Graphics g) {
		g2 = (Graphics2D) g.create(); // cast to Graphics2D
		rect = (Rectangle2D) this; // container for arc

		// initialize arcs
		yellow = new Rectangle(x, y, 250, 10);
		red = new Rectangle(x + 260, y, 10, 250);
		blue = new Rectangle(x + 20, y + 260, 250, 10);
		green = new Rectangle(x, y + 20, 10, 250);

		// for smoother graphic edges
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// rotate graphics
		g2.rotate(Math.toRadians(angle), rect.getX() + rect.getWidth() / 2, rect.getY() + rect.getHeight() / 2);

		// draw shapes
		g2.setStroke(new BasicStroke(10)); // stroke thickness
		g2.setPaint(GamePanel.YELLOW); // colour
		g2.draw(yellow); // draw each segment
		g2.fill(yellow);// fill each screen
		g2.setPaint(GamePanel.RED);
		g2.draw(red);
		g2.fill(red);
		g2.setPaint(GamePanel.BLUE);
		g2.draw(blue);
		g2.fill(blue);
		g2.setPaint(GamePanel.GREEN);
		g2.draw(green);
		g2.fill(green);

		// during painting, check if Graphics2D Stroke for obstacle collides with GameBall object
		checkCollision(g2);
		// protocol for any transformation of g2 (e.g. rotation) - none used here (but iss an alternative
		g2.dispose();
	}

	// method to check collision between ball and obstacles during painting
	public void checkCollision(Graphics2D g2) {
		// hit(Rectangle r, Shape s, boolean onStroke) - detects when s hits r during
		// drawing
		if (!GamePanel.gameOver) { // if game is in play
			// if ball hits obstacle, and ball colour is the same as the obstacle segment
			if (g2.hit(GamePanel.ball, yellow, true) && !GameBall.ballColour.equals(GamePanel.YELLOW)) {
				if (!GamePanel.mute)
					GamePanel.playSound("death.wav"); // play death sound effect if not muted
				GamePanel.gameOver = true; // game is over
				// repeat for all four segments
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