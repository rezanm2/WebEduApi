package nl.webedu.services;

import java.sql.Date;
import java.util.ArrayList;
import nl.webedu.dao.SprintDAO;
import nl.webedu.helpers.DateHelper;
import nl.webedu.models.CategoryModel;
import nl.webedu.models.EmployeeModel;
import nl.webedu.models.Role;

public class CategoryService {
    
    private SprintDAO categoryDao = new SprintDAO();
    
    public boolean create(CategoryModel sprintModel, EmployeeModel loggedUser){
        if(loggedUser.getEmployeeRole().equals(Role.administration.toString())){
            return categoryDao.createSprint(sprintModel);
        }else{return false;}
      
    }
    
    public boolean updateCategory(CategoryModel categoryModel, EmployeeModel loggedUser){
        if(loggedUser.getEmployeeRole().equals(Role.administration.toString())){
         DateHelper dateHelper = new DateHelper();
        Date startDateParsed = dateHelper.parseDate(categoryModel.getCategoryStartDate(), "yyyy-MM-dd");
        Date endDateParsed = dateHelper.parseDate(categoryModel.getCategoryEndDate(), "yyyy-MM-dd");
        return categoryDao.modifySprint(categoryModel, startDateParsed, endDateParsed);
        }else{return false;}
        
    }
    
    public ArrayList<CategoryModel> getAllCategories() throws Exception{
        return this.categoryDao.allSprints();
    }
    
    public boolean deleteCategory(int catId, EmployeeModel loggedUser) throws Exception{
        if(loggedUser.getEmployeeRole().equals(Role.administration.toString())){
            return this.categoryDao.removeSprint(catId);
        }else{return false;}
        
    }
    
    public boolean unDelete(int catId, EmployeeModel loggedUser) throws Exception{
        if(loggedUser.getEmployeeRole().equals(Role.administration.toString())){
            return this.categoryDao.removeSprint(catId);
        }else{return false;}
        
    }
    
}
