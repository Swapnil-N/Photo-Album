package models;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Album implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String name;
	private List<Photo> photoList;
	private Date firstDate;
	private Date lastDate;

	public Album(String name) {
		// CASE SENSSITIIVE
		this.name = name;
		photoList = new ArrayList<>();
		this.firstDate = null;
		this.lastDate = null;
	}

	public boolean addPhoto(Photo newPhoto) {
		for (Photo currPhoto : photoList) {
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

	public int getSize() {
		return photoList.size();
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

	public String getFirstDate() {
		if (firstDate == null)
			return "";
		return formatDate(firstDate);
	}

	public void setFirstDate(Date firstDate) {
		this.firstDate = firstDate;
	}

	public String getLastDate() {
		if (lastDate == null)
			return "";
		return formatDate(lastDate);
	}

	public void setLastDate(Date lastDate) {
		this.lastDate = lastDate;
	}

	private String formatDate(Date date) {
		DateFormat format = new SimpleDateFormat("MM.d.yyyy", Locale.ENGLISH);
		String dateFormatted = format.format(date);
		return dateFormatted;
	}

}
