package ie.brianhenry.veintobrain.client.events;

import com.google.web.bindery.event.shared.binder.GenericEvent;

/**
 * MovingAverageMenuEvent definition
 * 
 * @author Daniele
 *
 */
public class MovingAverageMenuEvent extends GenericEvent {

	private String movingAverage;
	
	public MovingAverageMenuEvent(String movingAverage) {
		this.movingAverage = movingAverage;
	}
	
	public String getMovingAverage(){
		return movingAverage;
	}
	
}