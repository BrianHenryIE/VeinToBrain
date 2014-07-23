package ie.brianhenry.veintobrain.client;

import static org.junit.Assert.assertEquals;
import ie.brianhenry.veintobrain.core.ComputeAnalyteStats;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ComputeAnalyteStatsTest {

	@Test
	public void standardDeviationTest() {

		String[] readingsS = { "11.1", "14.5", "17.9", "8.7", "15.7", "12.3", "2.8", "14.9", "4.5", "7.8", "6.6",
				"10.6", "23.0", "19.0", "4.7", "9.5", "7.1", "13.5", "9.5", "3.5", "16.1", "21.7", "11.2", "9.8",
				"13.8", "20.7", "6.6", "4.2", "6.2", "19.9", "15.9", "21.8", "9.0" };

		List<Double> readings = new ArrayList<Double>();

		for (String s : readingsS)
			readings.add(Double.parseDouble(s));

		// =STDEV.P(B13:B45)
		// 5.774553914
		double excel = 5.774553914;

		double sd = ComputeAnalyteStats.standardDeviation(readings);
		assertEquals(excel, sd, 0.001);

	}
}