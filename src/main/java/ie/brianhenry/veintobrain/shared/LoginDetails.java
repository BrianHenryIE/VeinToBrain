package ie.brianhenry.veintobrain.shared;

import com.kfuntak.gwt.json.serialization.client.JsonSerializable;

public class LoginDetails implements JsonSerializable {
	public String username;
	public String password;

	public LoginDetails(){}
	
	public LoginDetails(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return this.password;
	}

}