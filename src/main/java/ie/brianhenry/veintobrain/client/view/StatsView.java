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
	HorizontalPanel f5 = new HorizontalPanel();
	DisclosurePanel p = new DisclosurePanel("Statistics");

	TextBox mean2 = new TextBox();
	TextBox median2 = new TextBox();
	TextBox sd2 = new TextBox();
	TextBox var2 = new TextBox();
	TextBox avgTests2 = new TextBox();
	
	EventBus eventBus;

	public StatsView(RpcService rpcService, final EventBus eventBus) {
		
		f1.setSpacing(2);
		f2.setSpacing(2);
		f3.setSpacing(2);
		f4.setSpacing(2);
		f5.setSpacing(2);
		
		mean2.setSize("70px", "10px");
		median2.setSize("70px", "10px");
		sd2.setSize("70px", "10px");
		avgTests2.setSize("70px", "10px");
		var2.setSize("70px", "10px");
		
		mean2.setEnabled(false);
		median2.setEnabled(false);
		sd2.setEnabled(false);
		avgTests2.setEnabled(false);
		var2.setEnabled(false);
		
		Label meanLab = new Label("Mean");
		meanLab.setSize("146px", "20px");
		Label medianLab = new Label("Median");
		medianLab.setSize("146px", "20px");
		Label sdLab = new Label("Standard deviation");
		sdLab.setSize("146px", "20px");
		Label varLab = new Label ("Variance");
		varLab.setSize("146px", "20px");
		Label avgTestsLab = new Label ("Average daily tests");
		avgTestsLab.setSize("146px", "20px");

		this.eventBus = eventBus;
		p.setOpen(true);
		
		f1.add(meanLab);
		f1.add(mean2);
		f2.add(medianLab);
		f2.add(median2);
		f3.add(sdLab);
		f3.add(sd2);
		f4.add(varLab);
		f4.add(var2);
		f5.add(avgTestsLab);
		f5.add(avgTests2);
		
		flow.add(f1);
		flow.add(f2);
		flow.add(f3);
		flow.add(f4);
		flow.add(f5);

		p.setContent(flow);
	}

	@Override
	public Widget asWidget() {
		return p;
	}
	
	public void setStats(String mean, String median, String sd, String var, String avgTests) {
		mean2.setText(mean+" ug/L");
		median2.setText(median+" ug/L");
		sd2.setText(sd+" ug/L");
		var2.setText(var+" ug/L");
		avgTests2.setText(avgTests);
	}

}
