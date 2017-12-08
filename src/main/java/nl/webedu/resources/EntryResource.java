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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import nl.webedu.dao.*;
import nl.webedu.models.EntryModel;
import nl.webedu.models.ProjectModel;

/**
 *
 * @author rezanaser
 */
@Path("/entries")
public class EntryResource {
    private EntryDAO entryDao;

    public EntryResource(){
        entryDao = new EntryDAO();
    }

    @GET
    @Path("/read")
    @JsonProperty
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ArrayList<EntryModel> read(){
        return this.entryDao.getEntriesFull();
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
}
