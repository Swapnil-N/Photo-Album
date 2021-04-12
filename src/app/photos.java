package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class photos extends Application {

	
	/** 
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

	
	/** 
	 * THING THING
	 * @param primaryStage
	 * @throws Exception
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
		AnchorPane root = (AnchorPane) loader.load();

		Scene scene = new Scene(root, 1000, 750);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Login Page");
		primaryStage.setResizable(false);
		primaryStage.show();
	}

}
