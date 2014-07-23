package ie.brianhenry.veintobrain.client.view;

import ie.brianhenry.veintobrain.client.RpcService;
import ie.brianhenry.veintobrain.client.events.LoginEvent;
import ie.brianhenry.veintobrain.representations.LoginResponse;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class LoginClientView implements IsWidget {

	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network " + "connection and try again.";
	
	VerticalPanel p = new VerticalPanel();
	
	final Button loginButton = new Button("Login");
	final Label userLabel = new Label("Username");
	final TextBox userField = new TextBox();
	final Label passwordLabel = new Label("Password");
	final PasswordTextBox passwordField = new PasswordTextBox();

	final Label errorLabel = new Label();

	final Label textToServerLabel = new Label();
	final HTML serverResponseLabel = new HTML();

	final DialogBox dialogBox = new DialogBox();
	final Button closeButton = new Button("Close");

	private RpcService rpcService;

	private EventBus eventBus;

	public LoginClientView(RpcService rpcService, EventBus eventBus) {

		this.rpcService = rpcService;
		this.eventBus = eventBus;	

		userField.setText("GWT User");
		
		// We can add style names to widgets
		loginButton.addStyleName("loginButton");

		p.add(userLabel);
		p.add(userField);
		p.add(passwordLabel);
		p.add(passwordField);
		p.add(loginButton);
		
		// Focus the cursor on the name field when the app loads
		userField.setFocus(true);
		userField.selectAll();

		// Create the popup dialog box
		dialogBox.setText("Remote Procedure Call");
		dialogBox.setAnimationEnabled(true);

		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");

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

			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				loginButton.setEnabled(true);
				loginButton.setFocus(true);

			}
		});

		// Add a handler to send the name to the server
		MyHandler handler = new MyHandler();
		loginButton.addClickHandler(handler);
		userField.addKeyUpHandler(handler);
	}

	@Override
	public Widget asWidget() {

		return p;

	}

	

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
			String textToServer = userField.getText();

			// Then, we send the input to the server.
			loginButton.setEnabled(false);
			textToServerLabel.setText(textToServer);
			serverResponseLabel.setText("");

			rpcService.sendPassword(userField.getText(), passwordField.getText(), new AsyncCallback<LoginResponse>() {
				public void onFailure(Throwable caught) {
					// Show the RPC error message to the user
					dialogBox.setText("Remote Procedure Call - Failure");
					serverResponseLabel.addStyleName("serverResponseLabelError");
					serverResponseLabel.setHTML(SERVER_ERROR);
					dialogBox.center();
					closeButton.setFocus(true);
				}

				public void onSuccess(LoginResponse result) {

					if (result.getSuccess())
						eventBus.fireEvent(new LoginEvent(result.getUser()));
					else {
						dialogBox.setText("Remote Procedure Call");
						serverResponseLabel.removeStyleName("serverResponseLabelError");
						serverResponseLabel.setHTML(result.getMessage());
						dialogBox.center();
						closeButton.setFocus(true);
					}
				}
			});
		}
	}

}