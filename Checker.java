import java.awt.*;

/************************************************************************
 * Checker class - each checker represents a ConnectFour piece.
 * Based on Ball class.
 * @author kat
 * 
 * June 23, 2019
 ************************************************************************/

class Checker
{
	// DATA:
	private int x, y;		// center of checker
	private int row, col;  // row and col of checker
	private int dy = 0;	 // y-velocity of checker	- currently set to 0
	private int radius;	// radius of checker	
	private Color color;  // color of checker
	private boolean placed = false; // has the checker been placed in the grid?

	// METHODS:

	/**
	 * constructor method
	 * @param xIn - center of ball
	 * @param yIn
	 * @param dxIn
	 * @param dyIn
	 * @param radiusIn
	 * @param colorIn
	 */
	public Checker (int xIn, int yIn, int radiusIn, Color colorIn)
	{
		x = xIn;
		y = yIn;
		radius = radiusIn;
		color = colorIn;
	}
	
	public int getRow()
	{
		return row;
	}
	
	public void setRow(int rowIn)
	{
		row = rowIn;
	}
	
	public int getCol()
	{
		return col;
	}
	
	public void setCol(int colIn)
	{
		col = colIn;
	}
	
	public int getColorAsInt()
	{
		if (color.equals(Color.RED))
		{
			return ConnectFour.RED;
		}
		else
		{
			return ConnectFour.BLUE;
		}
	}

	/**
	 * Move the ball.  Add the velocity to its center.
	 */
	public void move()
	{
		y = y + dy;
	}
	
	/*
	 * getter method for the boolean instance variable placed 
	 */
	public boolean placed()
	{
		return placed;
	}
	
	public void setPlaced(boolean newValue)
	{
		placed = newValue;
	}
	
	public double getY()
	{
		return y;
	}
	
	public int getX()
	{
		return x;
	}
	
	public void setY(int newY)
	{
		y = newY;
	}
	
	public void moveLeftOneColumn(int stepSize)
	{
		x = x - stepSize;
	}
	public void moveRightOneColumn(int stepSize)
	{
		x = x + stepSize;
	}
	
	

	/**
	 * This method draws the ball.
	 * @param g (graphics object)
	 */
	public void draw(Graphics g)
	{
		g.setColor(color);
		g.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
	}
}