package ie.brianhenry.veintobrain.client;

import ie.brianhenry.veintobrain.client.overlay.AnalyteStat;
import ie.brianhenry.veintobrain.client.view.AnalyteMenu;
import ie.brianhenry.veintobrain.client.view.AnalyteView;

import org.moxieapps.gwt.highcharts.client.Chart;
import org.moxieapps.gwt.highcharts.client.Point;
import org.moxieapps.gwt.highcharts.client.Series;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;

public class AppController {

	RpcService rpcService;
	EventBus eventBus;

	HorizontalPanel app = new HorizontalPanel();

	SimplePanel menuContainer = new SimplePanel();
	SimplePanel appContainer = new SimplePanel();

	public AppController(RpcService rpcService, EventBus eventBus) {

		this.rpcService = rpcService;
		this.eventBus = eventBus;

		app.add(menuContainer);
		app.add(appContainer);
	}

	public void go(HasWidgets container) {

		menuContainer.add(new AnalyteMenu(rpcService, eventBus));

		appContainer.add(new AnalyteView(rpcService, eventBus));

		container.add(app);

	}
}
