/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.webedu.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import javax.ws.rs.QueryParam;
import com.google.common.base.Optional;
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
    @GET
    @JsonProperty
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ArrayList<ProjectModel> ProjectName(){
        ProjectDAO projectDAO = new ProjectDAO();
        System.out.println(this.getClass().toString()+": GET Werkt!");
        return projectDAO.getAllProjects();
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
        public String confirmation(@QueryParam("name") Optional<String> name,
                @QueryParam("description") Optional<String> description,
                @QueryParam("custid") Optional<String> customerId){

        String name_parse = name.get();
        String description_parse = description.get();
        int customerId_parse = Integer.parseInt(customerId.get());
//        String name_parse = String.format("%b", name.or("empty"));
//        String description_parse = String.format("%b", description.or("empty"));
//        String customerId_parse1 = String.format("%b", customerId.or("empty"));
//        int customerId_parse2 = Integer.parseInt(customerId_parse1);
        
        ProjectDAO projectDAO = new ProjectDAO();
        projectDAO.addProject(name_parse, description_parse, customerId_parse);
        System.out.println(this.getClass().toString()+": POST Werkt!: "+name_parse+description_parse+customerId_parse);
        return "Kelukt";
    }
}
