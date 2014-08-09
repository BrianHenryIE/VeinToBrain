package ie.brianhenry.veintobrain.client.view;

import ie.brianhenry.veintobrain.client.RpcService;
import ie.brianhenry.veintobrain.client.events.TimeRangeMenuEvent;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public class TimeRangeMenuView implements IsWidget {
	
	FlowPanel flow = new FlowPanel();
	DisclosurePanel p = new DisclosurePanel("Time Range");

	EventBus eventBus;

	public TimeRangeMenuView(RpcService rpcService, final EventBus eventBus) {

		this.eventBus = eventBus;		

		String[] menuItems = { "daily", "monthly", "yearly" };

		for (final String mi : menuItems){
			Button b = new Button(mi);
			b.setSize("90px", "30px");
			b.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					eventBus.fireEvent(new TimeRangeMenuEvent(mi));
				}
			});
			flow.add(b);
		}
		p.setContent(flow);
	}

	@Override
	public Widget asWidget() {
		return p;
	}

}
