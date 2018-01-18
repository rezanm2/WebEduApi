package nl.webedu.services;

import java.util.ArrayList;
import nl.webedu.dao.CustomerDAO;
import nl.webedu.models.CustomerModel;
import nl.webedu.models.EmployeeModel;

public class CustomerService {
    private CustomerDAO customerDao = new CustomerDAO();
    
    
    public ArrayList<CustomerModel> getAllCustomers(EmployeeModel employeeModel){
            return this.customerDao.getCustomerList();
        }
    
    
    public boolean createNewCustomer(EmployeeModel employeeModel,CustomerModel csModel){
          return this.customerDao.addCustomer(csModel.getCustomer_name(), csModel.getCustomer_description());
    }
    
    public boolean update(EmployeeModel employeeModel, CustomerModel csModel){
        return customerDao.modifyCustomer(csModel.getCustomer_id(), csModel.getCustomer_name(), csModel.getCustomer_description());
    }
    
    public boolean deleteCustomer(EmployeeModel employeeModel, CustomerModel csModel){
        return customerDao.removeCustomer(csModel.getCustomer_id());
    }
}
