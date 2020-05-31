/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

package solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import grid.SudokuGrid;

/**
 * Dancing links solver for standard Sudoku.
 */
public class DancingLinksSolver extends StdSudokuSolver {
	// TODO: Add attributes as needed.
	private int[][] coverMatrix;
	private ColumnNode masterColumn;
	private DancingLinks links;
	private ColumnNode minColConstraint;
	protected List<DancingNode> answer;
	protected List<DancingNode> result;
	private List<Integer> colsCovered;
	private int size;
	private List<Integer> listOfAcceptedIntegers;

	public DancingLinksSolver() {
		// TODO: any initialization you want to implement.
		this.coverMatrix = null;
		this.masterColumn = null;
		this.links = null;
		this.minColConstraint = null;
		this.answer = new ArrayList<DancingNode>();
		this.result = new ArrayList<DancingNode>();
		this.colsCovered = new ArrayList<Integer>();
		this.listOfAcceptedIntegers = new ArrayList<Integer>();
		this.size = 0;

	} // end of DancingLinksSolver()

	@Override
	public boolean solve(SudokuGrid grid) {
		// TODO: your implementation of the dancing links solver for Killer Sudoku.
		this.size = grid.getSudokuGridLength();
		this.listOfAcceptedIntegers = grid.getListOfvalidIntegers();
		ExactCoverTransformation transform = new ExactCoverTransformation(grid);
		this.coverMatrix = transform.createCoverMatrix(grid.getSudokuGrid());
		this.links = new DancingLinks(this.coverMatrix);
		this.masterColumn = this.links.masterColumn;
		this.colsCovered = transform.colsCovered;
		if (recursiveSolve()) {
			fillGridWithSolution(grid.getSudokuGrid());
			return true;
		}
		// for (int[] row : this.coverMatrix) {
		// System.out.println(Arrays.toString(row));
		// }
		// System.out.println(recursiveSolve());
		// System.out.println(this.answer.size());
		// // if (recursiveSolve()) {
		// //// convertDLXListToGrid(grid.getSudokuGrid());
		// for (DancingNode node : this.answer) {
		// System.out.println(node.number);
		// }
		// // System.out.println(this.answer.size());
		// }
		return false;
		// return recursiveSolve();
	} // end of solve()

	private void fillGridWithSolution(int[][] grid) {
		for (DancingNode node : this.answer) {
			int majorRow = node.number;
			int rowNumber = majorRow / (this.size * this.size);
			int colNumber = (majorRow - (rowNumber * this.size * this.size)) / this.size;
			int valueNumber = -1;
			if (colNumber != 0) {
				valueNumber = (majorRow - (rowNumber * this.size * this.size)) % (colNumber * this.size);
			} else {
				valueNumber = majorRow - (rowNumber * this.size * this.size);
			}

			int value = this.listOfAcceptedIntegers.get(valueNumber);
			grid[rowNumber][colNumber] = value;
		}
	}

	// private void convertDLXListToGrid(int[][] originalGrid) {
	// // int[][] result = new int[this.size][this.size];
	//
	// for (DancingNode n : answer) {
	// DancingNode rcNode = n;
	// int min = rcNode.column.number;
	//
	// for (DancingNode tmp = n.right; tmp != n; tmp = tmp.right) {
	// int val = tmp.column.number;
	//
	// if (val < min) {
	// min = val;
	// rcNode = tmp;
	// }
	// }
	//
	// // we get line and column
	// int ans1 = rcNode.column.number;
	// int ans2 = rcNode.right.column.number;
	// int r = ans1 / this.size;
	// int c = ans1 % this.size;
	// // and the affected value
	// int num = (ans2 % this.size) + 1;
	// // we affect that on the result grid
	// originalGrid[r][c] = this.listOfAcceptedIntegers.get(num);
	// }
	//
	// // return result;
	// }

	private boolean selectColumnNodeHeuristic() {
		// this.minColConstraint = this.links.columnNodes.get(0);
		// int minColValue = minColConstraint.size;

		int minColValue = 0;
		this.minColConstraint = null;
		for (ColumnNode columnNode : this.links.columnNodes) {
			if (!this.colsCovered.contains(columnNode.number)) {
				minColValue = columnNode.size;
				this.minColConstraint = columnNode;
				break;
			}
		}

		for (ColumnNode columnNode : this.links.columnNodes) {
			if (!this.colsCovered.contains(columnNode.number))
				if (columnNode.size == 0)
					return false;
				else if (columnNode.size < minColValue) {
					minColValue = columnNode.size;
					this.minColConstraint = columnNode;
				}
		}

		return true;
	}

	private boolean recursiveSolve() {
		boolean result = true;

		if (this.masterColumn.right == this.masterColumn) {
			this.result = this.answer;
			return true;
		}

		if (this.colsCovered.size() == this.coverMatrix[0].length) {
			return true;
		}

		boolean foundColumnNode = selectColumnNodeHeuristic();
		if (!foundColumnNode)
			return false;

		this.minColConstraint.cover();
		this.colsCovered.add(this.minColConstraint.number);

		for (DancingNode dancingNode = this.minColConstraint.down; dancingNode != this.minColConstraint; dancingNode = dancingNode.down) {
			this.answer.add(dancingNode);

			for (DancingNode dancingNodeInternal = dancingNode.right; dancingNodeInternal != dancingNode; dancingNodeInternal = dancingNodeInternal.right) {
				dancingNodeInternal.column.cover();
				this.colsCovered.add(dancingNodeInternal.column.number);
			}

			if (recursiveSolve()) {
				result = true;
				break;
			} else {
				result = false;
				dancingNode = this.answer.remove(this.answer.size() - 1);
				this.minColConstraint = dancingNode.column;
				for (DancingNode dancingNodeRecover = dancingNode.left; dancingNodeRecover != dancingNode; dancingNodeRecover = dancingNodeRecover.left) {
					dancingNodeRecover.column.uncover();
					this.colsCovered.remove(dancingNodeRecover.column.number);
				}
			}
		}
		this.minColConstraint.uncover();
		this.colsCovered.remove(this.minColConstraint.number);
		return result;
	}

	// private void process(int k) {
	// if (header.right == header) {
	// // End of Algorithm X
	// // Result is copied in a result list
	// result = new LinkedList<>(answer);
	// } else {
	// // we choose column c
	// ColumnNode c = selectColumnNodeHeuristic();
	// c.cover();
	//
	// for (DancingNode r = c.bottom; r != c; r = r.bottom) {
	// // We add r line to partial solution
	// answer.add(r);
	//
	// // We cover columns
	// for (DancingNode j = r.right; j != r; j = j.right) {
	// j.column.cover();
	// }
	//
	// // recursive call to leverl k + 1
	// process(k + 1);
	//
	// // We go back
	// r = answer.remove(answer.size() - 1);
	// c = r.column;
	//
	// // We uncover columns
	// for (DancingNode j = r.left; j != r; j = j.left) {
	// j.column.uncover();
	// }
	// }
	//
	// c.uncover();
	// }
	// }

} // end of class DancingLinksSolver
