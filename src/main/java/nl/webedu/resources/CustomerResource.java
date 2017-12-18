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
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import nl.webedu.dao.CustomerDAO;
import nl.webedu.models.CustomerModel;
import nl.webedu.models.ProjectModel;

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
    
    @POST
    @Path("/createCustomer")
    @JsonProperty
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public void createNewCustomer(@Valid CustomerModel csModel){
        try {
            this.customerDao.addCustomer(csModel.getCustomer_name(), csModel.getCustomer_description());
        } catch (Exception ex) {
            Logger.getLogger(CustomerResource.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @PUT
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)

    public boolean update(@Valid CustomerModel csModel){

        try {
            customerDao.modifyCustomer(csModel.getCustomer_id(), csModel.getCustomer_name(), csModel.getCustomer_description());
        } catch (Exception ex) {
            Logger.getLogger(CustomerResource.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        System.out.println(this.getClass().toString()+": update werkt!: ");
        return true;
    }
    
    @PUT
    @Path("/remove")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)

    public boolean deleteCustomer(@Valid CustomerModel csModel){

        try {
            customerDao.removeCustomer(csModel.getCustomer_id());
        } catch (Exception ex) {
            Logger.getLogger(CustomerResource.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        System.out.println(this.getClass().toString()+": update werkt!: ");
        return true;
    }
    
}
