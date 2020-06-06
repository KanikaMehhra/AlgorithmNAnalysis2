/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

package solver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import grid.KillerSudokuGrid;
import grid.SudokuGrid;

/**
 * Abstract class for common attributes or methods for solvers of Killer Sudoku.
 * Note it is not necessary to use this, but provided in case you wanted to do
 * so and then no need to change the hierarchy of solver types.
 */
public abstract class KillerSudokuSolver extends SudokuSolver {
	private static final int START_INDEX = 0;
	protected static final int UNASSIGNED = -1;
	protected int[][] matrix;
	protected int size;
	protected List<Integer> acceptedNumbers;
	protected Map<List<String>, Integer> cageCoordsWithValuesMap;
	protected List<Cage> cages;
	protected List<Cell> cells;
	
	public KillerSudokuSolver() {
		this.matrix = null;
		this.size = 0;
		this.acceptedNumbers = new ArrayList<Integer>();
		this.cageCoordsWithValuesMap = new HashMap<List<String>, Integer>();
		this.cages = new ArrayList<Cage>();
		this.cells = new ArrayList<Cell>();
	}
	
	@Override
	public boolean solve(SudokuGrid grid) {
		this.size = grid.getSudokuGridLength();
		this.acceptedNumbers = grid.getListOfvalidIntegers();
		this.matrix = grid.getSudokuGrid();
		this.cageCoordsWithValuesMap = ((KillerSudokuGrid) grid).getCageCoordsWithValuesMap();
		setCagesInfo();
		Collections.sort(this.cells, new CellIndexComparator());
		return recursiveSolve(START_INDEX);
	} // end of solve()
	
	protected void fillCageCoords(Cage cage, List<Integer> permutation) {
		for (int i = 0; i < permutation.size(); i++) {
			String[] rc = cage.coordinates.get(i).split(",");
			int r = Integer.parseInt(rc[0]);
			int c = Integer.parseInt(rc[1]);
			this.matrix[r][c] = permutation.get(i);
		}
	}

	protected void unFillCageCoords(Cage cage) {
		for (String cageCoord : cage.coordinates) {
			String[] rc = cageCoord.split(",");
			int r = Integer.parseInt(rc[0]);
			int c = Integer.parseInt(rc[1]);
			this.matrix[r][c] = UNASSIGNED;
		}
	}

	protected void setCagesInfo() {
		for (Map.Entry<List<String>, Integer> entry : this.cageCoordsWithValuesMap.entrySet()) {
			Cage cage = new Cage(entry.getValue(), entry.getKey(), this.size, this.acceptedNumbers, this.cells);
			this.cages.add(cage);
		}
	}
	
	public abstract boolean recursiveSolve(int cellIndex);

} // end of class KillerSudokuSolver
