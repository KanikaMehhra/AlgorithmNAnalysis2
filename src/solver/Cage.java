package solver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	public Cage(int value, List<String> coordinates, int size, List<Integer> acceptedNumbers,
			Map<Cage, List<List<Integer>>> cagesPermutationsMap, List<Cell> cells) {
		this.value = value;
		this.coordinates = coordinates;
		this.size = size;
		this.id = ++this.count;
		this.acceptedNumbers = acceptedNumbers;
		this.combinations = combinationSum3(this.coordinates.size(), this.value);
		permuteAllCombinations();
		createCells(cells);
	}

	private void createCells(List<Cell> cells) {
		for (String cageCoord : this.coordinates) {
			String[] rc = cageCoord.split(",");
			int r = Integer.parseInt(rc[0]);
			int c = Integer.parseInt(rc[1]);
			Cell cell = new Cell(r, c, this.size, this.combinations, this.id);
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

	public List<List<Integer>> permute(List<Integer> nums) {
		List<List<Integer>> result = new ArrayList<>();
		helper(0, nums, result);
		return result;
	}

	private void helper(int start, List<Integer> nums, List<List<Integer>> result) {
		if (start == nums.size() - 1) {
			ArrayList<Integer> list = new ArrayList<>();
			for (int num : nums) {
				list.add(num);
			}
			result.add(list);
			return;
		}

		for (int i = start; i < nums.size(); i++) {
			swap(nums, i, start);
			helper(start + 1, nums, result);
			swap(nums, i, start);
		}
	}

	private void swap(List<Integer> nums, int i, int j) {
		int temp = nums.get(i);
		nums.set(i, nums.get(j));
		nums.set(j, temp);
	}

	public List<List<Integer>> combinationSum3(int k, int n) {
		List<List<Integer>> result = new ArrayList<List<Integer>>();
		List<Integer> curr = new ArrayList<Integer>();
		helper(result, curr, k, 0, n);
		return result;
	}

	public void helper(List<List<Integer>> result, List<Integer> curr, int k, int start, int sum) {
		if (sum < 0) {
			return;
		}

		if (sum == 0 && curr.size() == k) {
			result.add(new ArrayList<Integer>(curr));
			return;
		}

		for (int i = start; i < this.size; i++) {
			curr.add(this.acceptedNumbers.get(i));
			helper(result, curr, k, i + 1, sum - this.acceptedNumbers.get(i));
			curr.remove(curr.size() - 1);
		}
	}
}
