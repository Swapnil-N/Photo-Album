package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	private String username;
	private List<Album> albums;
	private List<String> tags;

	private String addNewTag = "Add New Tag";

	public User(String username) {
		this.username = username.toLowerCase();
		albums = new ArrayList<>();
		tags = new ArrayList<>();

		tags.add("location");
		tags.add("person");
		tags.add(addNewTag);
	}

	
	/** 
	 * @param name
	 * @return boolean
	 */
	public boolean hasAlbumWithName(String name) {
		for (Album item : albums) {
			if (item.getName().equals(name))
				return true;
		}
		return false;
	}

	
	/** 
	 * @param name
	 * @return Album
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
	 * @param name
	 * @return boolean
	 */
	public boolean addAlbum(String name) {
		if (hasAlbumWithName(name))
			return false;

		albums.add(new Album(name));

		UserList.serialize();
		return true;
	}

	
	/** 
	 * @param name
	 * @return boolean
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
	 * @param tag
	 * @return boolean
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
	 * @return String
	 */
	public String getUsername() {
		return username;
	}

	
	/** 
	 * @return List<Album>
	 */
	public List<Album> getAlbums() {
		Collections.sort(albums, (a, b) -> a.getName().compareTo(b.getName()));
		return albums;
	}

	
	/** 
	 * @param albums
	 */
	public void setAlbums(List<Album> albums) {
		this.albums = albums;
	}

	
	/** 
	 * @return List<String>
	 */
	public List<String> getTags() {
		tags.remove(tags.size() - 1);
		Collections.sort(tags);
		tags.add(addNewTag);

		return tags;
	}

	
	/** 
	 * @return String
	 */
	public String getAddNewTag() {
		return addNewTag;
	}

	
	/** 
	 * @return String
	 */
	@Override
	public String toString() {
		return username;

	}

}
