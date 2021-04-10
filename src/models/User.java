package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	private String username;
	private List<Album> albums;

	public User(String username) {
		this.username = username.toLowerCase();
		albums = new ArrayList<>();
	}

	public boolean addAlbum(String name) {
		if (hasAlbumWithName(name))
			return false;

		albums.add(new Album(name));
		return true;
	}

	public boolean hasAlbumWithName(String name) {
		for (Album item : albums) {
			if (item.getName().equals(name))
				return true;
		}
		return false;
	}

	public boolean deleteAlbumWithName(String name) {
		if (!hasAlbumWithName(name))
			return false;
		
		for (int i = 0; i < albums.size(); i++) {
			if (albums.get(i).getName().equals(name))
				albums.remove(i);
				return true;
		}
		return false;
	}

	public String getUsername() {
		return username;
	}

	public List<Album> getAlbums() {
		return albums;
	}

	public void setAlbums(List<Album> albums) {
		this.albums = albums;
	}

	@Override
	public String toString() {
		
		return username +" "+ albums.size();
		
	}

}
