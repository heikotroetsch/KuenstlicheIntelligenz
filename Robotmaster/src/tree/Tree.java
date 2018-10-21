package tree;

import monteCarlo.State;

public class Tree {
	Node root;
	
	public Tree(State state) {
		root = new Node(state);
	}
	
	public Node getRoot() {
		return root;
	}
	
	public void setRoot(Node root) {
		this.root = root;
	}
	
	public void addChild(Node parent, Node child) {
		parent.getChildArray().add(child);
	}
}
