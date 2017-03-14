
/**
 * AUTHOR: 
 * Name - ROhit Negi
 * Roll No - 130101144
 * Sys id -2013014812
 * This is the Main file which contains the code for speech recognition and invoking the  UI of the application
 * This file also contains the code to invoke various function for various words extracted from speech recognition system.
 */
import edu.cmu.sphinx.frontend.util.Microphone;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.Result;
import edu.cmu.sphinx.util.props.ConfigurationManager;
import edu.cmu.sphinx.util.props.PropertyException;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.swing.JPanel;
import javax.swing.UIManager;
import java.awt.Robot;

public class Main {
	/* On variable: used to check whether to detect the voice or not. Default=Off*/
	static boolean On = false; 
	static Frontend maincontrol; // Object Reference Varible for UI

	public static void main(String[] args) {

		/*
		 * Since application allows the user to interact with the UI as well as
		 * give the input with voice. Both these tasks work in parallel. Hence
		 * There is need to make the threads. Displaying and handling UI is done
		 * on new thread as done below.
		 */
		new Thread() {
			public void run() {
				maincontrol = new Frontend();
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (Exception e) {
				}
			}
		}.start();

		/*
		 * below is the code to handle the voice recognition. Voice is captured
		 * and converted into text only when value of On variable is True.
		 */
		try {
			URL url;

			url = Main.class.getResource("helloworld.config.xml");

			System.out.println("Loading...");

			ConfigurationManager cm = new ConfigurationManager(url);

			Recognizer recognizer = (Recognizer) cm // Lookup for speech
													// recognizer
					.lookup("recognizer");
			Microphone microphone = (Microphone) cm.lookup("microphone");

			recognizer.allocate();

			/*If Microphone is ready to record*/
			if (microphone.startRecording()) { 

				System.out.println("Say something:");

				while (true) {
					System.out.println("Start speaking. Press Ctrl-C to quit.\n");

					/*the recorded result from mic will be stored in result variable below*/
					Result result = recognizer.recognize(); 
					
					/* Detect the text only when On is true */
					if (result != null && Main.On) {
						/*best possible word which is closest to the spoken word is returned*/
						String resultText = result.getBestFinalResultNoFiller();
						if (resultText.isEmpty())
							continue;
						System.out.println("You said: " + resultText + "\n");

						switch (resultText) {
						case "minimize":
							/* TO minimize the currently focused window */
							Test.minimizeWindow();
							break;
						case "minimize all":
							/* TO minimize all the windows */
							Test.minimizeAll();
							break;
						case "show processes":
							/*
							 * to show all the process running which have
							 * windows
							 */
							maincontrol.showProcesses();
							maincontrol.repaint();
							break;
						case "close":
							try {
								// close the process which is in the focus
								// currently
								Test.closeProcess();

							} catch (Exception e) {
								e.printStackTrace();
							}
							break;
						case "focus":
							try {
								/*
								 * Focus on the select program
								 */
								Test.toForeground((String) maincontrol.content
										.getValueAt(maincontrol.content.getSelectedRow(), 0));
							} catch (Exception e) {

							}
							break;
						case "down":
							try {
								// to go down in the list of processes
								int n = maincontrol.content.getSelectedRow();
								maincontrol.content.setRowSelectionInterval(n + 1, n + 1);
							} catch (Exception e) {

							}
							break;
						case "up":
							try {
								// to go up in the list of processes
								int n = maincontrol.content.getSelectedRow();
								maincontrol.content.setRowSelectionInterval(n - 1, n - 1);
							} catch (Exception e) {

							}
							break;

						case "simplify":
							/*
							 * code in this case is used to simplify the input
							 * text. Text can be captured from any other
							 * application running on current PC. This is
							 * achieved by using the Robot class in java.
							 * 
							 * User will select any text in any application and
							 * way 'simplify'. Then our application will fetch
							 * the selected text and simplify it using the Code
							 * stored in Wordnet.java(see Documentation).
							 * 
							 * Selected text is replaced with the simplified
							 * text.
							 */
							Robot robot = new Robot();

							// Mimicking the ctrl+c. selected text will be
							// transfered to system clipboard
							robot.keyPress(KeyEvent.VK_CONTROL);
							robot.keyPress(KeyEvent.VK_C);
							robot.keyRelease(KeyEvent.VK_C);
							robot.keyRelease(KeyEvent.VK_CONTROL);

							Toolkit toolkit = Toolkit.getDefaultToolkit();
							Clipboard clipboard = toolkit.getSystemClipboard();

							Thread.sleep(2000);

							if (clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {

								// reading the clipboard content
								String res = (String) clipboard.getData(DataFlavor.stringFlavor);

								// Simplifying the Text. See Documentation of
								// Dictionary.java and Wordnet.java
								Dictionary get = new Dictionary(res);
								get.tagText();
								String simplifiedText = get.simplify();

								// Transferring simplified text to clipboard
								StringSelection selec = new StringSelection(simplifiedText);
								clipboard.setContents(selec, selec);

								// Mimicking ctrl+v which will replace the
								// selected text with simplified text
								robot.keyPress(KeyEvent.VK_CONTROL);
								robot.keyPress(KeyEvent.VK_V);
								robot.keyRelease(KeyEvent.VK_V);
								robot.keyRelease(KeyEvent.VK_CONTROL);
								System.out.println("hello");

							}

							break;
						}

					}
				}
			} else {
				System.out.println("Cannot start microphone.");
				recognizer.deallocate();
				System.exit(1);
			}
		} catch (IOException e) {
			System.err.println("Problem when loading HelloWorld: " + e);
			e.printStackTrace();
		} catch (PropertyException e) {
			System.err.println("Problem configuring HelloWorld: " + e);
			e.printStackTrace();
		} catch (InstantiationException e) {
			System.err.println("Problem creating HelloWorld: " + e);
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
