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
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Album;
import models.User;

public class UserHomeController {

	@FXML
	Button logout, search, newAlbum;
	@FXML
	TilePane tilepane;

	private User currentUser;

	public void start(User currentUser) throws IOException {
		this.currentUser = currentUser;

		loadAlbums();
	}

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

	public void deleteAlbum(String albumName) throws IOException {
		currentUser.deleteAlbum(albumName);

		loadAlbums();
	}

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

	private void invalidAlbumNameAlert() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Add Album Failed");
		alert.setContentText("That album name is not available.");
		alert.showAndWait();
	}

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
