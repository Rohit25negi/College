

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;

import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
//import javafx.event.ActionEvent;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
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
	
   
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	
    	/*Add first column into the TreeTableview
    	 * Song Title field will appear on the screen.*/ 
        JFXTreeTableColumn<Music, String> stitle = new JFXTreeTableColumn<>("Song Title");
        
        /*set the predefined width of the column*/
        stitle.setPrefWidth(300);
        
        /* the below line make changes to the cell 
		 * and also to determine what kind of data is displayed on the screen*/ 
        stitle.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Music, String>, ObservableValue<String>>() {
            
        	/*the method method defines what data will be displayed. 
        	 * Here the value of songTitle will be displayed on the screen */
        	@Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Music, String> param) {
                return param.getValue().getValue().songTitle;
            }
        });

        
        
        /*Add second column into the TreeTableview
    	 * Length field will appear on the screen.*/ 
        JFXTreeTableColumn<Music, String> slength = new JFXTreeTableColumn<>("Length");
        slength.setPrefWidth(90);
        slength.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Music, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Music, String> param) {
                return param.getValue().getValue().length;
            }
        });

        JFXTreeTableColumn<Music, String> sartist = new JFXTreeTableColumn<>("Artist");
        sartist.setPrefWidth(120);
        sartist.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Music, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Music, String> param) {
                return param.getValue().getValue().artist;
            }
        });

        JFXTreeTableColumn<Music, String> sgenre = new JFXTreeTableColumn<>("Genre");
        sgenre.setPrefWidth(110);
        sgenre.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Music, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Music, String> param) {
                return param.getValue().getValue().genre;
            }
        });
        
        ObservableList<Music> Musics = FXCollections.observableArrayList();
        Musics.add(new Music("asdh", "23", "asdh","12"));
        Musics.add(new Music("asdh", "23", "asdh","12"));
        Musics.add(new Music("asdh", "23", "asdh","12"));
        Musics.add(new Music("asdh", "23", "asdh","12"));
        Musics.add(new Music("asdh", "23", "asdh","12"));
        

        final TreeItem<Music> root = new RecursiveTreeItem<Music>(Musics, RecursiveTreeObject::getChildren);
        treeView.getColumns().setAll(stitle, slength, sartist,sgenre);
        treeView.setRoot(root);
        treeView.setShowRoot(false);

    }

    /*The following class defines
	 the properties/fields that will be displayed on the screen.*/
    class Music extends RecursiveTreeObject<Music> {

        StringProperty songTitle;
        StringProperty length;
        StringProperty artist;
        StringProperty genre;

        public Music(String songTitle, String length, String artist, String genre) {
            this.songTitle = new SimpleStringProperty(songTitle);
            this.length = new SimpleStringProperty(length);
            this.artist = new SimpleStringProperty(artist);
            this.genre = new SimpleStringProperty(genre);
        }

    }
    
    @FXML
    void loadDialogBox(ActionEvent event) {

    	JFXDialogLayout content = new JFXDialogLayout();
    	content.setHeading(new Text ("Please enter the size of the playlist:"));
    	content.setAlignment(Pos.CENTER);
    	JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
    	JFXTextField userdata = new JFXTextField();
    	
    	userdata.setMaxSize(300, 20);
    	content.getChildren().add(userdata);
    	
    	Button button = new Button("Ok");
    	
    	button.setAlignment(Pos.CENTER);
    	button.setOnAction(new EventHandler<ActionEvent>() {
    		String regex ="[0-9]+";	
			@Override
			public void handle(ActionEvent event) {
				System.out.println(userdata.getText());
				if(userdata.getText().matches(regex)){
					dialog.close();
				}
			}
		});
    	content.setActions(button);
    	dialog.show();
    	
    }
    public void play() { // play the music from staring of from middle.
		try {
			fin = new FileInputStream(files[current]);
			bin = new BufferedInputStream(fin);
			player = new Player(bin);
			if (starting_point > 0)
				fin.skip(starting_point);
			else
				total_length = fin.available();
			String str = files[current].toString();
			name.setText(files[current].getName());

			tracker.setMinimum(0);
			tracker.setMaximum((int) (total_length));

			/*
			 * below code is commented because this thread is hindering the song
			 * playing
			 */
			// Thread x=new Thread() {
			// public void run() {
			// player.
			// while (!player.isComplete())
			// try {
			// tracker.setValue((int) (total_length - fin.available()));
			// } catch (IOException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// }
			// };
			// x.setPriority(1);
			// x.start();

			Thread t = new Thread(new Runnable() {
				public void run() {
					try {

						player.play();
						System.out.println("called");
						if (player.isComplete()) { // Checking if the music is
													// completed or not

							next(); // if yes move to next music in the list
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			t.setPriority(10);
			t.start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void pause() { // Pause the music
		try {
			/*
			 * to get the pausing point so that the song can be continued from
			 * there later
			 */
			starting_point = total_length - fin.available();
			player.close();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	public void stop() { // stop the playing of song
		player.close();
		current = 0;
	}

	public void next() { // move to next song in the list
		if (current < files.length - 1) {
			current++;
			starting_point = -1;
			player.close();
			play(); // play the next song selected
		}
	}

	public void previous() { // move to previous song
		if (current > 0) {
			current--;
			starting_point = -1;
			player.close();
			play();
		}
	}
    @FXML
    void setDirectory()
    {
    	DirectoryChooser direc=new DirectoryChooser();
    	File file=direc.showDialog(null);
    	MusicOperation.directoryOpr(file);
    	
    }
    
    @FXML
    void selectSongs()
    {
    	FileChooser choose=new FileChooser();
    	FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Music files", "*.mp3","*.wav");
    	choose.getExtensionFilters().add(extFilter);
    	choose.showOpenMultipleDialog(null);
    }

   
}
