package nl.webedu.dao;

import nl.webedu.models.ProjectModel;
import nl.webedu.models.CategoryModel;

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
    
    public CategoryModel getSprint(int sprintId) throws Exception{
            String sprintSql = "SELECT * FROM sprint INNER JOIN sprint_version "
                                + "ON sprint_id=sprint_version_sprint_fk "
                                + "WHERE sprint_id=? AND sprint_version_current=true;";
            
            Connection connection = this.connect.makeConnection();
            PreparedStatement sprintStatement = connection.prepareStatement(sprintSql);
            sprintStatement.setInt(1,sprintId);
            ResultSet sprintSet = sprintStatement.executeQuery();
            
            sprintSet.next();
            CategoryModel sprint = new CategoryModel();
            sprint.setCategoryId(sprintSet.getInt("sprint_id"));
            sprint.setCategoryName(sprintSet.getString("sprint_version_name"));
            sprint.setCategoryDescription(sprintSet.getString("sprint_version_description"));
            sprint.setIsDeleted(sprintSet.getBoolean("sprint_isdeleted"));
            sprint.setIsCurrent(true);
            sprint.setProjectFK(sprintSet.getInt("sprint_version_project_fk"));
            
            //close alles zodat de connection pool niet op gaat.
            sprintSet.close();
            sprintStatement.close();
            connection.close();
            
            return sprint;
        }
       
	/**
	 * Deze methode vult de combobox met de sprints van het gevraagde project
	 * @param employeeId    Id van de employee voor wie je de projecten wilt hebben
	 * @author rezanaser
	 * @return sprint_alist
         * @throws Exception SQL exception en normale exception
	 */
	public ArrayList<CategoryModel> allSprintsEmployee(int employeeId) throws Exception {
		ArrayList<CategoryModel> sprintList = new ArrayList<CategoryModel>();

		String projectsSprintsSql = "SELECT sprint_version_sprint_fk, sprint_version_name, " +
				"sprint_version_description,sprint_version_startdate, sprint_version_enddate " +
				"FROM sprint_version, project_employee WHERE project_employee_employee_fk = ? AND " +
				"project_employee_project_fk = sprint_version_project_fk";
                
                Connection connection = this.connect.makeConnection();
		PreparedStatement sprintsStatement = connection.prepareStatement(projectsSprintsSql);
		sprintsStatement.setInt(1, employeeId);
		ResultSet sprintsSets = sprintsStatement.executeQuery();

		while(sprintsSets.next()) {
                    CategoryModel sprintContainer = new CategoryModel();
                                sprintContainer.setCategoryId(sprintsSets.getInt("sprint_version_sprint_fk"));
                    sprintContainer.setCategoryName(sprintsSets.getString("sprint_version_name"));
                    sprintContainer.setCategoryStartDate(sprintsSets.getString("sprint_version_startdate"));
                    sprintContainer.setCategoryEndDate(sprintsSets.getString("sprint_version_enddate"));

                    sprintList.add(sprintContainer);
                }
                
                // close alles
                sprintsSets.close();
		sprintsStatement.close();
                connection.close();
		return sprintList;
  	}

	/**
	 * Deze methode vult de combobox met de sprints van het gevraagde project
	 * @author rezanaser
	 * @return sprint_alist lijst van sprints
         * @throws Exception SQL exception en normale exception
	 */
	public ArrayList<CategoryModel> allSprints() throws Exception {
            ArrayList<CategoryModel> sprintList = new ArrayList<CategoryModel>();
            String projectsSprintsSql = "SELECT *  FROM sprint_version INNER JOIN sprint ON sprint_id=sprint_version_sprint_fk "
                                        + "INNER JOIN project_version ON sprint_version_project_fk = project_version_project_fk WHERE sprint_version_current = true AND project_version_current = true ";
            //+ "AND entry_version_current = 'y' ";
            
            Connection connection = this.connect.makeConnection();
            PreparedStatement sprintsStatement = connection.prepareStatement(projectsSprintsSql);
            ResultSet sprintsSets = sprintsStatement.executeQuery();

            while(sprintsSets.next()) {
                CategoryModel sprintContainer = new CategoryModel();
                sprintContainer.setCategoryId(sprintsSets.getInt("sprint_version_sprint_fk"));
                sprintContainer.setCategoryIsDeleted(sprintsSets.getBoolean("sprint_isdeleted"));
                sprintContainer.setProjectFK(sprintsSets.getInt("sprint_version_project_fk"));
                sprintContainer.setCategoryName(sprintsSets.getString("sprint_version_name"));
                sprintContainer.setCategoryDescription(sprintsSets.getString("sprint_version_description"));
                sprintContainer.setCategoryStartDate(sprintsSets.getString("sprint_version_startdate"));
                sprintContainer.setCategoryEndDate(sprintsSets.getString("sprint_version_enddate"));
                sprintContainer.setCategoryEndDate(sprintsSets.getString("sprint_version_enddate"));
                sprintContainer.setIsCurrent(sprintsSets.getBoolean("sprint_version_current"));
                sprintContainer.setProjectName(sprintsSets.getString("project_version_name"));
                sprintList.add(sprintContainer);
            }
            //close alles
            sprintsSets.close();
            sprintsStatement.close();
            connection.close();
            return sprintList;
	}

	/**
	 * Deze methode laat alleen de current version zien van een sprint.
	 * @author Jeroen Zandvliet
	 * @return sprintList
	 */
	public ArrayList<CategoryModel> sprintListVersion(){
		ArrayList<CategoryModel> sprintList = new ArrayList<>();
		String sprintListSQL = "SELECT * FROM sprint_version "
				+ "INNER JOIN sprint ON (sprint_id = sprint_version_sprint_fk"
                                + "INNER JOIN project_version ON sprint_version_project_fk = project_version_project_fk"
				+ "AND sprint_version_current = true "
				+ "ORDER BY sprint_version.sprint_version_sprint_fk ASC";
		try {
                    Connection connection = this.connect.makeConnection();
			PreparedStatement sprint_statement = connection.prepareStatement(sprintListSQL);
			ResultSet sprint_set = sprint_statement.executeQuery();
			while(sprint_set.next()) {
				CategoryModel sprintModelContainer = new CategoryModel();
				sprintModelContainer.setCategoryId(sprint_set.getInt("sprint_id"));
				sprintModelContainer.setCategoryDescription(sprint_set.getString("sprint_version_description"));
				sprintModelContainer.setCategoryName(sprint_set.getString("sprint_version_name"));
				sprintModelContainer.setCategoryIsDeleted(sprint_set.getBoolean("sprint_isdeleted"));
				sprintModelContainer.setCategoryStartDate(sprint_set.getString("sprint_version_startdate"));
				sprintModelContainer.setCategoryEndDate(sprint_set.getString("sprint_version_enddate"));
				sprintModelContainer.setProjectFK(sprint_set.getInt("sprint_version_project_fk"));
                                sprintModelContainer.setProjectName(sprint_set.getString("project_version_name"));
				sprintList.add(sprintModelContainer);
			}
                        // close alles zodat de connection pool niet op gaat
                        sprint_set.close();
			sprint_statement.close();
                        connection.close();
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
	public ArrayList<CategoryModel> sprintListProject(ProjectModel projectModel){
		ArrayList<CategoryModel> sprint_list = new ArrayList<CategoryModel>();
		String sprint_list_sql = "SELECT * FROM sprint_version "
				+ "INNER JOIN sprint ON (sprint.sprint_id = sprint_version.sprint_version_sprint_fk) "
				+ "WHERE sprint_version.sprint_version_project_fk="+projectModel.getProjectId()
				+ " ORDER BY sprint_version.sprint_version_name ASC";
		try {
                    Connection connection = this.connect.makeConnection();
			PreparedStatement sprint_statement = connection.prepareStatement(sprint_list_sql);
			ResultSet sprint_set = sprint_statement.executeQuery();
			while(sprint_set.next()) {
				CategoryModel sprintModelContainer = new CategoryModel();
				sprintModelContainer.setCategoryId(sprint_set.getInt("sprint_id"));
				sprintModelContainer.setCategoryDescription(sprint_set.getString("sprint_version_description"));
				sprintModelContainer.setCategoryName(sprint_set.getString("sprint_version_name"));
				sprintModelContainer.setCategoryIsDeleted(sprint_set.getBoolean("sprint_isdeleted"));
				sprint_list.add(sprintModelContainer);
			}
                        // close alles zodat de connection pool niet op gaat
                        sprint_set.close();
			sprint_statement.close();
                        connection.close();
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
	public ArrayList<CategoryModel> sprintListEmployee(int employeeID){
		ArrayList<CategoryModel> sprint_list = new ArrayList<CategoryModel>();
		String sprint_list_sql = "SELECT * FROM sprint_version";
		try {
                    Connection connection = this.connect.makeConnection();
			PreparedStatement sprint_statement = connection.prepareStatement(sprint_list_sql);
			sprint_statement.setInt(1, employeeID);
			ResultSet sprint_set = sprint_statement.executeQuery();
			
			while(sprint_set.next()) {
				CategoryModel sprintModelContainer = new CategoryModel();
				
				sprintModelContainer.setCategoryId(sprint_set.getInt("sprint_version_sprint_fk"));
				sprintModelContainer.setCategoryDescription(sprint_set.getString("sprint_version_description"));
				sprintModelContainer.setCategoryName(sprint_set.getString("sprint_version_name"));
				sprintModelContainer.setCategoryStartDate(sprint_set.getString("sprint_version_startdate"));
				sprintModelContainer.setCategoryEndDate(sprint_set.getString("sprint_version_enddate"));
				sprint_list.add(sprintModelContainer);
			}
                        // close alles zodat de connection pool niet op gaat
                        sprint_set.close();
			sprint_statement.close();
                        connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sprint_list;
	}
        
        public CategoryModel sprintListTask(int TaskId){
		CategoryModel sprint = new CategoryModel();
		String sprint_list_sql = "SELECT * FROM sprint_version " +
                                        "JOIN sprint ON sprint_id=sprint_version_sprint_fk " +
                                        "JOIN userstory_sprint ON userstory_sprint_sprint_fk=sprint_id " +
                                        "WHERE userstory_sprint_userstory_fk= ? AND sprint_version_current=true " +
                                        "AND sprint_isdeleted=false;";
		try {
                    Connection connection = this.connect.makeConnection();
			PreparedStatement sprint_statement = connection.prepareStatement(sprint_list_sql);
			sprint_statement.setInt(1, TaskId);
			ResultSet sprint_set = sprint_statement.executeQuery();
			
			while(sprint_set.next()) {
				CategoryModel sprintModelContainer = new CategoryModel();
                                sprintModelContainer.setProjectFK(sprint_set.getInt("sprint_version_project_fk"));
				sprintModelContainer.setCategoryId(sprint_set.getInt("sprint_version_sprint_fk"));
				sprintModelContainer.setCategoryDescription(sprint_set.getString("sprint_version_description"));
				sprintModelContainer.setCategoryName(sprint_set.getString("sprint_version_name"));
				sprintModelContainer.setCategoryStartDate(sprint_set.getString("sprint_version_startdate"));
				sprintModelContainer.setCategoryEndDate(sprint_set.getString("sprint_version_enddate"));
				sprint=sprintModelContainer;
			}
                        // close alles zodat de connection pool niet op gaat
                        sprint_set.close();
			sprint_statement.close();
                        connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sprint;
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
                Connection connection = this.connect.makeConnection();
		createSprint = connection.prepareStatement(insertSprintStatement, Statement.RETURN_GENERATED_KEYS);
		createSprint.setBoolean(1, false);
		createSprint.executeUpdate();
		sprintID = createSprint.getGeneratedKeys();
		while(sprintID.next()) {
                    generatedID = sprintID.getInt(1);
                }
                // close alles zodat de connection pool niet op gaat
                sprintID.close();
		createSprint.close();
                connection.close();
		return generatedID;
	}

	public boolean createSprint(CategoryModel sprintModel){
		PreparedStatement addSprint;
		String insertStatement = "SELECT add_sprint(?,?,?,?,?)";
		
		try {
                    Connection connection = this.connect.makeConnection();
			addSprint = connection.prepareStatement(insertStatement);
			
                        Date startDateParsed = dateHelper.parseDate(sprintModel.getCategoryStartDate(), "yyyy-MM-dd");
                        Date endDateParsed = dateHelper.parseDate(sprintModel.getCategoryEndDate(), "yyyy-MM-dd");
			addSprint.setInt(1,  sprintModel.getProjectFK());
			addSprint.setString(2, sprintModel.getCategoryName());
			addSprint.setString(3, sprintModel.getCategoryDescription());
			addSprint.setDate(4, startDateParsed);
			addSprint.setDate(5, endDateParsed);
			
			addSprint.executeQuery();
			// close alles zodat de connection pool niet op gaat
			addSprint.close();
                        connection.close();
                        return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
                return false;
	}
	
	


	public ArrayList <CategoryModel> toonUserSprint (int e_id){
		ArrayList<CategoryModel> sprintList = new ArrayList <CategoryModel>();
		String sprintQuery = "SELECT * FROM sprint_version, sprint "
				+ "WHERE sprint_version_sprint_fk = sprint_id";
		
		try {
                    Connection connection = this.connect.makeConnection();
			PreparedStatement sprintStatement = connection.prepareStatement(sprintQuery);
			ResultSet sprint_set = sprintStatement.executeQuery();
			while(sprint_set.next()){
				CategoryModel model = new CategoryModel();
				model.setCategoryName(sprint_set.getString("sprint_version_name"));
				model.setCategoryStartDate(sprint_set.getString("sprint_version_startdate"));
				model.setCategoryEndDate(sprint_set.getString("sprint_version_enddate"));
				sprintList.add(model);
			}
			// close alles zodat de connection pool niet op gaat
                        sprint_set.close();
                        sprintStatement.close();
                        connection.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sprintList;
	}


/**
	 * Deze methode past een eerder gemaakte sprint aan en zet de vorige versie op nonactief.
	 * @author Jeroen Zandvliet
	 * @param categoryModel          lol
	 * @param startDate        lol
	 * @param endDate         lol
         * @return boolean
	 */
	
	public boolean modifySprint(CategoryModel categoryModel, Date startDate, Date endDate) {
		String changePreviousVersion = "UPDATE sprint_version SET sprint_version_current = false WHERE sprint_version_sprint_fk = ?";
		String change_sprint = "INSERT INTO sprint_version(sprint_version_sprint_fk, sprint_version_name, sprint_version_project_fk, sprint_version_description, sprint_version_startdate, sprint_version_enddate, sprint_version_current)"
				+ "VALUES(?, ?, ?, ?, ?, ?, true)";
		
		
		try {
                    Connection connection = this.connect.makeConnection();
			PreparedStatement changeVersions= connection.prepareStatement(changePreviousVersion);
			changeVersions.setInt(1, categoryModel.getCategoryId());
			changeVersions.executeUpdate();
			// close alles zodat de connection pool niet op gaat
                        changeVersions.close();
                        
			PreparedStatement changeSprint = this.connect.makeConnection().prepareStatement(change_sprint);
			changeSprint.setInt(1, categoryModel.getCategoryId());
			changeSprint.setString(2, categoryModel.getCategoryName());
			changeSprint.setInt(3, categoryModel.getProjectFK());
			changeSprint.setString(4, categoryModel.getCategoryDescription());
			changeSprint.setDate(5, startDate);
			changeSprint.setDate(6, endDate);
			changeSprint.executeQuery();
			
			// close alles zodat de connection pool niet op gaat
                        changeSprint.close();
                        connection.close();
                        return true;
		} catch (Exception e) {
			e.getMessage();
		}
                return false;
	}
	

	public boolean removeSprint(int categoryId) throws Exception {
		String deleteSprintVersion = "UPDATE sprint_version SET sprint_version_current = false WHERE sprint_version_sprint_fk = ?";
                String deleteSprint = "UPDATE sprint SET sprint_isdeleted = true WHERE sprint_id = ? ";
                Connection connection = this.connect.makeConnection();
		PreparedStatement lockStatement = connection.prepareStatement(deleteSprintVersion);
		lockStatement.setInt(1, categoryId);
		lockStatement.executeUpdate();
                
                PreparedStatement removeSprint = connection.prepareStatement(deleteSprint);
		removeSprint.setInt(1, categoryId);
		removeSprint.executeUpdate();
                removeSprint.close();
                // close alles zodat de connection pool niet op gaat
                lockStatement.close();
                connection.close();
                return true;
	}
        public void unRemoveSprint(int sprintID) throws Exception {
		String deleteSprint = "UPDATE sprint SET sprint_isdeleted = false WHERE sprint_id = ?";
                Connection connection = this.connect.makeConnection();
		PreparedStatement lockStatement = connection.prepareStatement(deleteSprint);
		lockStatement.setInt(1, sprintID);
		lockStatement.executeUpdate();
                // close alles zodat de connection pool niet op gaat
                lockStatement.close();
                connection.close();
	}
}
