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
import javax.ws.rs.POST;
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
    @Path("/read")
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
    
//    @POST
//    @Path("/create")
//    @JsonProperty
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    public boolean create(){
//        
//    }
}
