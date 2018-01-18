package nl.webedu.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import nl.webedu.helpers.DateHelper;
import nl.webedu.models.CategoryModel;
import nl.webedu.models.ProjectModel;
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
	String sprintProcedureSql = "CREATE OR REPLACE FUNCTION add_userstory(project_id INT4, sprint_name TEXT, description TEXT, startdate DATE, enddate DATE) " +
                                    "RETURNS void AS $$ " +
                                    "DECLARE pk INT; " +
                                    "BEGIN " +
                                    "	INSERT INTO sprint(sprint_isdeleted) VALUES(false) " +
                                    "	RETURNING sprint_version_sprint_fk INTO pk; " +
                                    "	INSERT INTO sprint_version(sprint_version_sprint_fk, sprint_version_project_fk," +
                                    "    	sprint_version_name, sprint_version_description, sprint_version_startdate, sprint_version_enddate," +
                                    "        sprint_version_current) " +
                                    "    VALUES(pk,project_id,sprint_name,description,startdate,enddate, true); " +
                                    "END $$ LANGUAGE plpgsql;";
	try {
            Connection connection = this.connect.makeConnection();
            PreparedStatement sprintProcedureStatement = connection.prepareStatement(sprintProcedureSql);
            sprintProcedureStatement.executeUpdate();
 
            //close alles
            sprintProcedureStatement.close();
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
	 * Deze methode vult de combobox met de userstories van het gevraagde project
	 * @author rezanaser
	 * @return p_id >het project nummer
	 */
	public ArrayList<TaskModel> userstoriesProjects(int p_id){
		ArrayList<TaskModel> userstory_alist = new ArrayList<TaskModel>();
		String projects_userstories_sql = "SELECT *  "
				+ "FROM userstory_sprint, userstory_version "
				+ "WHERE userstory_sprint_sprint_fk = ? "
				+ "AND userstory_version_current = true";
				//+ "AND entry_version_current = 'y' ";
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
	 * 
	 * @author Fardin Samandar
	 * @date 24-10-2017
	 */

		
		/**
		 * Deze methode vult de combobox met de sprints van het gevraagde project
		 * @author rezanaser
		 * @return
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

//--------------------------------------------------------------------------------------
		

		/**
		 * Deze methode vult de combobox met de userStorys van het gevraagde sprint
		 * @author rezanaser
		 * @return
		 */
		public ArrayList<TaskModel> userStorysSprints(int userStoryID){
			ArrayList<TaskModel> userStory_alist = new ArrayList<TaskModel>();
			String sprints_userStorys_sql = "SELECT *  FROM userStory_version where userStory_version_sprint_fk = ? ";
					//+ "AND entry_version_current = 'y' ";
			try {
				PreparedStatement userStorys_statement = connect.makeConnection().prepareStatement(sprints_userStorys_sql);
				userStorys_statement.setInt(1, userStoryID);
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
			 * @return
			 */
			public ArrayList<TaskModel> userStorysUserStorys(int p_id){
				ArrayList<TaskModel> userStory_alist = new ArrayList<TaskModel>();
				String userStorys_userStorys_sql = "SELECT *  FROM userStory_version where userStory_version_userStory_fk = ? ";
						//+ "AND entry_version_current = 'y' ";
				try {
					PreparedStatement userStorys_statement = connect.makeConnection().prepareStatement(userStorys_userStorys_sql);
					userStorys_statement.setInt(1, p_id);
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
				String userStoryListSQL = "SELECT  pv.project_version_project_fk, pv.project_version_name, usv.userstory_version_userstory_fk, usv.userstory_version_name, usv.userstory_version_description,usv.userstory_version_current , sv.sprint_version_name, sv.sprint_version_sprint_fk, u.userstory_isdeleted " +
					    "FROM sprint_version sv " +
					    "JOIN sprint s ON sv.sprint_version_sprint_fk=s.sprint_id " +
					    "JOIN userstory_sprint us ON s.sprint_id=us.userstory_sprint_sprint_fk " +
					    "JOIN userstory_version usv ON usv.userstory_version_userstory_fk=us.userstory_sprint_userstory_fk " +
					    "JOIN userstory u ON userstory_id=usv.userstory_version_userstory_fk " +
                                            "JOIN project_version pv ON  sv.sprint_version_project_fk=pv.project_version_project_fk " +
					    "WHERE us.userstory_sprint_sprint_fk=sv.sprint_version_sprint_fk " +
					    "AND usv.userstory_version_current=TRUE AND sv.sprint_version_current=true ";
			
				
				
				try {
					PreparedStatement userStory_statement = connect.makeConnection().prepareStatement(userStoryListSQL);
					ResultSet userStory_set = userStory_statement.executeQuery();
					while(userStory_set.next()) {
						TaskModel userStoryModelContainer = new TaskModel();
                                                userStoryModelContainer.setProjectName(userStory_set.getString("project_version_name"));
                                                userStoryModelContainer.setProjectId(userStory_set.getInt("project_version_project_fk"));
						userStoryModelContainer.setUserStoryId(userStory_set.getInt("userstory_version_userstory_fk"));
						userStoryModelContainer.setUserStoryName(userStory_set.getString("userstory_version_name"));
                                                userStoryModelContainer.setUserStoryDescription(userStory_set.getString("userstory_version_description"));
						userStoryModelContainer.setCategoryName(userStory_set.getString("sprint_version_name"));
                                                userStoryModelContainer.setCategoryId(userStory_set.getInt("sprint_version_sprint_fk"));
						userStoryModelContainer.setDeleted(userStory_set.getBoolean("userstory_isdeleted"));
                                                userStoryModelContainer.setIsCurrent(userStory_set.getBoolean("userstory_version_current"));
                                                userStoryModelContainer.setSprintFK(userStory_set.getInt("sprint_version_sprint_fk"));
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
			 * @param userStoryModel
			 * @return
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
			 * @param employeeID
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


			/**
			 * Deze methode geeft het gegenereerde ID van een userStory terug
			 * @author Jeroen Zandvliet
			 * @return generatedID
			 */
			public int createNewUserStory()
				{
					generatedID = 0;
					PreparedStatement createUserStory;
					ResultSet userStoryID = null;
					String insertUserStoryStatement = "INSERT INTO userStory(userstory_isdeleted) VALUES(?)";
					
					try 
					{
						createUserStory = connect.makeConnection().prepareStatement(insertUserStoryStatement, Statement.RETURN_GENERATED_KEYS);
						
						createUserStory.setBoolean(1, false);
						createUserStory.executeUpdate();
			//			createUserStory.getGeneratedKeys();
						userStoryID = createUserStory.getGeneratedKeys();
						
						
						while(userStoryID.next())
						{
							generatedID = userStoryID.getInt(1);
						}
					
					} catch (Exception e) 
					{
						System.out.println(e.getMessage());
					}
						return generatedID;
				}


			/**
			 * Deze methode voegt een gekozen userStory aan de database toe.
			 * @author Jeroen Zandvliet
			 * @param userStoryName
			 * @param userStoryID
			 * @param userStoryDescription
			 * @param userStoryStartDate
			 * @param userStoryEndDate
			 */
			public void addUserStoryToDatabase(int sprintID, String userStoryName, String userStoryDescription)
			{
				PreparedStatement addUserStory;
				String insertStatement = "INSERT INTO userstory_version(userstory_version_userstory_fk, userstory_version_name, userstory_version_description, userstory_version_current) " 
						+ "VALUES(?,?,?, true)";
		
				try 
				{
					addUserStory = connect.makeConnection().prepareStatement(insertStatement);
					
					addUserStory.setInt(1, createNewUserStory());
					addUserStory.setString(2, userStoryName);
					addUserStory.setString(3, userStoryDescription);
					
					addUserStory.executeQuery();
					addUserStory.close();			
					
				} catch (SQLException e) {

					System.out.println(e.getMessage());
				} catch (Exception e) {
					System.out.println(e.getMessage());
					
				} 
				
				
				
				PreparedStatement linkUserStorySprint;
				String linkStatement = "INSERT INTO userstory_sprint(userstory_sprint_userstory_fk, userstory_sprint_sprint_fk) " 
						+ "VALUES(?,?)";
				
				try 
				{
					linkUserStorySprint = connect.makeConnection().prepareStatement(linkStatement);
					
					linkUserStorySprint.setInt(1, generatedID);
					linkUserStorySprint.setInt(2,  sprintID);
					
					linkUserStorySprint.executeQuery();
					linkUserStorySprint.close();			
					
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				} catch (Exception e) {
					System.out.println(e.getMessage());
					
				} 
				
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
		
			
                        
                        
              public boolean addUserStory(TaskModel userStoryModel) {
		String login_sql = "SELECT add_userstory(?,?,?)";
		PreparedStatement userStory_statement;

		try {
                    Connection connection = this.connect.makeConnection();
			userStory_statement = connection.prepareStatement(login_sql);
                        userStory_statement.setString(1, userStoryModel.getUserStoryName());
                        userStory_statement.setString(2, userStoryModel.getUserStoryDescription());
                        userStory_statement.setInt(3, userStoryModel.getSprintFK());
			userStory_statement.executeQuery();
			//close alles zodat de connection pool niet op gaat.
                        userStory_statement.close();
                        connection.close();
                        return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
                        return false;
		}
	}


		/**
			 * Deze methode past een eerder gemaakte userStory aan en zet de vorige versie op nonactief.
			 * @author Jeroen Zandvliet
			 * @param userStoryID
			 * @param userStoryName
			 * @param userStoryID
			 * @param userStoryDescription
			 * @param userStoryStartDate
			 * @param userStoryEndDate
			 */
			
			public boolean modifyUserStory(TaskModel userStoryModel)
			{
                                        System.out.println("Category ID before added block of code: " + userStoryModel.getCategoryId());
                                        System.out.println("UserStory ID before added block of code: " + userStoryModel.getCategoryId());
				String changePreviousVersion = "UPDATE userstory_version SET userstory_version_current = false "
						+ "WHERE userstory_version_userstory_fk = ? AND userstory_version_current= true";
				
				String change_userStory = "INSERT INTO userstory_version(userstory_version_userstory_fk, userstory_version_name, userstory_version_description, userstory_version_current)"
						+ "VALUES(?, ?, ?, true)";
                                //Added Jeroen
                                String change_userStory_sprint = "UPDATE userstory_sprint " +
                                                "SET userstory_sprint_sprint_fk = ? " +
                                                "WHERE userstory_sprint_userstory_fk = ? ";
				
				
				try {
					PreparedStatement changeVersions= connect.makeConnection().prepareStatement(changePreviousVersion);
					changeVersions.setInt(1, userStoryModel.getUserStoryId());
					changeVersions.executeUpdate();
					changeVersions.close();
					PreparedStatement changeUserStory = connect.makeConnection().prepareStatement(change_userStory);
					changeUserStory.setInt(1, userStoryModel.getUserStoryId());
					changeUserStory.setString(2, userStoryModel.getUserStoryName());
					changeUserStory.setString(3, userStoryModel.getUserStoryDescription());
					changeUserStory.executeQuery();
					
					changeUserStory.close();
                            
                                        
                                        PreparedStatement changeUserStorySprint = connect.makeConnection().prepareStatement(change_userStory_sprint);
                                        changeUserStorySprint.setInt(1, userStoryModel.getCategoryId());
                                        changeUserStorySprint.setInt(2, userStoryModel.getUserStoryId());
					changeUserStorySprint.executeQuery();
					changeUserStorySprint.close();
                                        return true;
                                        
				} catch (Exception e) {
					System.out.println(e.getMessage());
                                        return false;
				}
			}
			
			public boolean removeUserStory(int userStoryID) 
			{
				String deleteUserStory = "UPDATE userStory "
					+ "SET userStory_isdeleted = true "
					+ "WHERE userStory_id = ?";
				try 
				{
					PreparedStatement lockStatement = connect.makeConnection().prepareStatement(deleteUserStory);
					lockStatement.setInt(1, userStoryID);
					lockStatement.executeUpdate();
                                        return true;
				} catch (Exception e) 
				{
					System.out.println(e.getMessage());
                                        return false;
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

         public boolean unRemoveUserStory(int userStoryId) {
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
                        return true;
		} catch (Exception e) {
			e.printStackTrace();
                        return false;
		}
	}
		}

