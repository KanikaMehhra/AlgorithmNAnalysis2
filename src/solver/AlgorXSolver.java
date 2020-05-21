/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */
package solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import grid.SudokuGrid;

/**
 * Algorithm X solver for standard Sudoku.
 */
public class AlgorXSolver extends StdSudokuSolver {
	private static final int UNASSIGNED = -1;
	private static final int CONSTRAINTS = 4;
	private int[][] matrix;
	private int size;
	private int smallGridSize;
	private List<Integer> acceptedNumbers;
	private List<Integer[]> solutionRows;

	public AlgorXSolver() {
		this.matrix = null;
		this.size = 0;
		this.acceptedNumbers = new ArrayList<Integer>();
		this.smallGridSize = 0;
		this.solutionRows = new ArrayList<Integer[]>();
	} // end of AlgorXSolver()

	@Override
	public boolean solve(SudokuGrid grid) {
		this.size = grid.getSudokuGridLength();
		this.acceptedNumbers = grid.getListOfvalidIntegers();
		this.smallGridSize = (int) Math.sqrt(this.size);
		this.matrix = createCoverMatrix(grid.getSudokuGrid());

		return false;
	} // end of solve()

	private boolean recursiveSolve() {
		if (this.matrix[0].length == 0) {
			return true;
		}
		int constraintCol = 0;
		for (int row = 0; row < this.size; row++) {
			if (this.matrix[row][constraintCol] == 1) {
				// this.solutionRows.add(e)
			}
		}
		return false;
	}

	private int getIndexFromCoverMatrix(int row, int col, int num) {
		return (row) * this.size * this.size + (col) * this.size + (num);
	}

	private int[][] transformSudokuGridToCoverMatrix() {
		int[][] coverMatrix = new int[this.size * this.size * this.size][this.size * this.size * CONSTRAINTS];
		// for (int[] row : coverMatrix)
		// Arrays.fill(row, -1);
		int head = 0;
		head = cellConstraintsCreation(coverMatrix, head);
		head = rowConstraintsCreation(coverMatrix, head);
		head = columnConstraintsCreation(coverMatrix, head);
		boxConstraintsCreation(coverMatrix, head);
		for (int[] row : coverMatrix) {
			System.out.println(Arrays.toString(row));
		}
		return coverMatrix;
	}

	private int boxConstraintsCreation(int[][] matrix, int head) {
		for (int row = 0; row < this.size; row += this.smallGridSize) {
			for (int col = 0; col < this.size; col += this.smallGridSize) {
				for (int num = 0; num < this.size; num++, head++) {
					for (int rowDelValue = 0; rowDelValue < this.smallGridSize; rowDelValue++) {
						for (int colDelValue = 0; colDelValue < this.smallGridSize; colDelValue++) {
							int index = getIndexFromCoverMatrix(row + rowDelValue, col + colDelValue, num);
							matrix[index][head] = 1;
						}
					}
				}
			}
		}

		return head;
	}

	private int columnConstraintsCreation(int[][] matrix, int head) {
		for (int col = 0; col < this.size; col++) {
			for (int num = 0; num < this.size; num++, head++) {
				for (int row = 0; row < this.size; row++) {
					int index = getIndexFromCoverMatrix(row, col, num);
					matrix[index][head] = 1;
				}
			}
		}

		return head;
	}

	private int rowConstraintsCreation(int[][] matrix, int head) {
		for (int row = 0; row < this.size; row++) {
			for (int num = 0; num < this.size; num++, head++) {
				for (int col = 0; col < this.size; col++) {
					int index = getIndexFromCoverMatrix(row, col, num);
					matrix[index][head] = 1;
				}
			}
		}

		return head;
	}

	private int cellConstraintsCreation(int[][] matrix, int head) {
		for (int row = 0; row < this.size; row++) {
			for (int col = 0; col < this.size; col++, head++) {
				for (int num = 0; num < this.size; num++) {
					int index = getIndexFromCoverMatrix(row, col, num);
					matrix[index][head] = 1;
				}
			}
		}

		return head;
	}

	private int[][] createCoverMatrix(int[][] grid) {
		int[][] coverMatrix = transformSudokuGridToCoverMatrix();
		int index = 0;
		for (int row = 0; row < this.size; row++) {
			for (int col = 0; col < this.size; col++) {
				int value = grid[row][col];

				if (value != UNASSIGNED) {
					for (int num = 0; num < this.size; num++) {
						if (this.acceptedNumbers.get(num) == value) {
							Arrays.fill(coverMatrix[getIndexFromCoverMatrix(row, col, num)], 0);
							int cellConstraintColumnToBeCovered = getCellConstraintColumn(row, col);
							int rowConstraintColumnToBeCovered = getRowConstraintColumn(row, num);
							int colConstraintColumnToBeCovered = getColConstraintColumn(col, num);
							int boxConstraintColumnToBeCovered = getBoxConstraintColumn(row, col, num, index);
							for (int i = 0; i < coverMatrix.length; i++) {
								coverMatrix[i][cellConstraintColumnToBeCovered] = 0;
								coverMatrix[i][rowConstraintColumnToBeCovered] = 0;
								coverMatrix[i][colConstraintColumnToBeCovered] = 0;
								coverMatrix[i][boxConstraintColumnToBeCovered] = 0;
							}

						}
					}
				}
				index++;
			}
		}

		return coverMatrix;
	}

	private int getCellConstraintColumn(int row, int col) {
		return row * this.size + col;
	}

	private int getRowConstraintColumn(int row, int num) {
		return this.size * this.size + row * this.size + num;
	}

	private int getColConstraintColumn(int col, int num) {
		return 2 * this.size * this.size + col * this.size + num;
	}

	private int getBoxConstraintColumn(int row, int col, int num, int index) {
		// calculation for box number starting from 0;
		int boxwidth = this.smallGridSize; /* Width of a small box */
		int boxheight = this.smallGridSize; /* Height of a small box */
		int hboxes = this.smallGridSize;/* Boxes horizontally */
		int vboxes = this.smallGridSize; /* Boxes vertically */
		int width = boxwidth * hboxes;
		// int height = boxheight * vboxes;
		int y = index / width;
		int x = index % width;
		int boxy = y / boxheight;
		// int innery = y % boxheight;
		int boxx = x / boxwidth;
		// int innerx = x % boxwidth;
		int box = boxx + hboxes * boxy;

		return 3 * this.size * this.size + box * this.size + num;
	}

} // end of class AlgorXSolver
