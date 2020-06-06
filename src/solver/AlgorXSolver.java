/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */
package solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import grid.SudokuGrid;

/**
 * Algorithm X solver for standard Sudoku.
 */
public class AlgorXSolver extends StdSudokuSolver {
	private int[][] matrix;
	private List<Integer> solutionRows;
	private Map<Integer, String> rowSolMap;
	private List<Integer> colsCovered;
	private ExactCoverTransformation transform;

	public AlgorXSolver() {
		super();
		this.transform = null;
		this.matrix = null;
		this.solutionRows = new ArrayList<Integer>();
		this.rowSolMap = new HashMap<Integer, String>();
		this.colsCovered = new ArrayList<Integer>();
	} // end of AlgorXSolver()

	@Override
	public boolean solve(SudokuGrid grid) {
		this.transform = new ExactCoverTransformation();
		this.size = grid.getSudokuGridLength();
		this.acceptedNumbers = grid.getListOfvalidIntegers();
		this.createCoverMatrix(grid.getSudokuGrid());
		if (recursiveSolve()) {
			fillGridWithSolution(grid.getSudokuGrid());
			return true;
		}
		return false;
	} // end of solve()

	// converts the solution rows to fill the sudoku.
	private void fillGridWithSolution(int[][] grid) {
		for (int solRow : this.solutionRows) {
			String[] splitSolRow = this.rowSolMap.get(solRow).split(",");
			int row = Integer.parseInt(splitSolRow[0]);
			int col = Integer.parseInt(splitSolRow[1]);
			int indexValue = Integer.parseInt(splitSolRow[2]);
			grid[row][col] = this.acceptedNumbers.get(indexValue);
		}
	}

	private boolean recursiveSolve() {
		boolean result = true;
		for (int i = 0; i < this.matrix.length; i++) {
			for (int j = 0; j < this.matrix[0].length; j++) {
				if (this.matrix[i][j] != 0) {
					result = false;
					break;
				}
			}
		}
		// if all the cells in the cover matrix are 0, then the solution is found.
		if (result) {
			return true;
		}

		Map<Integer, Integer> colValueMap = new TreeMap<Integer, Integer>();
		// map the cols with the number of cells having value 1.
		for (int j = 0; j < this.matrix[0].length; j++) {
			if (!this.colsCovered.contains(j)) {
				int colMinValue = 0;
				for (int i = 0; i < this.matrix.length; i++) {
					colMinValue += this.matrix[i][j];
				}
				if (colMinValue == 0)
					return false;
				colValueMap.put(j, colMinValue);
			}
		}
		int minColConstraint = 0;
		int minColValue = 0;
		Iterator<Integer> iterator = colValueMap.keySet().iterator();
		// initialise the column to be considered as the first column of the map created
		// above.
		if (iterator.hasNext()) {
			int next = iterator.next();
			minColValue = colValueMap.get(next);
			minColConstraint = next;
		}
		// iterate over the map to get the column with the least number of 1s.
		while (iterator.hasNext()) {
			int next = iterator.next();
			if (colValueMap.get(next) < minColValue) {
				minColConstraint = next;
				minColValue = colValueMap.get(next);
			}
		}

		List<Integer> suspectedSolRows = new ArrayList<Integer>();
		// select the rows that satisfy the column constraint, which is selected in
		// above process and cover them.
		for (int outerRow = 0; outerRow < this.matrix.length; outerRow++) {
			if (this.matrix[outerRow][minColConstraint] == 1) {
				suspectedSolRows.add(outerRow);
				Arrays.fill(this.matrix[outerRow], 0);
			}
		}
		// here we perform the covering/uncovering nd backtracking to get the solution
		// rows.
		for (int i = 0; i < suspectedSolRows.size(); i++) {
			this.solutionRows.add(suspectedSolRows.get(i));
			String[] splitValue = this.rowSolMap.get(suspectedSolRows.get(i)).split(",");
			int row = Integer.parseInt(splitValue[0]);
			int col = Integer.parseInt(splitValue[1]);
			int num = Integer.parseInt(splitValue[2]);
			int index = -1;
			boolean flag = false;
			// calculate the index of the cell which corresponds to the calculated row and
			// column in the actual sudoku grid.
			for (int indexI = 0; indexI < this.size; indexI++) {
				for (int indexJ = 0; indexJ < this.size; indexJ++) {
					++index;
					if (indexI == row && indexJ == col) {
						flag = true;
						break;
					}
				}
				if (flag)
					break;
			}
			// calculate the columns that satisfy the row selected s the solution for the
			// selected column constraint.
			int cellConstraintColumnToBeCovered = this.transform.getCellConstraintColumn(this.size, row, col);
			int rowConstraintColumnToBeCovered = this.transform.getRowConstraintColumn(this.size, row, num);
			int colConstraintColumnToBeCovered = this.transform.getColConstraintColumn(this.size, col, num);
			int boxConstraintColumnToBeCovered = this.transform.getBoxConstraintColumn(this.size, row, col, num, index);
			int[][] previousMatrix = this.matrix;
			this.transform.cover(this.colsCovered, this.matrix, suspectedSolRows.get(i),
					cellConstraintColumnToBeCovered, rowConstraintColumnToBeCovered, colConstraintColumnToBeCovered,
					boxConstraintColumnToBeCovered);
			if (recursiveSolve()) {
				result = true;
				break;
			} else {// backtrack and choose the next row from the suspectedRows as a solution to the
					// selected column constraint.
				// uncover
				this.matrix = previousMatrix;
				this.colsCovered.remove(this.solutionRows.size() - 1);
				this.colsCovered.remove(this.solutionRows.size() - 1);
				this.colsCovered.remove(this.solutionRows.size() - 1);
				this.colsCovered.remove(this.solutionRows.size() - 1);
				this.solutionRows.remove(this.solutionRows.size() - 1);
				result = false;
			}
		}
		return result;
	}

	private void createCoverMatrix(int[][] grid) {
		int v = 0;
		for (int row = 0; row < this.size; row++) {
			for (int col = 0; col < this.size; col++) {
				for (int num = 0; num < this.size; num++) {
					String key = "" + row + "," + col + "," + num;
					this.rowSolMap.put(v++, key);
				}
			}
		}
		this.matrix = this.transform.createCoverMatrix(grid, this.size, this.acceptedNumbers, this.colsCovered);
	}
} // end of class AlgorXSolver
