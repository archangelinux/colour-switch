/* COLOUR SWITCH PROGRAM
 * Name: Angelina Wang
 * Class: ICS4UP
 * Program name: Colour Switch
*/

/*SWITCH CLASS
 * Creates collectible switch object that changes the GameBall colour
 */

//IMPORT STATEMENTS
import java.awt.*;

public class Switch extends Rectangle {
	
	//VARIABLE DECLARATION
	public static final int size = 25; // size of circle
	public boolean collected = false;
	public ImageProcessor img;

	// CONSTRUCTOR
	public Switch(int x, int y) {
		super(x, y, size, size);
		img = new ImageProcessor("ColourSwitch.png", x, y, size, size); //process new switch icon

	}

	//scroll movement
	public void shiftDown(int down) {
		y = y + down;
		img.shiftDown(down); //shift switch icon from ImageProcessor accordingly
	}

	// display switch, called by GamePanel draw()
	public void draw(Graphics g) {
		if (!this.collected) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // smoother graphics
			g2.setColor(Color.white);
			g2.fillOval(x, y, size, size);
			img.draw(g2);
			this.checkCollision(g2);
		}
	}
	
	// method to check collision between ball and switch during painting
	public void checkCollision(Graphics2D g2) {
		// hit(Rectangle r, Shape s, boolean onStroke)
		//if the game ball hits the switch		
		if (g2.hit(GamePanel.ball, this, true)) {
			GameBall.changeColour(); //change the colour of the ball
			this.collected = true; //to signal current switch to stop being drawn
			if(!GamePanel.mute)
				GamePanel.playSound("switch.wav"); //plays switch sound effect if unmuted

		}
	}

}