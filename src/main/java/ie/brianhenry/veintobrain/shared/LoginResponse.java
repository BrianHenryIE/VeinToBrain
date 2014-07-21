package ie.brianhenry.veintobrain.shared;


import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.kfuntak.gwt.json.serialization.client.JsonSerializable;

//@GwtIncompatible
@JsonTypeInfo(use=Id.CLASS, include=As.PROPERTY, property="class")
public class LoginResponse implements JsonSerializable {

	// @JsonProperty("class")
	// public String classname = LoginResponse.class.getName();
	
	public boolean success;
	public User user;
	private String message;

	public LoginResponse(){}
	
	public LoginResponse(boolean success, User user, String message) {
		this.success = success;
		this.user = user;
		this.message = message;
	}

	public boolean getSuccess() {
		return this.success;
	}

	public String getMessage() {
		return this.message;
	}	

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public User getUser() {
		return user;
	}

}