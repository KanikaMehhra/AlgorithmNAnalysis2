package solver;

/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

import java.util.List;

/**
 * Your advanced solver for Killer Sudoku.
 */

public class KillerAdvancedSolver extends KillerSudokuSolver {

	public KillerAdvancedSolver() {
		super();
	} // end of KillerBackTrackingSolver()

	public boolean recursiveSolve(int cellIndex) {
		for (int i = cellIndex; i < this.cells.size(); i++) {
			Cell cell = this.cells.get(i);
			if (this.matrix[cell.row][cell.col] == UNASSIGNED) {
				for (int permittedValue : cell.permittedIntegers) {
					Cage cage=this.cages.get(cell.cageId);
					for (List<Integer> permutation : cage.mapOfPermutationsStartingWithASpecificDigit.get(permittedValue)) {
						if (validateThePermutation(cage, permutation)) {
							fillCageCoords(cage, permutation);
							if (recursiveSolve(i + 1)) {
								return true;
							} else {
								unFillCageCoords(cage);
							}
						}
					}
				}
				return false;
			}
		}
		return true;
	}

	// Checks if the selected permutation for a cage is valid.
	public boolean validateThePermutation(Cage cage, List<Integer> permutation) {
		for (int i = 0; i < permutation.size(); i++) {
			String[] rc = cage.coordinates.get(i).split(",");
			int r = Integer.parseInt(rc[0]);
			int c = Integer.parseInt(rc[1]);
			if (!isValidCommon(r, c, permutation.get(i))) {
				return false;
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
} // end of class KillerAdvancedSolver()
