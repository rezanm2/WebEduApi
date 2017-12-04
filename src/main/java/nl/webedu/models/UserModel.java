package nl.webedu.models;

/**
 * User database representation
 */
public class UserModel {

    private int userId;
    private String userFirstName;
    private String userLastName;
    private String email;
    private String password;

    public UserModel(){}

    public UserModel(int id, String fName, String lName){
        this.userId = id;
        this.userFirstName = fName;
        this.userLastName = lName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }
}
