package ie.brianhenry.veintobrain.client.events;

import com.google.web.bindery.event.shared.binder.GenericEvent;

public class MenuEvent extends GenericEvent {

	private final String name;

	public MenuEvent(String name) {
		this.name = name;
	}
	
	public String getName(){
		return name;
	}

}