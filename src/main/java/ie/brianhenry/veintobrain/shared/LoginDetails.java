package ie.brianhenry.veintobrain.shared;

import com.kfuntak.gwt.json.serialization.client.JsonSerializable;

public class LoginDetails implements JsonSerializable {

	private String username;

	private String password;

	public LoginDetails() {
	}

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

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}