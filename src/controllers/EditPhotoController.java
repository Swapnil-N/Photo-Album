package controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.Photo;
import models.User;

public class EditPhotoController {
	
	User currentUser;
	Photo currentPhoto;
	
	public void start(User currentUser, Photo currentPhoto) {
		this.currentUser = currentUser;
		this.currentPhoto = currentPhoto;
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
	
	
	public void onActionMove(ActionEvent e) {
		System.out.println("move");
	}
	public void onActionCopy(ActionEvent e) {
		System.out.println("copy");
	}
	
	public void onActionCancel(ActionEvent e) {
		System.out.println("cancel");
	}
	public void onActionSave(ActionEvent e) {
		System.out.println("save");
	}
	
	public void onActionAdd(ActionEvent e) {
		System.out.println("add");
	}
	
	
}
