package controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Photo;
import models.User;

public class SearchController {

	@FXML
	Button searchDate, searchTags, createAlbum;
	@FXML
	ChoiceBox<String> tag1, value1, tag2, value2;
	@FXML
	DatePicker startDate, endDate;
	@FXML
	RadioButton andChoice, orChoice;
	@FXML
	TilePane tilepane;

	private User currentUser;
	private ToggleGroup toggleGroup;
	private List<Photo> recentSearchResults;

	
	/** 
	 * @param currentUser
	 */
	public void start(User currentUser) {
		this.currentUser = currentUser;
		toggleGroup = new ToggleGroup();
		recentSearchResults = new ArrayList<>();

		startDate.setDayCellFactory(param -> new DateCell() {
			@Override
			public void updateItem(LocalDate date, boolean empty) {
				super.updateItem(date, empty);
				endDate.setValue(null);
			}
		});

		endDate.disableProperty().bind(startDate.valueProperty().isNull());
		endDate.setDayCellFactory(param -> new DateCell() {
			@Override
			public void updateItem(LocalDate date, boolean empty) {
				super.updateItem(date, empty);
				boolean beforeStartDate = true;
				if (startDate.getValue() != null) {
					beforeStartDate = date.compareTo(startDate.getValue()) < 0;
				}
				setDisable(empty || beforeStartDate);
			}
		});

		searchDate.disableProperty().bind(startDate.valueProperty().isNull().or(endDate.valueProperty().isNull()));

		value1.disableProperty().bind(tag1.valueProperty().isNull());
		tag2.disableProperty().bind(value1.valueProperty().isNull());
		value2.disableProperty().bind(tag2.valueProperty().isNull());

		toggleGroup.getToggles().add(andChoice);
		toggleGroup.getToggles().add(orChoice);

		andChoice.disableProperty().bind(tag2.valueProperty().isNull().or(value2.valueProperty().isNull()));
		orChoice.disableProperty().bind(tag2.valueProperty().isNull().or(value2.valueProperty().isNull()));

		BooleanBinding pair1Binding = tag1.valueProperty().isNull().or(value1.valueProperty().isNull());
		BooleanBinding pair2Binding = pair1Binding.not()
				.and(tag2.valueProperty().isNotNull().and(value2.valueProperty().isNull()));
		BooleanBinding pair2FilledBinding = tag2.valueProperty().isNotNull().and(value2.valueProperty().isNotNull());
		BooleanBinding comboBinding = pair2FilledBinding.and(toggleGroup.selectedToggleProperty().isNull());

		searchTags.disableProperty().bind(pair1Binding.or(pair2Binding).or(comboBinding));

		createAlbum.setDisable(true);

		loadTags();

		tag1.valueProperty().addListener((obs, oldItem, newItem) -> {
			loadValues(newItem, 1);

			value1.setValue(null);
			tag2.setValue(null);
			value2.setValue(null);
			toggleGroup.selectToggle(null);
		});

		tag2.valueProperty().addListener((obs, oldItem, newItem) -> {
			loadValues(newItem, 2);

			value2.setValue(null);
			toggleGroup.selectToggle(null);
		});
	}

	private void loadTags() {
		List<String> tagList = currentUser.getTags();

		tag1.getItems().clear();
		for (int i = 0; i < tagList.size() - 1; i++) {
			tag1.getItems().add(tagList.get(i));
		}

		tag2.getItems().clear();
		tag2.getItems().add(null);
		for (int i = 0; i < tagList.size() - 1; i++) {
			tag2.getItems().add(tagList.get(i));
		}
	}

	
	/** 
	 * @param searchTag
	 * @param valueBox
	 */
	private void loadValues(String searchTag, int valueBox) {
		Set<String> possiblePairs = new HashSet<String>();

		for (int i = 0; i < currentUser.getAlbums().size(); i++) {
			for (Photo albumPhoto : currentUser.getAlbums().get(i).getPhotoList()) {
				if (albumPhoto.getTags().containsKey(searchTag)) {
					possiblePairs.addAll(albumPhoto.getTags().get(searchTag));
				}
			}
		}

		List<String> sortedValues = new ArrayList<>(possiblePairs);
		Collections.sort(sortedValues);

		if (valueBox == 1) {
			value1.getItems().clear();
			for (int i = 0; i < sortedValues.size(); i++) {
				value1.getItems().add(sortedValues.get(i));
			}
		} else if (valueBox == 2) {
			value2.getItems().clear();
			for (int i = 0; i < sortedValues.size(); i++) {
				value2.getItems().add(sortedValues.get(i));
			}
		}
	}

	
	/** 
	 * @return Set<Photo>
	 */
	private Set<Photo> dateSearchResults() {
		Set<Photo> desiredPhotos = new HashSet<Photo>();

		for (int i = 0; i < currentUser.getAlbums().size(); i++) {
			for (Photo currentPhoto : currentUser.getAlbums().get(i).getPhotoList()) {
				LocalDate currentPhotoDate = currentPhoto.getDate().toInstant().atZone(ZoneId.systemDefault())
						.toLocalDate();
				if (currentPhotoDate.equals(startDate.getValue()) || currentPhotoDate.equals(endDate.getValue())) {
					desiredPhotos.add(currentPhoto);
				} else if (currentPhotoDate.isAfter(startDate.getValue())
						&& currentPhotoDate.isBefore(endDate.getValue())) {
					desiredPhotos.add(currentPhoto);
				}
			}
		}

		return desiredPhotos;
	}

	private void nullDateValues() {
		startDate.setValue(null);
		endDate.setValue(null);
	}

	
	/** 
	 * @return Set<Photo>
	 */
	private Set<Photo> tagsSearchResults() {
		Set<Photo> desiredPhotos = new HashSet<Photo>();

		for (int i = 0; i < currentUser.getAlbums().size(); i++) {
			for (Photo currentPhoto : currentUser.getAlbums().get(i).getPhotoList()) {
				if (fitsTagsSpecifications(currentPhoto))
					desiredPhotos.add(currentPhoto);
			}
		}

		return desiredPhotos;
	}

	
	/** 
	 * @param currentPhoto
	 * @return boolean
	 */
	private boolean fitsTagsSpecifications(Photo currentPhoto) {
		boolean tag1Include = true;

		String toggleValue = null;
		boolean tag2Include = true;

		if (currentPhoto.getTags().containsKey(tag1.getValue())
				&& currentPhoto.getTags().get(tag1.getValue()).contains(value1.getValue())) {
			tag1Include = true;
		} else {
			tag1Include = false;
		}

		if (tag2.getValue() != null) {
			toggleValue = ((RadioButton) toggleGroup.getSelectedToggle()).getText();

			if (currentPhoto.getTags().containsKey(tag2.getValue())
					&& currentPhoto.getTags().get(tag2.getValue()).contains(value2.getValue())) {
				tag2Include = true;
			} else {
				tag2Include = false;
			}
		}

		if (toggleValue == null) {
			return tag1Include;
		} else if (toggleValue.equals(andChoice.getText())) {
			return tag1Include && tag2Include;
		} else if (toggleValue.equals(orChoice.getText())) {
			return tag1Include || tag2Include;
		}

		return false;
	}

	private void nullTagsValues() {
		tag1.setValue(null);
		value1.setValue(null);
		tag2.setValue(null);
		value2.setValue(null);
		toggleGroup.selectToggle(null);
	}

	
	/** 
	 * @param e
	 * @throws IOException
	 */
	public void onActionSearchDate(ActionEvent e) throws IOException {
		Set<Photo> desiredPhotos = dateSearchResults();
		if (desiredPhotos.size() > 0)
			updateTilePane(desiredPhotos);
		else
			noSearchResultsAlert();

		nullDateValues();
	}

	
	/** 
	 * @param e
	 * @throws IOException
	 */
	public void onActionSearchTags(ActionEvent e) throws IOException {
		Set<Photo> desiredPhotos = tagsSearchResults();
		if (desiredPhotos.size() > 0)
			updateTilePane(desiredPhotos);
		else
			noSearchResultsAlert();

		nullTagsValues();
	}

	
	/** 
	 * @param desiredPhotos
	 * @throws IOException
	 */
	private void updateTilePane(Set<Photo> desiredPhotos) throws IOException {
		tilepane.getChildren().clear();
		recentSearchResults.clear();

		for (Photo currentPhoto : desiredPhotos) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/searchPhotoPreview.fxml"));
			VBox root = (VBox) loader.load();

			SearchPhotoPreviewController searchPhotoPreviewController = loader.getController();
			searchPhotoPreviewController.start(currentPhoto);

			tilepane.getChildren().add(root);
			recentSearchResults.add(currentPhoto);
		}

		createAlbum.setDisable(false);
	}

	private void noSearchResultsAlert() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("No Valid Photos");
		alert.setContentText("There are no photos that fit the desired specifications.");
		alert.showAndWait();

		tilepane.getChildren().clear();
		createAlbum.setDisable(true);
	}

	
	/** 
	 * @param e
	 * @throws IOException
	 */
	public void onActionCreateAlbum(ActionEvent e) throws IOException {
		String desiredAlbumName = getDesiredAlbumName();
		if (desiredAlbumName.isEmpty()) {
			invalidAlbumNameAlert();
			return;
		}

		if (recentSearchResults.size() > 0) {
			currentUser.addAlbum(desiredAlbumName);
			for (Photo currentPhoto : recentSearchResults) {
				currentUser.getAlbumWithName(desiredAlbumName).addPhoto(currentPhoto);
			}
		}
	}

	
	/** 
	 * @return String
	 */
	private String getDesiredAlbumName() {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("New Album");
		dialog.setHeaderText("Enter New Album Name");

		Optional<String> opt = dialog.showAndWait();

		if (opt.isEmpty())
			return "";

		String albumName = opt.get().trim();
		if (albumName.length() == 0) {
			return "";
		} else if (currentUser.getAlbumWithName(albumName) != null) {
			return "";
		}

		return albumName;
	}

	private void invalidAlbumNameAlert() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Add Album Failed");
		alert.setContentText("That album name is not available.");
		alert.showAndWait();
	}

	
	/** 
	 * @param e
	 * @throws IOException
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
	 * @param e
	 * @throws IOException
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
