package com.huoyun.core.bo.query.impl;

public class Cursor {

	private int value;
	private int start;

	public int getValue() {
		return value;
	}

	public void setValue(int currentIndex) {
		this.value = currentIndex;
	}

	public void move(int step) {
		this.value += step;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}
	
	public void resetStart(){
		this.start = this.value;
	}

}
