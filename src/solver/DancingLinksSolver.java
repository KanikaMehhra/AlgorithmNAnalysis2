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
		return false;
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

	private boolean selectMinColumnNodeConstraint() {
		if (this.colsCovered.size() == this.coverMatrix[0].length) {
			this.masterColumn.right = this.masterColumn;
			return true;
		}

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
		// finds the column node with least number of dancing nodes linked to it.
		boolean foundColumnNode = selectMinColumnNodeConstraint();
		if (!foundColumnNode)
			return false;
		// covers the column node found.
		this.minColConstraint.cover();
		// add the column number to the list of columns covered.
		this.colsCovered.add(this.minColConstraint.number);

		for (DancingNode dancingNode = this.minColConstraint.down; dancingNode != this.minColConstraint; dancingNode = dancingNode.down) {
			// add the row which satisfies the column node.
			this.answer.add(dancingNode);
			// cover all the dancing nodes in the row selected as partial answer.
			for (DancingNode dancingNodeInternal = dancingNode.right; dancingNodeInternal != dancingNode; dancingNodeInternal = dancingNodeInternal.right) {
				dancingNodeInternal.column.cover();
				this.colsCovered.add(dancingNodeInternal.column.number);
			}

			if (recursiveSolve()) {
				result = true;
				break;
			} else {// backtrack for dancing node
				result = false;
				dancingNode = this.answer.remove(this.answer.size() - 1);
				this.minColConstraint = dancingNode.column;
				for (DancingNode dancingNodeRecover = dancingNode.left; dancingNodeRecover != dancingNode; dancingNodeRecover = dancingNodeRecover.left) {
					dancingNodeRecover.column.uncover();
					this.colsCovered.remove(dancingNodeRecover.column.number);
				}
			}
		} // backtrack for column node
		this.minColConstraint.uncover();
		this.colsCovered.remove(this.minColConstraint.number);
		return result;
	}
} // end of class DancingLinksSolver
