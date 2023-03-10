/* COLOUR SWITCH PROGRAM
 * Name: Angelina Wang
 * Class: ICS4UP
 * Program name: Colour Switch
*/

/*BUTTON CLASS
 * Creates invisible Rectangle with "button" dimensions for user MouseEvents
 */

//IMPORT STATMENTS
import java.awt.*;


public class Button extends Rectangle{

	//VAIRABLE DECLARATION
	public int size; //length of rectangle side
	
	public Button(int x, int y, int s) {
		super(x,y,s,s); //Rectangle constructor
		this.size = s; //set size

	}
	
	//draw transparent rectangle, called by Game Penl draw()
	public void draw(Graphics g) {
		g.setColor(new Color(0, 0, 0, (float)0)); //transparent (change colour to test location)
		g.fillRect(x,y,size,size); //fill rectangle
	}
}


