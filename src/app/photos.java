package app;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.Photo;
import models.User;
import models.UserList;

public class photos extends Application {

	/**
	 * Launches the application
	 * 
	 * @param args command-line arguments
	 */
	public static void main(String[] args) {
		/*
		User stockUser = UserList.getUserWithName("stock");
		stockUser.deleteAlbum("stock");
		stockUser.addAlbum("stock");
		stockUser.getAlbumWithName("stock").addPhoto(new Photo(new File("file:../../data/antelopecanyon.jpeg")));
		stockUser.getAlbumWithName("stock").addPhoto(new Photo(new File("file:../../data/grandcanyon.jpeg")));
		stockUser.getAlbumWithName("stock").addPhoto(new Photo(new File("file:../../data/halekalanationalpark.jpeg")));
		stockUser.getAlbumWithName("stock").addPhoto(new Photo(new File("file:../../data/havasufalls.jpeg")));
		stockUser.getAlbumWithName("stock").addPhoto(new Photo(new File("file:../../data/sacredfalls.jpeg")));
		stockUser.getAlbumWithName("stock").addPhoto(new Photo(new File("file:../../data/shoshonefalls.jpeg")));
		stockUser.getAlbumWithName("stock").addPhoto(new Photo(new File("file:../../data/volcanoesnationalpark.jpeg")));
		stockUser.getAlbumWithName("stock").addPhoto(new Photo(new File("file:../../data/yellowstonenationalpark.jpeg")));
		stockUser.getAlbumWithName("stock").addPhoto(new Photo(new File("file:../../data/yosemitenationalpark.jpeg")));
		*/
		launch(args);
	}

	/**
	 * Loads the login page
	 * 
	 * @param primaryStage top level JavaFX container
	 * @throws Exception if login screen file is not found
	 */
	@Override
	public void start(Stage primaryStage) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
		AnchorPane root = (AnchorPane) loader.load();

		Scene scene = new Scene(root, 1000, 750);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Login Page");
		primaryStage.setResizable(false);
		primaryStage.show();
	}

}
