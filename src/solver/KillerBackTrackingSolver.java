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
	private List<Cage> cages;
	private int maxTotal;

	public KillerBackTrackingSolver() {
		// TODO: any initialisation you want to implement.
		this.matrix = null;
		this.size = 0;
		this.acceptedNumbers = new ArrayList<Integer>();
		this.cageCoordsWithValuesMap = new HashMap<List<String>, Integer>();
		this.numberOfCages = 0;
		this.grid = null;
		this.cages = new ArrayList<Cage>();
		this.maxTotal = 0;

	} // end of KillerBackTrackingSolver()

	@Override
	public boolean solve(SudokuGrid grid) {
		// TODO: your implementation of a backtracking solver for Killer Sudoku.
		this.grid = grid;
		this.size = grid.getSudokuGridLength();
		this.acceptedNumbers = grid.getListOfvalidIntegers();
		for (int num : this.acceptedNumbers) {
			this.maxTotal += num;
		}
		this.matrix = grid.getSudokuGrid();
		this.cageCoordsWithValuesMap = ((KillerSudokuGrid) grid).getCageCoordsWithValuesMap();
		this.numberOfCages = this.cageCoordsWithValuesMap.size();
//		setCagesInfo();
//		cageRowSolver();
//		cageColSolver();
//		cageBoxSolver();

		 return recursiveSolve();
		// placeholder
//		return false;
	} // end of solve()

	public void cageRowSolver() {
		for (int i = 0; i < this.size; i++) {
			List<Cage> cagesInRowi = new ArrayList<Cage>();
			for (Cage cage : this.cages) {
				if (cage.rowNum == i) {
					cagesInRowi.add(cage);
				}
			}
			if (cagesInRowi.size() > 0) {
				// calculate the number of cells covered with cages in row i
				// int rowCagesCellSum = 0;
				List<Integer> colsCovered = new ArrayList<Integer>();
				int totalValueIniRow = 0;

				for (Cage cage : cagesInRowi) {
					totalValueIniRow += cage.value;
					// rowCagesCellSum += cage.coordinates.size();
					for (String coord : cage.coordinates) {
						String[] splitCoord = coord.split(",");
						colsCovered.add(Integer.parseInt(splitCoord[1]));
					}
				}
				// look if any cell in this row has a value already filled in
				int alreadyFilledValue = 0;
				// int numOfAlreadyFilledCells = 0;
				for (int col = 0; col < this.size; col++) {
					if (!colsCovered.contains(col)) {
						if (this.matrix[i][col] != UNASSIGNED) {
							alreadyFilledValue = this.matrix[i][col];
							colsCovered.add(col);
							// numOfAlreadyFilledCells++;
						}
					}
				}
				// total value in a row
				totalValueIniRow += alreadyFilledValue;
				// find the col which acts as an innie for ith row.
				int colLeft = -1;
				if (colsCovered.size() == this.size - 1) {
					for (int c = 0; c < this.size; c++) {
						if (!colsCovered.contains(c)) {
							colLeft = c;
							break;
						}
					}
					this.matrix[i][colLeft] = this.maxTotal - totalValueIniRow;
				}
			}

		}

	}

	private List<String> cellsInBox(int row, int col) {
		List<String> returnList = new ArrayList<String>();
		int sqrt = (int) Math.sqrt(this.size);
		int r = row - row % sqrt;
		int c = col - col % sqrt;

		for (int i = r; i < r + sqrt; i++) {
			for (int j = c; j < c + sqrt; j++) {
				// if (this.matrix[i][j] == number) {
				// return true;
				// }
				returnList.add("" + i + "," + j);
			}
		}
		return returnList;
	}

	public void cageBoxSolver() {
		for (int i = 0; i < this.size; i++) {
			List<Cage> cagesInBoxi = new ArrayList<Cage>();
			for (Cage cage : this.cages) {
				if (cage.boxNum == i) {
					cagesInBoxi.add(cage);
				}
			}
			if (cagesInBoxi.size() > 0) {
				// calculate the number of cells covered with cages in row i
				// int rowCagesCellSum = 0;
				List<String> cellsCovered = new ArrayList<String>();
				int totalValueIniBox = 0;

				for (Cage cage : cagesInBoxi) {
					totalValueIniBox += cage.value;
					// rowCagesCellSum += cage.coordinates.size();
					for (String coord : cage.coordinates) {
						// String[] splitCoord = coord.split(",");
						cellsCovered.add(coord);
						// colsCovered.add(Integer.parseInt(splitCoord[1]));
					}
				}
				// get the list of all the cells in ith box.
				String[] rc = cagesInBoxi.get(0).coordinates.get(0).split(",");
				List<String> cellsInBoxi = cellsInBox(Integer.parseInt(rc[0]), Integer.parseInt(rc[1]));
				// look if any cell in this box has a value already filled in
				int alreadyFilledValue = 0;
				// int numOfAlreadyFilledCells = 0;
				for (int cell = 0; cell < this.size; cell++) {
					if (!cellsCovered.contains(cellsInBoxi.get(cell))) {
						String[] split = cellsInBoxi.get(cell).split(",");
						if (this.matrix[Integer.parseInt(split[0])][Integer.parseInt(split[1])] != UNASSIGNED) {
							alreadyFilledValue = this.matrix[Integer.parseInt(split[0])][Integer.parseInt(split[1])];
							cellsCovered.add(cellsInBoxi.get(cell));
							// numOfAlreadyFilledCells++;
						}
					}
				}
				// total value in a row
				totalValueIniBox += alreadyFilledValue;
				// find the col which acts as an innie for ith row.
				String cellLeft = "";
				if (cellsCovered.size() == this.size - 1) {
					for (int cell = 0; cell < this.size; cell++) {
						if (!cellsCovered.contains(cellsInBoxi.get(cell))) {
							cellLeft = cellsInBoxi.get(cell);
							break;
						}
					}
					String[] splitCell = cellLeft.split(",");
					this.matrix[Integer.parseInt(splitCell[0])][Integer.parseInt(splitCell[0])] = this.maxTotal
							- totalValueIniBox;
				}
			}

		}

	}

	public void cageColSolver() {
		for (int i = 0; i < this.size; i++) {
			List<Cage> cagesInColi = new ArrayList<Cage>();
			for (Cage cage : this.cages) {
				if (cage.colNum == i) {
					cagesInColi.add(cage);
				}
			}

			if (cagesInColi.size() > 0) {
				// calculate the number of cells covered with cages in row i
				// int rowCagesCellSum = 0;
				List<Integer> rowsCovered = new ArrayList<Integer>();
				int totalValueIniCol = 0;

				for (Cage cage : cagesInColi) {
					totalValueIniCol += cage.value;
					// rowCagesCellSum += cage.coordinates.size();
					for (String coord : cage.coordinates) {
						String[] splitCoord = coord.split(",");
						rowsCovered.add(Integer.parseInt(splitCoord[0]));
					}
				}
				// look if any cell in this row has a value already filled in
				int alreadyFilledValue = 0;
				// int numOfAlreadyFilledCells = 0;
				for (int row = 0; row < this.size; row++) {
					if (!rowsCovered.contains(row)) {
						if (this.matrix[row][i] != UNASSIGNED) {
							alreadyFilledValue = this.matrix[row][i];
							rowsCovered.add(row);
							// numOfAlreadyFilledCells++;
						}
					}
				}
				// total value in a row
				totalValueIniCol += alreadyFilledValue;
				// find the col which acts as an innie for ith row.
				int rowLeft = -1;
				if (rowsCovered.size() == this.size - 1) {
					for (int r = 0; r < this.size; r++) {
						if (!rowsCovered.contains(r)) {
							rowLeft = r;
							break;
						}
					}
					this.matrix[rowLeft][i] = this.maxTotal - totalValueIniCol;
				}
			}

		}

	}

	public void setCagesInfo() {
		for (Map.Entry<List<String>, Integer> entry : this.cageCoordsWithValuesMap.entrySet()) {
			Cage cage = new Cage(entry.getValue(), entry.getKey(), this.size);
			this.cages.add(cage);
		}
	}

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
