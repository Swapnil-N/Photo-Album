package controllers;

import java.io.IOException;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.Album;
import models.User;

/**
 * Handles each album's display on the user home page
 * 
 * @author Swapnil Napuri
 * @author Srinandini Marpaka
 */

public class AlbumPreviewController {

	/**
	 * Component that holds the album's cover photo
	 */
	@FXML
	ImageView imageView;

	/**
	 * Component that holds the album's name
	 */
	@FXML
	Text albumName;

	/**
	 * Component that holds the size of the album
	 */
	@FXML
	Text photoCount;

	/**
	 * Component that holds the date range of the album's photos
	 */
	@FXML
	Text dateRange;

	/**
	 * Container that holds the entire object
	 */
	@FXML
	VBox container;

	/**
	 * Album to be displayed
	 */
	private Album currentAlbum;

	/**
	 * User logged in
	 */
	private User currentUser;

	/**
	 * Controller that displays all the user's albums
	 */
	private UserHomeController userHomeController;

	/**
	 * Sets up the container displaying a album on the 'Home' screen
	 * 
	 * @param currentUser        user logged in
	 * @param currentAlbum       album to be displayed
	 * @param userHomeController controller that displays all the user's albums
	 */
	public void start(User currentUser, Album currentAlbum, UserHomeController userHomeController) {
		this.currentUser = currentUser;
		this.currentAlbum = currentAlbum;
		this.userHomeController = userHomeController;

		container.setId(currentAlbum.getName());

		if (currentAlbum.getSize() > 0)
			imageView.setImage(new Image(currentAlbum.getPhotoList().get(0).getPhotoURL()));

		albumName.setText(currentAlbum.getName());
		photoCount.setText(currentAlbum.getSize() + "");

		if (!currentAlbum.getFirstDate().isEmpty())
			dateRange.setText(currentAlbum.getFirstDate() + " - " + currentAlbum.getLastDate());
		else
			dateRange.setText("N/A");
	}

	/**
	 * Takes the user to the album view
	 * 
	 * @param e represents that the album's cover has been clicked
	 * @throws IOException if album screen file is not found
	 */
	public void imageViewMouseClicked(MouseEvent e) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/album.fxml"));
		AnchorPane root = (AnchorPane) loader.load();

		Node node = (Node) e.getSource();
		Stage primaryStage = (Stage) node.getScene().getWindow();

		AlbumViewController albumViewController = loader.getController();
		albumViewController.start(currentUser, currentAlbum);

		Scene scene = new Scene(root, 1000, 750);

		primaryStage.setScene(scene);
		primaryStage.setTitle("Home Screen");
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	/**
	 * Renames the album based on user's input
	 * 
	 * @param e represents that the 'Delete' button has been clicked
	 */
	public void onActionRename(ActionEvent e) {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle(" ");
		dialog.setHeaderText("Enter New Album Name");

		Optional<String> opt = dialog.showAndWait();

		if (opt.isEmpty())
			return;

		String newName = opt.get().trim();
		if (newName.length() == 0) {
			invalidAlbumAlert();
			return;
		}

		if (currentUser.hasAlbumWithName(newName)) {
			invalidAlbumAlert();
		} else {
			currentAlbum.setName(newName);
			albumName.setText(currentAlbum.getName());
			container.setId(newName);
		}
	}

	/**
	 * Informs the user that he/she entered an invalid album name
	 */
	private void invalidAlbumAlert() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Add Album Failed");
		alert.setContentText("That album name is not available.");
		alert.showAndWait();
	}

	/**
	 * Deletes the album from the user's albums
	 * 
	 * @param e represents that the 'Delete' button has been clicked
	 * @throws IOException if album preview file is not found
	 */
	public void onActionDelete(ActionEvent e) throws IOException {
		userHomeController.deleteAlbum(currentAlbum.getName());
	}

}
