/**
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */
package grid;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Class implementing the grid for standard Sudoku. Extends SudokuGrid (hence
 * implements all abstract methods in that abstract class). You will need to
 * complete the implementation for this for task A and subsequently use it to
 * complete the other classes. See the comments in SudokuGrid to understand what
 * each overriden method is aiming to do (and hence what you should aim for in
 * your implementation).
 */
public class StdSudokuGrid extends SudokuGrid {
	// TODO: Add your own attributes
	private int[][] sudokuGrid;
	private int sudokuGridLength;

	public StdSudokuGrid() {
		super();
		this.sudokuGrid = null;
		this.sudokuGridLength = 0;
		// TODO: any necessary initialisation at the constructor
	} // end of StdSudokuGrid()

	/* ********************************************************* */

	@Override
	public void initGrid(String filename) throws FileNotFoundException, IOException {
		// TODO
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			String line;
			String firstLine = br.readLine();
			this.sudokuGridLength = Integer.parseInt(firstLine);
			this.sudokuGrid = new int[this.sudokuGridLength][this.sudokuGridLength];

			String secondLine = br.readLine();
			String[] splitSecondLine = secondLine.split(" ");
			List<Integer> listOfvalidIntegers = new ArrayList<Integer>();
			for (String num : splitSecondLine) {
				listOfvalidIntegers.add(Integer.parseInt(num));
			}

			while ((line = br.readLine()) != null) {
				String[] coordValue = line.split(" ");
				String[] coord = coordValue[0].split(",");
				this.sudokuGrid[Integer.parseInt(coord[0])][Integer.parseInt(coord[1])] = Integer
						.parseInt(coordValue[1]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	} // end of initBoard()

	@Override
	public void outputGrid(String filename) throws FileNotFoundException, IOException {
		// TODO
		try (PrintWriter writer = new PrintWriter(new File(filename))) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < this.sudokuGridLength; i++) {
				for (int j = 0; j < this.sudokuGridLength; j++) {
					sb.append(this.sudokuGrid[i][j]);
					sb.append(",");
				}
				sb.append("\n");
				writer.write(sb.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	} // end of outputBoard()

	@Override
	public String toString() {
		// TODO
		return Arrays.deepToString(this.sudokuGrid);
	} // end of toString()

	@Override
	public boolean validate() {
		// TODO

		// placeholder
		return false;
	} // end of validate()

} // end of class StdSudokuGrid
