package models;

import java.io.Serializable;

public class User implements Serializable {

	private String username;

	public User(String username) {
		this.username = username.toLowerCase();
	}

	public String getUsername() {
		return username;
	}

}
