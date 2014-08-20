package ie.brianhenry.veintobrain.client;

import ie.brianhenry.veintobrain.shared.representations.AnalyteStat;
import ie.brianhenry.veintobrain.shared.representations.AnalyteStat.StatPeriod;
import ie.brianhenry.veintobrain.shared.representations.LoginResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.UmbrellaException;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.kfuntak.gwt.json.serialization.client.ArrayListSerializer;
import com.kfuntak.gwt.json.serialization.client.Serializer;

public class RpcService {

	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network " + "connection and try again.";

	interface MyEventBinder extends EventBinder<RpcService> {
	}

	private final MyEventBinder eventBinder = GWT.create(MyEventBinder.class);

	private EventBus eventBus;

	// This serializes the POJO to json for POSTing to the server
	Serializer serializer = (Serializer) GWT.create(Serializer.class);

	public RpcService(EventBus eventBus) {

		this.eventBus = eventBus;

		eventBinder.bindEventHandlers(this, eventBus);

	}

	private HashMap<String, ArrayList<AnalyteStat>> retrieved = new HashMap<String, ArrayList<AnalyteStat>>();

	public HashMap<String, ArrayList<AnalyteStat>> getRetrievedMap(){
		return retrieved;
	}
	
	public void getAnalyte(final String type, final StatPeriod period, final AsyncCallback<List<AnalyteStat>> asyncCallback) {

		final String jsonUrl = "/api/analyte/" + type + "/" + period;
		
		
		if (this.retrieved.containsKey(jsonUrl)) {
			// We already have it
			asyncCallback.onSuccess(retrieved.get(jsonUrl));

		} else {

			String url = URL.encode(jsonUrl);

			System.out.println(url);

			// Send request to server and catch any errors.
			RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);

			try {
				builder.sendRequest(null, new RequestCallback() {

					@Override
					public void onResponseReceived(Request request, Response response) {
						ArrayListSerializer alSerializer = (ArrayListSerializer) GWT.create(ArrayListSerializer.class);

						@SuppressWarnings("unchecked")
						ArrayList<AnalyteStat> deResponse = (ArrayList<AnalyteStat>) alSerializer.deSerialize(response.getText(),
								"java.util.ArrayList");

						Collections.sort(deResponse);

						retrieved.put(jsonUrl, deResponse);
						asyncCallback.onSuccess(deResponse);

					}

					@Override
					public void onError(Request request, Throwable exception) {
						// TODO Auto-generated method stub

					}
				});
			} catch (RequestException e) {
				System.out.println("Couldn't retrieve JSON : " + e.getMessage() + " :getEventsForPage()");
			}
		}
	}

	public void sendPassword(final String username, String password, final AsyncCallback<LoginResponse> asyncCallback) {

		String jsonUrl = "/api/login";

		String url = URL.encode(jsonUrl);

		// Send request to server and catch any errors.
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);

		builder.setUser(username);
		builder.setPassword(password);

		try {
			builder.sendRequest(null, new RequestCallback() {

				@Override
				public void onResponseReceived(Request request, Response response) {

					// To check for unauthorised!
					if (response.getStatusCode() != 200) {

						asyncCallback.onSuccess(new LoginResponse(false, null, response.getStatusCode() + ""));

					} else {

						// This converts from JSON to a Java object
						LoginResponse deResponse = serializer.deSerialize(response.getText(), LoginResponse.class);

						GWT.log(deResponse.getMessage());
						GWT.log(response.getText());

						asyncCallback.onSuccess(deResponse);
					}

				}

				@Override
				public void onError(Request request, Throwable exception) {
					// TODO Auto-generated method stub

				}
			});
		} catch (RequestException e) {
			System.out.println("Couldn't retrieve JSON : " + e.getMessage() + " :getEventsForPage()");
		}
	}
}
