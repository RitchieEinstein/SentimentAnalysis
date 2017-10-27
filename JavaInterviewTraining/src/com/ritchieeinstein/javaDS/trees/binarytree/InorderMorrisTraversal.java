package com.ritchieeinstein.javaDS.trees.binarytree;

public class InorderMorrisTraversal {
	
	public static void main(String[] args) {
		Node root = new Node(1);
		root.left = new Node(2);
		root.right = new Node(3);
		root.left.left = new Node(4);
		root.left.right = new Node(5);
		root.right.left = new Node(6);
	}
	
	public static void morrisConnect(Node parent) {
		if(null == parent) {
			return;
		}
		Node prev = parent;
		Node temp = parent;
		while(null!=temp.left) {
			prev = temp;
			temp = temp.left;
			if(temp.left == null) {
				System.out.println(temp.key + " ");
			}
		}
	}

}
