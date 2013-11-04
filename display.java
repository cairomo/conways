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
                
                Pause = new PauseButton();
                Pause.setBounds(200, 550, 100, 36);
                add(Pause);
                Pause.setVisible(true);
                
                Clear = new ClearButton();
                Clear.setBounds(300, 550, 100, 36);
                add(Clear);
                Clear.setVisible(true);
                
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
                        repaint();
                }
        }


        public void initCells() {
                for (int row = 0; row < ROWS; row++) {
                        for (int col = 0; col < COLS; col++) {
                                cell[row][col] = new Cell(row, col);
                        }
                }
                
                cell[36][22].setAlive(true); // sample use of cell mutator method
                cell[36][23].setAlive(true); // sample use of cell mutator method
                cell[36][24].setAlive(true); // sample use of cell mutator method
                cell[37][25].setAlive(true);
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

        }


        public void mouseClicked(MouseEvent arg0) {

        }


        public void mouseEntered(MouseEvent arg0) {

        }


        public void mouseExited(MouseEvent arg0) {

        }


        public void mousePressed(MouseEvent arg0) {

        }


        public void mouseReleased(MouseEvent arg0) {

        }


        public void mouseDragged(MouseEvent arg0) {

        }


        public void mouseMoved(MouseEvent arg0) {
                
        }
        

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
                        } else {
                                togglePaintLoop();
                                setText("Start");
                        }
                        repaint();
                }
        }
        
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
                        } else {
                                togglePaintLoop();
                                setText("Step");
                        }
                        repaint();
                }
        }
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
        private class ClearButton extends JButton implements ActionListener {
                ClearButton() {
                        super("Clear");
                        addActionListener(this);
                }


                public void actionPerformed(ActionEvent arg0) {
                        // nextGeneration(); // test the start button
                        if (this.getText().equals("Clear")) {
                                togglePaintLoop();
                                setText("Clear");
                        } else {
                                togglePaintLoop();
                                setText("Clear");
                        }
                        repaint();
                }
        }

}
