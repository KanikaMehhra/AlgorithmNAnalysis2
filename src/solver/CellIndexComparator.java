package solver;

import java.util.Comparator;

public class CellIndexComparator implements Comparator<Cell> {

	@Override
	public int compare(Cell cell1, Cell cell2) {
		return cell1.index - cell2.index;
	}
}