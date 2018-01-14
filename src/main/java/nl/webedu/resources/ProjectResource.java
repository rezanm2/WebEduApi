/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.webedu.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Optional;
import io.dropwizard.auth.Auth;
import nl.webedu.dao.ProjectDAO;
import nl.webedu.models.EmployeeModel;
import nl.webedu.models.ProjectModel;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.Valid;

/**
 *
 * @author rezanaser
 */
@Path("/projects")
public class ProjectResource {
    private ProjectDAO projectDAO;

    public ProjectResource(){
        projectDAO = new ProjectDAO();
        projectDAO.createAddProjectFunction();
    }

    @GET
    @Path("/read")
    @JsonProperty
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ArrayList<ProjectModel> read(@Auth EmployeeModel employeeModel){
        return this.projectDAO.getAllProjects();
    }

    @GET
    @Path("/read/by_employee/")
    @JsonProperty
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ArrayList<ProjectModel> readFromUrl(@QueryParam("empid") Optional<String> employeeId){
        int employeeId_parse = Integer.parseInt(employeeId.get());
        return this.projectDAO.project_list_employee(employeeId_parse);
    }
    
    @POST
    @Path("/create")
    public boolean createProject(@Valid ProjectModel projectModel){
        projectDAO.addProject(projectModel);
        return true;
    }
        
    /**
     * De method mist een manier om de de klant aan te passen,
     * want de method in de DAO ondersteunt dat niet
     *
     * @param projectModel is het projectmodel gekregen van de front-end
     * @return true
     */
    @PUT
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean update(@Valid ProjectModel projectModel){
        try {
            projectDAO.modifyProject(projectModel);
        } catch (Exception ex) {
            Logger.getLogger(ProjectResource.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        System.out.println(this.getClass().toString()+": update werkt!: ");
        return true;
    }

    @POST
    @Path("/delete")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean delete(@Valid ProjectModel projectModel){
        projectDAO.removeProject(projectModel);
        System.out.println(this.getClass().toString()+": update werkt!: ");
        return true;
    }

    @POST
    @Path("/undelete/url")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean unDeleteByUrl(@QueryParam("pid") Optional<String> projectId){
        int projectId_parse = Integer.parseInt(projectId.get());
        this.projectDAO.unRemoveProject(projectId_parse);
        System.out.println(this.getClass().toString()+": update werkt!: ");
        return true;
    }
}
