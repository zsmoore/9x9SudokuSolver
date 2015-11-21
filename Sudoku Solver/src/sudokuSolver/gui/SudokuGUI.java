package sudokuSolver.gui;

import javax.swing.JFormattedTextField;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;
import javax.swing.text.NumberFormatter;

import sudokuSolver.code.DataModel;

/**
 * <code>SudokuGUI</code> creates a graphical user interface that represents a sudoku puzzle.
 * <p>
 * <code>SudokuGUI</code> has methods which are used to gather information by the GUI and send them to <code>DataModel</code> to be analyzed.
 * 
 * @author Zachary Moore 
 *
 */
public class SudokuGUI implements Runnable {

	/**
	 * Represents the DataModel to send GUI data to.
	 */
	private DataModel _model;

	/**
	 * 2-D array of JFormattedTextFields to limit user input and accept numbers.
	 */
	private JFormattedTextField[][] _grid;

	/**
	 * 2-D int array which represents the data within the GUI.
	 */
	private int[][] _representation;

	/**
	 * One dimension int of the Sudoku puzzle.
	 */
	private static final int SIZE = 9;

	/**
	 * Initializes all instance variables.
	 * @param dataModel The dataModel to send information to
	 */
	public SudokuGUI () {
		_model = new DataModel(this);
		_grid = new JFormattedTextField[SIZE][SIZE];
		_representation = new int[SIZE][SIZE];
	}

	/**
	 * Sets up the GUI for Sudoku. Uses <code>JFormattedTextField</code> in order to limit user's inputs to numbers.  
	 * Uses a switch statement to create borders. <code>BorderFactory</code> creates <code>MatteBorder</code> to create thicker borders.
	 */
	@Override
	public void run() {
		JFrame window = new JFrame("Sudoku Game");
		window.setLayout(new GridLayout(2,1,0,0));
		
		JPanel gridPanel = new JPanel();
		gridPanel.setLayout(new GridLayout(SIZE,SIZE,0,0));
		
		JPanel otherPanel = new JPanel();
		otherPanel.setLayout(new GridLayout(1,3,0,0));
		
		
		NumberFormat format = NumberFormat.getInstance();
		NumberFormatter formatter = new NumberFormatter(format);
		formatter.setValueClass(Integer.class);
		formatter.setMinimum(0);
		formatter.setMaximum(9);
		
		MatteBorder rightBorder = new MatteBorder(0,0,0,4, Color.BLACK);
		MatteBorder topBorder = new MatteBorder(4,0,0,0, Color.BLACK);
		MatteBorder leftBorder = new MatteBorder(0,4,0,0, Color.BLACK);
		MatteBorder bottomBorder = new MatteBorder(0,0,4,0, Color.BLACK);

		for(int row = 0; row < SIZE; row++){
			for(int col = 0; col< SIZE; col++){
				JFormattedTextField numberTextField = new JFormattedTextField(formatter);
				numberTextField.setBorder(BorderFactory.createLineBorder(Color.black));
				formatter.setAllowsInvalid(false);
				_grid[row][col] = numberTextField;

				switch(row%((int)Math.sqrt(SIZE))){
				case 0: numberTextField.setBorder(BorderFactory.createCompoundBorder(topBorder, numberTextField.getBorder()));
				break;
				case 1: 
				break;
				case 2: numberTextField.setBorder(BorderFactory.createCompoundBorder(bottomBorder, numberTextField.getBorder()));
				break;
				}

				switch(col%((int)Math.sqrt(SIZE))){
				case 0: numberTextField.setBorder(BorderFactory.createCompoundBorder(leftBorder, numberTextField.getBorder()));
				break;
				case 1: break;
				case 2: numberTextField.setBorder(BorderFactory.createCompoundBorder(rightBorder,numberTextField.getBorder()));
				break;
				}

				numberTextField.addFocusListener(new FocusListener(){

					@Override
					public void focusGained(FocusEvent e) {
						numberTextField.setText("");
					}

					@Override
					public void focusLost(FocusEvent e) {
					}
				});

				gridPanel.add(_grid[row][col]);		
			}
		}
		
		JLabel instructions = new JLabel("<HTML><body><H1>Instructions</H1><p>Input your sudoku numbers into the text fields. <p><p> click the solve button to solve the given inputs!<p></body></HTML>",SwingConstants.LEFT);
		instructions.setVerticalAlignment(SwingConstants.TOP);
		otherPanel.add(instructions);
		
		JLabel notes = new JLabel("<HTML><body><H1>Notes</H1><p> You can set a text field to be "+ '"' + "empty" + '"' + " by inputting 0.</body></HTML>",SwingConstants.LEFT );
		notes.setVerticalAlignment(SwingConstants.TOP);
		otherPanel.add(notes);

		JButton solveButton = new JButton("Solve");
		solveButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				getData();
				if(_model.isDuplicatesAll(_representation)){
					JOptionPane.showMessageDialog(null , "Duplicates found in:\n\nRows\nColumns\nOr Squares", "Illegal Inputs" , JOptionPane.WARNING_MESSAGE);
				}
				else{	
					_model.solve(_representation);
				}
			}
		});
		
		otherPanel.add(solveButton);
		
		
		
		window.add(gridPanel);
		window.add(otherPanel);
		window.pack();
		window.setSize(600, 600);
		window.setVisible(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * Gathers numbers from the JFormattedTextFields in the GUI.
	 * @return A 2-D int array that represents the GUI
	 */
	public int[][] getData(){
		for(int row= 0; row < SIZE; row++){
			for(int col = 0; col < SIZE; col++){
				String q = _grid[row][col].getText().trim();
				if(!q.isEmpty()){
					_representation[row][col] = Integer.parseInt(_grid[row][col].getText().trim());
				}
			}	
		}
		return _representation;
	}	

	/**
	 * Sets the GUI to the given 2-D int array.
	 * @param updatedNumbers The 2-D int array to set the GUI to
	 */
	public void setGUI(int[][] updatedNumbers){
		for(int row = 0; row < SIZE; row++){
			for(int col = 0; col < SIZE; col++){
				_grid[row][col].setText(Integer.toString(updatedNumbers[row][col]));
			}
		}
	}
}
