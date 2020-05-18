/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

package solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import grid.SudokuGrid;

/**
 * Backtracking solver for standard Sudoku.
 */
public class BackTrackingSolver extends StdSudokuSolver {
	// TODO: Add attributes as needed.
	private static final int UNASSIGNED = -1;
	List<String> unassignedCells;

	public BackTrackingSolver() {
		unassignedCells = new ArrayList<String>();
		// TODO: any initialisation you want to implement.
	} // end of BackTrackingSolver()

	@Override
	public boolean solve(SudokuGrid grid) {
		setListOfUnassignedCells(grid.getSudokuGrid());
		solve(grid, 0);
		return grid.validate();
	}

	public boolean solve(SudokuGrid grid, int assignmentNo) {
		boolean result = true;
		for (int i = assignmentNo; i < this.unassignedCells.size(); i++) {
			String[] coords = this.unassignedCells.get(i).split(",");
			int row = Integer.parseInt(coords[0]);
			int col = Integer.parseInt(coords[1]);
			int vNum = -1;
			if (grid.getSudokuGrid()[row][col] != UNASSIGNED) {
				vNum = grid.getListOfvalidIntegers().indexOf(grid.getSudokuGrid()[row][col]);
			}
			for (int v = vNum + 1; v < grid.getListOfvalidIntegers().size();v++) {
				grid.getSudokuGrid()[row][col] = grid.getListOfvalidIntegers().get(v);
				if (grid.validate()) {
					solve(grid, ++assignmentNo);
					result = true;
					break;
				} else {
//					System.out.println("validated with " + row + col + grid.getListOfvalidIntegers().get(v));

					grid.getSudokuGrid()[row][col] = UNASSIGNED;
					result = false;
					continue;
					
				}
			}
			if (grid.getSudokuGrid()[row][col] == UNASSIGNED) {
				solve(grid, --assignmentNo);
			}
		}

		return result;
		// for (int row = 0; row < grid.getSudokuGridLength(); row++) {
		// for (int col = 0; col < grid.getSudokuGridLength(); col++) {
		// if (grid.getSudokuGrid()[row][col] == UNASSIGNED) {
		// for (int number = 0; number < grid.getListOfvalidIntegers().size(); number++)
		// {
		// grid.getSudokuGrid()[row][col] = grid.getListOfvalidIntegers().get(number);
		// if (grid.validate()) {
		// if (solve(grid)) {
		// return true;
		// } else {
		// grid.getSudokuGrid()[row][col] = UNASSIGNED;
		// }
		// }
		// }
		// return false;
		// }
		// }
		// }
		// return true;
	} // end of solve()

	public void setListOfUnassignedCells(int[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				if (matrix[i][j] == UNASSIGNED) {
					this.unassignedCells.add(String.valueOf(i) + "," + String.valueOf(j));
				}
			}
		}
		System.out.println(this.unassignedCells);
	}

} // end of class BackTrackingSolver()
