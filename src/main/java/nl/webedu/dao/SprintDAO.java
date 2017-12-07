package nl.webedu.dao;

import nl.webedu.models.SprintModel;
import nl.webedu.models.ProjectModel;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SprintDAO {
    private ConnectDAO connect;

    public SprintDAO(){
    	this.connect = new ConnectDAO();
	}
       
	/**
	 * Deze methode vult de combobox met de sprints van het gevraagde project
	 * @param employeeId    Id van de employee voor wie je de projecten wilt hebben
	 * @author rezanaser
	 * @return sprint_alist
	 */
	public ArrayList<SprintModel> allSprintsEmployee(int employeeId) throws Exception {
		ArrayList<SprintModel> sprintList = new ArrayList<SprintModel>();

		String projectsSprintsSql = "SELECT sprint_version_sprint_fk, sprint_version_name, " +
				"sprint_version_description,sprint_version_startdate, sprint_version_enddate " +
				"FROM sprint_version, project_employee WHERE project_employee_employee_fk = ? AND " +
				"project_employee_project_fk = sprint_version_project_fk";

		PreparedStatement sprintsStatement = this.connect.makeConnection().prepareStatement(projectsSprintsSql);
		sprintsStatement.setInt(1, employeeId);
		ResultSet sprintsSets = sprintsStatement.executeQuery();

		while(sprintsSets.next()) {
            SprintModel sprintContainer = new SprintModel();
			sprintContainer.setSprintId(sprintsSets.getInt("sprint_version_sprint_fk"));
            sprintContainer.setSprintName(sprintsSets.getString("sprint_version_name"));
            sprintContainer.setSprintStartDate(sprintsSets.getString("sprint_version_startdate"));
            sprintContainer.setSprintEndDate(sprintsSets.getString("sprint_version_enddate"));

            sprintList.add(sprintContainer);
        }
		sprintsStatement.close();
		return sprintList;
  	}

	/**
	 * Deze methode vult de combobox met de sprints van het gevraagde project
	 * @author rezanaser
	 * @return sprint_alist lijst van sprints
	 */
	public ArrayList<SprintModel> allSprints() throws Exception {
		ArrayList<SprintModel> sprintList = new ArrayList<SprintModel>();
		String projectsSprintsSql = "SELECT *  FROM sprint_version";
				//+ "AND entry_version_current = 'y' ";
		PreparedStatement sprintsStatement = this.connect.makeConnection().prepareStatement(projectsSprintsSql);
		ResultSet sprintsSets = sprintsStatement.executeQuery();

		while(sprintsSets.next()) {
            SprintModel sprintContainer = new SprintModel();
			sprintContainer.setSprintId(sprintsSets.getInt("sprint_version_sprint_fk"));
            sprintContainer.setSprintName(sprintsSets.getString("sprint_version_name"));
            sprintContainer.setSprintStartDate(sprintsSets.getString("sprint_version_startdate"));
            sprintContainer.setSprintEndDate(sprintsSets.getString("sprint_version_enddate"));
			sprintContainer.setSprintEndDate(sprintsSets.getString("sprint_version_enddate"));
            sprintList.add(sprintContainer);
        }
		sprintsStatement.close();
		return sprintList;
	  }
	
	/**
	 * Deze methode vult de combobox met de sprints van het gevraagde project
	 * @author rezanaser
	 * @param p_id  id van project waar je de sprints voor wilt hebben.
	 * @return sprint_alist lisjt van sprints
	 */
	public ArrayList<SprintModel> sprintsProjects(int p_id){
		ArrayList<SprintModel> sprint_alist = new ArrayList<SprintModel>();
		String projectsSprintsSql = "SELECT *  FROM sprint_version sv INNER JOIN project_version pv " +
				"ON sv.sprint_version_project_fk=pv.project_version_project_fk INNER JOIN project p " +
				"ON p.project_id=pv.project_version_project_fk WHERE pv.project_version_project_fk= ?" +
				"AND sv.sprint_version_current=TRUE AND project_isdeleted=FALSE";
				//+ "AND entry_version_current = 'y' ";
		try {
			PreparedStatement sprintsStatement = this.connect.makeConnection().prepareStatement(projectsSprintsSql);
			sprintsStatement.setInt(1, p_id);
			ResultSet sprintsSets = sprintsStatement.executeQuery();
			while(sprintsSets.next()) {
				SprintModel sprint;
				sprint = new SprintModel();
				sprint.setSprintId(sprintsSets.getInt("sprint_version_sprint_fk"));
				sprint.setSprintName(sprintsSets.getString("sprint_version_name"));
				sprint.setSprintStartDate(sprintsSets.getString("sprint_version_startdate"));
				sprint.setSprintEndDate(sprintsSets.getString("sprint_version_enddate"));
				sprint.setSprintIsDeleted(sprintsSets.getBoolean("sprint_version_isdeleted"));
				sprint_alist.add(sprint);
			}
			sprintsStatement.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return sprint_alist;
	  }


	/**
	 * Deze methode laat alleen de current version zien van een sprint.
	 * @author Jeroen Zandvliet
	 * @return sprintList
	 */
	public ArrayList<SprintModel> sprintListVersion(){
		ArrayList<SprintModel> sprintList = new ArrayList<SprintModel>();
		String sprintListSQL = "SELECT * FROM sprint_version "
				+ "INNER JOIN sprint ON (sprint_id = sprint_version_sprint_fk)"
				+ "AND sprint_version_current = true "
				+ "ORDER BY sprint_version.sprint_version_sprint_fk ASC";
		try {
			PreparedStatement sprint_statement = this.connect.makeConnection().prepareStatement(sprintListSQL);
			ResultSet sprint_set = sprint_statement.executeQuery();
			while(sprint_set.next()) {
				SprintModel sprintModelContainer = new SprintModel();
				sprintModelContainer.setSprintId(sprint_set.getInt("sprint_id"));
				sprintModelContainer.setSprintDescription(sprint_set.getString("sprint_version_description"));
				sprintModelContainer.setSprintName(sprint_set.getString("sprint_version_name"));
				sprintModelContainer.setSprintIsDeleted(sprint_set.getBoolean("sprint_isdeleted"));
				sprintModelContainer.setSprintStartDate(sprint_set.getString("sprint_version_startdate"));
				sprintModelContainer.setSprintEndDate(sprint_set.getString("sprint_version_enddate"));
				sprintModelContainer.setProjectFK(sprint_set.getInt("sprint_version_project_fk"));
				sprintList.add(sprintModelContainer);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return sprintList;
	}


	/**
	 * Deze methode geeft een lijst van sprints die bij een project horen
	 * @param projectModel  project waar je de sprints voor wilt hebben.
	 * @return sprint_list  lijst van de sprints
	 */
	public ArrayList<SprintModel> sprintListProject(ProjectModel projectModel){
		ArrayList<SprintModel> sprint_list = new ArrayList<SprintModel>();
		String sprint_list_sql = "SELECT * FROM sprint_version "
				+ "INNER JOIN sprint ON (sprint.sprint_id = sprint_version.sprint_version_sprint_fk) "
				+ "WHERE sprint_version.sprint_version_project_fk="+projectModel.getProjectId()
				+ " ORDER BY sprint_version.sprint_version_name ASC";
		try {
			PreparedStatement sprint_statement = this.connect.makeConnection().prepareStatement(sprint_list_sql);
			ResultSet sprint_set = sprint_statement.executeQuery();
			while(sprint_set.next()) {
				SprintModel sprintModelContainer = new SprintModel();
				sprintModelContainer.setSprintId(sprint_set.getInt("sprint_id"));
				sprintModelContainer.setSprintDescription(sprint_set.getString("sprint_version_description"));
				sprintModelContainer.setSprintName(sprint_set.getString("sprint_version_name"));
				sprintModelContainer.setSprintIsDeleted(sprint_set.getBoolean("sprint_isdeleted"));
				sprint_list.add(sprintModelContainer);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sprint_list;
	}
	
	/**
	 * Deze methode geeft alleen de sprints terug van de meegegeven medewerker.
	 * @author Jeroen Zandvliet
	 * @param employeeID    employee
	 * @return sprintList
	 */
	public ArrayList<SprintModel> sprintListEmployee(int employeeID){
		ArrayList<SprintModel> sprint_list = new ArrayList<SprintModel>();
		String sprint_list_sql = "SELECT * FROM sprint_version";
		try {
			PreparedStatement sprint_statement = this.connect.makeConnection().prepareStatement(sprint_list_sql);
			sprint_statement.setInt(1, employeeID);
			ResultSet sprint_set = sprint_statement.executeQuery();
			
			while(sprint_set.next()) {
				SprintModel sprintModelContainer = new SprintModel();
				
				sprintModelContainer.setSprintId(sprint_set.getInt("sprint_version_project_fk"));
				sprintModelContainer.setSprintDescription(sprint_set.getString("sprint_version_description"));
				sprintModelContainer.setSprintName(sprint_set.getString("sprint_version_name"));
				sprintModelContainer.setSprintStartDate(sprint_set.getString("sprint_version_startdate"));
				sprintModelContainer.setSprintEndDate(sprint_set.getString("sprint_version_enddate"));
				sprint_list.add(sprintModelContainer);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sprint_list;
	}


	/**
	 * Deze methode geeft het gegenereerde ID van een sprint terug
	 * @author Jeroen Zandvliet
	 * @return generatedID
	 */
	public int createNewSprint() throws Exception {
		int generatedID = 0;
		PreparedStatement createSprint;
		ResultSet sprintID = null;
		String insertSprintStatement = "INSERT INTO sprint(sprint_isdeleted) VALUES(?)";
		createSprint = this.connect.makeConnection().prepareStatement(insertSprintStatement, Statement.RETURN_GENERATED_KEYS);
		createSprint.setBoolean(1, false);
		createSprint.executeUpdate();
		sprintID = createSprint.getGeneratedKeys();
		while(sprintID.next()) {
            generatedID = sprintID.getInt(1);
        }
		return generatedID;
	}


	/**
	 * Deze methode voegt een gekozen sprint aan de database toe.
	 * @author Jeroen Zandvliet
	 * @param sprintName        lol
	 * @param projectID         lol
	 * @param sprintDescription lol
	 * @param sprintStartDate   lol
	 * @param sprintEndDate     lol
	 */
	public void addSprintToDatabase(int projectID, String sprintName, String sprintDescription, Date sprintStartDate, Date sprintEndDate)
	{
		PreparedStatement addSprint;
		String insertStatement = "INSERT INTO sprint_version(sprint_version_sprint_fk, sprint_version_project_fk, " +
				"sprint_version_name, sprint_version_description, sprint_version_startdate, sprint_version_enddate, " +
				"sprint_version_current) VALUES(?,?,?,?,?,?, true)";
		
		try {
			addSprint = this.connect.makeConnection().prepareStatement(insertStatement);
			
			addSprint.setInt(1, createNewSprint());
			addSprint.setInt(2,  projectID);
			addSprint.setString(3, sprintName);
			addSprint.setString(4, sprintDescription);
			addSprint.setDate(5, sprintStartDate);
			addSprint.setDate(6, sprintEndDate);
			
			addSprint.executeQuery();
			addSprint.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	


	public ArrayList <SprintModel> toonUserSprint (int e_id){
		ArrayList<SprintModel> sprintList = new ArrayList <SprintModel>();
		String sprintQuery = "SELECT * FROM sprint_version, sprint "
				+ "WHERE sprint_version_sprint_fk = sprint_id";
		
		try {
			PreparedStatement sprintStatement = this.connect.makeConnection().prepareStatement(sprintQuery);
			ResultSet sprint_set = sprintStatement.executeQuery();
			while(sprint_set.next()){
				SprintModel model = new SprintModel();
				model.setSprintName(sprint_set.getString("sprint_version_name"));
				model.setSprintStartDate(sprint_set.getString("sprint_version_startdate"));
				model.setSprintEndDate(sprint_set.getString("sprint_version_enddate"));
				sprintList.add(model);
			}
			sprintStatement.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sprintList;
	}


/**
	 * Deze methode past een eerder gemaakte sprint aan en zet de vorige versie op nonactief.
	 * @author Jeroen Zandvliet
	 * @param sprintID          lol
	 * @param sprintName        lol
	 * @param projectID         lol
	 * @param sprintDescription lol
	 * @param sprintStartDate   lol
	 * @param sprintEndDate     lol
	 */
	
	public void modifySprint(int sprintID, String sprintName, int projectID, String sprintDescription, Date sprintStartDate, Date sprintEndDate) {
		String changePreviousVersion = "UPDATE sprint_version SET sprint_version_current = 'n' "
				+ "WHERE sprint_version_sprint_fk = ? AND sprint_version_current= true";
		String change_sprint = "INSERT INTO sprint_version(sprint_version_sprint_fk, sprint_version_name, sprint_version_project_fk, sprint_version_description, sprint_version_startdate, sprint_version_enddate, sprint_version_current)"
				+ "VALUES(?, ?, ?, ?, ?, ?, true)";
		
		
		try {
			PreparedStatement changeVersions= this.connect.makeConnection().prepareStatement(changePreviousVersion);
			changeVersions.setInt(1, sprintID);
			changeVersions.executeUpdate();
			changeVersions.close();
			PreparedStatement changeSprint = this.connect.makeConnection().prepareStatement(change_sprint);
			changeSprint.setInt(1, sprintID);
			changeSprint.setString(2, sprintName);
			changeSprint.setInt(3, projectID);
			changeSprint.setString(4, sprintDescription);
			changeSprint.setDate(5, sprintStartDate);
			changeSprint.setDate(6, sprintEndDate);
			changeSprint.executeQuery();
			
			changeSprint.close();
		} catch (Exception e) {
			e.getMessage();
		}
	}
	

	public void removeSprint(int sprintID) throws Exception {
		String deleteSprint = "UPDATE sprint SET sprint_isdeleted = true WHERE sprint_id = ?";
		PreparedStatement lockStatement = this.connect.makeConnection().prepareStatement(deleteSprint);
		lockStatement.setInt(1, sprintID);
		lockStatement.executeUpdate();
	}
}
