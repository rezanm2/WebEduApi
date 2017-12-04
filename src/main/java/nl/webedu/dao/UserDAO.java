package nl.webedu.dao;
import nl.webedu.models.UserModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import nl.webedu.models.ProjectModel;

public class UserDAO {
    /**
     * @author nach7vs
     * @return  userModel
     */
    public ArrayList<UserModel> getEmployees(){
        try {
            Connection connect = new ConnectDAO().makeConnection();
            String getUserQuery = "SELECT * FROM employee_version where employee_version_current = true";
            PreparedStatement getUserStatement = connect.prepareStatement(getUserQuery);
            ResultSet userSet = getUserStatement.executeQuery();
            ArrayList<UserModel> data = new ArrayList<UserModel>();
            while(userSet.next()){
                UserModel employee =  new UserModel();
                employee.setUserId(userSet.getInt( "employee_version_employee_fk"));
                employee.setUserFirstName(userSet.getString("employee_version_firstname"));
                employee.setUserLastName(userSet.getString("employee_version_lastname"));
                employee.setEmail(userSet.getString("employee_version_email"));
                employee.setPassword(userSet.getString("employee_version_password"));
                data.add(employee);
            }
            userSet.close();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        // No user found with the id given
        return null;
    }
}
