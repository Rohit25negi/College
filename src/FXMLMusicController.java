package src;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import java.awt.event.MouseAdapter;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Port;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;

import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
//import javafx.event.ActionEvent;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
//import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import javazoom.jl.player.Player;

public class FXMLMusicController implements Initializable {

	@FXML
	private FlowPane main;
	@FXML
	private JFXTreeTableView<Music> treeView;
	@FXML
	private Button create_playlist;

	@FXML
	private StackPane stackPane;

	@FXML
	private Label listSize;

	JFXTreeTableColumn<Music, String> stitle, slength, sartist, sgenre;

	@FXML
	private Slider musicTrack, volume;

	@FXML
	ImageView pre,pl,pau,nex;
	@Override
	public void initialize(URL url, ResourceBundle rb) {

		/*
		 * Add first column into the TreeTableview Song Title field will appear
		 * on the screen.
		 */
		stitle = new JFXTreeTableColumn<>("Song Title");

		/* set the predefined width of the column */
		stitle.setPrefWidth(300);

		/*
		 * the below line make changes to the cell and also to determine what
		 * kind of data is displayed on the screen
		 */
		stitle.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<Music, String>, ObservableValue<String>>() {

					/*
					 * the method method defines what data will be displayed.
					 * Here the value of songTitle will be displayed on the
					 * screen
					 */
					@Override
					public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Music, String> param) {
						return param.getValue().getValue().sTitle;
					}
				});

		/*
		 * Add second column into the TreeTableview Length field will appear on
		 * the screen.
		 */
		slength = new JFXTreeTableColumn<>("Length");
		slength.setPrefWidth(90);
		slength.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<Music, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Music, String> param) {
						return param.getValue().getValue().slength;
					}
				});

		sartist = new JFXTreeTableColumn<>("Artist");
		sartist.setPrefWidth(120);
		sartist.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<Music, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Music, String> param) {
						return param.getValue().getValue().sartist;
					}
				});

		sgenre = new JFXTreeTableColumn<>("Genre");
		sgenre.setPrefWidth(110);
		sgenre.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<Music, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Music, String> param) {
						return param.getValue().getValue().sgenre;
					}
				});

		ObservableList<Music> Musics = FXCollections.observableArrayList();

		if (MusicOperation.files != null) {
			for (File f : MusicOperation.files) {
				Musics.add(new Music(f));
			}
			treeView.getSelectionModel().select(MusicOperation.current);
		}
		final TreeItem<Music> root = new RecursiveTreeItem<Music>(Musics, RecursiveTreeObject::getChildren);
		treeView.getColumns().setAll(stitle, slength, sartist, sgenre);
		treeView.setRoot(root);
		treeView.setShowRoot(false);
		treeView.requestFocus();
		volume.setMin(0);
		volume.setMax(100);
		volume.setValue(0);
		volume.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> observableValue, Boolean wasChanging,
					Boolean changing) {
				changeVolume();
			}
		});
		volume.setBlockIncrement(1);
		listSize.setText("Total Songs: 0");
		try
		{
			Runtime rt = Runtime.getRuntime();
			java.lang.Process pr = rt.exec("nircmd.exe setsysvolume 0");
		}catch(Exception e)
		{
			
		}
		
	}

	@FXML
	void loadDialogBox(ActionEvent event) {

		JFXDialogLayout content = new JFXDialogLayout();
		content.setHeading(new Text("Please enter the size of the playlist:"));
		content.setAlignment(Pos.CENTER);
		JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
		JFXTextField userdata = new JFXTextField();

		userdata.setMaxSize(300, 20);
		content.getChildren().add(userdata);

		Button button = new Button("Ok");

		button.setAlignment(Pos.CENTER);
		button.setOnAction(new EventHandler<ActionEvent>() {
			String regex = "[0-9]+";

			@Override
			public void handle(ActionEvent event) {

				if (userdata.getText().matches(regex)) {
					if (MusicOperation.player != null) {
						MusicOperation.player.close();
						MusicOperation.player = null;
					}
					MusicOperation.createPlayList(Integer.parseInt(userdata.getText()));
					setLength();
					setList();
					MusicOperation.current = 0;
					play();
					dialog.close();
				}
			}
		});
		content.setActions(button);
		dialog.show();

	}

	public void play() { // play the music from staring of from middle.
		if (MusicOperation.player != null||MusicOperation.files==null)
			return;
		try {
			MusicOperation.fin = new FileInputStream(MusicOperation.files.get(MusicOperation.current));
			MusicOperation.bin = new BufferedInputStream(MusicOperation.fin);
			MusicOperation.player = new Player(MusicOperation.bin);
			if (MusicOperation.starting_point > 0)
				MusicOperation.fin.skip(MusicOperation.starting_point);
			else
				MusicOperation.total_length = MusicOperation.fin.available();

			musicTrack.setMax(MusicOperation.total_length);
			treeView.getSelectionModel().select(MusicOperation.current);
			treeView.requestFocus();

			/*
			 * below code is commented because this thread is hindering the song
			 * playing
			 */
			Thread x = new Thread() {
				public void run() {

					while (MusicOperation.player != null && !MusicOperation.player.isComplete())
						try {
							musicTrack.setValue((int) (MusicOperation.total_length - MusicOperation.fin.available()));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							break;
						}
				}
			};
			x.setPriority(1);
			x.setDaemon(true);
			x.start();

			Thread t = new Thread(new Runnable() {
				public void run() {
					try {

						MusicOperation.player.play();

						
						if (MusicOperation.player != null && MusicOperation.player.isComplete()) { // Checking
																									// if
																									// the
																									// music
																									// is
							// completed or not

							next(); // if yes move to next music in the list
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			t.setPriority(10);
			t.setDaemon(true);
			t.start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void pause() { // Pause the music
		
		if (MusicOperation.player == null)
			return;
		try {
			/*
			 * to get the pausing point so that the song can be continued from
			 * there later
			 */
			
			MusicOperation.starting_point = MusicOperation.total_length - MusicOperation.fin.available();
			MusicOperation.player.close();
			MusicOperation.player = null;
		} catch (IOException e) {

			e.printStackTrace();
		}

	}
	@FXML
	public void next() { // move to next song in the list
		if (MusicOperation.player == null)
			return;
		if (MusicOperation.files != null && MusicOperation.current < MusicOperation.files.size() - 1) {
			MusicOperation.current++;
			MusicOperation.starting_point = -1;
			MusicOperation.player.close();
			MusicOperation.player = null;
			play(); // play the next song selected
		}
	}
	
	@FXML
	public void previous() { // move to previous song
		if (MusicOperation.player == null)
			return;
		if (MusicOperation.current > 0) {
			MusicOperation.current--;
			MusicOperation.starting_point = -1;
			MusicOperation.player.close();
			MusicOperation.player = null;
			play();
		}
	}

	@FXML
	void setDirectory() {
		DirectoryChooser direc = new DirectoryChooser();
		File file = direc.showDialog(null);
		if (file != null)
			MusicOperation.directoryOpr(file);

	}

	@FXML
	void selectSongs() {
		FileChooser choose = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Music files", "*.mp3", "*.wav");
		choose.getExtensionFilters().add(extFilter);
		List<File> files = choose.showOpenMultipleDialog(null);
		if (files != null) {
			if (MusicOperation.player != null) {
				MusicOperation.player.close();
				MusicOperation.player = null;
			}
			MusicOperation.selectFiles(files);
			MusicOperation.storeMusicDetails();
			MusicOperation.current = 0;
			MusicOperation.storeMusicDetails();
			setLength();
			setList();

			play();
		}

	}

	void setLength() {
		listSize.setText("Total Songs: " + MusicOperation.files.size());
	}

	void setList() {
		ObservableList<Music> Musics = FXCollections.observableArrayList();

		for (File f : MusicOperation.files)
			Musics.add(new Music(f));

		final TreeItem<Music> root = new RecursiveTreeItem<Music>(Musics, RecursiveTreeObject::getChildren);
		treeView.getColumns().setAll(stitle, slength, sartist, sgenre);
		treeView.setRoot(root);
		treeView.setShowRoot(false);
	}

	@FXML
	public void changeVolume() {
		
		try {
			double vol = volume.getValue();
			vol = 65535 * (vol / 100);
			Runtime rt = Runtime.getRuntime();
			java.lang.Process pr = rt.exec("nircmd.exe setsysvolume " + vol);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
