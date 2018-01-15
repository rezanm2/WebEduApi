package nl.webedu.models;

import nl.webedu.dao.SprintDAO;

import java.lang.Exception;

public class UserStoryModel {
	private int userStoryId;
	private String userStoryName;
	private String userStoryDescription;
	private int sprintFK;
	private String sprintName;
	private boolean isDeleted;
	private SprintDAO sprintDAO = new SprintDAO();
        private boolean isCurrent;

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

	public int getSprintFK() {
		return sprintFK;
	}

	public void setSprintFK(int sprintFK) throws Exception {
		this.sprintFK = sprintFK;
		
		for(int counter = 0; counter < this.sprintDAO.allSprints().size(); counter++)
		{
			if(sprintFK == sprintDAO.allSprints().get(counter).getCategoryId())
			{
				this.setSprintName(sprintDAO.allSprints().get(counter).getCategoryName());
				System.out.println("sprintDAO.sprint_list().get(counter).getSprintName()");
			};
		}
		
	}

	public String getSprintName() {
		return sprintName;
	}

	public void setSprintName(String sprintName) {
		this.sprintName = sprintName;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
        
        public boolean isCurrent() {
		return isCurrent;
	}

	public void setIsCurrent(boolean isCurrent) {
		this.isCurrent = isCurrent;
	}
	
}
