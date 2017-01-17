import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.sun.jna.platform.win32.WinDef.HWND;

public class Frontend extends JFrame implements ActionListener {
	JButton button1, button2, button3;
	JButton On, Off;
	JPanel leftp;
	JPanel rightp;

	public Frontend() {
		super("ABDA");
		leftp = new JPanel();
		rightp = new JPanel();

		button1 = new JButton("Click");
		button1.setBounds(50, 50, 100, 30);
		button1.addActionListener(this);

		button2 = new JButton("Music");
		button2.setBounds(50, 100, 100, 30);
		button2.addActionListener(this);

		button3 = new JButton("processes");
		button3.setBounds(50, 150, 100, 30);
		button3.addActionListener(this);

		On = new JButton("ON");
		On.setBounds(30, 400, 70, 30);
		On.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Main.On = true;
			}
		});

		Off = new JButton("OFF");
		Off.setBounds(110, 400, 70, 30);
		Off.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Main.On = false;
			}
		});

		leftp.setBounds(0, 0, 190, 500);
		leftp.add(button1);
		leftp.add(button2);
		leftp.add(button3);
		leftp.add(On);
		leftp.add(Off);
		leftp.setLayout(null);

		rightp.setBounds(200, 0, 510, 500);
		rightp.setBackground(Color.gray);
		this.getContentPane().add(leftp);
		this.getContentPane().add(rightp);
		this.setBounds(100, 100, 700, 500);
		this.setLayout(null);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == button1) {

			JTextField id = new JTextField();
			id.setBounds(50, 50, 100, 30);
			rightp.add(id);
			JTextField prog_path = new JTextField();
			prog_path.setBounds(200, 50, 100, 30);
			rightp.add(prog_path);
			JButton button = new JButton("select the file");
			button.addActionListener(this);
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
		} else if (e.getSource() == button3) {
			showProcesses();
			rightp.repaint();
		} else if (e.getSource() == button2) {
			showMusic();
			rightp.repaint();
		}
	}

	void showMusic() {
		rightp.removeAll();
		JLabel directory = new JLabel("Select Music directory");
		directory.setBounds(30, 10, 300, 30);
		JButton select = new JButton("select");
		select.setBounds(100, 40, 200, 30);
		JFrame fram = this;
		select.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setDialogTitle("select the music directory");
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

				chooser.showOpenDialog(fram);
				File f = chooser.getSelectedFile();
				if (f != null) {
					directory.setText(f.toString());
					MusicDetails music = new MusicDetails(f.toString());

					String list[] = f.list();
					String[] header = { "song", "type" };
					String[][] songs = new String[list.length][2];

					for (int i = 0; i < list.length; i++) {
						songs[i][0] = list[i];
						songs[i][1] = music.determineType(list[i]);
						
					}
					DefaultTableModel model = new DefaultTableModel(songs, header);

					JTable table = new JTable(model);
					table.setBounds(50, 200,200,300);
					JScrollPane js = new JScrollPane(table);
					js.setVisible(true);
					rightp.add(js,BorderLayout.PAGE_END);
					
				}
			}
		});

		rightp.add(select);
		rightp.add(directory);
		rightp.setLayout(new BorderLayout());

	}
	JTable content;
	void showProcesses() {
		rightp.removeAll();
		ArrayList<String> processList = Test.processList();

		String column[] = { "Applications" };
		String List[][] = new String[processList.size()][1];
		int i = 0;
		for (String ss : processList)
			List[i++][0] = ss.substring(ss.lastIndexOf('-') + 1).trim();

		content = new JTable(List, column);
		content.setBounds(30, 40, 200, 300);
		content.setRowSelectionInterval(0, 0);
		JScrollPane sp = new JScrollPane(content);
		
		content.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					String name = (String) content.getValueAt(content.getSelectedRow(), 0);
					// content.remove(content.getSelectedRow());
					Test.toForeground(name);
				}
			}
		});
		
		
		rightp.add(content);
		JButton close = new JButton("close");
		close.setBounds(50, 350, 100, 30);
		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int num = content.getSelectedRow();
				String name = (String) content.getValueAt(num, 0);
				Test.closeProcess(name);
				showProcesses();
				repaint();

			}
		});
		rightp.add(close);
		rightp.setLayout(null);
	}
}