package ie.brianhenry.veintobrain.shared;

import com.kfuntak.gwt.json.serialization.client.JsonSerializable;

public class User implements JsonSerializable {

    // TODO: hash password
    public User() {}

    public User(String name, String password) {
        setUsername(name);
        setPassword(password);
    }

    private String username;
    private String password;
    private String fullname;

    public String getUsername() {
        return username;
    }
    public void setUsername(String name) {
        username = name;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

}