package ie.brianhenry.veintobrain.client;

import ie.brianhenry.veintobrain.client.overlay.AnalyteStat;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.binder.EventBinder;

public class RpcService {


	
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	interface MyEventBinder extends EventBinder<RpcService> {
	}

	private final MyEventBinder eventBinder = GWT.create(MyEventBinder.class);


	private EventBus eventBus;


	public RpcService(EventBus eventBus) {

		this.eventBus = eventBus;

		eventBinder.bindEventHandlers(this, eventBus);

	}
	
	


	public void executeRequest(String message,
			final AsyncCallback<JsArray<AnalyteStat>> asyncCallback) {

		String jsonUrl = "/api/analyte-stat?name="
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

	
}
