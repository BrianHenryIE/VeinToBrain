package ie.brianhenry.veintobrain.server.health;

import javax.inject.Inject;
import javax.inject.Named;

import com.codahale.metrics.health.HealthCheck;
import com.mongodb.MongoClient;


public class MongoHealthCheck extends HealthCheck {
 
    private MongoClient mongo;
 
    @Inject
    public MongoHealthCheck(@Named("mongo") MongoClient mongo) {
        this.mongo = mongo;
    }
 
    @Override
    protected Result check() throws Exception {
        mongo.getDatabaseNames();
        return Result.healthy();
    }
 
}