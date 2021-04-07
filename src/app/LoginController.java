package app;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
	
	@FXML
	TextField username;
	@FXML
	Button loginButton;
	
	public void start(Stage mainStage) {
		
		loginButton.setOnAction(new EventHandler<ActionEvent>()
			{

				@Override
				public void handle(ActionEvent arg0) {
					
					
				}
		
			});
		
		
	}

}
