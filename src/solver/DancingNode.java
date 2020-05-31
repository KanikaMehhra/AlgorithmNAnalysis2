package solver;

public class DancingNode {
	protected DancingNode left, right, up, down;
	protected ColumnNode column;
	protected int number;


	//creates the initial dancing node without the column node reference
	public DancingNode(int number) {
		this.left = this.right = this.up = this.down = this;
		this.number=number;
	}
	
	//creates the initial dancing node with column node reference.
	public DancingNode(ColumnNode columnNode, int number) {
		this(number);
		this.column = columnNode;
	}
	
	//links the given node with the up and down nodes in the dancing links.
	public DancingNode linkVertically(DancingNode dancingNode) {
		dancingNode.down = this.down;
		dancingNode.down.up = dancingNode;
		dancingNode.up = this;
		this.down = dancingNode;
		return dancingNode;
	}
	
	//links the given node with left and right nodes in the dancing links.
	public DancingNode linkHorizontally(DancingNode dancingNode) {
		dancingNode.right = this.right;
		dancingNode.right.left = dancingNode;
		dancingNode.left = this;
		this.right = dancingNode;
		return dancingNode;
	}

	//unlinks this node from the left and right nodes in the dancing links.
	public void removeHorizontally() {
		this.left.right = this.right;
		this.right.left = this.left;
	}

	//links this node from the left and right nodes in the dancng links.
	public void reinsertHorizontally() {
		this.left.right = this;
		this.right.left = this;
	}

	//unlinks this node from the up and down nodes in the dancing links.	
	public void removeVertically() {
		this.up.down = this.down;
		this.down.up = this.up;
	}

	//links this node from the up and down nodes in the dancing links.	
	public void reinsertVertically() {
		this.up.down = this;
		this.down.up = this;
	}
}
