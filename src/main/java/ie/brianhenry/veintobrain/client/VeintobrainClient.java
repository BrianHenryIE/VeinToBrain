package ie.brianhenry.veintobrain.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class VeintobrainClient implements EntryPoint {

	public void onModuleLoad() {

		SimpleEventBus eventBus = new SimpleEventBus();
		RpcService rpcService = new RpcService(eventBus);
		AppController appViewer = new AppController(rpcService, eventBus);
		appViewer.go(RootPanel.get("gwt"));

	}

}