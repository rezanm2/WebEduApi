package nl.webedu.dao;

import nl.webedu.models.SprintModel;
import nl.webedu.models.UserStoryModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Deze klasse is verantwoordelijk voor de userstories
 * @author rezanaser
 *
 */
public class UserStoryDAO {

	int generatedID;
	private ConnectDAO connect;

	public UserStoryDAO(){
		this.connect = new ConnectDAO(); // Connectie maken met de database
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
        public UserStoryModel getUserstory(int userstoryId) throws Exception{
            String userstorySql = "SELECT * FROM userstory INNER JOIN userstory_version " + 
                                "ON userstory_id=userstory_version_userstory_fk " +
                                "WHERE userstory_id=? AND userstory_version_current=true;";
            System.out.println(this.getClass().toString()+": "+userstoryId);
            PreparedStatement userstoryStatement = this.connect.makeConnection().prepareStatement(userstorySql);
            userstoryStatement.setInt(1,userstoryId);
            ResultSet userstorySet = userstoryStatement.executeQuery();
            
            userstorySet.next();
            UserStoryModel userstory = new UserStoryModel();
            userstory.setUserStoryId(userstorySet.getInt("userstory_id"));
            userstory.setUserStoryName(userstorySet.getString("userstory_version_name"));
            userstory.setUserStoryDescription(userstorySet.getString("userstory_version_description"));
            userstory.setDeleted(userstorySet.getBoolean("userstory_isdeleted"));
            userstory.setIsCurrent(true);
            userstoryStatement.close();
            
            return userstory;
        }

	/**
	 * Deze methode vult de combobox met de userstories van het gevraagde project
	 * @author rezanaser
	 * @param p_id  project nummer
	 * @return p_id het project nummer
	 */
	public ArrayList<UserStoryModel> userstoriesProjects(int p_id){
		ArrayList<UserStoryModel> userstory_alist = new ArrayList<UserStoryModel>();
		String projects_userstories_sql = "SELECT *  "
				+ "FROM userstory_version "
				+ "WHERE userstory_version_project_fk = ? "
				+ "AND userstory_version_current = true";
		try {
			PreparedStatement userstories_statement = this.connect.makeConnection().prepareStatement(projects_userstories_sql);
			userstories_statement.setInt(1, p_id);
			ResultSet userstories_sets = userstories_statement.executeQuery();
			while(userstories_sets.next()) {
				UserStoryModel userstory = new UserStoryModel();
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
	 * @param sprintID id van sprint
	 * @return  sprint_alist
         * @throws Exception SQL exception en normale exception
	 */
	public ArrayList<SprintModel> sprintsProjects(int sprintID) throws Exception {
		SprintModel sprint;
		ArrayList<SprintModel> sprint_alist = new ArrayList<SprintModel>();
		String projects_sprints_sql = "SELECT *  FROM sprint_version where sprint_version_project_fk = ? ";
				//+ "AND entry_version_current = 'y' ";
		PreparedStatement sprints_statement = this.connect.makeConnection().prepareStatement(projects_sprints_sql);
		sprints_statement.setInt(1, sprintID);
		ResultSet sprints_sets = sprints_statement.executeQuery();
		while(sprints_sets.next()) {
			sprint = new SprintModel();
			sprint.setSprintId(sprints_sets.getInt("sprint_version_sprint_fk"));
            sprint.setSprintName(sprints_sets.getString("sprint_version_name"));
            sprint.setSprintStartDate(sprints_sets.getString("sprint_version_startdate"));
            sprint.setSprintEndDate(sprints_sets.getString("sprint_version_enddate"));
            sprint_alist.add(sprint);
        }
		sprints_statement.close();
		return sprint_alist;
	  }


	/**
	 * Deze methode vult de combobox met de userStorys van het gevraagde sprint
	 * @author rezanaser
	 * @param userStoryID   id van userstory
	 * @return usreSTory_alist
	 */
	public ArrayList<UserStoryModel> userStorysSprints(int userStoryID){
		UserStoryModel userStory;
		ArrayList<UserStoryModel> userStory_alist = new ArrayList<UserStoryModel>();
		String sprints_userStorys_sql = "SELECT *  FROM userStory_version where userStory_version_sprint_fk = ? ";
				//+ "AND entry_version_current = 'y' ";
		try {
			PreparedStatement userStorys_statement = this.connect.makeConnection().prepareStatement(sprints_userStorys_sql);
			userStorys_statement.setInt(1, userStoryID);
			ResultSet userStoriesSet = userStorys_statement.executeQuery();
			while(userStoriesSet.next()) {
				userStory = new UserStoryModel();
				userStory.setUserStoryId(userStoriesSet.getInt("userStory_version_userStory_fk"));
				userStory.setUserStoryName(userStoriesSet.getString("userStory_version_name"));
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
	 * @param   p_id    id van eits?
	 * @return  userStory_alist
         * @throws Exception SQL exception en normale exception
	 */
	public ArrayList<UserStoryModel> userStoriesVersion(int p_id) throws Exception {
		ArrayList<UserStoryModel> userStory_alist = new ArrayList<UserStoryModel>();
		String userStorys_userStorys_sql = "SELECT *  FROM userStory_version where userStory_version_userStory_fk = ? ";
				//+ "AND entry_version_current = 'y' ";
		PreparedStatement userStorys_statement = this.connect.makeConnection().prepareStatement(userStorys_userStorys_sql);
		userStorys_statement.setInt(1, p_id);
		ResultSet userStoriesSet = userStorys_statement.executeQuery();
		while(userStoriesSet.next()) {
                    UserStoryModel userStory = new UserStoryModel();
                    userStory.setUserStoryId(userStoriesSet.getInt("userStory_version_userStory_fk"));
                    userStory.setUserStoryName(userStoriesSet.getString("userStory_version_name"));
                    userStory_alist.add(userStory);
                }
		userStorys_statement.close();
		return userStory_alist;
	  }


	/**
	 * Deze methode laat alleen de current version zien van een userStory.
	 * @author Jeroen Zandvliet
	 * @return userStoryList
	 */
	public ArrayList<UserStoryModel> userStory_list(){
		ArrayList<UserStoryModel> userStoryList = new ArrayList<UserStoryModel>();
		String userStoryListSQL =     "SELECT usv.userstory_version_userstory_fk, usv.userstory_version_name, usv.userstory_version_description, sv.sprint_version_name, u.userstory_isdeleted " +
				"FROM sprint_version sv " +
				"JOIN sprint s ON sv.sprint_version_sprint_fk=s.sprint_id " +
				"JOIN userstory_sprint us ON s.sprint_id=us.userstory_sprint_sprint_fk " +
				"JOIN userstory_version usv ON usv.userstory_version_userstory_fk=us.userstory_sprint_userstory_fk " +
				"JOIN userstory u ON userstory_id=usv.userstory_version_userstory_fk " +
				"WHERE us.userstory_sprint_sprint_fk=sv.sprint_version_sprint_fk " +
				"AND usv.userstory_version_current=TRUE AND sv.sprint_version_current=true ";

		try {
			PreparedStatement userStory_statement = this.connect.makeConnection().prepareStatement(userStoryListSQL);
			ResultSet userStory_set = userStory_statement.executeQuery();
			while(userStory_set.next()) {
				UserStoryModel userStoryModelContainer = new UserStoryModel();
				userStoryModelContainer.setUserStoryId(userStory_set.getInt("userstory_version_userstory_fk"));
				userStoryModelContainer.setUserStoryDescription(userStory_set.getString("userstory_version_description"));
				userStoryModelContainer.setUserStoryName(userStory_set.getString("userstory_version_name"));
				userStoryModelContainer.setSprintName(userStory_set.getString("sprint_version_name"));
				userStoryModelContainer.setDeleted(userStory_set.getBoolean("userstory_isdeleted"));

				userStoryList.add(userStoryModelContainer);
			}
                        userStory_statement.close();
		} catch (SQLException e) {

			e.printStackTrace();
		} catch (Exception e) {

			e.printStackTrace();
		}
		return userStoryList;
	}


	/**
	 * Deze methode geeft een lijst van userStorys die bij een userStory horen
	 * @param userStoryModel    ayy
	 * @return  userStory_list
	 */
	public ArrayList<UserStoryModel> userStory_list(UserStoryModel userStoryModel){
		ArrayList<UserStoryModel> userStory_list = new ArrayList<UserStoryModel>();
		String userStory_list_sql = "SELECT * FROM userStory_version "
				+ "INNER JOIN userStory ON (userStory.userStory_id = userStory_version.userStory_version_userStory_fk) "
				+ "WHERE userStory_version.userStory_version_userStory_fk="+ userStoryModel.getUserStoryId()
				+ " ORDER BY userStory_version.userStory_version_name ASC";
		try {
			PreparedStatement userStory_statement = this.connect.makeConnection().prepareStatement(userStory_list_sql);
			ResultSet userStory_set = userStory_statement.executeQuery();
			while(userStory_set.next()) {
				UserStoryModel userStoryModelContainer = new UserStoryModel();
				userStoryModelContainer.setUserStoryId(userStory_set.getInt("userStory_id"));
				userStoryModelContainer.setUserStoryDescription(userStory_set.getString("userStory_version_description"));
				userStoryModelContainer.setUserStoryName(userStory_set.getString("userStory_version_name"));
				userStoryModelContainer.setDeleted(userStory_set.getBoolean("userStory_isdeleted"));
				userStory_list.add(userStoryModelContainer);
			}
                        userStory_statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userStory_list;
	}

	/**
	 * Deze methode geeft alleen de userStorys terug van de meegegeven medewerker.
	 * @author Jeroen Zandvliet
	 * @param employeeID    ayy
	 * @return userStoryList
	 */

	public ArrayList<UserStoryModel> userStory_list_employee(int employeeID){
		ArrayList<UserStoryModel> userStory_list = new ArrayList<UserStoryModel>();
		String userStory_list_sql = "SELECT * FROM userStory_version";
		try {
			PreparedStatement userStory_statement = this.connect.makeConnection().prepareStatement(userStory_list_sql);
			userStory_statement.setInt(1, employeeID);
			ResultSet userStory_set = userStory_statement.executeQuery();

			while(userStory_set.next()) {
				UserStoryModel userStoryModelContainer = new UserStoryModel();

				userStoryModelContainer.setUserStoryId(userStory_set.getInt("userStory_version_userStory_fk"));
				userStoryModelContainer.setUserStoryDescription(userStory_set.getString("userStory_version_description"));
				userStoryModelContainer.setUserStoryName(userStory_set.getString("userStory_version_name"));
				userStory_list.add(userStoryModelContainer);
			}
                        userStory_statement.close();
		} catch (SQLException e) {

			e.printStackTrace();
		} catch (Exception e) {

			e.printStackTrace();
		}
		return userStory_list;
	}


	/**
	 * Deze methode geeft het gegenereerde ID van een userStory terug
	 * @author Jeroen Zandvliet
	 * @return generatedID
	 */
	public int createNewUserStory() {
		generatedID = 0;
		PreparedStatement createUserStory;
		ResultSet userStoryID = null;
		String insertUserStoryStatement = "INSERT INTO userStory(userstory_isdeleted) VALUES(?)";

		try {
			createUserStory = this.connect.makeConnection().prepareStatement(insertUserStoryStatement, Statement.RETURN_GENERATED_KEYS);
			createUserStory.setBoolean(1, false);
			createUserStory.executeUpdate();
			userStoryID = createUserStory.getGeneratedKeys();
			while(userStoryID.next()) {
				generatedID = userStoryID.getInt(1);
			}
                        createUserStory.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return generatedID;
	}


	/**
	 * Deze methode voegt een gekozen userStory aan de database toe.
	 * @author Jeroen Zandvliet
	 * @param userStoryName         lol
	 * @param sprintID           lol
	 * @param userStoryDescription  lol
	 */
	public void addUserStoryToDatabase(int sprintID, String userStoryName, String userStoryDescription) {
		PreparedStatement addUserStory;
		String insertStatement = "INSERT INTO userstory_version(userstory_version_userstory_fk, userstory_version_name, userstory_version_description, userstory_version_current) "
				+ "VALUES(?,?,?, true)";

		try {
			addUserStory = this.connect.makeConnection().prepareStatement(insertStatement);

			addUserStory.setInt(1, createNewUserStory());
			addUserStory.setString(2, userStoryName);
			addUserStory.setString(3, userStoryDescription);

			addUserStory.executeQuery();
			addUserStory.close();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
	}



		PreparedStatement linkUserStorySprint;
		String linkStatement = "INSERT INTO userstory_sprint(userstory_sprint_userstory_fk, userstory_sprint_sprint_fk) "
				+ "VALUES(?,?)";

		try {
			linkUserStorySprint = this.connect.makeConnection().prepareStatement(linkStatement);

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




	public ArrayList <UserStoryModel> toonUserUserStory (int e_id) {
		ArrayList<UserStoryModel> userStoryList = new ArrayList <UserStoryModel>();
		String userStoryQuery = "SELECT * FROM userStory_version, userStory "
				+ "WHERE userStory_version_userStory_fk = userStory_id";

		try {
			PreparedStatement userStoryStatement = this.connect.makeConnection().prepareStatement(userStoryQuery);
			ResultSet userStory_set = userStoryStatement.executeQuery();
			while(userStory_set.next()){
				UserStoryModel model = new UserStoryModel();
				model.setUserStoryName(userStory_set.getString("userStory_version_name"));
				userStoryList.add(model);
			}
			userStoryStatement.close();
		} catch (Exception e){
			e.printStackTrace();
		}
		return userStoryList;
	}




/**
	 * Deze methode past een eerder gemaakte userStory aan en zet de vorige versie op nonactief.
	 * @author Jeroen Zandvliet
         * @throws Exception            kek
	 * @param userStoryID           lol
	 * @param userStoryName         lol
	 * @param sprintID              lol
	 * @param userStoryDescription  lol
	 */
	public void modifyUserStory(int userStoryID, String userStoryName, int sprintID, String userStoryDescription) throws Exception {
		String changePreviousVersion = "UPDATE userstory_version SET userstory_version_current = 'n' "
				+ "WHERE userstory_version_userstory_fk = ? AND userstory_version_current= true";

		String change_userStory = "INSERT INTO userstory_version(userstory_version_userstory_fk, userstory_version_name, " +
				"userstory_version_description, userstory_version_current) VALUES(?, ?, ?, true)";
		PreparedStatement changeVersions= this.connect.makeConnection().prepareStatement(changePreviousVersion);
		changeVersions.setInt(1, userStoryID);
		changeVersions.executeUpdate();
		changeVersions.close();

		PreparedStatement changeUserStory = this.connect.makeConnection().prepareStatement(change_userStory);
		changeUserStory.setInt(1, userStoryID);
		changeUserStory.setString(2, userStoryName);
		changeUserStory.setString(3, userStoryDescription);
		changeUserStory.executeQuery();
		changeUserStory.close();
	}

	public void removeUserStory(int userStoryID) throws Exception {
		String deleteUserStory = "UPDATE userStory SET userStory_isdeleted = true WHERE userStory_id = ?";
		PreparedStatement lockStatement = this.connect.makeConnection().prepareStatement(deleteUserStory);
		lockStatement.setInt(1, userStoryID);
		lockStatement.executeUpdate();
                lockStatement.close();
	}
}

