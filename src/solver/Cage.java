package solver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Cage {
	protected int value;
	protected List<String> coordinates;
	private int size;
	private static int count = -1;
	protected int id;
	private List<Integer> acceptedNumbers;
	protected List<List<Integer>> combinations = new ArrayList<List<Integer>>();
	protected List<List<Integer>> permutations = new ArrayList<List<Integer>>();
	protected Map<Integer, List<List<Integer>>> mapOfPermutationsStartingWithASpecificDigit = new HashMap<Integer, List<List<Integer>>>();

	public Cage(int value, List<String> coordinates, int size, List<Integer> acceptedNumbers, List<Cell> cells) {
		this.value = value;
		this.coordinates = coordinates;
		this.size = size;
		this.id = ++this.count;
		this.acceptedNumbers = acceptedNumbers;
		this.combinations = getCombinations(this.coordinates.size(), this.value);
		permuteAllCombinations();
		createCells(cells, setPermittedIntegers());
	}

	private List<Integer> setPermittedIntegers() {
		List<Integer> permittedIntegers = new ArrayList<Integer>();
		Set<Integer> combinationSetSet = new LinkedHashSet<>(permittedIntegers);
		for (List<Integer> combinationList : this.combinations) {
			combinationSetSet.addAll(combinationList);
		}
		permittedIntegers = new ArrayList<>(combinationSetSet);
		return permittedIntegers;
	}

	private void createCells(List<Cell> cells, List<Integer> permittedIntegers) {
		for (String cageCoord : this.coordinates) {
			String[] rc = cageCoord.split(",");
			int r = Integer.parseInt(rc[0]);
			int c = Integer.parseInt(rc[1]);
			Cell cell = new Cell(r, c, this.size, this.combinations, this.id, permittedIntegers);
			cells.add(cell);
		}
	}

	private void permuteAllCombinations() {
		for (List<Integer> combination : this.combinations) {
			List<List<Integer>> permute = permute(combination);
			for (List<Integer> permutation : permute) {
				this.permutations.add(permutation);
				List<List<Integer>> temList = new ArrayList<List<Integer>>();
				if (this.mapOfPermutationsStartingWithASpecificDigit.get(permutation.get(0)) != null) {
					temList = this.mapOfPermutationsStartingWithASpecificDigit.get(permutation.get(0));
				}
				temList.add(permutation);
				this.mapOfPermutationsStartingWithASpecificDigit.put(permutation.get(0), temList);
			}

		}
	}

	public List<List<Integer>> permute(List<Integer> combination) {
		List<List<Integer>> permutations = new ArrayList<>();
		permutationHelper(0, combination, permutations);
		return permutations;
	}

	private void permutationHelper(int start, List<Integer> combination, List<List<Integer>> permutations) {
		if (start == combination.size() - 1) {
			ArrayList<Integer> list = new ArrayList<>();
			for (int num : combination) {
				list.add(num);
			}
			permutations.add(list);
			return;
		}

		for (int i = start; i < combination.size(); i++) {
			performSwap(combination, i, start);
			permutationHelper(start + 1, combination, permutations);
			performSwap(combination, i, start);
		}
	}

	private void performSwap(List<Integer> combination, int i, int j) {
		int temp = combination.get(i);
		combination.set(i, combination.get(j));
		combination.set(j, temp);
	}

	public List<List<Integer>> getCombinations(int k, int n) {
		List<List<Integer>> combinations = new ArrayList<List<Integer>>();
		List<Integer> currCombination = new ArrayList<Integer>();
		combinationHelper(combinations, currCombination, k, 0, n);
		return combinations;
	}

	public void combinationHelper(List<List<Integer>> combinations, List<Integer> currCombination, int k, int start,
			int sum) {
		if (sum < 0) {
			return;
		}
		if (sum == 0 && currCombination.size() == k) {
			combinations.add(new ArrayList<Integer>(currCombination));
			return;
		}
		for (int i = start; i < this.size; i++) {
			currCombination.add(this.acceptedNumbers.get(i));
			combinationHelper(combinations, currCombination, k, i + 1, sum - this.acceptedNumbers.get(i));
			currCombination.remove(currCombination.size() - 1);
		}
	}
}
