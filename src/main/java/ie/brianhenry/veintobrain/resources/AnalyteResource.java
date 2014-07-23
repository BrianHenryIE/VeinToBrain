package ie.brianhenry.veintobrain.resources;

import ie.brianhenry.veintobrain.jdbi.Dummy;
import ie.brianhenry.veintobrain.representations.AnalyteStat;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;

@Path("/analyte-stat")
@Produces(MediaType.APPLICATION_JSON)
public class AnalyteResource {

	public AnalyteResource() {
	}

	@GET
	@Timed
	public List<AnalyteStat> sayHello(@QueryParam("name") Optional<String> name) {

		return Dummy.getListOfStats("psa");

	}
}