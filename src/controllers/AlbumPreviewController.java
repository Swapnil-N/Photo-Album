package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import models.Album;

public class AlbumPreviewController {

	@FXML
	VBox container;
	
	@FXML
	ImageView imageView;
	
	@FXML
	Text albumName, photoCount, dateRange;
	
	@FXML
	MenuButton settings;
	
	Album album;
	
	public void start() throws FileNotFoundException {
		ImageView menuIcon = new ImageView(new Image("file:///Users/srinandinim/Documents/Coursework/software-methodology/Photo-Album/src/controllers/flower.jpg"));
	    menuIcon.setFitHeight(20);
	    menuIcon.setFitWidth(20);
	    settings.setGraphic(menuIcon);
	}
	
	public void setAlbum(Album album) {
		//TODO: update with image from album
		
		albumName.setText(album.getName());
		photoCount.setText(album.getSize() + "");
		
		if (!album.getFirstDate().isEmpty())
			dateRange.setText(album.getFirstDate() + " - " + album.getLastDate());
		else
			dateRange.setText("N/A");
		
		this.album = album;
	}
	
	public void onActionRename(ActionEvent e) {
		
	}
	
	public void onActionDelete(ActionEvent e) {
		
	}
}
