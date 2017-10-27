package com.ritchieeinstein.javaDS.trees.binarytree;

public class BinaryTree {
	
	//Create a basic tree with few nodes;
	
	public static void main(String[] args) {
		Node root;
		
		root = new Node(1);
		root.left = new Node(2);
		root.right = new Node(3);
		root.left.left = new Node(4);
	}

}
