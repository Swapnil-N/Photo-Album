package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Album {
	
	private String name;
	private List<Photo> photoList;
	private User user;
	private Date firstDate;
	private Date lastDate;
	
	
	public Album(String name, User user) {
		this.name = name;
		this.user = user;
		photoList = new ArrayList<>();
	}
	
	public boolean addPhoto(Photo photo) {
		photoList.add(photo);
		return true;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Photo> getPhotoList() {
		return photoList;
	}

	public void setPhotoList(List<Photo> photoList) {
		this.photoList = photoList;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getFirstDate() {
		return firstDate;
	}

	public void setFirstDate(Date firstDate) {
		this.firstDate = firstDate;
	}

	public Date getLastDate() {
		return lastDate;
	}

	public void setLastDate(Date lastDate) {
		this.lastDate = lastDate;
	}

}
