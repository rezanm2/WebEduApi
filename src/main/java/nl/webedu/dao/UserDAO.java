package nl.webedu.dao;
import nl.webedu.models.UserModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {
    /**
     * @author nach7vs
     * @param id user id to return result set from
     * @return  userModel
     */
    public UserModel getUser(int id){
        try {
            Connection connect = new ConnectDAO().makeConnection();
            String getUserQuery = "SELECT * FROM employee_version WHERE employee_version_employee_fk = ?";
            PreparedStatement getUserStatement = connect.prepareStatement(getUserQuery);
            getUserStatement.setInt(1, id);
            ResultSet userSet = getUserStatement.executeQuery();

            if(userSet.next()){
                return new UserModel(userSet.getInt("employee_version_employee_fk"),
                        userSet.getString("employee_version_firstname"),
                        userSet.getString("employee_version_lastname"));
            }
            userSet.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // No user found with the id given
        return null;
    }
}
