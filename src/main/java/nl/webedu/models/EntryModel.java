package nl.webedu.models;

/**
 * Date: 9-10-2017
 * @author Robert
 */
public class EntryModel {
	private int entryId;
	//private String entryName;
	private String entryDescription;
//	private Exception entryException;
	private String entryStatus;
	private String entryDate;
	private String entryStartTime;
	private String entryEndTime;
	private Boolean entryIsLocked;
	private String entryProjectDescription;
	private String entrySprintDescription;
	private String entryUserStoryDescription;
        private int employeeFk;
        private String employeeName;
	private int entryProjectFk;
        private String entryProjectName;
	private int entrySprintFk;
        private String entrySprintName;
	private int entryUserstoryFk;
        private String entryUserstoryName;
        private Boolean isDeleted;
        private Boolean isCurrent;
        
        public int getEmployeeFk(){
            return employeeFk;
        }
        
        public void setEmployeeFk(int employeeFK){
            this.employeeFk=employeeFK;
        }
        
        public String getEntryEmployeeName() {
		return employeeName;
	}
        public void setEntryEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
        
        public Boolean getIsDeleted(){
            return isDeleted;
        }
        
        public void setIsDeleted(boolean isDeleted){
            this.isDeleted=isDeleted;
        }
        public Boolean getIsCurrent(){
            return isCurrent;
        }
        
        public void setIsCurrent(boolean isCurrent){
            this.isCurrent=isCurrent;
        }
	
	public String getEntryUserStoryDescription() {
		return entryUserStoryDescription;
	}
	public void setEntryUserStoryDescription(String entryUserStoryDescription) {
		this.entryUserStoryDescription = entryUserStoryDescription;
	}
	public String getEntryProjectDescription() {
		return entryProjectDescription;
	}
	public void setEntryProjectDescription(String entryProjectDescription) {
		this.entryProjectDescription = entryProjectDescription;
	}
	public String getEntrySprintDescription() {
		return entrySprintDescription;
	}
	public void setEntrySprintDescription(String entrySprintDescription) {
		this.entrySprintDescription = entrySprintDescription;
	}
	public int getEntryId() {
		return entryId;
	}
	public void setEntryId(int entryId) {
		this.entryId = entryId;
	}
	
//	public String getEntryName() {
//		return entryName;
//	}
//	public void setEntryName(String entryName) {
//		this.entryName = entryName;
//	}
	public String getEntryDescription() {
		return entryDescription;
	}
	public void setEntryDescription(String entryDescription) {
		this.entryDescription = entryDescription;
	}
//	public Exception getEntryException() {
//		return entryException;
//	}
//	public void setEntryException(Exception entryException) {
//		this.entryException = entryException;
//	}
	public String getEntryStatus() {
		return entryStatus;
	}
	public void setEntryStatus(String entryStatus) {
		this.entryStatus = entryStatus;
	}
	public String getEntryDate() {
		return entryDate;
	}
	public void setEntryDate(String entryDate) {
		this.entryDate = entryDate;
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
	public Boolean getEntryIsLocked() {
		return entryIsLocked;
	}
	public void setEntryIsLocked(Boolean entryIsLocked) {
		this.entryIsLocked = entryIsLocked;
	}
	public int getEntryProjectFk() {
		return entryProjectFk;
	}
	public void setEntryProjectFk(int entryProjectFk) {
		this.entryProjectFk = entryProjectFk;
	}
        public String getEntryProjectName() {
		return entryProjectName;
	}
        public void setEntryProjectName(String entryProjectName) {
		this.entryProjectName = entryProjectName;
	}
	public int getEntrySprintFk() {
		return entrySprintFk;
	}
	public void setEntrySprintFk(int entrySprintFk) {
		this.entrySprintFk = entrySprintFk;
	}
        public String getEntrySprintName() {
		return entrySprintName;
	}
        public void setEntrySprintName(String entrySprintName) {
		this.entrySprintName = entrySprintName;
	}
	public int getEntryUserstoryFk() {
		return entryUserstoryFk;
	}
	public void setEntryUserstoryFk(int entryUserstoryFk) {
		this.entryUserstoryFk = entryUserstoryFk;
	}
        public String getEntryUserstoryName() {
		return entryUserstoryName;
	}
        public void setEntryUserstoryName(String entryUserstoryName) {
		this.entryUserstoryName = entryUserstoryName;
	}
}
