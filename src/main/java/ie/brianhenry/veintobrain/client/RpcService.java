package ie.brianhenry.veintobrain.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;

public class RpcService {


	interface MyEventBinder extends EventBinder<RpcService> {
	}

	private final MyEventBinder eventBinder = GWT.create(MyEventBinder.class);


	private EventBus eventBus;


	public RpcService(EventBus eventBus) {

		this.eventBus = eventBus;

		eventBinder.bindEventHandlers(this, eventBus);

	}
}
