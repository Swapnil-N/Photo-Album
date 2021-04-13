package controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import models.Photo;

/**
 * Handles each search result's display
 * 
 * @author Swapnil Napuri
 * @author Srinandini Marpaka
 */

public class SearchPhotoPreviewController {

	/**
	 * Container that stores the picture of a photo
	 */
	@FXML
	ImageView imageView;

	/**
	 * Container that stores the name of a photo
	 */
	@FXML
	Text photoName;

	/**
	 * Container that stores the caption of a photo
	 */
	@FXML
	Text caption;

	/**
	 * Sets up the container displaying a photo on the 'Search' screen
	 * 
	 * @param currentPhoto photo to be displayed
	 */
	public void start(Photo currentPhoto) {
		photoName.setText(currentPhoto.getName());
		caption.setText(currentPhoto.getCaption());
		imageView.setImage(new Image(currentPhoto.getPhotoURL()));
	}

}
