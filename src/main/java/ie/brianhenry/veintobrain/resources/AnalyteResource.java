package ie.brianhenry.veintobrain.resources;

import ie.brianhenry.veintobrain.representations.AnalyteStat;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.vz.mongodb.jackson.DBCursor;
import net.vz.mongodb.jackson.JacksonDBCollection;

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

		DBCursor<AnalyteStat> dbCursor = analyteStats.find().is("analyteType", type).is("analytePeriod", period);
		List<AnalyteStat> stats = new ArrayList<AnalyteStat>();
		while (dbCursor.hasNext()) {
			AnalyteStat stat = dbCursor.next();
			stats.add(stat);
		}
		return stats;

	}
}