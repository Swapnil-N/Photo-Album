package controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class SlideshowController {

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
