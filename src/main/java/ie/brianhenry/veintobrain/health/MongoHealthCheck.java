package ie.brianhenry.veintobrain.health;

import javax.inject.Inject;
import javax.inject.Named;

import com.codahale.metrics.health.HealthCheck;
import com.mongodb.Mongo;


public class MongoHealthCheck extends HealthCheck {
 
    private Mongo mongo;
 
    @Inject
    public MongoHealthCheck(@Named("mongo") Mongo mongo) {
        this.mongo = mongo;
    }
 
    @Override
    protected Result check() throws Exception {
        mongo.getDatabaseNames();
        return Result.healthy();
    }
 
}