package nl.webedu.dao;

import nl.webedu.models.EntryModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.webedu.helpers.DateHelper;

public class EntryDAO {
    private ConnectDAO connect;
    
    public EntryDAO(){
    	this.connect = new ConnectDAO();
        this.createAddEntryProcedure();
    }
    
    private ArrayList<EntryModel> fillModels(ResultSet entrySet){
        DateHelper dateHelper = new DateHelper();
        ArrayList<EntryModel> entries = new ArrayList<EntryModel>();
        try{
        while(entrySet.next()) {
				EntryModel entry = new EntryModel();
				entry.setEntryId(entrySet.getInt("entry_id"));
                                
                                entry.setEmployeeFk(entrySet.getInt("entry_employee_fk"));
                                System.out.println(this.getClass()+": "+entry.getEmployeeFk());
                                entry.setEntryIsLocked(entrySet.getBoolean("entry_islocked"));
                                entry.setIsDeleted(entrySet.getBoolean("entry_isdeleted"));
                                
                                entry.setEntryProjectFk(entrySet.getInt("entry_version_project_fk"));
                                entry.setEntrySprintFk(entrySet.getInt("entry_version_sprint_fk"));
                                entry.setEntryUserstoryFk(entrySet.getInt("entry_version_userstory_fk"));
                                
                                entry.setEntryDescription(entrySet.getString("entry_version_description"));
				entry.setEntryStatus(entrySet.getString("entry_status"));
                                entry.setEntryStartTime(entrySet.getString("entry_version_starttime"));
                                entry.setEntryEndTime(entrySet.getString("entry_version_endtime"));
                                entry.setEntryDate(entrySet.getDate("entry_version_date"));
                                entry.setIsCurrent(entrySet.getBoolean("entry_version_current"));
                                entry.setEntryEmployeeName(entrySet.getString("employee_version_firstname") + " " 
                                                            + entrySet.getString("employee_version_lastname"));
				entries.add(entry);
			}
        }catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return entries;
    }
    
    public void createAddEntryProcedure(){
        String project_list_sql = "CREATE OR REPLACE FUNCTION add_entry(empid INT4, projid INT4, sprintid INT4, description text, datum date, starttime time, endtime time, userstoryid INT4)\n" +
            "RETURNS void AS $$ " +
            "DECLARE pk INT; " +
            "BEGIN " +
            "   	insert into entry(entry_employee_fk, entry_status, entry_islocked, entry_isdeleted) values (empid,'queued',false,false) " +
            "	RETURNING entry_id INTO pk; " +
            "	insert into entry_version (entry_version_entry_fk, entry_version_project_fk,entry_version_sprint_fk, entry_version_description, entry_version_current, " +
            "                               entry_version_date,entry_version_starttime, entry_version_endtime, entry_version_userstory_fk)" +
            "	VALUES (pk, projid, sprintid, description, true, datum,starttime,endtime,userstoryid); " +
            "END $$ LANGUAGE plpgsql;";
	try {
            PreparedStatement project_statement = this.connect.makeConnection().prepareStatement(project_list_sql);
            project_statement.executeUpdate();
            project_statement.close();
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
		String employee_entry_sql = "SELECT * FROM entry INNER JOIN entry_version ON entry_id=entry_version_entry_fk " +
                                            "INNER JOIN employee ON employee_id=entry_employee_fk " +
                                            "INNER JOIN employee_version ON employee_id=employee_version_employee_fk " +
                                            "WHERE employee_version_current=true;";
//                                            + "WHERE entry_version_current=true AND entry_isdeleted=false;";
		try {
			Connection connection = this.connect.makeConnection();
			PreparedStatement entries_statement = connection.prepareStatement(employee_entry_sql);
			
			ResultSet entry_set = entries_statement.executeQuery();
                        entry_alist = fillModels(entry_set);
			entry_set.close();
			entries_statement.close();
			connection.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return entry_alist;
	}

	/**
	 * Geeft alle entriees binnen een bepaalde tijdsperiode.
	 *
	 * @author Robert
	 * @param beginDate meest vroege teogestane datum
	 * @param endDate	meest late toegestane datum
	 * @return entry_alist
	 */
	public ArrayList<EntryModel> getEntriesWithinTimePeriod(Date beginDate, Date endDate){
		System.out.println(this.getClass().toString()+": getEntriesWithintTimePeriod BEGIN");
		ArrayList<EntryModel> entry_alist = new ArrayList<EntryModel>();
		String employee_entry_sql = "SELECT * FROM entry \n" +
									"INNER JOIN entry_version ON entry_id=entry_version_entry_fk " +
									"INNER JOIN employee ON employee_id=entry_employee_fk " +
									"INNER JOIN employee_version ON employee_id=employee_version_employee_fk " +
									"WHERE employee_version_current=true AND entry_version_date >= ? AND entry_version_date <= ? " +
									"ORDER BY entry_version_date, entry_version_starttime ASC;";
//                                            + "WHERE entry_version_current=true AND entry_isdeleted=false;";
		try {
			Connection connection = this.connect.makeConnection();
			PreparedStatement entries_statement = connection.prepareStatement(employee_entry_sql);
			entries_statement.setDate(1,beginDate);
			entries_statement.setDate(2,endDate);
			ResultSet entry_set = entries_statement.executeQuery();
			System.out.println(this.getClass().toString()+": getEntriesWithintTimePeriod EXECUTED");
			entry_alist = fillModels(entry_set);
			entry_set.close();
			entries_statement.close();
			connection.close();
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
         * @throws Exception SQL exception en normale exception
	 */
	public void approveHours(int id) throws Exception {
		String employee_entry_sql = "UPDATE entry SET entry_status = 'approved' WHERE entry_id = ? ";
		PreparedStatement entries_statement;
		Connection connection = this.connect.makeConnection();
		entries_statement = connection.prepareStatement(employee_entry_sql);
		entries_statement.setInt(1, id);
		entries_statement.executeUpdate();
                entries_statement.close();
		connection.close();
	}

	public void rejectHours(int id) {

		try {
			Connection connection = this.connect.makeConnection();
			String employee_entry_sql = "UPDATE entry SET entry_status = 'rejected' WHERE entry_id = ? ";
			PreparedStatement entries_statement;
			entries_statement = connection.prepareStatement(employee_entry_sql);
			entries_statement.setInt(1, id);
			entries_statement.executeUpdate();
                        entries_statement.close();
			connection.close();
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
		String employee_entry_sql = "SELECT * FROM entry " +
                                            "JOIN entry_version ON entry_id=entry_version_entry_fk INNER JOIN employee ON employee_id=entry_employee_fk " +
                                            "JOIN employee_version ON employee_id=employee_version_employee_fk " +
                                            "WHERE entry_version_current=true AND entry_status = 'queued' " + 
                                            "AND entry_isdeleted=false AND employee_version_current=true AND employee_isdeleted=false;";
		try {
			PreparedStatement entries_statement = this.connect.makeConnection().prepareStatement(employee_entry_sql);
			
			ResultSet entry_set = entries_statement.executeQuery();
                        entry_alist = fillModels(entry_set);
//			while(entry_set.next()) {
//				EntryModel dummy = new EntryModel();
//				dummy.setEntryId(entry_set.getInt("entry_id"));
//				dummy.setEntryDescription(entry_set.getString("entry_version_description"));
//				dummy.setEntryStartTime(entry_set.getString("entry_version_starttime"));
//				dummy.setEntryEndTime(entry_set.getString("entry_version_endtime"));
//				dummy.setEntryDate(entry_set.getString("entry_version_date"));
//				dummy.setEntryProjectFk(entry_set.getInt("entry_version_project_fk"));
//				dummy.setEntryIsLocked(entry_set.getBoolean("entry_islocked"));
//				dummy.setEntrySprintFk(entry_set.getInt("entry_version_sprint_fk"));
//				dummy.setEntryUserstoryFk(entry_set.getInt("entry_version_userstory_fk"));
//				entry_alist.add(dummy);
//			}
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
	 * @param userId        userstory Id
         */
	public void addEntry(int employeeId, int pId, int spId, Date date, String description, Time startTime, Time endTime, int userId){               
		PreparedStatement insertProject;
		String addEntrySQL = "SELECT add_entry(?,?,?,?,?,?,?,?);";
		try {
			insertProject = this.connect.makeConnection().prepareStatement(addEntrySQL);
			
			insertProject.setInt(1, employeeId);
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
			insertProject.setDate(5, date);
			insertProject.setTime(6, startTime);
			insertProject.setTime(7, endTime);
			if(userId == 0)
			{
				insertProject.setNull(8, java.sql.Types.INTEGER);
			}
			else
			{
				insertProject.setInt(8, userId);
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
                        createEntry.close();
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
				entry_container.setEntryDate(entry_set.getDate("entry_version_date"));
				
				entry_alist.add(entry_container);
			}
			entries_statement.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return entry_alist;
	}
        
        /**
         * @author Robert
         * @param entryId id van de entry die gedelete moet worden
         */
        public void deleteEntry(int entryId){
            PreparedStatement deleteStatement;
            String deleteSQL="UPDATE entry SET entry_isdeleted=true WHERE entry_id=?";
            try {
                deleteStatement = this.connect.makeConnection().prepareStatement(deleteSQL);
                deleteStatement.setInt(1, entryId);
                deleteStatement.executeUpdate();
                deleteStatement.close();
            } catch (Exception ex) {
                Logger.getLogger(EntryDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        /**
         * @author Robert
         * @param entryId id van entry
         */
        public void unDeleteEntry(int entryId){
            PreparedStatement deleteStatement;
            String deleteSQL="UPDATE entry SET entry_isdeleted=false WHERE entry_id=?";
            try {
                deleteStatement = this.connect.makeConnection().prepareStatement(deleteSQL);
                deleteStatement.setInt(1, entryId);
                deleteStatement.executeUpdate();
                deleteStatement.close();
            } catch (Exception ex) {
                Logger.getLogger(EntryDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
}
