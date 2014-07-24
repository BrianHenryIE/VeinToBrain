package ie.brianhenry.veintobrain.representations;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.kfuntak.gwt.json.serialization.client.JsonSerializable;

/**
 * Implements the response to show after the Login button is clicked
 */
@JsonTypeInfo(use = Id.CLASS, include = As.PROPERTY, property = "class")
public class LoginResponse implements JsonSerializable {

	public boolean success;
	public User user;
	private String message;

	/**
	 * Constructor
	 */
	public LoginResponse() {
	}

	/**
	 * Constructor
	 * @param success whether the login succeed or not
	 * @param user username
	 * @param message the string message to show the user
	 */
	public LoginResponse(boolean success, User user, String message) {
		this.success = success;
		this.user = user;
		this.message = message;
	}


	/**
	 * @return success field
	 */
	public boolean getSuccess() {
		return this.success;
	}

	/**
	 * @return username
	 */
	public User getUser() {
		return user;
	}
	
	/**
	 * @return message
	 */
	public String getMessage() {
		return this.message;
	}

	/**
	 * sets the success field
	 * @param success
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}

	/**
	 * sets the username
	 * @param user
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * sets the message to show the user
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

}