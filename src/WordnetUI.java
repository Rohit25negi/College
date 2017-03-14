
/**
 * Author:
 *	Name - Rohit Negi
 *  Roll No - 130101144
 *  Sys Id -2013014812
 *  
 *  
 *  This file contains the UI controller code for the wordnet UI.
 *   This gives the user ability to insert new words easily
 */
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class WordnetUI extends Application implements Initializable {
	public static void main(String arg[]) {
		Application.launch(arg);
	}

	@FXML
	public Button add;
	public ComboBox<String> pos, parent, child;
	Wordnet wordnet;
	public TextField newword;

	/* Initializing the fields */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		pos.getItems().addAll("JJ", "NN", "VB", "NNP");

		wordnet = new Wordnet();
		Set<String> keys = wordnet.getWordnet().keySet();
		parent.getItems().addAll(keys);

	}

	public void start(Stage primaryStage) {
		try {

			/* loading the UI, UI code stored in WordnetUI.fxml */
			Pane page = (Pane) FXMLLoader.load(Main.class.getResource("WordnetUI.fxml"));
			Scene scene = new Scene(page);
			primaryStage.setScene(scene);
			primaryStage.setTitle("FXML is Simple");
			primaryStage.show();
		} catch (Exception ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/*to insert the code into the wordnet.*/
	public void insertIntoWordnet() {
		String type = pos.getSelectionModel().getSelectedItem();

		if (type == null) {
			/* if no pos is selected */
			System.out.println("Select the POS");
			return;
		}
		String p = parent.getSelectionModel().getSelectedItem();
		String c = child.getSelectionModel().getSelectedItem();
		String word = newword.getText();

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Wordnet Operation");
		alert.setHeaderText(null);

		if (wordnet.insertWord(word, type, p, c)) {
			/* If the word is inserted successfully */
			alert.setContentText("Word inserted successfully");

		} else {
			/* If the word is not successfully stored */
			alert.setContentText("Word Could not be inserted");
		}

		alert.showAndWait();
		/* reinitializing the fields with new word added */
		parent.getItems().clear();
		wordnet = new Wordnet();
		Set<String> keys = wordnet.getWordnet().keySet();
		parent.getItems().addAll(keys);

	}

	/*
	 * Single node can not be parent and child both. Therefore, the node
	 * selected as parent should be removed from the children selection list
	 */
	public void prepareChildren() {
		Wordnet wordnet = new Wordnet();
		Set<String> keys = wordnet.getWordnet().keySet();
		keys.remove(parent.getSelectionModel().getSelectedItem());
		child.getItems().clear();
		child.getItems().addAll(keys);
	}
}
