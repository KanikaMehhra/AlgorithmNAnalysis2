package solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import grid.SudokuGrid;

public class ExactCoverTransformation {
	private static final int UNASSIGNED = -1;
	private static final int CONSTRAINTS = 4;
	private int[][] matrix;
	private int size;
	private int smallGridSize;
	private List<Integer> acceptedNumbers;
	protected List<Integer> colsCovered;

	public ExactCoverTransformation(SudokuGrid grid) {
		this.size = grid.getSudokuGridLength();
		this.smallGridSize = (int) Math.sqrt(this.size);
		this.matrix = null;
		this.acceptedNumbers = grid.getListOfvalidIntegers();
		this.colsCovered = new ArrayList<Integer>();

	}

	protected int getIndexFromCoverMatrix(int row, int col, int num) {
		return (row) * this.size * this.size + (col) * this.size + (num);
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

	protected int[][] transformSudokuGridToCoverMatrix() {
		int[][] coverMatrix = new int[this.size * this.size * this.size][this.size * this.size * CONSTRAINTS];
		int head = 0;
		head = cellConstraintsCreation(coverMatrix, head);
		head = rowConstraintsCreation(coverMatrix, head);
		head = columnConstraintsCreation(coverMatrix, head);
		boxConstraintsCreation(coverMatrix, head);
		return coverMatrix;
	}

	protected int[][] createCoverMatrix(int[][] grid) {
		this.matrix = transformSudokuGridToCoverMatrix();
		// cover the rows and columns associated with the given hints in the initial
		// sudoku grid.
		int index = 0;
		for (int row = 0; row < this.size; row++) {
			for (int col = 0; col < this.size; col++) {
				int value = grid[row][col];

				if (value != UNASSIGNED) {
					for (int num = 0; num < this.size; num++) {
						if (this.acceptedNumbers.get(num) == value) {
							int cellConstraintColumnToBeCovered = getCellConstraintColumn(row, col);
							int rowConstraintColumnToBeCovered = getRowConstraintColumn(row, num);
							int colConstraintColumnToBeCovered = getColConstraintColumn(col, num);
							int boxConstraintColumnToBeCovered = getBoxConstraintColumn(row, col, num, index);
							int bigRow = getIndexFromCoverMatrix(row, col, num);
							cover(this.colsCovered, this.matrix, bigRow, cellConstraintColumnToBeCovered,
									rowConstraintColumnToBeCovered, colConstraintColumnToBeCovered,
									boxConstraintColumnToBeCovered);

						}
					}
				}
				index++;
			}
		}
		return this.matrix;
	}

	protected int getCellConstraintColumn(int row, int col) {
		return row * this.size + col;
	}

	protected int getRowConstraintColumn(int row, int num) {
		return this.size * this.size + row * this.size + num;
	}

	protected int getColConstraintColumn(int col, int num) {
		return 2 * this.size * this.size + col * this.size + num;
	}

	protected int getBoxConstraintColumn(int row, int col, int num, int index) {
		// calculation for box number starting from 0;
		int boxwidth = this.smallGridSize; /* Width of a small box */
		int boxheight = this.smallGridSize; /* Height of a small box */
		int hboxes = this.smallGridSize;/* Boxes horizontally */
		// int vboxes = this.smallGridSize; /* Boxes vertically */
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

	protected void cover(List<Integer> colsCovered, int[][] matrix, int bigRow, int cellConstraintColumnToBeCovered,
			int rowConstraintColumnToBeCovered, int colConstraintColumnToBeCovered,
			int boxConstraintColumnToBeCovered) {
		Arrays.fill(matrix[bigRow], 0);
		for (int i = 0; i < matrix.length; i++) {
			if (matrix[i][cellConstraintColumnToBeCovered] == 1) {
				Arrays.fill(matrix[i], 0);
			}
			if (matrix[i][rowConstraintColumnToBeCovered] == 1) {
				Arrays.fill(matrix[i], 0);
			}
			if (matrix[i][colConstraintColumnToBeCovered] == 1) {
				Arrays.fill(matrix[i], 0);
			}
			if (matrix[i][boxConstraintColumnToBeCovered] == 1) {
				Arrays.fill(matrix[i], 0);

			}
		}
		colsCovered.add(cellConstraintColumnToBeCovered);
		colsCovered.add(rowConstraintColumnToBeCovered);
		colsCovered.add(colConstraintColumnToBeCovered);
		colsCovered.add(boxConstraintColumnToBeCovered);
	}

}
