package com.huoyun.core;

import java.util.Random;

import org.joda.time.DateTime;

public class UUIDGenerate {

	private final static int MAX = 9999;
	private final static int MIN = 1000;

	public String generate() {
		Random random = new Random();
		int randomNum = random.nextInt(MAX) % (MAX - MIN + 1) + MIN;
		return DateTime.now().toString("yyyyMMddHHmmssSSS") + randomNum;
	}
}
