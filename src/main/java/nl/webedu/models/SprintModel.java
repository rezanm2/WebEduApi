package nl.webedu.models;

import java.util.ArrayList;

import nl.webedu.dao.ProjectDAO;

/**
 * Date 9-10-2017
 * 
 * @author Robert
 */
public class SprintModel {
	private int sprintId;
	private boolean sprintIsDeleted;
	private String sprintName;
	private String sprintStartDate;
	private String sprintEndDate;
	private String sprintDescription;
	private int projectFK;
	private String projectName;
	private ProjectDAO projectDAO = new ProjectDAO();
	private ArrayList<EntryModel> entries;
	
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
	public ArrayList<EntryModel> getEntries() {
		return entries;
	}
	public void setEntries(ArrayList<EntryModel> entries) {
		this.entries = entries;
	}
	public int getSprintId() {
		return sprintId;
	}
	public void setSprintId(int sprintId) {
		this.sprintId = sprintId;
	}
	public boolean isSprintIsDeleted() {
		return sprintIsDeleted;
	}
	public void setSprintIsDeleted(boolean sprintIsDeleted) {
		this.sprintIsDeleted = sprintIsDeleted;
	}
	public int getProjectFK() {
		return projectFK;
	}
	public void setProjectFK(int projectFK) 
	{
		this.projectFK = projectFK;
		
		
		for(int counter = 0; counter < projectDAO.project_list().size(); counter++)
		{
			if(projectFK == projectDAO.project_list().get(counter).getProjectId())
			{
				this.setProjectName(projectDAO.project_list().get(counter).getProjectName());
			};
		}
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
}
