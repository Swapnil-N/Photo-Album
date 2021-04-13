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

	/**
	 * Nested class used to control cells in the user ListView
	 */
	private static class TagValueItem extends ListCell<String> {
		private HBox hBox = new HBox();
		private Label pair = new Label("");
		private Pane blank = new Pane();
		private Button delete = new Button();

		private String key, value;

		/**
		 * Initializes a cell in the ListView
		 */
		public TagValueItem(Photo photo) {
			super();

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

		/**
		 * Updates the ListView cell with a tag-value pair
		 * 
		 * @param item  new string to update items
		 * @param empty true if empty, false otherwise
		 */
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

		/**
		 * Sets the components' font and font size
		 */
		private void setStyle() {
			pair.setFont(Font.font(14));
			setStyle("-fx-font-family: AppleGothic;");

			ImageView trashIcon = new ImageView(new Image("file:../../resources/images/trashcan.png"));
			trashIcon.setFitHeight(15);
			trashIcon.setFitWidth(15);
			delete.setGraphic(trashIcon);
		}
	}

	/**
	 * Component that adds a new tag-value pair to a picture
	 */
	@FXML
	Button add;

	/**
	 * Component that displays the existing tag options and allows the user to
	 * create a new one
	 */
	@FXML
	ChoiceBox<String> tagInput;

	/**
	 * Component that displays the photo's picture
	 */
	@FXML
	ImageView photoImage;

	/**
	 * Component that displays the photo's tags
	 */
	@FXML
	ListView<String> tagsView;

	/**
	 * Component that displays the albums the photo is in
	 */
	@FXML
	TextArea photoAlbums;

	/**
	 * Component that displays and allows the user to edit the photo's caption
	 */
	@FXML
	TextArea photoCaption;

	/**
	 * Component that displays and allows the user to edit the photo's name
	 */
	@FXML
	TextField photoName;

	/**
	 * Component that displays the photo's date-time of capture
	 */
	@FXML
	TextField photoDate;

	/**
	 * Component that allows the user to enter a value when trying to add a new
	 * tag-value pair to a photo
	 */
	@FXML
	TextField valueInput;

	/**
	 * User logged in
	 */
	private User currentUser;

	/**
	 * Photo to be edited
	 */
	private Photo currentPhoto;

	/**
	 * Tags attached to this photo
	 */
	private ObservableList<String> photoTags;

	/**
	 * Sets up the 'Edit Photo' page
	 * 
	 * @param currentUser  user logged in
	 * @param currentPhoto photo to be edited
	 */
	public void start(User currentUser, Photo currentPhoto) {
		this.currentUser = currentUser;
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

	/**
	 * Gets all the albums that the photo is in
	 */
	private void loadAlbums() {
		photoAlbums.setText(null);

		Set<String> albumStrings = new HashSet<>();
		for (Album album : currentUser.getAlbums()) {
			for (Photo photo : album.getPhotoList()) {
				if (photo.equals(currentPhoto))
					albumStrings.add(album.getName());
			}
		}

		photoAlbums.setText(String.join(", ", albumStrings));
	}

	/**
	 * Loads the tags associated with photo
	 */
	private void loadTags() {
		List<String> tagList = currentUser.getTags();

		tagInput.getItems().clear();
		for (int i = 0; i < tagList.size(); i++) {
			tagInput.getItems().add(tagList.get(i));
		}
	}

	/**
	 * Pop-up to open a dialog to enter new tag key
	 */
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

	/**
	 * Informs the user that he/she attempted to add an existing tag
	 */
	private void invalidTagAlert() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Add Tag Failed");
		alert.setContentText("That tag is not available.");
		alert.showAndWait();
	}

	/**
	 * Allows an user to move a photo to another album
	 * 
	 * @param e represents that the 'Move' button has been clicked
	 */
	public void onActionMove(ActionEvent e) {

		Set<Album> currentlyInAlbums = new HashSet<>();
		for (Album album : currentUser.getAlbums()) {
			for (Photo photo : album.getPhotoList()) {
				if (photo.equals(currentPhoto))
					currentlyInAlbums.add(album);
			}
		}

		ChoiceDialog<Album> dialog = new ChoiceDialog<Album>(null, currentlyInAlbums);
		dialog.setHeaderText("Album Move Selection");
		dialog.setContentText("Please select an album to move from:");

		Optional<Album> optional = dialog.showAndWait();

		if (optional.isEmpty())
			return;

		ChoiceDialog<Album> dialog2 = new ChoiceDialog<Album>(null, currentUser.getAlbums());
		dialog2.setHeaderText("Album Move Selection");
		dialog2.setContentText("Please select an album to move to:");

		Optional<Album> optional2 = dialog2.showAndWait();

		if (optional2.isEmpty())
			return;

		optional.get().deletePhoto(currentPhoto);
		optional2.get().addPhoto(currentPhoto);

		loadAlbums();
	}

	/**
	 * Allows an user to copy a photo to another album
	 * 
	 * @param e represents that the 'Copy' button has been clicked
	 */
	public void onActionCopy(ActionEvent e) {
		ChoiceDialog<Album> dialog = new ChoiceDialog<Album>(null, currentUser.getAlbums());
		dialog.setHeaderText("Album Copy Selection");
		dialog.setContentText("Please select an album:");

		Optional<Album> optional = dialog.showAndWait();

		optional.ifPresent(choosenAlbum -> choosenAlbum.addPhoto(currentPhoto));

		loadAlbums();
	}

	/**
	 * Adds a tag-value pair to the photo
	 * 
	 * @param e represents that the 'Add Tag' button has been clicked
	 */
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

	/**
	 * Informs the user that he/she attempted to add an existing tag-value pair
	 */
	private void invalidTagValueAdded() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Add Tag Failed");
		alert.setContentText("An existing tag-value pair was entered.");
		alert.showAndWait();
	}

	/**
	 * Logs the user out and takes him/her to the login screen
	 * 
	 * @param e represents that the 'Logout' button has been clicked
	 * @throws IOException if login screen file is not found
	 */
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

	/**
	 * Takes the user to his/her home screen
	 * 
	 * @param e represents that the 'Home' button has been clicked
	 * @throws IOException if home screen file is not found
	 */
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
