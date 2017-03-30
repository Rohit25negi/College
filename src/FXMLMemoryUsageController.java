
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.net.URL;
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

public class FXMLMemoryUsageController implements Initializable {

    @FXML
    private FlowPane main;
    @FXML
    private JFXTreeTableView<Process> treeView;
    
    @FXML
    private AnchorPane processmain;

    @FXML
    private AnchorPane processanchorpane;
    @FXML
    private VBox processvbox;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	
    	/*Add first column into the TreeTableview
    	 * Process Name field will appear on the screen.*/ 
        JFXTreeTableColumn<Process, String> pname = new JFXTreeTableColumn<>("Process Name");
        
        /*set the predefined width of the column*/
        pname.setPrefWidth(150);
        
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
        JFXTreeTableColumn<Process, String> smemoryUsage = new JFXTreeTableColumn<>("memoryUsage");
        smemoryUsage.setPrefWidth(150);
        smemoryUsage.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Process, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Process, String> param) {
                return param.getValue().getValue().memoryUsage;
            }
        });

        
        
        ObservableList<Process> Processs = FXCollections.observableArrayList();
        Processs.add(new Process("asdh", "23"));
        Processs.add(new Process("asdh", "23"));
        Processs.add(new Process("asdh", "23"));
        Processs.add(new Process("asdh", "23"));
        Processs.add(new Process("asdh", "23"));
        

        final TreeItem<Process> root = new RecursiveTreeItem<Process>(Processs, RecursiveTreeObject::getChildren);
        //treeView.getColumns().setAll(pname, smemoryUsage);
       // treeView.setRoot(root);
        //treeView.setShowRoot(false);

    }

    /*The following class defines
	 the properties/fields that will be displayed on the screen.*/
    class Process extends RecursiveTreeObject<Process> {

        StringProperty processName;
        StringProperty memoryUsage;
       

        public Process(String processName, String memoryUsage) {
            this.processName = new SimpleStringProperty(processName);
            this.memoryUsage = new SimpleStringProperty(memoryUsage);
            
        }

    }
}
