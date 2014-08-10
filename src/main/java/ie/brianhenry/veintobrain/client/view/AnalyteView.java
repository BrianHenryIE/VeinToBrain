package ie.brianhenry.veintobrain.client.view;

import ie.brianhenry.veintobrain.client.RpcService;
import ie.brianhenry.veintobrain.representations.AnalyteStat;
import ie.brianhenry.veintobrain.representations.AnalyteStat.StatPeriod;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.moxieapps.gwt.highcharts.client.Axis.WeekDay;
import org.moxieapps.gwt.highcharts.client.BaseChart.ZoomType;
import org.moxieapps.gwt.highcharts.client.Series.Type;
import org.moxieapps.gwt.highcharts.client.ContextButton;
import org.moxieapps.gwt.highcharts.client.Credits;
import org.moxieapps.gwt.highcharts.client.DateTimeLabelFormats;
import org.moxieapps.gwt.highcharts.client.Exporting;
import org.moxieapps.gwt.highcharts.client.Navigation;
import org.moxieapps.gwt.highcharts.client.Point;
import org.moxieapps.gwt.highcharts.client.RangeSelector;
import org.moxieapps.gwt.highcharts.client.Series;
import org.moxieapps.gwt.highcharts.client.StockChart;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class AnalyteView implements IsWidget {

	FlowPanel p = new FlowPanel();

	SimplePanel chartPanel = new SimplePanel();

	private RpcService rpcService;

	List<AnalyteStat> analyteStats;

	StockChart chart;

	Series series;
	Series series2;
	Series series3;

	public AnalyteView(RpcService rpcService, EventBus eventBus) {
		this.rpcService = rpcService;

		p.add(chartPanel);

	}

	private void setChart(String analyte) {
		
		ContextButton contextButton = new ContextButton();
		contextButton.setText("Export");
		
		Credits credits = new Credits();
		credits.setText("UCD Labs Analytics");
		credits.setHref("http://ucdlabsanalytics.wordpress.com/");
		
		Exporting exporting = new Exporting();
		exporting.setEnabled(true);
		exporting.setContextButton(contextButton);
		
		chart = new StockChart().setChartTitleText(analyte);
		chart.setCredits(credits);
		chart.setSize("780px", "500px");
		chart.setZoomType(ZoomType.X_AND_Y);
		chart.setExporting(exporting);

		series = chart.createSeries();
		series2 = chart.createSeries();
		series3 = chart.createSeries();

		series.setName("7 day mean of medians");
		series2.setName("20 day mean of medians");
		series3.setName("50 day mean of medians");

		chart.addSeries(series);
		chart.addSeries(series2);
		chart.addSeries(series3);
		chartPanel.clear();
		
//		List<Date> categories = new ArrayList<Date>();
		
		for (int i = 0; i < analyteStats.size(); i++) {
			series.addPoint(new Point(analyteStats.get(i).getIncludedDates().get(0).getTime(),
					analyteStats.get(i).getMovingMeanOfMedians().get("7")));
			series2.addPoint(new Point(analyteStats.get(i).getIncludedDates().get(0).getTime(),
					analyteStats.get(i).getMovingMeanOfMedians().get("20")));
			series3.addPoint(new Point(analyteStats.get(i).getIncludedDates().get(0).getTime(),
					analyteStats.get(i).getMovingMeanOfMedians().get("50")));
//			categories.add(analyteStats.get(i).getIncludedDates().get(0));

			GWT.log("DATE=" + analyteStats.get(i).getIncludedDates().get(0).toString());
		}
		chartPanel.add(chart);
	}

	public void setAnalyte(final String analyte) {

		rpcService.executeRequest(analyte, StatPeriod.DAY, new AsyncCallback<List<AnalyteStat>>() {
			public void onFailure(Throwable caught) {
			}

			public void onSuccess(List<AnalyteStat> result) {
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