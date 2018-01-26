package nl.webedu.dao;

import nl.webedu.models.CustomerModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CustomerDAO {

	private ConnectDAO connect;

	public CustomerDAO(){
		this.connect = new ConnectDAO();
		createAddCustomerFunction();
	}

	 /**
	 * Deze methode maakt een stored procedure aan die een nieuwe klant kan toevoegen zonder onderbroken te worden
	 * door een andere gebruiker (atomicity). Date: 30-10-2017
	 * @author Robert den Blaauwen
	 */
	public void createAddCustomerFunction(){
		String project_list_sql = "CREATE OR REPLACE FUNCTION add_customer(name TEXT, description TEXT) " +
				"RETURNS void AS $$ " +
				"DECLARE pk INT; " +
				"BEGIN " +
				" INSERT INTO customer(customer_isdeleted) VALUES(false) " +
				"    RETURNING customer_id INTO pk; " +
				"    INSERT INTO customer_version(customer_version_customer_fk, customer_version_name, customer_version_description,customer_version_current) " +
				"    VALUES(pk,name,description,true); " +
				"END $$ LANGUAGE plpgsql; ";
		try {
			PreparedStatement project_statement = this.connect.makeConnection().prepareStatement(project_list_sql);
			project_statement.executeUpdate();
			System.out.println(this.getClass().toString()+": constructor: FUNCTION add_customer(name, description) has been created!");
                        project_statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Return a customer model filled with information relating to given customer id
	 * If id does not match with any given customer you will get a empty customer
	 * @param c_id - to match with unique customer
	 * @return CustomerModel with specific customer information
	 */
	public CustomerModel customerInformation(int c_id) {
		String login_sql = "SELECT * FROM customer c INNER JOIN customer_version cv ON c.customer_id=cv.customer_version_customer_fk WHERE customer_id = ? AND customer_version_current=true";
		PreparedStatement customer_statement;
		
		try {
			customer_statement = this.connect.makeConnection().prepareStatement(login_sql);
			customer_statement.setInt(1, c_id);
			ResultSet customer_set = customer_statement.executeQuery();
			
			CustomerModel customer = new CustomerModel();
			while(customer_set.next()){
				customer.setCustomer_id(customer_set.getInt("customer_id"));
				customer.setCustomer_isdeleted(customer_set.getBoolean("customer_isdeleted"));
				customer.setCustomer_name(customer_set.getString("customer_version_name"));
				customer.setCustomer_description(customer_set.getString("customer_version_description"));
				return customer;
			}
			customer_statement.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**Date:13-10-2017
	 * @author Robert
	 * @return Customers
	 */
	public ArrayList<CustomerModel> getCustomerList(){
		String login_sql = "SELECT * FROM customer c INNER JOIN customer_version cv "
				+ "ON c.customer_id=cv.customer_version_customer_fk "
				+ "AND cv.customer_version_current = true "
				+"ORDER BY cv.customer_version_name ASC";
		PreparedStatement customer_statement;
		
		try {
			customer_statement = this.connect.makeConnection().prepareStatement(login_sql);
			ResultSet customer_set = customer_statement.executeQuery();
			
			ArrayList<CustomerModel> customers = new ArrayList<CustomerModel>();
			while(customer_set.next()){
				CustomerModel customer = new CustomerModel();
				customer = new CustomerModel();
				customer.setCustomer_id(customer_set.getInt("customer_id"));
				customer.setCustomer_isdeleted(customer_set.getBoolean("customer_isdeleted"));
				customer.setCustomer_name(customer_set.getString("customer_version_name"));
				customer.setCustomer_description(customer_set.getString("customer_version_description"));
				customers.add(customer);
			}
			customer_statement.close();
			return customers;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


        /**
         * add a customer I suppose
         * 
         * @param name  foo
         * @param description   bar
         * @return boolean
         */
	public boolean addCustomer(String name, String description) {
		String login_sql = "SELECT add_customer('"+name+"','"+description+"')";
		PreparedStatement customer_statement;

		try {
			customer_statement = this.connect.makeConnection().prepareStatement(login_sql);
			ResultSet customer_set = customer_statement.executeQuery();
			customer_statement.close();
                        return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
                        return false;
		}
	}
	
	/**
	 * Deze methode wijzigt de geselcteerde klant
	 *
	 * @param cID           lol
	 * @param name          lol
	 * @param description   lol
	 * @author rezanaser
         * @return  boolean
	 */
	public boolean modifyCustomer(int cID, String name, String description) {
		String changePreviousVersion = "UPDATE customer_version set customer_version_current = false "
				+ "WHERE customer_version_customer_fk = ?";
                
                String changeCustomerTabel = "UPDATE customer set customer_isdeleted = true "
				+ "WHERE customer_id = ? ";
                
		String change_customer = "INSERT INTO customer_version(customer_version_customer_fk, customer_version_name, customer_version_description, customer_version_current)"
				+ "VALUES(?, ?, ?, true)";
		try {
			PreparedStatement changeVersions= this.connect.makeConnection().prepareStatement(changePreviousVersion);
			changeVersions.setInt(1, cID);
			changeVersions.executeUpdate();
			changeVersions.close();
                        
                        PreparedStatement changeCustomerTable= this.connect.makeConnection().prepareStatement(changeCustomerTabel);
			changeCustomerTable.setInt(1, cID);
			changeCustomerTable.executeUpdate();
			changeCustomerTable.close();
                        
                        
			PreparedStatement changeCustomer = this.connect.makeConnection().prepareStatement(change_customer);
			changeCustomer.setInt(1, cID);
			changeCustomer.setString(2, name);
			changeCustomer.setString(3, description);
			changeCustomer.executeUpdate();
			
			changeCustomer.close();
                        return true;
		} catch (Exception e) {
			e.getMessage();
                        return false;
		}
		
	}
	
	/**
	 * Deze methode zet de customer op inactive
	 * cId wordt meegekregen van CustomerManagementViewController
	 * @param cId   customer id
	 * @author rezanaser
         * @return boolean
	 */

	public boolean removeCustomer(int cId) {
		String remove_customer = "UPDATE customer SET customer_isdeleted = true WHERE customer_id = ?";
                String setCurrentOnFalse = "UPDATE customer_version set customer_version_current = false WHERE customer_version_customer_fk = ?";
		try {
			PreparedStatement lock_statement = this.connect.makeConnection().prepareStatement(remove_customer);
                        PreparedStatement lock_version_statement = this.connect.makeConnection().prepareStatement(setCurrentOnFalse);
			lock_statement.setInt(1, cId);
			lock_statement.executeUpdate();
                        lock_version_statement.setInt(1, cId);
                        lock_version_statement.executeUpdate();
                        lock_version_statement.close();
                        return true;
		} catch (Exception e) {
			e.printStackTrace();
                        return false;
		}
	}
	 /**
	 * De volgende voegt een nieuwe klant toe aan de customer_version tabel
	 * @param customerName klantNaam
	 * @param customerDes klant  beschriving
	 */
	public void addCustomerToDatabase(String customerName, String customerDes){
		PreparedStatement insertProject;
		String insertUser_sql = "insert into customer_version (customer_version_customer_fk, customer_version_name, customer_version_description, customer_version_current) "
				+ "VALUES (?, ?, ?, ?, ?)";
		try {
			insertProject = this.connect.makeConnection().prepareStatement(insertUser_sql);
			
			insertProject.setInt(1, createNewCustomer());
			insertProject.setString(2, customerName);
			insertProject.setString(3, customerDes);
			insertProject.setBoolean(4, true);
			insertProject.executeQuery();
			insertProject.close();
		} catch (Exception e) {
			System.out.println(e);
			//e.printStackTrace();
		}
	}
	/**
	 * De volgende voegt een nieuwe klant toe aan de klant tabel
	 * @author rezanaser
         * @return id
	 */
	public int createNewCustomer() {
		int id = 0;
		PreparedStatement createCustomer;
		ResultSet customerId = null;
		String insertProject_sql = "insert into customer (customer_isdeleted) values (?)";
		
		try {
			
			createCustomer = this.connect.makeConnection().prepareStatement(insertProject_sql, Statement.RETURN_GENERATED_KEYS);
			
			createCustomer.setBoolean(1, false);
			createCustomer.executeUpdate();
			createCustomer.getGeneratedKeys();
			customerId = createCustomer.getGeneratedKeys();
			
			while (customerId.next()) {
				id = customerId.getInt(1);
			}
                        createCustomer.close();
		} catch (Exception e) {
			
		}
		return id;
	}
	
}
