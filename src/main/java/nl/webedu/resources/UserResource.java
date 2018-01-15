package nl.webedu.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.auth.Auth;
import nl.webedu.dao.EmployeeDAO;
import nl.webedu.models.EmployeeModel;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import javax.validation.Valid;
import javax.ws.rs.POST;
import nl.webedu.services.EmployeeService;

@Path("/login")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {
    private EmployeeService employeeService;

    public UserResource(){
        this.employeeService = new EmployeeService();
    }
    
    @POST
    @Path("/create")
    public boolean createEmployee(@Auth EmployeeModel loggedUser, @Valid EmployeeModel employeeModel){
        return this.employeeService.createEmployee(employeeModel);
    }
    

//    @GET
//    public ArrayList<EmployeeModel> UserName(@Auth EmployeeModel employeeModel){
//        try{
//            return this.employeeService.getAllEmployees();
//        } catch(Exception e){
//            e.printStackTrace();
//        }
//        return null;
//    }
    
    @GET
    @Path("/users")
    public ArrayList<EmployeeModel> Users() {
        try {
            return this.employeeService.getAllEmployees();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
