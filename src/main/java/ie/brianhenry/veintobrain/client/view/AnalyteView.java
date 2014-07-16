package ie.brianhenry.veintobrain.client.view;

import ie.brianhenry.veintobrain.client.RpcService;
import ie.brianhenry.veintobrain.client.overlay.AnalyteStat;

import org.moxieapps.gwt.highcharts.client.Chart;
import org.moxieapps.gwt.highcharts.client.Point;
import org.moxieapps.gwt.highcharts.client.Series;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public class AnalyteView implements IsWidget {

	FlowPanel p = new FlowPanel();
	private RpcService rpcService;
	Chart chart;
	Series series;
	
	public AnalyteView(RpcService rpcService, EventBus eventBus) {
		this.rpcService = rpcService;
		setChart();
		
		// if no analyte set.. display a message of some sort... maybe a waiting
		// animation
	}

	@Override
	public Widget asWidget() {
		return p;
	}

	private void setChart() {
		chart = new Chart();

		chart.setType(Series.Type.BOXPLOT);
		chart.setSize("800px", "500px");

		chart.setCredits(null);
		

	}

	public void setAnalyte(final String analyte) {

		rpcService.executeRequest(analyte,
				new AsyncCallback<JsArray<AnalyteStat>>() {
					public void onFailure(Throwable caught) {

					}

					public void onSuccess(JsArray<AnalyteStat> result) {

						setChart();
						
						series = chart.createSeries();

						chart.addSeries(series);

						series.setName(analyte);

						for (int i = 0; i < result.length(); i++)
							series.addPoint(new Point(result.get(i)
									.getPercentile(0.025), result.get(i)
									.getPercentile(0.25), result.get(i)
									.getPercentile(0.5), result.get(i)
									.getPercentile(0.75), result.get(i)
									.getPercentile(0.975)));

						p.clear();
						p.add(chart);

					}
				});
	}

}
