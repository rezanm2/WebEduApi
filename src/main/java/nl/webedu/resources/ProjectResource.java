/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.webedu.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import nl.webedu.dao.ProjectDAO;
import nl.webedu.models.ProjectModel;

/**
 *
 * @author rezanaser
 */
@Path("/projects")
public class ProjectResource {
    private ProjectDAO projectDAO;

    public ProjectResource(){
        projectDAO = new ProjectDAO();
    }

    @GET
    @JsonProperty
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ArrayList<ProjectModel> ProjectName(){
        return this.projectDAO.getAllProjects();
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String confirmation(){
        this.projectDAO.createProject();
        return "Gelukt";
    }
}