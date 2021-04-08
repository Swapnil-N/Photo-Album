package controllers;

import javafx.scene.Node;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import models.User;
import models.UserList;
import controllers.AdminController;

public class LoginController {

	@FXML
	TextField username;

	public void onActionLogin(ActionEvent e) throws IOException {
		String usernameInput = username.getText().trim();

		if (usernameInput.equals("admin")) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/admin.fxml"));
			AnchorPane root = (AnchorPane) loader.load();

			Node node = (Node) e.getSource();
			Stage primaryStage = (Stage) node.getScene().getWindow();

			AdminController listController = loader.getController();
			listController.start(primaryStage);

			Scene scene = new Scene(root, 1000, 750);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Admin Control");
			primaryStage.setResizable(false);
			primaryStage.show();
		} else {
			UserList userList = new UserList();
			User user = new User(usernameInput);

			if (!userList.containsUser(user)) {
				invalidUsernameAlert();
			} else {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/userLandingScreen.fxml"));
				AnchorPane root = (AnchorPane) loader.load();

				Node node = (Node) e.getSource();
				Stage primaryStage = (Stage) node.getScene().getWindow();
				Scene scene = new Scene(root, 1000, 750);
				primaryStage.setScene(scene);
				primaryStage.setTitle("Home Screen");
				primaryStage.setResizable(false);
				primaryStage.show();
			}
		}

	}

	public void invalidUsernameAlert() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Login Failed");
		alert.setContentText("Please enter a valid username.");
		alert.showAndWait();
		username.setText("");
	}
}
