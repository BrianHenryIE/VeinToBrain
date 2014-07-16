package ie.brianhenry.veintobrain.client.view;

import ie.brianhenry.veintobrain.client.RpcService;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class AnalyteMenu implements IsWidget {

	FlowPanel p = new FlowPanel();
	
	public AnalyteMenu(RpcService rpcService, EventBus eventBus) {
		p.add(new Label("Folate"));
		p.add(new Label("NA"));
		p.add(new Label("CO3"));
		p.add(new Label("Urea"));
		p.add(new Label("Test"));
		p.add(new Label("More..."));
	}

	@Override
	public Widget asWidget() {
		return p;
	}

}
