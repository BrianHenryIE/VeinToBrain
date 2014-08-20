package ie.brianhenry.veintobrain.client.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

import ie.brianhenry.veintobrain.client.RpcService;
import ie.brianhenry.veintobrain.client.events.AnalyteMenuEvent;
import ie.brianhenry.veintobrain.shared.representations.AnalyteStat;
import ie.brianhenry.veintobrain.shared.representations.AnalyteStat.StatPeriod;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class AnalyteMenuView implements IsWidget {

	VerticalPanel flow = new VerticalPanel();
	DisclosurePanel p = new DisclosurePanel("Analytes");
	String active = new String();

	EventBus eventBus;

	
	public AnalyteMenuView(final RpcService rpcService, final EventBus eventBus) {

		
		this.eventBus = eventBus;
		p.setOpen(true);

		String[] menuItems = { "Folate", "PSA", "B12" };
		
		String group = "group";
		
		for (final String mi : menuItems) {
			//in order to allow one selected radio button at one time
			//we add all the buttons to the same group (variable "group")
			RadioButton b = new RadioButton(group);
			b.setText(mi);
			b.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
//					eventBus.fireEvent(new AnalyteMenuEvent(mi));
					setActiveButton(mi);
					enableAllChildren(false, flow);
					GWT.log("should now get analyte from server");
					
//					rpcService.getAnalyte(mi.toLowerCase(), StatPeriod.DAY, new AsyncCallback<List<AnalyteStat>>() {
//						public void onFailure(Throwable caught) {
//						}
//
//						public void onSuccess(List<AnalyteStat> result) {
//							// Nothing... we're just prefetching 
//							GWT.log("Analyte prefetched");
//						}
//					});
					
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
	
	private void enableAllChildren(boolean enable, Widget widget)
	{
	    if (widget instanceof HasWidgets)
	    {
	        Iterator<Widget> iter = ((HasWidgets)widget).iterator();
	        while (iter.hasNext())
	        {
	            Widget nextWidget = iter.next();
	            enableAllChildren(enable, nextWidget);
	            if (nextWidget instanceof FocusWidget)
	            {
	                ((FocusWidget)nextWidget).setEnabled(enable);
	            }
	        }
	    }
	}

	public String getActiveButton() {
		return active;
	}
	
	public void setActiveButton(String name) {
		active = name;
	}
	
	public VerticalPanel getFlow() {
		return flow;
	}

}
