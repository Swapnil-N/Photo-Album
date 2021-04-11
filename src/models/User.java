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
	
	public boolean hasAlbumWithName(String name) {
		for (Album item : albums) {
			if (item.getName().equals(name))
				return true;
		}
		return false;
	}
	
	public boolean addAlbum(String name) {
		if (hasAlbumWithName(name))
			return false;

		albums.add(new Album(name));
		return true;
	}

	public boolean deleteAlbum(String name) {
		if (!hasAlbumWithName(name))
			return false;

		for (int i = 0; i < albums.size(); i++) {
			if (albums.get(i).getName().equals(name)) {
				albums.remove(i);
				return true;
			}
		}
		return false;
	}
	
	public boolean addTag(String tag) {
		tag = tag.trim().toLowerCase();
		
		if (tag.equals(addNewTag.toLowerCase()) && !tags.contains(addNewTag)) {
			tags.add(tag);
			return true;
		} else if (tag.equals(addNewTag.toLowerCase()))
			return false;
		
		tag = tag.toLowerCase();
		
		if (tags.contains(tag))
			return false;
		
		tags.add(0, tag);
		
		return true;
	}

	public String getUsername() {
		return username;
	}
	
	public Album getAlbumWithName(String name) {
		if (!hasAlbumWithName(name))
			return null;
		
		for (Album album: albums) {
			if (album.getName().equals(name))
				return album;
		}
		
		return null;
	}

	public List<Album> getAlbums() {
		Collections.sort(albums, (a, b) -> a.getName().compareTo(b.getName()));
		return albums;
	}

	public void setAlbums(List<Album> albums) {
		this.albums = albums;
	}
	
	public List<String> getTags() {
		tags.remove(tags.size()-1);
		Collections.sort(tags);
		tags.add(addNewTag);
		
		return tags;
	}
	
	public String getAddNewTag() {
		return addNewTag;
	}
	
	@Override
	public String toString() {
		return username + " " + albums.size();

	}

}
