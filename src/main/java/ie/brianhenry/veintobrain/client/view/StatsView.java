package ie.brianhenry.veintobrain.client.view;

import ie.brianhenry.veintobrain.client.RpcService;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class StatsView implements IsWidget {
	
	FlowPanel flow = new FlowPanel();
	HorizontalPanel f1 = new HorizontalPanel();
	HorizontalPanel f2 = new HorizontalPanel();
	HorizontalPanel f3 = new HorizontalPanel();
	HorizontalPanel f4 = new HorizontalPanel();
	DisclosurePanel p = new DisclosurePanel("Statistics");

	TextBox mean = new TextBox();
	TextBox median = new TextBox();
	TextBox sd = new TextBox();
	TextBox avgTests = new TextBox();
	
	EventBus eventBus;

	public StatsView(RpcService rpcService, final EventBus eventBus) {
		
		f1.setSpacing(2);
		f2.setSpacing(2);
		f3.setSpacing(2);
		f4.setSpacing(2);
		
		mean.setSize("40px", "10px");
		median.setSize("40px", "10px");
		sd.setSize("40px", "10px");
		avgTests.setSize("40px", "10px");
		
		Label meanLab = new Label("Mean");
		meanLab.setSize("176px", "20px");
		Label medianLab = new Label("Median");
		medianLab.setSize("176px", "20px");
		Label sdLab = new Label("Standard deviation");
		sdLab.setSize("176px", "20px");
		Label avgTestsLab = new Label ("Average daily tests");
		avgTestsLab.setSize("176px", "20px");

		this.eventBus = eventBus;
		p.setOpen(true);
		
		f1.add(meanLab);
		f1.add(mean);
		f2.add(medianLab);
		f2.add(median);
		f3.add(sdLab);
		f3.add(sd);
		f4.add(avgTestsLab);
		f4.add(avgTests);
		
		flow.add(f1);
		flow.add(f2);
		flow.add(f3);
		flow.add(f4);

		p.setContent(flow);
	}

	@Override
	public Widget asWidget() {
		return p;
	}

}
