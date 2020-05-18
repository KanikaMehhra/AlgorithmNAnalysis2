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
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Class implementing the grid for standard Sudoku. Extends SudokuGrid (hence
 * implements all abstract methods in that abstract class). You will need to
 * complete the implementation for this for task A and subsequently use it to
 * complete the other classes. See the comments in SudokuGrid to understand what
 * each overriden method is aiming to do (and hence what you should aim for in
 * your implementation).
 */
public class StdSudokuGrid extends SudokuGrid {
	private int[][] sudokuGrid;
	private int sudokuGridLength;
	private List<Integer> listOfvalidIntegers;

	public StdSudokuGrid() {
		super();
		this.sudokuGrid = null;
		this.sudokuGridLength = 0;
		this.listOfvalidIntegers = new ArrayList<Integer>();
	} // end of StdSudokuGrid()

	/* ********************************************************* */

	@Override
	public void initGrid(String filename) throws FileNotFoundException, IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			String line;
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
		String result="";
		for (int[] row : this.sudokuGrid) {
			result+=Arrays.toString(row)+"\n";
		}
		return result;
	} // end of toString()

	@Override
	public boolean validate() {
		return commonValidate(this.sudokuGrid,this.listOfvalidIntegers,this.sudokuGridLength);
//		for (int[] row : this.sudokuGrid) {
//			List<Integer> rowList = Arrays.stream(row).boxed().collect(Collectors.toList());
//			rowList.removeAll(Collections.singleton(-1));
//			// checks first condition
//			if (!this.listOfvalidIntegers.containsAll(rowList)) {
//				return false;
//			}
//			// checks second condition
//			if (!isHashLengthSame(rowList)) {
//				return false;
//			}
//		}
//
//		for (int j = 0; j < this.sudokuGridLength; j++) {
//			List<Integer> smallGridArray = new ArrayList<Integer>();
//			List<Integer> colList = new ArrayList<Integer>();
//
//			for (int i = 0; i < this.sudokuGridLength; i++) {
//				colList.add(this.sudokuGrid[i][j]);		
//				int smallGridSize=(int)Math.sqrt(this.sudokuGridLength);
//				smallGridArray.add(i, this.sudokuGrid[(j / smallGridSize) * smallGridSize + i / smallGridSize][j * smallGridSize % this.sudokuGridLength + i % smallGridSize]);
//			}
//			colList.removeAll(Collections.singleton(-1));
//			// checks third condition
//			if (!isHashLengthSame(colList)) {
//				return false;
//			}
//			smallGridArray.removeAll(Collections.singleton(-1));
//			// check fourth condition
//			if (!isHashLengthSame(smallGridArray)) {
//				return false;
//			}
//
//		}
//		
//		return true;
	} // end of validate()
	
//	@Override
//	public boolean isHashLengthSame(List<Integer> list) {
//		Set<Integer> hash = new HashSet<Integer>(list);
//		return (hash.size() == list.size());
//	}

} // end of class StdSudokuGrid
