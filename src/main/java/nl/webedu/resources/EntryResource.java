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
    public ArrayList<EntryModel> readQueued(){
        return this.entryDao.entry_queued_list(0);
    }
    @POST
    @Path("/approve")
    @JsonProperty
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean approve(@FormParam("entryid") Optional<String> entryId){
        try {
            entryDao.approveHours(Integer.parseInt(entryId.get()));
        } catch (Exception ex) {
            Logger.getLogger(EntryResource.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    @POST
    @Path("/approve/url")
    @JsonProperty
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean approveByUrl(@QueryParam("entryid") Optional<String> entryId){
        try {
            entryDao.approveHours(Integer.parseInt(entryId.get()));
        } catch (Exception ex) {
            Logger.getLogger(EntryResource.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    @POST
    @Path("/reject")
    @JsonProperty
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean reject(@FormParam("entryid") Optional<String> entryId){
        try {
            entryDao.rejectHours(Integer.parseInt(entryId.get()));
        } catch (Exception ex) {
            Logger.getLogger(EntryResource.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    @POST
    @Path("/reject/url")
    @JsonProperty
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean rejectByUrl(@QueryParam("entryid") Optional<String> entryId){
        try {
            entryDao.rejectHours(Integer.parseInt(entryId.get()));
        } catch (Exception ex) {
            Logger.getLogger(EntryResource.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    
    @POST
    @JsonProperty
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean create(@Valid EntryModel entryModel, @Auth EmployeeModel employeeModel){
        System.out.println(this.getClass().toString()+": "+entryModel.getEntryDescription()+" auth: "+employeeModel.getEmployeeFirstname());
        if(entryModel.getEmployeeFk()==employeeModel.getEmployeeId()&&!employeeModel.getEmployeeRole().equals("administration")){
            System.out.println(this.getClass().toString()+": non-admins mogen geen entries maken voor anderen.");
           return false;
        }else{
            return this.entryService.createEntry(entryModel);
        }
        
//        DateHelper dateHelper = new DateHelper();
//        Date parsedDate = dateHelper.parseDate(date.get(),"dd-MM-yyyy");
//        Time parsedStartTime = dateHelper.parseTime(startTime.get(), "HH:mm:ss");
//        Time parsedEndTime = dateHelper.parseTime(endTime.get(), "HH:mm:ss");
//        try {
//            entryDao.addEntry(Integer.parseInt(employeeId.get()), 
//                    Integer.parseInt(projectId.get()), 
//                    Integer.parseInt(sprintId.get()), 
//                    parsedDate, 
//                    description.get(), 
//                    parsedStartTime, 
//                    parsedEndTime, 
//                    Integer.parseInt(userstoryId.get()));
//        } catch (NumberFormatException ex) {
//            Logger.getLogger(EntryResource.class.getName()).log(Level.SEVERE, null, ex);
//            return false;
//        }
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
            return this.entryService.createEntry(entryModel);
        }
    }
    
//    @POST
//    @Path("/update")
//    @JsonProperty
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    public boolean update(@FormParam("empid") Optional<String> employeeId,
//                        @FormParam("projid") Optional<String> projectId,
//                        @FormParam("sprintid") Optional<String> sprintId,
//                        @FormParam("date") Optional<String> date,
//                        @FormParam("description") Optional<String> description,
//                        @FormParam("starttime") Optional<String> startTime,
//                        @FormParam("endtime") Optional<String> endTime,
//                        @FormParam("userstoryid") Optional<String> userstoryId){
//        
//        DateHelper dateHelper = new DateHelper();
//        Date parsedDate = dateHelper.parseDate(date.get(),"dd-MM-yyyy");
//        Time parsedStartTime = dateHelper.parseTime(startTime.get(), "HH:mm:ss");
//        Time parsedEndTime = dateHelper.parseTime(endTime.get(), "HH:mm:ss");
//        try {
//            entryDao.modifyEntry(Integer.parseInt(employeeId.get()), 
//                    Integer.parseInt(projectId.get()), 
//                    Integer.parseInt(sprintId.get()), 
//                    parsedDate, 
//                    description.get(), 
//                    parsedStartTime, 
//                    parsedEndTime, 
//                    Integer.parseInt(userstoryId.get()));
//        } catch (NumberFormatException ex) {
//            Logger.getLogger(EntryResource.class.getName()).log(Level.SEVERE, null, ex);
//            return false;
//        }
//        return true;
//    }
    
    @POST
    @Path("/update/url")
    @JsonProperty
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean updateByUrl(@QueryParam("entryid") Optional<String> entryId,
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
            entryDao.modifyEntry(Integer.parseInt(entryId.get()), 
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
    
    @POST
    @Path("/delete")
    @JsonProperty
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean delete(@FormParam("entryid") Optional<String> entryId){
        try {
            entryDao.deleteEntry(Integer.parseInt(entryId.get()));
        } catch (NumberFormatException ex) {
            Logger.getLogger(EntryResource.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
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
