package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class photos extends Application {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
		
		

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("asdf");
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/LoginScreen.fxml"));
		AnchorPane root = (AnchorPane) loader.load();

		//ListController listController = loader.getController();
		//listController.start(primaryStage);

		Scene scene = new Scene(root, 1000, 750);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Login Page");
		//primaryStage.setResizable(false);
		primaryStage.show();
		
		
	}

}
