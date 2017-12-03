package nl.webedu.models;

public class UserModel {

    private int userId;
    private String userFirstName;
    private String userLastName;

    public UserModel(int id, String fName, String lName){
        this.userId = id;
        this.userFirstName = fName;
        this.userLastName = lName;
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
