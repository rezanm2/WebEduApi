/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.webedu.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import nl.webedu.models.ProjectModel;

/**
 *
 * @author rezanaser
 */
public class ProjectDAO {
    public ArrayList<ProjectModel> getProjects(){
        try {
            Connection connect = new ConnectDAO().makeConnection();
            String getUserQuery = "SELECT * FROM project_version where project_version_current = true";
            PreparedStatement getUserStatement = connect.prepareStatement(getUserQuery);
            ResultSet userSet = getUserStatement.executeQuery();
            ArrayList<ProjectModel> data = new ArrayList<ProjectModel>();
            while(userSet.next()){
                ProjectModel project =  new ProjectModel();
                project.setProjectId(userSet.getInt( "project_version_project_fk"));
                project.setProjectName(userSet.getString("project_version_name"));
                data.add(project);
            }
            userSet.close();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        // No user found with the id given
        return null;
    }
    
    public void createProject()
    {
            try {
            Connection connect = new ConnectDAO().makeConnection();
            String getUserQuery = "INSERT INTO project_version(project_version_project_fk, project_version_name, project_version_description, project_version_customer_fk)VALUES(25, 'DROPWIZARD', 'DIT IS TEST', 17)";
            PreparedStatement getUserStatement = connect.prepareStatement(getUserQuery);
            ResultSet userSet = getUserStatement.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
