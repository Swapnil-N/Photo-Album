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
		this.name = name;
		photoList = new ArrayList<>();
		firstDate = null;
		lastDate = null;
	}

	public boolean addPhoto(Photo newPhoto) {
		for (Photo currPhoto : photoList) {
			if (newPhoto.equals(currPhoto))
				return false;
		}

		if (firstDate == null || firstDate.after(newPhoto.getDate()))
			firstDate = newPhoto.getDate();
		if (lastDate == null || lastDate.before(newPhoto.getDate()))
			lastDate = newPhoto.getDate();

		photoList.add(newPhoto);

		UserList.serialize();
		return true;
	}

	public boolean deletePhoto(Photo photo) {
		for (int i = 0; i < photoList.size(); i++) {
			if (photo.equals(photoList.get(i))) {
				photoList.remove(i);

				if (photo.getDate().equals(firstDate)) {
					firstDate = null;
					for (Photo currPhoto : photoList) {
						if (firstDate == null || firstDate.after(currPhoto.getDate()))
							firstDate = currPhoto.getDate();
					}
				} else if (photo.getDate().equals(lastDate)) {
					lastDate = null;
					for (Photo currPhoto : photoList) {
						if (lastDate == null || lastDate.before(currPhoto.getDate()))
							lastDate = currPhoto.getDate();
					}
				}

				UserList.serialize();
				return true;
			}
		}

		return false;
	}

	private String formatDate(Date date) {
		DateFormat format = new SimpleDateFormat("MM.d.yyyy", Locale.ENGLISH);
		String formattedDate = format.format(date);
		return formattedDate;
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

	public int getSize() {
		return photoList.size();
	}

	public String getFirstDate() {
		if (firstDate == null)
			return "";
		return formatDate(firstDate);
	}

	public String getLastDate() {
		if (lastDate == null)
			return "";
		return formatDate(lastDate);
	}

	@Override
	public String toString() {
		return name;
	}

}
