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
	static boolean On = false;
	static Frontend maincontrol;

	public static void main(String[] args) {

		new Thread(){public void run(){
			maincontrol = new Frontend();
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e) {
			}}
		}.start();


				try {
					URL url;
					if (args.length > 0) {
						url = new File(args[0]).toURI().toURL();
					} else {
						url = Main.class.getResource("helloworld.config.xml");
					}

					System.out.println("Loading...");

					ConfigurationManager cm = new ConfigurationManager(url);

					Recognizer recognizer = (Recognizer) cm
							.lookup("recognizer");
					Microphone microphone = (Microphone) cm
							.lookup("microphone");

					recognizer.allocate();

					if (microphone.startRecording()) {

						System.out.println("Say something:");
						Toolkit toolkit = Toolkit.getDefaultToolkit();
						Clipboard clipboard = toolkit.getSystemClipboard();
						while (true) {
							System.out
									.println("Start speaking. Press Ctrl-C to quit.\n");

							Result result = recognizer.recognize();

							if (result != null && Main.On) {
								String resultText = result
										.getBestFinalResultNoFiller();
								if (resultText.isEmpty())
									continue;
								System.out.println("You said: " + resultText
										+ "\n");
								// Runtime.getRuntime().exec("wscript.exe
								// ./say.vbs \"On It\"");
								switch (resultText) {
//								case "minimize":
//									Test.minimizeWindow();
//									break;
//								case "minimize all":
//									Test.minimizeAll();
//									break;
//								case "show processes":
//									maincontrol.showProcesses();
//									maincontrol.repaint();
//									break;
//								case "close":
//									try {
//										Test.closeProcess();
//										
//									} catch (Exception e) {
//
//									}
//									break;
//								case "focus":
//									try {
//										Test.toForeground((String) maincontrol.content
//												.getValueAt(maincontrol.content
//														.getSelectedRow(), 0));
//									} catch (Exception e) {
//
//									}
//									break;
//								case "down":
//									try {
//										int n = maincontrol.content
//												.getSelectedRow();
//										maincontrol.content
//												.setRowSelectionInterval(n + 1,
//														n + 1);
//									} catch (Exception e) {
//
//									}
//									break;
//								case "up":
//									try {
//										int n = maincontrol.content
//												.getSelectedRow();
//										maincontrol.content
//												.setRowSelectionInterval(n - 1,
//														n - 1);
//									} catch (Exception e) {
//
//									}
//									break;
//								case "open paint":
//									try {
//										Runtime.getRuntime().exec("mspaint");
//									} catch (Exception e) {
//
//									}
//									break;
//								case "open sublime":
//									try {
//										ProcessBuilder pb = new ProcessBuilder(
//												"cmd.exe", "/c", new File(
//														"application/"
//																+ "sublime"
//																+ ".LNK")
//														.getAbsolutePath());
//										pb.start();
//									} catch (Exception e) {
//
//									}
//									break;
								case "simplify":
									Robot robot=new Robot();
									
									  robot.keyPress(KeyEvent.VK_CONTROL);
									  robot.keyPress(KeyEvent.VK_C);
									  robot.keyRelease(KeyEvent.VK_C);
									  robot.keyRelease(KeyEvent.VK_CONTROL);
									  
									  
									  	Thread.sleep(2000);
										if (clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor))
								        {
								            String res = (String) clipboard.getData(DataFlavor.stringFlavor);
											String get=maincontrol.simplify(res);
											StringSelection selec= new StringSelection(get);
											   clipboard.setContents(selec, selec);
											 
												robot.keyPress(KeyEvent.VK_CONTROL);
												  robot.keyPress(KeyEvent.VK_V);
												  robot.keyRelease(KeyEvent.VK_V);
												  robot.keyRelease(KeyEvent.VK_CONTROL);
												  System.out.println("hello");
											
								        }
										
									break;
								}
								
							} else {

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
