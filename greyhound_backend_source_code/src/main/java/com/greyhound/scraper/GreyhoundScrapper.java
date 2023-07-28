package com.greyhound.scraper;

import com.greyhound.service.impl.GreyhoundScraperServiceImpl;

public class GreyhoundScrapper {
	public static void main(String[] args) {
		try {
			GreyhoundScraperServiceImpl service = new GreyhoundScraperServiceImpl();
			//service.startScrapper();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getByValue(double resultRunTime, double raceWinTime) {
		if (raceWinTime != 0 || resultRunTime != 0) {
			double e = (resultRunTime - raceWinTime) / 0.08;
			e = 0.25 * Math.round(e / 0.25);
			int t = (int) Math.floor(e);
			String n = decimalToFraction(e - t);
			if (t == 0) {
				t = 0;
			}
			if (t != 0 && n != "") {
				n = " " + n;
			}
			return t + n;
		}
		return "";
	}

	public static String decimalToFraction(double e) {
		if (e == 0) {
			return "";
		}
		int t = String.valueOf(e).length() - 2;
		double n = Math.pow(10, t);
		double i = e * n;
		double s = findGCD(i, n);
		n /= s;
		i /= s;
		return String.format("%.0f/%.0f", i, n);
	}

	public static double findGCD(double a, double b) {
		if (b == 0) {
			return a;
		}
		return findGCD(b, a % b);
	}

}
