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

public class Photo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private String caption;
	private File file;
	private Date date;
	private HashMap<String, List<String>> tags;

	public Photo(File file) {
		this.file = file;
		name = file.getName();
		date = new Date(file.lastModified());
		tags = new HashMap<>();
		caption = "N/A";
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Photo))
			return false;

		return file.equals(((Photo) obj).getFile());
	}
	
	public boolean addTag(String key, String value) {
		key = key.trim().toLowerCase();
		value = value.trim();
		
		if (!tags.containsKey(key)){
			tags.put(key, new ArrayList<>());
		}
		
		if (tags.get(key).contains(value)) {
			UserList.serialize();
			return false;
		}
		
		tags.get(key).add(value);
		UserList.serialize();
		
		return true;
	}
	
	public boolean removeTag(String key, String value) {
		key = key.trim().toLowerCase();
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}
	
	public String getPhotoURL() {
		try {
			return file.toURI().toURL().toString();
		} catch (MalformedURLException e) {
			return null;
		}
	}

	public File getFile() {
		return file;
	}

	public Date getDate() {
		return date;
	}
	
	public String getDateString() {
		DateFormat format = new SimpleDateFormat("MM.d.yyyy", Locale.ENGLISH);
		String dateFormatted = format.format(date);
		return dateFormatted;
	}
	
	public String getDateTimeString() {
		DateFormat format = new SimpleDateFormat("MM.d.yyyy hh:mm:ss", Locale.ENGLISH);
		String dateFormatted = format.format(date);
		return dateFormatted;
	}

	public HashMap<String, List<String>> getTags() {
		return tags;
	}

}
