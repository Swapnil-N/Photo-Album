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

public class Photo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private String caption;
	private File file;
	private Date date;
	private Map<String, List<String>> tags;

	public Photo(File file) {
		this.file = file;
		name = file.getName();
		date = new Date(file.lastModified());
		tags = new HashMap<>();
		caption = "N/A";
	}

	
	/** 
	 * @param obj
	 * @return boolean
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Photo))
			return false;

		return file.equals(((Photo) obj).getFile());
	}

	
	/** 
	 * @param key
	 * @param value
	 * @return boolean
	 */
	public boolean addTag(String key, String value) {
		key = key.trim();
		value = value.trim();

		if (!tags.containsKey(key))
			tags.put(key, new ArrayList<>());

		if (tags.get(key).contains(value)) {
			UserList.serialize();
			return false;
		}

		tags.get(key).add(value);

		UserList.serialize();
		return true;
	}

	
	/** 
	 * @param key
	 * @param value
	 * @return boolean
	 */
	public boolean removeTag(String key, String value) {
		key = key.trim();
		value = value.trim();

		if (!tags.containsKey(key))
			return false;

		for (int i = 0; i < tags.get(key).size(); i++) {
			if (tags.get(key).get(i).equals(value)) {
				tags.get(key).remove(i);

				UserList.serialize();
				return true;
			}
		}

		return false;
	}

	
	/** 
	 * @return String
	 */
	public String getName() {
		return name;
	}

	
	/** 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	
	/** 
	 * @return String
	 */
	public String getCaption() {
		return caption;
	}

	
	/** 
	 * @param caption
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}

	
	/** 
	 * @return String
	 */
	public String getPhotoURL() {
		try {
			return file.toURI().toURL().toString();
		} catch (MalformedURLException e) {
			return null;
		}
	}

	
	/** 
	 * @return File
	 */
	public File getFile() {
		return file;
	}

	
	/** 
	 * @return Date
	 */
	public Date getDate() {
		return date;
	}

	
	/** 
	 * @return String
	 */
	public String getDateString() {
		DateFormat format = new SimpleDateFormat("MM.d.yyyy", Locale.ENGLISH);
		String dateFormatted = format.format(date);
		return dateFormatted;
	}

	
	/** 
	 * @return String
	 */
	public String getDateTimeString() {
		DateFormat format = new SimpleDateFormat("MM.d.yyyy hh:mm:ss", Locale.ENGLISH);
		String dateFormatted = format.format(date);
		return dateFormatted;
	}

	
	/** 
	 * @return Map<String, List<String>>
	 */
	public Map<String, List<String>> getTags() {
		return tags;
	}

}
