/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

package solver;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class for common attributes or methods for solvers of standard
 * Sudoku. Note it is not necessary to use this, but provided in case you wanted
 * to do so and then no need to change the hierarchy of solver types.
 */
public abstract class StdSudokuSolver extends SudokuSolver {
	protected int size;
	protected List<Integer> acceptedNumbers;

	public StdSudokuSolver() {
		this.size = 0;
		this.acceptedNumbers = new ArrayList<Integer>();
	}
} // end of class StdSudokuSolver
