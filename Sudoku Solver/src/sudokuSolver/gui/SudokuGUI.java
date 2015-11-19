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
import javax.swing.JOptionPane;
import javax.swing.border.CompoundBorder;
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
	 * The GUI currently worked on to send to DataModel.
	 */
	private SudokuGUI _sudokuGUI;

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
	public SudokuGUI (DataModel dataModel) {
		_grid = new JFormattedTextField[SIZE][SIZE];
		_model = dataModel;
		_sudokuGUI = this;
		_representation = new int[SIZE][SIZE];
	}

	/**
	 * Sets up the GUI for Sudoku. Uses <code>JFormattedTextField</code> in order to limit user's inputs to numbers.  
	 * Uses a switch statement to create borders. <code>BorderFactory</code> creates <code>MatteBorder</code> which is needed as a result of pixel issues with borders in <code>GridLayout</code>.
	 */
	@Override
	public void run() {
		JFrame window = new JFrame("Sudoku Game");
		window.setLayout(new GridLayout(SIZE+1,SIZE,0,0));
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		NumberFormat format = NumberFormat.getInstance();
		NumberFormatter formatter = new NumberFormatter(format);
		formatter.setValueClass(Integer.class);
		formatter.setMinimum(0);
		formatter.setMaximum(9);
		MatteBorder rightBorder = new MatteBorder(0,0,0,4, Color.BLACK);
		MatteBorder topBorder = new MatteBorder(4,0,0,0, Color.BLACK);
		MatteBorder leftBorder = new MatteBorder(0,4,0,0, Color.BLACK);
		MatteBorder bottomBorder = new MatteBorder(0,0,4,0, Color.BLACK);
		MatteBorder borderFix1 = new MatteBorder(1,1,1,1, Color.GRAY);
		MatteBorder borderFix2 = new MatteBorder(0,0,0,0, Color.GRAY);
		CompoundBorder combinedBorderFix = BorderFactory.createCompoundBorder(borderFix1,borderFix2);

		for(int row = 0; row < SIZE; row++){
			for(int col = 0; col< SIZE; col++){
				JFormattedTextField numberTextField = new JFormattedTextField(formatter);
				numberTextField.setBorder(BorderFactory.createEmptyBorder());
				formatter.setAllowsInvalid(false);
				_grid[row][col] = numberTextField;

				switch(row%(SIZE/3)){
				case 0: numberTextField.setBorder(topBorder);
				break;
				case 1: numberTextField.setBorder(combinedBorderFix);
				break;
				case 2: numberTextField.setBorder(bottomBorder);
				break;
				}

				switch(col%(SIZE/3)){
				case 0: CompoundBorder case0Border = BorderFactory.createCompoundBorder(leftBorder,numberTextField.getBorder());
				if(numberTextField.getBorder().equals(combinedBorderFix)){
					numberTextField.setBorder(leftBorder);
					break;
				}
				else{
					numberTextField.setBorder(BorderFactory.createCompoundBorder(case0Border,combinedBorderFix));
					break;
				}
				case 1: break;
				case 2: CompoundBorder case2Border = BorderFactory.createCompoundBorder(rightBorder,numberTextField.getBorder());
				if(numberTextField.getBorder().equals(combinedBorderFix)){
					numberTextField.setBorder(rightBorder);
					break;
				}
				else{
					numberTextField.setBorder(BorderFactory.createCompoundBorder(case2Border,combinedBorderFix));
					break;
				}
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

				window.add(_grid[row][col]);		
			}
		}

		JButton solveButton = new JButton("Solve");
		solveButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				_sudokuGUI.getData();
				_model.setGUI(_sudokuGUI);
				if(_model.isDuplicatesAll(_representation)){
					JOptionPane.showMessageDialog(null , "Duplicates found in:\n\nRows\nColumns\nOr Squares", "Illegal Inputs" , JOptionPane.WARNING_MESSAGE);
				}
				else{	
					_model.solve(_representation,_sudokuGUI);
				}
			}
		});

		window.add(solveButton);
		window.pack();
		window.setVisible(true);
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
