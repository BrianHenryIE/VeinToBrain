package ie.brianhenry.veintobrain.client.view;

import ie.brianhenry.veintobrain.client.RpcService;
import ie.brianhenry.veintobrain.shared.representations.AnalyteStat;
import ie.brianhenry.veintobrain.shared.representations.AnalyteStat.StatPeriod;

import java.util.ArrayList;
import java.util.List;

import org.moxieapps.gwt.highcharts.client.BaseChart.ZoomType;
import org.moxieapps.gwt.highcharts.client.Point;
import org.moxieapps.gwt.highcharts.client.Series;
import org.moxieapps.gwt.highcharts.client.StockChart;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class TableView implements IsWidget {

	FlowPanel p = new FlowPanel();

	ScrollPanel chartPanel = new ScrollPanel();

	private RpcService rpcService;

	List<AnalyteStat> analyteStats;

	//Chart chart;
//	StockChart chart = new StockChart();
	FlexTable table = new FlexTable();
	
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

		table.setText(0, 0, "Analyte");
		table.setText(0, 1, "Date(s)");
		table.setText(0, 2, "7 day mean of medians");
		table.setText(0, 3, "20 day mean of medians");
		table.setText(0, 4, "50 day mean of medians");

//	    // Let's put a button in the middle...
//		table.setWidget(1, 0, new Button("Wide Button"));
//
//	    // ...and set it's column span so that it takes up the whole row.
//		table.getFlexCellFormatter().setColSpan(1, 0, 3);
		
		chartPanel.clear();
		
		List<String> categories = new ArrayList<String>();
		for (int i = 0; i < analyteStats.size(); i++) {
			table.setText(i+1, 0, "test");

//			for(int j = 0; j < (int)((analyteStats.get(i).getIncludedDates().get(0).getTime()-analyteStats.get(i-1).getIncludedDates().get(0).getTime())/86400000); j++);
//				series.addPoint(new Point());

		}
		//chart.getXAxis().setType(Type.DATE_TIME);
		//chart.setOption("/plotOptions/line/pointInterval/",86400000);
		// chart.getXAxis().setCategories(categories.toArray(new String[categories.size()]));

		chartPanel.add(table);
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