package ie.brianhenry.veintobrain.client.events;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.web.bindery.event.shared.binder.GenericEvent;

/**
 * AnalyteMenuEvent definition
 * 
 * @author Daniele
 *
 */
public class AnalyteMenuEvent extends GenericEvent {

	private String analyte;
	
	public AnalyteMenuEvent(String analyte) {
		this.analyte = analyte;
	}
	
	public String getAnalyte(){
		return analyte;
	}
	
}