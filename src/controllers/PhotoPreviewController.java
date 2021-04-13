package controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.Album;
import models.Photo;
import models.User;

/**
 * Handles each photo's display in the album view
 * 
 * @author Swapnil Napuri
 * @author Srinandini Marpaka
 */

public class PhotoPreviewController {

	/**
	 * Container that holds photo picture
	 */
	@FXML
	ImageView imageView;

	/**
	 * Container that holds photo name
	 */
	@FXML
	Text photoName;

	/**
	 * Container that holds photo caption
	 */
	@FXML
	Text caption;

	/**
	 * Photo to be displayed
	 */
	private Photo currentPhoto;

	/**
	 * Album photo belongs to
	 */
	private Album currentAlbum;

	/**
	 * User logged in
	 */
	private User currentUser;

	/**
	 * Controller that displays all the album's photos
	 */
	private AlbumViewController albumViewController;

	/**
	 * Sets up the container displaying a photo on the 'Album' screen
	 * 
	 * @param currentAlbum        album photo belongs to
	 * @param currentPhoto        photo to be displayed
	 * @param currentUser         user logged in
	 * @param albumViewController controller that displays all the album's photos
	 */
	public void start(Album currentAlbum, Photo currentPhoto, User currentUser,
			AlbumViewController albumViewController) {
		this.currentPhoto = currentPhoto;
		this.currentAlbum = currentAlbum;
		this.currentUser = currentUser;
		this.albumViewController = albumViewController;

		photoName.setText(currentPhoto.getName());
		caption.setText(currentPhoto.getCaption());
		imageView.setImage(new Image(currentPhoto.getPhotoURL()));
	}

	/**
	 * Deletes a photo from an album
	 * 
	 * @param e represents that the 'Delete' button has been clicked
	 * @throws IOException if photo preview file is not found
	 */
	public void onActionDelete(ActionEvent e) throws IOException {
		currentAlbum.deletePhoto(currentPhoto);
		albumViewController.loadAlbum();
	}

	/**
	 * Takes the user to the edit photo screen
	 * 
	 * @param e represents that the photo's picture has been clicked
	 * @throws IOException if edit photo screen file is not found
	 */
	public void imageViewMouseClicked(MouseEvent e) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/editPhoto.fxml"));
		AnchorPane root = (AnchorPane) loader.load();

		Node node = (Node) e.getSource();
		Stage primaryStage = (Stage) node.getScene().getWindow();

		EditPhotoController editPhotoController = loader.getController();
		editPhotoController.start(currentUser, currentAlbum, currentPhoto);

		Scene scene = new Scene(root, 1000, 750);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Edit Photo");
		primaryStage.setResizable(false);
		primaryStage.show();
	}

}
