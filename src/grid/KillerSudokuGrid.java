/**
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */
package grid;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class implementing the grid for Killer Sudoku. Extends SudokuGrid (hence
 * implements all abstract methods in that abstract class). You will need to
 * complete the implementation for this for task E and subsequently use it to
 * complete the other classes. See the comments in SudokuGrid to understand what
 * each overriden method is aiming to do (and hence what you should aim for in
 * your implementation).
 */
public class KillerSudokuGrid extends SudokuGrid {

	private int numOfCages;
	private Map<List<String>, Integer> cageCoordsWithValuesMap;

	public KillerSudokuGrid() {
		super();
		numOfCages = 0;
		cageCoordsWithValuesMap = new HashMap<List<String>, Integer>();
	} // end of KillerSudokuGrid()

	/* ********************************************************* */

	@Override
	public void initGrid(String filename) throws FileNotFoundException, IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			String line;
			readFirstTwoLines(br);

			String thirdLine = br.readLine();
			this.numOfCages = Integer.parseInt(thirdLine);

			while ((line = br.readLine()) != null) {
				List<String> cageValueCoord = Arrays.asList(line.split(" "));
				int cageValue = Integer.parseInt(cageValueCoord.get(0));
				List<String> cageCoord = cageValueCoord.subList(1, cageValueCoord.size());
				this.cageCoordsWithValuesMap.put(cageCoord, cageValue);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	} // end of initBoard()

	@Override
	public void outputGrid(String filename) throws FileNotFoundException, IOException {
		// TODO
		if (validate())
			try (PrintWriter writer = new PrintWriter(new File(filename))) {
				for (int i = 0; i < this.sudokuGridLength; i++) {
					StringBuilder sb = new StringBuilder();
					for (int j = 0; j < this.sudokuGridLength; j++) {
						sb.append(this.sudokuGrid[i][j]);
						if (j != this.sudokuGridLength - 1)
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
		String result = "";
		for (int[] row : this.sudokuGrid) {
			result += Arrays.toString(row) + "\n";
		}
		return result;
		// String result = "";
		// for (Map.Entry<List<String>, Integer> entry :
		// this.cageCoordsWithValuesMap.entrySet())
		// result += "Key = " + entry.getKey() + ", Value = " + entry.getValue() + "\n";
		// return result;
	} // end of toString()

	@Override
	public boolean validate() {
		// TODO
		if (!commonValidate())
			return false;
		for (Map.Entry<List<String>, Integer> entry : this.cageCoordsWithValuesMap.entrySet()) {
			int sum = 0;
			List<Integer> cageCoordValues = new ArrayList<Integer>();

			for (String coord : entry.getKey()) {
				String[] coordSplit = coord.split(",");
				int value = this.sudokuGrid[Integer.parseInt(coordSplit[0])][Integer.parseInt(coordSplit[1])];
				sum += value;
				cageCoordValues.add(value);
			}
			if (sum != entry.getValue())
				return false;
			if (!isHashLengthSame(cageCoordValues))
				return false;
		}
		return true;
	} // end of validate()

	public Map<List<String>, Integer> getCageCoordsWithValuesMap() {
		return this.cageCoordsWithValuesMap;
	}
} // end of class KillerSudokuGrid
