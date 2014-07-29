package ie.brianhenry.veintobrain.core.cron;

import java.net.UnknownHostException;

import org.junit.Test;

import com.mongodb.DB;
import com.mongodb.MongoClient;

public class SimulationCalculationsTest {

	@Test
	public void TestDb() throws UnknownHostException {

		MongoClient mongo = new MongoClient("137.43.192.55", 27017);
		DB db = mongo.getDB("mydb");
		
		SimulationCalculations sc = new SimulationCalculations(db);
		
		sc.doJob();
		
	}

}
