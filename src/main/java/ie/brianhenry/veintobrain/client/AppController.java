package ie.brianhenry.veintobrain.client;

import ie.brianhenry.veintobrain.client.events.MenuEvent;
import ie.brianhenry.veintobrain.client.view.AnalyteMenuView;
import ie.brianhenry.veintobrain.client.view.AnalyteView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

public class AppController {

	RpcService rpcService;
	EventBus eventBus;

	HorizontalPanel app = new HorizontalPanel();

	SimplePanel menuContainer = new SimplePanel();
	SimplePanel appContainer = new SimplePanel();

	interface MyEventBinder extends EventBinder<AppController> {
	}

	private final MyEventBinder eventBinder = GWT.create(MyEventBinder.class);

	public AppController(RpcService rpcService, EventBus eventBus) {

		this.rpcService = rpcService;
		this.eventBus = eventBus;

		eventBinder.bindEventHandlers(this, eventBus);
		
		app.add(menuContainer);
		app.add(appContainer);
	}

	public void go(HasWidgets container) {

		menuContainer.add(new AnalyteMenuView(rpcService, eventBus));

		AnalyteView av = new AnalyteView(rpcService, eventBus);
		appContainer.add(av);

		av.setAnalyte("folate");
		
		
		container.add(app);

	}

	@EventHandler
	void newEvents(MenuEvent event) {
		// Something was clicked... what was it!?
		
		
		Window.alert(event.getName());
	}
}
