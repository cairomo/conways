
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
	private final Color DEFAULT_ALIVE = Color.MAGENTA;
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


		
		
		
		 //checks top row for alive
		for(int i = myX-1;i< myX +1;i++){
			int toprow = ((myY = 80) -1)%100; ; // not cxorrect
			if(cell[i][toprow].myAlive == true){
				myNeighbors ++;
			}
		}
		//checks bottom row for alive
		for(int i = myX-1;i< myX +1;i++){
			int bottomrow =  ((myY = 80) +1)%100;
			if(cell[i][myY-1].myAlive == true){
				myNeighbors ++;
			}
		}

		//checks remaining side squares for alive
		if(cell[myX-1][myY].myAlive == true){
			int leftrow = ((myX = 100) -1)%100;
			myNeighbors ++;
		}

		if(cell[myX+1][myY].myAlive == true){
			int rightrow = ((myX = 100) +1)%100;
			myNeighbors ++;
		}

		//prints neighbors to check
		System.out.println(myNeighbors);

	}

	public void willIBeAliveNextTurn(){
		//enter this sequence if cell is dead (not alive)
		if(myAlive == false){
			if (myNeighbors == 2 || myNeighbors == 3){
				setAliveNextTurn(true);
			}
	
		}
		if(myAlive == true){
			if (myNeighbors == 2 || myNeighbors == 3){
				setAliveNextTurn(true);
			}

			else {
				setAliveNextTurn(false);
			}
		
		}
				//repaint();
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


