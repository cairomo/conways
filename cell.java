
/*

 * Created on Dec 1, 2004

 * Last update: June 24, 2010

 */




import java.awt.Color;

import java.awt.Graphics;




public class Cell {

	private int myX, myY; // x,y position on grid

	private boolean myAlive; // alive (true) or dead (false)

	private int myNeighbors; // count of neighbors with respect to x,y

	private boolean myAliveNextTurn; // Used for state in next iteration

	private Color myColor; // Based on alive/dead rules

	private final Color DEFAULT_ALIVE = Color.ORANGE;

	private final Color DEFAULT_DEAD = Color.GRAY;




	public Cell(int x, int y) {

		this(x, y, false, Color.GRAY);

	}




	public Cell(int row, int col, boolean alive, Color color) {

		myAlive = alive;

		myColor = color;

		myX = col;

		myY = row;

	}




	public boolean getAlive() {

		return myAlive;

	}




	public int getX() {

		return myX;

	}




	public int getY() {

		return myY;

	}




	public Color getColor() {

		return myColor;

	}




	public void setAlive(boolean alive) {

		if (alive) {

			setAlive(true, DEFAULT_ALIVE);

		} else {

			setAlive(false, DEFAULT_DEAD);

		}

	}




	public void setAlive(boolean alive, Color color) {

		myColor = color;

		myAlive = alive;

	}




	public void setAliveNextTurn(boolean alive) {

		myAliveNextTurn = alive;

	}




	public boolean getAliveNextTurn() {

		return myAliveNextTurn;

	}




	public void setColor(Color color) {

		myColor = color;

	}




	public int getNeighbors() {

		return myNeighbors;

	}




	public void calcNeighbors(Cell[][] cell) {

		// You write this
	
		// You write this
		for(int i = myX-1;i< myX +1;i++){
			if(cell[i][myY+1].myAlive = true){
				myNeighbors = myNeighbors + 1;
			}
		}
		for(int i = myX-1;i< myX +1;i++){
			if(cell[i][myY-1].myAlive = true){
				myNeighbors = myNeighbors + 1;
			}
		}
		if(cell[myX-1][myY].myAlive = true){
			myNeighbors = myNeighbors + 1;
		}
		if(cell[myX+1][myY].myAlive = true){
			myNeighbors = myNeighbors + 1;
		}
	


	}




	public void draw(int x_offset, int y_offset, int width, int height,

			Graphics g) {

		// I leave this understanding to the reader

		int xleft = x_offset + 1 + (myX * (width + 1));

		int xright = x_offset + width + (myX * (width + 1));

		int ytop = y_offset + 1 + (myY * (height + 1));

		int ybottom = y_offset + height + (myY * (height + 1));

		Color temp = g.getColor();




		g.setColor(myColor);

		g.fillRect(xleft, ytop, width, height);

	}

}


