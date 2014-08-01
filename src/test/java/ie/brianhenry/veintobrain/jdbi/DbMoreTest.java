package ie.brianhenry.veintobrain.jdbi;

import ie.brianhenry.veintobrain.representations.AnalyteStat;
import ie.brianhenry.veintobrain.representations.AnalyteStat.StatPeriod;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import net.vz.mongodb.jackson.DBCursor;
import net.vz.mongodb.jackson.JacksonDBCollection;

import org.junit.Test;

import com.mongodb.DB;
import com.mongodb.MongoClient;

public class DbMoreTest {

	@Test
	public void TestDb() throws UnknownHostException {

		MongoClient mongo = new MongoClient("137.43.192.55", 27017);
		DB db = mongo.getDB("mydb");

		JacksonDBCollection<AnalyteStat, String> analyteStats = JacksonDBCollection.wrap(db.getCollection("analytestat"),
				AnalyteStat.class, String.class);

		DBCursor<AnalyteStat> dbCursor = analyteStats.find().is("analyteType", "psa").is("analytePeriod", StatPeriod.DAY);
		List<AnalyteStat> stats = new ArrayList<AnalyteStat>();
		while (dbCursor.hasNext()) {
			AnalyteStat stat = dbCursor.next();
			stats.add(stat);
		}

		System.out.println(stats.size() + " records retrieved");
	}

}
