package src;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class FXMLDocumentController extends Application implements Initializable {

		@FXML
	    private AnchorPane anchorPane;

	    @FXML
	    private JFXHamburger hamburger;

	    @FXML
	    private JFXDrawer drawer;
	    
	    @FXML
	    private AnchorPane subanchorPane;
	    
	    @FXML
	    private StackPane drawerStackPane;

	    @FXML
		public JFXToggleButton audioInput;
		@Override
		public void start(Stage primaryStage) throws Exception {
			// TODO Auto-generated method stub
			try {
				Parent root =FXMLLoader.load(getClass().getResource("demo.fxml")); 
				 
				//String css = Scale.class.getResource("application.css").toExternalForm();
				Scene scene = new Scene(root);
				primaryStage.setScene(scene);
				primaryStage.setTitle("ABDA - Your Personal Assistant");
				primaryStage.show();
			} catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		public static void createScene()
		{
			launch(null);
		}
		public void complementInput()
		{
			Main.On=!Main.On;
		}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try{
			VBox box = FXMLLoader.load(getClass().getResource("DrawerContent.fxml"));			
			drawer.setSidePane(box);
			
			subanchorPane.getChildren().add(FXMLLoader.load(getClass().getResource("LandingPage.fxml")));
			for(Node node : box.getChildren()){
				if(node.getAccessibleText() != null){
					node.addEventHandler(MouseEvent.MOUSE_CLICKED,(e) ->{
						switch(node.getAccessibleText()){
							case "Process_Control" : {
								try{
									subanchorPane.getChildren().clear();
									subanchorPane.getChildren().add(FXMLLoader.load(getClass().getResource("ProcessList.fxml")));
									}catch(Exception processex){
										processex.printStackTrace();	
									}
								break;
							}
							
							case "Process_MemoryUsage" : {
								try{
									subanchorPane.getChildren().clear();
									subanchorPane.getChildren().add(FXMLLoader.load(getClass().getResource("MemoryUsageList.fxml")));
									}catch(Exception processex){
										processex.printStackTrace();	
									}
								break;
							}
													
							case "Playlist_Creator" : {
								try{	
								subanchorPane.getChildren().clear();	
								subanchorPane.getChildren().add(FXMLLoader.load(getClass().getResource("MusicPlayer.fxml")));
								}catch(Exception ex){
									ex.printStackTrace();
									
								}
							
								//subanchorPane.setBackground(new Background(new BackgroundFill(Paint.valueOf("#0288D1"), CornerRadii.EMPTY,Insets.EMPTY )));
								/*try{
									Parent music_player_parent = (FXMLLoader.load(getClass().getResource("MusicPlayer.fxml")));
									Scene music_player_scene = new Scene(music_player_parent);
									Stage music_stage = (Stage) ( (Node) subanchorPane).getScene().getWindow();
									music_stage.setScene(music_player_scene);
									music_stage.show();
								
									subanchorPane.getChildren().setAll(music_player_parent);
								break;
								}catch(Exception se){
									se.printStackTrace();
								}*/
								break;
							}
													
							case "Text_Simplifier" : subanchorPane.setBackground(new Background(new BackgroundFill(Paint.valueOf("#C2185B"), CornerRadii.EMPTY,Insets.EMPTY )));
													break;
							case "Settings" : subanchorPane.setBackground(new Background(new BackgroundFill(Paint.valueOf("#4E342E"), CornerRadii.EMPTY,Insets.EMPTY )));
													break;
							case "About" :{
								
											break;
							}
							case "Exit_Option" : System.exit(0);
							break;
						}
					});
				}
			}
			
			/*Hamburger will be translated to backarrow when one clicks the mouse button */
			HamburgerBackArrowBasicTransition burgerTask2 = new HamburgerBackArrowBasicTransition(hamburger);
			burgerTask2.setRate(-1);
			hamburger.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
				//System.out.println("Hamburger Handler clicked");
				burgerTask2.setRate(burgerTask2.getRate()*-1);
				burgerTask2.play();
				
				if(drawer.isShown()){
					drawer.close();
				}
				else{
					drawer.open();
				}
				
			});
		}catch(Exception e){
			e.printStackTrace();
		}
			
	}
	
	
	 

}
