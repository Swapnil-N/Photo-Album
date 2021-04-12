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

public class AdminController {

	private static class UserItem extends ListCell<String> {
		private HBox hBox = new HBox();
		private Label username = new Label("");
		private Pane blank = new Pane();
		private Button delete = new Button("Delete");

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

		private void setStyle() {
			username.setFont(Font.font(16));
			delete.setFont(Font.font(15));
			setStyle("-fx-font-family: AppleGothic;");
		}
	}

	@FXML
	Button addUser, logout;
	@FXML
	ListView<String> listView;
	@FXML
	TextField newUsername;

	private ObservableList<String> obsList;

	public void start(Stage primaryStage) {
		obsList = FXCollections.observableArrayList();

		List<User> users = UserList.getUsers();
		for (int i = 0; i < users.size(); i++) {
			obsList.add(users.get(i).getUsername());
		}

		listView.setItems(obsList);
		listView.setCellFactory(userDisplay -> new UserItem());
	}

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
		primaryStage.setTitle("Login Page");
		primaryStage.setResizable(false);
		primaryStage.show();
	}

}
