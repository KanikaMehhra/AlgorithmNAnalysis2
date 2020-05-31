package solver;

import java.util.ArrayList;
import java.util.List;

public class DancingLinks {

	protected ColumnNode masterColumn;
	protected int numberOfColumnNodes;
	protected List<ColumnNode> columnNodes;

	public DancingLinks(int[][] exactCover) {
		this.numberOfColumnNodes = exactCover[0].length;
		this.columnNodes = new ArrayList<ColumnNode>();
		createDancingLinks(exactCover);
	}

	// creates the dancing links and sets the master column.
	private void createDancingLinks(int[][] coverMatrix) {
		this.masterColumn = new ColumnNode(-1);
		ColumnNode prevColumnNode = this.masterColumn;
		ColumnNode curColumn = null;
		for (int j = 0; j < this.numberOfColumnNodes; j++) {
			curColumn = new ColumnNode(j);
			curColumn.left = prevColumnNode;
			prevColumnNode.right = curColumn;
			this.columnNodes.add(curColumn);
			prevColumnNode = curColumn;
		}

		curColumn.right = this.masterColumn; // making the list circular i.e. the right-most ColumnHead is linked to the
												// // root
		this.masterColumn.left = curColumn;

		int rowCount = -1;
		for (int[] row : coverMatrix) {
			rowCount++;
			DancingNode prevDancingNode = null;
			for (int j = 0; j < this.numberOfColumnNodes; j++) {
				if (row[j] == 1) {
					ColumnNode columnNode = this.columnNodes.get(j);
					DancingNode newDancingNode = new DancingNode(columnNode, rowCount);

					if (prevDancingNode == null)
						prevDancingNode = newDancingNode;

					columnNode.up.linkVertically(newDancingNode);
					prevDancingNode = prevDancingNode.linkHorizontally(newDancingNode);
					columnNode.size++;
				}
			}
		}
	}
}