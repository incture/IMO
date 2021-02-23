package com.murphy.taskmgmt.util;

import java.math.BigInteger;

public class SequenceGenerator {

	volatile static int n = 1;

	public static synchronized int nextNum() {
		return n++;
	}

	/*@Autowired
	private static SessionFactory sessionFactory;


	public static Session getSession() {
		try {
			return sessionFactory.getCurrentSession();
		} catch (HibernateException e){
			e.printStackTrace();
			return sessionFactory.openSession();
		}

	}*/


	public long getNextKey(BigInteger current){
//		String current = getLatestKey( o, column );
		if(ServicesUtil.isEmpty(current)){
			//return "TMD0000001";
			return 1;
		}
//		String currentHeader = current.substring(0,3);
//		String currentNo = current.substring(3,current.length());
//		int currentNoLength = currentNo.length();
//		String currentNo = current;
//		int nextNo = Integer.parseInt(currentNo) + 1;
//		String nextNum = String.valueOf(nextNo);

//		if(current.length()<nextNum.length()){
//			currentNoLength= currentNoLength+1;
//		}
		return current.longValue()+1;
		//return currentHeader +String.format("%0"+currentNoLength+"d", nextNo); 
	}
	
	public long getNextKey(Integer current){
//		String current = getLatestKey( o, column );
		if(ServicesUtil.isEmpty(current)){
			//return "TMD0000001";
			return 1;
		}
//		String currentHeader = current.substring(0,3);
//		String currentNo = current.substring(3,current.length());
//		int currentNoLength = currentNo.length();
//		String currentNo = current;
//		int nextNo = Integer.parseInt(currentNo) + 1;
//		String nextNum = String.valueOf(nextNo);

//		if(current.length()<nextNum.length()){
//			currentNoLength= currentNoLength+1;
//		}
		return current.longValue()+1;
		//return currentHeader +String.format("%0"+currentNoLength+"d", nextNo); 
	}

	
	public String getNextKey(String current){
//		String current = getLatestKey( o, column );
		if(ServicesUtil.isEmpty(current)){
			//return "TMD0000001";
			return "1";
		}
//		String currentHeader = current.substring(0,3);
//		String currentNo = current.substring(3,current.length());
//		int currentNoLength = currentNo.length();
//		String currentNo = current;
//		int nextNo = Integer.parseInt(currentNo) + 1;
//		String nextNum = String.valueOf(nextNo);

//		if(current.length()<nextNum.length()){
//			currentNoLength= currentNoLength+1;
//		}
		return String.valueOf(Long.parseLong(current.trim())+1);
		//return currentHeader +String.format("%0"+currentNoLength+"d", nextNo); 
	}
}
