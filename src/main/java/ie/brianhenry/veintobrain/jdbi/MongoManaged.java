package ie.brianhenry.veintobrain.jdbi;

import io.dropwizard.lifecycle.Managed;

import javax.inject.Inject;
import javax.inject.Named;

import com.mongodb.Mongo;


public class MongoManaged implements Managed {

	private Mongo mongo;
	
	@Inject
	public MongoManaged(@Named("mongo") Mongo mongo) {
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
