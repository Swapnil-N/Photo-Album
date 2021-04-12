package controllers;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Optional;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import models.Album;
import models.Photo;
import models.User;

public class EditPhotoController {
	// yes case sensitive, fix the thing after entering in a new tag that is already taken in the alert
	private static class TagValueItem extends ListCell<String> {
		HBox hBox = new HBox();
		Label pair = new Label("");
		Pane blank = new Pane();
		Button delete = new Button();
		
		String key, value;
		
		Photo photo;

		public TagValueItem(Photo photo) {
			super();
			
			this.photo = photo;
			key = "";
			value = "";

			HBox.setHgrow(blank, Priority.ALWAYS);
			hBox.getChildren().addAll(pair, blank, delete);
			hBox.setAlignment(Pos.CENTER);
			
			delete.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					boolean success = photo.removeTag(key, value);
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
				key = item.split("  tag:  ")[1];
				value = item.split("  value:  ")[1];
				
				pair.setText(key + ": " + value);
				setGraphic(hBox);
			}

			setStyle();
		}
		
		private void setStyle() {
			pair.setFont(Font.font(14));
			setStyle("-fx-font-family: AppleGothic;");
			
			ImageView trashIcon = new ImageView(new Image("file:../../resources/images/trashcan.png"));
			trashIcon.setFitHeight(15);
			trashIcon.setFitWidth(15);
			delete.setGraphic(trashIcon);
		}
	}

	@FXML
	ImageView photoImage;

	@FXML
	TabPane tabpane;

	@FXML
	Tab detailsTab, tagsTab;

	@FXML
	TextField photoName, photoDate, valueInput;

	@FXML
	TextArea photoAlbums, photoCaption;
	
	@FXML
	ListView<String> tagsView;

	@FXML
	ChoiceBox<String> tagInput;
	
	@FXML
	Button add;

	User currentUser;
	Album currentAlbum;
	Photo currentPhoto;
	
	private ObservableList<String> photoTags;


	public void start(User currentUser, Album currentAlbum, Photo currentPhoto) {
		this.currentUser = currentUser;
		this.currentAlbum = currentAlbum;
		this.currentPhoto = currentPhoto;

		photoImage.setImage(new Image(currentPhoto.getPhotoURL()));

		photoName.setText(currentPhoto.getName());
		photoDate.setText(currentPhoto.getDateTimeString());
		photoAlbums.setText("later worry");
		photoCaption.setText(currentPhoto.getCaption());
		
		photoCaption.textProperty().addListener((Observable, oldValue, newValue) -> {
			currentPhoto.setCaption(newValue);
		});
		
		photoTags = FXCollections.observableArrayList();
		Map<String, List<String>> photoTagsMap = currentPhoto.getTags();
		for (String key: photoTagsMap.keySet()) {
			for (String value: photoTagsMap.get(key)) {
				photoTags.add("  tag:  " + key + "  tag:  " + "  value:  " + value + "  value:  ");
			}
		}
		photoTags.sort((i, j) -> i.compareTo(j));
		tagsView.setItems(photoTags);
		tagsView.setCellFactory(tagValueDisplay -> new TagValueItem(currentPhoto));
		
		loadTags();
		
		tagInput.valueProperty().addListener((obs, oldItem, newItem) -> {
            if (newItem != null && newItem.equals(currentUser.getAddNewTag())) {
                addNewTagAlert();
            } 
        });
		
		BooleanBinding valueFilled = Bindings.createBooleanBinding(() -> {
			return !valueInput.getText().isEmpty();
		}, valueInput.textProperty());
		
		add.disableProperty().bind(tagInput.valueProperty().isNull().or(valueFilled.not()));
	}
	
	private void loadTags() {
		List<String> tagList = currentUser.getTags();

		tagInput.getItems().clear();
		for (int i = 0; i < tagList.size(); i++) {
			tagInput.getItems().add(tagList.get(i));
		}
	}
	
	private void addNewTagAlert() {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle(" ");
		dialog.setHeaderText("Enter New Tag");

		Optional<String> opt = dialog.showAndWait();

		if (opt.isEmpty()) {
			//TODO: FIX
			tagInput.setValue(null);
			valueInput.setText("");
			return;
		}
			
		String tagName = opt.get().trim();
		if (tagName.length() == 0) {
			invalidTagAlert();
			return;
		}

		if (!currentUser.addTag(tagName)) {
			invalidTagAlert();
		} else {
			loadTags();
		}
		
		tagInput.setValue(null);
		valueInput.setText("");
	}
	
	private void invalidTagAlert() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Add Tag Failed");
		alert.setContentText("That tag is not available.");
		alert.showAndWait();
	}

	public void onActionLogout(ActionEvent e) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
		AnchorPane root = (AnchorPane) loader.load();

		Node node = (Node) e.getSource();
		Stage primaryStage = (Stage) node.getScene().getWindow();

		Scene scene = new Scene(root, 1000, 750);

		primaryStage.setScene(scene);
		primaryStage.setTitle("Login Screen");
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

	public void onActionMove(ActionEvent e) {
		
		ChoiceDialog<Album> dialog = new ChoiceDialog<Album>(null, currentUser.getAlbums());
		dialog.setHeaderText("Album Move Selection");
		dialog.setContentText("Please select an album:");
		
		Optional<Album> optional = dialog.showAndWait();
		
		optional.ifPresent(choosenAlbum -> {
			currentAlbum.deletePhoto(currentPhoto); 
			choosenAlbum.addPhoto(currentPhoto);
			});
				
	}

	public void onActionCopy(ActionEvent e) {
		
		ChoiceDialog<Album> dialog = new ChoiceDialog<Album>(null, currentUser.getAlbums());
		dialog.setHeaderText("Album Copy Selection");
		dialog.setContentText("Please select an album:");
		
		Optional<Album> optional = dialog.showAndWait();
				
		optional.ifPresent(choosenAlbum -> choosenAlbum.addPhoto(currentPhoto));
		
	}

	public void onActionAdd(ActionEvent e) {
		String key = tagInput.getValue();
		String value = valueInput.getText();
		
		boolean success = currentPhoto.addTag(key, value);
		
		if (success) {
			photoTags.add("  tag:  " + key + "  tag:  " + "  value:  " + value + "  value:  ");
			photoTags.sort((i, j) -> i.compareTo(j));
		} else {
			invalidTagValueAdded();
		}
		
		tagInput.setValue(null);
		valueInput.setText("");
	}
	
	private void invalidTagValueAdded() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Add Tag Failed");
		alert.setContentText("An existing tag-value pair was entered.");
		alert.showAndWait();
	}

}
