package controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.Album;
import models.User;

public class SlideshowController { // TODO Add more details about photos

	@FXML
	Button logout, home, previous, next;
	@FXML
	ImageView imageView;
	@FXML
	Label photoName;

	private User currentUser;
	private Album currentAlbum;
	private int iter;

	public void start(User currentUser, Album currentAlbum) {
		this.currentUser = currentUser;
		this.currentAlbum = currentAlbum;
		iter = 0;

		if (currentAlbum.getSize() > 0) {
			imageView.setImage(new Image(currentAlbum.getPhotoList().get(iter).getPhotoURL()));
			photoName.setText(currentAlbum.getPhotoList().get(iter).getName());
		}

	}

	public void onActionPrevious(ActionEvent e) {
		iter--;

		if (iter < 0)
			iter = currentAlbum.getSize() - 1;

		if (currentAlbum.getSize() > 0) {
			imageView.setImage(new Image(currentAlbum.getPhotoList().get(iter).getPhotoURL()));
			photoName.setText(currentAlbum.getPhotoList().get(iter).getName());
		}

	}

	public void onActionNext(ActionEvent e) {
		iter++;

		if (iter >= currentAlbum.getSize())
			iter = 0;

		if (currentAlbum.getSize() > 0) {
			imageView.setImage(new Image(currentAlbum.getPhotoList().get(iter).getPhotoURL()));
			photoName.setText(currentAlbum.getPhotoList().get(iter).getName());
		}
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

	public void onActionHome(ActionEvent e) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/userHome.fxml"));
		AnchorPane root = (AnchorPane) loader.load();

		Node node = (Node) e.getSource();
		Stage primaryStage = (Stage) node.getScene().getWindow();

		UserHomeController userLandingController = loader.getController();
		userLandingController.start(currentUser);

		Scene scene = new Scene(root, 1000, 750);

		primaryStage.setScene(scene);
		primaryStage.setTitle("Home Screen");
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	
}
