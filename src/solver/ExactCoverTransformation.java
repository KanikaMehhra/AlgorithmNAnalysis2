package solver;

import java.util.Arrays;
import java.util.List;

public class ExactCoverTransformation {
	private static final int UNASSIGNED = -1;
	private static final int CONSTRAINTS = 4;

	protected int getIndexFromCoverMatrix(int size, int row, int col, int num) {
		return (row) * size * size + (col) * size + (num);
	}

	private int boxConstraintsCreation(int[][] matrix, int head, int size) {
		int smallGridSize = (int) Math.sqrt(size);
		for (int row = 0; row < size; row += smallGridSize) {
			for (int col = 0; col < size; col += smallGridSize) {
				for (int num = 0; num < size; num++, head++) {
					for (int rowDelValue = 0; rowDelValue < smallGridSize; rowDelValue++) {
						for (int colDelValue = 0; colDelValue < smallGridSize; colDelValue++) {
							int index = getIndexFromCoverMatrix(size, row + rowDelValue, col + colDelValue, num);
							matrix[index][head] = 1;
						}
					}
				}
			}
		}

		return head;
	}

	private int columnConstraintsCreation(int[][] matrix, int head, int size) {
		for (int col = 0; col < size; col++) {
			for (int num = 0; num < size; num++, head++) {
				for (int row = 0; row < size; row++) {
					int index = getIndexFromCoverMatrix(size, row, col, num);
					matrix[index][head] = 1;
				}
			}
		}

		return head;
	}

	private int rowConstraintsCreation(int[][] matrix, int head, int size) {
		for (int row = 0; row < size; row++) {
			for (int num = 0; num < size; num++, head++) {
				for (int col = 0; col < size; col++) {
					int index = getIndexFromCoverMatrix(size, row, col, num);
					matrix[index][head] = 1;
				}
			}
		}

		return head;
	}

	private int cellConstraintsCreation(int[][] matrix, int head, int size) {
		for (int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++, head++) {
				for (int num = 0; num < size; num++) {
					int index = getIndexFromCoverMatrix(size, row, col, num);
					matrix[index][head] = 1;
				}
			}
		}

		return head;
	}

	protected int[][] transformSudokuGridToCoverMatrix(int size) {
		int[][] coverMatrix = new int[size * size * size][size * size * CONSTRAINTS];
		int head = 0;
		head = cellConstraintsCreation(coverMatrix, head, size);
		head = rowConstraintsCreation(coverMatrix, head, size);
		head = columnConstraintsCreation(coverMatrix, head, size);
		boxConstraintsCreation(coverMatrix, head, size);
		return coverMatrix;
	}

	protected int[][] createCoverMatrix(int[][] grid, int size, List<Integer> acceptedNumbers,
			List<Integer> colsCovered) {
		int[][] coverMatrix = transformSudokuGridToCoverMatrix(size);
		// cover the rows and columns associated with the given hints in the initial
		// sudoku grid.
		int index = 0;
		for (int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++) {
				int value = grid[row][col];

				if (value != UNASSIGNED) {
					for (int num = 0; num < size; num++) {
						if (acceptedNumbers.get(num) == value) {
							int cellConstraintColumnToBeCovered = getCellConstraintColumn(size, row, col);
							int rowConstraintColumnToBeCovered = getRowConstraintColumn(size, row, num);
							int colConstraintColumnToBeCovered = getColConstraintColumn(size, col, num);
							int boxConstraintColumnToBeCovered = getBoxConstraintColumn(size, row, col, num, index);
							int bigRow = getIndexFromCoverMatrix(size, row, col, num);
							cover(colsCovered, coverMatrix, bigRow, cellConstraintColumnToBeCovered,
									rowConstraintColumnToBeCovered, colConstraintColumnToBeCovered,
									boxConstraintColumnToBeCovered);

						}
					}
				}
				index++;
			}
		}
		return coverMatrix;
	}

	protected int getCellConstraintColumn(int size, int row, int col) {
		return row * size + col;
	}

	protected int getRowConstraintColumn(int size, int row, int num) {
		return size * size + row * size + num;
	}

	protected int getColConstraintColumn(int size, int col, int num) {
		return 2 * size * size + col * size + num;
	}

	protected int getBoxConstraintColumn(int size, int row, int col, int num, int index) {
		int smallGridSize = (int) Math.sqrt(size);
		// calculation for box number starting from 0;
		int boxwidth = smallGridSize; /* Width of a small box */
		int boxheight = smallGridSize; /* Height of a small box */
		int hboxes = smallGridSize;/* Boxes horizontally */
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

		return 3 * size * size + box * size + num;
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
