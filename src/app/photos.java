package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class photos extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/slideshow.fxml"));
		AnchorPane root = (AnchorPane) loader.load();

		Scene scene = new Scene(root, 1000, 750);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Login Page");
		primaryStage.setResizable(false);
		primaryStage.show();
	}

}
