package ie.brianhenry.veintobrain.shared;

import com.kfuntak.gwt.json.serialization.client.JsonSerializable;

public class LoginResponse implements JsonSerializable {
//	public boolean success;
//	public String username;
	private String message;

	public LoginResponse(){}
	
//	public LoginResponse(boolean success, String username, String password) {
//		this.success = success;
//		this.username = username;
//		this.message = password;
//	}
//
//	public boolean getSuccess() {
//		return this.success;
//	}
//
//	public String getUsername() {
//		return this.username;
//	}

	public String getMessage() {
		return this.message;
	}

}