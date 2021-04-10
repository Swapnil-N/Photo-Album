package controllers;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.Album;
import models.User;

public class AlbumPreviewController {

	@FXML
	VBox container;
	
	@FXML
	ImageView imageView;
	
	@FXML
	Text albumName, photoCount, dateRange;
	
	@FXML
	MenuButton settings;
	
	Album album;
	User currentUser;
	UserHomeController userLandingController;
	
	public void start(User currentUser) {
		this.currentUser = currentUser;

		ImageView menuIcon = new ImageView(new Image("file:../../resources/images/settings.png"));
	    menuIcon.setFitHeight(20);
	    menuIcon.setFitWidth(20);
	    settings.setGraphic(menuIcon);

	}
	
	public void setAlbum(Album album) {

		imageView.setImage(new Image("file:../../resources/images/settings.png"));
		imageView.setFitHeight(170);
		imageView.setFitWidth(265);
		
		albumName.setText(album.getName());
		photoCount.setText(album.getSize() + "");
		
		if (!album.getFirstDate().isEmpty())
			dateRange.setText(album.getFirstDate() + " - " + album.getLastDate());
		else
			dateRange.setText("N/A");
		
		container.setId(album.getName());
		this.album = album;
	}
	
	public void onActionRename(ActionEvent e) {
		
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle(" ");
		dialog.setHeaderText("Enter New Album Name");
		
		Optional<String> opt = dialog.showAndWait();
		
		if (opt.isEmpty())
			return;
		
		String newName = opt.get().trim();
		if(newName.length() == 0) {
			invalidAlbumAlert();
			return;
		}
		
		if (currentUser.hasAlbumWithName(newName)) {
			invalidAlbumAlert();
		} else {
			album.setName(newName);
			albumName.setText(album.getName());
			container.setId(newName);
		}
	}
	
	private void invalidAlbumAlert() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Add Album Failed");
		alert.setContentText("That album name is not available.");
		alert.showAndWait();
	}
	
	public void onActionDelete(ActionEvent e) throws IOException {
		if (currentUser.deleteAlbumWithName(album.getName()))
			userLandingController.deleteAlbum(album.getName());
	}
	
	public void imageViewMouseClicked(MouseEvent e) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/album.fxml"));
		AnchorPane root = (AnchorPane) loader.load();

		Node node = (Node) e.getSource();
		Stage primaryStage = (Stage) node.getScene().getWindow();
		
		AlbumViewController albumViewController = loader.getController();
		albumViewController.start(currentUser, album);
		
		Scene scene = new Scene(root, 1000, 750);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Home Screen");
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	
	public void setUserLandingController(UserHomeController controller) {
		userLandingController = controller;
	}
}
