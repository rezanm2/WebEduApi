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
    public ArrayList<ProjectModel> ProjectName(@Auth EmployeeModel employeeModel){
        return this.projectDAO.getAllProjects();
    }

    @GET
    @Path("/read/all")
    @JsonProperty
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ArrayList<ProjectModel> AllProjects(){
        return this.projectDAO.getAllProjects();
    }

    @GET
    @Path("/read/project-by-employee")
    @JsonProperty
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ArrayList<ProjectModel> read(@FormParam("empid") Optional<String> employeeId){
        int employeeId_parse = Integer.parseInt(employeeId.get());
        System.out.println(this.getClass().toString()+": read werkt!");
        return projectDAO.project_list_employee(employeeId_parse);
    }

    @GET
    @Path("/read/byemployee/url")
    @JsonProperty
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ArrayList<ProjectModel> readFromUrl(@QueryParam("empid") Optional<String> employeeId){
        int employeeId_parse = Integer.parseInt(employeeId.get());
        System.out.println(this.getClass().toString()+": read werkt!");
        return this.projectDAO.project_list_employee(employeeId_parse);
    }
    
    @POST
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean create(@FormParam("name") Optional<String> name,
            @FormParam("description") Optional<String> description,
            @FormParam("custid") Optional<String> customerId){

        String name_parse = name.get();
        String description_parse = description.get();
        int customerId_parse = Integer.parseInt(customerId.get());
        projectDAO.addProject(name_parse, description_parse, customerId_parse);
        System.out.println(this.getClass().toString()+": create werkt!: "+name_parse+description_parse+customerId_parse);
        return true;
    }

    @POST
    @Path("/create/url")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean createByUrl(@QueryParam("name") Optional<String> name,
            @QueryParam("description") Optional<String> description,
            @QueryParam("custid") Optional<String> customerId){
        String name_parse = name.get();
        System.out.println(name_parse);
        String description_parse = description.get();
        System.out.println(description_parse);
        int customerId_parse = Integer.parseInt(customerId.get());
        System.out.println(customerId_parse);
        this.projectDAO.addProject(name_parse, description_parse, customerId_parse);
        System.out.println(this.getClass().toString()+": create werkt!: "+name_parse+description_parse+customerId_parse);
        return true;
    }
        
    /**
     * De method mist een manier om de de klant aan te passen,
     * want de method in de DAO ondersteunt dat niet
     *
     * @param projectId id van bestaand project
     * @param name      nieuwe naam van project
     * @param description   nieuwe beschrijving vvan project
     * @return true
     */
    @PUT
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)

    public boolean update(@FormParam("pid") Optional<String> projectId,
            @FormParam("name") Optional<String> name,
            @FormParam("description") Optional<String> description){

        int projectId_parse = Integer.parseInt(projectId.get());
        String name_parse = name.get();
        String description_parse = description.get();
        try {
            projectDAO.modifyProject(projectId_parse,name_parse, description_parse);
        } catch (Exception ex) {
            Logger.getLogger(ProjectResource.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        System.out.println(this.getClass().toString()+": update werkt!: "+name_parse+description_parse);
        return true;
    }

    @POST
    @Path("/update/url")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean updateByUrl(@QueryParam("pid") Optional<String> projectId,
            @QueryParam("name") Optional<String> name,
            @QueryParam("description") Optional<String> description) throws Exception{
        int projectId_parse = Integer.parseInt(projectId.get());
        String name_parse = name.get();
        String description_parse = description.get();
        projectDAO.modifyProject(projectId_parse, name_parse, description_parse);
        System.out.println(this.getClass().toString()+": update werkt!: "+name_parse+description_parse);
        return true;
    }

    @DELETE
    @Path("/delete")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean delete(@FormParam("pid") Optional<String> projectId){
        int projectId_parse = Integer.parseInt(projectId.get());
        projectDAO.removeProject(projectId_parse);
        System.out.println(this.getClass().toString()+": update werkt!: ");
        return true;
    }

    @POST
    @Path("/delete/url")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean deleteByUrl(@QueryParam("pid") Optional<String> projectId){
        int projectId_parse = Integer.parseInt(projectId.get());
        this.projectDAO.removeProject(projectId_parse);
        System.out.println(this.getClass().toString()+": update werkt!: ");
        return true;
    }

    @POST
    @Path("/undelete")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean unDelete(@FormParam("pid") Optional<String> projectId){

        int projectId_parse = Integer.parseInt(projectId.get());
        projectDAO.unRemoveProject(projectId_parse);
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
        
    @GET
    @Path("/secured")
    @JsonProperty
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public String test(@Auth EmployeeModel employeeModel){
        return "SECURED";
    }
}
