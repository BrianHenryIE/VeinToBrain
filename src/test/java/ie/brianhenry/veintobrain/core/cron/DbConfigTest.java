package ie.brianhenry.veintobrain.core.cron;

import java.net.UnknownHostException;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;
import org.mockito.ArgumentMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Test;

import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mongodb.DB;
import com.mongodb.MongoClient;

// This was for TDD rather than unit testing
public class DbConfigTest {

	private static Logger logger = LoggerFactory.getLogger(DbConfigTest.class);

	@Test
	public void TestDb() throws UnknownHostException {

		ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory
				.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
		final Appender mockAppender = mock(Appender.class);
		when(mockAppender.getName()).thenReturn("MOCK");
		root.addAppender(mockAppender);

		MongoClient mongo = new MongoClient("137.43.192.55", 27017);
		DB db = mongo.getDB("mydb");

		DbConfig dbc = new DbConfig(db);

		// dbc.doJob();
		//
		// verify(mockAppender).doAppend(argThat(new ArgumentMatcher() {
		// @Override
		// public boolean matches(final Object argument) {
		// return ((LoggingEvent)
		// argument).getFormattedMessage().contains("Hey this is the message I want to see");
		// }
		// }));
	}

}
