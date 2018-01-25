/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.webedu.dao;

import nl.webedu.models.ProjectModel;

import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author rezanaser
 */
public class ProjectDAO {
    private ConnectDAO connect;

    public ProjectDAO(){
        this.connect = new ConnectDAO();
//        this.createAddProjectFunction();
    }
    
    /**
     * Krijg een enkel project
     * 
     * @author Robert
     * @throws SQLException kek
     * @throws Exception    kek
     * @param projectId id van project
     * @return project
     */
    public ProjectModel getProject(int projectId) throws SQLException, Exception {
       String projectSql = "SELECT * FROM project INNER JOIN project_version " + 
                                "ON project_id=project_version_project_fk " +
                                "WHERE project_id=? AND project_version_current=true;";
       
            Connection connection = this.connect.makeConnection();
            PreparedStatement projectStatement= connection.prepareStatement(projectSql);
            projectStatement.setInt(1,projectId);
            ResultSet projectSet = projectStatement.executeQuery();
            
            ProjectModel project = new ProjectModel();
            try{
                projectSet.next();
                //dit geeft een error: Resultset not positioned properly,
                // perhaps you need to call next.
                project.setProjectId(projectSet.getInt("project_id")); 
                project.setProjectName(projectSet.getString("project_version_name"));
                project.setProjectDescription(projectSet.getString("project_version_description"));
                project.setProjectIsDeleted(projectSet.getBoolean("project_isdeleted"));
                project.setIsCurrent(true);
                project.setProjectCustomerFk(projectSet.getInt("project_version_customer_fk"));
            } catch(SQLException e){
                e.printStackTrace();
            } finally{
                //close alles zodat de connection pool niet op gaat.
                projectSet.close();
                projectStatement.close();
                connection.close();
            }
            return project; 
    }

        /**
	 *Deze method maakt een procedure aan die projecten aan kan maken.
         * GEEFT BIZARRE NULLPOINTER EXCEPTIONS!
	 *
	 * @author Robert den Blaauwen
	 */
	public void createAddProjectFunction(){
		String projectProcedureSql = "CREATE OR REPLACE FUNCTION add_project(name TEXT, description TEXT, customer INT4) " +
				"RETURNS void AS $$ " +
				"DECLARE pk INT; " +
				"BEGIN " +
				"    INSERT INTO project(project_isdeleted) VALUES(false) " +
				"    RETURNING project_id INTO pk; " +
				"    INSERT INTO project_version(project_version_project_fk, project_version_name, project_version_description, project_version_customer_fk,project_version_current) " +
				"    VALUES(pk,name,description, customer,true); " +
				"END $$ LANGUAGE plpgsql;";
		try {
                    Connection connection = this.connect.makeConnection();
			PreparedStatement projectProcedureStatement = connection.prepareStatement(projectProcedureSql);
			projectProcedureStatement.executeUpdate();
                        //close alles zodat de connection pool niet op gaat.
                        projectProcedureStatement.close();
                        connection.close();
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
            String getUserQuery = "SELECT * FROM project_version pv JOIN project p on pv.project_version_project_fk=p.project_id "
                                + "JOIN customer_version cm on pv.project_version_customer_fk = cm.customer_version_customer_fk "
                                + "WHERE customer_version_current = true AND project_version_current = true;";
                            //  + "project_version_current = true AND project_isdeleted=false;";
            Connection connection = this.connect.makeConnection();
            PreparedStatement projectStatement = connection.prepareStatement(getUserQuery);
            ResultSet projectSet = projectStatement.executeQuery();
            ArrayList<ProjectModel> data = new ArrayList<ProjectModel>();
            while(projectSet.next()){
                ProjectModel project =  new ProjectModel();
                project.setProjectId(projectSet.getInt( "project_version_project_fk"));
                project.setProjectName(projectSet.getString("project_version_name"));
                project.setProjectDescription(projectSet.getString("project_version_description"));
                project.setProjectIsDeleted(projectSet.getBoolean("project_isdeleted"));
                project.setProjectCustomerFk(projectSet.getInt("project_version_customer_fk"));
                project.setCustomerName(projectSet.getString("customer_version_name"));
                project.setIsCurrent(projectSet.getBoolean("project_version_current"));
                data.add(project);
            }
            //close alles zodat de connection pool niet op gaat.
            projectSet.close();
            projectStatement.close();
            connection.close();
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
		PreparedStatement projectStatement = connect.prepareStatement(getUserQuery);
		ResultSet projectSet = projectStatement.executeQuery();
                //close alles zodat de connection pool niet op gaat.
                projectSet.close();
                projectStatement.close();
                connect.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
        /**
	 * Met deze methode krijg je een lijst van huidige project versies terug
	 * @return Een lijst van laatste project versies
	 * @author rezanaser
         * @throws Exception SQL exception en normale exception
	 */
	public ArrayList<ProjectModel> project_list() throws Exception {
		ArrayList<ProjectModel> proj_list = new ArrayList<ProjectModel>();
		String project_list_sql = "SELECT * FROM project_version "
				+ "INNER JOIN project ON (project_id = project_version_project_fk)"
				+ "AND project_version_current = true "
				+ "ORDER BY project_version.project_version_name ASC";
		try {
                    Connection connection = this.connect.makeConnection();
			PreparedStatement project_statement = connection.prepareStatement(project_list_sql);
			ResultSet project_set = project_statement.executeQuery();
			while(project_set.next()) {
				ProjectModel pm_container = new ProjectModel();
				pm_container.setProjectId(project_set.getInt("project_id"));
				pm_container.setProjectDescription(project_set.getString("project_version_description"));
				pm_container.setProjectName(project_set.getString("project_version_name"));
				pm_container.setProjectIsDeleted(project_set.getBoolean("project_isdeleted"));
				proj_list.add(pm_container);
			}
                        //close alles zodat de connection pool niet op gaat.
                        project_set.close();
                        project_statement.close();
                        connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
        return proj_list;
	}

	/**
	 * Deze methode geeft een overzicht van alle projecten die de employee aan deelneemt
	 * @param employeeId    id van de employee
	 * @return proj_list    lijst van projecten
	 * @author Fardin Samandar
	 */
	public ArrayList<ProjectModel> project_list_employee(int employeeId){
		ArrayList<ProjectModel> proj_list = new ArrayList<ProjectModel>();
		String project_list_sql = "select * from project_version " +
                    "INNER JOIN project_employee ON project_version_project_fk = project_employee_project_fk " +
                    "INNER JOIN project ON project_version_project_fk=project_id " +
                    "WHERE project_version_current = true AND project_employee_employee_fk = ? " +
                    "AND project_isdeleted=false;";

		try {
                    Connection connection = this.connect.makeConnection();
			PreparedStatement project_statement = connection.prepareStatement(project_list_sql);
			project_statement.setInt(1, employeeId);
			ResultSet project_set = project_statement.executeQuery();
			
			while(project_set.next()) {
				ProjectModel pm_container = new ProjectModel();
				
				pm_container.setProjectId(project_set.getInt("project_version_project_fk"));
				pm_container.setProjectDescription(project_set.getString("project_version_description"));
				pm_container.setProjectName(project_set.getString("project_version_name"));
                                pm_container.setProjectCustomerFk(project_set.getInt("project_version_customer_fk"));
				proj_list.add(pm_container);
			}
                        //close alles zodat de connection pool niet op gaat.
                        project_set.close();
                        project_statement.close();
                        connection.close();
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
         * @return id   de id van het project dat net gemaakt is.
	 * @author rezanaser
         * @throws Exception SQL exception en normale exception
	 */
	public int createNewProject() throws Exception {
		int id = 0;
		PreparedStatement createProject;
		ResultSet projectId = null;
		String insertProject_sql = "insert into project (project_isdeleted) values (?)";
                Connection connection = this.connect.makeConnection();
		createProject = connection.prepareStatement(insertProject_sql, Statement.RETURN_GENERATED_KEYS);
		createProject.setBoolean(1, false);
		createProject.executeUpdate();
		createProject.getGeneratedKeys();
		projectId = createProject.getGeneratedKeys();
		while (projectId.next()) {
                    id = projectId.getInt(1);
                }
                //close alles zodat de connection pool niet op gaat.
                        projectId.close();
                        createProject.close();
                        connection.close();
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
                    Connection connection = this.connect.makeConnection();
			insertProject = connection.prepareStatement(insertUser_sql);
			
			insertProject.setInt(1, createNewProject());
			insertProject.setString(2, projectName);
			insertProject.setString(3, projectDes);
			insertProject.setBoolean(4, true);
			insertProject.setInt(5, customerId);
			insertProject.executeQuery();
			//close alles zodat de connection pool niet op gaat.
                        insertProject.close();
                        connection.close();
			
		} catch (Exception e) {
			System.out.println(e);
			//e.printStackTrace();
		}
	}
	
	/**
	 * Voegt een nieuw project toe, maakt gebruik van de stored procedure die gemaakt wordt in createAddProjectFunction()
	 *
	 * @author Robert den Blaauwe
	 * @param projectModel  het projectModel meegekregen van front-end
         * @return boolean
	 */
	public boolean addProject(ProjectModel projectModel) {
		String login_sql = "SELECT add_project(?,?,?)";
		PreparedStatement project_statement;

		try {
                    Connection connection = this.connect.makeConnection();
			project_statement = connection.prepareStatement(login_sql);
                        project_statement.setString(1, projectModel.getProjectName());
                        project_statement.setString(2, projectModel.getProjectDescription());
                        project_statement.setInt(3, projectModel.getProjectCustomerFk());
			project_statement.executeQuery();
			//close alles zodat de connection pool niet op gaat.
                        project_statement.close();
                        connection.close();
                        return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
                    e.printStackTrace();
                    return false;
		}
	}

    /**
     * Deze methode wijzigt het geselcteerde project project
     * @param projectModel    het projectModel meegekregen van front-end
     * @author rezanaser
     * @throws Exception SQL exception en normale exception
     * @return boolean
     */
	public boolean modifyProject(ProjectModel projectModel) throws Exception {
		String changePreviousVersion = "UPDATE project_version set project_version_current = false "
				+ "WHERE project_version_project_fk = ? AND project_version_current= true";

		String change_project = "INSERT INTO project_version(project_version_project_fk, project_version_name, " +
				"project_version_description, project_version_customer_fk, project_version_current) VALUES(?, ?, ?,?, true)";
                Connection connection = this.connect.makeConnection();
		PreparedStatement changeVersions= connection.prepareStatement(changePreviousVersion);
		changeVersions.setInt(1, projectModel.getProjectId());
		changeVersions.executeUpdate();
		changeVersions.close();

                Connection connection2 = this.connect.makeConnection();
		PreparedStatement changeProject = connection2.prepareStatement(change_project);

		changeProject.setInt(1, projectModel.getProjectId());
		changeProject.setString(2, projectModel.getProjectName());
		changeProject.setString(3, projectModel.getProjectDescription());
                changeProject.setInt(4, projectModel.getProjectCustomerFk());
		changeProject.executeUpdate();
		//close alles zodat de connection pool niet op gaat.
                changeProject.close();
                connection.close();
                connection2.close();
                return true;
	}
	
	/**
	 * Deze methode zet het project op inactive
	 * @param projectModel het projectModel meegekregen van front-end
	 * @author rezanaser
         * @return boolean
	 */
	public boolean removeProject(ProjectModel projectModel) {
		String remove_project = "UPDATE project SET project_isdeleted = true WHERE project_id = ?";
                String setCurrentOnFalse = "UPDATE project_version set project_version_current = false WHERE project_version_project_fk = ?";
		try {
                    Connection connection = this.connect.makeConnection();
			PreparedStatement lock_statement = connection.prepareStatement(remove_project);
                        PreparedStatement lock_version_statement = connection.prepareStatement(setCurrentOnFalse);
			lock_statement.setInt(1, projectModel.getProjectId());
			lock_statement.executeUpdate();
                        lock_version_statement.setInt(1, projectModel.getProjectId());
                        lock_version_statement.executeUpdate();
                        //close
                        lock_statement.close();
                        connection.close();
                        return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
                return false;
	}
        public boolean unRemoveProject(int projectId) {
		String remove_project = "UPDATE project "
				+ "SET project_isdeleted = false "
				+ "WHERE project_id = ? AND project_isdeleted=true";
		try {
                    Connection connection = this.connect.makeConnection();
			PreparedStatement lock_statement = connection.prepareStatement(remove_project);
			lock_statement.setInt(1, projectId);
			lock_statement.executeUpdate();
                        lock_statement.close();
                        connection.close();
                        return true;
		} catch (Exception e) {
			e.printStackTrace();
                        return false;
		}
	}
}
