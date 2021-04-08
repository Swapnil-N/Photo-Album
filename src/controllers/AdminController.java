package controllers;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.util.List;
import models.User;
import models.UserList;

public class AdminController {
	
	@FXML
	ListView<String> listView;
	@FXML
	TextField newUsername;
	@FXML
	Button addUser, logout;
	
	private ObservableList<String> obsList;
	
	public void start(Stage primaryStage) {
		obsList = FXCollections.observableArrayList();
		List<User> users = new UserList().getUsers();
		
		for (int i = 0; i < users.size(); i++) {
			obsList.add(users.get(i).getUsername());
		}
		listView.setItems(obsList);
		
	}
	
	public void onActionAddUser (ActionEvent e) throws IOException {
		String usernameInput = newUsername.getText().trim();
		if (usernameInput.equals("admin")) {
			invalidUsernameAlert();
		} else {
			UserList userList = new UserList();
			User user = new User(usernameInput);

			if (userList.containsUser(user) || usernameInput.equals("")) {
				invalidUsernameAlert();
			} else {
				boolean success = userList.addUser(user);
				if (success) {
					newUsername.setText("");
				} else {
					invalidUsernameAlert();
				}
			}
		}
	}
	
	public void invalidUsernameAlert() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Add User Failed");
		alert.setContentText("That username is not available.");
		alert.showAndWait();
		newUsername.setText("");
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
