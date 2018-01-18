package nl.webedu.services;

import java.sql.Date;
import java.util.ArrayList;
import nl.webedu.dao.SprintDAO;
import nl.webedu.helpers.DateHelper;
import nl.webedu.models.CategoryModel;

public class CategoryService {
    
    private SprintDAO categoryDao = new SprintDAO();
    
    public boolean create(CategoryModel sprintModel){
      return categoryDao.createSprint(sprintModel);
    }
    
    public boolean updateCategory(CategoryModel categoryModel){
        DateHelper dateHelper = new DateHelper();
        Date startDateParsed = dateHelper.parseDate(categoryModel.getCategoryStartDate(), "yyyy-MM-dd");
        Date endDateParsed = dateHelper.parseDate(categoryModel.getCategoryEndDate(), "yyyy-MM-dd");
        return categoryDao.modifySprint(categoryModel, startDateParsed, endDateParsed);
    }
    
    public ArrayList<CategoryModel> getAllCategories() throws Exception{
        return this.categoryDao.allSprints();
    }
    
    public boolean deleteCategory(int catId) throws Exception{
        return this.categoryDao.removeSprint(catId);
    }
    
    public boolean unDelete(int catId) throws Exception{
        return this.categoryDao.removeSprint(catId);
    }
    
}
