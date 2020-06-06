package solver;

import java.util.ArrayList;
import java.util.List;

public class Cell {
	protected int cageId;
	protected int row;
	protected int col;
	protected List<Integer> permittedIntegers = new ArrayList<Integer>();
	protected int index;

	public Cell(int row, int col, int size, List<List<Integer>> combinationsList, int cageId,
			List<Integer> permittedIntegers) {
		this.index = calculateIndex(row, col, size);
		this.permittedIntegers = permittedIntegers;
		this.row = row;
		this.col = col;
		this.cageId = cageId;
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
