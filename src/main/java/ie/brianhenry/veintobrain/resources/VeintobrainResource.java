package ie.brianhenry.veintobrain.resources;

import ie.brianhenry.veintobrain.core.AnalyteStat;
import ie.brianhenry.veintobrain.jdbi.Dummy;

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
public class VeintobrainResource {
	// private final String template;
	// private final String defaultName;
	// private final AtomicLong counter;

	public VeintobrainResource(String template, String defaultName) {
		//this.template = template;
		//this.defaultName = defaultName;
		//this.counter = new AtomicLong();
	}

	@GET
	@Timed
	public List<AnalyteStat> sayHello(@QueryParam("name") Optional<String> name) {
		// final String value = String.format(template, name.or(defaultName));
		// return new Saying(counter.incrementAndGet(), value);
		return Dummy.getListOfStats(name.get());
		
	}
}