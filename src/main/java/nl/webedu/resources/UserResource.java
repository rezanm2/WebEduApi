package nl.webedu.resources;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import javax.ws.rs.Consumes;
import nl.webedu.dao.EmployeeDAO;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import nl.webedu.models.EmployeeModel;

@Path("/login")
public class UserResource {
    @GET
    @JsonProperty
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ArrayList<EmployeeModel> UserName(){
        EmployeeDAO employeeDAO = new EmployeeDAO();
        return employeeDAO.getAllEmployees();
    }
}
