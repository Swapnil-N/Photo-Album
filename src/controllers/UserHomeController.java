package controllers;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Album;
import models.User;

/**
 * Handles the user landing page
 * 
 * @author Swapnil Napuri
 * @author Srinandini Marpaka
 */

public class UserHomeController {

	/**
	 * Component that displays all of the user's albums
	 */
	@FXML
	TilePane tilepane;

	/**
	 * User logged in
	 */
	private User currentUser;

	/**
	 * Sets up the 'Home' screen
	 * 
	 * @param currentUser user logged in
	 * @throws IOException if album preview file is not found
	 */
	public void start(User currentUser) throws IOException {
		this.currentUser = currentUser;

		loadAlbums();
	}

	/**
	 * Loads the TilePane with the user's albums
	 * 
	 * @throws IOException if album preview file is not found
	 */
	private void loadAlbums() throws IOException {
		tilepane.getChildren().clear();

		List<Album> currentAlbums = currentUser.getAlbums();
		for (int i = 0; i < currentAlbums.size(); i++) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/albumPreview.fxml"));
			VBox root = (VBox) loader.load();

			AlbumPreviewController albumPreviewController = loader.getController();
			albumPreviewController.start(currentUser, currentAlbums.get(i), this);

			tilepane.getChildren().add(root);
		}
	}

	/**
	 * Deletes an album from the user's albums
	 * 
	 * @param albumName name of the album to be deleted
	 * @throws IOException if album preview file is not found
	 */
	public void deleteAlbum(String albumName) throws IOException {
		currentUser.deleteAlbum(albumName);

		loadAlbums();
	}

	/**
	 * Adds an album to the user's albums
	 * 
	 * @param e represents that the 'Create New Album' button has been clicked
	 * @throws IOException if album preview file is not found
	 */
	public void onActionAddAlbum(ActionEvent e) throws IOException {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle(" ");
		dialog.setHeaderText("Enter New Album Name");

		Optional<String> opt = dialog.showAndWait();

		if (opt.isEmpty())
			return;

		String albumName = opt.get().trim();
		if (albumName.length() == 0) {
			invalidAlbumNameAlert();
			return;
		}

		if (!currentUser.addAlbum(albumName)) {
			invalidAlbumNameAlert();
		} else {
			loadAlbums();
		}
	}

	/**
	 * Informs the user that he/she entered an invalid album name
	 */
	private void invalidAlbumNameAlert() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Add Album Failed");
		alert.setContentText("That album name is not available.");
		alert.showAndWait();
	}

	/**
	 * Logs the user out and takes him/her to the login screen
	 * 
	 * @param e represents that the 'Logout' button has been clicked
	 * @throws IOException if login screen file is not found
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
	 * Takes the user to the search screen
	 * 
	 * @param e represents that the 'Search' button has been clicked
	 * @throws IOException if search screen file is not found
	 */
	public void onActionSearch(ActionEvent e) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/search.fxml"));
		AnchorPane root = (AnchorPane) loader.load();

		Node node = (Node) e.getSource();
		Stage primaryStage = (Stage) node.getScene().getWindow();

		SearchController searchController = loader.getController();
		searchController.start(currentUser);

		Scene scene = new Scene(root, 1000, 750);

		primaryStage.setScene(scene);
		primaryStage.setTitle("Login Page");
		primaryStage.setResizable(false);
		primaryStage.show();
	}

}
