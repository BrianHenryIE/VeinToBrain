package ie.brianhenry.veintobrain.client.view;

import ie.brianhenry.veintobrain.client.RpcService;
import ie.brianhenry.veintobrain.client.events.LoginEvent;
import ie.brianhenry.veintobrain.shared.LoginResponse;

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

	FlowPanel p = new FlowPanel();

	final Button sendButton = new Button("Send!");
	final TextBox nameField = new TextBox();

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

		nameField.setText("GWT User");

		// We can add style names to widgets
		sendButton.addStyleName("sendButton");

		p.add(nameField);
		p.add(passwordField);

		p.add(sendButton);
		p.add(errorLabel);

		// Focus the cursor on the name field when the app loads
		nameField.setFocus(true);
		nameField.selectAll();

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
				sendButton.setEnabled(true);
				sendButton.setFocus(true);

			}
		});

		// Add a handler to send the name to the server
		MyHandler handler = new MyHandler();
		sendButton.addClickHandler(handler);
		nameField.addKeyUpHandler(handler);
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
			String textToServer = nameField.getText();

			// Then, we send the input to the server.
			sendButton.setEnabled(false);
			textToServerLabel.setText(textToServer);
			serverResponseLabel.setText("");

			rpcService.sendPassword(nameField.getText(), passwordField.getText(), new AsyncCallback<LoginResponse>() {
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