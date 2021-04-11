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

	static List<User> userList = new ArrayList<>();

	public UserList() {
		deserialize();
	}

	private static void serialize() throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeDir + File.separator + storeFile));
		oos.writeObject(userList);
		oos.close();
	}

	@SuppressWarnings("unchecked")
	private static void deserialize() {
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storeDir + File.separator + storeFile));
			userList = (List<User>) ois.readObject();
			ois.close();
		} catch (ClassNotFoundException | IOException e) {
			userList = new ArrayList<>();
		}

	}

	public boolean containsUser(User user) {
		for (User currentUser : userList) {
			if (currentUser.getUsername().equals(user.getUsername())) {
				return true;
			}

		}
		return false;

	}

	public boolean addUser(User user) throws IOException {
		if (user.getUsername().isEmpty() || containsUser(user))
			return false;

		userList.add(user);
		serialize();

		return true;
	}

	public boolean deleteUser(User user) throws IOException {
		if (user.getUsername().equals("admin"))
			return false;

		for (int i = 0; i < userList.size(); i++) {
			if (userList.get(i).getUsername().equals(user.getUsername())) {
				userList.remove(i);
				serialize();
				return true;
			}
		}

		return false;
	}

	public List<User> getUsers() {
		return userList;
	}

}
