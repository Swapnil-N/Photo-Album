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

/**
 * Handles the search page
 * 
 * @author Swapnil Napuri
 * @author Srinandini Marpaka
 */

public class SearchController {

	/**
	 * Component through which the user indicates he/she wants to perform a 'Search
	 * by Date'
	 */
	@FXML
	Button searchDate;

	/**
	 * Component through which the user indicates he/she wants to perform a 'Search
	 * by Tags'
	 */
	@FXML
	Button searchTags;

	/**
	 * Component through which the user indicates he/she wants to create an album
	 * with the search results
	 */
	@FXML
	Button createAlbum;

	/**
	 * Component through which the user selects tag 1
	 */
	@FXML
	ChoiceBox<String> tag1;

	/**
	 * Component through which the user selects the value of the tag 1
	 */
	@FXML
	ChoiceBox<String> value1;

	/**
	 * Component through which the user selects tag 2
	 */
	@FXML
	ChoiceBox<String> tag2;

	/**
	 * Component through which the user selects the value of the tag 2
	 */
	@FXML
	ChoiceBox<String> value2;

	/**
	 * Component that holds the beginning of the date range
	 */
	@FXML
	DatePicker startDate;

	/**
	 * Component that holds the end of the date range
	 */
	@FXML
	DatePicker endDate;

	/**
	 * Component that indicates conjunctive combination
	 */
	@FXML
	RadioButton andChoice;

	/**
	 * Component that indicates disjunctive combination
	 */
	@FXML
	RadioButton orChoice;

	/**
	 * Component that holds the search results
	 */
	@FXML
	TilePane tilepane;

	/**
	 * User logged in
	 */
	private User currentUser;

	/**
	 * Holds the conjunction and disjunction radio buttons
	 */
	private ToggleGroup toggleGroup;

	/**
	 * Photos resulted from the most recent search
	 */
	private Set<Photo> recentSearchResults;

	/**
	 * Sets up the 'Search' screen
	 * 
	 * @param currentUser user logged in
	 */
	public void start(User currentUser) {
		this.currentUser = currentUser;
		toggleGroup = new ToggleGroup();
		recentSearchResults = new HashSet<>();

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

	/**
	 * Populates the tag ChoiceBox based on the user's master list of tags
	 */
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
	 * Populates the value ChoiceBox based on the tag selected
	 * 
	 * @param searchTag tag
	 * @param valueBox  which value box to populate (1 or 2)
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
	 * Searches the user's photos based on date criteria
	 * 
	 * @return Set<Photo> set of photos that fit the date criteria
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

	/**
	 * Nulls components in the 'Search by Date' tab
	 */
	private void nullDateValues() {
		startDate.setValue(null);
		endDate.setValue(null);
	}

	/**
	 * Searches the user's photos based on tag criteria
	 * 
	 * @return Set<Photo> set of photos that fit the tag criteria
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
	 * Checks whether a photo has the desired tags
	 * 
	 * @param currentPhoto photo to be checked
	 * @return boolean true if photo fits the tag criteria; false otherwise
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

	/**
	 * Nulls components in the 'Search by Tags' tab
	 */
	private void nullTagsValues() {
		tag1.setValue(null);
		value1.setValue(null);
		tag2.setValue(null);
		value2.setValue(null);
		toggleGroup.selectToggle(null);
	}

	/**
	 * Obtains the 'Search by Date' results and updates screen
	 * 
	 * @param e represents that the 'Search' button on the 'Search by Date' tab has
	 *          been clicked
	 * @throws IOException if search photo preview file is not found
	 */
	public void onActionSearchDate(ActionEvent e) throws IOException {
		recentSearchResults.clear();
		tilepane.getChildren().clear();

		Set<Photo> desiredPhotos = dateSearchResults();
		if (desiredPhotos.size() > 0)
			updateTilePane(desiredPhotos);
		else
			noSearchResultsAlert();

		nullDateValues();
	}

	/**
	 * Obtains the 'Search by Tags' results and updates screen
	 * 
	 * @param e represents that the 'Search' button on the 'Search by Tags' tab has
	 *          been clicked
	 * @throws IOException if search photo preview file is not found
	 */
	public void onActionSearchTags(ActionEvent e) throws IOException {
		recentSearchResults.clear();
		tilepane.getChildren().clear();

		Set<Photo> desiredPhotos = tagsSearchResults();
		if (desiredPhotos.size() > 0)
			updateTilePane(desiredPhotos);
		else
			noSearchResultsAlert();

		nullTagsValues();
	}

	/**
	 * Updates the TilePane with the search results
	 * 
	 * @param desiredPhotos set of photos that fit the search criteria
	 * @throws IOException if search photo preview file is not found
	 */
	private void updateTilePane(Set<Photo> desiredPhotos) throws IOException {
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

	/**
	 * Informs the user that his/her search produced no results
	 */
	private void noSearchResultsAlert() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("No Valid Photos");
		alert.setContentText("There are no photos that fit the desired specifications.");
		alert.showAndWait();

		tilepane.getChildren().clear();
		createAlbum.setDisable(true);
	}

	/**
	 * Creates album with the search results
	 * 
	 * @param e represents that the 'Create Album with Results' button has been
	 *          clicked
	 */
	public void onActionCreateAlbum(ActionEvent e) {
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
	 * Prompts the user to enter a name for the new album
	 * 
	 * @return String new album name
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

	/**
	 * Informs the user that he/she entered an invalid album name
	 */
	private void invalidAlbumNameAlert() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Add Album Failed");
		alert.setContentText("That album name is not available.");
		alert.showAndWait();
	}

	/**
	 * Logs the user out and takes him/her to the login screen
	 * 
	 * @param e represents that the 'Logout' button has been clicked
	 * @throws IOException if logout screen file is not found
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
