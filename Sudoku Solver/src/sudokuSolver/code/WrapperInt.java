package sudokuSolver.code;

/**
 * <code>WrapperInt</code> is used as a way to carry int values through recursion without causing stack overflow issues.
 * 
 * @author Zachary Moore
 *
 */
public class WrapperInt {

	/**
	 * Allows <code>DataModel</code> to use the value of the int <code>WrapperInt</code> represents.
	 */
	public int _value;

	/**
	 * Constructor which allows the user to choose the value of <code>WrapperInt</code>.
	 * @param x Represents the value the use wants to make <code>WrapperInt</code> represent
	 */
	public WrapperInt(int x){
		_value = x;
	}

	/**
	 * No args constructor which sets {@code _value} to 0.
	 */
	public WrapperInt(){
		_value = 0;
	}
}
