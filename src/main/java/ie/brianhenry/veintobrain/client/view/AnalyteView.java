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
	
	public AnalyteView(RpcService rpcService, EventBus eventBus) {

		rpcService.executeRequest("a request object",
				new AsyncCallback<JsArray<AnalyteStat>>() {
					public void onFailure(Throwable caught) {

					}

					public void onSuccess(JsArray<AnalyteStat> result) {

						Chart chart = new Chart();
						chart.setType(Series.Type.BOXPLOT);
						chart.setSize("800px", "500px");

						chart.setCredits(null);

						Series series = chart.createSeries();
						for (int i = 0; i < result.length(); i++)
							series.addPoint(new Point(result.get(i).getMin(),
									result.get(i).getPercentile(0.25), result
											.get(i).getPercentile(0.5), result
											.get(i).getPercentile(0.75), result
											.get(i).getMax()));

						chart.addSeries(series);

						p.add(chart);

					}
				});

	}

	@Override
	public Widget asWidget() {
		return p;
	}

}
