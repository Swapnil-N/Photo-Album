package controllers;

import java.io.IOException;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.User;

public class UserLandingController {
	
	@FXML
	Button logout, search, newAlbum;
	
	@FXML
	TilePane tilepane;
	
	User currentUser;
		
	public void start(Stage primaryStage) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/albumPreview.fxml"));
        VBox root = (VBox) loader.load();
        
        tilepane.getChildren().add(root);
        FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/view/albumPreview.fxml"));
        VBox root1 = (VBox) loader1.load();
        tilepane.getChildren().add(root1);
        FXMLLoader loade2r = new FXMLLoader(getClass().getResource("/view/albumPreview.fxml"));
        VBox root2 = (VBox) loade2r.load();
        tilepane.getChildren().add(root2);
        FXMLLoader loader12 = new FXMLLoader(getClass().getResource("/view/albumPreview.fxml"));
        VBox root12 = (VBox) loader12.load();
        tilepane.getChildren().add(root12);
		
		
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
	
	public void onActionAddAlbum(ActionEvent e){
		
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
