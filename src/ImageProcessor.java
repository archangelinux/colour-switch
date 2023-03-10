/* COLOUR SWITCH PROGRAM
 * Name: Angelina Wang
 * Class: ICS4UP
 * Program name: Colour Switch
*/

/*IMAGE PROCESSOR CLASS
 * Processes and creates all image files in game (game menus, switch icon, mute icon)
 */

//IMPORT STATMENTS
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class ImageProcessor extends JPanel {

	//VARIABLE DECLARATION
	public Image image;
	public Image imageDisplay; //for scaled image
	public int x, y; //position
	//dimensions
	public int width; 
	public int height;

	// CONSTRUCTOR
	public ImageProcessor(String imageResource, int x, int y, int w, int h) {
		//set dimensions
		width = w;
		height = h;
		try {
			// retrieve and scale image
			image = ImageIO.read(ImageProcessor.class.getResource(imageResource)); // read image resource
			imageDisplay = getScaledImage((BufferedImage)image, width, height); //scales image
		} catch (Exception e) {
			System.out.println("Could not load image"); //catch exception message
		}
		// set default location
		this.x = x;
		this.y = y;
	}
	
	//called by Switch shiftDown() method to shift switch icon accordinly
	public void shiftDown(int down) {
		y = y + down;
	}

	//display image
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //smoother graphics
		g2.drawImage(imageDisplay, x, y, null);
	}
	
	
	//method to scale image for better image quality, return BufferedImage
	public static BufferedImage getScaledImage(BufferedImage image, int width, int height) throws IOException { 
		//variable declaration
	    int imageWidth  = image.getWidth();
	    int imageHeight = image.getHeight();
	    //scale image
	    double scaleX = (double)width/imageWidth;
	    double scaleY = (double)height/imageHeight;
	    //use AffineTransform to align and map x and y axis
	    AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
	    AffineTransformOp bilinearScaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BILINEAR); 
	    //scale original image to new BufferedImage
	    return bilinearScaleOp.filter(image, new BufferedImage(width, height, image.getType()));
	}
}

