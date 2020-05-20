/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */
package solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
		this.solutionRows=new ArrayList<Integer[]>();
	} // end of AlgorXSolver()

	@Override
	public boolean solve(SudokuGrid grid) {
		this.size = grid.getSudokuGridLength();
		this.acceptedNumbers = grid.getListOfvalidIntegers();
		this.smallGridSize = (int) Math.sqrt(this.size);
		this.matrix=createCoverMatrix(grid.getSudokuGrid());
		return false;
	} // end of solve()
	
	private boolean recursiveSolve() {
		if(this.matrix[0].length==0) {
			return true;
		}
		int constraintCol=0;
		for(int row=0;row<this.size;row++) {
			if(this.matrix[row][constraintCol]==1) {
//				this.solutionRows.add(e)
			}
		}
		return false;
	}

	private int getIndexFromCoverMatrix(int row, int col, int num) {
		return (row) * this.size * this.size + (col) * this.size + (num);
	}

	private int[][] transformSudokuGridToCoverMatrix() {
		int[][] coverMatrix = new int[this.size * this.size * this.size][this.size * this.size * CONSTRAINTS];
//		for (int[] row : coverMatrix)
//			Arrays.fill(row, -1);
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
							int index = getIndexFromCoverMatrix(row + rowDelValue, col + colDelValue,
									num);
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

		for (int row = 0; row < this.size; row++) {
			for (int col = 0; col < this.size; col++) {
				int value = grid[row][col];

				if (value != UNASSIGNED) {
					for (int num = 0; num < this.size; num++) {
						if (this.acceptedNumbers.get(num) != value) {
							Arrays.fill(coverMatrix[getIndexFromCoverMatrix(row, col, num)], 0);
						}
					}
				}
			}
		}

		return coverMatrix;
	}

} // end of class AlgorXSolver
