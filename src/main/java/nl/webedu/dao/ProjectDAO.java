/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.webedu.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import nl.webedu.models.ProjectModel;
import nl.webedu.dao.ConnectDAO;

/**
 *
 * @author rezanaser
 */
public class ProjectDAO {
    ConnectDAO connect = new ConnectDAO();
    /**
	 * Deze methode maakt een stored procedure aan die een nieuw project kan toevoegen zonder onderbroken te worden
	 * door een andere gebruiker (atomicity). Date:30-10-2017
	 *
	 * @author Robert den Blaauwen
	 */
	public void createAddProjectFunction(){
		String project_list_sql = "CREATE OR REPLACE FUNCTION add_project(name TEXT, description TEXT, customer INT4) " +
				"RETURNS void AS $$ " +
				"DECLARE pk INT; " +
				"BEGIN " +
				" INSERT INTO project(project_isdeleted) VALUES(false) " +
				"    RETURNING project_id INTO pk; " +
				"    INSERT INTO project_version(project_version_project_fk, project_version_name, project_version_description, project_version_customer_fk,project_version_current) " +
				"    VALUES(pk,name,description, customer,true); " +
				"END $$ LANGUAGE plpgsql;";
		try {
			PreparedStatement project_statement = connect.makeConnection().prepareStatement(project_list_sql);
			project_statement.executeUpdate();
			//System.out.println(this.getClass().toString()+": constructor: FUNCTION add_project(name, description, customer) has been created!");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    /**
     * Get all projects stored in database
     * @return array list of type Project Model to display to user
     */
    public ArrayList<ProjectModel> getAllProjects(){
        try {
            Connection connect = new ConnectDAO().makeConnection();
            String getUserQuery = "SELECT * FROM project_version pv INNER JOIN project p on pv.project_version_project_fk=p.project_id where project_version_current = true";
            PreparedStatement getUserStatement = connect.prepareStatement(getUserQuery);
            ResultSet userSet = getUserStatement.executeQuery();
            ArrayList<ProjectModel> data = new ArrayList<ProjectModel>();
            while(userSet.next()){
                ProjectModel project =  new ProjectModel();
                project.setProjectId(userSet.getInt( "project_version_project_fk"));
                project.setProjectName(userSet.getString("project_version_name"));
                project.setProjectDescription(userSet.getString("project_version_description"));
                project.setProjectIsDeleted(userSet.getBoolean("project_isdeleted"));
                project.setProjectCustomerFk(userSet.getInt("project_version_customer_fk"));
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
    
    /**
	 * Met deze methode krijg je een lijst van huidige project versies terug
	 * @return Een lijst van laatste project versies
	 * @author rezanaser
	 */
	
	public ArrayList<ProjectModel> project_list(){
		ArrayList<ProjectModel> proj_list = new ArrayList<ProjectModel>();
		String project_list_sql = "SELECT * FROM project_version "
				+ "INNER JOIN project ON (project_id = project_version_project_fk)"
				+ "AND project_version_current = true "
				+ "ORDER BY project_version.project_version_name ASC";
		try {
			PreparedStatement project_statement = connect.makeConnection().prepareStatement(project_list_sql);
			ResultSet project_set = project_statement.executeQuery();
			while(project_set.next()) {
				ProjectModel pm_container = new ProjectModel();
				pm_container.setProjectId(project_set.getInt("project_id"));
				pm_container.setProjectDescription(project_set.getString("project_version_description"));
				pm_container.setProjectName(project_set.getString("project_version_name"));
				pm_container.setProjectIsDeleted(project_set.getBoolean("project_isdeleted"));
				proj_list.add(pm_container);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return proj_list;
	}

	/**
	 * Geeft een lijst van projecten
	 *
	 * @param customerModel
	 * @return
	 */
//	public ArrayList<ProjectModel> project_list(CustomerModel customerModel){
//		ArrayList<ProjectModel> proj_list = new ArrayList<ProjectModel>();
//		String project_list_sql = "SELECT * FROM project_version "
//				+ "INNER JOIN project ON (project.project_id = project_version.project_version_project_fk) "
//				+ "WHERE project_version.project_version_customer_fk="+customerModel.getCustomer_id()
//				+ " ORDER BY project_version.project_version_name ASC";
//		try {
//			PreparedStatement project_statement = connect.connectToDB().prepareStatement(project_list_sql);
//			ResultSet project_set = project_statement.executeQuery();
//			while(project_set.next()) {
//				ProjectModel pm_container = new ProjectModel();
//				pm_container.setProjectId(project_set.getInt("project_id"));
//				pm_container.setProjectDescription(project_set.getString("project_version_description"));
//				pm_container.setProjectName(project_set.getString("project_version_name"));
//				pm_container.setProjectIsDeleted(project_set.getBoolean("project_isdeleted"));
//				proj_list.add(pm_container);
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return proj_list;
//	}

	/**
	 * Deze methode geeft een overzicht van alle projecten die de employee aan deelneemt
	 * @param employeeId    id van de employee
	 * @return proj_list    lijst van projecten
	 * @author Fardin Samandar
	 */
	public ArrayList<ProjectModel> project_list_employee(int employeeId){
		ArrayList<ProjectModel> proj_list = new ArrayList<ProjectModel>();
		String project_list_sql = "select * from project_version "
				+ "INNER JOIN project_employee "
				+ "ON project_version_project_fk = project_employee_project_fk  AND project_version_current = true "
				+ "AND project_employee_employee_fk = ?";
		try {
			PreparedStatement project_statement = connect.makeConnection().prepareStatement(project_list_sql);
			project_statement.setInt(1, employeeId);
			ResultSet project_set = project_statement.executeQuery();
			
			while(project_set.next()) {
				ProjectModel pm_container = new ProjectModel();
				
				pm_container.setProjectId(project_set.getInt("project_version_project_fk"));
				pm_container.setProjectDescription(project_set.getString("project_version_description"));
				pm_container.setProjectName(project_set.getString("project_version_name"));
				proj_list.add(pm_container);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return proj_list;
	}

	/**
	 * De volgende voegt een nieuwe project toe aan de project tabel
         * 
         * @return id   de id van het project dat net gemaakt is.
	 * @author rezanaser
	 */
	public int createNewProject() {
		int id = 0;
		PreparedStatement createProject;
		ResultSet projectId = null;
		String insertProject_sql = "insert into project (project_isdeleted) values (?)";
		
		try {
			
			createProject = connect.makeConnection().prepareStatement(insertProject_sql, Statement.RETURN_GENERATED_KEYS);
			
			createProject.setBoolean(1, false);
			createProject.executeUpdate();
			createProject.getGeneratedKeys();
			projectId = createProject.getGeneratedKeys();
			
			while (projectId.next()) {
				id = projectId.getInt(1);
			}
		} catch (Exception e) {
			
		}
		
		return id;
	}      
        /**
         * De volgende voegt een nieuwe project toe aan de project_version tabel
         * 
         * @param projectName   De naam van het project
         * @param projectDes    beschrijving van het project
         * @param customerId    id van de customer
         * @author rezanaser
         */
	public void addProjectToDatabase(String projectName, String projectDes, int customerId){
		PreparedStatement insertProject;
		String insertUser_sql = "insert into project_version (project_version_project_fk, project_version_name, project_version_description, project_version_current, project_version_customer_fk) "
				+ "VALUES (?, ?, ?, ?, ?)";
		try {
			insertProject = connect.makeConnection().prepareStatement(insertUser_sql);
			
			insertProject.setInt(1, createNewProject());
			insertProject.setString(2, projectName);
			insertProject.setString(3, projectDes);
			insertProject.setBoolean(4, true);
			insertProject.setInt(5, customerId);
			insertProject.executeQuery();
			insertProject.close();
			
		} catch (Exception e) {
			System.out.println(e);
			//e.printStackTrace();
		}
	}
	
	/**
	 * Voegt een nieuw project toe, maakt gebruik van de stored procedure die gemaakt wordt in createAddProjectFunction()
	 *
	 * @author Robert den Blaauwen
	 * @param name          naam van project
	 * @param description   description van project
	 * @param customerID    id van bijbehorende klant
	 */
	public void addProject(String name, String description, int customerID) {
		String login_sql = "SELECT add_project('"+name+"','"+description+"','"+customerID+"')";
		PreparedStatement project_statement;

		try {
			project_statement = connect.makeConnection().prepareStatement(login_sql);
			project_statement.executeQuery();
			project_statement.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
        /**
         * Deze methode wijzigt het geselcteerde project project
         * 
         * @param pId           project id
         * @param name          project naam
         * @param description   project description
         * @author rezanaser
         */
	public void modifyProject(int pId, String name, String description) {
		String changePreviousVersion = "UPDATE project_version set project_version_current = 'n' "
				+ "WHERE project_version_project_fk = ? AND project_version_current= true";
		String change_project = "INSERT INTO project_version(project_version_project_fk, project_version_name, project_version_description, project_version_current)"
				+ "VALUES(?, ?, ?, true)";
		try {
			PreparedStatement changeVersions= connect.makeConnection().prepareStatement(changePreviousVersion);
			changeVersions.setInt(1, pId);
			changeVersions.executeUpdate();
			changeVersions.close();
			PreparedStatement changeProject = connect.makeConnection().prepareStatement(change_project);
			changeProject.setInt(1, pId);
			changeProject.setString(2, name);
			changeProject.setString(3, description);
			changeProject.executeQuery();
			
			changeProject.close();
		} catch (Exception e) {
			e.getMessage();
		}
		
	}
	
	/**
	 * Deze methode zet het project op inactive
         * 
	 * @param projectId meegekregen van ProjectBeherenViewController
	 * @author rezanaser
	 */
	public void removeProject(int projectId) {
		String remove_project = "UPDATE project "
				+ "SET project_isdeleted = true "
				+ "WHERE project_id = ?";
		try {
			PreparedStatement lock_statement = connect.makeConnection().prepareStatement(remove_project);
			lock_statement.setInt(1, projectId);
			lock_statement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
