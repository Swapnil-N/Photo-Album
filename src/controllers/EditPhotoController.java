package controllers;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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

	private static class TagValueItem extends ListCell<String> {
		private HBox hBox = new HBox();
		private Label pair = new Label("");
		private Pane blank = new Pane();
		private Button delete = new Button();

		private String key, value;

		private Photo photo;

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
	Button add;
	@FXML
	ChoiceBox<String> tagInput;
	@FXML
	ImageView photoImage;
	@FXML
	ListView<String> tagsView;
	@FXML
	Tab detailsTab, tagsTab;
	@FXML
	TabPane tabpane;
	@FXML
	TextArea photoAlbums, photoCaption;
	@FXML
	TextField photoName, photoDate, valueInput;

	private User currentUser;
	private Album currentAlbum;
	private Photo currentPhoto;

	private ObservableList<String> photoTags;

	public void start(User currentUser, Album currentAlbum, Photo currentPhoto) {
		this.currentUser = currentUser;
		this.currentAlbum = currentAlbum;
		this.currentPhoto = currentPhoto;
		photoTags = FXCollections.observableArrayList();

		photoImage.setImage(new Image(currentPhoto.getPhotoURL()));

		photoName.setText(currentPhoto.getName());
		photoDate.setText(currentPhoto.getDateTimeString());
		loadAlbums();
		photoCaption.setText(currentPhoto.getCaption());

		photoName.textProperty().addListener((Observable, oldValue, newValue) -> {
			currentPhoto.setName(newValue);
		});
		photoCaption.textProperty().addListener((Observable, oldValue, newValue) -> {
			currentPhoto.setCaption(newValue);
		});

		Map<String, List<String>> photoTagsMap = currentPhoto.getTags();
		for (String key : photoTagsMap.keySet()) {
			for (String value : photoTagsMap.get(key)) {
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

	private void loadAlbums() {
		photoAlbums.setText(null);

		Set<String> albumStrings = new HashSet<>();
		for (Album album : currentUser.getAlbums()) {
			for (Photo photo : album.getPhotoList()) {
				if (photo.equals(currentPhoto)) {
					albumStrings.add(null);
				}
			}
		}

		photoAlbums.setText(String.join(",", albumStrings));
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

	public void onActionMove(ActionEvent e) {
		ChoiceDialog<Album> dialog = new ChoiceDialog<Album>(null, currentUser.getAlbums());
		dialog.setHeaderText("Album Move Selection");
		dialog.setContentText("Please select an album:");

		Optional<Album> optional = dialog.showAndWait();

		optional.ifPresent(choosenAlbum -> {
			currentAlbum.deletePhoto(currentPhoto);
			choosenAlbum.addPhoto(currentPhoto);
		});

		loadAlbums();
	}

	public void onActionCopy(ActionEvent e) {
		ChoiceDialog<Album> dialog = new ChoiceDialog<Album>(null, currentUser.getAlbums());
		dialog.setHeaderText("Album Copy Selection");
		dialog.setContentText("Please select an album:");

		Optional<Album> optional = dialog.showAndWait();

		optional.ifPresent(choosenAlbum -> choosenAlbum.addPhoto(currentPhoto));

		loadAlbums();
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

}
