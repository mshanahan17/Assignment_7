package Project;

import java.util.ArrayList;

public class Persons implements People{

	private String code;
	private String firstName;
	private String lastName;
	private Address address;
	private ArrayList<String> email;


	public Persons(String code, String lastName, String firstName, Address address, ArrayList<String> email) {
		this.code = code.trim();
		this.firstName = firstName.trim();
		this.lastName = lastName.trim();
		this.address = address;
		this.email = email;
	}

	public String getCode() {
		return code;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public Address getAddress() {
		return address;
	}

	public ArrayList<String> getEmail() {
		return email;
	}

	public String printName(){
		String name = lastName + ", " + firstName;
		return name;
	}

	public String getType() {
		return null;
	}

	public double getFees(String a, int b) {
		return 0;
	}

	public double getCommision(String a, double b) {
		return 0;
	}
}
