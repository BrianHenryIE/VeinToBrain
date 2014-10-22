package ie.brianhenry.veintobrain.client;

import ie.brianhenry.veintobrain.client.events.LoginEvent;
import ie.brianhenry.veintobrain.client.events.TimeRangeMenuEvent;
import ie.brianhenry.veintobrain.client.resources.VeintobrainResources;
import ie.brianhenry.veintobrain.client.view.AnalyteMeanView;
import ie.brianhenry.veintobrain.client.view.AnalyteMedianView;
import ie.brianhenry.veintobrain.client.view.AnalyteMenuView;
import ie.brianhenry.veintobrain.client.view.ExtremesView;
import ie.brianhenry.veintobrain.client.view.FileUploadView;
import ie.brianhenry.veintobrain.client.view.LoginClientView;
import ie.brianhenry.veintobrain.client.view.OptionMenuView;
import ie.brianhenry.veintobrain.client.view.RangeView;
import ie.brianhenry.veintobrain.client.view.StatsView;
import ie.brianhenry.veintobrain.client.view.TableView;
import ie.brianhenry.veintobrain.shared.representations.AnalyteStat;
import ie.brianhenry.veintobrain.shared.representations.AnalyteStat.StatPeriod;
import ie.brianhenry.veintobrain.shared.representations.User;

import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
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

	FlowPanel top = new FlowPanel();
	FlowPanel leftFrame = new FlowPanel();
	FlowPanel centerFrame = new FlowPanel();
	FlowPanel rightFrame = new FlowPanel();
	FlowPanel middle = new FlowPanel();
	FlowPanel bottom = new FlowPanel();
	
	Button plot = new Button("Plot");
	Button upload = new Button("Upload Data");;

	// this line has to be added anytime you use CSS
	VeintobrainResources resources = VeintobrainResources.INSTANCE;

	public AppController(RpcService rpcService, EventBus eventBus) {

		resources.css().ensureInjected();

		summaryLab.addStyleName(resources.css().summaryLab());
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

		middle.add(leftFrame);
		middle.add(centerFrame);
		middle.add(rightFrame);

		contentPanel.add(top);
		contentPanel.add(middle);
		contentPanel.add(bottom);

		upload.addStyleName(resources.css().uploadButton());
		
		FileUploadView uploadPanel = new FileUploadView();
		final PopupPanel uploadPopup = new PopupPanel();
		uploadPopup.add(uploadPanel);
		Button uploadButton = new Button("upload");
		uploadButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				uploadPopup.center();
				uploadPopup.show();
			}
		});
		
		uploadPanel.getCloseButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				uploadPopup.hide();
			}
		});
		
		//leftFrame.add(uploadButton);
	}

	TabLayoutPanel tab = new TabLayoutPanel(2.5, Unit.EM); // centerFrame
	AnalyteMeanView avMean = new AnalyteMeanView(rpcService, eventBus); // centerFrame
	AnalyteMedianView avMedian = new AnalyteMedianView(rpcService, eventBus); // centerFrame
	TableView tv = new TableView(rpcService, eventBus); // centerFrame

	// TODO
	AnalyteMenuView amv = new AnalyteMenuView(rpcService, eventBus);
	RangeView rv = new RangeView(rpcService, eventBus);
	StatsView sv = new StatsView(rpcService, eventBus);
	ExtremesView ev = new ExtremesView(rpcService, eventBus);

	Label version = new Label("Version 1.0");
	Label summaryLab = new Label("Summary"); // rightFrame
	Label timeRangeLab = new Label(); // rightFrame
	// FlowPanel statsPanel = new FlowPanel(); // rightFrame
	DisclosurePanel p = new DisclosurePanel("Click to disclose something:");
	Label copyright = new Label("Copyright © 2014. All rights reserved.");

	public void go(HasWidgets container) {

		//leftFrame.add(new LoginClientView(rpcService, eventBus));

		container.add(contentPanel);

		loggedIn(new LoginEvent(new User("sad", "secret")));
	}

	@EventHandler
	void loggedIn(LoginEvent event) {
		//leftFrame.clear();
		centerFrame.clear();
		rightFrame.clear();

		plot.setSize("150px", "40px");
		// plot.setEnabled(false);
		plot.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				GWT.log("appcontroller logged in");
				rpcService.getAnalyte(amv.getActiveButton(), StatPeriod.DAY, new AsyncCallback<List<AnalyteStat>>() {
					public void onFailure(Throwable caught) {
					}

					public void onSuccess(List<AnalyteStat> result) {

						enableAllChildren(true, amv.getFlow());
						avMean.setAnalyte(result);
						avMedian.setAnalyte(result);
						tv.setChart(amv.getActiveButton(), result);
						// statsPanel.clear();
						rv.setRange(amv.getActiveButton());
						sv.setStats(avMean.getOverallMean(amv.getActiveButton()), avMedian.getOverallMedian(amv.getActiveButton()),
								avMean.standardDev(amv.getActiveButton()), avMean.variance(amv.getActiveButton()),
								avMean.avgDailyTests(amv.getActiveButton()));
						ev.setExtremes(avMean.maxValue(amv.getActiveButton()), avMean.minValue(amv.getActiveButton()),
								avMean.getMaxDiff750s(), avMean.getMinDiff750s(), avMedian.getMaxDiff750s(), avMedian.getMinDiff750s());
					}

				});

			}
		});

		top.add(version);

		// leftFrame.add(new AnalyteMenuView(rpcService, eventBus));
		leftFrame.add(amv);
		leftFrame.add(new OptionMenuView(rpcService, eventBus));
		// leftFrame.add(new RangeView(rpcService, eventBus));
		leftFrame.add(rv);
		leftFrame.add(plot);
		tab.setAnimationDuration(1000);

		// tab.setSize("740px", "480px");
		tab.add(avMean, "Moving means");
		tab.add(avMedian, "Moving medians");
		tab.add(tv, "Table");

		centerFrame.add(tab);

		rightFrame.add(summaryLab);
		// rightFrame.add(new StatsView(rpcService, eventBus));
		rightFrame.add(sv);
		// rightFrame.add(new ExtremesView(rpcService, eventBus));
		rightFrame.add(ev);

		rightFrame.add(timeRangeLab);
		// rightFrame.add(statsPanel);

		bottom.add(copyright);
	}

	// @EventHandler
	// void OnShow(AnalyteMenuEvent event) {
	// GWT.log("SFASDFASDADS");
	// plot.setEnabled(true);
	// }

	@EventHandler
	void OnShow(TimeRangeMenuEvent event) {
		timeRangeLab.setText("Time Range: " + event.getTimeRange());
	}

	private void enableAllChildren(boolean enable, Widget widget) {
		if (widget instanceof HasWidgets) {
			Iterator<Widget> iter = ((HasWidgets) widget).iterator();
			while (iter.hasNext()) {
				Widget nextWidget = iter.next();
				enableAllChildren(enable, nextWidget);
				if (nextWidget instanceof FocusWidget) {
					((FocusWidget) nextWidget).setEnabled(enable);
				}
			}
		}
	}

}