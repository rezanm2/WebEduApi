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
import javax.ws.rs.PUT;
import nl.webedu.services.EmployeeService;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
    private EmployeeService employeeService;

    public UserResource(){
        this.employeeService = new EmployeeService();
    }
    
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean createEmployee(@Auth EmployeeModel logegdUser, @Valid EmployeeModel employeeModel){
        return this.employeeService.createEmployee(employeeModel);
    }
    
    @PUT
    @Path("/update")
    public boolean updateEmployee(@Auth EmployeeModel logegdUser, @Valid EmployeeModel employeeModel){
        return this.employeeService.updateEmployee(employeeModel);
    }
    

    @GET
    @Path("/login")
    public EmployeeModel UserName(@Auth EmployeeModel employeeModel){
        return employeeModel;
    }
    
    @GET
    public ArrayList<EmployeeModel> Users(@Auth EmployeeModel employeeModel) {
        try {
            return this.employeeService.getAllEmployees();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
