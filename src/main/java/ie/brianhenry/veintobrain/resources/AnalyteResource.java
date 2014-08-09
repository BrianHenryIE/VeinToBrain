package ie.brianhenry.veintobrain.resources;

import ie.brianhenry.veintobrain.core.ComputeAnalyteStats;
import ie.brianhenry.veintobrain.representations.AnalyteDate;
import ie.brianhenry.veintobrain.representations.AnalyteResult;
import ie.brianhenry.veintobrain.representations.AnalyteStat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.joda.time.LocalDate;

import net.vz.mongodb.jackson.DBCursor;
import net.vz.mongodb.jackson.JacksonDBCollection;

import com.google.gwt.core.shared.GWT;
import com.mongodb.DB;

@Path("/analyte")
@Produces(MediaType.APPLICATION_JSON)
public class AnalyteResource {

	JacksonDBCollection<AnalyteStat, String> analyteStats;

	@Inject
	public AnalyteResource(DB db) {

		analyteStats = JacksonDBCollection.wrap(db.getCollection("analytestat"), AnalyteStat.class, String.class);

	}

	@GET
	@Path("{type}/{period}")
	public List<AnalyteStat> getStat(@PathParam("type") String type, @PathParam("period") String period) {
		//
		// DBCursor<AnalyteStat> dbCursor =
		// analyteStats.find().is("analyteType", type).is("analytePeriod",
		// period);
		// List<AnalyteStat> stats = new ArrayList<AnalyteStat>();
		// while (dbCursor.hasNext()) {
		// AnalyteStat stat = dbCursor.next();
		// stats.add(stat);
		// }
//return stats;
		PSAdata pd = new PSAdata();
		HashMap<LocalDate, AnalyteStat> allDailyAnalyteStats = new HashMap<LocalDate, AnalyteStat>();

		List<AnalyteResult> analyteResultsList;
		List<AnalyteDate> allVaildAnalyteDates = new ArrayList<AnalyteDate>();
		List<AnalyteDate> allInVaildAnalyteDates = new ArrayList<AnalyteDate>();

		//the RAW data are here
		analyteResultsList = pd.getResults();

		//hm contains the results divided by day.
		//The analytes are still mixed up though.
		HashMap<LocalDate, List<String>> hm = new HashMap<LocalDate, List<String>>();

		for (AnalyteResult r : analyteResultsList) {
			if (hm.get(r.getDate()) == null)
				hm.put(r.getDate(), new ArrayList<String>());
			hm.get(r.getDate()).add(r.getResult());
		}

		for (LocalDate day : hm.keySet()) {
			AnalyteDate d = new AnalyteDate("psa", day.toDate(), hm.get(day));
			AnalyteStat s = ComputeAnalyteStats.computeDay(d, "psa");
			if (s.getIsValid()) {
				allDailyAnalyteStats.put(day, s);
				allVaildAnalyteDates.add(d);
			} else
				allInVaildAnalyteDates.add(d);
		}

		ComputeAnalyteStats.getMovingMeanOfMedian(allDailyAnalyteStats, 50);
		ComputeAnalyteStats.getMovingMeanOfMedian(allDailyAnalyteStats, 7);
		ComputeAnalyteStats.getMovingMeanOfMedian(allDailyAnalyteStats, 20);

		ComputeAnalyteStats.getMovingMean(allDailyAnalyteStats, 50);
		ComputeAnalyteStats.getMovingMean(allDailyAnalyteStats, 7);
		ComputeAnalyteStats.getMovingMean(allDailyAnalyteStats, 20);

		List<AnalyteStat> stats = new ArrayList<AnalyteStat>();

		for(AnalyteStat as : allDailyAnalyteStats.values())
			stats.add(as);

		return stats;

	}
}