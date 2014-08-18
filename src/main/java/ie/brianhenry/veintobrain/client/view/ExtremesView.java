package ie.brianhenry.veintobrain.client.view;

import ie.brianhenry.veintobrain.client.RpcService;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class ExtremesView implements IsWidget {
	
	FlowPanel flow = new FlowPanel();
	HorizontalPanel f1 = new HorizontalPanel();
	HorizontalPanel f2 = new HorizontalPanel();
	HorizontalPanel f3 = new HorizontalPanel();
	HorizontalPanel f4 = new HorizontalPanel();
	HorizontalPanel f5 = new HorizontalPanel();
	HorizontalPanel f6 = new HorizontalPanel();
	HorizontalPanel f7 = new HorizontalPanel();
	HorizontalPanel f8 = new HorizontalPanel();
	DisclosurePanel p = new DisclosurePanel("Extreme values");

	TextBox max = new TextBox();
	TextBox min = new TextBox();
	TextBox maxDiff750mean = new TextBox();
	TextBox minDiff750mean = new TextBox();
	TextBox maxDiff750median = new TextBox();
	TextBox minDiff750median = new TextBox();
	
	EventBus eventBus;

	public ExtremesView(RpcService rpcService, final EventBus eventBus) {
		
		f1.setSpacing(2);
		f2.setSpacing(2);
		f3.setSpacing(2);
		f4.setSpacing(2);
		f5.setSpacing(2);
		f6.setSpacing(2);
		f7.setSpacing(2);
		f8.setSpacing(2);
		
		max.setSize("40px", "10px");
		min.setSize("40px", "10px");
		maxDiff750mean.setSize("40px", "10px");
		minDiff750mean.setSize("40px", "10px");
		maxDiff750median.setSize("40px", "10px");
		minDiff750median.setSize("40px", "10px");
		
		Label maxLab = new Label("Maximum");
		maxLab.setSize("176px", "20px");
		Label minLab = new Label("Minimum");
		minLab.setSize("176px", "20px");
		Label meanLab = new Label ("Difference between means");
		Label maxDiff750meanLab = new Label("Max (7-50 day)");
		maxDiff750meanLab.setSize("176px", "20px");
		Label minDiff750meanLab = new Label("Min (7-50 day)");
		minDiff750meanLab.setSize("176px", "20px");
		Label medianLab = new Label ("Difference between medians");
		Label maxDiff750medianLab = new Label("Max (7-50 day)");
		maxDiff750medianLab.setSize("176px", "20px");
		Label minDiff750medianLab = new Label("Min (7-50 day)");
		minDiff750medianLab.setSize("176px", "20px");

		this.eventBus = eventBus;
		p.setOpen(true);
		
		f1.add(maxLab);
		f1.add(max);
		f2.add(minLab);
		f2.add(min);
		f3.add(meanLab);
		f4.add(maxDiff750meanLab);
		f4.add(maxDiff750mean);
		f5.add(minDiff750meanLab);
		f5.add(minDiff750mean);
		f6.add(medianLab);
		f7.add(maxDiff750medianLab);
		f7.add(maxDiff750median);
		f8.add(minDiff750medianLab);
		f8.add(minDiff750median);
		
		flow.add(f1);
		flow.add(f2);
		flow.add(f3);
		flow.add(f4);
		flow.add(f5);
		flow.add(f6);
		flow.add(f7);
		flow.add(f8);

		p.setContent(flow);
	}

	@Override
	public Widget asWidget() {
		return p;
	}

}
