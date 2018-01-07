
package nl.webedu.auth;

import com.google.common.base.Optional;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;
import java.util.ArrayList;
import nl.webedu.dao.EmployeeDAO;
import nl.webedu.models.EmployeeModel;

/**
 *
 * @author rezanaser
 */
public class Auth implements Authenticator<BasicCredentials, EmployeeModel>{

    @Override
    public Optional<EmployeeModel> authenticate(BasicCredentials c) throws AuthenticationException {
        EmployeeDAO employeeDao = new EmployeeDAO();
        EmployeeModel currentEmployee = null;
        
        ArrayList<EmployeeModel> employees = employeeDao.getAllEmployees();
        if(employees!=null){
            for(EmployeeModel employee : employees){
                if(employee.getEmployeeEmail().equals(c.getUsername())&&employee.getEmployeePassword().equals(c.getPassword())){
                    currentEmployee = employee;
                }
            }
            if(currentEmployee != null)
            {
                return Optional.of(currentEmployee);
            }else
            {
                return Optional.absent();
            }
        }
        else
        {
            return Optional.absent();
        }

    }
}
