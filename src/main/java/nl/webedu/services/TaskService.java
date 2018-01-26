package nl.webedu.services;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.webedu.dao.ProjectDAO;
import nl.webedu.dao.SprintDAO;
import nl.webedu.dao.TaskDAO;
import nl.webedu.models.CategoryModel;
import nl.webedu.models.EmployeeModel;
import nl.webedu.models.ProjectModel;
import nl.webedu.models.TaskModel;

public class TaskService {
    private TaskDAO taskDao = new TaskDAO();
    private SprintDAO sprintDao = new SprintDAO();
    private ProjectDAO projectDao = new ProjectDAO();
    public ArrayList<TaskModel> read(EmployeeModel employeeModel){
        ArrayList<TaskModel> tasks=this.taskDao.userStory_list();
        for(TaskModel task:tasks){
            CategoryModel category = sprintDao.sprintListTask(task.getUserStoryId());
            task.setCategoryId(category.getCategoryId());
            task.setCategoryName(category.getCategoryName());
            task.setProjectId(7);
        }
        try {
            for(TaskModel task:tasks){
                ProjectModel projectModel = projectDao.getProject(task.getProjectId());
                task.setProjectName(projectModel.getProjectName());
            }
            return tasks;
        } catch (Exception ex) {
            Logger.getLogger(TaskService.class.getName()).log(Level.SEVERE, null, ex);
            return tasks;
        }
    }
    public ArrayList<TaskModel> readFromUrl(EmployeeModel employeeModel, int employeeId){
        return this.taskDao.userStory_list_employee(employeeId);
    }
    
    public ArrayList<TaskModel> getTasksByCategory(int categoryId){
        return this.taskDao.getTasksByCategory(categoryId);
    }
    
    public ArrayList<CategoryModel> getCategoriesByProject(EmployeeModel employeeModel, int projectId){
        return this.taskDao.categoriesProject(projectId);
    }
    
    public boolean create(EmployeeModel employeeModel, TaskModel userStoryModel){
        if(employeeModel.getEmployeeRole().equals("administration")){
            taskDao.addUserStory(userStoryModel);
        return true;
        }else{return false;}
        
    }
    public boolean update(EmployeeModel employeeModel, TaskModel userStoryModel){
        if(employeeModel.getEmployeeRole().equals("administration")){
            taskDao.modifyUserStory(userStoryModel);
         return true;
        }else{return false;}
    }

    public boolean delete(EmployeeModel employeeModel, TaskModel userStoryModel){
        if(employeeModel.getEmployeeRole().equals("administration")){
            taskDao.removeUserStory(userStoryModel);
        return true;
        }else{return false;}
    }
    public boolean unDeleteByUrl(EmployeeModel employeeModel, int projectId){
        if(employeeModel.getEmployeeRole().equals("administration")){
            this.taskDao.unRemoveUserStory(projectId);
            return true;
        }else{return false;}
        
    }
}

