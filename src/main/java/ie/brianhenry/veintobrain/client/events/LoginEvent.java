package ie.brianhenry.veintobrain.client.events;

import ie.brianhenry.veintobrain.shared.User;

import com.google.web.bindery.event.shared.binder.GenericEvent;

public class LoginEvent extends GenericEvent {

	private final User user;

	public LoginEvent(User user) {
		this.user = user;
	}
	
	public User getUser(){
		return user;
	}

}