package ie.brianhenry.veintobrain;

import ie.brianhenry.veintobrain.jdbi.DbProvider;

import java.net.UnknownHostException;

import javax.inject.Named;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;

public class VeintobrainModule extends AbstractModule {
	
	@Override
	protected void configure() {
		bind(DB.class).toProvider(DbProvider.class);
	}

	@Provides
	@Named("mongohost")
	public String provideMongohost(VeintobrainConfiguration configuration) {
		return configuration.getMongohost();
	}

	@Provides
	@Named("mongoport")
	public int provideMongoport(VeintobrainConfiguration configuration) {
		return configuration.getMongoport();
	}
	
	@Provides
	@Named("mongodb")
	public String provideMongodb(VeintobrainConfiguration configuration) {
		return configuration.getMongodb();
	}

	@Provides
	@Named("mongo")
	public Mongo provideMongo(VeintobrainConfiguration configuration) throws UnknownHostException {
		MongoClient mongo = new MongoClient(configuration.getMongohost(), configuration.getMongoport());
		return mongo;
	}

}
