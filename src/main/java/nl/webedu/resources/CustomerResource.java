/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.webedu.resources;

import io.dropwizard.auth.Auth;
import java.util.ArrayList;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import nl.webedu.models.CustomerModel;
import nl.webedu.models.EmployeeModel;
import nl.webedu.services.CustomerService;

/**
 *
 * @author rezanaser
 */
@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {
    
    private CustomerService customerService = new CustomerService();
    
    @GET
    @Path("/getAll")
    public ArrayList<CustomerModel> getAllCustomers(@Auth EmployeeModel employeeModel){
        return this.customerService.getAllCustomers(employeeModel);
    }
    
    @POST
    @Path("/createCustomer")
    public boolean createNewCustomer(@Auth EmployeeModel employeeModel, @Valid CustomerModel csModel){
            return this.customerService.createNewCustomer(employeeModel, csModel);
    }
    
    @PUT
    @Path("/update")

    public boolean update(@Auth EmployeeModel employeeModel, @Valid CustomerModel csModel){
          return customerService.update(employeeModel, csModel);
    }
    
    @PUT
    @Path("/remove")
    public boolean deleteCustomer(@Auth EmployeeModel employeeModel, @Valid CustomerModel csModel){
         return customerService.deleteCustomer(employeeModel, csModel);
    }
    
}
