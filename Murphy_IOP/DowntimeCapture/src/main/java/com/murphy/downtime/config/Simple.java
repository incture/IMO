package com.murphy.downtime.config;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Simple {

	public static void main(String args[]) throws ParseException

	{

		Date date = new Date();

		System.out.println(date);

		Calendar calendar = Calendar.getInstance();

		calendar.setTime(date);

		int n = calendar.get(Calendar.HOUR_OF_DAY);

		if (n < 7) {

			calendar.add(Calendar.HOUR_OF_DAY, -6);
			System.out.println(calendar.getTime());

			// date = new Date(calendar.getTimeInMillis());

		}

		// SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println(dt.format(calendar.getTime()));

		System.out.println("          " + (int) (2.5 * 60));

		dt = new SimpleDateFormat("HH:mm:ss");
		System.out.println(dt.format(calendar.getTime()));
		calendar.add(Calendar.MINUTE, (int) 2.5 * 60);

		System.out.println(Calendar.MINUTE);
		System.out.println((float) 3 / 60);

//		System.out.println(Integer.parseInt(""));
		// System.out.println(date);
		
		Date d = new Date();
		SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		String s  = dt1.format(d);
		System.out.println(s);
		Date d1 = dt1.parse(s);
		System.out.println(d1);
		
		System.out.println(new Date(551664000000l));
		

	}

}