package nl.webedu.models;

public class EmployeeModel{
	/**
	 * Employee Model.
	 */
	private int employeeId;
	
	private String employeeFirstname;
	private String employeeLastname;
	private String employeePassword;
	private String employeeEmail;
	
	private boolean employeeIsDeleted;
	
	private String employeeRole;

	public EmployeeModel(int id, boolean isdeleted, String firstname, String lastname, String wachtwoord, String email, String role){
		this.employeeId = id;
		this.employeeFirstname = firstname;
		this.employeeLastname = lastname;
		this.employeePassword = wachtwoord;
		this.employeeEmail = email;
		this.employeeRole = role;
	}
	public EmployeeModel(){}
	public int getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}
	public String getEmployeeFirstname() {
		return employeeFirstname;
	}

	public String getEmployeeLastName() {
		return employeeLastname;
	}
	public void setEmployeeFirstname(String employeeFirstname) {
		this.employeeFirstname = employeeFirstname;
	}
	public void setEmployeeLastname(String employeeLastname) {
		this.employeeLastname = employeeLastname;
	}
	public String getEmployeePassword() {
		return employeePassword;
	}
	public void setEmployeePassword(String employeePassword) {
		this.employeePassword = employeePassword;
	}
	public String getEmployeeEmail() {
		return employeeEmail;
	}
	public void setEmployeeEmail(String employeeEmail) {
		this.employeeEmail = employeeEmail;
	}

	public String getEmployeeRole() {
		return employeeRole;
	}
	public void setEmployeeRole(String employeeRol) {
		this.employeeRole = employeeRol;
	}
	
	public boolean isEmployeeIsDeleted() {
		return employeeIsDeleted;
	}

	public void setEmployeeIsDeleted(boolean employeeIsDeleted) {
		this.employeeIsDeleted = employeeIsDeleted;
	}



}
