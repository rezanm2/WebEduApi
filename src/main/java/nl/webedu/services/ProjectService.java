package nl.webedu.services;

import java.util.ArrayList;
import nl.webedu.dao.ProjectDAO;
import nl.webedu.models.EmployeeModel;
import nl.webedu.models.ProjectModel;

public class ProjectService {
    private ProjectDAO projectDAO;

    public ProjectService(){
        projectDAO = new ProjectDAO();
    }
    
    public ArrayList<ProjectModel> read(){
        return this.projectDAO.getAllProjects();
    }
    public ArrayList<ProjectModel> readFromUrl(int employeeId){
        return this.projectDAO.project_list_employee(employeeId);
    }
    
    public boolean createProject(ProjectModel projectModel, EmployeeModel loggedUser){
        if(loggedUser.getEmployeeRole().equals("administration")){
            return projectDAO.addProject(projectModel);
        }else{return false;}
        
    }
        
    public boolean update(ProjectModel projectModel, EmployeeModel loggedUser) throws Exception{
        if(loggedUser.getEmployeeRole().equals("administration")){
           return projectDAO.modifyProject(projectModel); 
        }else{return false;}
         
    }

    public boolean delete(ProjectModel projectModel, EmployeeModel loggedUser){
        if(loggedUser.getEmployeeRole().equals("administration")){
            return projectDAO.removeProject(projectModel);
        }else{return false;}
        
    }

    public boolean unDeleteByUrl(int projectId, EmployeeModel loggedUser){
        if(loggedUser.getEmployeeRole().equals("administration")){
            return this.projectDAO.unRemoveProject(projectId);
        }else{return false;}
        
    }
    

}
