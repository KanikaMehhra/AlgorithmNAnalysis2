/**
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */
package grid;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class implementing the grid for standard Sudoku. Extends SudokuGrid (hence
 * implements all abstract methods in that abstract class). You will need to
 * complete the implementation for this for task A and subsequently use it to
 * complete the other classes. See the comments in SudokuGrid to understand what
 * each overriden method is aiming to do (and hence what you should aim for in
 * your implementation).
 */
public class StdSudokuGrid extends SudokuGrid {

	public StdSudokuGrid() {
		super();
	} // end of StdSudokuGrid()

	/* ********************************************************* */

	@Override
	public void initGrid(String filename) throws FileNotFoundException, IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			String line;
			readFirstTwoLines(br);
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
		if (validate())
			if (validate())
				try (PrintWriter writer = new PrintWriter(new File(filename))) {
					for (int i = 0; i < this.sudokuGridLength; i++) {
						StringBuilder sb = new StringBuilder();
						for (int j = 0; j < this.sudokuGridLength; j++) {
							sb.append(this.sudokuGrid[i][j]);
							if (j != this.sudokuGridLength - 1)
								sb.append(",");
						}
						if (i != this.sudokuGridLength - 1)
							sb.append("\n");
						writer.write(sb.toString());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
	} // end of outputBoard()

	@Override
	public String toString() {
		String result = "";
		for (int[] row : this.sudokuGrid) {
			result += Arrays.toString(row) + "\n";
		}
		return result;
	} // end of toString()

	@Override
	public boolean validate() {
		return commonValidate();
	} // end of validate()

} // end of class StdSudokuGrid
