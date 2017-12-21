package nl.webedu.models;

public class ProjectModel {
	
        private int projectId;
        private String projectName;
        private String projectDescription;
	private boolean projectIsDeleted;
	private int projectCustomerFk;
        private String projectCustomerName;
        private boolean isCurrent;
;	

    public String getCustomerName() {
        return projectCustomerName;
    }

    public void setCustomerName(String customerName) {
        this.projectCustomerName = customerName;
    }
	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public boolean isProjectIsDeleted() {
		return projectIsDeleted;
	}

	public void setProjectIsDeleted(boolean projectIsDeleted) {
		this.projectIsDeleted = projectIsDeleted;
	}

	public String getProjectDescription() {
		return projectDescription;
	}

	public void setProjectDescription(String projectDescription) {
		this.projectDescription = projectDescription;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public int getProjectCustomerFk() {
		return projectCustomerFk;
	}

	public void setProjectCustomerFk(int projectCustomerFk) {
		this.projectCustomerFk = projectCustomerFk;
	}
        
     /**
     * @author Robert
     * @return the isCurrent
     */
    public boolean getIsCurrent() {
        return isCurrent;   
    }

    /**
     * @author Robert
     * @param isCurrent the isCurrent to set
     */
    public void setIsCurrent(boolean isCurrent) {
        this.isCurrent = isCurrent;
    }

}
