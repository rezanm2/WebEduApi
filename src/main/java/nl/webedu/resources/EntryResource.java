/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.webedu.resources;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import javax.ws.rs.QueryParam;
import javax.ws.rs.FormParam;
import com.google.common.base.Optional;
import io.dropwizard.auth.Auth;
import java.sql.Date;
import java.sql.Time;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import nl.webedu.models.EntryModel;
import nl.webedu.helpers.DateHelper;
import nl.webedu.dao.EntryDAO;
import javax.validation.Valid;

import javax.ws.rs.Path;
import nl.webedu.models.EmployeeModel;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;

import nl.webedu.models.entrymodels.WeekModel;
import nl.webedu.services.*;

/**
 *
 * @author rezanaser
 */
@Path("/entries")
public class EntryResource {
    private EntryDAO entryDao;    
    private EntryService entryService;

    @Inject
    public EntryResource(){
        entryDao = new EntryDAO();
        this.entryService = new EntryService();
    }

    @GET
    @JsonProperty
    @Produces(MediaType.APPLICATION_JSON)
    public WeekModel read(@Auth EmployeeModel employeeModel,
                          @QueryParam("startdate") Optional<String> startDate){
        DateHelper dateHelper = new DateHelper();
        System.out.println(employeeModel.getEmployeeRole()+ startDate.get());
        return this.entryService.getEntriesWeek(employeeModel, dateHelper.parseDate(startDate.get(), "dd-MM-yyyy"));
    }
    @GET
    @Path("/queued")
    @JsonProperty
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ArrayList<EntryModel> readQueued(@Auth EmployeeModel employeeModel){
        return this.entryService.getEntriesQueued(employeeModel);
    }
    @PUT
    @Path("/approve")
    @JsonProperty
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean approve(int[] entryIds, @Auth EmployeeModel employeeModel){
        return this.entryService.approveEntries(entryIds, employeeModel);
    }
    @PUT
    @Path("/reject")
    @JsonProperty
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean reject(int[] entryIds, @Auth EmployeeModel employeeModel){
        return this.entryService.rejectEntries(entryIds, employeeModel);
    }
    
    @POST
    @JsonProperty
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean create(@Valid EntryModel entryModel, @Auth EmployeeModel employeeModel){
        System.out.println(this.getClass().toString()+": "+entryModel.getEntryDescription()
                +" auth: "+employeeModel.getEmployeeFirstname()
                +" date: "+entryModel.getEntryDate());
        if(entryModel.getEmployeeFk()!=employeeModel.getEmployeeId()&&!employeeModel.getEmployeeRole().equals("administration")){
            System.out.println(this.getClass().toString()+": non-admins mogen geen entries maken voor anderen.");
           return false;
        }else{
            return this.entryService.createEntry(entryModel);
        }
    }
    
    @POST
    @Path("/create/url")
    @JsonProperty
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean createByUrl(@QueryParam("empid") Optional<String> employeeId,
                        @QueryParam("projid") Optional<String> projectId,
                        @QueryParam("sprintid") Optional<String> sprintId,
                        @QueryParam("date") Optional<String> date,
                        @QueryParam("description") Optional<String> description,
                        @QueryParam("starttime") Optional<String> startTime,
                        @QueryParam("endtime") Optional<String> endTime,
                        @QueryParam("userstoryid") Optional<String> userstoryId){
        
        DateHelper dateHelper = new DateHelper();
        Date parsedDate = dateHelper.parseDate(date.get(),"dd-MM-yyyy");
        Time parsedStartTime = dateHelper.parseTime(startTime.get(), "HH:mm:ss");
        Time parsedEndTime = dateHelper.parseTime(endTime.get(), "HH:mm:ss");
        try {
            entryDao.addEntry(Integer.parseInt(employeeId.get()), 
                    Integer.parseInt(projectId.get()), 
                    Integer.parseInt(sprintId.get()), 
                    parsedDate, 
                    description.get(), 
                    parsedStartTime, 
                    parsedEndTime, 
                    Integer.parseInt(userstoryId.get()));
        } catch (NumberFormatException ex) {
            Logger.getLogger(EntryResource.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    
    @PUT
    @JsonProperty
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean update(@Valid EntryModel entryModel, @Auth EmployeeModel employeeModel){
        if(entryModel.getEmployeeFk()==employeeModel.getEmployeeId()
                &&!employeeModel.getEmployeeRole().equals("administration")){
            System.out.println(this.getClass().toString()+": non-admins mogen geen entries van anderen bewerken.");
            return false;
        }else{
            return this.entryService.updateEntry(entryModel);
        }
    }
    
    @DELETE
    @JsonProperty
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean delete(@QueryParam("entryid") Optional<String> entryId, @Auth EmployeeModel employeeModel){
        int parsedId=Integer.parseInt(entryId.get());
        if(parsedId==employeeModel.getEmployeeId()&&!employeeModel.getEmployeeRole().equals("administration")){
            System.out.println(this.getClass().toString()+": non-admins mogen geen entries van anderen VERWIJDEREN.");
            return false;
        }else{
            return this.entryService.deleteEntry(parsedId);
        }
    }
    
    @POST
    @Path("/delete/url")
    @JsonProperty
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean deleteByUrl(@QueryParam("entryid") Optional<String> entryId){
        try {
            entryDao.deleteEntry(Integer.parseInt(entryId.get()));
        } catch (NumberFormatException ex) {
            Logger.getLogger(EntryResource.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    
    @POST
    @Path("/undelete")
    @JsonProperty
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean unDelete(@FormParam("entryid") Optional<String> entryId){
        try {
            entryDao.unDeleteEntry(Integer.parseInt(entryId.get()));
        } catch (NumberFormatException ex) {
            Logger.getLogger(EntryResource.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    
    @POST
    @Path("/undelete/url")
    @JsonProperty
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean unDeleteByUrl(@QueryParam("entryid") Optional<String> entryId){
        try {
            entryDao.unDeleteEntry(Integer.parseInt(entryId.get()));
        } catch (NumberFormatException ex) {
            Logger.getLogger(EntryResource.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
}
