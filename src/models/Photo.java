package models;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Photo {
	
	private String name;
	private String caption;
	private File file;
	private Date date;
	private HashMap<String, List<String>> tags;
	
	public Photo(File file, Album album) {
		name = file.getName();
		this.file = file;
		date = new Date(file.lastModified());
		tags = new HashMap<>();
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
