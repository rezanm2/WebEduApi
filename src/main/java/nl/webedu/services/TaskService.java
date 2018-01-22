package nl.webedu.services;

import java.util.ArrayList;
import nl.webedu.dao.TaskDAO;
import nl.webedu.models.EmployeeModel;
import nl.webedu.models.TaskModel;

public class TaskService {
    private TaskDAO taskDao = new TaskDAO();
    public ArrayList<TaskModel> read(EmployeeModel employeeModel){
        return this.taskDao.userStory_list();
    }
    public ArrayList<TaskModel> readFromUrl(EmployeeModel employeeModel, int employeeId){
        return this.taskDao.userStory_list_employee(employeeId);
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

