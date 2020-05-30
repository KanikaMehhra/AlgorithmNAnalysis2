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
//		createDoubleLinkedLists(exactCover);
		// System.out.println(this.masterColumn);
	}

	private void createDoubleLinkedLists(int[][] matrix) {
		this.masterColumn = new ColumnNode(-1); // the root is used as an entry-way to the linked list i.e. we access the list
									// through the root
		// create the column heads
		ColumnNode curColumn = this.masterColumn;
		for (int col = 0; col < matrix[0].length; col++) // getting the column heads from the sparse matrix and filling
															// in the information about the
		// constraints. We iterate for all the column heads, thus going through all the
		// items in the first row of the sparse matrix
		{
			// We create the ColumnID that will store the information. We will later map
			// this ID to the current curColumn
//			ColumnID id = new ColumnID();
//			if (col < 3 * N * N) {
//				// identifying the digit
//				int digit = (col / (3 * N)) + 1;
//				id.number = digit;
//				// is it for a row, column or block?
//				int index = col - (digit - 1) * 3 * N;
//				if (index < N) {
//					id.constraint = 0; // we're in the row constraint
//					id.position = index;
//				} else if (index < 2 * N) {
//					id.constraint = 1; // we're in the column constraint
//					id.position = index - N;
//				} else {
//					id.constraint = 2; // we're in the block constraint
//					id.position = index - 2 * N;
//				}
//			} else {
//				id.constraint = 3; // we're in the cell constraint
//				id.position = col - 3 * N * N;
//			}
			curColumn.right = new ColumnNode(col);
			curColumn.right.left = curColumn;
			curColumn = (ColumnNode) curColumn.right;
//			curColumn.info = id; // the information about the column is set to the new column
			curColumn.column= curColumn;
		}
		curColumn.right = this.masterColumn; // making the list circular i.e. the right-most ColumnHead is linked to the root
		this.masterColumn.left = curColumn;

		// Once all the ColumnHeads are set, we iterate over the entire matrix
		// Iterate over all the rows
		for (int row = 0; row < matrix.length; row++) {
			// iterator over all the columns
			curColumn = (ColumnNode) this.masterColumn.right;
			DancingNode lastCreatedElement = null;
			DancingNode firstElement = null;
			for (int col = 0; col < matrix[row].length; col++) {
				if (matrix[row][col] == 1) // i.e. if the sparse matrix element has a 1 i.e. there is a clue here i.e.
											// we were given this value in the Grid
				{
					// create a new data element and link it
					DancingNode colElement = curColumn;
					while (colElement.down != null) {
						colElement = colElement.down;
					}
					colElement.down = new DancingNode();
					if (firstElement == null) {
						firstElement = colElement.down;
					}
					colElement.down.up = colElement;
					colElement.down.left = lastCreatedElement;
					colElement.down.column = curColumn;
					if (lastCreatedElement != null) {
						colElement.down.left.right = colElement.down;
					}
					lastCreatedElement = colElement.down;
					curColumn.size++;
				}
				curColumn = (ColumnNode) curColumn.right;
			}
			// link the first and the last element, again making it circular
			if (lastCreatedElement != null) {
				lastCreatedElement.right = firstElement;
				firstElement.left = lastCreatedElement;
			}
		}
		curColumn = (ColumnNode) this.masterColumn.right;
		// link the last column elements with the corresponding columnHeads
		for (int i = 0; i < matrix[0].length; i++) {
			DancingNode colElement = curColumn;
			while (colElement.down != null) {
				colElement = colElement.down;
			}
			colElement.down = curColumn;
			curColumn.up = colElement;
			curColumn = (ColumnNode) curColumn.right;
		}
//		return this.masterColumn; // We've made the doubly-linked list; we return the root of the list
	}

	// creates the dancing links and sets the master column.
	private void createDancingLinks(int[][] coverMatrix) {
//		ColumnNode prevColumnNode = new ColumnNode(-1);
//		this.columnNodes.add(prevColumnNode);
		// this.masterColumn = prevColumnNode;
		this.masterColumn = new ColumnNode(-1);
		ColumnNode prevColumnNode = this.masterColumn;
		ColumnNode curColumn=null;
		for (int j = 0; j < this.numberOfColumnNodes ; j++) {
			curColumn = new ColumnNode(j);
			curColumn.left = prevColumnNode;
			prevColumnNode.right=curColumn;
//			curColumn.right = new ColumnNode(j + 1);
			this.columnNodes.add(curColumn);
//			this.masterColumn = (ColumnNode) this.masterColumn.linkHorizontally(columnNode);
			prevColumnNode = curColumn;
		}

//		this.masterColumn = this.masterColumn.right.column;
		curColumn.right = this.masterColumn; // making the list circular i.e. the right-most ColumnHead is linked to the root
		this.masterColumn.left = curColumn;

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