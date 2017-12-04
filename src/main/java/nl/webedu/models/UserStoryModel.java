package nl.webedu.models;

import nl.webedu.dao.SprintDAO;

public class UserStoryModel {
	private int userStoryId;
	private String userStoryName;
	private String userStoryDescription;
	private int sprintFK;
	private String sprintName;
	private boolean isDeleted;
	private SprintDAO sprintDAO;

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

	public void setSprintFK(int sprintFK) {
		this.sprintFK = sprintFK;
		
		for(int counter = 0; counter < sprintDAO.sprint_list().size(); counter++)
		{
			if(sprintFK == sprintDAO.sprint_list().get(counter).getSprintId())
			{
				this.setSprintName(sprintDAO.sprint_list().get(counter).getSprintName());
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
	
}
