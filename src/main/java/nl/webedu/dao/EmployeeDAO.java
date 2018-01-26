package nl.webedu.dao;

import java.sql.Connection;
import nl.webedu.models.EmployeeModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Employee DAO to be used to access database to get employee information
 */
public class EmployeeDAO {
	private ConnectDAO connect;

	public EmployeeDAO(){
		this.connect = new ConnectDAO();
	}
        
        public boolean updateEmployee(EmployeeModel employeeModel){
            String setCurrentVersionToFalse = "UPDATE employee_version "
				+ "SET employee_version_current = false "
				+ "WHERE employee_version_employee_fk = ?";
            String insertUser_sql = "insert into employee_version (employee_version_employee_fk, " +
				"employee_version_firstname, employee_version_lastname, employee_version_role, "
				+ "employee_version_email, employee_version_password, employee_version_current) " +
				"values (?, ?, ?, ?::enum_role, ?, ?,true)";
    
            try {
                Connection connection = this.connect.makeConnection();
                PreparedStatement setCurrentFalse = connection.prepareStatement(setCurrentVersionToFalse);
                setCurrentFalse.setInt(1, employeeModel.getEmployeeId());
                setCurrentFalse.executeUpdate();
                setCurrentFalse.close();
                
                PreparedStatement insertEmployeeVersion = connection.prepareStatement(insertUser_sql);
                insertEmployeeVersion.setInt(1, employeeModel.getEmployeeId());
                insertEmployeeVersion.setString(2, employeeModel.getEmployeeFirstname());
                insertEmployeeVersion.setString(3, employeeModel.getEmployeeLastname());
                insertEmployeeVersion.setString(4, employeeModel.getEmployeeRole());
                insertEmployeeVersion.setString(5, employeeModel.getEmployeeEmail());
                insertEmployeeVersion.setString(6, employeeModel.getEmployeePassword());
                insertEmployeeVersion.executeUpdate();
                        
                    // close stuff
                insertEmployeeVersion.close();
                connection.close();
                return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
                return false;
        }
        
	/**Deze methode voegt een nieuwe user toe. In tegenstelling tot createEmployee()
	 * voegt hij een version toe
         * @param employeeModel de employee die je toe wilt voegen
         * @return boolean
	 */
	public boolean createEmployeeVersion(EmployeeModel employeeModel){
            String insertEmployee_sql = "insert into employee (employee_isdeleted)values(false)returning employee_id";
            String insertUser_sql = "insert into employee_version (employee_version_employee_fk, " +
				"employee_version_firstname, employee_version_lastname, employee_version_role, "
				+ "employee_version_email, employee_version_password, employee_version_current) " +
				"values (?, ?, ?, ?::enum_role, ?, ?,?)";
            PreparedStatement insertEmployee;
            PreparedStatement insertEmployeeVersion;
		
		try {
                    Connection connection = this.connect.makeConnection();
                    insertEmployee = connection.prepareStatement(insertEmployee_sql);
                    ResultSet rs = insertEmployee.executeQuery();
                    rs.next();
                    int employeeId = rs.getInt(1);
                    insertEmployee.close();
                    
                
                    insertEmployeeVersion = connection.prepareStatement(insertUser_sql);
                    insertEmployeeVersion.setInt(1, employeeId);
                    insertEmployeeVersion.setString(2, employeeModel.getEmployeeFirstname());
                    insertEmployeeVersion.setString(3, employeeModel.getEmployeeLastname());
                    insertEmployeeVersion.setString(4, employeeModel.getEmployeeRole());
                    insertEmployeeVersion.setString(5, employeeModel.getEmployeeEmail());
                    insertEmployeeVersion.setString(6, employeeModel.getEmployeePassword());
                    insertEmployeeVersion.setBoolean(7,true);
                    insertEmployeeVersion.executeUpdate();
                        
                    // close stuff
                    insertEmployeeVersion.close();
                    connection.close();
                    return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
                return false;
	}
	/**
	 * Unlock an employee
	 * @param emp_id id to lock
         * @throws Exception Een sql exception en normale exception
	 */
	public void unlockEmployee(int emp_id) throws Exception {
		String lock_query = "UPDATE employee "
				+ "SET employee_isdeleted = false "
				+ "WHERE employee_id = ?";
                Connection connection = this.connect.makeConnection();
		PreparedStatement lock_statement = connection.prepareStatement(lock_query);
		lock_statement.setInt(1, emp_id);
		lock_statement.executeUpdate();
                lock_statement.close();
                connection.close();
	}

	/**
	 * Method to assign a user to a empty user model to be used throughout the application.
	 * This is put inside the administrator class for convience.
	 * @param email - users email to verify login
	 * @param pw - users password to verify login
	 * @return a user model if login was successful
	 */
	public EmployeeModel loginAssignment(String email, String pw){

		String login_sql = "SELECT * FROM employee, employee_version "
				+ "WHERE employee_version_email = ? AND employee_version_password = ?"
				+ "AND employee_id = employee_version_employee_fk";
		PreparedStatement login_statement;
		
		try {
                    Connection connection = this.connect.makeConnection();
			login_statement = connection.prepareStatement(login_sql);
			login_statement.setString(1, email);
			login_statement.setString(2, pw);
			ResultSet user_set = login_statement.executeQuery();
			
			if(user_set.next()){
				return new EmployeeModel(
						user_set.getInt("employee_id"),
						user_set.getBoolean("employee_isdeleted"),
						user_set.getString("employee_version_firstname"),
						user_set.getString("employee_version_lastname"),
						user_set.getString("employee_version_password"),
						user_set.getString("employee_version_email"),
						user_set.getString("employee_version_role"));
			}
                        user_set.close();
			login_statement.close();
                        connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Lock an employee
	 * @param emp_id id to lock
         * @throws Exception een SQL exceptione n normale exception
         * @return boolean
	 */
	public boolean lockEmployee(int emp_id) throws Exception {
		String lock_query = "UPDATE employee SET employee_isdeleted = true WHERE employee_id = ?";
                Connection connection = this.connect.makeConnection();
		PreparedStatement lock_statement = connection.prepareStatement(lock_query);
		lock_statement.setInt(1, emp_id);
		lock_statement.executeUpdate();
                lock_statement.close();
                connection.close();
                return true;
	}

        /**
         * 
         * @return employee_alist arraylist met employees
         */
	public ArrayList<EmployeeModel> getAllEmployees(){
		//Empty list to return
		ArrayList<EmployeeModel> employee_alist = new ArrayList<EmployeeModel>();
		
		String employee_entry_sql = "SELECT * FROM employee_version INNER JOIN employee ON employee_version_employee_fk = employee_id WHERE employee_version_current = true AND employee.employee_isdeleted = false";
                try{
                    Connection connection = this.connect.makeConnection();
                    PreparedStatement user_statement = connection.prepareStatement(employee_entry_sql);

                    ResultSet userSet = user_statement.executeQuery();
                    while(userSet.next()) {
                        EmployeeModel employee = new EmployeeModel();
                            employee.setEmployeeId(userSet.getInt("employee_version_employee_fk"));
                            employee.setEmployeeFirstname(userSet.getString("employee_version_firstname"));
                            employee.setEmployeeLastname(userSet.getString("employee_version_lastname"));
                            employee.setEmployeeEmail(userSet.getString("employee_version_email"));
                            employee.setEmployeePassword(userSet.getString("employee_version_password"));
                            employee.setEmployeeRole(userSet.getString("employee_version_role"));
                            employee_alist.add(employee);
                    }
                userSet.close();
                    user_statement.close();
                    connection.close();
                    return employee_alist;
            }catch(Exception c){
                c.getMessage();
            }
           return null;
        }

	/**
	 * Accounts that are currently active (not this.connected, but active in the sense that they are able to user their account
	 * @return ArrayList of active employee models
	 * @throws Exception - SQLException
	 */
	public ArrayList<EmployeeModel> activeAccountsList() throws Exception {
		//Empty list to return
		ArrayList<EmployeeModel> employee_alist = new ArrayList<EmployeeModel>();
		
		String employee_entry_sql = "SELECT * FROM employee, employee_version "
				+ "WHERE  employee_id = employee_version_employee_fk "
				+ "AND employee_isdeleted = false AND employee_version_current=TRUE";
                Connection connection = this.connect.makeConnection();
		PreparedStatement user_statement = connection.prepareStatement(employee_entry_sql);

		ResultSet userSet = user_statement.executeQuery();
		while(userSet.next()) {
                    EmployeeModel employee_container = new EmployeeModel(
                    userSet.getInt("employee_id"), userSet.getBoolean("employee_isdeleted"),
                    userSet.getString("employee_version_firstname"), userSet.getString("employee_version_lastname"),
                    userSet.getString("employee_version_password"), userSet.getString("employee_version_email"),
                    userSet.getString("employee_version_role"));
                    employee_alist.add(employee_container);
                }
                userSet.close();
		user_statement.close();
                connection.close();
		return employee_alist;
	}

	/**
	 * Query db for list with all accounts that are locked
	 * @return ArrayList of employee models who are locked
	 */
	public ArrayList<EmployeeModel> lockedAccountsList(){
		//Empty list to return
		ArrayList<EmployeeModel> employee_alist = new ArrayList<EmployeeModel>();
		
		String employee_entry_sql = "SELECT * FROM employee, employee_version "
				+ "WHERE  employee_id = employee_version_employee_fk "
				+ "AND employee_isdeleted = true";
		try {
                    Connection connection = this.connect.makeConnection();
			PreparedStatement user_statement = connection.prepareStatement(employee_entry_sql);

			ResultSet userSet = user_statement.executeQuery();
			while(userSet.next()) {
				EmployeeModel employee_container = new EmployeeModel(
				userSet.getInt("employee_id"), userSet.getBoolean("employee_isdeleted"), 
				userSet.getString("employee_version_firstname"), userSet.getString("employee_version_lastname"), 
				userSet.getString("employee_version_password"), userSet.getString("employee_version_email"), 
				userSet.getString("employee_version_role")
						
				);	
				employee_alist.add(employee_container);
			}
                        userSet.close();
			user_statement.close();
                        connection.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return employee_alist;
	}
	
	/**
	 * Return a employee model filled with information relating to given employee id
	 * @param e_id - use the employee ID to link user to model
	 * @return null of user employee model with given information
	 */
	public EmployeeModel employee_information(int e_id) {
		String employee_sql = "SELECT * FROM employee, employee_version WHERE employee_id = ?";
		PreparedStatement employee_statement;
		
		try {
                    Connection connection = this.connect.makeConnection();
			employee_statement = connection.prepareStatement(employee_sql);
			employee_statement.setInt(1, e_id);
			ResultSet user_set = employee_statement.executeQuery();
			
			while(user_set.next()){
				EmployeeModel user = new EmployeeModel(
						user_set.getInt("employee_id"),
						user_set.getBoolean("employee_isdeleted"),
						user_set.getString("employee_version_firstname"),
						user_set.getString("employee_version_lastname"),
						user_set.getString("employee_version_password"),
						user_set.getString("employee_version_email"),
						user_set.getString("employee_version_role"));
				return user;
			}
                        user_set.close();
			employee_statement.close();
                        connection.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

        /**
         * Bewerken employee
         * 
         * @param employeeModel model van de medewerker
         */
	public void editEmployee(EmployeeModel employeeModel){
		String oldVersionDisableSql = "UPDATE employee_version SET employee_version_current=false" +
				" WHERE employee_version_employee_fk = ? AND employee_version_current = true";
		String addNewVersionSql = "INSERT INTO employee_version(employee_version_employee_fk," +
				"employee_version_firstname,employee_version_lastname,employee_version_role,employee_version_email," +
				"employee_version_password,employee_version_current)" +
				"VALUES(?,?,?,?::enum_role,?,?,?)";
		try {
                    Connection connection = this.connect.makeConnection();
			PreparedStatement changeVersions= connection.prepareStatement(oldVersionDisableSql);
			changeVersions.setInt(1, employeeModel.getEmployeeId());

			changeVersions.executeUpdate();
			changeVersions.close();

			PreparedStatement addVersionStatement = connection.prepareStatement(addNewVersionSql);
			addVersionStatement.setInt(1,employeeModel.getEmployeeId());
			addVersionStatement.setString(2,employeeModel.getEmployeeFirstname());
			addVersionStatement.setString(3,employeeModel.getEmployeeLastname());
			addVersionStatement.setString(4,employeeModel.getEmployeeRole());
			addVersionStatement.setString(5,employeeModel.getEmployeeEmail());
			addVersionStatement.setString(6,employeeModel.getEmployeePassword());
			addVersionStatement.setBoolean(7,true);

			addVersionStatement.executeQuery();
			addVersionStatement.close();
                        connection.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}

