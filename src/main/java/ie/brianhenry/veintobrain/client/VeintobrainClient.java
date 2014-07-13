package ie.brianhenry.veintobrain.client;

import ie.brianhenry.veintobrain.client.overlay.AnalyteStat;

import org.moxieapps.gwt.highcharts.client.Chart;
import org.moxieapps.gwt.highcharts.client.Legend;
import org.moxieapps.gwt.highcharts.client.Point;
import org.moxieapps.gwt.highcharts.client.Series;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class VeintobrainClient implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	// Add the nameField and sendButton to the RootPanel
	// Use RootPanel.get() to get the entire body element
	RootPanel root = RootPanel.get("gwt");

	VerticalPanel panel = new VerticalPanel();

	private void executeRequest(String message,
			final AsyncCallback<AnalyteStat> asyncCallback) {

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
							.<AnalyteStat> safeEval(response.getText()));

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

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		final Button sendButton = new Button("Send!");
		final TextBox nameField = new TextBox();
		nameField.setText("GWT User");
		final Label errorLabel = new Label();

		// We can add style names to widgets
		sendButton.addStyleName("sendButton");
		//
		// Chart chart = new Chart().setType(Series.Type.BOXPLOT)
		// .setChartTitleText("Folate").setMarginRight(10);
		// chart.setSize(700, 700);
		//
		// //Series series = chart.createSeries().setName("milithings per mole")
		// // .setPoints(new Number[] { 163, 203, 276, 408, 547 });
		// // chart.addSeries(series);
		// chart.setPersistent(true)
		// .addSeries(chart.createSeries()
		// .setName("Observations")
		// .setOption("data", new Number[][] {
		// {760, 801, 848, 895, 965},
		// {733, 853, 939, 980, 1080},
		// {714, 762, 817, 870, 918},
		// {724, 802, 806, 871, 950},
		// {834, 836, 864, 882, 910}
		// }));
		//

		root.add(panel);

		panel.add(nameField);
		panel.add(sendButton);
		panel.add(errorLabel);

		
		executeRequest("a request object", new AsyncCallback<AnalyteStat>() {
			public void onFailure(Throwable caught) {
				
			}

			public void onSuccess(AnalyteStat result) {
				
				Chart chart = new Chart();
				chart.setType(Series.Type.BOXPLOT);
				chart.setSize("200px", "200px");
				// chart.setPersistent(true); //remove this line to see
				// the proper behavior

				Series series = chart.createSeries();
				series.addPoint(new Point(result.getMin(), result.get25th(), result.getMedian(), result.get75th(), result.getMax()));
				chart.addSeries(series);

				panel.add(chart);

			}
		});
		
		
	
		// Focus the cursor on the name field when the app loads
		nameField.setFocus(true);
		nameField.selectAll();

		// Create the popup dialog box
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("Remote Procedure Call");
		dialogBox.setAnimationEnabled(true);
		final Button closeButton = new Button("Close");
		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");
		final Label textToServerLabel = new Label();
		final HTML serverResponseLabel = new HTML();
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
		dialogVPanel.add(textToServerLabel);
		dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
		dialogVPanel.add(serverResponseLabel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeButton);
		dialogBox.setWidget(dialogVPanel);

		// Add a handler to close the DialogBox
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				sendButton.setEnabled(true);
				sendButton.setFocus(true);
			}
		});

		// Create a handler for the sendButton and nameField
		class MyHandler implements ClickHandler, KeyUpHandler {
			/**
			 * Fired when the user clicks on the sendButton.
			 */
			public void onClick(ClickEvent event) {
				sendNameToServer();
			}

			/**
			 * Fired when the user types in the nameField.
			 */
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					sendNameToServer();
				}
			}

			/**
			 * Send the name from the nameField to the server and wait for a
			 * response.
			 */
			private void sendNameToServer() {
				// First, we validate the input.
				errorLabel.setText("");
				String textToServer = nameField.getText();

				// Then, we send the input to the server.
				sendButton.setEnabled(false);
				textToServerLabel.setText(textToServer);
				serverResponseLabel.setText("");
				executeRequest(textToServer, new AsyncCallback<AnalyteStat>() {
					public void onFailure(Throwable caught) {
						// Show the RPC error message to the user
						dialogBox.setText("Remote Procedure Call - Failure");
						serverResponseLabel
								.addStyleName("serverResponseLabelError");
						serverResponseLabel.setHTML(SERVER_ERROR);
						dialogBox.center();
						closeButton.setFocus(true);
					}

					public void onSuccess(AnalyteStat result) {
						dialogBox.setText("Remote Procedure Call");

						serverResponseLabel
								.removeStyleName("serverResponseLabelError");
						serverResponseLabel.setHTML("median: "
								+ result.getMedian());

						dialogBox.center();
						closeButton.setFocus(true);

						Chart chart = new Chart();
						chart.setType(Series.Type.BOXPLOT);
						chart.setSize("200px", "200px");
						// chart.setPersistent(true); //remove this line to see
						// the proper behavior

						Series series = chart.createSeries();
						series.addPoint(new Point(50, 100, 150, 200, 250));
						chart.addSeries(series);

						panel.add(chart);

					}
				});
			}
		}

		// Add a handler to send the name to the server
		MyHandler handler = new MyHandler();
		sendButton.addClickHandler(handler);
		nameField.addKeyUpHandler(handler);
	}
}