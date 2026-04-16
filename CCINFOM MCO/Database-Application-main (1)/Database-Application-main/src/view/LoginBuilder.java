package view; 

public class LoginBuilder {
	private int ID;
	private String password; 
	private String type;

	public LoginBuilder() {
	}

	public LoginBuilder(int ID, String password, String type) {
		this.ID = ID;
		this.password = password;
		this.type = type;
	}

	public int getID() { return ID; }
	public String getPassword() { return password; }
	public String getType() { return type; }
}
