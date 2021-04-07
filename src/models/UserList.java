package models;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserList implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private static final String storeDir = "resources";
	private static final String storeFile = "SerializedUsers.dat";
	
	static List<User> userList;
	
	public UserList() {
		userList = new ArrayList<>();
		userList.add(new User("stock"));
	}
	
	private static void serialize() throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeDir + File.separator + storeFile));
		oos.writeObject(userList);
	}
	
	private static void deserialize() throws ClassNotFoundException, IOException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storeDir + File.separator + storeFile));
		userList = (ArrayList<User>) ois.readObject();
	}
	
	public boolean userExists(User user) throws ClassNotFoundException, IOException {
		deserialize();
		
		for (User currentUser: userList) {
			if (currentUser.getUsername() == user.getUsername())
				return true;
		}
		
		return false;
		
	}
	
	public boolean addUser(User user) throws ClassNotFoundException, IOException {
		if (user.getUsername().isEmpty() || userExists(user))
			return false;
		
		userList.add(user);
		serialize();
		
		return true;
	}
	
	public boolean deleteUser(User user) throws ClassNotFoundException, IOException {
		deserialize();
		
		if (user.getUsername() == "admin")
			return false;
		
		for (int i = 0; i < userList.size(); i++) {
			if (userList.get(i).getUsername() == user.getUsername()) {
				userList.remove(i);
				serialize();
				return true;
			}
		} 
		
		return false;
	}
	
	public List<User> getUsers() throws ClassNotFoundException, IOException{
		deserialize();
		return userList;
	}

}
