package controllers;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javafx.collections.ObservableList;
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
	
	User currentUser;
		
	public void loadAlbums() throws IOException {
		tilepane.getChildren().removeAll();
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/albumPreview.fxml"));
        VBox root = (VBox) loader.load();

		List<Album> currentAlbums = currentUser.getAlbums();
		if (currentAlbums.size() > 0) {
			AlbumPreviewController albumPreviewController = loader.getController();
			albumPreviewController.start(currentUser);
			albumPreviewController.setUserLandingController(this);
	        albumPreviewController.setAlbum(currentAlbums.get(currentAlbums.size()-1));
	        
	        tilepane.getChildren().add(albumPreviewController.container);
		}
	}
	
	public void deleteAlbum(String albumName) {
		ObservableList<Node> currentChildren = tilepane.getChildren();
		for (int i = 0; i < currentChildren.size(); i++) {
			if (currentChildren.get(i).getId().equals(albumName)) {
				currentChildren.remove(i);
			}
				
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
		
		Optional<String> opt = dialog.showAndWait();
		
		if (opt.isEmpty())
			return;
		
		String albumName = opt.get().trim();
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

	private void invalidAlbumAlert() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Add Album Failed");
		alert.setContentText("That album name is not available.");
		alert.showAndWait();
	}
	
	public void setCurrentUser(User user) {
		currentUser = user;
	}

}
