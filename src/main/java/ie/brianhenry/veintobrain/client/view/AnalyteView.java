package ie.brianhenry.veintobrain.client.view;

import ie.brianhenry.veintobrain.client.RpcService;
import ie.brianhenry.veintobrain.representations.AnalyteStat;
import ie.brianhenry.veintobrain.representations.AnalyteStat.StatPeriod;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.moxieapps.gwt.highcharts.client.BaseChart.ZoomType;
import org.moxieapps.gwt.highcharts.client.ContextButton;
import org.moxieapps.gwt.highcharts.client.Credits;
import org.moxieapps.gwt.highcharts.client.Exporting;
import org.moxieapps.gwt.highcharts.client.Legend;
import org.moxieapps.gwt.highcharts.client.Legend.Align;
import org.moxieapps.gwt.highcharts.client.Point;
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
		
		Legend legend = new Legend();
		legend.setEnabled(true);
		legend.setAlign(Align.CENTER);
		
		chart = new StockChart().setChartTitleText(analyte);
		chart.setCredits(credits);
//		chart.setSize("800px", "500px");
		chart.setSizeToMatchContainer();
		chart.setZoomType(ZoomType.X_AND_Y);
		chart.setExporting(exporting);
		chart.setLegend(legend);

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

		for (int i = 0; i < analyteStats.size(); i++) {			
			if (analyteStats.get(i).getMovingMeanOfMedians().get("7") == null) {
				series.addPoint(new Point(analyteStats.get(i).getIncludedDates().get(0).getTime(), analyteStats.get(i)
						.getMovingMeanOfMedians().get("7")));
			} else {
				series.addPoint(new Point(analyteStats.get(i).getIncludedDates().get(0).getTime(), round(analyteStats.get(i)
						.getMovingMeanOfMedians().get("7"))));
			}
			if (analyteStats.get(i).getMovingMeanOfMedians().get("20") == null) {
				series2.addPoint(new Point(analyteStats.get(i).getIncludedDates().get(0).getTime(), analyteStats.get(i)
						.getMovingMeanOfMedians().get("20")));
			} else {
				series2.addPoint(new Point(analyteStats.get(i).getIncludedDates().get(0).getTime(), round(analyteStats.get(i)
						.getMovingMeanOfMedians().get("20"))));
			}
			if (analyteStats.get(i).getMovingMeanOfMedians().get("50") == null) {
				series3.addPoint(new Point(analyteStats.get(i).getIncludedDates().get(0).getTime(), analyteStats.get(i)
						.getMovingMeanOfMedians().get("50")));
			} else {
				series3.addPoint(new Point(analyteStats.get(i).getIncludedDates().get(0).getTime(), round(analyteStats.get(i)
						.getMovingMeanOfMedians().get("50"))));
			}
		}
		GWT.log("overall mean="+getOverallMean("psa", analyteStats));
		
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

	public static double round(double value) {
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(2, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}
	
	public static double getOverallMean(String analyte, List<AnalyteStat> analyteStats) {
//		public static double getOverallMean(List<AnalyteDate> allValidAnalyteDates) {
			double sum = 0.0;
			int count = 0;
			for (int i=0; i<analyteStats.size(); i++) {
				if (analyteStats.get(i).getAnalyteType().equals(analyte)) {
					for (int j=0; j<analyteStats.get(i).getNumericReadings().size(); j++) {
						count++;
						sum+=analyteStats.get(i).getNumericReadings().get(j);
					}
				}
			}
			return (sum/count);
		}

}