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

/**
 * Stores the list of all users
 * 
 * @author Swapnil Napuri
 * @author Srinandini Marpaka
 */

public class UserList implements Serializable {

	/**
	 * Identifier used to serialize/deserialize an object
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Directory in which the serialized file is stored
	 */
	private static final String storeDir = "resources";

	/**
	 * Name of the serialized file
	 */
	private static final String storeFile = "SerializedData.dat";

	/**
	 * List of users of the application
	 */
	private static List<User> userList = new ArrayList<>();

	/**
	 * Serializes the user data
	 */
	public static void serialize() {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(
					new FileOutputStream(storeDir + File.separator + storeFile));
			oos.writeObject(userList);
			oos.close();
		} catch (IOException e) {
		}
	}

	/**
	 * Deserializes the user data
	 */
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
	 * Returns whether a specific user exists or not
	 * 
	 * @param username username of the user
	 * @return boolean true if user exists; false otherwise
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
	 * Returns the instance of the desired user
	 * 
	 * @param username username of the desired user
	 * @return User instance of the desired user if it exists; null if such user
	 *         does not exist
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
	 * Adds an user to the list of users
	 * 
	 * @param username username of the new user
	 * @return boolean true if successfully added; false otherwise
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
	 * Deletes a specific user from the list of users
	 * 
	 * @param username username of the user to be deleted
	 * @return boolean true of successfully deleted, false otherwise
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
	 * Returns the list of users of the application
	 * 
	 * @return List<User> list of users of the application
	 */
	public static List<User> getUsers() {
		deserialize();

		return userList;
	}

}
