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
    public boolean createProject(@Valid ProjectModel projectModel){
        return projectService.createProject(projectModel);
    }
    @PUT
    @Path("/update")
    public boolean update(@Valid ProjectModel projectModel) throws Exception{
        return projectService.update(projectModel);
    }

    @POST
    @Path("/delete")
    public boolean delete(@Valid ProjectModel projectModel){
        return projectService.delete(projectModel);
    }

    @POST
    @Path("/undelete/url")
    public boolean unDeleteByUrl(@QueryParam("pid") int projectId){
        return projectService.unDeleteByUrl(projectId);
    }
}
