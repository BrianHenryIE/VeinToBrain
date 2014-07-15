package ie.brianhenry.veintobrain.core;

import ie.brianhenry.veintobrain.core.AnalyteStat;

import java.util.Date;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AnalyteStatTest {

	@SuppressWarnings("deprecation")
	@Test
	public void constructor(){

		Date statDate = new Date();
		statDate.setYear(2009);
		statDate.setMonth(0);
		statDate.setDate(2);
		
		String statType = "Folate";
		
		double[] readings = {11.1,14.5,17.9,8.7,15.7,12.3,2.8,14.9,4.5,7.8,6.6,10.6,23.0,19.0,4.7,9.5,7.1,13.5,9.5,3.5,16.1,21.7,11.2,9.8,13.8,20.7,6.6,4.2,6.2,19.9,15.9,21.8,9.0};
		
		// Maybe this should take in an array of number and just have getters
		AnalyteStat folate02Jan2009 = new AnalyteStat(statDate, statType, readings);

		double min	= 2.8;
		double i2p5th = 3.36;
		double i25th= 7.1;
		double median = 11.1;
		double mean = 11.9;
		double mode = 6.6;
		double i75th = 15.9;
		double i97p5th = 22.04;
		double max	= 23.0;
		
		assertEquals(statDate, folate02Jan2009.getDate());
		assertEquals(statType, folate02Jan2009.getType());
		
		assertEquals(min, folate02Jan2009.getMin(), 0.001);
		assertEquals(i2p5th, folate02Jan2009.getPercentile(0.025), 0.001);
		assertEquals(i25th, folate02Jan2009.getPercentile(0.25), 0.001);
		assertEquals(median, folate02Jan2009.getPercentile(0.5),0.001);
		assertEquals(mean, folate02Jan2009.getMean(), 0.001);
		assertEquals(mode, folate02Jan2009.getMode()[0], 0.001);
		assertEquals(i75th,folate02Jan2009.getPercentile(0.75), 0.001);
		assertEquals(i97p5th, folate02Jan2009.getPercentile(0.975), 0.001);
		assertEquals(max, folate02Jan2009.getMax(), 0.001);
		
	}
}

/*
 * 
 * // Sample values for calculations // >24.0 // 2.8 // UXH
 */