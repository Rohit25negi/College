package src;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;

import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
//import javafx.event.ActionEvent;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public class FXMLProcessController implements Initializable {

    @FXML
    private FlowPane main;
    @FXML
   JFXTreeTableView<Process> treeView;
    
    @FXML
    private AnchorPane processmain;

    @FXML
    private AnchorPane processanchorpane;
    @FXML
    private VBox processvbox;
    JFXTreeTableColumn<Process, String> pname;
    
    static FXMLProcessController var;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	var =this;
    	/*Add first column into the TreeTableview
    	 * Process Name field will appear on the screen.*/ 
       pname  = new JFXTreeTableColumn<>("Process Name");
        
        /*set the predefined width of the column*/
        pname.setPrefWidth(400);
        
        /* the below line make changes to the cell 
		 * and also to determine what kind of data is displayed on the screen*/ 
        pname.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Process, String>, ObservableValue<String>>() {
            
        	/*the method method defines what data will be displayed. 
        	 * Here the value of processName will be displayed on the screen */
        	@Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Process, String> param) {
                return param.getValue().getValue().processName;
            }
        });

        
        
        /*Add second column into the TreeTableview
    	 * memoryUsage field will appear on the screen.*/ 
  
        
		ObservableList<Process> Processs = FXCollections.observableArrayList();
       
        ArrayList<String>processes=JNAOperations.processList();
        
        for(String s: processes)
        {
        	Processs.add(new Process(s,null));
        }
       
        final TreeItem<Process> root = new RecursiveTreeItem<Process>(Processs, RecursiveTreeObject::getChildren);
        treeView.getColumns().setAll(pname);
        treeView.setRoot(root);
        treeView.setShowRoot(false);
        treeView.getSelectionModel().select(0);
        treeView.requestFocus();

    }
    @FXML
    void focusOn()
    {
    	TreeItem<Process>t=treeView.getSelectionModel().getSelectedItem();
    	if(t!=null)
    	{	String tempString=t.getValue().processName.getValue();
    		
    		tempString=tempString.substring(tempString.lastIndexOf('-')+1).trim();
    		
    		JNAOperations.toForeground(tempString);
    	}
    	
    }
    @FXML
   void closeProcess()
    {
    	
    	TreeItem<Process>t=treeView.getSelectionModel().getSelectedItem();
    	if(t!=null)
    	{	String tempString=t.getValue().processName.getValue();
    		
    		tempString=tempString.substring(tempString.lastIndexOf('-')+1).trim();
    		
    		JNAOperations.closeProcess(tempString);
    	}
    	
    	ObservableList<Process> Processs = FXCollections.observableArrayList();
        
        ArrayList<String>processes=JNAOperations.processList();
        
        for(String s: processes)
        {
        	Processs.add(new Process(s,null));
        }
       
        final TreeItem<Process> root = new RecursiveTreeItem<Process>(Processs, RecursiveTreeObject::getChildren);
        treeView.getColumns().setAll(pname);
        treeView.setRoot(root);
        treeView.setShowRoot(false);
        treeView.getSelectionModel().select(0);
        treeView.requestFocus();
    }
 
}
