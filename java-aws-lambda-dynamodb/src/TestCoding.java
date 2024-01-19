package com.lambda.jav.lambda;

import java.util.LinkedList;

public class TestCoding {

	public static void main(String args[]) {
		LinkedList<Integer> linkedList = new LinkedList<Integer>();
		linkedList.add(new Integer(1));
		linkedList.add(new Integer(2));
		linkedList.add(new Integer(3));
		linkedList.add(new Integer(4));
		linkedList.add(new Integer(5));
		
		System.out.println("Output: " + findMiddleElement(linkedList));
		
	}
	
	private static Integer findMiddleElement(LinkedList<Integer> list) {
		long count = list.stream().count();
		long index = count / 2;
		
		return list.get((int)index);
	}
}
