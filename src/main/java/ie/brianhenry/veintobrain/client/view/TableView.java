package ie.brianhenry.veintobrain.client.view;

import ie.brianhenry.veintobrain.client.RpcService;
import ie.brianhenry.veintobrain.shared.representations.AnalyteStat;

import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.Range;

public class TableView implements IsWidget {

	ScrollPanel panel = new ScrollPanel();

	CellTable<AnalyteStat> table = new CellTable<AnalyteStat>();

	RpcService rpcService;

	public TableView(RpcService rpcService, EventBus eventBus) {
		this.rpcService = rpcService;

		table.addColumn(dateColumn, "Date");
		table.addColumn(sevenDayMean, "7 day mean");
		table.addColumn(twentyDayMean, "20 day mean");
		table.addColumn(fiftyDayMean, "50 day mean");

		table.addColumn(sevenDayMedian, "7 day median");
		table.addColumn(twentyDayMedian, "20 day median");
		table.addColumn(fiftyDayMedian, "50 day median");

		panel.add(table);
	}

	TextColumn<AnalyteStat> dateColumn = new TextColumn<AnalyteStat>() {
		@Override
		public String getValue(AnalyteStat stat) {
			return DateTimeFormat.getFormat(PredefinedFormat.DATE_SHORT).format(stat.getIncludedDates().get(0));
		}
	};
	
	NumberFormat decimalFormat = NumberFormat.getFormat(".##");

	
	/*
	

	analytePeriod: null
analyteType: "psa"
class: "ie.brianhenry.veintobrain.shared.representations.AnalyteStat"
coefficientOfVaritation: 1.7380952380952381
id: null
includedDates: [1376002800000]
includedDays: []
inputCount: 88
isValid: true
max: 27.17
mean: 2.52
min: 0.029
mode: [<0.03, 0.97, 0.82]
0: "<0.03"
1: "0.97"
2: "0.82"
movingMean: {7:2.844817204301078, 20:2.891893842887475}
7: 2.844817204301078
20: 2.891893842887475
movingMeanOfMedians: {}
movingMedian: {7:1.24, 20:1.34}
7: 1.24
20: 1.34
numericReadings: [1.88, 0.028999999999999998, 8.26, 0.63, 1.21, 6.27, 1.6, 3.32, 0.59, 3.64, 1.01, 0.97, 2.07, 1.47,…]
originalReadings: [1.88, <0.03, 8.26, 0.63, 1.21, 6.27, 1.6, 3.32, 0.59, 3.64, 1.01, 0.97, 2.07, 1.47, 10.9, 3.91, 0.39,…]
otherData: {4SD:2, <0_03:3}
4SD: 2
<0_03: 3
percentileCalculations: {0_75:2.265, 0_975:17.657, 0_25:0.573, 0_025:0.029, 0_5:0.995}
0_5: 0.995
0_025: 0.029
0_25: 0.573
0_75: 2.265
0_975: 17.657
standardDeviation: 4.38
validCount: 86

*/
	
	TextColumn<AnalyteStat> sevenDayMean = new TextColumn<AnalyteStat>() {
		@Override
		public String getValue(AnalyteStat stat) {
			if (stat.getMovingMean().get("7") != null)
				return decimalFormat.format(stat.getMovingMean().get("7"));
			else
				return "";
		}
	};
	
	TextColumn<AnalyteStat> twentyDayMean = new TextColumn<AnalyteStat>() {
		@Override
		public String getValue(AnalyteStat stat) {
			if (stat.getMovingMean().get("20") != null)
				return decimalFormat.format(stat.getMovingMean().get("20"));
			else
				return "";
		}
	};
	
	TextColumn<AnalyteStat> fiftyDayMean = new TextColumn<AnalyteStat>() {
		@Override
		public String getValue(AnalyteStat stat) {
			if (stat.getMovingMean().get("50") != null)
				return decimalFormat.format(stat.getMovingMean().get("50"));
			else
				return "";
		}
	};


	TextColumn<AnalyteStat> sevenDayMedian = new TextColumn<AnalyteStat>() {
		@Override
		public String getValue(AnalyteStat stat) {
			if (stat.getMovingMean().get("7") != null)
				return decimalFormat.format(stat.getMovingMedian().get("7"));
			else
				return "";
		}
	};
	
	TextColumn<AnalyteStat> twentyDayMedian = new TextColumn<AnalyteStat>() {
		@Override
		public String getValue(AnalyteStat stat) {
			if (stat.getMovingMean().get("20") != null)
				return decimalFormat.format(stat.getMovingMedian().get("20"));
			else
				return "";
		}
	};
	
	TextColumn<AnalyteStat> fiftyDayMedian = new TextColumn<AnalyteStat>() {
		@Override
		public String getValue(AnalyteStat stat) {
			if (stat.getMovingMean().get("50") != null)
				return decimalFormat.format(stat.getMovingMedian().get("50"));
			else
				return "";
		}
	};

	public void setChart(String analyte, List<AnalyteStat> stats) {

		GWT.log("stats.size() " + stats.size());

		table.setVisibleRange(new Range(0, stats.size()));
		table.setPageSize(stats.size());
		
		table.setRowCount(stats.size(), true);
		table.setRowData(0, stats);
		
	}

	@Override
	public Widget asWidget() {
		return panel;
	}

}