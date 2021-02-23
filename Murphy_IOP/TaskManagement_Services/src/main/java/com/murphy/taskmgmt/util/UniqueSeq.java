package com.murphy.taskmgmt.util;

import java.util.concurrent.atomic.AtomicLong;

public class UniqueSeq {

	private static final AtomicLong sequence = new AtomicLong(System.currentTimeMillis() / 1000);

	private static UniqueSeq generator = new UniqueSeq();

	private UniqueSeq() { }

	public static synchronized UniqueSeq getInstance() {
		return generator;
	}

	public static  long getNext() {
		return sequence.incrementAndGet();
	}
	
	public Object clone() throws CloneNotSupportedException { 
		  
	      throw new CloneNotSupportedException(); 
	  
	}

}
