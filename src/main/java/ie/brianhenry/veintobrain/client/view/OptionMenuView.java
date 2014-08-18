package ie.brianhenry.veintobrain.client.view;

import ie.brianhenry.veintobrain.client.RpcService;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class OptionMenuView implements IsWidget {
	
	VerticalPanel flow = new VerticalPanel();
	HorizontalPanel test = new HorizontalPanel();
	DisclosurePanel p = new DisclosurePanel("Options");

	EventBus eventBus;

	public OptionMenuView(RpcService rpcService, final EventBus eventBus) {

		this.eventBus = eventBus;
		p.setOpen(true);

		Label exclude = new Label("Exclude:");
		CheckBox weekends = new CheckBox();
		CheckBox holidays = new CheckBox();
		CheckBox sd = new CheckBox();
		ListBox minTest = new ListBox();
		Label minTestLab = new Label("Minimum Tests: ");
		
		weekends.setText("Weekends");
		weekends.setChecked(true);
		weekends.setEnabled(false);
		holidays.setText("Holidays (IRL)");
		holidays.setChecked(true);
		holidays.setEnabled(false);
		sd.setText("> 4 SD (standard deviantion)");
		sd.setEnabled(false);
		minTest.addItem("12");
//		minTest.addItem("20");
//		minTest.addItem("30");
//		minTest.addItem("40");
		test.add(minTestLab);
		test.add(minTest);
		
		flow.add(test);
		flow.add(exclude);
		flow.add(weekends);
		flow.add(holidays);
		flow.add(sd);
		
		p.setContent(flow);
	}

	@Override
	public Widget asWidget() {
		return p;
	}

}
