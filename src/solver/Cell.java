package solver;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Cell {
	protected int cageId;
	protected int row;
	protected int col;
	protected List<Integer> permittedIntegers = new ArrayList<Integer>();
	protected int index;

	public Cell(int row, int col, int size, List<List<Integer>> combinationsList, int cageId) {
		this.index = calculateIndex(row, col, size);
		setPermittedIntegers(combinationsList);
		this.row = row;
		this.col = col;
		this.cageId = cageId;
	}

	private void setPermittedIntegers(List<List<Integer>> combinationsList) {
		Set<Integer> combinationSetSet = new LinkedHashSet<>(this.permittedIntegers);
		for (List<Integer> combinationList : combinationsList) {
			combinationSetSet.addAll(combinationList);
		}
		this.permittedIntegers = new ArrayList<>(combinationSetSet);
	}

	private int calculateIndex(int row, int col, int size) {
		int index = -1;
		boolean flag = false;
		// calculate the index of the cell which corresponds to the calculated row and
		// column in the actual sudoku grid.
		for (int indexI = 0; indexI < size; indexI++) {
			for (int indexJ = 0; indexJ < size; indexJ++) {
				++index;
				if (indexI == row && indexJ == col) {
					flag = true;
					break;
				}
			}
			if (flag)
				break;
		}
		return index;
	}

}
