/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.webedu.resources;

import io.dropwizard.auth.Auth;
import nl.webedu.models.EmployeeModel;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import javax.validation.Valid;
import nl.webedu.models.TaskModel;
import nl.webedu.services.TaskService;

/**
 *
 * @author rezanaser
 */
@Path("/userstories")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TaskResource {
    private TaskService taskService;

    public TaskResource(){
        taskService = new TaskService();
    }

    @GET
    @Path("/read")
    public ArrayList<TaskModel> read(@Auth EmployeeModel employeeModel){
        return this.taskService.read(employeeModel);
    }

    @GET
    @Path("/read/by_employee/")
    public ArrayList<TaskModel> readFromUrl(@Auth EmployeeModel employeeModel, @QueryParam("empid") int employeeId){
        return this.taskService.readFromUrl(employeeModel, employeeId);
    }
    
    @POST
    @Path("/create")
    public boolean create(@Auth EmployeeModel employeeModel, @Valid TaskModel userStoryModel){
        taskService.create(employeeModel, userStoryModel);
        return true;
    }
        
    @PUT
    @Path("/update")
    public boolean update(@Auth EmployeeModel employeeModel, @Valid TaskModel userStoryModel){
        return taskService.update(employeeModel, userStoryModel);
    }

    @POST
    @Path("/delete")
    public boolean delete(@Auth EmployeeModel employeeModel, @Valid TaskModel userStoryModel){
        return  taskService.delete(employeeModel, userStoryModel);
    }

    @POST
    @Path("/undelete/url")
    public boolean unDeleteByUrl(@Auth EmployeeModel employeeModel, @QueryParam("pid") int projectId){
        return this.taskService.unDeleteByUrl(employeeModel, projectId);
    }
}
