package ie.brianhenry.veintobrain.server.jdbi;

import io.dropwizard.lifecycle.Managed;

import javax.inject.Inject;
import javax.inject.Named;

import com.mongodb.MongoClient;

public class MongoManaged implements Managed {

	private MongoClient mongo;

	@Inject
	public MongoManaged(@Named("mongo") MongoClient mongo) {

		this.mongo = mongo;
	}

	@Override
	public void start() throws Exception {
	}

	@Override
	public void stop() throws Exception {
		mongo.close();
	}

}
