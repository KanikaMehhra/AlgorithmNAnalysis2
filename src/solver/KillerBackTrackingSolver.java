/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

package solver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import grid.KillerSudokuGrid;
import grid.SudokuGrid;

/**
 * Backtracking solver for Killer Sudoku.
 */
public class KillerBackTrackingSolver extends KillerSudokuSolver {
	// TODO: Add attributes as needed.
	private static final int UNASSIGNED = -1;
	private int[][] matrix;
	private int size;
	private List<Integer> acceptedNumbers;
	private Map<List<String>, Integer> cageCoordsWithValuesMap;
	private int numberOfCages;
	private SudokuGrid grid;

	public KillerBackTrackingSolver() {
		// TODO: any initialisation you want to implement.
		this.matrix = null;
		this.size = 0;
		this.acceptedNumbers = new ArrayList<Integer>();
		this.cageCoordsWithValuesMap = new HashMap<List<String>, Integer>();
		this.numberOfCages = 0;
		this.grid = null;

	} // end of KillerBackTrackingSolver()

	@Override
	public boolean solve(SudokuGrid grid) {
		// TODO: your implementation of a backtracking solver for Killer Sudoku.
		this.grid = grid;
		this.size = grid.getSudokuGridLength();
		this.acceptedNumbers = grid.getListOfvalidIntegers();
		this.matrix = grid.getSudokuGrid();
		this.cageCoordsWithValuesMap = ((KillerSudokuGrid) grid).getCageCoordsWithValuesMap();
		this.numberOfCages = this.cageCoordsWithValuesMap.size();
		return recursiveSolve();
		// placeholder
		// return false;
	} // end of solve()

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

	private boolean isInRow(int row, int number) {
		for (int i = 0; i < this.size; i++) {
			if (this.matrix[row][i] == number) {
				return true;
			}
		}
		return false;
	}

	private boolean isInCol(int col, int number) {
		for (int i = 0; i < this.size; i++) {
			if (this.matrix[i][col] == number)
				return true;
		}
		return false;
	}

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

	private boolean isGreaterThanCageValueRange(int row, int col, int number) {
		String rowCol = "" + row + "," + col;
		for (Map.Entry<List<String>, Integer> entry : this.cageCoordsWithValuesMap.entrySet()) {
			if (entry.getKey().contains(rowCol)) {
				int actualTotalValueOfTheCageCoord = 0;
				List<Integer> cageCoordValues = new ArrayList<Integer>();

				for (String coord : entry.getKey()) {
					String[] rc = coord.split(",");
					int r = Integer.parseInt(rc[0]);
					int c = Integer.parseInt(rc[1]);
					if (this.matrix[r][c] != -1) {
						actualTotalValueOfTheCageCoord += this.matrix[r][c];
						cageCoordValues.add(this.matrix[r][c]);
					}

				}
				actualTotalValueOfTheCageCoord += number;
				if (entry.getValue() < actualTotalValueOfTheCageCoord)
					return true;
				// checks the uniqueness of cage values.
				if (!this.grid.isHashLengthSame(cageCoordValues))
					return true;
				break;
			}
		}
		return false;
	}

	private boolean isValidCommon(int row, int col, int number) {
		return !isInRow(row, number) && !isInCol(col, number) && !isInBox(row, col, number)
				&& !isGreaterThanCageValueRange(row, col, number);
	}

} // end of class KillerBackTrackingSolver()
