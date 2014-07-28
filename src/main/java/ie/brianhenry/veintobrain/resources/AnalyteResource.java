package ie.brianhenry.veintobrain.resources;

import ie.brianhenry.veintobrain.core.ComputeAnalyteStats;
import ie.brianhenry.veintobrain.jdbi.PSAdata;
import ie.brianhenry.veintobrain.representations.AnalyteResult;
import ie.brianhenry.veintobrain.representations.AnalyteStat;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import net.vz.mongodb.jackson.DBCursor;
import net.vz.mongodb.jackson.JacksonDBCollection;
import net.vz.mongodb.jackson.WriteResult;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;

@Path("/analyte-stat")
@Produces(MediaType.APPLICATION_JSON)
public class AnalyteResource {

	JacksonDBCollection<AnalyteResult, String> analyteResults;
	JacksonDBCollection<AnalyteStat, String> analyteStats;

	public AnalyteResource(JacksonDBCollection<AnalyteResult, String> analyteResults,
			JacksonDBCollection<AnalyteStat, String> analyteStat) {

		this.analyteResults = analyteResults;
		this.analyteStats = analyteStat;

	}

	@GET
	@Timed
	public List<AnalyteStat> sayHello(@QueryParam("name") Optional<String> name) {

		DBCursor<AnalyteStat> dbCursor = analyteStats.find();
		List<AnalyteStat> stats = new ArrayList<AnalyteStat>();
		while (dbCursor.hasNext()) {
			AnalyteStat stat = dbCursor.next();
			stats.add(stat);
		}

		return stats; // Dummy.getListOfStats("psa");

	}
}