package ie.brianhenry.veintobrain.client.events;

import com.google.web.bindery.event.shared.binder.GenericEvent;

/**
 * TimeRangeMenuEvent definition
 * 
 * @author Daniele
 *
 */
public class TimeRangeMenuEvent extends GenericEvent {

	private String timeRange;
	
	public TimeRangeMenuEvent(String timeRange) {
		this.timeRange = timeRange;
	}
	
	public String getTimeRange(){
		return timeRange;
	}
	
}