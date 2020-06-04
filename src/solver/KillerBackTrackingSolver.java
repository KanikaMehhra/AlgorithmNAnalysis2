/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

package solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
	private Map<Cage, List<List<Integer>>> cagesPermutationsMap;
	private List<Cage> cagesCovered;
	private List<Cage> cagesLeft;
	// private List<List<Integer>> answerPermutations;
	// private List<List<Integer>> combinationList;
	private List<Cell> cells;

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
		this.cagesPermutationsMap = new HashMap<Cage, List<List<Integer>>>();
		this.cagesCovered = new ArrayList<Cage>();
		// this.answerPermutations = new ArrayList<List<Integer>>();
		this.cagesLeft = new ArrayList<Cage>();
		// this.combinationList = new ArrayList<List<Integer>>();
		this.cells = new ArrayList<Cell>();

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
		setCagesInfo();
		Collections.sort(this.cells, new CellIndexComparator());
		// Arrays.sort(this.cells, Comparator.comparing(Cell::index));
		// Collections.sort(this.cells);
		// calculateCombinationPermutation();
		// cageRowSolver();
		// cageColSolver();
		// cageBoxSolver();

		// return recursiveSolve();
		// placeholder
		// return rSolve();
		return newSolve(0);
	} // end of solve()

	public boolean newSolve(int cellIndex) {
		for (int i = cellIndex; i < this.cells.size(); i++) {
			Cell cell = this.cells.get(i);
			if (this.matrix[cell.row][cell.col] == UNASSIGNED) {
				for (int permittedValue : cell.permittedIntegers) {
					for (List<Integer> permutation : this.cages
							.get(cell.cageId).mapOfPermutationsStartingWithASpecificDigit.get(permittedValue)) {
						fillCageCoords(this.cages.get(cell.cageId), permutation);
						if (commonValidate()) {
							if (newSolve(i + 1)) {
								return true;
							} else {
								unFillCageCoords(this.cages.get(cell.cageId));
							}
						} else {
							unFillCageCoords(this.cages.get(cell.cageId));
							// if(!isInRow(row, number))
							// break;
						}
					}
					if (permittedValue == cell.permittedIntegers.get(cell.permittedIntegers.size() - 1))
						return false;
				}
				return false;
			}
		}
		return true;
	}

	public boolean isHashLengthSame(List<Integer> list) {
		Set<Integer> hash = new HashSet<Integer>(list);
		return (hash.size() == list.size());
	}

	// public boolean validate(Cage smallestValueCage) {
	// // TODO
	// if (!commonValidate())
	// return false;
	// // for(St)
	// // smallestValueCage
	// for (Map.Entry<List<String>, Integer> entry :
	// this.cageCoordsWithValuesMap.entrySet()) {
	// int sum = 0;
	// List<Integer> cageCoordValues = new ArrayList<Integer>();
	//
	// for (String coord : entry.getKey()) {
	// String[] coordSplit = coord.split(",");
	//
	// if
	// (this.matrix[Integer.parseInt(coordSplit[0])][Integer.parseInt(coordSplit[1])]
	// != UNASSIGNED) {
	// int value =
	// this.matrix[Integer.parseInt(coordSplit[0])][Integer.parseInt(coordSplit[1])];
	// sum += value;
	// cageCoordValues.add(this.matrix[Integer.parseInt(coordSplit[0])][Integer.parseInt(coordSplit[1])]);
	// }
	//
	// }
	// if (entry.getValue() != sum)
	// return false;
	// // checks the uniqueness of cage values.
	// if (!this.grid.isHashLengthSame(cageCoordValues))
	// return false;
	// }
	// return true;
	// }

	public boolean commonValidate() {
		int smallGridSize = (int) Math.sqrt(this.size);
		for (int[] row : this.matrix) {
			List<Integer> rowList = Arrays.stream(row).boxed().collect(Collectors.toList());
			rowList.removeAll(Collections.singleton(UNASSIGNED));
			// checks first condition
			if (!this.acceptedNumbers.containsAll(rowList)) {
				return false;
			}
			// checks second condition
			if (!isHashLengthSame(rowList)) {
				return false;
			}
		}

		for (int j = 0; j < this.size; j++) {
			List<Integer> smallGridArray = new ArrayList<Integer>();
			List<Integer> colList = new ArrayList<Integer>();

			for (int i = 0; i < this.size; i++) {
				colList.add(this.matrix[i][j]);

				smallGridArray.add(i, this.matrix[(j / smallGridSize) * smallGridSize + i / smallGridSize][j
						* smallGridSize % this.size + i % smallGridSize]);
			}
			colList.removeAll(Collections.singleton(UNASSIGNED));
			smallGridArray.removeAll(Collections.singleton(UNASSIGNED));

			// checks third condition
			if (!isHashLengthSame(colList)) {
				return false;
			}
			// check fourth condition
			if (!isHashLengthSame(smallGridArray)) {
				return false;
			}

		}

		return true;
	}

//	private boolean rSolve() {
//		boolean returnValue = true;
//		if (!commonValidate()) {
//			return false;
//		}
//		if (this.cages.size() == this.cagesCovered.size()) {
//			return true;
//		}
//		Cage smallestValueCage = getCageWithSmallestValue();
//		if (smallestValueCage.id == 7) {
//			System.out.println(smallestValueCage.id);
//		}
//		for (List<Integer> permutation : this.cagesPermutationsMap.get(smallestValueCage)) {
//			this.cagesCovered.add(smallestValueCage);
//			this.cagesLeft.remove(smallestValueCage);
//			fillCageCoords(smallestValueCage, permutation);
//			if (rSolve()) {
//				returnValue = true;
//				break;
//			} else {
//				this.cagesCovered.remove(smallestValueCage);
//				this.cagesLeft.add(smallestValueCage);
//				unFillCageCoords(smallestValueCage);
//				returnValue = false;
//			}
//		}
//
//		return returnValue;
//	}
//
	private void fillCageCoords(Cage cage, List<Integer> permutation) {
		for (int i = 0; i < permutation.size(); i++) {
			String[] rc = cage.coordinates.get(i).split(",");
			int r = Integer.parseInt(rc[0]);
			int c = Integer.parseInt(rc[1]);
			this.matrix[r][c] = permutation.get(i);
		}
	}

	private void unFillCageCoords(Cage cage) {
		for (String cageCoord : cage.coordinates) {
			String[] rc = cageCoord.split(",");
			int r = Integer.parseInt(rc[0]);
			int c = Integer.parseInt(rc[1]);
			this.matrix[r][c] = UNASSIGNED;
		}
	}

//	private Cage getCageWithSmallestValue() {
//		Cage smallestValueCage = this.cagesLeft.get(0);
//		for (Cage cage : this.cagesLeft) {
//			if (cage.value < smallestValueCage.value) {
//				smallestValueCage = cage;
//			}
//		}
//		return smallestValueCage;
//	}

	// public List<List<Integer>> combinationSum3(int k, int n) {
	// List<List<Integer>> result = new ArrayList<List<Integer>>();
	// List<Integer> curr = new ArrayList<Integer>();
	// helper(result, curr, k, 0, n);
	// return result;
	// }
	//
	// public void helper(List<List<Integer>> result, List<Integer> curr, int k, int
	// start, int sum) {
	// if (sum < 0) {
	// return;
	// }
	//
	// if (sum == 0 && curr.size() == k) {
	// result.add(new ArrayList<Integer>(curr));
	// return;
	// }
	//
	// for (int i = start; i < this.size; i++) {
	// curr.add(this.acceptedNumbers.get(i));
	// helper(result, curr, k, i + 1, sum - this.acceptedNumbers.get(i));
	// curr.remove(curr.size() - 1);
	// }
	// }

	// private void calculateCombinationPermutation() {
	// for (Cage cage : this.cages) {
	// List<List<Integer>> solutionIntegersCombinations = new
	// ArrayList<List<Integer>>();
	// List<List<Integer>> solutionIntegersPermutations = new
	// ArrayList<List<Integer>>();
	// int value = cage.value;
	// int numOfCells = cage.coordinates.size();
	// solutionIntegersCombinations = combinationSum3(numOfCells, value);
	// for (List<Integer> combination : solutionIntegersCombinations) {
	// List<List<Integer>> permute = permute(combination);
	// for (List<Integer> permutation : permute) {
	// solutionIntegersPermutations.add(permutation);
	// }
	//
	// }
	// this.cagesPermutationsMap.put(cage, solutionIntegersPermutations);
	// }
	// }
	//
	// public List<List<Integer>> permute(List<Integer> nums) {
	// List<List<Integer>> result = new ArrayList<>();
	// helper(0, nums, result);
	// return result;
	// }
	//
	// private void helper(int start, List<Integer> nums, List<List<Integer>>
	// result) {
	// if (start == nums.size() - 1) {
	// ArrayList<Integer> list = new ArrayList<>();
	// for (int num : nums) {
	// list.add(num);
	// }
	// result.add(list);
	// return;
	// }
	//
	// for (int i = start; i < nums.size(); i++) {
	// swap(nums, i, start);
	// helper(start + 1, nums, result);
	// swap(nums, i, start);
	// }
	// }
	//
	// private void swap(List<Integer> nums, int i, int j) {
	// int temp = nums.get(i);
	// nums.set(i, nums.get(j));// [i] = nums.get(j);
	// nums.set(j, temp);// = temp;
	// }

//	public void cageRowSolver() {
//		for (int i = 0; i < this.size; i++) {
//			List<Cage> cagesInRowi = new ArrayList<Cage>();
//			for (Cage cage : this.cages) {
//				if (cage.rowNum == i) {
//					cagesInRowi.add(cage);
//				}
//			}
//			if (cagesInRowi.size() > 0) {
//				// calculate the number of cells covered with cages in row i
//				// int rowCagesCellSum = 0;
//				List<Integer> colsCovered = new ArrayList<Integer>();
//				int totalValueIniRow = 0;
//
//				for (Cage cage : cagesInRowi) {
//					totalValueIniRow += cage.value;
//					for (String coord : cage.coordinates) {
//						String[] splitCoord = coord.split(",");
//						colsCovered.add(Integer.parseInt(splitCoord[1]));
//					}
//				}
//				// look if any cell in this row has a value already filled in
//				int alreadyFilledValue = 0;
//				// int numOfAlreadyFilledCells = 0;
//				for (int col = 0; col < this.size; col++) {
//					if (!colsCovered.contains(col)) {
//						if (this.matrix[i][col] != UNASSIGNED) {
//							alreadyFilledValue = this.matrix[i][col];
//							colsCovered.add(col);
//							// numOfAlreadyFilledCells++;
//						}
//					}
//				}
//				// total value in a row
//				totalValueIniRow += alreadyFilledValue;
//				// find the col which acts as an innie for ith row.
//				int colLeft = -1;
//				if (colsCovered.size() == this.size - 1) {
//					for (int c = 0; c < this.size; c++) {
//						if (!colsCovered.contains(c)) {
//							colLeft = c;
//							break;
//						}
//					}
//					this.matrix[i][colLeft] = this.maxTotal - totalValueIniRow;
//				}
//			}
//
//		}
//
//	}
//
//	private List<String> cellsInBox(int row, int col) {
//		List<String> returnList = new ArrayList<String>();
//		int sqrt = (int) Math.sqrt(this.size);
//		int r = row - row % sqrt;
//		int c = col - col % sqrt;
//
//		for (int i = r; i < r + sqrt; i++) {
//			for (int j = c; j < c + sqrt; j++) {
//				// if (this.matrix[i][j] == number) {
//				// return true;
//				// }
//				returnList.add("" + i + "," + j);
//			}
//		}
//		return returnList;
//	}
//
//	public void cageBoxSolver() {
//		for (int i = 0; i < this.size; i++) {
//			List<Cage> cagesInBoxi = new ArrayList<Cage>();
//			for (Cage cage : this.cages) {
//				if (cage.boxNum == i) {
//					cagesInBoxi.add(cage);
//				}
//			}
//			if (cagesInBoxi.size() > 0) {
//				// calculate the number of cells covered with cages in row i
//				// int rowCagesCellSum = 0;
//				List<String> cellsCovered = new ArrayList<String>();
//				int totalValueIniBox = 0;
//
//				for (Cage cage : cagesInBoxi) {
//					totalValueIniBox += cage.value;
//					// rowCagesCellSum += cage.coordinates.size();
//					for (String coord : cage.coordinates) {
//						// String[] splitCoord = coord.split(",");
//						cellsCovered.add(coord);
//						// colsCovered.add(Integer.parseInt(splitCoord[1]));
//					}
//				}
//				// get the list of all the cells in ith box.
//				String[] rc = cagesInBoxi.get(0).coordinates.get(0).split(",");
//				List<String> cellsInBoxi = cellsInBox(Integer.parseInt(rc[0]), Integer.parseInt(rc[1]));
//				// look if any cell in this box has a value already filled in
//				int alreadyFilledValue = 0;
//				// int numOfAlreadyFilledCells = 0;
//				for (int cell = 0; cell < this.size; cell++) {
//					if (!cellsCovered.contains(cellsInBoxi.get(cell))) {
//						String[] split = cellsInBoxi.get(cell).split(",");
//						if (this.matrix[Integer.parseInt(split[0])][Integer.parseInt(split[1])] != UNASSIGNED) {
//							alreadyFilledValue = this.matrix[Integer.parseInt(split[0])][Integer.parseInt(split[1])];
//							cellsCovered.add(cellsInBoxi.get(cell));
//							// numOfAlreadyFilledCells++;
//						}
//					}
//				}
//				// total value in a row
//				totalValueIniBox += alreadyFilledValue;
//				// find the col which acts as an innie for ith row.
//				String cellLeft = "";
//				if (cellsCovered.size() == this.size - 1) {
//					for (int cell = 0; cell < this.size; cell++) {
//						if (!cellsCovered.contains(cellsInBoxi.get(cell))) {
//							cellLeft = cellsInBoxi.get(cell);
//							break;
//						}
//					}
//					String[] splitCell = cellLeft.split(",");
//					this.matrix[Integer.parseInt(splitCell[0])][Integer.parseInt(splitCell[0])] = this.maxTotal
//							- totalValueIniBox;
//				}
//			}
//
//		}
//
//	}
//
//	public void cageColSolver() {
//		for (int i = 0; i < this.size; i++) {
//			List<Cage> cagesInColi = new ArrayList<Cage>();
//			for (Cage cage : this.cages) {
//				if (cage.colNum == i) {
//					cagesInColi.add(cage);
//				}
//			}
//
//			if (cagesInColi.size() > 0) {
//				// calculate the number of cells covered with cages in row i
//				// int rowCagesCellSum = 0;
//				List<Integer> rowsCovered = new ArrayList<Integer>();
//				int totalValueIniCol = 0;
//
//				for (Cage cage : cagesInColi) {
//					totalValueIniCol += cage.value;
//					// rowCagesCellSum += cage.coordinates.size();
//					for (String coord : cage.coordinates) {
//						String[] splitCoord = coord.split(",");
//						rowsCovered.add(Integer.parseInt(splitCoord[0]));
//					}
//				}
//				// look if any cell in this row has a value already filled in
//				int alreadyFilledValue = 0;
//				// int numOfAlreadyFilledCells = 0;
//				for (int row = 0; row < this.size; row++) {
//					if (!rowsCovered.contains(row)) {
//						if (this.matrix[row][i] != UNASSIGNED) {
//							alreadyFilledValue = this.matrix[row][i];
//							rowsCovered.add(row);
//							// numOfAlreadyFilledCells++;
//						}
//					}
//				}
//				// total value in a row
//				totalValueIniCol += alreadyFilledValue;
//				// find the col which acts as an innie for ith row.
//				int rowLeft = -1;
//				if (rowsCovered.size() == this.size - 1) {
//					for (int r = 0; r < this.size; r++) {
//						if (!rowsCovered.contains(r)) {
//							rowLeft = r;
//							break;
//						}
//					}
//					this.matrix[rowLeft][i] = this.maxTotal - totalValueIniCol;
//				}
//			}
//
//		}
//
//	}

	public void setCagesInfo() {
		for (Map.Entry<List<String>, Integer> entry : this.cageCoordsWithValuesMap.entrySet()) {
			Cage cage = new Cage(entry.getValue(), entry.getKey(), this.size, this.acceptedNumbers,
					this.cagesPermutationsMap, this.cells);
			this.cages.add(cage);
			this.cagesLeft.add(cage);
		}
	}

//	public boolean recursiveSolve() {
//		for (int row = 0; row < this.size; row++) {
//			for (int col = 0; col < this.size; col++) {
//				if (this.matrix[row][col] == UNASSIGNED) {
//					for (int number = 0; number < this.size; number++) {
//						if (isValidCommon(row, col, this.acceptedNumbers.get(number))) {
//							matrix[row][col] = this.acceptedNumbers.get(number);
//							if (recursiveSolve()) {
//								return true;
//							} else {
//								this.matrix[row][col] = UNASSIGNED;
//							}
//						}
//					}
//					return false;
//				}
//			}
//		}
//		return true;
//	}
//
//	private boolean isInRow(int row, int number) {
//		for (int i = 0; i < this.size; i++) {
//			if (this.matrix[row][i] == number) {
//				return true;
//			}
//		}
//		return false;
//	}
//
//	private boolean isInCol(int col, int number) {
//		for (int i = 0; i < this.size; i++) {
//			if (this.matrix[i][col] == number)
//				return true;
//		}
//		return false;
//	}
//
//	private boolean isInBox(int row, int col, int number) {
//		int sqrt = (int) Math.sqrt(this.size);
//		int r = row - row % sqrt;
//		int c = col - col % sqrt;
//
//		for (int i = r; i < r + sqrt; i++) {
//			for (int j = c; j < c + sqrt; j++) {
//				if (this.matrix[i][j] == number) {
//					return true;
//				}
//			}
//		}
//		return false;
//	}

	private boolean checkCageRule(Cage cage) {
		int sum = 0;
		List<Integer> list = new ArrayList<Integer>();

		for (String coord : cage.coordinates) {
			String[] rc = coord.split(",");
			int r = Integer.parseInt(rc[0]);
			int c = Integer.parseInt(rc[1]);
			if (this.matrix[r][c] != UNASSIGNED) {
				sum += this.matrix[r][c];
				list.add(this.matrix[r][c]);
			}

		}
		if (sum > cage.value)
			return false;
		if (!isHashLengthSame(list))
			return false;
		return true;
	}

//	private boolean isGreaterThanCageValueRange(int row, int col, int number) {
//		if (!commonValidate())
//			return false;
//		String rowCol = "" + row + "," + col;
//		for (Map.Entry<List<String>, Integer> entry : this.cageCoordsWithValuesMap.entrySet()) {
//			if (entry.getKey().contains(rowCol)) {
//				int actualTotalValueOfTheCageCoord = 0;
//				List<Integer> cageCoordValues = new ArrayList<Integer>();
//
//				for (String coord : entry.getKey()) {
//					String[] rc = coord.split(",");
//					int r = Integer.parseInt(rc[0]);
//					int c = Integer.parseInt(rc[1]);
//					if (this.matrix[r][c] != UNASSIGNED) {
//						actualTotalValueOfTheCageCoord += this.matrix[r][c];
//						cageCoordValues.add(this.matrix[r][c]);
//					}
//
//				}
//				actualTotalValueOfTheCageCoord += number;
//				if (entry.getValue() < actualTotalValueOfTheCageCoord)
//					return true;
//				// checks the uniqueness of cage values.
//				if (!this.grid.isHashLengthSame(cageCoordValues))
//					return true;
//				break;
//			}
//		}
//		return false;
//	}
//
//	private boolean isValidCommon2(int row, int col, int number, Cage cage) {
//		return !isInRow(row, number) && !isInCol(col, number) && !isInBox(row, col, number) && checkCageRule(cage);
//	}
//
//	private boolean isValidCommon(int row, int col, int number) {
//		return !isInRow(row, number) && !isInCol(col, number) && !isInBox(row, col, number)
//				&& !isGreaterThanCageValueRange(row, col, number);
//	}

} // end of class KillerBackTrackingSolver()
