package com.ritchieeinstein.javaDS.trees.binarytree;

import java.util.Stack;

public class IterativePreOrderTraversal {
	
	public static void main(String[] args) {
		Node root = new Node(1);
		root.left = new Node(2);
		root.right = new Node(3);
		root.left.left = new Node(4);
		root.left.right = new Node(5);
		root.right.left = new Node(6);
		
		Stack<Node>stack = new Stack<>();
		stack.push(root);
		while(!stack.isEmpty()) {
			Node temp = stack.pop();
			System.out.print(temp.key + " ");
			if(null!= temp.right) stack.push(temp.right);
			if(null!= temp.left) stack.push(temp.left);
		}
		
	}

}
