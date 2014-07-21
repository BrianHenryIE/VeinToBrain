package ie.brianhenry.veintobrain.resources;

import ie.brianhenry.veintobrain.shared.LoginResponse;
import ie.brianhenry.veintobrain.shared.User;
import io.dropwizard.auth.Auth;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;

@Path("/login")
@Produces(MediaType.APPLICATION_JSON)
public class FriendlyLoginResource {

	public FriendlyLoginResource() {
	}

	@GET
	@Timed
	public LoginResponse sayHello(@QueryParam("name") Optional<String> name, @Auth Boolean isAuthenticated) {

		return new LoginResponse(isAuthenticated, new User("test", "test"), "All good in the hood");
		
	}
}