package com.ritchieeinstein.javaDS.trees.binarytree;

import java.util.LinkedList;
import java.util.Queue;

import javax.print.attribute.standard.QueuedJobCount;

public class LevelOrderTraversalByLine {
	
	public static void main(String[] args) {
		Node root = new Node(1);
		root.left = new Node(2);
		root.right = new Node(3);
		root.left.left = new Node(4);
		root.left.right = new Node(5);
		root.right.left = new Node(6);
		Queue<Node> queue = new LinkedList<>();
		queue.add(root);
		while(true) {
			int queueCount = queue.size();
			if(queueCount == 0)break;
			while(queueCount > 0) {
				Node temp = queue.poll();
				queueCount--;
				System.out.print(temp.key + " ");
				if(null != temp.left) queue.add(temp.left);
				if(null != temp.right) queue.add(temp.right);
			}
			System.out.println();
		}
	}

}
