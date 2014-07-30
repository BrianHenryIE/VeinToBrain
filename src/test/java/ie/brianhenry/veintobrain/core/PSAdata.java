package ie.brianhenry.veintobrain.core;

import ie.brianhenry.veintobrain.representations.AnalyteDate;
import ie.brianhenry.veintobrain.representations.AnalyteResult;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * @author BrianHenry.ie
 * 
 *         This is the basis for the data import
 *
 */
public class PSAdata {

	private List<AnalyteResult> results = new ArrayList<AnalyteResult>();

	public List<AnalyteResult> getResults() {
		return results;
	}

	// http://www.mkyong.com/java/how-to-read-and-parse-csv-file-in-java/
	public PSAdata() {

		String csvFile = "./src/main/resources/psa-full-forimport.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";

		final DateTimeFormatter dtf = DateTimeFormat.forPattern("dd-MMM-yy");

		try {

			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {

				// use comma as separator
				String[] result = line.split(cvsSplitBy);

				if (result.length > 1) // && result[1] != null) //TODO we should be counting null results, probably
					results.add(new AnalyteResult("psa", dtf.parseLocalDate(result[0]), result[1]));

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public List<AnalyteDate> getMonth(int i) {

		List<AnalyteDate> ads = new ArrayList<AnalyteDate>();

		HashMap<LocalDate, List<String>> hm = new HashMap<LocalDate, List<String>>();

		for (AnalyteResult r : results)
			if (r.getDate().getMonthOfYear() == i) {
				if (hm.get(r.getDate()) == null)
					hm.put(r.getDate(), new ArrayList<String>());
				hm.get(r.getDate()).add(r.getResult());
			}
		
		for (LocalDate day : hm.keySet())
			ads.add(new AnalyteDate("psa", day.toDate(), hm.get(day).toArray(new String[hm.get(day).size()])));

		return ads;
	}

}
