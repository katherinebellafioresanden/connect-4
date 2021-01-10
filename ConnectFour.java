import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.event.*; // NEW #1 !!!!!!!!!!
import java.util.Random;
import javax.swing.*;

/***********************************************************************
 * ConnectFour - 2 player game involving red and blue game pieces.
 * Uses the Checker class (based on Ball class).
 * @author kat
 * 
 * June 23, 2019
 ************************************************************************/

public class ConnectFour
extends JFrame 
implements KeyListener
{

	// screen info
	public static final int SCREEN_WIDTH = 850;
	public static final int SCREEN_HEIGHT = 875;
	public static final int TITLE_BAR = 25;
	public static final Color BACKGROUND = Color.BLACK;

	// checker dimensions
	public static final int CHECKER_RADIUS = 40;
	public static final int CHECKER_WIDTH = 2 * CHECKER_RADIUS;
	public static final int GRID_WIDTH = CHECKER_WIDTH * 5 / 4;

	// board info
	public static final int NUM_COLS = 7;
	public static final int NUM_ROWS = 6;
	public static final int BOARD_LEFT = 100;
	public static final int BOARD_TOP = 180;
	public static final int BOARD_WIDTH = GRID_WIDTH * NUM_COLS;
	public static final int BOARD_HEIGHT = GRID_WIDTH * NUM_ROWS;

	// checker birthplace info
	public static final int CHECKER_BIRTH_CENTER_X = BOARD_LEFT + GRID_WIDTH / 2;
	public static final int CHECKER_BIRTH_CENTER_Y = BOARD_TOP - GRID_WIDTH / 2;

	// checker arrays
	private static Checker[] redCheckers = new Checker[NUM_COLS * NUM_ROWS];
	private static Checker [] blueCheckers = new Checker[NUM_COLS * NUM_ROWS];
	private static int currentRedIndex = 0;
	private static int currentBlueIndex = 0;

	// turn info
	public static final int RED = 0; // even means red (red goes first)
	public static final int BLUE = 1; // odd means blue
	private static int turn = 0;  

	// game board availability info
	public static final int EMPTY = -1; 
	private static int[][] availability = new int[NUM_ROWS][NUM_COLS];


	public static void main(String[] args)
	{
		ConnectFour game = new ConnectFour();

		// set all spots to available
		for (int row = 0; row < NUM_ROWS; row++)
		{
			for (int col = 0; col < NUM_COLS; col++)
			{
				availability[row][col] = EMPTY;
			}
		}

		// create first checkers
		redCheckers[currentRedIndex] = createNewChecker(RED);
		currentRedIndex++;

		blueCheckers[currentBlueIndex] = createNewChecker(BLUE);
		currentBlueIndex++;

		// Show the window 
		game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		game.setVisible(true);
		game.createBufferStrategy(2);					// NEW B
		game.repaint();

		// add the listeners
		game.addKeyListener(game);


	}

	@Override
	public void keyTyped(KeyEvent e)
	{
	

	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		int keyCode = e.getKeyCode();

		if (keyCode == KeyEvent.VK_LEFT)
		{
			if (turn % 2 == RED)
			{
				left(redCheckers[currentRedIndex - 1]);
			}
			else
			{
				left(blueCheckers[currentBlueIndex - 1]);
			}

		}
		else if (keyCode == KeyEvent.VK_RIGHT)
		{
			if (turn % 2 == RED)
			{
				right(redCheckers[currentRedIndex - 1]);
			}
			else
			{
				right(blueCheckers[currentBlueIndex - 1]);
			}
		}
		else if (keyCode == KeyEvent.VK_DOWN)
		{
			if (turn % 2 == RED)
			{
				down(redCheckers[currentRedIndex - 1]);

				// only do these updates if we actually placed the checker
				if (redCheckers[currentRedIndex - 1].placed())
				{
					redCheckers[currentRedIndex] = createNewChecker(RED);
					currentRedIndex++;


				}

			}
			else
			{
				down(blueCheckers[currentBlueIndex - 1]);

				// only do these updates if we actually placed the checker
				if (blueCheckers[currentBlueIndex - 1].placed())
				{
					blueCheckers[currentBlueIndex] = createNewChecker(BLUE);
					currentBlueIndex++;
				}

			}
		}

		repaint();

	}

	public static boolean checkForHorizontalWin(Checker c)
	{
		int row = c.getRow();
		int col = c.getCol();
		int playerColor = c.getColor();
		int connect4Count = 0;
		
		for (int i = col - 3; i <= col + 3; i++)
		{
			// if i is valid
			if (i >= 0 && i < NUM_COLS)
			{
			
				// if we've got the right color in this slot
				if (availability[row][i] == playerColor)
				{
					connect4Count++;
					
					// four in a row
					if (connect4Count >= 4)
					{
						if (playerColor == RED)
						{
						System.out.println("The winner is RED!");
						}
						else
						{
							System.out.println("The winner is BLUE!");
						}
						return true;
					}
				}
			}
		}
		return false;
	

	}

	public static void left(Checker c)
	{
		if (c.getX() - GRID_WIDTH > BOARD_LEFT)
		{
			c.moveLeftOneColumn(GRID_WIDTH);
		}

	}

	public static void right(Checker c)
	{
		if (c.getX() + GRID_WIDTH < BOARD_LEFT + BOARD_WIDTH)
		{
			c.moveRightOneColumn(GRID_WIDTH);
		}
	}

	public static int getCol(Checker c)
	{
		int cornerX = c.getX() - GRID_WIDTH / 2;
		int distanceFromBoardLeft = (cornerX - BOARD_LEFT);
		int col = distanceFromBoardLeft / GRID_WIDTH;
		return col;
	}

	public static void down(Checker c)
	{

		// find out which column the checker is hovering above
		int col = getCol(c);

		// find out which row to place the checker into
		for (int row = NUM_ROWS - 1; row >= 0; row--)
		{
			// we found the slot to put the checker into
			if (availability[row][col] == EMPTY)
			{
				availability[row][col] = turn % 2; // slot is now occupied
				placeChecker(c, row, col);
				boolean won = checkForHorizontalWin(c);
				turn++;
				return;
			}
		}
	}

	public static void placeChecker(Checker c, int row, int col)
	{
		int newY = BOARD_TOP + row * GRID_WIDTH + GRID_WIDTH / 2;
		c.setY(newY); // checker location is now updated
		c.setPlaced(true); // checker is flagged as placed
		c.setRow(row); // update row & col of checker in the grid
		c.setCol(col);
	}


	public static Checker createNewChecker(int color)
	{
		Checker c = new Checker(CHECKER_BIRTH_CENTER_X, CHECKER_BIRTH_CENTER_Y, CHECKER_RADIUS, color);
		return c;
	}


	@Override
	public void keyReleased(KeyEvent e)
	{
		// TODO Auto-generated method stub

	}



	/**
	 * paint 		draw the window					// NEW C - Insert exactly this method
	 * 
	 * @param g		Graphics object to draw on - which we won't use
	 */
	public void paint(Graphics g)
	{
		BufferStrategy bf = this.getBufferStrategy();
		if (bf == null)
			return;

		Graphics g2 = null;

		try 
		{
			g2 = bf.getDrawGraphics();

			// myPaint does the actual drawing
			myPaint(g2);
		} 
		finally 
		{
			// It is best to dispose() a Graphics object when done with it.
			g2.dispose();
		}

		// Shows the contents of the backbuffer on the screen.
		bf.show();

		//Tell the System to do the Drawing now, otherwise it can take a few extra ms until 
		//Drawing is done which looks very jerky
		Toolkit.getDefaultToolkit().sync();	
	}

	public void myPaint(Graphics g)
	{
		// background
		g.setColor(BACKGROUND);
		g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

		// connect four board
		g.setColor(Color.YELLOW);
		g.fillRect(BOARD_LEFT, BOARD_TOP, BOARD_WIDTH, BOARD_HEIGHT);

		// draw the holes in the board
		int firstCornerX = BOARD_LEFT + GRID_WIDTH / 2 - CHECKER_RADIUS;
		int firstCornerY = BOARD_TOP + GRID_WIDTH / 2 - CHECKER_RADIUS;

		for (int row = 0; row < NUM_ROWS; row++)
		{
			for (int col = 0; col < NUM_COLS; col++)
			{
				g.setColor(BACKGROUND);

				int xCorner = firstCornerX + col * GRID_WIDTH;
				int yCorner = firstCornerY + row * GRID_WIDTH;
				g.fillOval(xCorner, yCorner, CHECKER_WIDTH, CHECKER_WIDTH);
			}
		}

		// draw placed checkers
		for (int i = 0; i < currentRedIndex - 1; i++)
		{
			redCheckers[i].draw(g);
		}

		for (int i = 0; i < currentBlueIndex - 1; i++)
		{
			blueCheckers[i].draw(g);
		}

		// draw the current checker
		if (turn % 2 == RED)
		{
			redCheckers[currentRedIndex - 1].draw(g);
		}
		else
		{
			blueCheckers[currentBlueIndex - 1].draw(g);
		}

	}



}
