package ie.brianhenry.veintobrain;

import ie.brianhenry.veintobrain.jdbi.DbProvider;

import java.net.UnknownHostException;

import javax.inject.Named;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.mongodb.DB;
import com.mongodb.Mongo;

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
		Mongo mongo = new Mongo(configuration.getMongohost(), configuration.getMongoport());
		return mongo;
	}
//
//	@Provides
//	@Named("db")
//	public DB provideDb(VeintobrainConfiguration configuration) throws UnknownHostException {
//		// DB db =  guiceBundle.getInjector().getInstance(MongoManaged.class).getDB(configuration.getMongodb());
//		DB db = provideMongo(configuration).getDB(provideMongodb(configuration));
//		return db;
//	}
}
