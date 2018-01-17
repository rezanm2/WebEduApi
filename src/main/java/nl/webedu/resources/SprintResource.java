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
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import static javax.ws.rs.HttpMethod.PUT;
import javax.ws.rs.POST;
import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import nl.webedu.dao.*;
import nl.webedu.models.CategoryModel;
import nl.webedu.models.ProjectModel;
import nl.webedu.helpers.DateHelper;
import javax.ws.rs.Path;
import nl.webedu.dao.SprintDAO;
import nl.webedu.models.EmployeeModel;

/**
 *
 * @author rezanaser
 */
@Path("/categories")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SprintResource {
    private SprintDAO sprintDao;

    public SprintResource(){
        sprintDao = new SprintDAO();
    }

    @GET
    @Path("/read")
    public ArrayList<CategoryModel> read(@Auth EmployeeModel loggedUser){
        try {
            return this.sprintDao.allSprints();
        } catch (Exception ex) {
            Logger.getLogger(SprintResource.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @PUT
    @Path("/by_employee/url")
    public ArrayList<CategoryModel> readByEmployeeByUrl(@QueryParam("empid") Optional<String> employeeId){
        try {
            return this.sprintDao.allSprintsEmployee(Integer.parseInt(employeeId.get()));
        } catch (Exception ex) {
            Logger.getLogger(SprintResource.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @PUT
    @Path("/by_project/")
    public ArrayList<CategoryModel> readByProjectByUrl(@QueryParam("projid") Optional<String> projectId){
        try {
            return this.sprintDao.sprintsProjects(Integer.parseInt(projectId.get()));
        } catch (Exception ex) {
            Logger.getLogger(SprintResource.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    

    /**
     * Deze methode krijgt een hele object van de frondend. 
     * Hij checkt od het model voldoet aan java model. Zo ja,
     * word er een nieuwe model aangemaakt.
     * @param sprintModel   sprintModel
     * @param employeeModel model
     * @return returneert true of false
     * @author rezanaser
     */
    @POST
    @Path("/create")
    public boolean create(@Auth EmployeeModel employeeModel, @Valid CategoryModel sprintModel){
        sprintDao.createSprint(sprintModel);
        return true;
    }
    
    @PUT
    @Path("/update")
    public boolean update(@Auth EmployeeModel loggedUser, @Valid CategoryModel categoryModel){
        DateHelper dateHelper = new DateHelper();
        Date startDateParsed = dateHelper.parseDate(categoryModel.getCategoryStartDate(), "yyyy-MM-dd");
        Date endDateParsed = dateHelper.parseDate(categoryModel.getCategoryEndDate(), "yyyy-MM-dd");
        sprintDao.modifySprint(categoryModel, startDateParsed, endDateParsed);
        return true;
    }
    
    @DELETE
    @Path("/delete")
    public boolean delete(@QueryParam("sprintid") Optional<String> sprintId){
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
    public boolean unDelete(@QueryParam("sprintid") String sprintId){
        try {
            sprintDao.unRemoveSprint(Integer.parseInt(sprintId));
        } catch (Exception ex) {
            Logger.getLogger(SprintResource.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
}
