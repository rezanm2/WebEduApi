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

@Path("/login")
public class UserResource {
    private EmployeeDAO employeeDAO;

    public UserResource(){
        this.employeeDAO = new EmployeeDAO();
    }

    @GET
    @JsonProperty
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ArrayList<EmployeeModel> UserName(@Auth EmployeeModel employeeModel){
        try{
            return this.employeeDAO.getAllEmployees();
        } catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @GET
    @JsonProperty
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ArrayList<EmployeeModel> Users() {
        try {
            return this.employeeDAO.getAllEmployees();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
