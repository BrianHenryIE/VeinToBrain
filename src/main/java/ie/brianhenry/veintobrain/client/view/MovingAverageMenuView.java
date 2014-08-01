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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DatePicker;

public class MovingAverageMenuView implements IsWidget {
	FlowPanel flow = new FlowPanel();
	DisclosurePanel p = new DisclosurePanel("Moving Averages");
	DatePicker date = new DatePicker();

	EventBus eventBus;

	public MovingAverageMenuView(RpcService rpcService, final EventBus eventBus) {

		this.eventBus = eventBus;		

		Label l1 = new Label("Average #1");
		TextBox n1 = new TextBox();
		n1.setSize("20px", "20px");
		Label l2 = new Label("Average #2");
		TextBox n2 = new TextBox();
		flow.add(l1);
		flow.add(n1);
		flow.add(date);
		flow.add(l2);
		flow.add(n2);
		p.setContent(flow);
	}

	@Override
	public Widget asWidget() {
		return p;
	}

}
