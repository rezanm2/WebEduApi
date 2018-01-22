/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.webedu.resources;

import io.dropwizard.auth.Auth;
import nl.webedu.models.EmployeeModel;
import nl.webedu.models.ProjectModel;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import javax.validation.Valid;
import nl.webedu.services.ProjectService;

/**
 *
 * @author rezanaser
 */
@Path("/projects")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProjectResource {
    private ProjectService projectService;

    public ProjectResource(){
        projectService = new ProjectService();
    }
    
    
    @GET
    @Path("/read")
    public ArrayList<ProjectModel> read(@Auth EmployeeModel employeeModel){
        return this.projectService.read();
    }

    @GET
    @Path("/read/by_employee/")
    public ArrayList<ProjectModel> readFromUrl(@QueryParam("empid") int employeeId){
        return this.projectService.readFromUrl(employeeId);
    }
    
    @POST
    @Path("/create")
    public boolean createProject(@Valid ProjectModel projectModel, @Auth EmployeeModel loggedUser){
        return projectService.createProject(projectModel, loggedUser);
    }
    @PUT
    @Path("/update")
    public boolean update(@Valid ProjectModel projectModel, @Auth EmployeeModel loggedUser) throws Exception{
        return projectService.update(projectModel, loggedUser);
    }

    @POST
    @Path("/delete")
    public boolean delete(@Valid ProjectModel projectModel, @Auth EmployeeModel loggedUser){
        return projectService.delete(projectModel, loggedUser);
    }

    @POST
    @Path("/undelete/url")
    public boolean unDeleteByUrl(@QueryParam("pid") int projectId, @Auth EmployeeModel loggedUser){
        return projectService.unDeleteByUrl(projectId, loggedUser);
    }
}
