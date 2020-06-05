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
import grid.KillerSudokuGrid;
import grid.SudokuGrid;

/**
 * Backtracking solver for Killer Sudoku.
 */
public class KillerBackTrackingSolver extends KillerSudokuSolver {
	private static final int UNASSIGNED = -1;
	private int[][] matrix;
	private int size;
	private List<Integer> acceptedNumbers;
	private Map<List<String>, Integer> cageCoordsWithValuesMap;
	private List<Cage> cages;
	private List<Cell> cells;

	public KillerBackTrackingSolver() {
		this.matrix = null;
		this.size = 0;
		this.acceptedNumbers = new ArrayList<Integer>();
		this.cageCoordsWithValuesMap = new HashMap<List<String>, Integer>();
		this.cages = new ArrayList<Cage>();
		this.cells = new ArrayList<Cell>();
	} // end of KillerBackTrackingSolver()

	@Override
	public boolean solve(SudokuGrid grid) {
		this.size = grid.getSudokuGridLength();
		this.acceptedNumbers = grid.getListOfvalidIntegers();
		this.matrix = grid.getSudokuGrid();
		this.cageCoordsWithValuesMap = ((KillerSudokuGrid) grid).getCageCoordsWithValuesMap();
		setCagesInfo();
		Collections.sort(this.cells, new CellIndexComparator());
		return recursiveSolve(0);
	} // end of solve()

	public boolean recursiveSolve(int cellIndex) {
		for (int i = cellIndex; i < this.cells.size(); i++) {
			Cell cell = this.cells.get(i);
			if (this.matrix[cell.row][cell.col] == UNASSIGNED) {
				for (int permittedValue : cell.permittedIntegers) {
					for (List<Integer> permutation : this.cages
							.get(cell.cageId).mapOfPermutationsStartingWithASpecificDigit.get(permittedValue)) {
						fillCageCoords(this.cages.get(cell.cageId), permutation);
						if (commonValidate()) {
							if (recursiveSolve(i + 1)) {
								return true;
							} else {
								unFillCageCoords(this.cages.get(cell.cageId));
							}
						} else {
							unFillCageCoords(this.cages.get(cell.cageId));
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

	public void setCagesInfo() {
		for (Map.Entry<List<String>, Integer> entry : this.cageCoordsWithValuesMap.entrySet()) {
			Cage cage = new Cage(entry.getValue(), entry.getKey(), this.size, this.acceptedNumbers, this.cells);
			this.cages.add(cage);
		}
	}
} // end of class KillerBackTrackingSolver()
