/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.webedu.resources;
import java.util.ArrayList;
import javax.ws.rs.QueryParam;
import io.dropwizard.auth.Auth;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.*;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import nl.webedu.models.CategoryModel;
import javax.ws.rs.Path;
import nl.webedu.models.EmployeeModel;
import nl.webedu.services.CategoryService;

/**
 *
 * @author rezanaser
 */
@Path("/categories")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategoryResource {
    private CategoryService categoryService;

    public CategoryResource(){
        categoryService = new CategoryService();
    }

    @GET
    @Path("/read")
    public ArrayList<CategoryModel> read(@Auth EmployeeModel loggedUser){
        try {
            return this.categoryService.getAllCategories();
        } catch (Exception ex) {
            Logger.getLogger(CategoryResource.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @POST
    @Path("/create")
    public boolean create(@Auth EmployeeModel employeeModel, @Valid CategoryModel sprintModel){
        return categoryService.create(sprintModel, employeeModel);
    }
    
    @PUT
    @Path("/update")
    public boolean update(@Auth EmployeeModel loggedUser, @Valid CategoryModel categoryModel){
        return this.categoryService.updateCategory(categoryModel, loggedUser);
    }
    
    @DELETE
    @Path("/delete")
    public boolean delete(@QueryParam("catId") int catId, @Auth EmployeeModel employeeModel) throws Exception{
        return categoryService.deleteCategory(catId, employeeModel);
    }
    
    @PUT
    @Path("/undelete")
    public boolean unDelete(@Auth EmployeeModel employeeModel, @QueryParam("sprintid") int sprintId) throws Exception{
      return categoryService.unDelete(sprintId, employeeModel);
    }
}
