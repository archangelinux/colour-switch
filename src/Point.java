/* COLOUR SWITCH PROGRAM
 * Name: Angelina Wang
 * Class: ICS4UP
 * Program name: Colour Switch
*/

/*POINT CLASS
 * Creates small white circular points in each obstacle to be colelcted by the player
 */

//IMPORT STATMENTS
import java.awt.*;

public class Point extends Rectangle {
	
	//VARIABLE DECLARATION
	public static final int size = 15; // size of ball
	public boolean collected = false; //default: each new point has not been collected

	// constructor
	public Point(int x, int y) {
		super(x, y, size, size); //Rectangle constructor
	}

	// scroll movement, called by GamePanel method shiftDown()
	public void shiftDown(int down) {
		y = y + down;
	}

	//displays the point, called by GamePanel draw()
	public void draw(Graphics g) {
		if (!this.collected) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // smoother graphics
			g2.setColor(Color.white);
			g2.fillOval(x, y, size, size);
			this.checkCollision(g2);
		}
	}
	
	// method to check collision between ball and point during painting
	public void checkCollision(Graphics2D g2) {
		// hit(Rectangle r, Shape s, boolean onStroke)
		//if the game ball hits the point
		if (g2.hit(GamePanel.ball, this, true)) {
			GamePanel.currentScore += 1; //increment score
			this.collected = true; //to signal current point to stop being drawn
			GamePanel.createObstacles(); //checks if this point has signaled more obstacles to be created
			if(!GamePanel.mute)
				GamePanel.playSound("collect.wav"); //play point collection sound effect if unmuted
		}
	}

}