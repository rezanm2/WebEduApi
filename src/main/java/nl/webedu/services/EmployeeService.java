package nl.webedu.services;

import java.util.ArrayList;
import nl.webedu.dao.EmployeeDAO;
import nl.webedu.models.EmployeeModel;
import nl.webedu.models.Role;

public class EmployeeService {
    
    private EmployeeDAO employeeDao = new EmployeeDAO(); 
    
    public boolean createEmployee(EmployeeModel loggedUser, EmployeeModel employeeModel){
        if(loggedUser.getEmployeeRole().equals(Role.administration.toString())){
            return this.employeeDao.createEmployeeVersion(employeeModel);
        }else{return false;}
       
    }
    
    public ArrayList<EmployeeModel> getAllEmployees(){
        return this.employeeDao.getAllEmployees();
    }
    
    public boolean updateEmployee( EmployeeModel loggedUser, EmployeeModel employeeModel){
        System.out.println(loggedUser.getEmployeeRole());
        if(loggedUser.getEmployeeRole().equals(Role.administration.toString())){
            return this.employeeDao.updateEmployee(employeeModel);
        }else{return false;}
        
    }
    
    public boolean deleteEmployee( EmployeeModel loggedUser, int employeeId) throws Exception{
        if(loggedUser.getEmployeeRole().equals(Role.administration.toString())){
            return this.employeeDao.lockEmployee(employeeId);
        }else{return false;}
        
    }
    
}
