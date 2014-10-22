package ie.brianhenry.veintobrain.server;

import ie.brianhenry.veintobrain.server.resources.AnalyteResource;
import ie.brianhenry.veintobrain.server.resources.FriendlyLoginResource;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.auth.basic.BasicAuthProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class VeintobrainApplication extends Application<VeintobrainConfiguration> {
	public static void main(String[] args) throws Exception {
		new VeintobrainApplication().run(args);
	}

	@Override
	public void initialize(Bootstrap<VeintobrainConfiguration> bootstrap) {

		bootstrap.addBundle(new AssetsBundle("/gwt", "/", "index.html", "gwt"));
		bootstrap.addBundle(new AssetsBundle("/gwt/js", "/js", "", "js"));

	}

	@Override
	public void run(VeintobrainConfiguration configuration, Environment environment) throws Exception {
		
		environment.jersey().setUrlPattern("/api/*");
		environment.jersey().register(new BasicAuthProvider<Boolean>(new SimpleAuthenticator(), "Super secret stufff"));

		final AnalyteResource resource = new AnalyteResource();
		environment.jersey().register(resource);

		final FriendlyLoginResource loginResource = new FriendlyLoginResource();
		environment.jersey().register(loginResource);

	}

}