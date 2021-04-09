package controllers;

import java.io.IOException;
import java.util.Date;
import java.util.List;

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

public class UserLandingController {
	
	@FXML
	Button logout, search, newAlbum;
	
	@FXML
	TilePane tilepane;
	
	private User currentUser;
		
	public void loadAlbums() throws IOException {
		tilepane.getChildren().clear();
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/albumPreview.fxml"));
        VBox root = (VBox) loader.load();
        
		List<Album> currentAlbums = currentUser.getAlbums();
		for (int i = 0; i < currentAlbums.size(); i++) {
			AlbumPreviewController albumPreviewController = loader.getController();
			albumPreviewController.start();
	        albumPreviewController.setAlbum(currentAlbums.get(i));
	        
	        tilepane.getChildren().add(albumPreviewController.container);
		}
	}
	
	public void onActionLogout(ActionEvent e) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
		AnchorPane root = (AnchorPane) loader.load();
		
		Node node = (Node) e.getSource();
		Stage primaryStage = (Stage) node.getScene().getWindow();
		Scene scene = new Scene(root, 1000, 750);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Admin Control");
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	
	public void onActionAddAlbum(ActionEvent e) throws IOException{
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle(" ");
		dialog.setHeaderText("Enter New Album Name");
		String albumName = dialog.showAndWait().get().trim();
		if(albumName.length() == 0) {
			invalidAlbumAlert();
			return;
		}
		
		if (!currentUser.addAlbum(albumName)) {
			invalidAlbumAlert();
		} else {
			loadAlbums();
		}
	}

	public void invalidAlbumAlert() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Add Album Failed");
		alert.setContentText("That album name is not available.");
		alert.showAndWait();
	}
	
	public void setCurrentUser(User user) {
		currentUser = user;
	}

}
