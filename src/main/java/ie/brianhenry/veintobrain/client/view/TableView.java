package ie.brianhenry.veintobrain.client.view;

import ie.brianhenry.veintobrain.client.RpcService;
import ie.brianhenry.veintobrain.representations.AnalyteStat;
import ie.brianhenry.veintobrain.representations.AnalyteStat.StatPeriod;

import java.util.ArrayList;
import java.util.List;

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

public class TableView implements IsWidget {

	FlowPanel p = new FlowPanel();

	SimplePanel chartPanel = new SimplePanel();

	private RpcService rpcService;

	List<AnalyteStat> analyteStats;

	StockChart chart;

	Series series;
	// TODO implement the choise of moving mean and two graphs at a time
	// int movingMean;
	Series series2;
	Series series3;

	// TODO THERE IS A TABLEVIEW CLASS ALREADY IMPLEMENTED IN JAVA. USE THAT
	public TableView(RpcService rpcService, EventBus eventBus) {
		this.rpcService = rpcService;

		p.add(chartPanel);

	}

	private void setChart(String analyte) {

		 chart = new StockChart().setChartTitleText(analyte)
				 .setMarginRight(10);
		 chart.setSize("750px", "500px");
		 
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
		 List<String> categories = new ArrayList<String>();
			GWT.log("size="+analyteStats.size());
			for (int i = 0; i < analyteStats.size(); i++) {
				series.addPoint(new Point(analyteStats.get(i).getMovingMeanOfMedians().get("7")));
				series2.addPoint(new Point(analyteStats.get(i).getMovingMeanOfMedians().get("20")));
				series3.addPoint(new Point(analyteStats.get(i).getMovingMeanOfMedians().get("50")));
				categories.add(analyteStats.get(i).getIncludedDates().get(0).toString());
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