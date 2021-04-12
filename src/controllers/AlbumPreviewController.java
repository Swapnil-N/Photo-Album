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
import javafx.scene.control.MenuButton;
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

public class AlbumPreviewController {

	@FXML
	ImageView imageView;
	@FXML
	MenuButton settings;
	@FXML
	Text albumName, photoCount, dateRange;
	@FXML
	VBox container;

	private Album currentAlbum;
	private User currentUser;
	private UserHomeController userHomeController;

	
	/** 
	 * @param currentUser
	 * @param currentAlbum
	 * @param userHomeController
	 */
	public void start(User currentUser, Album currentAlbum, UserHomeController userHomeController) {
		this.currentUser = currentUser;
		this.currentAlbum = currentAlbum;
		this.userHomeController = userHomeController;

		container.setId(currentAlbum.getName());

		setAlbum();
	}

	public void setAlbum() {
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
	 * @param e
	 * @throws IOException
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
	 * @param e
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

	private void invalidAlbumAlert() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Add Album Failed");
		alert.setContentText("That album name is not available.");
		alert.showAndWait();
	}

	
	/** 
	 * @param e
	 * @throws IOException
	 */
	public void onActionDelete(ActionEvent e) throws IOException {
		userHomeController.deleteAlbum(currentAlbum.getName());
	}
	
}
