package controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import models.Photo;
import models.User;

public class SearchPhotoPreviewController {
	
	@FXML
	ImageView imageView;
	
	@FXML
	Text photoName, caption;
	
	Photo currentPhoto;
	User currentUser;
	
	AlbumViewController albumViewController;
	
	public void start(Photo currentPhoto, User currentUser) {
		this.currentPhoto = currentPhoto;
		this.currentUser = currentUser;
		
		photoName.setText(currentPhoto.getName());
		caption.setText(currentPhoto.getCaption());
		imageView.setImage(new Image(currentPhoto.getPhotoURL()));
	}

}
