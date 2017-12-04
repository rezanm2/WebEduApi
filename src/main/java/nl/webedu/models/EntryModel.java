package nl.webedu.models;

public class EntryModel {
    private int entryId;
    private String entryDescription;

    private boolean entryStatus;
    private boolean entryIsLocked;
    private boolean entryIsDeleted;
    private boolean entryCurrent;

    private String entryStartTime;
    private String entryEndTime;

    public EntryModel(int id, String description, boolean status, boolean locked, boolean deleted, boolean current, String startTime, String endTime){
        this.entryId = id;
        this.entryDescription = description;
        this.entryStatus = status;
        this.entryIsDeleted = deleted;
        this.entryCurrent = current;
        this.entryStartTime = startTime;
        this.entryEndTime = endTime;
    }

    public int getEntryId() {
        return entryId;
    }

    public void setEntryId(int entryId) {
        this.entryId = entryId;
    }

    public String getEntryDescription() {
        return entryDescription;
    }

    public void setEntryDescription(String entryDescription) {
        this.entryDescription = entryDescription;
    }

    public boolean isEntryStatus() {
        return entryStatus;
    }

    public void setEntryStatus(boolean entryStatus) {
        this.entryStatus = entryStatus;
    }

    public boolean isEntryIsLocked() {
        return entryIsLocked;
    }

    public void setEntryIsLocked(boolean entryIsLocked) {
        this.entryIsLocked = entryIsLocked;
    }

    public boolean isEntryIsDeleted() {
        return entryIsDeleted;
    }

    public void setEntryIsDeleted(boolean entryIsDeleted) {
        this.entryIsDeleted = entryIsDeleted;
    }

    public boolean isEntryCurrent() {
        return entryCurrent;
    }

    public void setEntryCurrent(boolean entryCurrent) {
        this.entryCurrent = entryCurrent;
    }

    public String getEntryStartTime() {
        return entryStartTime;
    }

    public void setEntryStartTime(String entryStartTime) {
        this.entryStartTime = entryStartTime;
    }

    public String getEntryEndTime() {
        return entryEndTime;
    }

    public void setEntryEndTime(String entryEndTime) {
        this.entryEndTime = entryEndTime;
    }
}
