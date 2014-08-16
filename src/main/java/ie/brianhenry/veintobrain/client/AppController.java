package ie.brianhenry.veintobrain.client;

import ie.brianhenry.veintobrain.client.events.AnalyteMenuEvent;
import ie.brianhenry.veintobrain.client.events.LoginEvent;
import ie.brianhenry.veintobrain.client.events.MovingAverageMenuEvent;
import ie.brianhenry.veintobrain.client.events.TimeRangeMenuEvent;
import ie.brianhenry.veintobrain.client.resources.VeintobrainResources;
import ie.brianhenry.veintobrain.client.view.AnalyteMenuView;
import ie.brianhenry.veintobrain.client.view.AnalyteView;
import ie.brianhenry.veintobrain.client.view.LoginClientView;
import ie.brianhenry.veintobrain.client.view.MovingAverageMenuView;
import ie.brianhenry.veintobrain.client.view.TableView;
import ie.brianhenry.veintobrain.client.view.TimeRangeMenuView;
import ie.brianhenry.veintobrain.shared.representations.User;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabLayoutPanel;
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
	
	FlowPanel top = new FlowPanel();
	FlowPanel leftFrame = new FlowPanel();
	FlowPanel centerFrame = new FlowPanel();
	FlowPanel rightFrame = new FlowPanel();
	FlowPanel middle = new FlowPanel();
	FlowPanel bottom = new FlowPanel();

	// this line has to be added anytime you use CSS
	VeintobrainResources resources = VeintobrainResources.INSTANCE;

	public AppController(RpcService rpcService, EventBus eventBus) {

		resources.css().ensureInjected();
		
		version.addStyleName(resources.css().version());
		top.addStyleName(resources.css().top());
		middle.addStyleName(resources.css().middle());
		leftFrame.addStyleName(resources.css().menuPanel());
		centerFrame.addStyleName(resources.css().appPanel());
		rightFrame.addStyleName(resources.css().helpPanel());
		bottom.addStyleName(resources.css().bottom());
		copyright.addStyleName(resources.css().copyright());

		this.rpcService = rpcService;
		this.eventBus = eventBus;

		eventBinder.bindEventHandlers(this, eventBus);
//		middle.setSize("1000px", "1000px");
//		middle.setHeight("510px");
		
		middle.add(leftFrame);
		middle.add(centerFrame);
		middle.add(rightFrame);
		
		contentPanel.add(top);
		contentPanel.add(middle);
		contentPanel.add(bottom);
	}

	TabLayoutPanel tab = new TabLayoutPanel(2.5, Unit.EM); // leftFrame
	AnalyteView av = new AnalyteView(rpcService, eventBus); // centerFrame
	TableView tv = new TableView(rpcService, eventBus); // centerFrame
	
	Label version = new Label("Version 1.0");
	Label summaryLab = new Label("Summary:"); // rightFrame
	Label analytesLab = new Label(); // rightFrame
	Label timeRangeLab = new Label(); // rightFrame
	Label movingAverageLab = new Label(); //rightFrame
	FlowPanel statsPanel = new FlowPanel(); //rightFrame
	DisclosurePanel p = new DisclosurePanel("Click to disclose something:");
	Label copyright = new Label("Copyright © 2014. All rights reserved.");

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
		
		top.add(version);

		leftFrame.add(new AnalyteMenuView(rpcService, eventBus));
		leftFrame.add(new TimeRangeMenuView(rpcService, eventBus));
		leftFrame.add(new MovingAverageMenuView(rpcService, eventBus));

//		tab.getElement().getStyle().setMarginBottom(10.0, Unit.PX);
		tab.setAnimationDuration(1000);
		
//		tab.setSize("740px", "480px");
		tab.add(av, "Graph");
//		tab.add(new HTML("that content"), "Table");
		tab.add(tv, "Table");
		centerFrame.add(tab);

		rightFrame.add(summaryLab);
		rightFrame.add(analytesLab);
		rightFrame.add(timeRangeLab);
		rightFrame.add(movingAverageLab);
		rightFrame.add(statsPanel);
		
		bottom.add(copyright);
	}

	@EventHandler
	void OnShow(AnalyteMenuEvent event) {
		av.setAnalyte(event.getAnalyte());
		tv.setAnalyte(event.getAnalyte());
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