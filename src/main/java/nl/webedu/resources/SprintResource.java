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
import java.sql.Date;
import java.sql.Time;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import static javax.ws.rs.HttpMethod.PUT;
import javax.ws.rs.POST;
import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import nl.webedu.dao.*;
import nl.webedu.models.SprintModel;
import nl.webedu.models.ProjectModel;
import nl.webedu.helpers.DateHelper;
import javax.ws.rs.Path;
import nl.webedu.dao.SprintDAO;

/**
 *
 * @author rezanaser
 */
@Path("/sprints")
public class SprintResource {
    private SprintDAO sprintDao;
    public SprintResource(){
        sprintDao = new SprintDAO();
    }
    
    @GET
//    @Path("/read")
    @JsonProperty
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ArrayList<SprintModel> read(){
        try {
            return this.sprintDao.allSprints();
        } catch (Exception ex) {
            Logger.getLogger(SprintResource.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    @POST
//    @Path("/create")
    @JsonProperty
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean create(@FormParam("projid") Optional<String> projectId,
                        @FormParam("name") Optional<String> name,
                        @FormParam("description") Optional<String> description,
                        @FormParam("startdate") Optional<String> startDate,
                        @FormParam("endDate") Optional<String> endDate){
        DateHelper dateHelper = new DateHelper();
        Date startDateParsed = dateHelper.parseDate(startDate.get(), "dd-MM-yyyy");
        Date endDateParsed = dateHelper.parseDate(endDate.get(), "dd-MM-yyyy");
        sprintDao.addSprintToDatabase(Integer.parseInt(projectId.get()), name.get(), 
                description.get(), startDateParsed, endDateParsed);
        return true;
    }
    
    @POST
    @Path("/url")
    @JsonProperty
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean createByUrl(@QueryParam("projid") Optional<String> projectId,
                        @QueryParam("name") Optional<String> name,
                        @QueryParam("description") Optional<String> description,
                        @QueryParam("startdate") Optional<String> startDate,
                        @QueryParam("enddate") Optional<String> endDate){
        DateHelper dateHelper = new DateHelper();
        Date startDateParsed = dateHelper.parseDate(startDate.get(), "dd-MM-yyyy");
        Date endDateParsed = dateHelper.parseDate(endDate.get(), "dd-MM-yyyy");
        sprintDao.addSprintToDatabase(Integer.parseInt(projectId.get()), name.get(), 
                description.get(), startDateParsed, endDateParsed);
        return true;
    }
    
    @PUT
//    @Path("/update")
    @JsonProperty
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean update(@FormParam("sprintid") Optional<String> sprintId,
                        @FormParam("projid") Optional<String> projectId,
                        @FormParam("name") Optional<String> name,
                        @FormParam("description") Optional<String> description,
                        @FormParam("startdate") Optional<String> startDate,
                        @FormParam("endDate") Optional<String> endDate){
        DateHelper dateHelper = new DateHelper();
        Date startDateParsed = dateHelper.parseDate(startDate.get(), "dd-MM-yyyy");
        Date endDateParsed = dateHelper.parseDate(endDate.get(), "dd-MM-yyyy");
        sprintDao.modifySprint(Integer.parseInt(sprintId.get()),
                name.get(), Integer.parseInt(projectId.get()),  
                description.get(), startDateParsed, endDateParsed);
        return true;
    }
    @PUT
    @Path("/url")
    @JsonProperty
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean updateByUrl(@QueryParam("sprintid") Optional<String> sprintId,
                        @QueryParam("projid") Optional<String> projectId,
                        @QueryParam("name") Optional<String> name,
                        @QueryParam("description") Optional<String> description,
                        @QueryParam("startdate") Optional<String> startDate,
                        @QueryParam("endDate") Optional<String> endDate){
        DateHelper dateHelper = new DateHelper();
        Date startDateParsed = dateHelper.parseDate(startDate.get(), "dd-MM-yyyy");
        Date endDateParsed = dateHelper.parseDate(endDate.get(), "dd-MM-yyyy");
        sprintDao.modifySprint(Integer.parseInt(sprintId.get()),
                name.get(), Integer.parseInt(projectId.get()),  
                description.get(), startDateParsed, endDateParsed);
        return true;
    }
    
    @DELETE
//    @Path("/delete")
    @JsonProperty
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean delete(@FormParam("sprintid") Optional<String> sprintId){
        try {
            sprintDao.removeSprint(Integer.parseInt(sprintId.get()));
        } catch (Exception ex) {
            Logger.getLogger(SprintResource.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    @DELETE
    @Path("/url")
    @JsonProperty
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean deleteByUrl(@QueryParam("sprintid") Optional<String> sprintId){
        try {
            sprintDao.removeSprint(Integer.parseInt(sprintId.get()));
        } catch (Exception ex) {
            Logger.getLogger(SprintResource.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    
    @PUT
    @Path("/undelete")
    @JsonProperty
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean unDelete(@FormParam("sprintid") Optional<String> sprintId){
        try {
            sprintDao.unRemoveSprint(Integer.parseInt(sprintId.get()));
        } catch (Exception ex) {
            Logger.getLogger(SprintResource.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    @PUT
    @Path("/undelete/url")
    @JsonProperty
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean unDeleteByUrl(@QueryParam("sprintid") Optional<String> sprintId){
        try {
            sprintDao.unRemoveSprint(Integer.parseInt(sprintId.get()));
        } catch (Exception ex) {
            Logger.getLogger(SprintResource.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
}
