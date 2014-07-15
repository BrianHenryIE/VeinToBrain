package ie.brianhenry.veintobrain.client;

import ie.brianhenry.veintobrain.client.overlay.AnalyteStat;

import org.moxieapps.gwt.highcharts.client.Chart;
import org.moxieapps.gwt.highcharts.client.Point;
import org.moxieapps.gwt.highcharts.client.Series;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class AppController {

	
	EventBus eventBus;

	FlowPanel app = new FlowPanel();

	SimplePanel appContainer = new SimplePanel();

	
	RpcService rpcService;


	public AppController(RpcService rpcService, EventBus eventBus) {

		this.rpcService = rpcService;
		this.eventBus = eventBus;

		RootPanel.get("gwt").getElement().getStyle().setPosition(Position.ABSOLUTE);
		RootPanel.get("gwt").getElement().getStyle().setWidth(100, Unit.PCT);
		RootPanel.get("gwt").getElement().getStyle().setHeight(100, Unit.PCT);


		//app.add(new AppHeading());
		app.add(appContainer);

	}
	
	
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";


	private void executeRequest(String message,
			final AsyncCallback<JsArray<AnalyteStat>> asyncCallback) {

		String jsonUrl = "http://localhost:8080/api/analyte-stat?name="
				+ message;

		String url = URL.encode(jsonUrl);

		System.out.println(url);

		// Send request to server and catch any errors.
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);

		try {
			builder.sendRequest(null, new RequestCallback() {

				@Override
				public void onResponseReceived(Request request,
						Response response) {

					System.out.println("response: " + response.getText());

					asyncCallback.onSuccess(JsonUtils
							.<JsArray<AnalyteStat>> safeEval(response.getText()));

				}

				@Override
				public void onError(Request request, Throwable exception) {
					// TODO Auto-generated method stub

				}
			});
		} catch (RequestException e) {
			System.out.println("Couldn't retrieve JSON : " + e.getMessage()
					+ " :getEventsForPage()");
		}
	}

	
	public void go(HasWidgets container) {
		

		container.add(app);

		

		

		executeRequest("a request object", new AsyncCallback<JsArray<AnalyteStat>>() {
			public void onFailure(Throwable caught) {
				
			}

			public void onSuccess(JsArray<AnalyteStat> result) {
				
				Chart chart = new Chart();
				chart.setType(Series.Type.BOXPLOT);
				chart.setSize("800px", "500px");
				
				chart.setCredits(null);

				
				Series series = chart.createSeries();
				for(int i=0; i< result.length(); i++)
					series.addPoint(new Point(result.get(i).getMin(), result.get(i).getPercentile(0.25), result.get(i).getPercentile(0.5), result.get(i).getPercentile(0.75), result.get(i).getMax()));
				
				
				chart.addSeries(series);

				app.add(chart);

			}
		});
		
		
	

	}
}
