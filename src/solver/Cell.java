package solver;

import java.util.List;

public class Cell {
	protected List<Integer> permittedIntegers;
	protected int index;

	public Cell(int row, int col, int size) {
		this.index = calculateIndex(row, col, size);

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
