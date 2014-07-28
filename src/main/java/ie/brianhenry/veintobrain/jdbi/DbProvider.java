package ie.brianhenry.veintobrain.jdbi;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import com.mongodb.DB;
import com.mongodb.Mongo;

public class DbProvider implements Provider<DB> {

	private final Mongo mongo;
	private final String mongodb;

	@Inject
	public DbProvider(@Named("mongo") Mongo mongo, @Named("mongodb") String mongodb) {
		this.mongo = mongo;
		this.mongodb = mongodb;
	}

	public DB get() {

		DB db = mongo.getDB(mongodb);

		return db;
	}

}
