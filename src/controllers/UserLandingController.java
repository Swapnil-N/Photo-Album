package controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UserLandingController {
	
	@FXML
	Button logout, search, newAlbum;
	
	@FXML
	TilePane tilepane;
	
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
	

}
