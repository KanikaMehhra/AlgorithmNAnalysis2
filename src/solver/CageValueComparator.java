package solver;

import java.util.Comparator;

public class CageValueComparator implements Comparator<Cage> {
	@Override
	public int compare(Cage cage1, Cage cage2) {
		return cage1.value - cage2.value;
	}
}