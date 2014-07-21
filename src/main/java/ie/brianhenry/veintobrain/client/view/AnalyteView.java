package ie.brianhenry.veintobrain.client.view;

import ie.brianhenry.veintobrain.client.RpcService;
import ie.brianhenry.veintobrain.client.overlay.AnalyteStat;

import org.moxieapps.gwt.highcharts.client.BaseChart.ZoomType;
import org.moxieapps.gwt.highcharts.client.Chart;
import org.moxieapps.gwt.highcharts.client.Point;
import org.moxieapps.gwt.highcharts.client.Series;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class AnalyteView implements IsWidget {

	FlowPanel p = new FlowPanel();

	SimplePanel chartPanel = new SimplePanel();
	SimplePanel tablePanel = new SimplePanel();
	FlowPanel optionsPanel = new FlowPanel();

	private RpcService rpcService;

	JsArray<AnalyteStat> analyteStats;

	Chart chart;
	Series series;

	public AnalyteView(RpcService rpcService, EventBus eventBus) {
		this.rpcService = rpcService;

		p.add(chartPanel);
		p.add(tablePanel);
		p.add(optionsPanel);

		// TODO if no analyte set.. display a message of some sort... maybe a
		// waiting animation

		// Options (should be app-wide defaults for anythign configurable)
		
		// duration of each b
		// minimum number of 

	}

	private void setChart(String analyte) {
		chart = new Chart();

		chart.setZoomType(ZoomType.Y);
		chart.getYAxis().setMin(0);
		
		chart.setType(Series.Type.BOXPLOT);
		chart.setSize("800px", "500px");

		chart.setCredits(null);

		series = chart.createSeries();

		chart.addSeries(series);

		series.setName(analyte);

		chartPanel.clear();

		for (int i = 0; i < analyteStats.length(); i++) {
			series.addPoint(new Point(analyteStats.get(i)
					.getPercentile(0.025), analyteStats.get(i)
					.getPercentile(0.25), analyteStats.get(i)
					.getPercentile(0.5), analyteStats.get(i)
					.getPercentile(0.75), analyteStats.get(i)
					.getPercentile(0.975)));
		}

		chartPanel.add(chart);

	}

	public void setAnalyte(final String analyte) {

		rpcService.executeRequest(analyte,
				new AsyncCallback<JsArray<AnalyteStat>>() {
					public void onFailure(Throwable caught) {

					}

					public void onSuccess(JsArray<AnalyteStat> result) {

						analyteStats = result;

						setChart(analyte);

					}
				});
	}

	@Override
	public Widget asWidget() {
		return p;
	}

}
