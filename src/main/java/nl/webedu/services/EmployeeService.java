package nl.webedu.services;

import java.util.ArrayList;
import nl.webedu.dao.EmployeeDAO;
import nl.webedu.models.EmployeeModel;

public class EmployeeService {
    
    private EmployeeDAO employeeDao = new EmployeeDAO(); 
    
    public boolean createEmployee(EmployeeModel employeeModel){
       return this.employeeDao.createEmployeeVersion(employeeModel);
    }
    
    public ArrayList<EmployeeModel> getAllEmployees(){
        return this.employeeDao.getAllEmployees();
    }
    
}
