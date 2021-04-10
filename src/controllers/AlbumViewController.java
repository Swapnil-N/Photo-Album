package controllers;

import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import models.Album;
import models.Photo;
import models.User;

public class AlbumViewController {
	
	@FXML
	Text albumName;
	
	@FXML
	Button logout, home, addPhoto, slideshow;
	
	User currentUser;
	Album currentAlbum;

	public void start(User currentUser, Album currentAlbum) {
		
		//System.out.println(currentUser +" "+ currentAlbum);
		this.currentUser = currentUser;
		this.currentAlbum = currentAlbum;
		albumName.setText(currentAlbum.getName());
		
	}
		
	public void onActionAddPhoto(ActionEvent e) {
		        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose an image");
        
        fileChooser.getExtensionFilters().add(
        		new ExtensionFilter("Image Files", "*.png", "*.jpeg", "*.jpg", "*.gif", "*.bmp"));
        
        Node node = (Node) e.getSource();
		Stage primaryStage = (Stage) node.getScene().getWindow();
        File selectedPhoto = fileChooser.showOpenDialog(primaryStage);
        
        if (selectedPhoto != null) {
        	
        	if (currentAlbum.addPhoto(new Photo(selectedPhoto))) {
        		//refresh tile pane
        	}
        	else {
        		invalidPhotoAlert();
        	}
        }
        		
	}

	private void invalidPhotoAlert() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Add Photo Failed");
		alert.setContentText("This photo is already in the album");
		alert.showAndWait();
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
	
	public void onActionHome(ActionEvent e) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/userHome.fxml"));
		AnchorPane root = (AnchorPane) loader.load();

		Node node = (Node) e.getSource();
		Stage primaryStage = (Stage) node.getScene().getWindow();
		
		UserHomeController userLandingController = loader.getController();
		userLandingController.setCurrentUser(currentUser);
		userLandingController.loadAlbums();
		
		Scene scene = new Scene(root, 1000, 750);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Home Screen");
		primaryStage.setResizable(false);
		primaryStage.show();
	}

}
