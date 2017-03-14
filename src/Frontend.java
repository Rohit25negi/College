import java.awt.BorderLayout;
import java.net.HttpURLConnection;
import java.net.URL;
import java.awt.Button;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
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
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.sun.jna.platform.win32.WinDef.HWND;

import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
public class Frontend extends JFrame implements ActionListener {
	JButton button1, button2, button3, button4,simplify;
	JButton On, Off;
	JPanel leftp;
	JPanel rightp;
	JTextArea untagged, tagged;	//untagged area contains input Text, Tagged Area will contained output Text

	public Frontend() {
		super("ABDA");
		leftp = new JPanel();
		rightp = new JPanel();

		button1 = new JButton("click");
		button1.setBounds(50, 150, 100, 30);
		button1.addActionListener(this);

		button2 = new JButton("Text simplifier");
		button2.setBounds(50, 130, 100, 30);
		button2.addActionListener(this);

		button3 = new JButton("processes");
		button3.setBounds(50, 50, 100, 30);
		button3.addActionListener(this);

		button4 = new JButton("memory consumptions");
		button4.setBounds(50, 90, 100, 30);
		button4.addActionListener(this);

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
		// leftp.add(button1);
		leftp.add(button2);
		leftp.add(button3);
		leftp.add(button4);
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
					Runtime.getRuntime().exec(
							"wscript shortcut.vbs \"" + id.getText()
									+ ".LNK\" \"" + prog_path.getText() + "\"");

				} catch (Exception exc) {

				}
				JOptionPane.showMessageDialog(this, "shortbut created");
			}
		} else if (e.getSource() == button3) {
			showProcesses();
			rightp.repaint();
		} else if (e.getSource() == button2) {
			
			untagged=new JTextArea();
			untagged.setBounds(30,30,400,100);
			rightp.add(untagged);
			
			tagged=new JTextArea();
			tagged.setBounds(30,150,400,100);
			rightp.add(tagged);
			rightp.repaint();
			simplify=new JButton("Simplify");
			simplify.setBounds(10,280,200,40);
			rightp.add(simplify);
			simplify.addActionListener(this);
			rightp.add(simplify);
			rightp.setLayout(null);
			
			
		} else if (e.getSource() == button4) {
			showMemoryUseage();
			rightp.repaint();
		}else if(e.getSource()==simplify)	//author Rohit Negi, When user wants to simplify the Text
		{
			
			
		}
	}

	void showMusic() {	//	author Rohit Negi, Moto is to show the selected music playlist

		// new Music();
	}
	
	JTable content;

	void showMemoryUseage() {	//author Rohit Negi, Showing the memory usage of every process using the window's tasklist.exe program
		try {
			Process p = Runtime.getRuntime().exec("tasklist.exe /nh");
			BufferedReader in = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String str;
			String column[] = { "Service", "Memory Occupied" };
			ArrayList<String[]> list = new ArrayList();

			while ((str = in.readLine()) != null) {
				str = str.replaceAll(" +", " ");
				String s[] = str.split(" ");
				if (s.length >= 6)
					list.add(new String[] { s[0],
							s[s.length - 2] + " " + s[s.length - 1] });
			}
			String list2[][] = new String[list.size()][2];
			for (int i = 0; i < list2.length; i++) {
				list2[i][0] = list.get(i)[0];
				list2[i][1] = list.get(i)[1];
				System.out.println(list2[i][0] + ":" + list2[i][1]);
			}
			content = new JTable(list2, column);
			content.setBounds(30, 40, 200, 300);
			content.setRowSelectionInterval(0, 0);
			JScrollPane sp = new JScrollPane(content);
			

			rightp.setLayout(null);
		} catch (Exception e) {
			System.out.println(e);
		}

	}

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
					String name = (String) content.getValueAt(
							content.getSelectedRow(), 0);
					// content.remove(content.getSelectedRow());
					Test.toForeground(name);
				}
			}
		});

		rightp.add(content);

		rightp.setLayout(null);
	}
}