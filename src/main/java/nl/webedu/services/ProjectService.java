package nl.webedu.services;

import com.google.common.base.Optional;
import io.dropwizard.auth.Auth;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.Valid;
import javax.ws.rs.QueryParam;
import nl.webedu.dao.ProjectDAO;
import nl.webedu.models.EmployeeModel;
import nl.webedu.models.ProjectModel;
import nl.webedu.resources.ProjectResource;

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
    
    public boolean createProject(ProjectModel projectModel){
        return projectDAO.addProject(projectModel);
    }
        
    public boolean update(ProjectModel projectModel) throws Exception{
         return projectDAO.modifyProject(projectModel);
    }

    public boolean delete(ProjectModel projectModel){
        return projectDAO.removeProject(projectModel);
    }

    public boolean unDeleteByUrl(int projectId){
        return this.projectDAO.unRemoveProject(projectId);
    }
    

}
