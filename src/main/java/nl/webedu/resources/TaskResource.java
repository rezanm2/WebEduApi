/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.webedu.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Optional;
import io.dropwizard.auth.Auth;
import nl.webedu.models.EmployeeModel;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.Valid;
import nl.webedu.dao.TaskDAO;
import nl.webedu.models.TaskModel;

/**
 *
 * @author rezanaser
 */
@Path("/userstories")
public class TaskResource {
    private TaskDAO userStoryDAO;

    public TaskResource(){
        userStoryDAO = new TaskDAO();
        userStoryDAO.createAddUserStoryFunction();
    }

    @GET
    @Path("/read")
    @JsonProperty
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ArrayList<TaskModel> read(@Auth EmployeeModel employeeModel){
        return this.userStoryDAO.userStory_list();
    }

    @GET
    @Path("/read/by_employee/")
    @JsonProperty
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ArrayList<TaskModel> readFromUrl(@QueryParam("empid") Optional<String> employeeId){
        int employeeId_parse = Integer.parseInt(employeeId.get());
        return this.userStoryDAO.userStory_list_employee(employeeId_parse);
    }
    
    @POST
    @Path("/create")
    public boolean createProject(@Valid TaskModel userStoryModel){
        userStoryDAO.addUserStory(userStoryModel);
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
    public boolean update(@Valid TaskModel userStoryModel){
        try {
            userStoryDAO.modifyUserStory(userStoryModel);
        } catch (Exception ex) {
            Logger.getLogger(TaskResource.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(this.getClass().toString()+": update werkt niet!: ");
            return false;
        }
        System.out.println(this.getClass().toString()+": update werkt!: ");
        return true;
    }

    @POST
    @Path("/delete")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean delete(@Valid TaskModel userStoryModel){
        userStoryDAO.removeUserStory(userStoryModel);
        System.out.println(this.getClass().toString()+": delete werkt!: ");
        return true;
    }

    @POST
    @Path("/undelete/url")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean unDeleteByUrl(@QueryParam("pid") Optional<String> projectId){
        int projectId_parse = Integer.parseInt(projectId.get());
        this.userStoryDAO.unRemoveUserStory(projectId_parse);
        System.out.println(this.getClass().toString()+": update werkt!: ");
        return true;
    }
}
