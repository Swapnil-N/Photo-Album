package app;

import java.io.File;

import controllers.EditPhotoController;
import controllers.SearchController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.Photo;
import models.User;

public class photos extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Photo newPhoto = new Photo(new File("file:///Users/srinandinim/Documents/Coursework/software-methodology/Photo-Album/resources/images/noimageavailable.png"));
		User user = new User("fake");
		user.addAlbum("fake");
		user.getAlbumWithName("fake").addPhoto(newPhoto);

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/editPhoto.fxml"));
		AnchorPane root = (AnchorPane) loader.load();
		
		EditPhotoController controller = loader.getController();
		controller.start(user, newPhoto);

		Scene scene = new Scene(root, 1000, 750);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Login Page");
		primaryStage.setResizable(false);
		primaryStage.show();
	}

}
