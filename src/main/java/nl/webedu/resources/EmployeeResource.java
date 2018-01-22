package nl.webedu.resources;

import io.dropwizard.auth.Auth;
import nl.webedu.models.EmployeeModel;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import nl.webedu.services.EmployeeService;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmployeeResource {
    private EmployeeService employeeService;

    public EmployeeResource(){
        this.employeeService = new EmployeeService();
    }
    
    @POST
    @Path("/create")
    public boolean createEmployee(@Auth EmployeeModel logegdUser, @Valid EmployeeModel employeeModel){
        return this.employeeService.createEmployee(employeeModel, employeeModel);
    }
    
    @PUT
    @Path("/update")
    public boolean updateEmployee(@Auth EmployeeModel logegdUser, @Valid EmployeeModel employeeModel){
        return this.employeeService.updateEmployee(employeeModel, employeeModel);
    }
    

    @GET
    @Path("/login")
    public EmployeeModel UserName(@Auth EmployeeModel employeeModel){
        return employeeModel;
    }
    
    @DELETE
    @Path("delete")
    public boolean removeEmployee(@QueryParam("emId") int employeeId , @Valid EmployeeModel employeeModel) throws Exception{
        return this.employeeService.deleteEmployee(employeeId, employeeModel);
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
