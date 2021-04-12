package controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import models.Photo;

public class SearchPhotoPreviewController {

	@FXML
	ImageView imageView;
	@FXML
	Text photoName, caption;

	public void start(Photo currentPhoto) {
		photoName.setText(currentPhoto.getName());
		caption.setText(currentPhoto.getCaption());
		imageView.setImage(new Image(currentPhoto.getPhotoURL()));
	}

}
