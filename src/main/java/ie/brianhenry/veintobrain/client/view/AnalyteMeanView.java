package ie.brianhenry.veintobrain.client.view;

import ie.brianhenry.veintobrain.client.RpcService;
import ie.brianhenry.veintobrain.shared.representations.AnalyteStat;
import ie.brianhenry.veintobrain.shared.representations.AnalyteStat.StatPeriod;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.moxieapps.gwt.highcharts.client.BaseChart.ZoomType;
import org.moxieapps.gwt.highcharts.client.ContextButton;
import org.moxieapps.gwt.highcharts.client.Credits;
import org.moxieapps.gwt.highcharts.client.Exporting;
import org.moxieapps.gwt.highcharts.client.Legend;
import org.moxieapps.gwt.highcharts.client.Legend.Align;
import org.moxieapps.gwt.highcharts.client.PlotLine;
import org.moxieapps.gwt.highcharts.client.PlotLine.DashStyle;
import org.moxieapps.gwt.highcharts.client.labels.PlotLineLabel;
import org.moxieapps.gwt.highcharts.client.Point;
import org.moxieapps.gwt.highcharts.client.Series;
import org.moxieapps.gwt.highcharts.client.StockChart;
import org.moxieapps.gwt.highcharts.client.XAxis;
import org.moxieapps.gwt.highcharts.client.YAxis;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class AnalyteMeanView implements IsWidget {

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

	public AnalyteMeanView(RpcService rpcService, EventBus eventBus) {
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
		// chart.setSize("800px", "500px");
		chart.setSizeToMatchContainer();
		chart.setZoomType(ZoomType.X_AND_Y);
		chart.setExporting(exporting);
		chart.setLegend(legend);
		chart.getYAxis().setAxisTitleText("ug/L");

		series = chart.createSeries();
		series2 = chart.createSeries();
		series3 = chart.createSeries();
		mean = chart.createSeries();

		series.setName("7 day moving mean");
		series2.setName("20 day moving mean");
		series3.setName("50 day moving mean");
		mean.setName("Overall mean");

		chart.addSeries(series);
		chart.addSeries(series2);
		chart.addSeries(series3);
		chart.addSeries(mean);
		chartPanel.clear();

		double overMean = round(getOverallMean("psa", analyteStats));
		// YAxis xAxis = chart.getYAxis();
		// xAxis.setPlotLines(
		// xAxis.createPlotLine()
		// .setColor("#CC0000")
		// .setValue(overMean)
		// .setWidth(2)
		// .setDashStyle(DashStyle.DASH)
		// .setLabel(new PlotLineLabel()
		// .setText("Overall mean"))
		// );

		// TODO
		double val7 = 0;
		double val50 = 0;

		setMaxDiff750(0);
		setMinDiff750(Double.MAX_VALUE);

		for (int i = 0; i < analyteStats.size(); i++) {
			mean.addPoint(new Point(analyteStats.get(i).getIncludedDates().get(0).getTime(), overMean));
			if (analyteStats.get(i).getMovingMean().get("7") == null) {
				series.addPoint(new Point(analyteStats.get(i).getIncludedDates().get(0).getTime(), analyteStats.get(i).getMovingMean()
						.get("7")));
			} else {
				series.addPoint(new Point(analyteStats.get(i).getIncludedDates().get(0).getTime(), round(analyteStats.get(i)
						.getMovingMean().get("7"))));
			}
			if (analyteStats.get(i).getMovingMean().get("20") == null) {
				series2.addPoint(new Point(analyteStats.get(i).getIncludedDates().get(0).getTime(), analyteStats.get(i)
						.getMovingMean().get("20")));
			} else {
				series2.addPoint(new Point(analyteStats.get(i).getIncludedDates().get(0).getTime(), round(analyteStats.get(i)
						.getMovingMean().get("20"))));
			}
			if (analyteStats.get(i).getMovingMean().get("50") == null) {
				series3.addPoint(new Point(analyteStats.get(i).getIncludedDates().get(0).getTime(), analyteStats.get(i)
						.getMovingMean().get("50")));
			} else {
				series3.addPoint(new Point(analyteStats.get(i).getIncludedDates().get(0).getTime(), round(analyteStats.get(i)
						.getMovingMean().get("50"))));
				// TODO
				val7 = round(analyteStats.get(i).getMovingMean().get("7"));
				// TODO
				val50 = round(analyteStats.get(i).getMovingMean().get("50"));
			}
			// TODO
			if (Math.abs(val50 - val7) > maxDiff750)
				setMaxDiff750(Math.abs(val50 - val7));
			else if (Math.abs(val50 - val7) < minDiff750)
				setMinDiff750(Math.abs(val50 - val7));
		}
		GWT.log("maxdist=" + getMaxDiff750());
		GWT.log("mindist=" + getMinDiff750());
		GWT.log("overall mean=" + round(getOverallMean("psa", analyteStats)));

		chartPanel.add(chart);
	}

	public void setAnalyte(List<AnalyteStat> analyteStats) {

		this.analyteStats = analyteStats;
		setChart(analyteStats.get(0).getAnalyteType());
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

	public String getOverallMean(String analyte) {
		Double mean = round(getOverallMean("psa", analyteStats));
		// GWT.log("MEAN="+mean);
		String m = mean.toString();
		return m;
	}

	public static double getOverallMean(String analyte, List<AnalyteStat> analyteStats) {
		double sum = 0.0;
		int count = 0;
		for (int i = 0; i < analyteStats.size(); i++) {
			if (analyteStats.get(i).getAnalyteType().equals(analyte)) {
				for (int j = 0; j < analyteStats.get(i).getNumericReadings().size(); j++) {
					count++;
					sum += analyteStats.get(i).getNumericReadings().get(j);
				}
			}
		}
		return (sum / count);
	}

	public String standardDev(String analyte) {
		Double sd = round(standardDev("psa", analyteStats));
		// GWT.log("MEAN="+mean);
		String m = sd.toString();
		return m;
	}

	public static double standardDev(String analyte, List<AnalyteStat> analyteStats) {
		double sum = 0.0;
		int count = 0;
		double mean = getOverallMean(analyte, analyteStats);
		for (int i = 0; i < analyteStats.size(); i++) {
			if (analyteStats.get(i).getAnalyteType().equals(analyte)) {
				for (int j = 0; j < analyteStats.get(i).getNumericReadings().size(); j++) {
					count++;
					double val = analyteStats.get(i).getNumericReadings().get(j);
					sum += Math.pow(val - mean, 2);
				}
			}
		}
		GWT.log("sd=" + Math.sqrt(sum / count));
		return (Math.sqrt(sum / count));
	}

	public String variance(String analyte) {
		Double sd = round(variance("psa", analyteStats));
		// GWT.log("MEAN="+mean);
		String m = sd.toString();
		return m;
	}

	public static double variance(String analyte, List<AnalyteStat> analyteStats) {
		double sum = 0.0;
		int count = 0;
		double mean = getOverallMean(analyte, analyteStats);
		for (int i = 0; i < analyteStats.size(); i++) {
			if (analyteStats.get(i).getAnalyteType().equals(analyte)) {
				for (int j = 0; j < analyteStats.get(i).getNumericReadings().size(); j++) {
					count++;
					sum += Math.pow(analyteStats.get(i).getNumericReadings().get(j) - mean, 2);
					// analyteStats.get(i).getNumericReadings().get(j);
				}
			}
		}
		GWT.log("var=" + (sum / count));
		return (sum / count);
	}

	public String avgDailyTests(String analyte) {
		Double avg = round(avgDailyTests("psa", analyteStats));
		// GWT.log("MEAN="+mean);
		String m = avg.toString();
		return m;
	}

	public static double avgDailyTests(String analyte, List<AnalyteStat> analyteStats) {
		int sum = 0;
		int count = 0;
		for (int i = 0; i < analyteStats.size(); i++) {
			if (analyteStats.get(i).getAnalyteType().equals(analyte)) {
				count++;
				sum += analyteStats.get(i).getNumericReadings().size();

			}
		}
		return (sum / count);
	}

	public String maxValue(String analyte) {
		Double max = round(maxValue("psa", analyteStats));
		// GWT.log("MEAN="+mean);
		String m = max.toString();
		return m;
	}

	public static double maxValue(String analyte, List<AnalyteStat> analyteStats) {

		double max = 0;

		for (int i = 0; i < analyteStats.size(); i++) {
			if (analyteStats.get(i).getAnalyteType().equals(analyte)) {
				for (int j = 0; j < analyteStats.get(i).getNumericReadings().size(); j++) {
					double val = analyteStats.get(i).getNumericReadings().get(j);
					if (val >= max)
						max = val;
				}
			}
		}
		return (max);
	}

	public String minValue(String analyte) {
		Double min = round(minValue("psa", analyteStats));
		// GWT.log("MEAN="+mean);
		String m = min.toString();
		return m;
	}

	public static double minValue(String analyte, List<AnalyteStat> analyteStats) {

		double min = 0;

		for (int i = 0; i < analyteStats.size(); i++) {
			if (analyteStats.get(i).getAnalyteType().equals(analyte)) {
				for (int j = 0; j < analyteStats.get(i).getNumericReadings().size(); j++) {
					double val = analyteStats.get(i).getNumericReadings().get(j);
					if (val <= min)
						min = val;
				}
			}
		}
		return (min);
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