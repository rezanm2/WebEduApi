package nl.webedu.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import nl.webedu.helpers.DateHelper;
import nl.webedu.models.CategoryModel;
import nl.webedu.models.TaskModel;


/**
 * Deze klasse is verantwoordelijk voor de userstories
 * @author rezanaser
 *
 */
public class TaskDAO {
        private int generatedID;
	private ConnectDAO connect;
        DateHelper dateHelper = new DateHelper();
        public TaskDAO(){
    	this.connect = new ConnectDAO();
        this.createAddUserStoryFunction();
    }
        
        
    /**
     * Maakt een procedure die sprints toe kan voegen.
     * 
     * @author Robert den Blaauwen
     */
    public void createAddUserStoryFunction(){
	String userStoryProcedureSql = "CREATE OR REPLACE FUNCTION add_userstory(name TEXT, description TEXT, sprintId INT4) " +
                                    "RETURNS void AS $$  " +
                                    "DECLARE pk INT; " +
                                    "BEGIN " +
                                        "INSERT INTO userstory(userstory_isdeleted) VALUES(false) " +
                                        "RETURNING userstory_id INTO pk; " +
                                        "INSERT INTO userstory_version(userstory_version_userstory_fk, userstory_version_name, userstory_version_description, userstory_version_current) " +
                                        "VALUES(pk, name, description, true); " +
                                        "IF sprintId > 0 THEN" +
                                        "   INSERT INTO userstory_sprint(userstory_sprint_userstory_fk, userstory_sprint_sprint_fk) " +
                                        "   VALUES(pk, sprintId); " +
                                        "END IF;" +
                                    "END $$ LANGUAGE plpgsql; ";
	try {
            Connection connection = this.connect.makeConnection();
            PreparedStatement userStoryProcedureStatement = connection.prepareStatement(userStoryProcedureSql);
            userStoryProcedureStatement.executeUpdate();
 
            //close alles
            userStoryProcedureStatement.close();
            connection.close();
            } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
	} catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
		}
    }
        
        
	/**
	 * Deze methode vult de combobox met de userstories van het gevraagde project
	 * @author rezanaser
         * @param p_id project id
	 * @return p_id het project nummer
	 */
	public ArrayList<TaskModel> userstoriesProjects(int p_id){
		ArrayList<TaskModel> userstory_alist = new ArrayList<TaskModel>();
		String projects_userstories_sql = "SELECT *  "
				+ "FROM userstory_sprint, userstory_version "
				+ "WHERE userstory_sprint_sprint_fk = ? "
				+ "AND userstory_version_current = true";
		try {
			PreparedStatement userstories_statement = connect.makeConnection().prepareStatement(projects_userstories_sql);
			userstories_statement.setInt(1, p_id);
			ResultSet userstories_sets = userstories_statement.executeQuery();
			while(userstories_sets.next()) {
				TaskModel userstory = new TaskModel();
				userstory.setUserStoryId(userstories_sets.getInt("userstory_version_userstory_fk"));
				userstory.setUserStoryName(userstories_sets.getString("userstory_version_name"));
				userstory_alist.add(userstory);
			}
			userstories_statement.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return userstory_alist;
	  }
		/**
		 * Deze methode vult de combobox met de sprints van het gevraagde project
		 * @author rezanaser
                 * @param sprintID bar
		 * @return sprint_alist
		 */
		public ArrayList<CategoryModel> sprintsProjects(int sprintID){
			ArrayList<CategoryModel> sprint_alist = new ArrayList<CategoryModel>();
			String projects_sprints_sql = "SELECT *  FROM sprint_version where sprint_version_project_fk = ? ";
					//+ "AND entry_version_current = 'y' ";
			try {
				PreparedStatement sprints_statement = connect.makeConnection().prepareStatement(projects_sprints_sql);
				sprints_statement.setInt(1, sprintID);
				ResultSet sprints_sets = sprints_statement.executeQuery();
				while(sprints_sets.next()) {
					CategoryModel sprint = new CategoryModel();
					sprint.setCategoryId(sprints_sets.getInt("sprint_version_sprint_fk"));
					sprint.setCategoryName(sprints_sets.getString("sprint_version_name"));
					sprint.setCategoryStartDate(sprints_sets.getString("sprint_version_startdate"));
					sprint.setCategoryEndDate(sprints_sets.getString("sprint_version_enddate"));
					sprint_alist.add(sprint);
				}
				sprints_statement.close();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			return sprint_alist;
		  }


		/**
		 * Deze methode vult de combobox met de userStorys van het gevraagde sprint
		 * @author rezanaser
                 * @param categoryId foo
		 * @return userStory_alist
		 */
		public ArrayList<TaskModel> userStorysSprints(int categoryId){
			ArrayList<TaskModel> userStory_alist = new ArrayList<TaskModel>();
			String sprints_userStorys_sql = "SELECT *  FROM userStory_version "
                                + "INNER JOIN userstory ON userstory_version_userstory_fk = userstory_id "
                                + "INNER JOIN sprint ON userStory_version_sprint_fk = sprint_id "
                                + "WHERE userStory_version_sprint_fk = ? AND sprint.sprint_isdeleted = FALSE";
			try {
				PreparedStatement userStorys_statement = connect.makeConnection().prepareStatement(sprints_userStorys_sql);
				userStorys_statement.setInt(1, categoryId);
				ResultSet userStorys_sets = userStorys_statement.executeQuery();
				while(userStorys_sets.next()) {
					TaskModel userStory = new TaskModel();
					userStory.setUserStoryId(userStorys_sets.getInt("userStory_version_userStory_fk"));
					userStory.setUserStoryName(userStorys_sets.getString("userStory_version_name"));
					userStory_alist.add(userStory);
				}
				userStorys_statement.close();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			return userStory_alist;
		  }

		

			
			/**
			 * Deze methode vult de combobox met de userStorys van het gevraagde userStory
			 * @author rezanaser
                         * @param categoryId    foo
			 * @return userStory_alist
			 */
			public ArrayList<TaskModel> getTasksByCategory(int categoryId){
				ArrayList<TaskModel> userStory_alist = new ArrayList<TaskModel>();
				String userStorys_userStorys_sql = "SELECT * FROM userstory_sprint " +
                                                                    "INNER JOIN userstory_version ON userstory_sprint_userstory_fk = userstory_version_userstory_fk " +
                                                                    "INNER JOIN sprint ON userstory_sprint_sprint_fk = sprint_id " +
                                                                    "WHERE userstory_version_current = true AND sprint_isdeleted = false"
                                                                    + " AND sprint.sprint_id = ? ";
						//+ "AND entry_version_current = 'y' ";
				try {
					PreparedStatement userStorys_statement = connect.makeConnection().prepareStatement(userStorys_userStorys_sql);
					userStorys_statement.setInt(1, categoryId);
					ResultSet userStorys_sets = userStorys_statement.executeQuery();
					while(userStorys_sets.next()) {
						TaskModel userStory = new TaskModel();
						userStory.setUserStoryId(userStorys_sets.getInt("userStory_version_userstory_fk"));
						userStory.setUserStoryName(userStorys_sets.getString("userStory_version_name"));
						userStory_alist.add(userStory);
					}
					userStorys_statement.close();
				}
				catch(Exception e) {
					System.out.println(e.getMessage());
				}
				return userStory_alist;
			  }


			/**
			 * Deze methode laat alleen de current version zien van een userStory.
			 * @author Jeroen Zandvliet
			 * 
			 * @return userStoryList
			 */
			public ArrayList<TaskModel> userStory_list(){
				ArrayList<TaskModel> userStoryList = new ArrayList<TaskModel>();
				String userStoryListSQL = "SELECT * " +
                                                        "FROM userstory_version " +
                                                        "JOIN userstory ON userstory_id=userstory_version_userstory_fk " +
                                                        "WHERE userstory_version_current=TRUE AND userstory_isdeleted=false;";
				try {
					PreparedStatement userStory_statement = connect.makeConnection().prepareStatement(userStoryListSQL);
					ResultSet userStory_set = userStory_statement.executeQuery();
					while(userStory_set.next()) {
						TaskModel userStoryModelContainer = new TaskModel();
                                                userStoryModelContainer.setProjectId(userStory_set.getInt("userstory_version_project_fk"));
						userStoryModelContainer.setUserStoryId(userStory_set.getInt("userstory_version_userstory_fk"));
						userStoryModelContainer.setUserStoryName(userStory_set.getString("userstory_version_name"));
                                                userStoryModelContainer.setUserStoryDescription(userStory_set.getString("userstory_version_description"));
						userStoryModelContainer.setDeleted(userStory_set.getBoolean("userstory_isdeleted"));
                                                userStoryModelContainer.setIsCurrent(userStory_set.getBoolean("userstory_version_current"));
						userStoryList.add(userStoryModelContainer);
					}
				} catch (SQLException e) {

					System.out.println(e.getMessage());
				} catch (Exception e) {

					System.out.println(e.getMessage());
				} 
				return userStoryList;
			}


			/**
			 * Deze methode geeft een lijst van userStorys die bij een userStory horen
			 * @param userStoryModel model van taak
			 * @return userStory_list
			 */
			
			public ArrayList<TaskModel> userStory_list(TaskModel userStoryModel){
				ArrayList<TaskModel> userStory_list = new ArrayList<TaskModel>();
				String userStory_list_sql = "SELECT * FROM userStory_version "
						+ "INNER JOIN userStory ON (userStory.userStory_id = userStory_version.userStory_version_userStory_fk) "
						+ "WHERE userStory_version.userStory_version_userStory_fk="+userStoryModel.getUserStoryId()
						+ " ORDER BY userStory_version.userStory_version_name ASC";
				try {
					PreparedStatement userStory_statement = connect.makeConnection().prepareStatement(userStory_list_sql);
					ResultSet userStory_set = userStory_statement.executeQuery();
					while(userStory_set.next()) {
						TaskModel userStoryModelContainer = new TaskModel();
						userStoryModelContainer.setUserStoryId(userStory_set.getInt("userStory_id"));
						userStoryModelContainer.setUserStoryDescription(userStory_set.getString("userStory_version_description"));
						userStoryModelContainer.setUserStoryName(userStory_set.getString("userStory_version_name"));
						userStoryModelContainer.setDeleted(userStory_set.getBoolean("userStory_isdeleted"));
						userStory_list.add(userStoryModelContainer);
					}
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
				return userStory_list;
			}
			
			/**
			 * Deze methode geeft alleen de userStorys terug van de meegegeven medewerker.
			 * @author Jeroen Zandvliet
			 * @param employeeID id van medewerker
			 * @return userStoryList
			 */
			
			public ArrayList<TaskModel> userStory_list_employee(int employeeID){
				ArrayList<TaskModel> userStory_list = new ArrayList<TaskModel>();
				String userStory_list_sql = "SELECT * FROM userStory_version";
				try {
					PreparedStatement userStory_statement = connect.makeConnection().prepareStatement(userStory_list_sql);
					userStory_statement.setInt(1, employeeID);
					ResultSet userStory_set = userStory_statement.executeQuery();
					
					while(userStory_set.next()) {
						TaskModel userStoryModelContainer = new TaskModel();
						
						userStoryModelContainer.setUserStoryId(userStory_set.getInt("userStory_version_userStory_fk"));
						userStoryModelContainer.setUserStoryDescription(userStory_set.getString("userStory_version_description"));
						userStoryModelContainer.setUserStoryName(userStory_set.getString("userStory_version_name"));
						userStory_list.add(userStoryModelContainer);
					}
				} catch (SQLException e) {

					System.out.println(e.getMessage());
				} catch (Exception e) {

					System.out.println(e.getMessage());
				}
				return userStory_list;
			}
			

			public ArrayList <TaskModel> toonUserUserStory (int e_id)
			{
				ArrayList<TaskModel> userStoryList = new ArrayList <TaskModel>();
				String userStoryQuery = "SELECT * FROM userStory_version, userStory "
						+ "WHERE userStory_version_userStory_fk = userStory_id";
				
				try 
				{
					PreparedStatement userStoryStatement = connect.makeConnection().prepareStatement(userStoryQuery);
					ResultSet userStory_set = userStoryStatement.executeQuery();
					while(userStory_set.next())
					{
						TaskModel model = new TaskModel();
						model.setUserStoryName(userStory_set.getString("userStory_version_name"));
						userStoryList.add(model);
					}
					userStoryStatement.close();
				} catch (Exception e) 
				{

					e.printStackTrace();
				}
				return userStoryList;
			}
		
			
                        
                        
                public void addUserStory(TaskModel userStoryModel) {
		String login_sql = "SELECT add_userstory(?,?,?)";
		PreparedStatement userStory_statement;
		try {
                    Connection connection = this.connect.makeConnection();
			userStory_statement = connection.prepareStatement(login_sql);
                        userStory_statement.setString(1, userStoryModel.getUserStoryName());
                        userStory_statement.setString(2, userStoryModel.getUserStoryDescription());
                        userStory_statement.setInt(3, userStoryModel.getCategoryId());
			userStory_statement.executeQuery();
			//close alles zodat de connection pool niet op gaat.
                        userStory_statement.close();
                        connection.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


		/**
			 * Deze methode past een eerder gemaakte userStory aan en zet de vorige versie op nonactief.
			 * @author Jeroen Zandvliet
			 * @param userStoryModel model van Taak
			 */
			
			public void modifyUserStory(TaskModel userStoryModel)
			{
                            System.out.println(userStoryModel.getUserStoryId());
				String changePreviousVersion = "UPDATE userstory_version SET userstory_version_current = false "
						+ "WHERE userstory_version_userstory_fk = ? AND userstory_version_current= true";
				
				String change_userStory = "INSERT INTO userstory_version(userstory_version_userstory_fk, userstory_version_name, userstory_version_description, userstory_version_current)"
						+ "VALUES(?, ?, ?, true)";
                                //Added Jeroen
                                String change_userStory_sprint = "UPDATE userstory_sprint SET userstory_sprint_sprint_fk = ? WHERE userstory_sprint_userstory_fk = ? ";
                                
                                
				
				try {
					PreparedStatement changeVersions= connect.makeConnection().prepareStatement(changePreviousVersion);
					changeVersions.setInt(1, userStoryModel.getUserStoryId());
					changeVersions.executeUpdate();
					changeVersions.close();
                                        
                                        
					PreparedStatement changeUserStory = connect.makeConnection().prepareStatement(change_userStory);
					changeUserStory.setInt(1, userStoryModel.getUserStoryId());
					changeUserStory.setString(2, userStoryModel.getUserStoryName());
					changeUserStory.setString(3, userStoryModel.getUserStoryDescription());
					changeUserStory.executeUpdate();
					
					changeUserStory.close();
                            
                                        
                                        PreparedStatement changeUserStorySprint = connect.makeConnection().prepareStatement(change_userStory_sprint);
                                        changeUserStorySprint.setInt(1, userStoryModel.getCategoryId());
                                        changeUserStorySprint.setInt(2, userStoryModel.getUserStoryId());
					changeUserStorySprint.executeUpdate();
					changeUserStorySprint.close();
                                        
//                 
                                        
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			public void removeUserStory(TaskModel userStoryModel) 
			{
				String deleteUserStory = "UPDATE userstory "
					+ "SET userstory_isdeleted = true "
					+ "WHERE userStory_id = ?";
                                String setCurrentOnFalse = "UPDATE userstory_version set userstory_version_current = false WHERE userstory_version_userstory_fk = ?";
				try 
				{
					PreparedStatement lockStatement = connect.makeConnection().prepareStatement(deleteUserStory);
                                        System.out.println("NEW" + userStoryModel.getUserStoryId());
					lockStatement.setInt(1, userStoryModel.getUserStoryId());
					lockStatement.executeUpdate();
                                        
                                        PreparedStatement unCurrent = connect.makeConnection().prepareStatement(setCurrentOnFalse);
					unCurrent.setInt(1, userStoryModel.getUserStoryId());
					unCurrent.executeUpdate();
				} catch (Exception e) 
				{
					System.out.println(e.getMessage());
				}
			}
			
        /**
         * Haalt een enkele userstory op basiis van userstory_id
         * Haalt alleen op waar userstory_version_current=true
         * 
         * @throws Exception    kek
         * @author    Robert
         * @param userstoryId   id van userstory
         * @return  userstory
         */
        public TaskModel getUserstory(int userstoryId) throws Exception{
            String userstorySql = "SELECT * FROM userstory INNER JOIN userstory_version " + 
                                "ON userstory_id=userstory_version_userstory_fk " +
                                "WHERE userstory_id=? AND userstory_version_current=true;";
            System.out.println(this.getClass().toString()+": "+userstoryId);
            
            Connection connection = this.connect.makeConnection();
            PreparedStatement userstoryStatement = connection.prepareStatement(userstorySql);
            userstoryStatement.setInt(1,userstoryId);
            ResultSet userstorySet = userstoryStatement.executeQuery();
            
            userstorySet.next();
            TaskModel userstory = new TaskModel();
            userstory.setUserStoryId(userstorySet.getInt("userstory_id"));
            userstory.setUserStoryName(userstorySet.getString("userstory_version_name"));
            userstory.setUserStoryDescription(userstorySet.getString("userstory_version_description"));
            userstory.setDeleted(userstorySet.getBoolean("userstory_isdeleted"));
            userstory.setIsCurrent(true);
            
            //close alles zodat de pool niet op gaat.
            userstorySet.close();
            userstoryStatement.close();
            connection.close();
            
            return userstory;
        }

         public void unRemoveUserStory(int userStoryId) {
		String remove_project = "UPDATE project "
				+ "SET project_isdeleted = false "
				+ "WHERE project_id = ? AND project_isdeleted=true";
		try {
                    Connection connection = this.connect.makeConnection();
			PreparedStatement lock_statement = connection.prepareStatement(remove_project);
			lock_statement.setInt(1, userStoryId);
			lock_statement.executeUpdate();
                        lock_statement.close();
                        connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
         	public ArrayList<CategoryModel> categoriesProject(int p_id){
		ArrayList<CategoryModel> sprint_alist = new ArrayList<CategoryModel>();
		String projectsSprintsSql = "SELECT *  FROM sprint_version sv INNER JOIN sprint s ON s.sprint_id=sv.sprint_version_sprint_fk " +
                                "INNER JOIN project_version pv " +
				"ON sv.sprint_version_project_fk=pv.project_version_project_fk INNER JOIN project p " +
				"ON p.project_id=pv.project_version_project_fk WHERE pv.project_version_project_fk= ? " +
				"AND sv.sprint_version_current=TRUE AND project_isdeleted=FALSE AND pv.project_version_current = true";
				//+ "AND entry_version_current = 'y' ";
		try {
                        Connection connection = this.connect.makeConnection();
			PreparedStatement sprintsStatement = connection.prepareStatement(projectsSprintsSql);
			sprintsStatement.setInt(1, p_id);
			ResultSet sprintsSets = sprintsStatement.executeQuery();
			while(sprintsSets.next()) {
				CategoryModel sprint;
				sprint = new CategoryModel();
				sprint.setCategoryId(sprintsSets.getInt("sprint_version_sprint_fk"));
				sprint.setCategoryName(sprintsSets.getString("sprint_version_name"));
                                sprint.setCategoryDescription(sprintsSets.getString("sprint_version_description"));
				sprint.setCategoryStartDate(sprintsSets.getString("sprint_version_startdate"));
				sprint.setCategoryEndDate(sprintsSets.getString("sprint_version_enddate"));
				sprint.setCategoryIsDeleted(sprintsSets.getBoolean("sprint_isdeleted"));
                                sprint.setIsCurrent(sprintsSets.getBoolean("sprint_version_current"));
                                sprint.setProjectFK(sprintsSets.getInt("sprint_version_project_fk"));
				sprint_alist.add(sprint);
			}
                        // close alles zodat de connection pool niet op gaat
                        sprintsSets.close();
			sprintsStatement.close();
                        connection.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return sprint_alist;
	  }
}

