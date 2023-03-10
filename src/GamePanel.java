/* COLOUR SWITCH PROGRAM
 * Name: Angelina Wang
 * Class: ICS4UP
 * Program name: Colour Switch
*/

/*GAME PANEL CLASS
 * JPanel
 * Creates and controls the game menus and game graphics to display in GameFrame
 * Detects user actions
 * Tracks points and other components of the game
 */


//IMPORT STATEMENTS
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javax.swing.*;
import javax.sound.sampled.*;


public class GamePanel extends JPanel implements Runnable, KeyListener, ActionListener, MouseListener {

	// VARIABLE DECLARATION
	
	// dimensions of window
	public static final int GAME_WIDTH = 650;
	public static final int GAME_HEIGHT = 850;
	
	// graphics
	public Thread gameThread;
	public Image image;
	public Graphics graphics;

	// game elements
	public static GameBall ball;
	public static ArrayList<Rectangle> obstacles;
	public static ArrayList<Point> points;
	public static ArrayList<Switch> switches;

	//HashSets to keep track of created obstacles
	public static Set<Integer> circleIndex;
	public static Set<Integer> jackIndex;
	public static Set<Integer> squareIndex;
	
	//element locations in the panel
	public static int position; 
	public static int randomObs;
	public static int rounds;
	
	//score
	public static int currentScore;
	public static int highScore = 0;

	// booleans
	public static boolean lowerBound;
	public static boolean newRound;
	public static boolean gameOver;
	public static boolean showReplay;

	public static boolean playClicked = false;
	public static boolean infoClicked = false;
	public static boolean okayClicked = false;
	public static boolean mute = false;

	// images
	public static ImageProcessor mainMenu;
	public static ImageProcessor replayMenu;
	public static ImageProcessor muteIcon;

	// font & colours
	public static Font sansSerifBold = new Font("Sans Serif", Font.BOLD, 45);
	public static Font sansSerif = new Font("Sans Serif", Font.PLAIN, 35);
	public static Font sansSerifSmall = new Font("Sans Serif", Font.PLAIN, 14);
	public static Color RED = new Color(255, 102, 102);
	public static Color YELLOW = new Color(255, 204, 51);
	public static Color GREEN = new Color(62, 178, 153);
	public static Color BLUE = new Color(51, 153, 255);
	public static Color WHITE = new Color(255, 255, 255);
	public static Color BLACK_TRANSLUSCENT = new Color(0, 0, 0, (float) 0.85);

	// menu buttons
	public Button play = new Button(235, 400, 180);
	public Button replay = new Button(255, 485, 155);
	public Button info = new Button(240, 685, 65);
	public Button okay = new Button(100, 420, 70);
	public Button volume = new Button(345, 684, 65);

	

	// CONSTRUCTOR
	public GamePanel() {

		prepareGame(); //set up new game upon creation of GamePanel

		this.setFocusable(true); // make everything in this class appear on the screen
		this.addKeyListener(this); // start listening for keyboard input
		this.addMouseListener(this); //start listening for mouse actions
		this.setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));

		// make this class run at the same time as other classes
		gameThread = new Thread(this);
		gameThread.start();

	}
	
	

	//GAME PREPARATION
	
	//prepare-game method sets default values, and resets the game upon replay
	public static void prepareGame() {
		//default values for new game
		gameOver = false;
		GameBall.ballColour = YELLOW;
		GameBall.size = 20;
		GameBall.yMvmt = 0;
		ball = new GameBall(GAME_WIDTH / 2 - GameBall.size / 2, 680); // create ball
		ball.y = 680;
		rounds = 0;
		position = 250;
		currentScore = 0;
		lowerBound = true; //ball at starting position doesn't move
		newRound = false; //prevents endless creation of obstacles in memory before game starts 
		showReplay = false; 
		circleIndex = new HashSet<Integer>(); //HashSets allow for random generation of obstacle combinations
		jackIndex = new HashSet<Integer>();
		squareIndex = new HashSet<Integer>();
		obstacles = new ArrayList<Rectangle>(10); //ArrayLists allow for infinite game loop
		points = new ArrayList<Point>(10); //points and switches ArrayLists start at 10 and increase alongside the number of obstacles
		switches = new ArrayList<Switch>(10);

		createObstacles(); //create the first 10 obstacles
		
		//initialize menu images, access png files in src folder
		mainMenu = new ImageProcessor("ColourSwitchMainMenu.png", 0, 0, GAME_WIDTH, GAME_HEIGHT);
		replayMenu = new ImageProcessor("ColourSwitchReplayMenu.png", 0, 0, GAME_WIDTH, GAME_HEIGHT);
		muteIcon = new ImageProcessor("MuteIcon.png", 335, 675, 84, 79);
	}
	
	//method to create new obstacle objects
	public static void createObstacles() {
		if (currentScore % 10 == 9 || rounds == 0) {// when the player gets to the ninth obstacle, start creating more obstacles
			if (rounds == 0) // for the first round
				position = 250; 
			else
				position = -1300; //for all successive rounds of 10
			for (int i = 10 * rounds; i < 10 * rounds + 10; i++) { //for every round, generate new obstacle objects
				randomObs = (int) (Math.random() * 3) + 1; //generate a random index value [1,3]
				//each index value is attributed to a different obstacle
				if (randomObs == 1) {
					obstacles.add(new CircleObstacle(GAME_WIDTH / 2 - 250 / 2, position, 250)); // initialize new obstacle and add to obstacle ArrayList
					points.add(new Point(GAME_WIDTH / 2 - Point.size / 2, position + 250 / 2)); //initialize new point and add to poits ArrayList
					circleIndex.add(i); //keep track of obstacle using attributed HashSet
				} else if (randomObs == 2) { 
					obstacles.add(new JackObstacle(GAME_WIDTH / 2 - 110, position, 100));
					points.add(new Point(GAME_WIDTH / 2 - Point.size / 2, position + 50));
					jackIndex.add(i);
				} else if (randomObs == 3) {
					obstacles.add(new SquareObstacle(GAME_WIDTH / 2 - 100, position, 200));
					points.add(new Point(GAME_WIDTH / 2 - Point.size / 2, position + 100));
					squareIndex.add(i);
				}
				switches.add(new Switch(GAME_WIDTH / 2 - Switch.size / 2, position - 250)); //initialize new switch for every obstacle and add to swithc ArrayList

				position -= 650; // spread out obstacles along the y axis; each new obstacle is positioned higher than the previous
			}
			rounds += 1; //increment after round '0'
		}
	}

	
	//DISPLAYING GRAPHICS

	// overridden paint method form java.awt.* (automatically called)
	public void paint(Graphics g) {
		// "double buffering"
		image = createImage(GAME_WIDTH, GAME_HEIGHT); // draw off screen
		graphics = image.getGraphics();
		draw(graphics); // update the positions of everything on the screen
		g.drawImage(image, 0, 0, this); // redraw everything on the screen
	}

	// call the draw methods in each class to update positions as things move
	public void draw(Graphics g) {
		// display main menu until play is clicked
		if (!playClicked) {
			mainMenu.draw(g); //main menu screen
			play.draw(g); //play button
			info.draw(g); //info button
			if (infoClicked)
				infoClicked(g); //show info popup
			if(mute)
				muteIcon.draw(g); //show mute icon
		}
		else { 
			// display game elements
			if (GameBall.size < 5000) { // buffer after player loses
				for (int i = 0; i < obstacles.size(); i++) { 
					// display each obstacle depending on random generation tracked in HashSets
					if (circleIndex.contains(i)) {
						((CircleObstacle) obstacles.get(i)).draw(g);
					} else if (jackIndex.contains(i)) {
						((JackObstacle) obstacles.get(i)).draw(g);
					} else if (squareIndex.contains(i)) {
						((SquareObstacle) obstacles.get(i)).draw(g);
					}
					points.get(i).draw(g); //display Point for evey obstacle
					if (i % 3 == 0) //display Switch for every 3 obstacles
						switches.get(i).draw(g);
				}
				// display score
				g.setColor(Color.white);
				g.setFont(sansSerifBold);
				g.drawString(String.valueOf(currentScore), 50, 100);
			} 
			else //game is over, signal following if-else statement to display replay menu
				showReplay = true; 

			if (!gameOver) {
				ball.draw(g, GameBall.ballColour); //display ball when game is not over
			}

			else {
				ball.drawDeath(g); //display ball animation when player loses
				if (showReplay) {
					findHighScore(); //find high score of all games played upon creation of GamePanel
					replayMenu.draw(g); //display replay menu
					
					// Get the FontMetrics of the score
					FontMetrics metrics = g.getFontMetrics(sansSerif);
					Rectangle rect = new Rectangle(0, 280, GAME_WIDTH, 50);
					// Determine the X coordinate for the text
					int a = rect.x + (rect.width - metrics.stringWidth(String.valueOf(currentScore))) / 2;
					// Determine the Y coordinate for the text
					int b = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
					g.setColor(WHITE);
					g.setFont(sansSerifBold);
					g.drawString(String.valueOf(currentScore), a, b); //display current score
					a = rect.x + (rect.width - metrics.stringWidth(String.valueOf(highScore))) / 2;
					b = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
					g.setFont(sansSerif);
					g.drawString(String.valueOf(highScore), a, b + 125); //display high score
					replay.draw(g); //replay button
					info.draw(g); //info button
					volume.draw(g); //volume button

					if (infoClicked) //if info button is clicked
						infoClicked(g); //display info popup
					if(mute) //if volume is muted
						muteIcon.draw(g);  //display mute icon
				}
			}
		}
	}
	
	//method for How to Play popup when the info button is clicked
	public void infoClicked(Graphics g) {
		g.setColor(BLACK_TRANSLUSCENT);
		g.fillRoundRect(50, 150, GAME_WIDTH - 100, 400, 30, 30); // popup
		g.setColor(WHITE);
		g.setFont(sansSerif);
		g.drawString("How to play", 100, 235); //heading
		g.setFont(sansSerifSmall);
		g.drawString("1. Click any key repeatedly to keep the ball in the air", 100, 280); //instructions
		g.drawString("2. Carry the ball upwards and pass through the obstacles", 100, 300);
		g.drawString("3. Only pass through parts of the obstacle that match the ball colour", 100, 320);
		g.drawString("4. Collect the white circles to add to your score", 100, 340);
		g.drawString("5. The 4-coloured circles will change your ball colour", 100, 360);
		g.drawString("6. The game ends when your ball touches a different colour", 100, 380);

		okay.draw(g); //okay button to close popup
		g.setColor(YELLOW);
		g.fillRoundRect(100, 430, 70, 30, 10, 10); // okay button
		g.setColor(BLACK_TRANSLUSCENT);
		g.drawString("OKAY", 112, 450);
		if (okayClicked) { //if the okay button is clicked, close popup, and reset booleans
			infoClicked = false;
			okayClicked = false;
		}
	}
	
	
	//UPDATING GAME ELEMENTS & GRAPHICS
	
	//run method automatically invoked
	public void run() {
		//timing of frames to slow graphic updates
		long lastTime = System.nanoTime();
		double amountOfTicks = 60;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long now;

		while (true) { // this is the infinite game loop
			now = System.nanoTime();
			delta = delta + (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1) {
				move(); //calls move method every frame to perform basic movements for each game object
				if (!gameOver) { //for every frame while game is in process
					checkBound(); //controls ball location
					gravity(); //mimics gravity for ball movement
					shiftDown(); //shift game elements down
				}
				repaint(); //update graphics
				delta--;
			}
		}
	}
	
	//method to move game elements
	public void move() {
		if (!gameOver)
			ball.move(); //move ball if game is not over
		else
			ball.scaleTransform(); //play ball animation when player loses
		for (int i = 0; i < obstacles.size(); i++) { //rotate each obstacle continuously
			if (circleIndex.contains(i)) { //uses HashSet to identify obstacles
				((CircleObstacle) obstacles.get(i)).move(); //calls the move method in each obstacle's class
			} else if (jackIndex.contains(i)) {
				((JackObstacle) obstacles.get(i)).move();
			} else if (squareIndex.contains(i)) {
				((SquareObstacle) obstacles.get(i)).move();
			}
		}
	}
	
	//gravity method simulates a gravitational pull on the ball object
	public void gravity() {
		 // for ball to decrease velocity every frame in run()
		ball.setYDirection(GameBall.yMvmt - 1); 
	}

	// shifts game obstacles down, to keep game ball in the center of the playable area
	public void shiftDown() {
		if (ball.y <= 350) { //every time the ball rises about this checkpoint
			for (int i = 0; i < obstacles.size(); i++) { //shift obstacles, points, and switches down
				obstacles.get(i).y += 3; //shift obstacles of all classes 3 pixels down
				points.get(i).shiftDown(3); //shifts all points 3 pixels down
				if (i % 3 == 0 && i < switches.size() )
					switches.get(i).shiftDown(3); //shift all displayed swithces 3 pixels down
				lowerBound = false; //ground disappears near beginning of play
			}
		}
	}

	public void checkBound() {
		// force player to remain on screen
		if (ball.y >= 700 && lowerBound == true) { // invisible ground
			ball.y = 700;
		}
		// invisible ground after lower bound has been removes (off screen)
		if (ball.y >= 1000 && lowerBound == false) { 
			gameOver = true;
		}
	}
	
	//method to find high score for all games played within one GamePanel instantiation
	public void findHighScore() {
		if (currentScore > highScore)	//after every game, compare update high score by comparing with current score
			highScore = currentScore;
	}


	//USER ACTIONS
	
	// KeyEvent methods
	public void keyPressed(KeyEvent e) { //communicate to GameBall class when any key is pressed
		ball.keyPressed(e); 
	}
	public void keyReleased(KeyEvent e) {
		ball.keyReleased(e); //nothing happens - GameBall class
	}

	//MouseEvent method
	public void mouseClicked(MouseEvent e) {
		//if user is viewing replay menu, and the mouse clicks inside the Rectangle of the replay Button 
		if (showReplay && replay.getBounds().contains(e.getPoint())) { 
			if(!mute)
				playSound("start.wav"); //if unmuted, play starting sound effect
			showReplay = false; //signal draw method to stop showing replay menu
			prepareGame(); //reinvoke this method to reset the game
		}

		//if the play Button hasn't been clicked, and the mouse clicks inside the Rectangle of the play Button 
		if (!playClicked && play.getBounds().contains(e.getPoint())) {
			if(!mute)
				playSound("start.wav"); //if unmuted, play starting sound effect
			playClicked = true; //signal draw method to start showing game
		}
		//if the info Button hasn't been clicked, and the mouse clicks inside the Rectangle of the info Button 
		if (!infoClicked && info.getBounds().contains(e.getPoint())) {
			infoClicked = true; //signal draw method to show info popup
		}
		//if the play okay hasn't been clicked, and the mouse clicks inside the Rectangle of the okay Button 
		if (!okayClicked && okay.getBounds().contains(e.getPoint())) {
			okayClicked = true; //signal draw method to close popup and reset booleans
		}
		//if the mouse clicks inside the Rectangle of the volume Button 
		if(volume.getBounds().contains(e.getPoint())) {
			if(mute) //if muted, turn on volume for sound effects
				mute = false;
			else //if unmuted, turn off volume for sound effects
				mute = true;
		}
	}
	
	//sound effects method
	public static void playSound(String url) {
		ClassLoader loader = GamePanel.class.getClassLoader(); //get class loader of game panel to load url file
  	  	Clip clip; //create new Clip object
	      try {
	    	 AudioInputStream audioIn = AudioSystem.getAudioInputStream(loader.getResource(url)); //loads url file 
	         clip = AudioSystem.getClip(); //get clip
	         clip.open(audioIn); //open audio file
	         clip.start(); //start playing sound
	      } catch (UnsupportedAudioFileException e) { //catch errors
	         e.printStackTrace();
	      } catch (IOException e) {
	         e.printStackTrace();
	      } catch (LineUnavailableException e) {
	         e.printStackTrace();
	      }
	}
	
	//empty overridden methods of KeyListener, ActionListener, MouseListener
	public void keyTyped(KeyEvent e) {
	}
	public void actionPerformed(ActionEvent e) {
	}
	public void mousePressed(MouseEvent e) {
	}
	public void mouseReleased(MouseEvent e) {
	}
	public void mouseEntered(MouseEvent e) {
	}
	public void mouseExited(MouseEvent e) {
	}
}