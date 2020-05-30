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
//		System.out.println(this.masterColumn);
	}
	
	//creates the dancing links and sets the master column.
	private void createDancingLinks(int[][] coverMatrix) {
		ColumnNode prevColumnNode = new ColumnNode(0);
		this.columnNodes.add(prevColumnNode);
//		this.masterColumn = prevColumnNode;
		this.masterColumn = new ColumnNode(-1);

		for (int j = 1; j < this.numberOfColumnNodes - 1; j++) {
			ColumnNode columnNode = new ColumnNode(j);
			columnNode.left = prevColumnNode;
			columnNode.right = new ColumnNode(j + 1);
			this.columnNodes.add(columnNode);
			this.masterColumn = (ColumnNode) this.masterColumn.linkHorizontally(columnNode);
			prevColumnNode = columnNode;
		}
		
		this.masterColumn = this.masterColumn.right.column;
		
		for (int[] row : coverMatrix) {
			DancingNode prevDancingNode = null;
			for (int j = 0; j < this.numberOfColumnNodes; j++) {
				if (row[j] == 1) {
					ColumnNode columnNode = this.columnNodes.get(j);
					DancingNode newDancingNode = new DancingNode(columnNode);

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