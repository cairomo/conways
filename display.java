import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

// Note that the JComponent is set up to listen for mouse clicks
// and mouse movement.  To achieve this, the MouseListener and
// MousMotionListener interfaces are implemented and there is additional
// code in init() to attach those interfaces to the JComponent.

public class Display extends JComponent implements MouseListener, MouseMotionListener {
	public static final int ROWS = 80;
	public static final int COLS = 100;
	public static Cell[][] cell = new Cell[ROWS][COLS];
	private final int X_GRID_OFFSET = 25; // 25 pixels from left
	private final int Y_GRID_OFFSET = 40; // 40 pixels from top
	private final int CELL_WIDTH = 5;
	private final int CELL_HEIGHT = 5;

	// Note that a final field can be initialized in constructor
	private final int DISPLAY_WIDTH; 
	private final int DISPLAY_HEIGHT;
	private StartButton startStop;
	private ClearButton Clear;
	private StepButton Step;
	private RocketButton Rocket;
	private PeaceButton Peace;
	private JLabel description;
	//private ChooseBox Choose;
	private boolean paintloop = false;

	public Display(int width, int height) {
		DISPLAY_WIDTH = width;
		DISPLAY_HEIGHT = height;
		init();
	}

	public void init() {
		setSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
		initCells();

		addMouseListener(this);
		addMouseMotionListener(this);
		
		description = new JLabel("Click and drag on the grid, then press start");
		description.setSize(950,20);
		add(description);
		description.setLocation(220, 20);
		description.setVisible(true);
		
		//sets up buttons
		startStop = new StartButton();
		startStop.setBounds(100, 550, 100, 36);
		add(startStop);
		startStop.setVisible(true);
		startStop.setToolTipText("Click to start simulation sequence");


		//Clear button
		Clear = new ClearButton();
		Clear.setBounds(200, 550, 100, 36);
		add(Clear);
		Clear.setVisible(true);
		Clear.setToolTipText("Click to clear grid of cells");

		//Step button
		Step = new StepButton();
		Step.setBounds(300, 550, 100, 36);
		add(Step);
		Step.setVisible(true);
		Step.setToolTipText("Click to move one generation at a time");
		
		Rocket = new RocketButton();
		Rocket.setBounds(400, 550, 100, 36);
		add(Rocket);
		Rocket.setVisible(true);
		Rocket.setToolTipText("Click to display a preset arrangement of cells in the shape of a rocket");
		
		Peace = new PeaceButton();
		Peace.setBounds(500, 550, 100, 36);
		add(Peace);
		Peace.setVisible(true);
		Peace.setToolTipText("Click to display a preset arrangement of cells in the shape of a peace sign");

		repaint();
	}

	public void paintComponent(Graphics g) {
		final int TIME_BETWEEN_REPLOTS = 100; // change to your liking

		g.setColor(Color.BLACK);
		drawGrid(g);
		drawCells(g);
		drawButtons();

		if (paintloop) {
			try {
				Thread.sleep(TIME_BETWEEN_REPLOTS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			nextGeneration();
			update();
		}
		update();
	}
	
	public void update() {
		repaint();
		Cell.turnCount++;
	}

	public void initCells() {
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				cell[row][col] = new Cell(row, col);
			}
		}
		//no cells initially alive
	//	cell[41][42].calcNeighbors(cell);
		//[y][x]
	}

	public void togglePaintLoop() {
		paintloop = !paintloop;
	}

	public void setPaintLoop(boolean value) {
		paintloop = value;
	}

	void drawGrid(Graphics g) {
		for (int row = 0; row <= ROWS; row++) {
			g.drawLine(X_GRID_OFFSET,
					Y_GRID_OFFSET + (row * (CELL_HEIGHT + 1)), X_GRID_OFFSET
					+ COLS * (CELL_WIDTH + 1), Y_GRID_OFFSET
					+ (row * (CELL_HEIGHT + 1)));
		}
		for (int col = 0; col <= COLS; col++) {
			g.drawLine(X_GRID_OFFSET + (col * (CELL_WIDTH + 1)), Y_GRID_OFFSET,
					X_GRID_OFFSET + (col * (CELL_WIDTH + 1)), Y_GRID_OFFSET
					+ ROWS * (CELL_HEIGHT + 1));
		}
	}


	void drawCells(Graphics g) {
		// Have each cell draw itself
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				// The cell cannot know for certain the offsets nor the height
				// and width; it has been set up to know its own position, so
				// that need not be passed as an argument to the draw method
				cell[row][col].draw(X_GRID_OFFSET, Y_GRID_OFFSET, CELL_WIDTH,
						CELL_HEIGHT, g);
			}
		}
	}

	private void drawButtons() {
		startStop.repaint();
	}

	private void nextGeneration() {
		for(int row = 0; row < ROWS; row++){
			for(int col = 0; col < COLS; col++){

				//Invoking calcNeighbors and willIBeAliveNextTurn
				cell[row][col].calcNeighbors(cell);
				cell[row][col].willIBeAliveNextTurn();
				update();
				
			}
			update();
		}
		
		//Sets whether you will be alive in the next turn given calcNeighbors
		for(int row = 0; row < ROWS; row++){
			for(int col = 0; col < COLS; col++){
				boolean resultofIWillBeAlive = cell[row][col].getAliveNextTurn();
						cell[row][col].setAlive(resultofIWillBeAlive);
					update();
			}
			update();
		}
		//repaint();
	}

	//When the mouse is clicked, it finds the cell that was clicked and
	//toggles the cell between alive or dead
	public void mouseClicked(MouseEvent arg0) {
		int x, y;
		x =arg0.getX();
		y = arg0.getY();
		x = ((arg0.getX() -X_GRID_OFFSET)/6);
		y = ((arg0.getY() -Y_GRID_OFFSET)/6);
		boolean makeAlive=false;
		if(makeAlive){
			cell[y][x].setAlive(true);  //setAlive(true);

			update();
		}
		else{
			cell[y][x].setAlive(false);  //setAlive(true);

		}
		update();


	}

	public void mouseEntered(MouseEvent arg0) {

	}

	public void mouseExited(MouseEvent arg0) {

	}

	public void mousePressed(MouseEvent arg0) {

	}

	public void mouseReleased(MouseEvent arg0) {

	}
	//When the mouse is dragged across cells, it finds the cells 
	//that were dragged across and toggles the cell between alive or dead
	public void mouseDragged(MouseEvent arg0) {
		int x, y;
		x =arg0.getX();
		y = arg0.getY();
		x = ((arg0.getX() -X_GRID_OFFSET)/6);
		y = ((arg0.getY() -Y_GRID_OFFSET)/6);
		cell[y][x].setAlive(true);  //setAlive(true);
		getLocation(arg0);
		x = 0;
		y= 0;
		update();
	}

	public void mouseMoved(MouseEvent arg0) {

	}
	//prints grid location to console for easy button creation
	public void getLocation(MouseEvent arg0) {
		int x, y;
		x =arg0.getX();
		y = arg0.getY();
		x = ((arg0.getX() -X_GRID_OFFSET)/6);
		y = ((arg0.getY() -Y_GRID_OFFSET)/6);
		String retVal = "cell[" + y + "][" + x + "].setAlive(true);";
		System.out.println(retVal);
	}

//Creates start/stop button that when clicked either starts or stops the program
	private class StartButton extends JButton implements ActionListener {
		StartButton() {
			super("Start");
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent arg0) {
			// nextGeneration(); // test the start button
			if (this.getText().equals("Start")) {
				togglePaintLoop();
				setText("Stop");
				nextGeneration();
				repaint();
				
			} else {
				togglePaintLoop();
				setText("Start");
			}
		}
	}
	
	//Creates a button that when clicked advances the cells to the next 
	//turn 
	//logic: do nextgeneration but only one, then pause.
	private class StepButton extends JButton implements ActionListener {
		StepButton() {
			super("Step");
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent arg0) {
			// nextGeneration(); // test the start button
			if (this.getText().equals("Step")) {
				togglePaintLoop();
				setText("Step");
				togglePaintLoop();
				//cell[ROWS][COLS].willIBeAliveNextTurn();
				nextGeneration();
				repaint();
			} 
			
		}
	}
	
	//Creates a button that when clicked stops the program until the 
	//start button is clicked to resume it
	
	
	//Creates a button that when clicked resets the board so that all 
	// cells are dead
	private class ClearButton extends JButton implements ActionListener {
		ClearButton() {
			super("Clear");		
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent arg0) {
			// nextGeneration(); // test the start button
			for(int ROW = 0;ROW <100; ROW++){
				for(int col = 0; col < 80; col++) {
					cell[col][ROW].setAlive(false);
					repaint();
				}

			}

		
		}
	
	
	}
	
	private class PeaceButton extends JButton implements ActionListener {
		PeaceButton() {
			super("Draw Peace");		
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent arg0) {
			// REALLY LONG TEXT GENERATED FROM getLocation, BEWARE
			cell[21][52].setAlive(true);
			cell[21][52].setAlive(true);
			cell[21][51].setAlive(true);
			cell[21][51].setAlive(true);
			cell[21][50].setAlive(true);
			cell[20][49].setAlive(true);
			cell[20][48].setAlive(true);
			cell[20][47].setAlive(true);
			cell[20][47].setAlive(true);
			cell[20][46].setAlive(true);
			cell[20][45].setAlive(true);
			cell[20][45].setAlive(true);
			cell[20][44].setAlive(true);
			cell[20][44].setAlive(true);
			cell[20][43].setAlive(true);
			cell[20][42].setAlive(true);
			cell[20][41].setAlive(true);
			cell[21][40].setAlive(true);
			cell[21][40].setAlive(true);
			cell[21][39].setAlive(true);
			cell[22][38].setAlive(true);
			cell[22][37].setAlive(true);
			cell[23][36].setAlive(true);
			cell[24][35].setAlive(true);
			cell[24][35].setAlive(true);
			cell[25][34].setAlive(true);
			cell[25][33].setAlive(true);
			cell[26][32].setAlive(true);
			cell[26][32].setAlive(true);
			cell[27][32].setAlive(true);
			cell[27][31].setAlive(true);
			cell[28][31].setAlive(true);
			cell[29][31].setAlive(true);
			cell[30][30].setAlive(true);
			cell[31][29].setAlive(true);
			cell[32][29].setAlive(true);
			cell[33][29].setAlive(true);
			cell[33][28].setAlive(true);
			cell[34][28].setAlive(true);
			cell[35][28].setAlive(true);
			cell[36][28].setAlive(true);
			cell[38][28].setAlive(true);
			cell[38][28].setAlive(true);
			cell[39][28].setAlive(true);
			cell[40][28].setAlive(true);
			cell[41][28].setAlive(true);
			cell[41][28].setAlive(true);
			cell[42][28].setAlive(true);
			cell[42][28].setAlive(true);
			cell[43][28].setAlive(true);
			cell[44][28].setAlive(true);
			cell[44][28].setAlive(true);
			cell[45][28].setAlive(true);
			cell[45][28].setAlive(true);
			cell[46][29].setAlive(true);
			cell[47][29].setAlive(true);
			cell[47][29].setAlive(true);
			cell[48][29].setAlive(true);
			cell[48][29].setAlive(true);
			cell[49][29].setAlive(true);
			cell[49][30].setAlive(true);
			cell[50][30].setAlive(true);
			cell[50][30].setAlive(true);
			cell[51][31].setAlive(true);
			cell[52][31].setAlive(true);
			cell[52][31].setAlive(true);
			cell[53][32].setAlive(true);
			cell[54][33].setAlive(true);
			cell[55][33].setAlive(true);
			cell[55][34].setAlive(true);
			cell[56][35].setAlive(true);
			cell[57][36].setAlive(true);
			cell[58][37].setAlive(true);
			cell[59][38].setAlive(true);
			cell[60][40].setAlive(true);
			cell[60][41].setAlive(true);
			cell[61][43].setAlive(true);
			cell[61][43].setAlive(true);
			cell[61][43].setAlive(true);
			cell[61][44].setAlive(true);
			cell[61][44].setAlive(true);
			cell[61][45].setAlive(true);
			cell[61][46].setAlive(true);
			cell[61][46].setAlive(true);
			cell[61][47].setAlive(true);
			cell[61][48].setAlive(true);
			cell[62][49].setAlive(true);
			cell[62][49].setAlive(true);
			cell[62][50].setAlive(true);
			cell[62][51].setAlive(true);
			cell[62][51].setAlive(true);
			cell[62][52].setAlive(true);
			cell[62][52].setAlive(true);
			cell[62][53].setAlive(true);
			cell[62][54].setAlive(true);
			cell[61][54].setAlive(true);
			cell[61][54].setAlive(true);
			cell[61][55].setAlive(true);
			cell[61][56].setAlive(true);
			cell[60][56].setAlive(true);
			cell[60][57].setAlive(true);
			cell[60][58].setAlive(true);
			cell[59][59].setAlive(true);
			cell[59][60].setAlive(true);
			cell[58][60].setAlive(true);
			cell[58][60].setAlive(true);
			cell[58][60].setAlive(true);
			cell[57][61].setAlive(true);
			cell[56][61].setAlive(true);
			cell[56][62].setAlive(true);
			cell[55][62].setAlive(true);
			cell[54][63].setAlive(true);
			cell[54][63].setAlive(true);
			cell[54][63].setAlive(true);
			cell[53][64].setAlive(true);
			cell[53][64].setAlive(true);
			cell[53][64].setAlive(true);
			cell[52][64].setAlive(true);
			cell[52][64].setAlive(true);
			cell[52][65].setAlive(true);
			cell[51][65].setAlive(true);
			cell[50][65].setAlive(true);
			cell[50][65].setAlive(true);
			cell[49][66].setAlive(true);
			cell[49][66].setAlive(true);
			cell[48][66].setAlive(true);
			cell[48][66].setAlive(true);
			cell[47][66].setAlive(true);
			cell[47][66].setAlive(true);
			cell[46][66].setAlive(true);
			cell[46][66].setAlive(true);
			cell[45][66].setAlive(true);
			cell[45][66].setAlive(true);
			cell[44][67].setAlive(true);
			cell[43][67].setAlive(true);
			cell[43][67].setAlive(true);
			cell[42][67].setAlive(true);
			cell[41][67].setAlive(true);
			cell[40][67].setAlive(true);
			cell[40][67].setAlive(true);
			cell[39][67].setAlive(true);
			cell[38][67].setAlive(true);
			cell[37][67].setAlive(true);
			cell[36][66].setAlive(true);
			cell[34][66].setAlive(true);
			cell[33][66].setAlive(true);
			cell[33][66].setAlive(true);
			cell[32][66].setAlive(true);
			cell[32][65].setAlive(true);
			cell[30][65].setAlive(true);
			cell[30][64].setAlive(true);
			cell[29][64].setAlive(true);
			cell[28][64].setAlive(true);
			cell[27][64].setAlive(true);
			cell[27][64].setAlive(true);
			cell[27][64].setAlive(true);
			cell[27][63].setAlive(true);
			cell[27][63].setAlive(true);
			cell[27][63].setAlive(true);
			cell[27][63].setAlive(true);
			cell[26][63].setAlive(true);
			cell[26][63].setAlive(true);
			cell[26][62].setAlive(true);
			cell[26][62].setAlive(true);
			cell[26][62].setAlive(true);
			cell[26][61].setAlive(true);
			cell[26][61].setAlive(true);
			cell[25][61].setAlive(true);
			cell[25][61].setAlive(true);
			cell[25][60].setAlive(true);
			cell[25][60].setAlive(true);
			cell[24][60].setAlive(true);
			cell[24][59].setAlive(true);
			cell[23][58].setAlive(true);
			cell[23][58].setAlive(true);
			cell[23][58].setAlive(true);
			cell[23][58].setAlive(true);
			cell[23][57].setAlive(true);
			cell[23][57].setAlive(true);
			cell[22][57].setAlive(true);
			cell[22][56].setAlive(true);
			cell[22][56].setAlive(true);
			cell[22][55].setAlive(true);
			cell[22][55].setAlive(true);
			cell[22][54].setAlive(true);
			cell[22][54].setAlive(true);
			cell[22][54].setAlive(true);
			cell[21][54].setAlive(true);
			cell[21][53].setAlive(true);
			cell[21][53].setAlive(true);
			cell[21][53].setAlive(true);
			cell[21][53].setAlive(true);
			cell[21][52].setAlive(true);
			cell[21][52].setAlive(true);
			cell[21][52].setAlive(true);
			cell[21][52].setAlive(true);
			cell[21][51].setAlive(true);
			cell[21][51].setAlive(true);
			cell[21][51].setAlive(true);
			cell[21][51].setAlive(true);
			cell[21][51].setAlive(true);
			cell[21][51].setAlive(true);
			cell[21][50].setAlive(true);
			cell[21][50].setAlive(true);
			cell[21][50].setAlive(true);
			cell[21][50].setAlive(true);
			cell[21][50].setAlive(true);
			cell[21][50].setAlive(true);
			cell[21][49].setAlive(true);
			cell[21][49].setAlive(true);
			cell[21][49].setAlive(true);
			cell[20][49].setAlive(true);
			cell[20][49].setAlive(true);
			cell[20][48].setAlive(true);
			cell[20][48].setAlive(true);
			cell[20][48].setAlive(true);
			cell[20][48].setAlive(true);
			cell[20][48].setAlive(true);
			cell[20][48].setAlive(true);
			cell[21][48].setAlive(true);
			cell[21][48].setAlive(true);
			cell[22][48].setAlive(true);
			cell[22][48].setAlive(true);
			cell[22][48].setAlive(true);
			cell[23][48].setAlive(true);
			cell[24][48].setAlive(true);
			cell[24][48].setAlive(true);
			cell[25][48].setAlive(true);
			cell[25][48].setAlive(true);
			cell[25][48].setAlive(true);
			cell[26][48].setAlive(true);
			cell[26][48].setAlive(true);
			cell[27][48].setAlive(true);
			cell[27][48].setAlive(true);
			cell[27][48].setAlive(true);
			cell[28][48].setAlive(true);
			cell[29][48].setAlive(true);
			cell[29][48].setAlive(true);
			cell[30][48].setAlive(true);
			cell[30][48].setAlive(true);
			cell[31][48].setAlive(true);
			cell[31][48].setAlive(true);
			cell[31][48].setAlive(true);
			cell[32][48].setAlive(true);
			cell[32][48].setAlive(true);
			cell[33][48].setAlive(true);
			cell[33][48].setAlive(true);
			cell[34][48].setAlive(true);
			cell[34][48].setAlive(true);
			cell[34][48].setAlive(true);
			cell[35][48].setAlive(true);
			cell[36][48].setAlive(true);
			cell[37][48].setAlive(true);
			cell[37][48].setAlive(true);
			cell[38][48].setAlive(true);
			cell[38][48].setAlive(true);
			cell[39][48].setAlive(true);
			cell[39][48].setAlive(true);
			cell[39][48].setAlive(true);
			cell[40][48].setAlive(true);
			cell[40][48].setAlive(true);
			cell[41][48].setAlive(true);
			cell[41][48].setAlive(true);
			cell[42][48].setAlive(true);
			cell[42][48].setAlive(true);
			cell[42][48].setAlive(true);
			cell[43][48].setAlive(true);
			cell[43][48].setAlive(true);
			cell[43][48].setAlive(true);
			cell[44][48].setAlive(true);
			cell[45][48].setAlive(true);
			cell[45][48].setAlive(true);
			cell[46][48].setAlive(true);
			cell[46][48].setAlive(true);
			cell[46][48].setAlive(true);
			cell[47][48].setAlive(true);
			cell[47][48].setAlive(true);
			cell[48][48].setAlive(true);
			cell[48][48].setAlive(true);
			cell[48][48].setAlive(true);
			cell[49][48].setAlive(true);
			cell[49][48].setAlive(true);
			cell[49][48].setAlive(true);
			cell[50][48].setAlive(true);
			cell[50][48].setAlive(true);
			cell[51][48].setAlive(true);
			cell[51][48].setAlive(true);
			cell[51][48].setAlive(true);
			cell[52][48].setAlive(true);
			cell[52][48].setAlive(true);
			cell[52][48].setAlive(true);
			cell[53][48].setAlive(true);
			cell[53][48].setAlive(true);
			cell[54][48].setAlive(true);
			cell[54][48].setAlive(true);
			cell[55][48].setAlive(true);
			cell[55][48].setAlive(true);
			cell[55][48].setAlive(true);
			cell[55][48].setAlive(true);
			cell[55][48].setAlive(true);
			cell[55][48].setAlive(true);
			cell[56][48].setAlive(true);
			cell[56][48].setAlive(true);
			cell[56][48].setAlive(true);
			cell[56][48].setAlive(true);
			cell[56][48].setAlive(true);
			cell[56][48].setAlive(true);
			cell[57][48].setAlive(true);
			cell[57][48].setAlive(true);
			cell[57][48].setAlive(true);
			cell[57][48].setAlive(true);
			cell[58][48].setAlive(true);
			cell[58][48].setAlive(true);
			cell[58][48].setAlive(true);
			cell[58][48].setAlive(true);
			cell[58][48].setAlive(true);
			cell[59][48].setAlive(true);
			cell[59][48].setAlive(true);
			cell[59][48].setAlive(true);
			cell[59][48].setAlive(true);
			cell[59][48].setAlive(true);
			cell[59][48].setAlive(true);
			cell[59][48].setAlive(true);
			cell[60][48].setAlive(true);
			cell[60][48].setAlive(true);
			cell[60][48].setAlive(true);
			cell[60][48].setAlive(true);
			cell[60][48].setAlive(true);
			cell[60][48].setAlive(true);
			cell[60][48].setAlive(true);
			cell[61][48].setAlive(true);
			cell[61][48].setAlive(true);
			cell[61][48].setAlive(true);
			cell[53][31].setAlive(true);
			cell[52][32].setAlive(true);
			cell[52][32].setAlive(true);
			cell[52][33].setAlive(true);
			cell[52][33].setAlive(true);
			cell[51][33].setAlive(true);
			cell[51][33].setAlive(true);
			cell[51][34].setAlive(true);
			cell[50][34].setAlive(true);
			cell[50][35].setAlive(true);
			cell[50][35].setAlive(true);
			cell[50][36].setAlive(true);
			cell[49][36].setAlive(true);
			cell[49][37].setAlive(true);
			cell[49][37].setAlive(true);
			cell[48][38].setAlive(true);
			cell[48][38].setAlive(true);
			cell[47][39].setAlive(true);
			cell[47][39].setAlive(true);
			cell[47][40].setAlive(true);
			cell[46][40].setAlive(true);
			cell[46][40].setAlive(true);
			cell[46][41].setAlive(true);
			cell[46][41].setAlive(true);
			cell[45][41].setAlive(true);
			cell[45][42].setAlive(true);
			cell[45][42].setAlive(true);
			cell[44][42].setAlive(true);
			cell[44][42].setAlive(true);
			cell[44][43].setAlive(true);
			cell[43][43].setAlive(true);
			cell[43][43].setAlive(true);
			cell[43][44].setAlive(true);
			cell[43][44].setAlive(true);
			cell[42][44].setAlive(true);
			cell[42][44].setAlive(true);
			cell[42][45].setAlive(true);
			cell[42][45].setAlive(true);
			cell[42][45].setAlive(true);
			cell[41][46].setAlive(true);
			cell[41][46].setAlive(true);
			cell[41][46].setAlive(true);
			cell[41][46].setAlive(true);
			cell[41][47].setAlive(true);
			cell[41][47].setAlive(true);
			cell[40][47].setAlive(true);
			cell[40][47].setAlive(true);
			cell[40][47].setAlive(true);
			cell[40][47].setAlive(true);
			cell[40][48].setAlive(true);
			cell[40][48].setAlive(true);
			cell[40][48].setAlive(true);
			cell[40][48].setAlive(true);
			cell[40][48].setAlive(true);
			cell[40][48].setAlive(true);
			cell[40][48].setAlive(true);
			cell[40][48].setAlive(true);
			cell[40][49].setAlive(true);
			cell[40][49].setAlive(true);
			cell[40][49].setAlive(true);
			cell[40][49].setAlive(true);
			cell[40][49].setAlive(true);
			cell[40][50].setAlive(true);
			cell[41][50].setAlive(true);
			cell[41][50].setAlive(true);
			cell[41][50].setAlive(true);
			cell[41][51].setAlive(true);
			cell[42][51].setAlive(true);
			cell[42][51].setAlive(true);
			cell[42][51].setAlive(true);
			cell[42][52].setAlive(true);
			cell[42][52].setAlive(true);
			cell[43][52].setAlive(true);
			cell[43][52].setAlive(true);
			cell[43][53].setAlive(true);
			cell[43][53].setAlive(true);
			cell[44][53].setAlive(true);
			cell[44][53].setAlive(true);
			cell[44][53].setAlive(true);
			cell[44][54].setAlive(true);
			cell[44][54].setAlive(true);
			cell[45][54].setAlive(true);
			cell[45][54].setAlive(true);
			cell[45][55].setAlive(true);
			cell[45][55].setAlive(true);
			cell[46][55].setAlive(true);
			cell[46][56].setAlive(true);
			cell[47][56].setAlive(true);
			cell[47][57].setAlive(true);
			cell[47][57].setAlive(true);
			cell[47][57].setAlive(true);
			cell[47][57].setAlive(true);
			cell[47][58].setAlive(true);
			cell[48][58].setAlive(true);
			cell[48][58].setAlive(true);
			cell[48][59].setAlive(true);
			cell[48][59].setAlive(true);
			cell[48][59].setAlive(true);
			cell[49][59].setAlive(true);
			cell[49][60].setAlive(true);
			cell[49][60].setAlive(true);
			cell[49][60].setAlive(true);
			cell[49][60].setAlive(true);
			cell[50][61].setAlive(true);
			cell[50][61].setAlive(true);
			cell[50][61].setAlive(true);
			cell[50][61].setAlive(true);
			cell[51][62].setAlive(true);
			cell[51][62].setAlive(true);
			cell[51][62].setAlive(true);
			cell[51][62].setAlive(true);
			cell[51][63].setAlive(true);
			cell[51][63].setAlive(true);
			cell[51][63].setAlive(true);
			cell[52][63].setAlive(true);
			cell[52][63].setAlive(true);
			cell[52][63].setAlive(true);
			cell[52][64].setAlive(true);
		}
	
	
	}
	
	private class RocketButton extends JButton implements ActionListener {
		RocketButton() {
			super("Draw Rocket");		
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent arg0) {
			//makes a rocket shape
			cell[53][43].setAlive(true);
			cell[52][43].setAlive(true);
			cell[52][42].setAlive(true);
			cell[52][42].setAlive(true);
			cell[51][42].setAlive(true);
			cell[51][42].setAlive(true);
			cell[51][42].setAlive(true);
			cell[50][41].setAlive(true);
			cell[50][41].setAlive(true);
			cell[49][41].setAlive(true);
			cell[49][41].setAlive(true);
			cell[49][41].setAlive(true);
			cell[48][41].setAlive(true);
			cell[48][41].setAlive(true);
			cell[47][40].setAlive(true);
			cell[47][40].setAlive(true);
			cell[46][40].setAlive(true);
			cell[46][40].setAlive(true);
			cell[46][40].setAlive(true);
			cell[45][40].setAlive(true);
			cell[44][40].setAlive(true);
			cell[44][40].setAlive(true);
			cell[43][40].setAlive(true);
			cell[43][40].setAlive(true);
			cell[42][40].setAlive(true);
			cell[41][40].setAlive(true);
			cell[41][40].setAlive(true);
			cell[40][40].setAlive(true);
			cell[39][40].setAlive(true);
			cell[39][40].setAlive(true);
			cell[38][40].setAlive(true);
			cell[37][40].setAlive(true);
			cell[36][41].setAlive(true);
			cell[36][41].setAlive(true);
			cell[36][41].setAlive(true);
			cell[35][41].setAlive(true);
			cell[34][42].setAlive(true);
			cell[34][42].setAlive(true);
			cell[33][42].setAlive(true);
			cell[33][42].setAlive(true);
			cell[33][43].setAlive(true);
			cell[32][43].setAlive(true);
			cell[31][43].setAlive(true);
			cell[31][43].setAlive(true);
			cell[31][43].setAlive(true);
			cell[30][44].setAlive(true);
			cell[30][44].setAlive(true);
			cell[29][44].setAlive(true);
			cell[29][44].setAlive(true);
			cell[28][45].setAlive(true);
			cell[28][45].setAlive(true);
			cell[27][45].setAlive(true);
			cell[27][45].setAlive(true);
			cell[27][45].setAlive(true);
			cell[27][45].setAlive(true);
			cell[26][46].setAlive(true);
			cell[26][46].setAlive(true);
			cell[26][46].setAlive(true);
			cell[26][46].setAlive(true);
			cell[25][46].setAlive(true);
			cell[25][46].setAlive(true);
			cell[25][46].setAlive(true);
			cell[25][46].setAlive(true);
			cell[24][47].setAlive(true);
			cell[24][47].setAlive(true);
			cell[24][47].setAlive(true);
			cell[24][47].setAlive(true);
			cell[24][47].setAlive(true);
			cell[25][47].setAlive(true);
			cell[25][47].setAlive(true);
			cell[25][47].setAlive(true);
			cell[25][48].setAlive(true);
			cell[25][48].setAlive(true);
			cell[26][48].setAlive(true);
			cell[26][48].setAlive(true);
			cell[26][48].setAlive(true);
			cell[26][48].setAlive(true);
			cell[27][49].setAlive(true);
			cell[27][49].setAlive(true);
			cell[27][49].setAlive(true);
			cell[27][50].setAlive(true);
			cell[28][50].setAlive(true);
			cell[28][50].setAlive(true);
			cell[28][50].setAlive(true);
			cell[29][50].setAlive(true);
			cell[30][50].setAlive(true);
			cell[30][51].setAlive(true);
			cell[30][51].setAlive(true);
			cell[32][51].setAlive(true);
			cell[32][51].setAlive(true);
			cell[33][51].setAlive(true);
			cell[34][52].setAlive(true);
			cell[35][52].setAlive(true);
			cell[37][52].setAlive(true);
			cell[38][52].setAlive(true);
			cell[39][52].setAlive(true);
			cell[41][53].setAlive(true);
			cell[43][53].setAlive(true);
			cell[43][53].setAlive(true);
			cell[43][53].setAlive(true);
			cell[43][53].setAlive(true);
			cell[43][53].setAlive(true);
			cell[42][53].setAlive(true);
			cell[41][53].setAlive(true);
			cell[41][53].setAlive(true);
			cell[41][53].setAlive(true);
			cell[40][53].setAlive(true);
			cell[40][53].setAlive(true);
			cell[40][53].setAlive(true);
			cell[39][53].setAlive(true);
			cell[39][53].setAlive(true);
			cell[39][53].setAlive(true);
			cell[38][53].setAlive(true);
			cell[38][53].setAlive(true);
			cell[38][52].setAlive(true);
			cell[37][52].setAlive(true);
			cell[37][52].setAlive(true);
			cell[36][52].setAlive(true);
			cell[36][52].setAlive(true);
			cell[36][52].setAlive(true);
			cell[36][52].setAlive(true);
			cell[36][52].setAlive(true);
			cell[35][52].setAlive(true);
			cell[35][52].setAlive(true);
			cell[35][52].setAlive(true);
			cell[34][52].setAlive(true);
			cell[34][52].setAlive(true);
			cell[34][52].setAlive(true);
			cell[34][52].setAlive(true);
			cell[34][52].setAlive(true);
			cell[33][52].setAlive(true);
			cell[34][52].setAlive(true);
			cell[34][52].setAlive(true);
			cell[34][52].setAlive(true);
			cell[35][52].setAlive(true);
			cell[35][52].setAlive(true);
			cell[36][52].setAlive(true);
			cell[36][52].setAlive(true);
			cell[37][52].setAlive(true);
			cell[37][52].setAlive(true);
			cell[38][52].setAlive(true);
			cell[38][53].setAlive(true);
			cell[38][53].setAlive(true);
			cell[39][53].setAlive(true);
			cell[39][53].setAlive(true);
			cell[40][53].setAlive(true);
			cell[41][53].setAlive(true);
			cell[41][53].setAlive(true);
			cell[42][53].setAlive(true);
			cell[42][53].setAlive(true);
			cell[42][53].setAlive(true);
			cell[43][53].setAlive(true);
			cell[43][53].setAlive(true);
			cell[43][53].setAlive(true);
			cell[44][53].setAlive(true);
			cell[45][53].setAlive(true);
			cell[45][53].setAlive(true);
			cell[46][53].setAlive(true);
			cell[46][53].setAlive(true);
			cell[47][53].setAlive(true);
			cell[47][53].setAlive(true);
			cell[47][53].setAlive(true);
			cell[48][53].setAlive(true);
			cell[49][53].setAlive(true);
			cell[50][52].setAlive(true);
			cell[50][52].setAlive(true);
			cell[51][52].setAlive(true);
			cell[51][52].setAlive(true);
			cell[51][52].setAlive(true);
			cell[51][52].setAlive(true);
			cell[51][52].setAlive(true);
			cell[51][52].setAlive(true);
			cell[52][52].setAlive(true);
			cell[52][52].setAlive(true);
			cell[52][52].setAlive(true);
			cell[52][52].setAlive(true);
			cell[52][51].setAlive(true);
			cell[53][51].setAlive(true);
			cell[53][51].setAlive(true);
			cell[53][51].setAlive(true);
			cell[53][51].setAlive(true);
			cell[53][51].setAlive(true);
			cell[53][50].setAlive(true);
			cell[53][50].setAlive(true);
			cell[53][50].setAlive(true);
			cell[53][50].setAlive(true);
			cell[53][49].setAlive(true);
			cell[53][49].setAlive(true);
			cell[53][48].setAlive(true);
			cell[53][48].setAlive(true);
			cell[53][48].setAlive(true);
			cell[53][48].setAlive(true);
			cell[53][47].setAlive(true);
			cell[53][47].setAlive(true);
			cell[53][47].setAlive(true);
			cell[53][46].setAlive(true);
			cell[53][46].setAlive(true);
			cell[53][46].setAlive(true);
			cell[53][46].setAlive(true);
			cell[53][46].setAlive(true);
			cell[53][45].setAlive(true);
			cell[53][45].setAlive(true);
			cell[53][45].setAlive(true);
			cell[53][45].setAlive(true);
			cell[53][45].setAlive(true);
			cell[53][45].setAlive(true);
			cell[53][45].setAlive(true);
			cell[53][44].setAlive(true);
			cell[53][44].setAlive(true);
			cell[53][43].setAlive(true);
			cell[54][43].setAlive(true);
			cell[54][42].setAlive(true);
			cell[54][42].setAlive(true);
			cell[54][42].setAlive(true);
			cell[55][41].setAlive(true);
			cell[55][41].setAlive(true);
			cell[55][41].setAlive(true);
			cell[56][41].setAlive(true);
			cell[56][40].setAlive(true);
			cell[56][40].setAlive(true);
			cell[56][40].setAlive(true);
			cell[57][40].setAlive(true);
			cell[57][40].setAlive(true);
			cell[57][39].setAlive(true);
			cell[57][39].setAlive(true);
			cell[58][39].setAlive(true);
			cell[58][39].setAlive(true);
			cell[59][39].setAlive(true);
			cell[59][39].setAlive(true);
			cell[59][39].setAlive(true);
			cell[59][39].setAlive(true);
			cell[60][39].setAlive(true);
			cell[60][39].setAlive(true);
			cell[60][39].setAlive(true);
			cell[60][39].setAlive(true);
			cell[61][39].setAlive(true);
			cell[61][39].setAlive(true);
			cell[61][39].setAlive(true);
			cell[62][39].setAlive(true);
			cell[62][39].setAlive(true);
			cell[62][39].setAlive(true);
			cell[62][39].setAlive(true);
			cell[62][39].setAlive(true);
			cell[63][39].setAlive(true);
			cell[63][39].setAlive(true);
			cell[63][39].setAlive(true);
			cell[63][39].setAlive(true);
			cell[63][39].setAlive(true);
			cell[63][39].setAlive(true);
			cell[63][39].setAlive(true);
			cell[63][39].setAlive(true);
			cell[62][39].setAlive(true);
			cell[62][39].setAlive(true);
			cell[62][40].setAlive(true);
			cell[62][40].setAlive(true);
			cell[62][40].setAlive(true);
			cell[62][40].setAlive(true);
			cell[61][40].setAlive(true);
			cell[61][41].setAlive(true);
			cell[61][41].setAlive(true);
			cell[61][41].setAlive(true);
			cell[61][41].setAlive(true);
			cell[61][41].setAlive(true);
			cell[61][42].setAlive(true);
			cell[61][42].setAlive(true);
			cell[61][42].setAlive(true);
			cell[61][42].setAlive(true);
			cell[62][42].setAlive(true);
			cell[62][43].setAlive(true);
			cell[62][43].setAlive(true);
			cell[62][43].setAlive(true);
			cell[62][43].setAlive(true);
			cell[63][43].setAlive(true);
			cell[63][43].setAlive(true);
			cell[63][43].setAlive(true);
			cell[62][44].setAlive(true);
			cell[62][44].setAlive(true);
			cell[61][45].setAlive(true);
			cell[61][45].setAlive(true);
			cell[60][45].setAlive(true);
			cell[60][45].setAlive(true);
			cell[59][46].setAlive(true);
			cell[59][46].setAlive(true);
			cell[59][46].setAlive(true);
			cell[58][46].setAlive(true);
			cell[58][46].setAlive(true);
			cell[58][46].setAlive(true);
			cell[59][46].setAlive(true);
			cell[59][47].setAlive(true);
			cell[60][47].setAlive(true);
			cell[60][47].setAlive(true);
			cell[61][47].setAlive(true);
			cell[61][47].setAlive(true);
			cell[61][47].setAlive(true);
			cell[62][47].setAlive(true);
			cell[62][48].setAlive(true);
			cell[62][48].setAlive(true);
			cell[63][48].setAlive(true);
			cell[63][48].setAlive(true);
			cell[63][48].setAlive(true);
			cell[62][48].setAlive(true);
			cell[62][48].setAlive(true);
			cell[62][49].setAlive(true);
			cell[61][49].setAlive(true);
			cell[60][49].setAlive(true);
			cell[60][49].setAlive(true);
			cell[60][50].setAlive(true);
			cell[60][50].setAlive(true);
			cell[60][50].setAlive(true);
			cell[60][50].setAlive(true);
			cell[60][50].setAlive(true);
			cell[60][51].setAlive(true);
			cell[61][51].setAlive(true);
			cell[61][51].setAlive(true);
			cell[62][52].setAlive(true);
			cell[62][52].setAlive(true);
			cell[63][52].setAlive(true);
			cell[63][52].setAlive(true);
			cell[63][53].setAlive(true);
			cell[63][53].setAlive(true);
			cell[63][53].setAlive(true);
			cell[63][53].setAlive(true);
			cell[63][53].setAlive(true);
			cell[63][54].setAlive(true);
			cell[62][54].setAlive(true);
			cell[62][54].setAlive(true);
			cell[61][54].setAlive(true);
			cell[61][54].setAlive(true);
			cell[60][54].setAlive(true);
			cell[59][54].setAlive(true);
			cell[59][54].setAlive(true);
			cell[58][54].setAlive(true);
			cell[58][54].setAlive(true);
			cell[57][54].setAlive(true);
			cell[57][53].setAlive(true);
			cell[56][53].setAlive(true);
			cell[56][53].setAlive(true);
			cell[56][52].setAlive(true);
			cell[55][52].setAlive(true);
			cell[55][52].setAlive(true);
			cell[54][52].setAlive(true);
			cell[54][52].setAlive(true);
			cell[54][51].setAlive(true);
			cell[53][51].setAlive(true);
			cell[53][51].setAlive(true);
			cell[53][51].setAlive(true);
			cell[52][50].setAlive(true);
			cell[62][44].setAlive(true);
			cell[62][44].setAlive(true);
			cell[62][44].setAlive(true);
			cell[62][43].setAlive(true);
			cell[63][43].setAlive(true);
			cell[63][43].setAlive(true);
			cell[64][43].setAlive(true);
			cell[65][43].setAlive(true);
			cell[66][43].setAlive(true);
			cell[67][43].setAlive(true);
			cell[67][43].setAlive(true);
			cell[69][43].setAlive(true);
			cell[70][43].setAlive(true);
			cell[71][43].setAlive(true);
			cell[72][43].setAlive(true);
			cell[73][43].setAlive(true);
			cell[74][43].setAlive(true);
			cell[74][43].setAlive(true);
			cell[74][43].setAlive(true);
			cell[74][44].setAlive(true);
			cell[73][44].setAlive(true);
			cell[71][45].setAlive(true);
			cell[69][45].setAlive(true);
			cell[69][45].setAlive(true);
			cell[69][46].setAlive(true);
			cell[69][46].setAlive(true);
			cell[69][46].setAlive(true);
			cell[69][46].setAlive(true);
			cell[71][47].setAlive(true);
			cell[72][47].setAlive(true);
			cell[74][48].setAlive(true);
			cell[75][48].setAlive(true);
			cell[76][48].setAlive(true);
			cell[76][48].setAlive(true);
			cell[76][48].setAlive(true);
			cell[76][48].setAlive(true);
			cell[75][49].setAlive(true);
			cell[73][49].setAlive(true);
			cell[72][49].setAlive(true);
			cell[71][49].setAlive(true);
			cell[71][50].setAlive(true);
			cell[71][50].setAlive(true);
			cell[71][50].setAlive(true);
			cell[71][50].setAlive(true);
			cell[71][50].setAlive(true);
			cell[70][50].setAlive(true);
			cell[69][50].setAlive(true);
			cell[68][50].setAlive(true);
			cell[65][50].setAlive(true);
			cell[64][50].setAlive(true);
			cell[64][50].setAlive(true);
			cell[63][50].setAlive(true);
			cell[62][50].setAlive(true);
			cell[62][50].setAlive(true);
			cell[62][50].setAlive(true);
			cell[61][50].setAlive(true);
			cell[33][46].setAlive(true);
			cell[34][46].setAlive(true);
			cell[35][46].setAlive(true);
			cell[36][46].setAlive(true);
			cell[37][45].setAlive(true);
			cell[38][45].setAlive(true);
			cell[38][45].setAlive(true);
			cell[38][45].setAlive(true);
			cell[39][45].setAlive(true);
			cell[39][45].setAlive(true);
			cell[40][45].setAlive(true);
			cell[40][45].setAlive(true);
			cell[40][45].setAlive(true);
			cell[41][45].setAlive(true);
			cell[41][45].setAlive(true);
			cell[41][46].setAlive(true);
			cell[41][46].setAlive(true);
			cell[42][46].setAlive(true);
			cell[42][47].setAlive(true);
			cell[42][47].setAlive(true);
			cell[42][48].setAlive(true);
			cell[42][48].setAlive(true);
			cell[42][49].setAlive(true);
			cell[42][49].setAlive(true);
			cell[42][49].setAlive(true);
			cell[42][49].setAlive(true);
			cell[42][50].setAlive(true);
			cell[42][50].setAlive(true);
			cell[42][50].setAlive(true);
			cell[41][50].setAlive(true);
			cell[41][50].setAlive(true);
			cell[40][50].setAlive(true);
			cell[40][50].setAlive(true);
			cell[39][50].setAlive(true);
			cell[39][49].setAlive(true);
			cell[38][49].setAlive(true);
			cell[37][49].setAlive(true);
			cell[37][49].setAlive(true);
			cell[36][48].setAlive(true);
			cell[36][48].setAlive(true);
			cell[35][48].setAlive(true);
			cell[35][48].setAlive(true);
			cell[35][47].setAlive(true);
			cell[34][47].setAlive(true);
			cell[34][47].setAlive(true);
			cell[34][46].setAlive(true);
			cell[34][46].setAlive(true);
			cell[33][46].setAlive(true);
			cell[33][46].setAlive(true);
			cell[33][46].setAlive(true);
			cell[40][39].setAlive(true);
			cell[40][39].setAlive(true);
			cell[40][39].setAlive(true);
			cell[40][39].setAlive(true);
			cell[41][39].setAlive(true);
			cell[41][39].setAlive(true);
			cell[42][39].setAlive(true);
			cell[42][39].setAlive(true);
			cell[42][39].setAlive(true);
			cell[43][38].setAlive(true);
			cell[43][38].setAlive(true);
			cell[44][38].setAlive(true);
			cell[44][37].setAlive(true);
			cell[44][37].setAlive(true);
			cell[45][37].setAlive(true);
			cell[45][37].setAlive(true);
			cell[46][36].setAlive(true);
			cell[46][36].setAlive(true);
			cell[46][36].setAlive(true);
			cell[47][36].setAlive(true);
			cell[47][36].setAlive(true);
			cell[47][36].setAlive(true);
			cell[47][36].setAlive(true);
			cell[47][36].setAlive(true);
			cell[47][37].setAlive(true);
			cell[48][39].setAlive(true);
			cell[48][39].setAlive(true);
			cell[48][40].setAlive(true);
			cell[48][40].setAlive(true);
			cell[48][40].setAlive(true);
			cell[48][41].setAlive(true);
			cell[48][41].setAlive(true);
			cell[48][41].setAlive(true);
			cell[48][42].setAlive(true);
			cell[48][42].setAlive(true);
			cell[48][42].setAlive(true);
			cell[39][53].setAlive(true);
			cell[40][54].setAlive(true);
			cell[40][54].setAlive(true);
			cell[42][55].setAlive(true);
			cell[42][55].setAlive(true);
			cell[43][56].setAlive(true);
			cell[43][56].setAlive(true);
			cell[44][56].setAlive(true);
			cell[44][56].setAlive(true);
			cell[44][56].setAlive(true);
			cell[44][56].setAlive(true);
			cell[45][56].setAlive(true);
			cell[45][57].setAlive(true);
			cell[45][57].setAlive(true);
			cell[45][57].setAlive(true);
			cell[45][57].setAlive(true);
			cell[45][57].setAlive(true);
			cell[46][56].setAlive(true);
			cell[46][56].setAlive(true);
			cell[46][56].setAlive(true);
			cell[46][55].setAlive(true);
			cell[46][55].setAlive(true);
			cell[46][55].setAlive(true);
			cell[46][55].setAlive(true);
			cell[46][54].setAlive(true);
			cell[46][54].setAlive(true);
			cell[46][54].setAlive(true);
			cell[46][54].setAlive(true);
			cell[47][54].setAlive(true);
		
		}
	}
}
