package ie.brianhenry.veintobrain.server.core.cron;

import java.util.List;

import javax.inject.Inject;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;

import de.spinscale.dropwizard.jobs.Job;
import de.spinscale.dropwizard.jobs.annotations.OnApplicationStart;

/**
 * This defines the database indexes and runs on application start to ensure
 * they are set
 * 
 * @author BrianHenry.ie
 * @see https://github.com/spinscale/dropwizard-jobs
 * @see http
 *      ://quartz-scheduler.org/documentation/quartz-2.2.x/tutorials/tutorial
 *      -lesson-06
 */
@OnApplicationStart
public class DbConfig extends Job {

	private static final Logger logger = LoggerFactory.getLogger(DbConfig.class);

	private DB db;

	@Inject
	public DbConfig(DB db) {
		this.db = db;
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		super.execute(context);
	}

	@Override
	public void doJob() {

		BasicDBObject indexObj = new BasicDBObject();
		indexObj.put("analyteType", 1);
		indexObj.put("date", -1); // We'll be searching for the most recent date
									// usually

		db.getCollection("analyteresult").createIndex(indexObj);

		for (String dbc : db.getCollectionNames()) {

			List<DBObject> list = db.getCollection(dbc).getIndexInfo();

			for (DBObject o : list)
				logger.info(o.toString());
		}
	}
}