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
					for (List<Integer> permutation : this.cages
							.get(cell.cageId).mapOfPermutationsStartingWithASpecificDigit.get(permittedValue)) {
						if (validateThePermutation(this.cages.get(cell.cageId), permutation)) {
							fillCageCoords(this.cages.get(cell.cageId), permutation);
							if (recursiveSolve(i + 1)) {
								return true;
							} else {
								unFillCageCoords(this.cages.get(cell.cageId));
							}
						}
					}
				}
				return false;
			}
		}
		return true;
	}

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

	private boolean isValidCommon(int row, int col, int number) {
		return !isInRow(row, number) && !isInCol(col, number) && !isInBox(row, col, number);
	}
} // end of class KillerAdvancedSolver()
