package src;
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
import javafx.application.Platform;
import uicontrol.FXMLDocumentController;
import uicontrol.FXMLProcessController;

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
	public static boolean On = false; 
	

	public static void main(String[] args) {

		/*
		 * Since application allows the user to interact with the UI as well as
		 * give the input with voice. Both these tasks work in parallel. Hence
		 * There is need to make the threads. Displaying and handling UI is done
		 * on new thread as done below.
		 */
		

		/*
		 * below is the code to handle the voice recognition. Voice is captured
		 * and converted into text only when value of On variable is True.
		 */
		Thread t=new Thread()
				{
			public void run()
			{
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
									JNAOperations.minimizeWindow();
									break;
								case "minimize all":
									/* TO minimize all the windows */
									JNAOperations.minimizeAll();
									break;
								case "process list":
									/*
									 * to show all the process running which have
									 * windows
									 */
									Platform.runLater(new Runnable(){
										public void run(){
											FXMLDocumentController.var.processList();
										}
									});
//									maincontrol.showProcesses();
//									maincontrol.repaint();
									break;
								case "close":
									try {
										// close the process which is in the focus
										// currently
										JNAOperations.closeProcess();

									} catch (Exception e) {
										e.printStackTrace();
									}
									break;
								case "kill":
									try {
										// close the process which is in the focus
										// currently
										Platform.runLater(new Runnable(){ 
											public void run(){FXMLProcessController.var.closeProcess();}
										});
									} catch (Exception e) {
										
									}
									break;
								case "focus":
									try {
										FXMLProcessController.var.focusOn();
									} catch (Exception e) {

									}
									break;
								case "next":
									try {
										int sel=FXMLProcessController.var.treeView.getSelectionModel().getSelectedIndex();
										FXMLProcessController.var.treeView.getSelectionModel().select(sel+1);
										FXMLProcessController.var.treeView.requestFocus();
									} catch (Exception e) {

									}
									break;
								case "previous":
									try {
										int sel=FXMLProcessController.var.treeView.getSelectionModel().getSelectedIndex();
										if(sel>=1)
										{FXMLProcessController.var.treeView.getSelectionModel().select(sel-1);
										FXMLProcessController.var.treeView.requestFocus();}
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
//										System.out.println("hello");

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
				};
				t.setDaemon(true);
				t.start();
				
				FXMLDocumentController.createScene();

	}
}
