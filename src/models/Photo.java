package models;

import java.io.File;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Photo))
			return false;

		return file.equals(((Photo) obj).getFile());
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

	public void setFile(File file) {
		this.file = file;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public HashMap<String, List<String>> getTags() {
		return tags;
	}

	public void setTags(HashMap<String, List<String>> tags) {
		this.tags = tags;
	}

}
