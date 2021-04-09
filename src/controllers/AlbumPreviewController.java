package controllers;

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
	
	public void setAlbum(Album album) {
		//TODO: update with image from album
//		Image image = new Image("https://www.google.com/url?sa=i&url=https%3A%2F%2Funsplash.com%2Fimages%2Fnature%2Fsunset&psig=AOvVaw3sYhZWWxC_ieayE98kZKrH&ust=1617944658552000&source=images&cd=vfe&ved=0CAIQjRxqFwoTCLjA5dbv7e8CFQAAAAAdAAAAABAD");
//		imageView = new ImageView();
//		imageView.setImage(image);
		
		albumName.setText(album.getName());
		photoCount.setText(album.getSize() + "");
		
		if (!album.getFirstDate().isEmpty())
			dateRange.setText(album.getFirstDate() + " - " + album.getLastDate());
		
		System.out.println(albumName.getText());
		System.out.println(photoCount.getText());
		System.out.println(dateRange.getText());
	}
	
	// add button onclick
	
}
