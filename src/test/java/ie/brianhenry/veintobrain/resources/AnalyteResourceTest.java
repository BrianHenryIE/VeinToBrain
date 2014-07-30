package ie.brianhenry.veintobrain.resources;

import ie.brianhenry.veintobrain.representations.AnalyteStat;
import ie.brianhenry.veintobrain.representations.AnalyteStat.StatPeriod;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import net.vz.mongodb.jackson.DBCursor;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;

public class AnalyteResourceTest {

	
	
	/*
	
	
	
	@GET
	@Path("{type}/{period}")
	@Timed
	public List<AnalyteStat> getStat(@PathParam("type") String type, @PathParam("period") Optional<StatPeriod> period) {

		StatPeriod searchPeriod;
		if(period.isPresent())
			searchPeriod = period.get();
		else
			searchPeriod = StatPeriod.DAY;
		
		DBCursor<AnalyteStat> dbCursor = analyteStats.find().is("analyteType", type); //.is("analytePeriod", "DAY");
		List<AnalyteStat> stats = new ArrayList<AnalyteStat>();
		while (dbCursor.hasNext()) {
			AnalyteStat stat = dbCursor.next();
			stats.add(stat);
		}

		return stats;

	}
	
	*/
}
