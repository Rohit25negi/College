package src;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Process extends RecursiveTreeObject<Process> {

    private StringProperty processName;
    private StringProperty memory;
   

    public Process(String processName,String memory) {
        this.processName = new SimpleStringProperty(processName);
        this.memory = new SimpleStringProperty(memory);
     
        
    }
    public StringProperty getMemory()
    {
    	return memory;
    }
    public StringProperty getProcessName()
    {
    	return processName;
    }

}