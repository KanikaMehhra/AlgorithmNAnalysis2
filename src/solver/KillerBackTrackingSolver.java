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

	public KillerBackTrackingSolver() {
		super();
	} // end of KillerBackTrackingSolver()

	public boolean recursiveSolve(int cellIndex) {
		for (int i = cellIndex; i < this.cells.size(); i++) {
			Cell cell = this.cells.get(i);
			if (this.matrix[cell.row][cell.col] == UNASSIGNED) {
				for (int permittedValue : cell.permittedIntegers) {
					Cage cage = this.cages.get(cell.cageId);
					for (List<Integer> permutation : cage.mapOfPermutationsStartingWithASpecificDigit
							.get(permittedValue)) {
						fillCageCoords(cage, permutation);
						if (commonValidate()) {
							if (recursiveSolve(i + 1)) {
								return true;
							} else {
								unFillCageCoords(cage);
							}
						} else {
							unFillCageCoords(cage);
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
} // end of class KillerBackTrackingSolver()
