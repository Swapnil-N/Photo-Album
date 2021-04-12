package models;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Represents an album
 * 
 * @author Swapnil Napuri
 * @author Srinandini Marpaka
 */

public class Album implements Serializable {

	/**
	 * Identifier used to serialize/deserialize an object
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Name of the album
	 */
	private String name;

	/**
	 * List of photos in the album
	 */
	private List<Photo> photoList;

	/**
	 * Earliest date that a photo in the album was taken
	 */
	private Date firstDate;

	/**
	 * Latest date that a photo in the album was taken
	 */
	private Date lastDate;

	/**
	 * Initializes an album
	 * 
	 * @param name name of the album
	 */
	public Album(String name) {
		this.name = name;
		photoList = new ArrayList<>();
		firstDate = null;
		lastDate = null;
	}

	/**
	 * Adds photo to the album
	 * 
	 * @param newPhoto photo to be added
	 * @return boolean true if photo was successfully added; false otherwise
	 */
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

	/**
	 * Deletes photo from the album
	 * 
	 * @param photo photo to be deleted
	 * @return boolean true if photo was successfully deleted; false otherwise
	 */
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

	/**
	 * Formats a date into a string
	 * 
	 * @param date date to be formatted
	 * @return String formatted date
	 */
	private String formatDate(Date date) {
		DateFormat format = new SimpleDateFormat("MM.d.yyyy", Locale.ENGLISH);
		String formattedDate = format.format(date);
		return formattedDate;
	}

	/**
	 * Returns the album name
	 * 
	 * @return String album name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the album name
	 * 
	 * @param name new album name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns a list of photos in the album
	 * 
	 * @return List<Photo> list of photos in the album
	 */
	public List<Photo> getPhotoList() {
		return photoList;
	}

	/**
	 * Returns the size of the album
	 * 
	 * @return int size of the album
	 */
	public int getSize() {
		return photoList.size();
	}

	/**
	 * Returns the earliest date of photos in the album
	 * 
	 * @return String formatted earliest date
	 */
	public String getFirstDate() {
		if (firstDate == null)
			return "";
		return formatDate(firstDate);
	}

	/**
	 * Returns the latest date of photos in the album
	 * 
	 * @return String formatted latest date
	 */
	public String getLastDate() {
		if (lastDate == null)
			return "";
		return formatDate(lastDate);
	}

}
