/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */
package solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
	private List<Integer> solutionRows;
	private Map<Integer, String> rowSolMap;
	// private int[][] originalMatrix

	public AlgorXSolver() {
		this.matrix = null;
		this.size = 0;
		this.acceptedNumbers = new ArrayList<Integer>();
		this.smallGridSize = 0;
		this.solutionRows = new ArrayList<Integer>();
		this.rowSolMap = new HashMap<Integer, String>();

		int value = 0;
		for (int row = 0; row < this.size; row++) {
			for (int col = 0; col < this.size; col++) {
				for (int num = 0; num < this.size; num++) {
					String key = "" + row + "," + col + "," + num;
					this.rowSolMap.put(value++, key);
				}
			}
		}

	} // end of AlgorXSolver()

	@Override
	public boolean solve(SudokuGrid grid) {
		this.size = grid.getSudokuGridLength();
		this.acceptedNumbers = grid.getListOfvalidIntegers();
		this.smallGridSize = (int) Math.sqrt(this.size);
		createCoverMatrix(grid.getSudokuGrid());
		System.out.println(recursiveSolve());

		// List<Integer> emptyList = new ArrayList<Integer>();

		return false;
	} // end of solve()

	private boolean recursiveSolve() {
		for (int[] row : this.matrix) {
			List<Integer> rowList = Arrays.stream(row).boxed().collect(Collectors.toList());
			Set<Integer> s = new HashSet<>(rowList);
			if (s.size() == 1 && row[0] == 0) {
				return true;
			}
		}

		// int maxConstraintColValue = 0;
		List<Integer> minConstraintColValues = new ArrayList<Integer>();

		for (int j = 0; j < this.matrix.length; j++) {
			int colMinValue = 0;
			for (int i = 0; i < this.matrix.length; i++) {
				colMinValue += this.matrix[i][j];
			}
			minConstraintColValues.add(colMinValue);
		}

		int minConstraintCol = minConstraintColValues.indexOf(Collections.min(minConstraintColValues));
		if (minConstraintColValues.get(minConstraintCol) == 0)
			return false;
		List<Integer> suspectedSolRows = new ArrayList<Integer>();
		// if (minConstraintColValues.get(minConstraintCol) != 0) {
		// int foundFirstSolInstance = 0;
		for (int outerRow = 0; outerRow < this.matrix.length; outerRow++) {
			if (this.matrix[outerRow][minConstraintCol] == 1) {
				suspectedSolRows.add(outerRow);
				Arrays.fill(this.matrix[outerRow], 0);

				/*
				 * String[] splitValue = this.rowSolMap.get(outerRow).split(","); int row =
				 * Integer.parseInt(splitValue[0]); int col = Integer.parseInt(splitValue[1]);
				 * int num = Integer.parseInt(splitValue[2]); int index = -1;
				 * 
				 * for (int i = 0; i < row; i++) { for (int j = 0; j < this.size; j++) {
				 * ++index; } }
				 * 
				 * for (int k = 0; k <= row; k++) { ++index; }
				 * 
				 * int cellConstraintColumnToBeCovered = getCellConstraintColumn(row, col); int
				 * rowConstraintColumnToBeCovered = getRowConstraintColumn(row, num); int
				 * colConstraintColumnToBeCovered = getColConstraintColumn(col, num); int
				 * boxConstraintColumnToBeCovered = getBoxConstraintColumn(row, col, num,
				 * index);
				 * 
				 * cover(outerRow, cellConstraintColumnToBeCovered,
				 * rowConstraintColumnToBeCovered, colConstraintColumnToBeCovered,
				 * boxConstraintColumnToBeCovered);
				 */
			}
		}

		for (int i = 0; i < suspectedSolRows.size(); i++) {
			this.solutionRows.add(suspectedSolRows.get(i));
			String[] splitValue = this.rowSolMap.get(suspectedSolRows.get(i)).split(",");
			int row = Integer.parseInt(splitValue[0]);
			int col = Integer.parseInt(splitValue[1]);
			int num = Integer.parseInt(splitValue[2]);
			int index = -1;

			for (int in = 0; in < row; in++) {
				for (int j = 0; j < this.size; j++) {
					++index;
				}
			}

			for (int k = 0; k <= row; k++) {
				++index;
			}
			int cellConstraintColumnToBeCovered = getCellConstraintColumn(row, col);
			int rowConstraintColumnToBeCovered = getRowConstraintColumn(row, num);
			int colConstraintColumnToBeCovered = getColConstraintColumn(col, num);
			int boxConstraintColumnToBeCovered = getBoxConstraintColumn(row, col, num, index);
			int[][] previousMatrix=this.matrix;
			cover(suspectedSolRows.get(i), cellConstraintColumnToBeCovered, rowConstraintColumnToBeCovered,
					colConstraintColumnToBeCovered, boxConstraintColumnToBeCovered);
			if (recursiveSolve()) {
				return true;
			} else {
				this.matrix=previousMatrix;
				this.solutionRows.remove(this.solutionRows.size() - 1);
			}
		}
		// }
		// else {
		//
		// }

		return true;
	}

	private void cover(int bigRow, int cellConstraintColumnToBeCovered, int rowConstraintColumnToBeCovered,
			int colConstraintColumnToBeCovered, int boxConstraintColumnToBeCovered) {
		Arrays.fill(this.matrix[bigRow], 0);
		for (int i = 0; i < this.matrix.length; i++) {
			if (this.matrix[i][cellConstraintColumnToBeCovered] == 1) {
				// this.matrix[i][cellConstraintColumnToBeCovered] = 0;
				Arrays.fill(this.matrix[i], 0);
			}
			if (this.matrix[i][rowConstraintColumnToBeCovered] == 1) {
				// this.matrix[i][rowConstraintColumnToBeCovered] = 0;
				Arrays.fill(this.matrix[i], 0);
			}
			if (this.matrix[i][colConstraintColumnToBeCovered] == 1) {
				// this.matrix[i][colConstraintColumnToBeCovered] = 0;
				Arrays.fill(this.matrix[i], 0);
			}
			if (this.matrix[i][boxConstraintColumnToBeCovered] == 1) {
				// this.matrix[i][boxConstraintColumnToBeCovered] = 0;
				Arrays.fill(this.matrix[i], 0);

			}
		}
	}

	private void uncover() {

	}

	private int getIndexFromCoverMatrix(int row, int col, int num) {
		return (row) * this.size * this.size + (col) * this.size + (num);
	}

	private int[][] transformSudokuGridToCoverMatrix() {
		int[][] coverMatrix = new int[this.size * this.size * this.size][this.size * this.size * CONSTRAINTS];
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

	private void createCoverMatrix(int[][] grid) {
		this.matrix = transformSudokuGridToCoverMatrix();
		// cover(grid);
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
							cover(bigRow, cellConstraintColumnToBeCovered, rowConstraintColumnToBeCovered,
									colConstraintColumnToBeCovered, boxConstraintColumnToBeCovered);
						}
					}
				}
				index++;
			}
		}
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
