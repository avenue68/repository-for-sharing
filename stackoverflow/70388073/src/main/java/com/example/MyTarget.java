package com.example;

@MyAnnotation
public class MyTarget {

	/**
	 * This constructor should be woven
	 */
	public MyTarget() {
		System.out.println("********** Target's constructor called.");
	}

	/**
	 * This method should be woven
	 */
	public void MyMethod() {
		System.out.println("********** Target's method called.");
	}

}
