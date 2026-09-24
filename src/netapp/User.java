package netapp;

import java.sql.Date;

public class User {	// class that holds the personal information of the logged-in user
	String firstName;
	String SecondName;
	String email;
	String country;
	String gender;
	Date dateOfBirth;
	
	public User(String firstName, String secondName, String email, String country, String gender, Date dateOfBirth) {
		super();
		this.firstName = firstName;
		SecondName = secondName;
		this.email = email;
		this.country = country;
		this.gender = gender;
		this.dateOfBirth = dateOfBirth;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getSecondName() {
		return SecondName;
	}

	public String getEmail() {
		return email;
	}

	public String getCountry() {
		return country;
	}
	public String getGender() {
		return gender;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	// setters to use when updating personal information
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setSecondName(String secondName) {
		SecondName = secondName;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Override
	public String toString() {
		return firstName+" "+SecondName+ " ("+email+")";
	}
}
