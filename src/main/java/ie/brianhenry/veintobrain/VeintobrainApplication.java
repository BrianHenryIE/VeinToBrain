package ie.brianhenry.veintobrain;

import ie.brianhenry.veintobrain.health.TemplateHealthCheck;
import ie.brianhenry.veintobrain.resources.VeintobrainResource;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class VeintobrainApplication extends Application<VeintobrainConfiguration> {
	public static void main(String[] args) throws Exception {
		new VeintobrainApplication().run(args);
	}

	@Override
	public String getName() {
		return "hello-world";
	}

	@Override
	public void initialize(Bootstrap<VeintobrainConfiguration> bootstrap) {
		bootstrap.addBundle(new AssetsBundle("/gwt", "/", "index.html", "gwt"));
		
		
	}

	@Override
	public void run(VeintobrainConfiguration configuration,
			Environment environment) {
		final VeintobrainResource resource = new VeintobrainResource(
				configuration.getTemplate(), configuration.getDefaultName());

		final TemplateHealthCheck healthCheck = new TemplateHealthCheck(
				configuration.getTemplate());
		environment.healthChecks().register("template", healthCheck);

		environment.jersey().setUrlPattern("/api/*");

		environment.jersey().register(resource);

	}

}