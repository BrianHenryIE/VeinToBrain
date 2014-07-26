package ie.brianhenry.veintobrain;

import ie.brianhenry.veintobrain.health.MongoHealthCheck;
import ie.brianhenry.veintobrain.jdbi.MongoManaged;
import ie.brianhenry.veintobrain.representations.AnalyteResult;
import ie.brianhenry.veintobrain.representations.AnalyteStat;
import ie.brianhenry.veintobrain.resources.AnalyteResource;
import ie.brianhenry.veintobrain.resources.FriendlyLoginResource;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.auth.basic.BasicAuthProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import net.vz.mongodb.jackson.JacksonDBCollection;

import com.mongodb.DB;
import com.mongodb.Mongo;

import de.spinscale.dropwizard.jobs.JobsBundle;

public class VeintobrainApplication extends Application<VeintobrainConfiguration> {
	public static void main(String[] args) throws Exception {
		new VeintobrainApplication().run(args);
	}

	@Override
	public void initialize(Bootstrap<VeintobrainConfiguration> bootstrap) {
		bootstrap.addBundle(new AssetsBundle("/gwt", "/", "index.html", "gwt"));
		bootstrap.addBundle(new AssetsBundle("/gwt/js", "/js", "", "js"));

		bootstrap.addBundle(new JobsBundle("ie.brianhenry.veintobrain.core.cron"));

	}

	@Override
	public void run(VeintobrainConfiguration configuration, Environment environment) throws Exception {
		Mongo mongo = new Mongo(configuration.mongohost, configuration.mongoport);
        MongoManaged mongoManaged = new MongoManaged(mongo);
        environment.lifecycle().manage(mongoManaged);
 
        environment.healthChecks().register("mongodb", new MongoHealthCheck(mongo));
 
        DB db = mongo.getDB(configuration.mongodb);
        JacksonDBCollection<AnalyteResult, String> analyteResults = JacksonDBCollection.wrap(db.getCollection("analyteresult"), AnalyteResult.class, String.class);
        
        JacksonDBCollection<AnalyteStat, String> analyteStat = JacksonDBCollection.wrap(db.getCollection("analytestat"), AnalyteStat.class, String.class);
        
		environment.jersey().setUrlPattern("/api/*");
		environment.jersey().register(new BasicAuthProvider<Boolean>(new SimpleAuthenticator(), "Super secret stufff"));

		final AnalyteResource resource = new AnalyteResource(analyteResults, analyteStat);
		environment.jersey().register(resource);

		final FriendlyLoginResource loginResource = new FriendlyLoginResource();
		environment.jersey().register(loginResource);

//		final TemplateHealthCheck healthCheck = new TemplateHealthCheck(configuration.getTemplate());
//		environment.healthChecks().register("template", healthCheck);
		
		// SundialManager sm = new SundialManager(configuration.getSundialProperties()); 
		//environment.manage(sm);
	}

}