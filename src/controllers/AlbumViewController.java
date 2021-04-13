package controllers;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import models.Album;
import models.Photo;
import models.User;

/**
 * Handles the album view page
 * 
 * @author Swapnil Napuri
 * @author Srinandini Marpaka
 */

public class AlbumViewController {

	/**
	 * Component that indicates current album
	 */
	@FXML
	Text albumName;

	/**
	 * Component that holds the album photos
	 */
	@FXML
	TilePane tilepane;

	/**
	 * User logged in
	 */
	private User currentUser;

	/**
	 * Album opened
	 */
	private Album currentAlbum;

	/**
	 * Sets up the 'Album View' screen
	 * 
	 * @param currentUser  user logged in
	 * @param currentAlbum album opened
	 * @throws IOException if photo preview file is not found
	 */
	public void start(User currentUser, Album currentAlbum) throws IOException {
		this.currentUser = currentUser;
		this.currentAlbum = currentAlbum;

		albumName.setText(currentAlbum.getName());

		loadAlbum();
	}

	/**
	 * Loads the photos in the album
	 * 
	 * @throws IOException if photo preview file is not found
	 */
	public void loadAlbum() throws IOException {
		tilepane.getChildren().clear();

		Album album = currentUser.getAlbumWithName(currentAlbum.getName());
		if (album == null)
			return;

		List<Photo> albumPhotos = album.getPhotoList();
		for (int i = 0; i < albumPhotos.size(); i++) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/photoPreview.fxml"));
			VBox root = (VBox) loader.load();

			PhotoPreviewController photoPreviewController = loader.getController();
			photoPreviewController.start(album, albumPhotos.get(i), currentUser, this);

			tilepane.getChildren().add(root);
		}
	}

	/**
	 * Adds the selected photo to the album
	 * 
	 * @param e represents that the 'Add Photo' button has been clicked
	 * @throws IOException if photo preview file is not found
	 */
	public void onActionAddPhoto(ActionEvent e) throws IOException {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Choose an Image");

		fileChooser.getExtensionFilters()
				.add(new ExtensionFilter("Image Files", "*.png", "*.jpeg", "*.jpg", "*.gif", "*.bmp"));

		Node node = (Node) e.getSource();
		Stage primaryStage = (Stage) node.getScene().getWindow();
		File selectedFile = fileChooser.showOpenDialog(primaryStage);

		if (selectedFile != null) {
			Photo selectedPhoto = new Photo(selectedFile);

			for (Album iterAlbum : currentUser.getAlbums()) {
				for (Photo iterPhoto : iterAlbum.getPhotoList()) {
					if (iterPhoto.equals(selectedPhoto))
						selectedPhoto = iterPhoto;
				}
			}

			if (currentAlbum.addPhoto(selectedPhoto)) {
				loadAlbum();
			} else {
				invalidPhotoAlert();
			}
		}
	}

	/**
	 * Informs the user that he/she added a photo already in the album
	 */
	private void invalidPhotoAlert() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Add Photo Failed");
		alert.setContentText("This photo is already in the album");
		alert.showAndWait();
	}

	/**
	 * Logs the user out and takes him/her to the login screen
	 * 
	 * @param e represents that the 'Logout' button has been clicked
	 * @throws IOException if logout screen file is not found
	 */
	public void onActionLogout(ActionEvent e) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
		AnchorPane root = (AnchorPane) loader.load();

		Node node = (Node) e.getSource();
		Stage primaryStage = (Stage) node.getScene().getWindow();

		Scene scene = new Scene(root, 1000, 750);

		primaryStage.setScene(scene);
		primaryStage.setTitle("Login Screen");
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	/**
	 * Takes the user to his/her home screen
	 * 
	 * @param e represents that the 'Home' button has been clicked
	 * @throws IOException if home screen file is not found
	 */
	public void onActionHome(ActionEvent e) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/userHome.fxml"));
		AnchorPane root = (AnchorPane) loader.load();

		Node node = (Node) e.getSource();
		Stage primaryStage = (Stage) node.getScene().getWindow();

		UserHomeController userLandingController = loader.getController();
		userLandingController.start(currentUser);

		Scene scene = new Scene(root, 1000, 750);

		primaryStage.setScene(scene);
		primaryStage.setTitle("Home Screen");
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	/**
	 * Takes the user to the album slideshow screen
	 * 
	 * @param e represents that the 'Slideshow' button has been clicked
	 * @throws IOException if slideshow screen file is not found
	 */
	public void onActionSlideshow(ActionEvent e) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/slideshow.fxml"));
		AnchorPane root = (AnchorPane) loader.load();

		Node node = (Node) e.getSource();
		Stage primaryStage = (Stage) node.getScene().getWindow();

		SlideshowController slideshowController = loader.getController();
		slideshowController.start(currentUser, currentAlbum);

		Scene scene = new Scene(root, 1000, 750);

		primaryStage.setScene(scene);
		primaryStage.setTitle("Album Slideshow");
		primaryStage.setResizable(false);
		primaryStage.show();
	}

}
