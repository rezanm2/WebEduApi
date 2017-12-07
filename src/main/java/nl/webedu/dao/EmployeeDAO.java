package nl.webedu.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import nl.webedu.models.EntryModel;
import nl.webedu.models.EmployeeModel;
import nl.webedu.models.Role;
import nl.webedu.dao.ConnectDAO;

/**
 * Employee DAO to be used to access database to get employee information
 */
public class EmployeeDAO {
	private ConnectDAO connect;

	public EmployeeDAO(){
		this.connect = new ConnectDAO();
	}
        
	/**Deze methode voegt een nieuwe user toe. In tegenstelling tot createEmployee()
	 * voegt hij een version toe
	 *
	 * @param firstName firstname
	 * @param lastName  lastname
	 * @param role  role
	 * @param email email
	 * @param password password
	 */
	public void createEmployeeVersion(String firstName, String lastName, String role, String email, String password){
		PreparedStatement insertNewUser;
		String insertUser_sql = "insert into employee_version (employee_version_employee_fk, " +
				"employee_version_firstname, employee_version_lastname, employee_version_role, "
				+ "employee_version_email, employee_version_password, employee_version_current) " +
				"values (?, ?, ?, ?::enum_role, ?, ?,?)";
		try {
			insertNewUser = this.connect.makeConnection().prepareStatement(insertUser_sql);

			insertNewUser.setInt(1, createEmployee());
			insertNewUser.setString(2, firstName);
			insertNewUser.setString(3, lastName);
			insertNewUser.setString(4, role);
			insertNewUser.setString(5, email);
			insertNewUser.setString(6, password);
			insertNewUser.setBoolean(7,true);
			insertNewUser.executeUpdate();

			insertNewUser.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * return the id of the last employee id
	 * @return int of id
	 * @throws Exception
	 */
	public int createEmployee() throws Exception {
		int id = 0;
		PreparedStatement insertEmployee;
		ResultSet employeeId = null;
		String insertEmployee_sql = "insert into employee (employee_isdeleted) values (?)";

		insertEmployee = this.connect.makeConnection().prepareStatement(insertEmployee_sql, Statement.RETURN_GENERATED_KEYS);

		insertEmployee.setBoolean(1, false);
		insertEmployee.executeUpdate();
		insertEmployee.getGeneratedKeys();
		employeeId = insertEmployee.getGeneratedKeys();
		while (employeeId.next()) {
            id = employeeId.getInt(1);
        }
		return id;
	}

	/**
	 * Unlock an employee
	 * @param emp_id id to lock
	 */
	public void unlockEmployee(int emp_id) throws Exception {
		String lock_query = "UPDATE employee "
				+ "SET employee_isdeleted = false "
				+ "WHERE employee_id = ?";
		PreparedStatement lock_statement = this.connect.makeConnection().prepareStatement(lock_query);
		lock_statement.setInt(1, emp_id);
		lock_statement.executeUpdate();
	}

	/**
	 * Method to assign a user to a empty user model to be used throughout the application.
	 * This is put inside the administrator class for convience.
	 * @param email - users email to verify login
	 * @param pw - users password to verify login
	 * @return a user model if login was successful
	 */
	public nl.webedu.models.EmployeeModel loginAssignment(String email, String pw){

		String login_sql = "SELECT * FROM employee, employee_version "
				+ "WHERE employee_version_email = ? AND employee_version_password = ?"
				+ "AND employee_id = employee_version_employee_fk";
		PreparedStatement login_statement;
		
		try {
			login_statement = this.connect.makeConnection().prepareStatement(login_sql);
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
			login_statement.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Lock an employee
	 * @param emp_id id to lock
	 */
	public void lockEmployee(int emp_id) throws Exception {
		String lock_query = "UPDATE employee SET employee_isdeleted = true WHERE employee_id = ?";
		PreparedStatement lock_statement = this.connect.makeConnection().prepareStatement(lock_query);
		lock_statement.setInt(1, emp_id);
		lock_statement.executeUpdate();
	}
	public ArrayList<EmployeeModel> getAllEmployees() throws Exception {
		//Empty list to return
		ArrayList<EmployeeModel> employee_alist = new ArrayList<EmployeeModel>();
		
		String employee_entry_sql = "SELECT * FROM employee, employee_version "
				+ "WHERE  employee_id = employee_version_employee_fk "
				+ "AND employee_version_current = true AND employee_isdeleted = FALSE ";
		PreparedStatement user_statement = this.connect.makeConnection().prepareStatement(employee_entry_sql);

		ResultSet userSet = user_statement.executeQuery();
		while(userSet.next()) {
            EmployeeModel employee_container = new EmployeeModel(
            userSet.getInt("employee_id"), userSet.getBoolean("employee_isdeleted"),
            userSet.getString("employee_version_firstname"), userSet.getString("employee_version_lastname"),
            userSet.getString("employee_version_password"), userSet.getString("employee_version_email"),
            userSet.getString("employee_version_role"));
            employee_alist.add(employee_container);
        }
		user_statement.close();
		return employee_alist;
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
		PreparedStatement user_statement = this.connect.makeConnection().prepareStatement(employee_entry_sql);

		ResultSet userSet = user_statement.executeQuery();
		while(userSet.next()) {
            EmployeeModel employee_container = new EmployeeModel(
            userSet.getInt("employee_id"), userSet.getBoolean("employee_isdeleted"),
            userSet.getString("employee_version_firstname"), userSet.getString("employee_version_lastname"),
            userSet.getString("employee_version_password"), userSet.getString("employee_version_email"),
            userSet.getString("employee_version_role"));
            employee_alist.add(employee_container);
        }
		user_statement.close();
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
			PreparedStatement user_statement = this.connect.makeConnection().prepareStatement(employee_entry_sql);

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
			user_statement.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return employee_alist;
	}
	
	/**
	 * Return a employee model filled with information relating to given employee id
	 * @param e_id - use the employee ID to link user to model
	 * @return employee model with given information
	 */
	public EmployeeModel employee_information(int e_id) {
		String employee_sql = "SELECT * FROM employee, employee_version WHERE employee_id = ?";
		PreparedStatement employee_statement;
		
		try {
			employee_statement = this.connect.makeConnection().prepareStatement(employee_sql);
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
			employee_statement.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void editEmployee(EmployeeModel employeeModel){
		String oldVersionDisableSql = "UPDATE employee_version SET employee_version_current=false" +
				" WHERE employee_version_employee_fk = ? AND employee_version_current = true";
		String addNewVersionSql = "INSERT INTO employee_version(employee_version_employee_fk," +
				"employee_version_firstname,employee_version_lastname,employee_version_role,employee_version_email," +
				"employee_version_password,employee_version_current)" +
				"VALUES(?,?,?,?::enum_role,?,?,?)";
		try {
			PreparedStatement changeVersions= this.connect.makeConnection().prepareStatement(oldVersionDisableSql);
			changeVersions.setInt(1, employeeModel.getEmployeeId());

			changeVersions.executeUpdate();
			changeVersions.close();

			PreparedStatement addVersionStatement = this.connect.makeConnection().prepareStatement(addNewVersionSql);
			addVersionStatement.setInt(1,employeeModel.getEmployeeId());
			addVersionStatement.setString(2,employeeModel.getEmployeeFirstname());
			addVersionStatement.setString(3,employeeModel.getEmployeeLastName());
			addVersionStatement.setString(4,employeeModel.getEmployeeRole());
			addVersionStatement.setString(5,employeeModel.getEmployeeEmail());
			addVersionStatement.setString(6,employeeModel.getEmployeePassword());
			addVersionStatement.setBoolean(7,true);

			addVersionStatement.executeQuery();
			addVersionStatement.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}

