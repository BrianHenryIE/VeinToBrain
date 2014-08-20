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

public class RangeView implements IsWidget {
	
	FlowPanel flow = new FlowPanel();
	HorizontalPanel f1 = new HorizontalPanel();
	HorizontalPanel f2 = new HorizontalPanel();
	HorizontalPanel f3 = new HorizontalPanel();
	HorizontalPanel f4 = new HorizontalPanel();
	DisclosurePanel p = new DisclosurePanel("Reference Range");
	
	TextBox ref049 = new TextBox();
	TextBox ref5059 = new TextBox();
	TextBox ref6069 = new TextBox();
	TextBox ref7099 = new TextBox();

	EventBus eventBus;

	public RangeView(RpcService rpcService, final EventBus eventBus) {
		
		f1.setSpacing(2);
		f2.setSpacing(2);
		f3.setSpacing(2);
		f4.setSpacing(2);
		
		ref049.setSize("60px", "10px");
		ref5059.setSize("60px", "10px");
		ref6069.setSize("60px", "10px");
		ref7099.setSize("60px", "10px");
		
		ref049.setEnabled(false);
		ref5059.setEnabled(false);
		ref6069.setEnabled(false);
		ref7099.setEnabled(false);
		
		Label ref049Lab = new Label("0 – 49 years");
		ref049Lab.setSize("110px", "20px");
		Label ref5059Lab = new Label("50 – 59 years");
		ref5059Lab.setSize("110px", "20px");
		Label ref6069Lab = new Label("60 – 69 years");
		ref6069Lab.setSize("110px", "20px");
		Label ref7099Lab = new Label ("70 – 99 years");
		ref7099Lab.setSize("110px", "20px");

		this.eventBus = eventBus;
		p.setOpen(true);
		
		f1.add(ref049Lab);
		f1.add(ref049);
		f2.add(ref5059Lab);
		f2.add(ref5059);
		f3.add(ref6069Lab);
		f3.add(ref6069);
		f4.add(ref7099Lab);
		f4.add(ref7099);
		
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
	
	public void setRange(String analyte) {
		//TODO set different ranges based on the analyte
		ref049.setText("0-2.5 ug/L");
		ref5059.setText("0-3.5 ug/L");
		ref6069.setText("0-4.5 ug/L");
		ref7099.setText("0-6.5 ug/L");
	}

}
