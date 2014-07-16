package ie.brianhenry.veintobrain.client.view;

import ie.brianhenry.veintobrain.client.RpcService;
import ie.brianhenry.veintobrain.client.events.MenuEvent;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class AnalyteMenuView implements IsWidget {

	FlowPanel p = new FlowPanel();

	EventBus eventBus;

	public AnalyteMenuView(RpcService rpcService, EventBus eventBus) {

		this.eventBus = eventBus;

		String[] menuItems = { "folate", "psa", "test", "CO3" };

		for (String mi : menuItems)
			p.add(new MenuItem(mi));

	}

	@Override
	public Widget asWidget() {
		return p;
	}

	
	//TODO should just use buttons
	class MenuItem extends Composite {

		Label p = new Label();

		MenuItem(final String name) {

			p.setText(name);
			p.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					eventBus.fireEvent(new MenuEvent(name));
				}
			});
			
			initWidget(p);
		}

	}
}
