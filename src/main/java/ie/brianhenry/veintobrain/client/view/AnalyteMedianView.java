package ie.brianhenry.veintobrain.client.view;

import ie.brianhenry.veintobrain.client.RpcService;
import ie.brianhenry.veintobrain.shared.representations.AnalyteStat;
import ie.brianhenry.veintobrain.shared.representations.AnalyteStat.StatPeriod;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
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

public class AnalyteMedianView implements IsWidget {

	FlowPanel p = new FlowPanel();

	SimplePanel chartPanel = new SimplePanel();

	private RpcService rpcService;

	List<AnalyteStat> analyteStats;

	StockChart chart;

	Series series;
	Series series2;
	Series series3;
	Series mean;
	
	double maxDiff750;
	double minDiff750;

	public AnalyteMedianView(RpcService rpcService, EventBus eventBus) {
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
		// TODO see why it doesn't change the size reading the css file
		chart.setSize("800px", "400px");
		chart.setSizeToMatchContainer();
		chart.setZoomType(ZoomType.X_AND_Y);
		chart.setExporting(exporting);
		chart.setLegend(legend);
		chart.getYAxis().setAxisTitleText("ug/L");

		series = chart.createSeries();
		series2 = chart.createSeries();
		series3 = chart.createSeries();
		mean = chart.createSeries();

		series.setName("7 day moving median");
		series2.setName("20 day moving median");
		series3.setName("50 day moving median");
		mean.setName("Overall median");

		chart.addSeries(series);
		chart.addSeries(series2);
		chart.addSeries(series3);
		chart.addSeries(mean);

		chartPanel.clear();

		double overMedian = round(getOverallMedian("psa", analyteStats));
		
		//TODO
		double val7 = 0;
		double val50 = 0;
		
		setMaxDiff750(0);
		setMinDiff750(Double.MAX_VALUE);

		for (int i = 0; i < analyteStats.size(); i++) {
			mean.addPoint(new Point(analyteStats.get(i).getIncludedDates().get(0).getTime(), overMedian));
			if (analyteStats.get(i).getMovingMedian().get("7") == null) {
				series.addPoint(new Point(analyteStats.get(i).getIncludedDates().get(0).getTime(), analyteStats.get(i)
						.getMovingMedian().get("7")));
			} else {
				series.addPoint(new Point(analyteStats.get(i).getIncludedDates().get(0).getTime(), round(analyteStats.get(i)
						.getMovingMedian().get("7"))));
			}
			if (analyteStats.get(i).getMovingMedian().get("20") == null) {
				series2.addPoint(new Point(analyteStats.get(i).getIncludedDates().get(0).getTime(), analyteStats.get(i)
						.getMovingMedian().get("20")));
			} else {
				series2.addPoint(new Point(analyteStats.get(i).getIncludedDates().get(0).getTime(), round(analyteStats.get(i)
						.getMovingMedian().get("20"))));
			}
			if (analyteStats.get(i).getMovingMedian().get("50") == null) {
				series3.addPoint(new Point(analyteStats.get(i).getIncludedDates().get(0).getTime(), analyteStats.get(i)
						.getMovingMedian().get("50")));
			} else {
				series3.addPoint(new Point(analyteStats.get(i).getIncludedDates().get(0).getTime(), round(analyteStats.get(i)
						.getMovingMedian().get("50"))));
				//TODO
				val7 = round(analyteStats.get(i).getMovingMedian().get("7"));
				//TODO
				val50 = round(analyteStats.get(i).getMovingMedian().get("50"));
			}
			//TODO
			if (Math.abs(val50-val7)>maxDiff750) setMaxDiff750(Math.abs(val50-val7));
			else if (Math.abs(val50-val7)<minDiff750) setMinDiff750(Math.abs(val50-val7));
		}

//		GWT.log("overall median=" + round(getOverallMedian("psa", analyteStats)));

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
	
	public String getOverallMedian(String analyte) {
		Double median = round(getOverallMedian("psa", analyteStats));
//		GWT.log("MEAN="+mean);
		String m = median.toString();
		return m;
	}

	public static double getOverallMedian(String analyte, List<AnalyteStat> analyteStats) {

		List<Double> sortedArray = new ArrayList<Double>();

		for (int i = 0; i < analyteStats.size(); i++) {
			if (analyteStats.get(i).getAnalyteType().equals(analyte)) {
				for (int j = 0; j < analyteStats.get(i).getNumericReadings().size(); j++) {
					sortedArray.add(analyteStats.get(i).getNumericReadings().get(j));
				}
			}
		}

		Collections.sort(sortedArray);
		int middle = sortedArray.size() / 2;
		if (sortedArray.size() % 2 == 1) {
			return sortedArray.get(middle);
		} else {
			return (sortedArray.get(middle - 1) + sortedArray.get(middle) / 2.0);
		}
	}
	
	public double getMaxDiff750() {
		return maxDiff750;
	}
	
	public String getMaxDiff750s() {
		Double mad = round(getMaxDiff750());
		String m = mad.toString();
		return m;
	}

	public void setMaxDiff750(double maxDiff750) {
		this.maxDiff750 = maxDiff750;
	}

	public double getMinDiff750() {
		return minDiff750;
	}
	
	public String getMinDiff750s() {
		Double mid = round(getMinDiff750());
		String m = mid.toString();
		return m;
	}

	public void setMinDiff750(double minDiff750) {
		this.minDiff750 = minDiff750;
	}

}