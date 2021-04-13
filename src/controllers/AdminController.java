package controllers;

import java.io.IOException;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import models.User;
import models.UserList;

/**
 * Handles the admin page
 * 
 * @author Swapnil Napuri
 * @author Srinandini Marpaka
 */

public class AdminController {

	/**
	 * Nested class used to control cells in the tags ListView
	 */
	private static class UserItem extends ListCell<String> {
		private HBox hBox = new HBox();
		private Label username = new Label("");
		private Pane blank = new Pane();
		private Button delete = new Button("Delete");

		/**
		 * Initializes a cell in the ListView
		 */
		public UserItem() {
			super();

			HBox.setHgrow(blank, Priority.ALWAYS);
			hBox.setAlignment(Pos.CENTER);
			hBox.getChildren().addAll(username, blank, delete);

			delete.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					boolean success = UserList.deleteUser(username.getText());

					if (success)
						getListView().getItems().remove(getItem());
				}
			});

			setStyle();
		}

		/**
		 * Updates the ListView cell with a user's username
		 * 
		 * @param item  new string to update items
		 * @param empty true if empty, false otherwise
		 */
		@Override
		protected void updateItem(String item, boolean empty) {
			super.updateItem(item, empty);

			setText(null);
			setGraphic(null);

			if (item != null && !empty) {
				username.setText(item);
				setGraphic(hBox);
			}

			setStyle();
		}

		/**
		 * Sets the components' font and font size
		 */
		private void setStyle() {
			username.setFont(Font.font(16));
			delete.setFont(Font.font(15));
			setStyle("-fx-font-family: AppleGothic;");
		}
	}

	/**
	 * Component through which the admin views and deletes existing users
	 */
	@FXML
	ListView<String> listView;

	/**
	 * Component in which admin enters the username of the new user
	 */
	@FXML
	TextField newUsername;

	/**
	 * Allows the ListView to track changes in the user list as they occur
	 */
	private ObservableList<String> obsList;

	/**
	 * Sets up the 'Admin Control' screen
	 */
	public void start() {
		obsList = FXCollections.observableArrayList();

		List<User> users = UserList.getUsers();
		for (int i = 0; i < users.size(); i++) {
			obsList.add(users.get(i).getUsername());
		}

		listView.setItems(obsList);
		listView.setCellFactory(userDisplay -> new UserItem());
	}

	/**
	 * Adds an user to the application
	 * 
	 * @param e represents that the 'Add User' button has been clicked
	 */
	public void onActionAddUser(ActionEvent e) {
		String usernameInput = newUsername.getText().trim();
		if (usernameInput.equals("admin")) {
			invalidUsernameAlert();
		} else {
			if (UserList.addUser(usernameInput)) {
				newUsername.setText("");
				obsList.add(usernameInput);
			} else {
				invalidUsernameAlert();
			}
		}
	}

	/**
	 * Informs the admin that he/she entered an invalid username
	 */
	public void invalidUsernameAlert() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Add User Failed");
		alert.setContentText("That username is not available.");
		alert.showAndWait();

		newUsername.setText("");
	}

	/**
	 * Logs the admin out and takes him/her to the login screen
	 * 
	 * @param e represents that the 'Logout' button has been clicked
	 * @throws IOException if login screen file is not found
	 */
	public void onActionLogout(ActionEvent e) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
		AnchorPane root = (AnchorPane) loader.load();

		Node node = (Node) e.getSource();
		Stage primaryStage = (Stage) node.getScene().getWindow();

		Scene scene = new Scene(root, 1000, 750);

		primaryStage.setScene(scene);
		primaryStage.setTitle("Login Page");
		primaryStage.setResizable(false);
		primaryStage.show();
	}

}
