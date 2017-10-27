package com.ritchieeinstein.javaDS.trees.binarytree;

public class TreeTraversal {
	
	public static void main(String[] args) {
		Node root = new Node(1);
		root.left = new Node(2);
		root.right = new Node(3);
		root.left.left = new Node(4);
		root.left.right = new Node(5);
		root.right.left = new Node(6);
		inOrderTraversal(root);
		System.out.println();
		preOrderTraversal(root);
		System.out.println();
		postOrderTraversal(root);
	}
	
	public static void postOrderTraversal(Node node) {
		if(null == node)
			return;
		postOrderTraversal(node.left);
		postOrderTraversal(node.right);
		System.out.print(" " + node.key);
	}
	
	public static void inOrderTraversal(Node node) {
		if(null == node)
			return;
		inOrderTraversal(node.left);
		System.out.print(" " + node.key);
		inOrderTraversal(node.right);
	}
	
	public static void preOrderTraversal(Node node) {
		if(null == node)
			return;
		System.out.print(" " + node.key);
		preOrderTraversal(node.left);
		preOrderTraversal(node.right);
	}

}
