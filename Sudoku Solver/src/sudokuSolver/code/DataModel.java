package sudokuSolver.code;

import javax.swing.JOptionPane;

import sudokuSolver.gui.*;

/**
 * <code>DataModel</code> takes a 2-D array from <code>SudokuGUI</code> and interprets it through a recursive method to solve a given sudoku puzzle.
 * <p>
 * <code>DataModel</code> has a main recursive method as well as helper methods for the recursive method.
 * <p>
 * <code>DataModel</code> also has methods to feed the information back to the <code>SudokuGUI</code>.
 * @author Zachary Moore
 *
 */
public class DataModel {

	/**
	 * Represents the <code>SudokuGUI</code> that <code>DataModel</code> is dealing with.
	 */
	private SudokuGUI _gui;

	/**
	 * One dimension int of the Sudoku puzzle.
	 */
	private static final int SIZE = 9;

	/**
	 * Intakes a <code>SudokuGUI</code> to be analyzed and sets {@code _gui} to that <code>SudokuGUI</code>.
	 */
	public DataModel(SudokuGUI gui){
		_gui = gui;
	}

	/**
	 * Called by <code>SudokuGUI</code> when the solve button is pressed.
	 * <p>
	 * Calls the recursive method as well as sets {@code _gui} GUI to be the correct answer or an empty 2-D array.
	 * @param data Data given from the {@code gui} to be solved
	 *
	 */
	public void solve(int[][] data){
		if(analyze(data)){
			_gui.setGUI(data);
		}
		else{
			JOptionPane.showMessageDialog(null , "No Solution Possible", null , JOptionPane.WARNING_MESSAGE);
		}
	}

	/**
	 * Recursive boolean method to solve a 2-D array into a correct sudoku solution.
	 * <p>
	 * First checks if there are any open spaces in the data representation.
	 * If there are none the puzzle is solved.
	 * Otherwise begins looking at numbers to be placed.
	 * <p>
	 * Checks if the number {@code isValid} within the data.
	 * If the number is valid sets the data to the number in the correct location and calls {@code analyze} on the data again.
	 * Otherwise sets the data at the location to be 0.
	 * <p>
	 * If there are no solutions {@code analyze} will return false.
	 * <p>
	 * @param data Represents the 2-D array to be solved
	 * @return false if there are no solutions
	 */
	public boolean analyze(int[][] data) {
		WrapperInt row = new WrapperInt();
		WrapperInt col = new WrapperInt();
		if(!isOpen(data, row, col))
			return true;
		else{
			for(int num = 1; num <= SIZE; num++){
				if(isValid(data, row._value, col._value,num)){
					data[row._value][col._value] = num;
					if(analyze(data))
						return true;
					else{
						data[row._value][col._value] = 0;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Checks the {@code data} representation to see if there are open spaces in the 2-D array.
	 * @param data The 2-D array to be checked
	 * @param rowInt <code>WrapperInt</code> which is used to represent the row traversal
	 * @param colInt <code>WrapperInt</code> which is used to represent the column traversal
	 * @return true if there is open spaces within {@code data}
	 */
	public boolean isOpen(int[][] data, WrapperInt rowInt , WrapperInt colInt){
		for(rowInt._value = 0 ; rowInt._value < SIZE; rowInt._value++){
			for(colInt._value = 0; colInt._value < SIZE; colInt._value++){
				if(data[rowInt._value][colInt._value] == 0)
					return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the {@code num} is within the {@code row}.
	 * @param data The 2-D array to be checked
	 * @param row The row to check 
	 * @param num The number to check
	 * @return true if {@code num} is within {@code row}
	 */
	public boolean inRow(int[][] data,int row, int num){
		for(int col = 0; col < SIZE; col++){
			if(data[row][col] == num)
				return true;
		}
		return false;
	}

	/**
	 * Checks if the {@code num} is within the {@code col}.
	 * @param data The 2-D array to be checked
	 * @param col The column to check
	 * @param num The number to check
	 * @return true if {@code num} is within {@code row}
	 */
	public boolean inCol(int[][] data, int col, int num){
		for(int row = 0; row < SIZE; row++){
			if(data[row][col] == num)
				return true;
		}
		return false;
	}

	/**
	 * Checks if the {@code num} is within the 3x3 square.
	 * @param data the 2-D array to be checked
	 * @param squareStartRow The start index for row of the square being looked at
	 * @param squareStartCol The start index for column of the square being looked at
	 * @param num The number to check
	 * @return true if {@code num} is within the square that starts at {@code squareStartRow} and {@code squareStartCol}
	 */
	public boolean inSquare(int[][] data, int squareStartRow, int squareStartCol, int num){
		for(int row = 0; row < (SIZE/3); row++){
			for(int col = 0; col < (SIZE/3); col++){
				if(data[row + squareStartRow][col + squareStartCol] == num)
					return true;
			}
		}
		return false;
	}

	/**
	 * Checks if {@code num} is valid to be placed within the given parameters.
	 * @param data The 2-D array to be checked
	 * @param row The row to check
	 * @param col The column to check
	 * @param num The number to check
	 * @return true if {@code num} is not in the {@code row}, {@code col}, and square
	 */
	public boolean isValid(int[][] data, int row, int col, int num){
		if(!inRow(data, row, num) && !inCol(data, col, num) && !inSquare(data, row-(row%3), col - (col%3), num)){
			return true;
		}
		else{
			return false;
		}
	}

	/**
	 * Checks if there are duplicate ints in any of the rows of {@code data}.
	 * @param data The 2-D array to be checked
	 * @return true if there are duplicate ints in {@code data}
	 */
	public boolean isDuplicatesRow(int[][] data){
		for(int row = 0 ; row < SIZE; row ++){
			for(int col = 0; col < SIZE; col ++){
				int temp = data[row][col];
				if(temp != 0){
					for(int col2 = 0; col2 < SIZE; col2 ++){
						if(col2 != col){
							if(data[row][col2] == temp){
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * Checks if there are duplicate ints in any of the columns of {@code data}.
	 * @param data the 2-D array to be checked
	 * @return true if there are duplicate ints in {@code data}
	 */
	public boolean isDuplicatesCol(int[][] data){
		for(int row = 0; row < SIZE; row ++){
			for(int col = 0; col < SIZE; col ++){
				int temp  = data[row][col];
				if(temp != 0){
					for(int row2 = 0; row2 < SIZE; row2 ++){
						if(row2 != row){
							if(data[row2][col] == temp){
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * Checks if there are duplicate ints in any of the squares of {@code data}.
	 * @param data the 2-D array to be checked
	 * @return true if there are duplicate ints in {@code data}
	 */
	public boolean isDuplicatesSquare(int[][] data){
		for(int row = 0; row < SIZE; row ++){
			for(int col = 0; col < SIZE; col ++){
				int temp = data[row][col];
				if(temp != 0){
					for(int row2 = row - row%3; row2 < (SIZE/3); row2++){
						for(int col2 = col - col%3; col2 < (SIZE/3); col2++){
							if(row2 != row && col2 != col){
								if(data[row2][col2] == temp){
									return true;
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * Combines all duplicate checking methods to check if {@code data} has any illegal inputs.
	 * @param data the 2-D array to be checked
	 * @return true if any of the duplicate methods return true
	 */
	public boolean isDuplicatesAll(int[][] data){
		if(isDuplicatesSquare(data) || isDuplicatesRow(data) || isDuplicatesCol(data)){
			return true;
		}
		return false;
	}
}