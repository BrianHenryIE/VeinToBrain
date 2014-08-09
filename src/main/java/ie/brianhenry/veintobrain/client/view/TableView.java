package ie.brianhenry.veintobrain.client.view;

import ie.brianhenry.veintobrain.client.RpcService;
import ie.brianhenry.veintobrain.representations.AnalyteStat;
import ie.brianhenry.veintobrain.representations.AnalyteStat.StatPeriod;

import java.util.ArrayList;
import java.util.List;

import org.moxieapps.gwt.highcharts.client.BaseChart.ZoomType;
import org.moxieapps.gwt.highcharts.client.Point;
import org.moxieapps.gwt.highcharts.client.Series;
import org.moxieapps.gwt.highcharts.client.StockChart;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class TableView implements IsWidget {

	FlowPanel p = new FlowPanel();

	SimplePanel chartPanel = new SimplePanel();
//	SimplePanel tablePanel = new SimplePanel();
//	FlowPanel optionsPanel = new FlowPanel();

	private RpcService rpcService;

	List<AnalyteStat> analyteStats;

	//Chart chart;
	StockChart chart = new StockChart();

	Series series;
	Series mean;
	
	// TODO THERE IS A TABLEVIEW CLASS ALREADY IMPLEMENTED IN JAVA. USE THAT
	public TableView(RpcService rpcService, EventBus eventBus) {
		this.rpcService = rpcService;

		p.add(chartPanel);

		// TODO if no analyte set.. display a message of some sort... maybe a
		// waiting animation

		// Options (should be app-wide defaults for anythign configurable)

		// duration of each b
		// minimum number of

	}

	private void setChart(String analyte) {
		chart = new StockChart().setChartTitleText(analyte);
		chart.setZoomType(ZoomType.Y);
		chart.setCredits(null);
		chart.setSizeToMatchContainer();
		
		series = chart.createSeries();		
		chart.addSeries(series);
		series.setName(analyte+" 7");

		chartPanel.clear();
		
		List<String> categories = new ArrayList<String>();
		for (int i = 0; i < analyteStats.size(); i++) {

//			for(int j = 0; j < (int)((analyteStats.get(i).getIncludedDates().get(0).getTime()-analyteStats.get(i-1).getIncludedDates().get(0).getTime())/86400000); j++);
//				series.addPoint(new Point());
			series.addPoint(new Point(analyteStats.get(i).getMovingMeanOfMedians().get("50")));
			categories.add(analyteStats.get(i).getIncludedDates().get(0).toString());
		}
		//chart.getXAxis().setType(Type.DATE_TIME);
		//chart.setOption("/plotOptions/line/pointInterval/",86400000);
		// chart.getXAxis().setCategories(categories.toArray(new String[categories.size()]));

		chartPanel.add(chart);
		//
		// StockChart stockChart = new
		// StockChart().setChartTitleText(analyte).setMarginRight(10);
		//
		// Series series1 =
		// stockChart.createSeries().addPoint(40).addPoint(35).addPoint(60);
		// stockChart.addSeries(series1);
		// stockChart.setSize("800px", "500px");
		// chartPanel.add(stockChart);
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