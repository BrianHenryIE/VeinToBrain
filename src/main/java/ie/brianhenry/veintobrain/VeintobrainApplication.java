package ie.brianhenry.veintobrain;

import ie.brianhenry.veintobrain.health.MongoHealthCheck;
import ie.brianhenry.veintobrain.jdbi.MongoManaged;
import ie.brianhenry.veintobrain.resources.AnalyteResource;
import ie.brianhenry.veintobrain.resources.FriendlyLoginResource;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.auth.basic.BasicAuthProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import com.hubspot.dropwizard.guice.GuiceBundle;

import de.spinscale.dropwizard.jobs.GuiceJobsBundle;

public class VeintobrainApplication extends Application<VeintobrainConfiguration> {
	public static void main(String[] args) throws Exception {
		new VeintobrainApplication().run(args);
	}

	private GuiceBundle<VeintobrainConfiguration> guiceBundle;

	@Override
	public void initialize(Bootstrap<VeintobrainConfiguration> bootstrap) {

		guiceBundle = GuiceBundle.<VeintobrainConfiguration> newBuilder().addModule(new VeintobrainModule())
				.enableAutoConfig(getClass().getPackage().getName()).setConfigClass(VeintobrainConfiguration.class)
				.build();

		bootstrap.addBundle(guiceBundle);

		bootstrap.addBundle(new AssetsBundle("/gwt", "/", "index.html", "gwt"));
		bootstrap.addBundle(new AssetsBundle("/gwt/js", "/js", "", "js"));

		bootstrap.addBundle(new GuiceJobsBundle("ie.brianhenry.veintobrain.core.cron", guiceBundle.getInjector()));

	}

	@Override
	public void run(VeintobrainConfiguration configuration, Environment environment) throws Exception {

		MongoManaged mongoManaged = guiceBundle.getInjector().getInstance(MongoManaged.class);
		environment.lifecycle().manage(mongoManaged);
		environment.healthChecks().register("mongodb", guiceBundle.getInjector().getInstance(MongoHealthCheck.class));

		environment.jersey().setUrlPattern("/api/*");
		environment.jersey().register(new BasicAuthProvider<Boolean>(new SimpleAuthenticator(), "Super secret stufff"));

		final AnalyteResource resource = guiceBundle.getInjector().getInstance(AnalyteResource.class);
		environment.jersey().register(resource);

		final FriendlyLoginResource loginResource = new FriendlyLoginResource();
		environment.jersey().register(loginResource);

	}

}