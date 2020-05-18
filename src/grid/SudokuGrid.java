/**
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

package grid;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Abstract class representing the general interface for a Sudoku grid. Both
 * standard and Killer Sudoku extend from this abstract class.
 */
public abstract class SudokuGrid {
	protected int[][] sudokuGrid;
	protected int sudokuGridLength;
	protected List<Integer> listOfvalidIntegers;

	public SudokuGrid() {
		super();
		this.sudokuGrid = null;
		this.sudokuGridLength = 0;
		this.listOfvalidIntegers = new ArrayList<Integer>();

		// TODO: any necessary initialisation at the constructor
	}

	public int[][] getSudokuGrid() {
		return sudokuGrid;
	}

	public int getSudokuGridLength() {
		return sudokuGridLength;
	}

	public List<Integer> getListOfvalidIntegers() {
		return listOfvalidIntegers;
	}

	public void readFirstTwoLines(BufferedReader br) {
		try {
			String firstLine = br.readLine();

			this.sudokuGridLength = Integer.parseInt(firstLine);
			this.sudokuGrid = new int[this.sudokuGridLength][this.sudokuGridLength];

			for (int[] row : this.sudokuGrid)
				Arrays.fill(row, -1);

			String secondLine = br.readLine();
			String[] splitSecondLine = secondLine.split(" ");
			for (String num : splitSecondLine) {
				this.listOfvalidIntegers.add(Integer.parseInt(num));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Load the specified file and construct an initial grid from the contents of
	 * the file. See assignment specifications and sampleGames to see more details
	 * about the format of the input files.
	 *
	 * @param filename
	 *            Filename of the file containing the intial configuration of the
	 *            grid we will solve.
	 *
	 * @throws FileNotFoundException
	 *             If filename is not found.
	 * @throws IOException
	 *             If there are some IO exceptions when openning or closing the
	 *             files.
	 */
	public abstract void initGrid(String filename) throws FileNotFoundException, IOException;

	/**
	 * Write out the current values in the grid to file. This must be implemented in
	 * order for your assignment to be evaluated by our testing.
	 *
	 * @param filename
	 *            Name of file to write output to.
	 *
	 * @throws FileNotFoundException
	 *             If filename is not found.
	 * @throws IOException
	 *             If there are some IO exceptions when openning or closing the
	 *             files.
	 */
	public abstract void outputGrid(String filename) throws FileNotFoundException, IOException;

	/**
	 * Converts grid to a String representation. Useful for displaying to output
	 * streams.
	 *
	 * @return String representation of the grid.
	 */
	public abstract String toString();

	/**
	 * Checks and validates whether the current grid satisfies the constraints of
	 * the game in question (either standard or Killer Sudoku). Override to
	 * implement game specific checking.
	 *
	 * @return True if grid satisfies all constraints of the game in question.
	 */
	public abstract boolean validate();

	/**
	 * Checks and validates whether the current list satisfies the constraint of
	 * unique values of the game in question (either standard or Killer Sudoku).
	 * Override to implement game specific checking.
	 *
	 * @return True if list satisfies the unique value constraint of the game in
	 *         question.
	 */

	public boolean isHashLengthSame(List<Integer> list) {
		Set<Integer> hash = new HashSet<Integer>(list);
		return (hash.size() == list.size());
	}

	public boolean commonValidate() {
		for (int[] row : sudokuGrid) {
			List<Integer> rowList = Arrays.stream(row).boxed().collect(Collectors.toList());
			rowList.removeAll(Collections.singleton(-1));
			// checks first condition
			if (!listOfvalidIntegers.containsAll(rowList)) {
				return false;
			}
			// checks second condition
			if (!isHashLengthSame(rowList)) {
				return false;
			}
		}

		for (int j = 0; j < sudokuGridLength; j++) {
			List<Integer> smallGridArray = new ArrayList<Integer>();
			List<Integer> colList = new ArrayList<Integer>();

			for (int i = 0; i < sudokuGridLength; i++) {
				colList.add(sudokuGrid[i][j]);
				int smallGridSize = (int) Math.sqrt(sudokuGridLength);
				smallGridArray.add(i, sudokuGrid[(j / smallGridSize) * smallGridSize + i / smallGridSize][j
						* smallGridSize % sudokuGridLength + i % smallGridSize]);
			}
			colList.removeAll(Collections.singleton(-1));
			// checks third condition
			if (!isHashLengthSame(colList)) {
				return false;
			}
			smallGridArray.removeAll(Collections.singleton(-1));
			// check fourth condition
			if (!isHashLengthSame(smallGridArray)) {
				return false;
			}

		}

		return true;
	} // end of validate()
} // end of abstract class SudokuGrid
