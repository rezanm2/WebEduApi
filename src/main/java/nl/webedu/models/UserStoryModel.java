package nl.webedu.models;

/**
 * User story database representation
 */
public class UserStoryModel {
    private int userStoryId;
    private String userStoryName;
    private String userStoryDescription;

    public int getUserStoryId() {
        return userStoryId;
    }

    public void setUserStoryId(int userStoryId) {
        this.userStoryId = userStoryId;
    }

    public String getUserStoryName() {
        return userStoryName;
    }

    public void setUserStoryName(String userStoryName) {
        this.userStoryName = userStoryName;
    }

    public String getUserStoryDescription() {
        return userStoryDescription;
    }

    public void setUserStoryDescription(String userStoryDescription) {
        this.userStoryDescription = userStoryDescription;
    }
}
