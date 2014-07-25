package ie.brianhenry.veintobrain.client;

import ie.brianhenry.veintobrain.client.events.LoginEvent;
import ie.brianhenry.veintobrain.client.events.MenuEvent;
import ie.brianhenry.veintobrain.client.resources.VeintobrainResources;
import ie.brianhenry.veintobrain.client.view.AnalyteMenuView;
import ie.brianhenry.veintobrain.client.view.AnalyteView;
import ie.brianhenry.veintobrain.client.view.LoginClientView;
import ie.brianhenry.veintobrain.representations.User;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

public class AppController {

	/**
	 * Client side of the client/server communication. Implements
	 * all the methods needed and translate JSON instructions to Java.
	 */
	RpcService rpcService;
	/**
	 * The "pipe" which captures all the events during the run of the
	 * application.
	 */
	EventBus eventBus;

	private final FlowPanel contentPanel = new FlowPanel();

	SimplePanel leftFrame = new SimplePanel();
	SimplePanel centerFrame = new SimplePanel(); 
	SimplePanel rightFrame = new SimplePanel();

	interface MyEventBinder extends EventBinder<AppController> {
	}

	private final MyEventBinder eventBinder = GWT.create(MyEventBinder.class);

	//this line has to be added anytime you use CSS
	VeintobrainResources resources = VeintobrainResources.INSTANCE;

	public AppController(RpcService rpcService, EventBus eventBus) {

		resources.css().ensureInjected();

		leftFrame.addStyleName(resources.css().menuPanel());
		centerFrame.addStyleName(resources.css().appPanel());
		rightFrame.addStyleName(resources.css().helpPanel());

		this.rpcService = rpcService;
		this.eventBus = eventBus;

		eventBinder.bindEventHandlers(this, eventBus);

		contentPanel.add(leftFrame);
		contentPanel.add(centerFrame);
		contentPanel.add(rightFrame);
	}
	
	TabLayoutPanel tab = new TabLayoutPanel(1.0, Unit.EM); //leftFrame
	AnalyteView av = new AnalyteView(rpcService, eventBus); //centerFrame
	Label lab = new Label("Summary:"); //rightFrame
	DisclosurePanel p = new DisclosurePanel();
	
	public void go(HasWidgets container) {

		leftFrame.add(new LoginClientView(rpcService, eventBus));

		container.add(contentPanel);
		
		loggedIn(new LoginEvent(new User("sad","secret")));
	}
		
	@EventHandler
	void loggedIn(LoginEvent event){
		leftFrame.clear();
		centerFrame.clear();
		rightFrame.clear();
		
		leftFrame.add(new AnalyteMenuView(rpcService, eventBus));
		
		av.setAnalyte("folate");
		tab.setSize("800px", "550px");
		tab.add(av, "Graph");
		tab.add(new HTML("that content"), "Table");
		centerFrame.add(tab);
		
		rightFrame.add(lab);
	
	}

	@EventHandler
	void newEvents(MenuEvent event) {
		// Something was clicked... what was it!?
		av.setAnalyte(event.getName());
		
	}
}
