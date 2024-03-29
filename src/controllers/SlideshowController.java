package controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.Album;
import models.User;

/**
 * Handles the slideshow page
 * 
 * @author Swapnil Napuri
 * @author Srinandini Marpaka
 */

public class SlideshowController {

	/**
	 * Picture of the photo being displayed
	 */
	@FXML
	ImageView imageView;

	/**
	 * Name of the photo being displayed
	 */
	@FXML
	Label photoName;

	/**
	 * User logged in
	 */
	private User currentUser;

	/**
	 * Album to be displayed
	 */
	private Album currentAlbum;

	/**
	 * Represents position in the album
	 */
	private int iter;

	/**
	 * Sets up the 'Slideshow' screen
	 * 
	 * @param currentUser  user logged in
	 * @param currentAlbum album to be displayed
	 */
	public void start(User currentUser, Album currentAlbum) {
		this.currentUser = currentUser;
		this.currentAlbum = currentAlbum;
		iter = 0;

		if (currentAlbum.getSize() > 0) {
			imageView.setImage(new Image(currentAlbum.getPhotoList().get(iter).getPhotoURL()));
			photoName.setText(currentAlbum.getPhotoList().get(iter).getName());
		} else {
			photoName.setText("No Image Available");
		}

	}

	/**
	 * Moves the slideshow to the previous image of the album
	 * 
	 * @param e represents that the 'Previous' button has been clicked
	 */
	public void onActionPrevious(ActionEvent e) {
		iter--;

		if (iter < 0)
			iter = currentAlbum.getSize() - 1;

		if (currentAlbum.getSize() > 0) {
			imageView.setImage(new Image(currentAlbum.getPhotoList().get(iter).getPhotoURL()));
			photoName.setText(currentAlbum.getPhotoList().get(iter).getName());
		}

	}

	/**
	 * Moves the slideshow to the next image of the album
	 * 
	 * @param e represents that the 'Next' button has been clicked
	 */
	public void onActionNext(ActionEvent e) {
		iter++;

		if (iter >= currentAlbum.getSize())
			iter = 0;

		if (currentAlbum.getSize() > 0) {
			imageView.setImage(new Image(currentAlbum.getPhotoList().get(iter).getPhotoURL()));
			photoName.setText(currentAlbum.getPhotoList().get(iter).getName());
		}
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
		primaryStage.setTitle("Login Page");
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

}
