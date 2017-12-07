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
    @Path("/read")
    @JsonProperty
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ArrayList<ProjectModel> read(){
        ProjectDAO projectDAO = new ProjectDAO();
        System.out.println(this.getClass().toString()+": read werkt!");
        return projectDAO.getAllProjects();
    }
    
    @POST
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
        public boolean create(@QueryParam("name") Optional<String> name,
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
        System.out.println(this.getClass().toString()+": create werkt!: "+name_parse+description_parse+customerId_parse);
        return true;
    }
        
        /**
         * De method mist een manier om de de klant aan te passen,
         * want de method in de DAO ondersteunt dat niet >:(
         * 
         * @param projectId id van bestaand project
         * @param name      nieuwe naam van project
         * @param description   nieuwe beschrijving vvan project
         * @return true
         */
    @POST
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
        public boolean update(@QueryParam("pid") Optional<String> projectId,
                @QueryParam("name") Optional<String> name,
                @QueryParam("description") Optional<String> description){

        int projectId_parse = Integer.parseInt(projectId.get());
        String name_parse = name.get();
        String description_parse = description.get();
//        String name_parse = String.format("%b", name.or("empty"));
//        String description_parse = String.format("%b", description.or("empty"));
//        String customerId_parse1 = String.format("%b", customerId.or("empty"));
//        int customerId_parse2 = Integer.parseInt(customerId_parse1);
        
        ProjectDAO projectDAO = new ProjectDAO();
        projectDAO.modifyProject(projectId_parse,name_parse, description_parse);
        System.out.println(this.getClass().toString()+": update werkt!: "+name_parse+description_parse);
        return true;
    }
    @POST
    @Path("/delete")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
        public boolean delete(@QueryParam("pid") Optional<String> projectId){

        int projectId_parse = Integer.parseInt(projectId.get());
//        String name_parse = String.format("%b", name.or("empty"));
//        String description_parse = String.format("%b", description.or("empty"));
//        String customerId_parse1 = String.format("%b", customerId.or("empty"));
//        int customerId_parse2 = Integer.parseInt(customerId_parse1);
        
        ProjectDAO projectDAO = new ProjectDAO();
        projectDAO.removeProject(projectId_parse);
        System.out.println(this.getClass().toString()+": update werkt!: ");
        return true;
    }
}
