package solver;

public class ColumnNode extends DancingNode {
	protected int size;
	protected int number;

	public ColumnNode(int number) {
		super();
		this.size = 0;
		this.number = number;
		this.column = this;
	}

	public void cover() {
		// first unlink this column node from the left and right nodes.
		removeHorizontally();
		// second unlink all the dancing nodes which corresponds to the this column.
		for (DancingNode i = this.down; i != this; i = i.down) {
			for (DancingNode j = i.right; j != i; j = j.right) {
				j.removeVertically();
				j.column.size--;
			}
		}
	}

	public void uncover() {// strictly opposite procedure of cover.
		for (DancingNode i = this.up; i != this; i = i.up) {
			for (DancingNode j = i.left; j != i; j = j.left) {
				j.column.size++;
				j.reinsertVertically();
			}
		}
		reinsertHorizontally();
	}

}
