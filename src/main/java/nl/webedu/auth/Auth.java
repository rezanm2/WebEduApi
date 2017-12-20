
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
        for(EmployeeModel employee : employees){
            if(employee.getEmployeeEmail().equals(c.getUsername())&&employee.getEmployeePassword().equals(c.getPassword())){
                currentEmployee = employee;
            }
        }
        
//        for(int x=0;x<employeeDao.getAllEmployees().size();x++)// Haalt alle emplopyees vanuit de dao klasse
//            // Checkt of de gebruiker bestaat
//        {
//            if(employeeDao.getAllEmployees().get(x).getEmployeeEmail().equals(c.getUsername())
//                    && employeeDao.getAllEmployees().get(x).getEmployeePassword().equals(c.getPassword()))
//            {
//                 currentEmployee = employeeDao.getAllEmployees().get(x);//Zo ja, wordt het model teruggestuurd.
//            }
//        }
        
        if(currentEmployee != null)
        {
            return Optional.of(currentEmployee);
        }
        else
        {
            return Optional.absent();
        }

    }
}
