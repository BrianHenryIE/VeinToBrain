package ie.brianhenry.veintobrain.resources;

import ie.brianhenry.veintobrain.jdbi.Dummy;
import ie.brianhenry.veintobrain.representations.AnalyteStat;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import net.vz.mongodb.jackson.DBCursor;
import net.vz.mongodb.jackson.JacksonDBCollection;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;
import com.mongodb.DB;

@Path("/analyte-stat")
@Produces(MediaType.APPLICATION_JSON)
public class AnalyteResource {

	JacksonDBCollection<AnalyteStat, String> analyteStats;

	@Inject
	public AnalyteResource(DB db) {

		analyteStats = JacksonDBCollection.wrap(db.getCollection("analytestat"), AnalyteStat.class, String.class);

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

		return stats;

	}
}