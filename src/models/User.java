package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents an user
 * 
 * @author Swapnil Napuri
 * @author Srinandini Marpaka
 */

public class User implements Serializable {

	/**
	 * Identifier used to serialize/deserialize an object
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Username of the user
	 */
	private String username;

	/**
	 * Albums of the user
	 */
	private List<Album> albums;

	/**
	 * Master list of the tags on the user's photos
	 */
	private List<String> tags;

	/**
	 * Label of the option to create a new tag
	 */
	private String addNewTag = "Add New Tag";

	/**
	 * Initializes an user
	 * 
	 * @param username username of the user
	 */
	public User(String username) {
		this.username = username;
		albums = new ArrayList<>();
		tags = new ArrayList<>();

		tags.add("location");
		tags.add("person");
		tags.add(addNewTag);
	}

	/**
	 * Returns whether a specific album exists or not
	 * 
	 * @param name name of the album
	 * @return boolean true if album exists; false otherwise
	 */
	public boolean hasAlbumWithName(String name) {
		for (Album item : albums) {
			if (item.getName().equals(name))
				return true;
		}
		return false;
	}

	/**
	 * Returns the instance of the desired album
	 * 
	 * @param name name of the desired album
	 * @return Album instance of the desired album if it exists; null if such album
	 *         does not exist
	 */
	public Album getAlbumWithName(String name) {
		if (!hasAlbumWithName(name))
			return null;

		for (Album album : albums) {
			if (album.getName().equals(name))
				return album;
		}

		return null;
	}

	/**
	 * Adds an album to the user's albums
	 * 
	 * @param name name of the new album
	 * @return boolean true if successfully added; false otherwise
	 */
	public boolean addAlbum(String name) {
		if (hasAlbumWithName(name))
			return false;

		albums.add(new Album(name));

		UserList.serialize();
		return true;
	}

	/**
	 * Deletes a specific album from the user's albums
	 * 
	 * @param name name of the album to be deleted
	 * @return boolean true of successfully deleted, false otherwise
	 */
	public boolean deleteAlbum(String name) {
		if (!hasAlbumWithName(name))
			return false;

		for (int i = 0; i < albums.size(); i++) {
			if (albums.get(i).getName().equals(name)) {
				albums.remove(i);

				UserList.serialize();
				return true;
			}
		}

		return false;
	}

	/**
	 * Adds a tag to the user's master list of tags
	 * 
	 * @param tag tag to be added
	 * @return boolean true if successfully added; false if it already exists
	 */
	public boolean addTag(String tag) {
		tag = tag.trim();

		if (tags.contains(tag))
			return false;

		tags.add(0, tag);

		UserList.serialize();
		return true;
	}

	/**
	 * Returns the user's username
	 * 
	 * @return String user's username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Returns the user's list of albums
	 * 
	 * @return List<Album> user's list of albums
	 */
	public List<Album> getAlbums() {
		Collections.sort(albums, (a, b) -> a.getName().compareTo(b.getName()));
		return albums;
	}

	/**
	 * Returns the master list of the user's tags
	 * 
	 * @return List<String> master list of the user's tags
	 */
	public List<String> getTags() {
		tags.remove(tags.size() - 1);
		Collections.sort(tags);
		tags.add(addNewTag);

		return tags;
	}

	/**
	 * Returns the label of the option to add a new tag
	 * 
	 * @return String label of the option to add a new tag
	 */
	public String getAddNewTag() {
		return addNewTag;
	}

}
