package controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.Album;
import models.User;

public class AlbumViewController {
	
	@FXML
	Text albumName;
	
	@FXML
	Button logout, home, addPhoto, slideshow;
	
	User currentUser;
	Album currentAlbum;

	public void start(User currentUser, Album currentAlbum) {
		
		System.out.println(currentUser +" "+ currentAlbum);
		this.currentUser = currentUser;
		this.currentAlbum = currentAlbum;
		
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
