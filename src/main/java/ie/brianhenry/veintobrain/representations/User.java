package ie.brianhenry.veintobrain.representations;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.kfuntak.gwt.json.serialization.client.JsonSerializable;


/**
 * Implements the user credentials
 */
@JsonTypeInfo(use=Id.CLASS, include=As.PROPERTY, property="class")
public class User implements JsonSerializable {

    // TODO: hash password
    /**
     * Constructor
     */
    public User() {}

    /**
     * Contructor
     * @param name username
     * @param password
     */
    public User(String name, String password) {
        setUsername(name);
        setPassword(password);
    }

    private String username;
    private String password;

    /**
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return password
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * sets the username
     * @param name
     */
    public void setUsername(String name) {
        username = name;
    }
    
    /**
     * sets the password
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

}