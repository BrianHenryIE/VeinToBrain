package ie.brianhenry.veintobrain.client.view;

import ie.brianhenry.veintobrain.client.RpcService;
import ie.brianhenry.veintobrain.client.events.AnalyteMenuEvent;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class AnalyteMenuView implements IsWidget {

	VerticalPanel flow = new VerticalPanel();
	DisclosurePanel p = new DisclosurePanel("Analytes");

	EventBus eventBus;

	public AnalyteMenuView(RpcService rpcService, final EventBus eventBus) {

		this.eventBus = eventBus;
		p.setOpen(true);

		String[] menuItems = { "Folate", "PSA", "B12" };

		for (final String mi : menuItems) {
			RadioButton b = new RadioButton(mi);
			b.setText(mi);
			b.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					eventBus.fireEvent(new AnalyteMenuEvent(mi));
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
