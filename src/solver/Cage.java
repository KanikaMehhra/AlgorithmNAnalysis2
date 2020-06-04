package solver;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Cage {
	protected int value;
	protected List<String> coordinates;
	protected int colNum;
	protected int rowNum;
	protected int boxNum;
	private int size;
	private int smallGridSize;
	private static int count=-1;
	protected int id;

	public Cage(int value, List<String> coordinates, int size) {
		this.value = value;
		this.coordinates = coordinates;
		this.size = size;
		this.smallGridSize = (int) Math.sqrt(this.size);
		calculateRowNum();
		calculateColNum();
		calculateBoxNum();
		this.id=++this.count;
	}

	private void calculateColNum() {
		String[] rc1 = this.coordinates.get(0).split(",");
		int previousCol = Integer.parseInt(rc1[1]);
		this.colNum = previousCol;

		for (int i = 1; i < this.coordinates.size(); i++) {
			String[] rc = this.coordinates.get(i).split(",");
			int c = Integer.parseInt(rc[1]);
			if (c != previousCol) {
				this.colNum = -1;
				break;
			}
		}

	}

	private void calculateRowNum() {
		String[] rc1 = this.coordinates.get(0).split(",");
		int previousRow = Integer.parseInt(rc1[0]);
		this.rowNum = previousRow;

		for (int i = 1; i < this.coordinates.size(); i++) {
			String[] rc = this.coordinates.get(i).split(",");
			int r = Integer.parseInt(rc[0]);
			if (r != previousRow) {
				this.rowNum = -1;
				break;
			}
		}

	}

	private void calculateBoxNum() {
		String[] rc1 = this.coordinates.get(0).split(",");
		int previousBox = getBoxNum(Integer.parseInt(rc1[0]), Integer.parseInt(rc1[1]));
		this.boxNum = previousBox;

		for (int i = 1; i < this.coordinates.size(); i++) {
			String[] rc = this.coordinates.get(i).split(",");
			int b = getBoxNum(Integer.parseInt(rc[0]), Integer.parseInt(rc[1]));
			// int r = Integer.parseInt(rc[0]);
			if (b != previousBox) {
				this.boxNum = -1;
				break;
			}
		}
	}

	public int getBoxNum(int row, int col) {
		int index = -1;
		boolean flag = false;
		// calculate the index of the cell which corresponds to the calculated row and
		// column in the actual sudoku grid.
		for (int indexI = 0; indexI < this.size; indexI++) {
			for (int indexJ = 0; indexJ < this.size; indexJ++) {
				++index;
				if (indexI == row && indexJ == col) {
					flag = true;
					break;
				}
			}
			if (flag)
				break;
		}

		// calculation for box number starting from 0;
		int boxwidth = this.smallGridSize; /* Width of a small box */
		int boxheight = this.smallGridSize; /* Height of a small box */
		int hboxes = this.smallGridSize;/* Boxes horizontally */
		// int vboxes = this.smallGridSize; /* Boxes vertically */
		int width = boxwidth * hboxes;
		// int height = boxheight * vboxes;
		int y = index / width;
		int x = index % width;
		int boxy = y / boxheight;
		// int innery = y % boxheight;
		int boxx = x / boxwidth;
		// int innerx = x % boxwidth;
		int box = boxx + hboxes * boxy;

		return box;
	}

	public boolean isHashLengthSame(List<Integer> list) {
		Set<Integer> hash = new HashSet<Integer>(list);
		return (hash.size() == list.size());// true if satisfies the condition
	}

}
