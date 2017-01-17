import javax.swing.*;

import com.sun.jna.platform.win32.WinDef.HWND;

import java.awt.event.*;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Set;
import java.util.TreeMap;

public class Application extends JFrame implements ActionListener {
	JTextField id, prog_path;
	JButton button, button2, button3;
	
	public Application(String name) {
		super(name);
		this.setBounds(100, 100, 400, 400);
		id = new JTextField();
		id.setBounds(50, 50, 100, 30);
		this.add(id);
		prog_path = new JTextField();
		prog_path.setBounds(200, 50, 100, 30);
		this.add(prog_path);
		button = new JButton("select the file");
		button.addActionListener(this);
		button.setBounds(50, 100, 100, 30);
		button2 = new JButton("open");
		button2.setBounds(50, 150, 100, 30);
		button2.addActionListener(this);
		button3 = new JButton("processes");
		button3.setBounds(50, 200, 100, 30);
		button3.addActionListener(this);
		this.add(button3);
		this.add(button);
		this.add(button2);
		this.setLayout(null);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == button) {

			if (!prog_path.getText().trim().isEmpty()) {
				File f2 = new File("./application");

				if (!f2.exists())
					f2.mkdir();
				try {
					Runtime.getRuntime()
							.exec("wscript shortcut.vbs \"" + id.getText() + ".LNK\" \"" + prog_path.getText() + "\"");

				} catch (Exception exc) {

				}
				JOptionPane.showMessageDialog(this, "shortbut created");

			}

		} else if (e.getSource() == button2) {
			String name = JOptionPane.showInputDialog("enter the application");
			try {
				BufferedReader in = new BufferedReader(
						new InputStreamReader(new FileInputStream("application/list.txt")));
				boolean found = false;
				String file;
				while ((file = in.readLine()) != null)
					if (file.split(" ")[0].equals(name)) {
						found = true;
						break;
					}

				if (found) {
					Runtime.getRuntime().exec(file.substring(file.indexOf(' ') + 1));
				} else {
					File f = new File("application");
					String list[] = f.list();
					found = false;
					for (String item : list)
						if (item.equals(name + ".LNK")) {
							found = true;
							break;
						}
					if (found) {
						ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c",
								new File("application/" + name + ".LNK").getAbsolutePath());
						pb.start();
					} else
						JOptionPane.showMessageDialog(this, "application named : " + name + " is not linked");
				}
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "application named : " + name + " not linked");
			}
		} else {
			

		}
	}

	public static void main(String argp[]) {
		new Application("add new");
	}
}
