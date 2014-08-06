package ie.brianhenry.veintobrain.client;

import ie.brianhenry.veintobrain.client.events.LoginEvent;
import ie.brianhenry.veintobrain.client.events.AnalyteMenuEvent;
import ie.brianhenry.veintobrain.client.events.MovingAverageMenuEvent;
import ie.brianhenry.veintobrain.client.events.TimeRangeMenuEvent;
import ie.brianhenry.veintobrain.client.resources.VeintobrainResources;
import ie.brianhenry.veintobrain.client.view.AnalyteMenuView;
import ie.brianhenry.veintobrain.client.view.AnalyteView;
import ie.brianhenry.veintobrain.client.view.LoginClientView;
import ie.brianhenry.veintobrain.client.view.MovingAverageMenuView;
import ie.brianhenry.veintobrain.client.view.TimeRangeMenuView;
import ie.brianhenry.veintobrain.representations.User;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

public class AppController {

	/**
	 * Interface which allows to register the event handlers
	 * 
	 * @author Daniele
	 *
	 */
	interface MenuEventBinder extends EventBinder<AppController> {
	}

	private final MenuEventBinder eventBinder = GWT.create(MenuEventBinder.class);

	/**
	 * Client side of the client/server communication. Implements all the
	 * methods needed and translate JSON instructions to Java.
	 */
	RpcService rpcService;
	/**
	 * The "pipe" which captures all the events as the application runs.
	 */
	EventBus eventBus;

	private final FlowPanel contentPanel = new FlowPanel();

	FlowPanel leftFrame = new FlowPanel();
	SimplePanel centerFrame = new SimplePanel();
	FlowPanel rightFrame = new FlowPanel();

	// this line has to be added anytime you use CSS
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

	TabLayoutPanel tab = new TabLayoutPanel(1.0, Unit.EM); // leftFrame
	AnalyteView av = new AnalyteView(rpcService, eventBus); // centerFrame
	
	Label summaryLab = new Label("Summary:"); // rightFrame
	Label analytesLab = new Label(); // rightFrame
	Label timeRangeLab = new Label(); // rightFrame
	Label movingAverageLab = new Label(); //rightFrame
	FlowPanel statsPanel = new FlowPanel(); //rightFrame
	DisclosurePanel p = new DisclosurePanel("Click to disclose something:");

	public void go(HasWidgets container) {

		leftFrame.add(new LoginClientView(rpcService, eventBus));

		container.add(contentPanel);

		loggedIn(new LoginEvent(new User("sad", "secret")));
	}

	@EventHandler
	void loggedIn(LoginEvent event) {
		leftFrame.clear();
		centerFrame.clear();
		rightFrame.clear();

		leftFrame.add(new AnalyteMenuView(rpcService, eventBus));
		leftFrame.add(new TimeRangeMenuView(rpcService, eventBus));
		leftFrame.add(new MovingAverageMenuView(rpcService, eventBus));

		tab.setSize("800px", "550px");
		tab.add(av, "Graph");
		tab.add(new HTML("that content"), "Table");
		centerFrame.add(tab);

		rightFrame.add(summaryLab);
		rightFrame.add(analytesLab);
		rightFrame.add(timeRangeLab);
		rightFrame.add(movingAverageLab);
		rightFrame.add(statsPanel);
	}

	@EventHandler
	void OnShow(AnalyteMenuEvent event) {
		av.setAnalyte("folate");
		analytesLab.setText("Analyte: "+event.getAnalyte());
		statsPanel.clear();
		statsPanel.add(new Label("Long term mean:"));
		statsPanel.add(new Label("Reference interval:"));
		statsPanel.add(new Label("Average number of daily tests:"));
		statsPanel.add(new Label("Representative CV for stable periods:"));
	}
	@EventHandler
	void OnShow(TimeRangeMenuEvent event) {
		timeRangeLab.setText("Time Range: "+event.getTimeRange());
	}
	@EventHandler
	void OnShow(MovingAverageMenuEvent event) {
		movingAverageLab.setText("Moving Average: "+event.getMovingAverage());
	}

}
