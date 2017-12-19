package nl.webedu.dao;

import nl.webedu.models.ProjectModel;
import nl.webedu.models.SprintModel;

import java.sql.*;
import java.util.ArrayList;
import nl.webedu.helpers.DateHelper;

public class SprintDAO {
    private ConnectDAO connect;
       DateHelper dateHelper = new DateHelper();
    public SprintDAO(){
    	this.connect = new ConnectDAO();
        this.createAddSprintFunction();
    }
    
    /**
     * Maakt een procedure die sprints toe kan voegen.
     * 
     * @author Robert den Blaauwen
     */
    public void createAddSprintFunction(){
	String sprintProcedureSql = "CREATE OR REPLACE FUNCTION add_sprint(project_id INT4, sprint_name TEXT, description TEXT, startdate DATE, enddate DATE) " +
                                    "RETURNS void AS $$ " +
                                    "DECLARE pk INT; " +
                                    "BEGIN " +
                                    "	INSERT INTO sprint(sprint_isdeleted) VALUES(false) " +
                                    "	RETURNING sprint_id INTO pk; " +
                                    "	INSERT INTO sprint_version(sprint_version_sprint_fk, sprint_version_project_fk," +
                                    "    	sprint_version_name, sprint_version_description, sprint_version_startdate, sprint_version_enddate," +
                                    "        sprint_version_current) " +
                                    "    VALUES(pk,project_id,sprint_name,description,startdate,enddate, true); " +
                                    "END $$ LANGUAGE plpgsql;";
	try {
            PreparedStatement sprintProcedureStatement = this.connect.makeConnection().prepareStatement(sprintProcedureSql);
            sprintProcedureStatement.executeUpdate();
            sprintProcedureStatement.close();
            //System.out.println(this.getClass().toString()+": constructor: FUNCTION add_project(name, description, customer) has been created!");
	} catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
	} catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
	}
    }
    
    public SprintModel getSprint(int sprintId) throws Exception{
            String sprintSql = "SELECT * FROM sprint INNER JOIN sprint_version "
                                + "ON sprint_id=sprint_version_sprint_fk "
                                + "WHERE sprint_id=? AND sprint_version_current=true;";
            PreparedStatement sprintStatement = this.connect.makeConnection().prepareStatement(sprintSql);
            sprintStatement.setInt(1,sprintId);
            ResultSet sprintSet = sprintStatement.executeQuery();
            
            sprintSet.next();
            SprintModel sprint = new SprintModel();
            sprint.setSprintId(sprintSet.getInt("sprint_id"));
            sprint.setSprintName(sprintSet.getString("sprint_version_name"));
            sprint.setSprintDescription(sprintSet.getString("sprint_version_description"));
            sprint.setIsDeleted(sprintSet.getBoolean("sprint_isdeleted"));
            sprint.setIsCurrent(true);
            sprint.setProjectFK(sprintSet.getInt("sprint_version_project_fk"));
            sprintStatement.close();
            return sprint;
        }
       
	/**
	 * Deze methode vult de combobox met de sprints van het gevraagde project
	 * @param employeeId    Id van de employee voor wie je de projecten wilt hebben
	 * @author rezanaser
	 * @return sprint_alist
         * @throws Exception SQL exception en normale exception
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
         * @throws Exception SQL exception en normale exception
	 */
	public ArrayList<SprintModel> allSprints() throws Exception {
            ArrayList<SprintModel> sprintList = new ArrayList<SprintModel>();
            String projectsSprintsSql = "SELECT *  FROM sprint_version INNER JOIN sprint ON sprint_id=sprint_version_sprint_fk";
            //+ "AND entry_version_current = 'y' ";
            PreparedStatement sprintsStatement = this.connect.makeConnection().prepareStatement(projectsSprintsSql);
            ResultSet sprintsSets = sprintsStatement.executeQuery();

            while(sprintsSets.next()) {
                SprintModel sprintContainer = new SprintModel();
                sprintContainer.setSprintId(sprintsSets.getInt("sprint_version_sprint_fk"));
                sprintContainer.setSprintIsDeleted(sprintsSets.getBoolean("sprint_isdeleted"));
                sprintContainer.setProjectFK(sprintsSets.getInt("sprint_version_project_fk"));
                sprintContainer.setSprintName(sprintsSets.getString("sprint_version_name"));
                sprintContainer.setSprintDescription(sprintsSets.getString("sprint_version_description"));
                sprintContainer.setSprintStartDate(sprintsSets.getString("sprint_version_startdate"));
                sprintContainer.setSprintEndDate(sprintsSets.getString("sprint_version_enddate"));
                sprintContainer.setSprintEndDate(sprintsSets.getString("sprint_version_enddate"));
                sprintContainer.setIsCurrent(sprintsSets.getBoolean("sprint_version_current"));
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
		String projectsSprintsSql = "SELECT *  FROM sprint_version sv INNER JOIN sprint s ON s.sprint_id=sv.sprint_version_sprint_fk " +
                                "INNER JOIN project_version pv " +
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
                                sprint.setSprintDescription(sprintsSets.getString("sprint_version_description"));
				sprint.setSprintStartDate(sprintsSets.getString("sprint_version_startdate"));
				sprint.setSprintEndDate(sprintsSets.getString("sprint_version_enddate"));
				sprint.setSprintIsDeleted(sprintsSets.getBoolean("sprint_isdeleted"));
                                sprint.setIsCurrent(sprintsSets.getBoolean("sprint_version_current"));
                                sprint.setProjectFK(sprintsSets.getInt("sprint_version_project_fk"));
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
                        sprint_statement.close();
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
         * @throws Exception SQL exception en normale exception
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

	public void createSprint(SprintModel sprintModel){
		PreparedStatement addSprint;
		String insertStatement = "SELECT add_sprint(?,?,?,?,?)";
		
		try {
			addSprint = this.connect.makeConnection().prepareStatement(insertStatement);
			
                        Date startDateParsed = dateHelper.parseDate(sprintModel.getSprintStartDate(), "yyyy-MM-dd");
                        Date endDateParsed = dateHelper.parseDate(sprintModel.getSprintEndDate(), "yyyy-MM-dd");
			addSprint.setInt(1,  sprintModel.getProjectFK());
			addSprint.setString(2, sprintModel.getSprintName());
			addSprint.setString(3, sprintModel.getSprintDescription());
			addSprint.setDate(4, startDateParsed);
			addSprint.setDate(5, endDateParsed);
			
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
        public void unRemoveSprint(int sprintID) throws Exception {
		String deleteSprint = "UPDATE sprint SET sprint_isdeleted = false WHERE sprint_id = ?";
		PreparedStatement lockStatement = this.connect.makeConnection().prepareStatement(deleteSprint);
		lockStatement.setInt(1, sprintID);
		lockStatement.executeUpdate();
	}
}
