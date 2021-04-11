package controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.Album;
import models.Photo;
import models.User;

public class PhotoPreviewController {
	
	@FXML
	ImageView imageView;
	
	@FXML
	Text photoName, caption;
	
	@FXML
	Button delete;
	
	Photo currentPhoto;
	Album currentAlbum;
	User currentUser;
	
	AlbumViewController albumViewController;
	
	public void start(Album currentAlbum, Photo currentPhoto, User currentUser) {
		this.currentPhoto = currentPhoto;
		this.currentAlbum = currentAlbum;
		this.currentUser = currentUser;
		
		photoName.setText(currentPhoto.getName());
		caption.setText(currentPhoto.getCaption());
		imageView.setImage(new Image(currentPhoto.getPhotoURL()));
	}
	
	
	public void onActionDelete(ActionEvent e) throws IOException{
		currentAlbum.deletePhoto(currentPhoto);
		albumViewController.loadAlbum();
	}
	
	public void imageViewMouseClicked(MouseEvent e) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/editPhoto.fxml"));
		AnchorPane root = (AnchorPane) loader.load();

		Node node = (Node) e.getSource();
		Stage primaryStage = (Stage) node.getScene().getWindow();
		
		EditPhotoController editPhotoController = loader.getController();
		editPhotoController.start(currentUser, currentPhoto);
		
		Scene scene = new Scene(root, 1000, 750);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Edit Photo");
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	
	public void setAlbumViewController(AlbumViewController controller) {
		albumViewController = controller;
	}

}
