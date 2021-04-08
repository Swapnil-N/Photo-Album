package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Album {
	
	private String name;
	private List<Photo> photoList;
	private Date firstDate;
	private Date lastDate;
	
	
	public Album(String name) {
		this.name = name;
		photoList = new ArrayList<>();
	}
	
	public boolean addPhoto(Photo newPhoto) {
		
		for (Photo currPhoto: photoList) {
			if (newPhoto.equals(currPhoto))
				return false;
		}
		
		if (firstDate.after(newPhoto.getDate()))
			firstDate = newPhoto.getDate();
		if (lastDate.before(newPhoto.getDate()))
			lastDate = newPhoto.getDate();

		photoList.add(newPhoto);
		
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
