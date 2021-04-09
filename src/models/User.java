package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {

	private String username;
	private List<Album> albums;

	public User(String username) {
		this.username = username.toLowerCase();
		albums = new ArrayList<>();
	}
	
	public boolean addAlbum(String name) {
		for (Album item: albums) {
			if (item.getName().equals(name))
				return false;
		}
		
		albums.add(new Album(name));
		return true;
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

}
