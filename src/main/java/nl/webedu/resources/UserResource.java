package nl.webedu.resources;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.ws.rs.Consumes;
import nl.webedu.dao.UserDAO;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import nl.webedu.models.UserModel;

@Path("/login")
public class UserResource {
    @GET
    @JsonProperty
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public UserModel UserName(){
        UserDAO userDAO = new UserDAO();
        return new UserModel(
                userDAO.getUser(8).getUserId(), userDAO.getUser(8).getUserFirstName(),
                userDAO.getUser(8).getUserLastName()
        );
    }
}
