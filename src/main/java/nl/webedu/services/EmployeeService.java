package nl.webedu.services;

import java.util.ArrayList;
import nl.webedu.dao.EmployeeDAO;
import nl.webedu.models.EmployeeModel;

public class EmployeeService {
    
    private EmployeeDAO employeeDao = new EmployeeDAO(); 
    
    public boolean createEmployee(EmployeeModel employeeModel, EmployeeModel loggedUser){
        if(loggedUser.getEmployeeRole().equals("administration")){
            return this.employeeDao.createEmployeeVersion(employeeModel);
        }else{return false;}
       
    }
    
    public ArrayList<EmployeeModel> getAllEmployees(){
        return this.employeeDao.getAllEmployees();
    }
    
    public boolean updateEmployee(EmployeeModel employeeModel, EmployeeModel loggedUser){
        if(loggedUser.getEmployeeRole().equals("administration")){
            return this.employeeDao.updateEmployee(employeeModel);
        }else{return false;}
        
    }
    
    public boolean deleteEmployee(int employeeId, EmployeeModel loggedUser) throws Exception{
        if(loggedUser.getEmployeeRole().equals("administration")){
            return this.employeeDao.lockEmployee(employeeId);
        }else{return false;}
        
    }
    
}
