/* COLOUR SWITCH PROGRAM
 * Name: Angelina Wang
 * Class: ICS4UP
 * Program name: Colour Switch
*/

/*GAME FRAME CLASS
 * JFrame
 * Creates visible window containing GamePanel
 */

//IMPORT STATEMENTS
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;

public class GameFrame extends JFrame implements MouseListener {
	
	//VARIABLE DECLARATION
	GamePanel panel;
	public static final Color FADED_BLACK = new Color(21, 21, 21);

	//CONSTRUCTOR
	public GameFrame() {
		panel = new GamePanel(); // run GamePanel constructor
		this.add(panel); //add game panel to game frame
		this.setTitle("Colour Switch"); // set title for frame
		this.setResizable(false); // frame can't change size
		this.setBackground(FADED_BLACK); //background colour
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // closing window will stop program execution
		this.addMouseListener(this); //add mouse listener to frame
		this.pack();// makes components fit in window; adjusts accordingly
		this.setVisible(true); // makes window visible to user
		this.setLocationRelativeTo(null);// set window in middle of screen
	}

	//MouseEvent methods
	public void mouseClicked(MouseEvent e) {
		panel.mouseClicked(e); //calls GamePanel mouseClicked() method	
	}
	//unused overridden methods
	public void mousePressed(MouseEvent e) {
	}
	public void mouseReleased(MouseEvent e) {
	}
	public void mouseEntered(MouseEvent e) {
	}
	public void mouseExited(MouseEvent e) {
	}
}