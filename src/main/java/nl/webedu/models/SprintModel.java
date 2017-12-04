package nl.webedu.models;

/**
 * Sprint model database representation
 */
public class SprintModel {
    private int sprintId;

    private String sprintName;
    private String sprintDescription;

    private String sprintStartDate;
    private String sprintEndDate;

    private boolean sprintCurrent;

    public SprintModel(int id, String name, String description, String startDate, String endDate, boolean current){
        this.sprintId = id;
        this.sprintName = name;
        this.sprintDescription = description;
        this.sprintStartDate = startDate;
        this.sprintEndDate = endDate;
        this.sprintCurrent = current;
    }

    public int getSprintId() {
        return sprintId;
    }

    public void setSprintId(int sprintId) {
        this.sprintId = sprintId;
    }

    public boolean issprintCurrent() {
        return sprintCurrent;
    }

    public void setsprintCurrent(boolean sprintCurrent) {
        this.sprintCurrent = sprintCurrent;
    }

    public String getSprintName() {
        return sprintName;
    }

    public void setSprintName(String sprintName) {
        this.sprintName = sprintName;
    }

    public String getSprintStartDate() {
        return sprintStartDate;
    }

    public void setSprintStartDate(String sprintStartDate) {
        this.sprintStartDate = sprintStartDate;
    }

    public String getSprintEndDate() {
        return sprintEndDate;
    }

    public void setSprintEndDate(String sprintEndDate) {
        this.sprintEndDate = sprintEndDate;
    }

    public String getSprintDescription() {
        return sprintDescription;
    }

    public void setSprintDescription(String sprintDescription) {
        this.sprintDescription = sprintDescription;
    }
}
