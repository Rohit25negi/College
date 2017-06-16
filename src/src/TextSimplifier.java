package src;

import java.net.URL;
import java.net.URLConnection;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.*;

public class TextSimplifier {

	@FXML
	TextArea inputString, outputString;

	@FXML
	void updateDictionary() {
		

		try {
			URL url = new URL("https://transpersonal-speci.000webhostapp.com/givewordnet.php");
			HttpURLConnection urlcon = (HttpURLConnection) url.openConnection();
			urlcon.setRequestMethod("GET");
			urlcon.connect();
			BufferedReader in = new BufferedReader(new InputStreamReader(urlcon.getInputStream()));
			String dict="",temp;
			
			while((temp=in.readLine())!=null)
				dict += temp;

			FileOutputStream fout =new FileOutputStream("Wordnet.json");
			fout.write(dict.getBytes());
			fout.close();
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Alert");
			alert.setHeaderText(null);
			
			alert.setContentText("Dictionary is Updated");
			alert.showAndWait();
			urlcon.disconnect();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	void simplify() {
		String untagged = inputString.getText();
		Dictionary dict = new Dictionary(untagged);
		dict.tagText();

		try {
			String simplifiedText = dict.simplify();
			outputString.setText(simplifiedText);

		} catch (Exception e) {
			Dialog d = new Dialog();
			d.setContentText("Internal Erro");
			d.showAndWait();
			inputString.setText("");
		}

	}

}
