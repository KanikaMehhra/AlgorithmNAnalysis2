/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

package solver;

import grid.SudokuGrid;

/**
 * Backtracking solver for standard Sudoku.
 */
public class BackTrackingSolver extends StdSudokuSolver {
	private static final int UNASSIGNED = -1;
	private int[][] matrix;

	public BackTrackingSolver() {
		super();
		this.matrix = null;
	} // end of BackTrackingSolver()

	@Override
	public boolean solve(SudokuGrid grid) {
		this.size = grid.getSudokuGridLength();
		this.acceptedNumbers = grid.getListOfvalidIntegers();
		this.matrix = grid.getSudokuGrid();
		return recursiveSolve();
	}

	public boolean recursiveSolve() {
		for (int row = 0; row < this.size; row++) {
			for (int col = 0; col < this.size; col++) {
				if (this.matrix[row][col] == UNASSIGNED) {
					for (int number = 0; number < this.size; number++) {
						if (isValidCommon(row, col, this.acceptedNumbers.get(number))) {
							matrix[row][col] = this.acceptedNumbers.get(number);
							if (recursiveSolve()) {
								return true;
							} else {
								this.matrix[row][col] = UNASSIGNED;
							}
						}
					}
					return false;
				}
			}
		}
		return true;
	}

	// Checks the uniqueness of the selected number in a row.
	private boolean isInRow(int row, int number) {
		for (int i = 0; i < this.size; i++) {
			if (this.matrix[row][i] == number) {
				return true;
			}
		}
		return false;
	}

	// Checks the uniqueness of the selected number in a column.
	private boolean isInCol(int col, int number) {
		for (int i = 0; i < this.size; i++) {
			if (this.matrix[i][col] == number)
				return true;
		}
		return false;
	}

	// Checks the uniqueness of the selected number in a box.
	private boolean isInBox(int row, int col, int number) {
		int sqrt = (int) Math.sqrt(this.size);
		int r = row - row % sqrt;
		int c = col - col % sqrt;

		for (int i = r; i < r + sqrt; i++) {
			for (int j = c; j < c + sqrt; j++) {
				if (this.matrix[i][j] == number) {
					return true;
				}
			}
		}
		return false;
	}

	// Checks the validation of the selected number in a sudoku.
	private boolean isValidCommon(int row, int col, int number) {
		return !isInRow(row, number) && !isInCol(col, number) && !isInBox(row, col, number);
	}
} // end of class BackTrackingSolver()
