import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;
import javax.swing.JComponent;

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
	private PauseButton Pause;
	private ClearButton Clear;
	private StepButton Step;
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

		// Example of setting up a button.
		// See the StartButton class nested below.
		startStop = new StartButton();
		startStop.setBounds(100, 550, 100, 36);
		add(startStop);
		startStop.setVisible(true);

		//Pause button
		Pause = new PauseButton();
		Pause.setBounds(200, 550, 100, 36);
		add(Pause);
		Pause.setVisible(true);

		//Clear button
		Clear = new ClearButton();
		Clear.setBounds(300, 550, 100, 36);
		add(Clear);
		Clear.setVisible(true);

		//Step button
		Step = new StepButton();
		Step.setBounds(400, 550, 100, 36);
		add(Step);
		Step.setVisible(true);

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

		//Cells initially set alive by us, also calls calcNeighbors
		//cell[36][22].setAlive(true); // sample use of cell mutator method
		//cell[36][23].setAlive(true); // sample use of cell mutator method
		//cell[36][24].setAlive(true); // sample use of cell mutator method
	//	cell[46][24].setAlive(true); // sample use of cell mutator method

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
			//I DONT KNOW HOW MANY REPAINTS ARE NECESSARY
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
		x = 0;
		y= 0;
		update();
	}

	public void mouseMoved(MouseEvent arg0) {

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
	private class PauseButton extends JButton implements ActionListener {
		PauseButton() {
			super("Pause");
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent arg0) {
			// nextGeneration(); // test the start button
			if (this.getText().equals("Pause")) {
				togglePaintLoop();
				setText("Play");
			} else {
				togglePaintLoop();
				setText("Pause");
			}
			repaint();
		}
	}
	
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
	
	/*private class ChooseBox extends JComboBox implements ActionListener {
		ChooseBox() {
			super("Choose");
			
			addActionListener(this);
		}
		
		String[] petStrings = { "Bird", "Cat", "Dog", "Rabbit", "Pig" };

		//Create the combo box, select item at index 4.
		//Indices start at 0, so 4 specifies the pig.
		JComboBox petList = new JComboBox(petStrings);
		petList.setSelectedIndex(4);
		petList.addActionListener(this);

	}*/
	

}
