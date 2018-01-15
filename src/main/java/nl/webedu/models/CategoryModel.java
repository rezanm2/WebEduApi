package nl.webedu.models;

import nl.webedu.dao.ProjectDAO;

import java.util.ArrayList;

/**
 * Date 9-10-2017
 * 
 * @author Robert
 */
public class CategoryModel {

    
	private int categoryId;
	private boolean categoryIsDeleted;
	private String categoryName;
	private String categoryStartDate;
	private String categoryEndDate;
	private String categoryDescription;
	private int projectFK;
	private String projectName;
        private boolean isCurrent;
	private ProjectDAO projectDAO = new ProjectDAO();
	private ArrayList<EntryModel> entries;
        private boolean isDeleted;
	
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getCategoryStartDate() {
		return categoryStartDate;
	}
	public void setCategoryStartDate(String categoryStartDate) {
		this.categoryStartDate = categoryStartDate;
	}
	public String getCategoryEndDate() {
		return categoryEndDate;
	}
	public void setCategoryEndDate(String categoryEndDate) {
		this.categoryEndDate = categoryEndDate;
	}
	public String getCategoryDescription() {
		return categoryDescription;
	}
	public void setCategoryDescription(String categoryDescription) {
		this.categoryDescription = categoryDescription;
	}
	public ArrayList<EntryModel> getEntries() {
		return entries;
	}
	public void setEntries(ArrayList<EntryModel> entries) {
		this.entries = entries;
	}
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public boolean isCategoryIsDeleted() {
		return categoryIsDeleted;
	}
	public void setCategoryIsDeleted(boolean categoryIsDeleted) {
		this.categoryIsDeleted = categoryIsDeleted;
	}
	public int getProjectFK() {
		return projectFK;
	}
	public void setProjectFK(int projectFK) 
	{
		this.projectFK = projectFK;
		
//		for(int counter = 0; counter < projectDAO.getAllProjects().size(); counter++) {
//			if(projectFK == projectDAO.getAllProjects().get(counter).getProjectId()) {
//				this.setProjectName(projectDAO.getAllProjects().get(counter).getProjectName());
//			}
//		}
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
        
        public boolean getIsCurrent() {
		return this.isCurrent;
	}
	public void setIsCurrent(boolean isCurrent) {
		this.isCurrent=isCurrent;
	}
       /**
        * @author Robert
        * @return isDeleted
        */
       public boolean isIsDeleted() {
           return isDeleted;
       }

       /**
        * @author Robert
        * @param isDeleted the isDeleted to set
        */
       public void setIsDeleted(boolean isDeleted) {
           this.isDeleted = isDeleted;
       }
}
