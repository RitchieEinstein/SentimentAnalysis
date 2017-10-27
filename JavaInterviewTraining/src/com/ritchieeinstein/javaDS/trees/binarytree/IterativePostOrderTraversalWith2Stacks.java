package com.ritchieeinstein.javaDS.trees.binarytree;

import java.util.Stack;

public class IterativePostOrderTraversalWith2Stacks {
	
	public static void main(String[] args) {
		
		Node root = new Node(1);
		root.left = new Node(2);
		root.right = new Node(3);
		root.left.left = new Node(4);
		root.left.right = new Node(5);
		root.right.left = new Node(6);
		root.right.right = new Node(7);
		printPostOrder(root);
		
	}
	
	public static void printPostOrder(Node root) {
		if(null == root) {
			return;
		}
		Stack<Node> s1 = new Stack<>();
		Stack<Node> s2 = new Stack<>();
		Node temp = root;
		s1.push(root);
		while(!s1.empty()) {
			temp = s1.pop();
			if(null!=temp.left)s1.push(temp.left);
			if(null!=temp.right)s1.push(temp.right);
			s2.push(temp);
		}
		while(!s2.empty()) {
			temp = s2.pop();
			System.out.print(temp.key + " ");
		}
	}

}
