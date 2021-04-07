package models;

import java.util.ArrayList;
import java.util.List;

public class UserList {
	
	List<User> userList;
	
	public UserList() {
		userList = new ArrayList<>();
	}
	
	public boolean addUser(User user) {
		//deserialize
		if (user.getUsername().isEmpty())
			return false;
		for (User currentUser: userList) {
			if (currentUser.getUsername() == user.getUsername())
				return false;
		}
		
		userList.add(user);
		//serialize
		return true;
	}
	
	public boolean deleteUser(User user) {
		//deserialize
		if (user.getUsername() == "admin")
			return false;
		
		for (int i = 0; i < userList.size(); i++) {
			if (userList.get(i).getUsername() == user.getUsername()) {
				userList.remove(i);
				//serialize
				return true;
			}
		} 
		
		return false;
	}
	
	public boolean userExists(User user) {
		
		//deserialize
		
		for (User currentUser: userList) {
			if (currentUser.getUsername() == user.getUsername())
				return true;
		}
		
		return false;
		
	}
	
	public List<User> getUsers(){
		
		//deserialize
		return userList;
	}

}
