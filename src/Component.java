
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class Component {
	@FXML
    private AnchorPane anchorPane;

    @FXML
    private JFXHamburger hamburger;

    @FXML
    private JFXDrawer drawer;
	
	 public void loadcomponents(){
	    	try{
				VBox box = FXMLLoader.load(getClass().getResource("DrawerContent.fxml"));			
				drawer.setSidePane(box);
				//anchorPane.getChildren().add(FXMLLoader.load(getClass().getResource("LandingPage.fxml")));
				for(Node node : box.getChildren()){
					if(node.getAccessibleText() != null){
						node.addEventHandler(MouseEvent.MOUSE_CLICKED,(e) ->{
							switch(node.getAccessibleText()){
								case "Process_Control" : {
									//anchorPane.getChildren().removeAll();
									anchorPane.setBackground(new Background(new BackgroundFill(Paint.valueOf("#4CAF50"), CornerRadii.EMPTY,Insets.EMPTY )));
									break;
								}
														
								case "Playlist_Creator" : {
									//anchorPane.setBackground(new Background(new BackgroundFill(Paint.valueOf("#0288D1"), CornerRadii.EMPTY,Insets.EMPTY )));
									try{
										Parent music_player_parent = (FXMLLoader.load(getClass().getResource("MusicPlayer.fxml")));
										Scene music_player_scene = new Scene(music_player_parent);
										Stage music_stage = (Stage) ( (Node) anchorPane).getScene().getWindow();
										music_stage.setScene(music_player_scene);
										music_stage.show();
									
										anchorPane.getChildren().setAll(music_player_parent);
									break;
									}catch(Exception se){
										se.printStackTrace();
									}
								}
														
								case "Text_Simplifier" : anchorPane.setBackground(new Background(new BackgroundFill(Paint.valueOf("#C2185B"), CornerRadii.EMPTY,Insets.EMPTY )));
														break;
								case "Settings" : anchorPane.setBackground(new Background(new BackgroundFill(Paint.valueOf("#4E342E"), CornerRadii.EMPTY,Insets.EMPTY )));
														break;
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
					System.out.println("Hamburger Handler clicked");
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
