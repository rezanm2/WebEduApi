/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.webedu.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import nl.webedu.dao.CustomerDAO;
import nl.webedu.models.CustomerModel;

/**
 *
 * @author rezanaser
 */
@Path("/customers")
public class CustomerResource {
    
    private CustomerDAO customerDao = new CustomerDAO();
    
    @GET
    @Path("/getAll")
    @JsonProperty
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ArrayList<CustomerModel> read(){
        try {
            return this.customerDao.getCustomerList();
        } catch (Exception ex) {
            Logger.getLogger(CustomerResource.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
}
