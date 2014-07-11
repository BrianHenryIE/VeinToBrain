package ie.brianhenry.veintobrain.jdbi;

import ie.brianhenry.veintobrain.core.AnalyteStat;

import java.util.Date;

public class Dummy {

	public static AnalyteStat getIndividualStat(){

		Date statDate = new Date();
		statDate.setYear(2009);
		statDate.setMonth(0);
		statDate.setDate(2);
		
		String statType = "Folate";
		
		double[] readings = {11.1,14.5,17.9,8.7,15.7,12.3,2.8,14.9,4.5,7.8,6.6,10.6,23.0,19.0,4.7,9.5,7.1,13.5,9.5,3.5,16.1,21.7,11.2,9.8,13.8,20.7,6.6,4.2,6.2,19.9,15.9,21.8,9.0};
		
		// Maybe this should take in an array of number and just have getters
		AnalyteStat folate02Jan2009 = new AnalyteStat(statDate, statType, readings);

		return folate02Jan2009;
		
	}
	
}
