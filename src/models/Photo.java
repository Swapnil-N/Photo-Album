package models;

import java.io.File;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Represents a photo
 * 
 * @author Swapnil Napuri
 * @author Srinandini Marpaka
 */

public class Photo implements Serializable {

	/**
	 * Identifier used to serialize/deserialize an object
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Name of the photo
	 */
	private String name;

	/**
	 * Caption of the photo
	 */
	private String caption;

	/**
	 * Location of the photo on the disk
	 */
	private File file;

	/**
	 * Date-time of the photo capture
	 */
	private Date date;

	/**
	 * List of tags associated with photo
	 */
	private Map<String, List<String>> tags;

	/**
	 * Initializes an album
	 * 
	 * @param file location of the photo on the disk
	 */
	public Photo(File file) {
		this.file = file;
		name = file.getName();
		date = new Date(file.lastModified());
		tags = new HashMap<>();
		caption = "N/A";
	}

	/**
	 * Indicates whether some other object is "equal to" this photo
	 * 
	 * @param obj the reference object with which to compare
	 * @return boolean true if the same photo; false otherwise
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Photo))
			return false;

		return file.equals(((Photo) obj).getFile());
	}

	/**
	 * Adds a tag-value pair to the photo's tags
	 * 
	 * @param key   tag of the pair
	 * @param value value of the pair
	 * @return boolean true if tag was successfully added; false otherwise
	 */
	public boolean addTag(String key, String value) {
		key = key.trim();
		value = value.trim();

		String storedKey = key;
		boolean exists = false;

		for (String existingKey : tags.keySet()) {
			if (key.toLowerCase().equals(existingKey.toLowerCase())) {
				exists = true;
				storedKey = existingKey;
				break;
			}
		}

		if (!exists)
			tags.put(storedKey, new ArrayList<>());

		exists = false;

		for (String existingKey : tags.keySet()) {
			if (key.toLowerCase().equals(existingKey.toLowerCase())) {
				for (String existingValue : tags.get(existingKey)) {
					if (value.toLowerCase().equals(existingValue.toLowerCase())) {
						UserList.serialize();
						return false;
					}
				}
			}
		}

		tags.get(storedKey).add(value);

		UserList.serialize();
		return true;
	}

	/**
	 * Deletes a tag-value pair from the photo's tags
	 * 
	 * @param key   tag of the pair
	 * @param value value of the pair
	 * @return boolean true if tag was successfully deleted; false otherwise
	 */
	public boolean removeTag(String key, String value) {
		key = key.trim();
		value = value.trim();

		for (String existingKey : tags.keySet()) {
			if (key.toLowerCase().equals(existingKey.toLowerCase())) {
				for (int i = 0; i < tags.get(existingKey).size(); i++) {
					String existingValue = tags.get(existingKey).get(i);
					if (value.toLowerCase().equals(existingValue.toLowerCase())) {
						tags.get(existingKey).remove(i);

						UserList.serialize();
						return true;
					}
				}
			}
		}

		return false;
	}

	/**
	 * Returns the photo name
	 * 
	 * @return String photo name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the photo name
	 * 
	 * @param name new photo name
	 */
	public void setName(String name) {
		this.name = name;
		UserList.serialize();
	}

	/**
	 * Returns the photo caption
	 * 
	 * @return String photo caption
	 */
	public String getCaption() {
		return caption;
	}

	/**
	 * Sets the photo caption
	 * 
	 * @param caption new photo caption
	 */
	public void setCaption(String caption) {
		this.caption = caption;
		UserList.serialize();
	}

	/**
	 * Returns the photo URL
	 * 
	 * @return String photo URL
	 */
	public String getPhotoURL() {
		try {
			return file.toURI().toURL().toString();
		} catch (MalformedURLException e) {
			return null;
		}
	}

	/**
	 * Returns the photo file
	 * 
	 * @return File photo file
	 */
	public File getFile() {
		return file;
	}

	/**
	 * Returns the date-time of capture
	 * 
	 * @return Date date-time of capture
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Returns the date of capture formatted as a string
	 * 
	 * @return String date of capture
	 */
	public String getDateString() {
		DateFormat format = new SimpleDateFormat("MM.d.yyyy", Locale.ENGLISH);
		String dateFormatted = format.format(date);
		return dateFormatted;
	}

	/**
	 * Returns the date-time of capture formatted as a string
	 * 
	 * @return String date-time of capture
	 */
	public String getDateTimeString() {
		DateFormat format = new SimpleDateFormat("MM.d.yyyy HH:mm", Locale.ENGLISH);
		String dateFormatted = format.format(date);
		return dateFormatted;
	}

	/**
	 * Returns the tag-value pairs associated with the photo
	 * 
	 * @return Map<String, List<String>> tag-value pairs associated with photo
	 */
	public Map<String, List<String>> getTags() {
		return tags;
	}

}
