package nl.webedu.dao;

import nl.webedu.models.ProjectModel;
import nl.webedu.models.EntryModel;
import nl.webedu.models.EmployeeModel;
import nl.webedu.dao.ConnectDAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;

public class EntryDAO {
    private ConnectDAO connect;

    public EntryDAO(){
    	this.connect = new ConnectDAO();
	}

    /**
	 * Deze methode laat een lijst zien van entries die de status queued hebben.
	 * @author rezanaser
	 * @return entry_alist lijst van alle entry's
	 */
	public ArrayList<EntryModel> entry_all_list(){
		ArrayList<EntryModel> entry_alist = new ArrayList<EntryModel>();
		String employee_entry_sql = "SELECT  * from entry_version ";
				//+ "AND entry_version_current = 'y' ";
		try {
			PreparedStatement entries_statement = this.connect.makeConnection().prepareStatement(employee_entry_sql);
			
			ResultSet entry_set = entries_statement.executeQuery();
			while(entry_set.next()) {
				EntryModel dummy = new EntryModel();
				dummy.setEntryId(entry_set.getInt("entry_id"));
				dummy.setEntryStatus(entry_set.getString("entry_status"));
				entry_alist.add(dummy);
			}
			entries_statement.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return entry_alist;
	}
        
        /**
         * Geeft alle entries met hun data, inclusief deleted en oude versies.
         * 
         * @return entry_alist
         */
        public ArrayList<EntryModel> getEntriesFull(){
		ArrayList<EntryModel> entry_alist = new ArrayList<EntryModel>();
		String employee_entry_sql = "SELECT * FROM entry INNER JOIN entry_version ON entry_id=entry_version_entry_fk";
				//+ "AND entry_version_current = 'y' ";
		try {
			PreparedStatement entries_statement = this.connect.makeConnection().prepareStatement(employee_entry_sql);
			
			ResultSet entry_set = entries_statement.executeQuery();
			while(entry_set.next()) {
				EntryModel entry = new EntryModel();
				entry.setEntryId(entry_set.getInt("entry_id"));
                                entry.setEmployeeFk(entry_set.getInt("entry_employee_fk"));
                                entry.setEntryIsLocked(entry_set.getBoolean("entry_islocked"));
                                entry.setIsDeleted(entry_set.getBoolean("entry_isdeleted"));
                                entry.setEntryProjectFk(entry_set.getInt("entry_version_project_fk"));
                                entry.setEntrySprintFk(entry_set.getInt("entry_version_sprint_fk"));
                                entry.setEntryUserstoryFk(entry_set.getInt("entry_version_userstory_fk"));
				entry.setEntryStatus(entry_set.getString("entry_status"));
                                entry.setEntryStartTime(entry_set.getString("entry_version_starttime"));
                                entry.setEntryEndTime(entry_set.getString("entry_version_endtime"));
                                entry.setEntryDate(entry_set.getString("entry_version_date"));
                                entry.setIsCurrent(entry_set.getBoolean("entry_version_current"));
				entry_alist.add(entry);
			}
			entries_statement.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return entry_alist;
	}
        
        /**
	 * Deze methode krijgt de geselecteerde uur van de goedkeuren view en stuurt deze wijziging naar de database.
	 * @param id is de id van de entry.
	 * @author rezanaser
	 */
	public void approveHours(int id) throws Exception {
		String employee_entry_sql = "UPDATE entry SET entry_status = 'approved' WHERE entry_id = ? ";
		PreparedStatement entries_statement;
		entries_statement = this.connect.makeConnection().prepareStatement(employee_entry_sql);
		entries_statement.setInt(1, id);
		entries_statement.executeUpdate();
	}

	public void rejectHours(int id) {
		String employee_entry_sql = "UPDATE entry SET entry_status = 'rejected' WHERE entry_id = ? ";
		try {
			PreparedStatement entries_statement;
			entries_statement = this.connect.makeConnection().prepareStatement(employee_entry_sql);
			entries_statement.setInt(1, id);
			entries_statement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
        
    /**
	 * Deze methode laat een lijst zien van entries die de status queued hebben.
	 * @author rezanaser
	 * @param e_id dunno lol
	 * @return entry_alist lijst van alle entry's die in de wachtrij staan
	 */
	public ArrayList<EntryModel> entry_queued_list(int e_id){
		ArrayList<EntryModel> entry_alist = new ArrayList<EntryModel>();
		String employee_entry_sql = "SELECT * "
				+ " FROM entry_version, entry "
				+ "WHERE entry_version.entry_version_entry_fk = entry.entry_id AND entry.entry_status = 'queued' "
				+ "AND entry_version_current = true ";
		try {
			PreparedStatement entries_statement = this.connect.makeConnection().prepareStatement(employee_entry_sql);
			
			ResultSet entry_set = entries_statement.executeQuery();
			while(entry_set.next()) {
				EntryModel dummy = new EntryModel();
				dummy.setEntryId(entry_set.getInt("entry_id"));
				dummy.setEntryDescription(entry_set.getString("entry_version_description"));
				dummy.setEntryStartTime(entry_set.getString("entry_version_starttime"));
				dummy.setEntryEndTime(entry_set.getString("entry_version_endtime"));
				dummy.setEntryDate(entry_set.getString("entry_version_date"));
				dummy.setEntryProjectFk(entry_set.getInt("entry_version_project_fk"));
				dummy.setEntryIsLocked(entry_set.getBoolean("entry_islocked"));
				dummy.setEntrySprintFk(entry_set.getInt("entry_version_sprint_fk"));
				dummy.setEntryUserstoryFk(entry_set.getInt("entry_version_userstory_fk"));
				entry_alist.add(dummy);
			}
			entries_statement.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return entry_alist;
	}
	/**
	 * Deze methode voegt een nieuwe entry toe
	 *
	 * @param employeeId    employee id
	 * @param pId           project id
	 * @param spId          sprint id
	 * @param date          date
	 * @param description   entry description
	 * @param startTime     entry start time
	 * @param endTime       entry end time
	 * @param userId
         */
	public void addEntry(int employeeId, int pId, int spId, Date date, String description, Time startTime, Time endTime, int userId){
		PreparedStatement insertProject;
		String insertUser_sql = "insert into entry_version (entry_version_entry_fk, entry_version_project_fk,entry_version_sprint_fk, entry_version_description, entry_version_current, entry_version_date"
				+ ",entry_version_starttime, entry_version_endtime, entry_version_userstory_fk) "
				+ "VALUES (?, ?, ?, ?, ?, ?,?,?,?)";
		try {
			insertProject = this.connect.makeConnection().prepareStatement(insertUser_sql);
			
			insertProject.setInt(1, createNewEntry(employeeId));
			if(pId == 0)
			{
				insertProject.setNull(2, java.sql.Types.INTEGER);
			}
			else
			{
				insertProject.setInt(2, pId);
			}
			if(spId == 0)
			{
				insertProject.setNull(3, java.sql.Types.INTEGER);
			}
			else
			{
				insertProject.setInt(3, spId);
			}
			insertProject.setString(4, description);
			insertProject.setBoolean(5, true);
			insertProject.setDate(6, date);
			insertProject.setTime(7, startTime);
			insertProject.setTime(8, endTime);
			if(userId == 0)
			{
				insertProject.setNull(9, java.sql.Types.INTEGER);
			}
			else
			{
				insertProject.setInt(9, userId);
			}
			insertProject.executeQuery();
			insertProject.close();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}
    	/**
	 * De volgende voegt een nieuwe entry toe aan de entry tabel
	 * @author rezanaser
         * @param employeeId id van bijbehorende employee
         * @return id geeft id terug van aangemaakte entry
	 */
	public int createNewEntry(int employeeId) {
		int id = 0;
		PreparedStatement createEntry;
		ResultSet projectId = null;
		String insertEntry_sql = "insert into entry(entry_employee_fk, entry_status, entry_islocked, entry_isdeleted) values (?,'queued',false,false)";
		
		try {
			
			createEntry = this.connect.makeConnection().prepareStatement(insertEntry_sql, Statement.RETURN_GENERATED_KEYS);
			
			createEntry.setInt(1, employeeId);
			createEntry.executeUpdate();
			createEntry.getGeneratedKeys();
			projectId = createEntry.getGeneratedKeys();
			
			while (projectId.next()) {
				id = projectId.getInt(1);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return id;
	}
	
        /**
         * Deze methode wijzigt de geselcteerde entry
         * 
         * @param entryId       Entry ID
         * @param pId           lol
         * @param spId          lol
         * @param date          lol
         * @param description   lol
         * @param startTime     lol
         * @param endTime       lol
         * @param userId        lol
         */
	public void modifyEntry(int entryId, int pId, int spId, Date date, String description, Time startTime, Time endTime, int userId) {
		String changePreviousVersion = "UPDATE entry_version set entry_version_current = false "
				+ "WHERE entry_version_entry_fk = ? ";
		String changeEntry = "INSERT INTO entry_version(entry_version_entry_fk, entry_version_project_fk, entry_version_sprint_fk, "
				+ "entry_version_date, entry_version_description, entry_version_starttime, "
				+ "entry_version_endtime, entry_version_userstory_fk, entry_version_current)"
				+ "VALUES(?, ?, ?, ?, ?, ?, ?,?, true)";
		try {
			PreparedStatement changeVersions= this.connect.makeConnection().prepareStatement(changePreviousVersion);
			changeVersions.setInt(1, entryId);
			changeVersions.executeUpdate();
			changeVersions.close();
			PreparedStatement changeProject = this.connect.makeConnection().prepareStatement(changeEntry);
			changeProject.setInt(1, entryId);
			if(pId == 0)
			{
				changeProject.setNull(2, java.sql.Types.INTEGER);
			}
			else
			{
				changeProject.setInt(2, pId);
			}
			if(spId == 0)
			{
				changeProject.setNull(3, java.sql.Types.INTEGER);
			}
			else
			{
				changeProject.setInt(3, spId);
			}
			changeProject.setDate(4, date);
			changeProject.setString(5, description);
			changeProject.setTime(6, startTime);
			changeProject.setTime(7, endTime);
			if(userId == 0)
			{
				changeProject.setNull(8, java.sql.Types.INTEGER);
			}
			else
			{
				changeProject.setInt(8, userId);
			}
			changeProject.executeQuery();
			changeProject.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
    
        /**
	 * Return a list of entries belonging to user.
         * 
         * @param e_id entry employee
         * @return entry_alist lijst van entry's van een bepaalde employee
	 */
	public ArrayList<EntryModel> entry_list(int e_id){
		//Empty list to return
		ArrayList<EntryModel> entry_alist = new ArrayList<EntryModel>();
		
		String employee_entry_sql = "SELECT sprint_version_description, userstory_version_description, project_version_description, entry_id, entry_status, entry_version_description, entry_version_starttime, entry_version_endtime, entry_version_creationtime\r\n" + 
				"FROM entry_version \r\n" + 
				"INNER JOIN entry ON(entry_id = entry_version_entry_fk)\r\n" + 
				"INNER JOIN project_version ON(project_version_project_fk=entry_version_project_fk)\r\n" + 
				"INNER JOIN sprint_version ON(sprint_version_project_fk=project_version_project_fk)\r\n" + 
				"INNER JOIN userstory_version ON(userstory_version_project_fk=project_version_project_fk)\r\n" +
				"WHERE entry_employee_fk = ?";
		try {
			PreparedStatement entries_statement = this.connect.makeConnection().prepareStatement(employee_entry_sql);
			entries_statement.setInt(1, e_id);
			
			ResultSet entry_set = entries_statement.executeQuery();
			while(entry_set.next()) {
				EntryModel entry_container = new EntryModel();
				entry_container.setEntryProjectDescription(entry_set.getString("project_version_description"));
				entry_container.setEntrySprintDescription(entry_set.getString("sprint_version_description"));
				entry_container.setEntryUserStoryDescription(entry_set.getString("userstory_version_description"));
				entry_container.setEntryId(entry_set.getInt("entry_id"));
				entry_container.setEntryDescription(entry_set.getString("entry_version_description"));
				entry_container.setEntryStatus(entry_set.getString("entry_status"));
				entry_container.setEntryStartTime(entry_set.getString("entry_version_starttime"));
				entry_container.setEntryEndTime(entry_set.getString("entry_version_endtime"));
				entry_container.setEntryDate(entry_set.getString("entry_version_creationtime"));
				
				entry_alist.add(entry_container);
			}
			entries_statement.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return entry_alist;
	}
}
