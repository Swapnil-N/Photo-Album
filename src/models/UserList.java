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
	private static final String storeFile = "SerializedData.dat";

	private static List<User> userList = new ArrayList<>();

	public static void serialize() {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(
					new FileOutputStream(storeDir + File.separator + storeFile));
			oos.writeObject(userList);
			oos.close();
		} catch (IOException e) {
		}
	}

	@SuppressWarnings("unchecked")
	public static void deserialize() {
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storeDir + File.separator + storeFile));
			userList = (List<User>) ois.readObject();
			ois.close();
		} catch (ClassNotFoundException | IOException e) {
			userList = new ArrayList<>();
		}

	}

	
	/** 
	 * @param username
	 * @return boolean
	 */
	public static boolean containsUser(String username) {
		deserialize();

		for (User currentUser : userList) {
			if (currentUser.getUsername().equals(username))
				return true;
		}

		return false;
	}

	
	/** 
	 * @param username
	 * @return User
	 */
	public static User getUserWithName(String username) {
		deserialize();

		for (User currentUser : userList) {
			if (currentUser.getUsername().equals(username))
				return currentUser;
		}

		return null;
	}

	
	/** 
	 * @param username
	 * @return boolean
	 */
	public static boolean addUser(String username) {
		deserialize();

		if (username.isEmpty() || containsUser(username))
			return false;

		User user = new User(username);
		userList.add(user);

		serialize();
		return true;
	}

	
	/** 
	 * @param username
	 * @return boolean
	 */
	public static boolean deleteUser(String username) {
		deserialize();

		if (username.equals("admin"))
			return false;

		for (int i = 0; i < userList.size(); i++) {
			if (userList.get(i).getUsername().equals(username)) {
				userList.remove(i);

				serialize();
				return true;
			}
		}

		return false;
	}

	
	/** 
	 * @return List<User>
	 */
	public static List<User> getUsers() {
		deserialize();

		return userList;
	}

}
