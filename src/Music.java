import javazoom.jl.player.Player;
import javazoom.jl.player.advanced.AdvancedPlayer;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.TreeMap;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.FileDialog;
import java.awt.event.*;
import java.util.Set;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.apache.tika.sax.BodyContentHandler;
import java.util.Random;

public class Music extends JFrame implements ActionListener {
	JButton play, pause, next, previous, stop, selectList, selectDIR, createPlayList;
	JLabel name;
	JSlider tracker;
	File files[];
	int current = -1;
	FileInputStream fin;
	BufferedInputStream bin;
	public long starting_point = -1;
	public long total_length;
	Player player;

	Music() {
		this.setBounds(100, 100, 500, 400);
		name = new JLabel("Music name");
		name.setBounds(50, 20, 300, 30);

		tracker = new JSlider(JSlider.HORIZONTAL);
		tracker.setBounds(50, 60, 300, 30);
		tracker.setValue(0);

		play = new JButton("Play");
		play.setBounds(50, 100, 100, 30);
		play.addActionListener(this);

		pause = new JButton("Pause");
		pause.setBounds(160, 100, 100, 30);
		pause.addActionListener(this);
		next = new JButton("next");
		next.setBounds(270, 100, 100, 30);
		next.addActionListener(this);
		previous = new JButton("previous");
		previous.setBounds(380, 100, 100, 30);
		previous.addActionListener(this);
		stop = new JButton("stop");
		stop.setBounds(490, 100, 100, 30);
		stop.addActionListener(this);
		selectList = new JButton("Select List");
		selectList.setBounds(100, 150, 100, 30);
		selectList.addActionListener(this);

		selectDIR = new JButton("set directory");
		selectDIR.setBounds(100, 190, 100, 30);
		selectDIR.addActionListener(this);

		createPlayList = new JButton("create Playlist");
		createPlayList.setBounds(100, 220, 100, 30);
		createPlayList.addActionListener(this);

		this.add(play);
		this.add(pause);
		add(next);
		add(previous);
		add(stop);
		add(selectList);
		add(selectDIR);
		add(createPlayList);
		add(name);
		add(tracker);
		this.setLayout(null);
		this.setVisible(true);
		this.setDefaultCloseOperation(3);
	}

	public void actionPerformed(ActionEvent a) {
		Object obj = a.getSource();
		if (obj == play) {
			play();
		} else if (obj == pause) {
			pause();
		} else if (obj == stop) {
			stop();
		} else if (obj == selectList) {
			selectFiles();
			storeMusicDetails();
			if (files != null) {
				current = 0;
				play();
			}

		} else if (obj == next) {
			next();
		} else if (obj == previous) {
			previous();
		} else if (obj == selectDIR) {
			directoryOpr();
		} else if (obj == createPlayList) {
			Integer size = Integer.parseInt(JOptionPane.showInputDialog("enter the size of playlist"));

			if (size != null) {
				createPlayList(size.intValue());

			}
		}

	}

	public void createPlayList(int size) {
		try {
			FileInputStream fin = new FileInputStream(new File("past.record"));
			ObjectInputStream oin = new ObjectInputStream(fin);
			TreeMap<String, Integer> percent = new TreeMap();
			TreeMap<String, Integer> x;
			int total = 0;
			System.out.println(oin.available());
			while (true) {
				try {
					x = (TreeMap<String, Integer>) oin.readObject();
				} catch (Exception e) {
					break;
				}
				Set<String> S = x.keySet();
				for (String key : S) {
					total += x.get(key);
		
					if (!percent.containsKey(key))
						percent.put(key, x.get(key));
					else
						percent.put(key, percent.get(key) + x.get(key));

				}

			}
			oin.close();

			fin = new FileInputStream("music.list");
			oin = new ObjectInputStream(fin);
			TreeMap<String, ArrayList<String>> musicList;
			musicList = (TreeMap<String, ArrayList<String>>) oin.readObject();

			Set<String> keys = percent.keySet();
			ArrayList<File> tempFiles = new ArrayList();
			ArrayList<String> songs = new ArrayList();
			int count;
			Random random = new Random();

			for (String key : keys) {
				count = (int) ((percent.get(key) / (float) total) * size);
				songs = musicList.get(key);

				System.out.println(key+":"+percent.get(key)+":"+total+":"+count);
				while (count-- != 0 && !songs.isEmpty())
					tempFiles.add(new File(songs.remove(random.nextInt(songs.size()))));

			}
			System.out.println(tempFiles);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getGenere(File file) throws Exception {
		BodyContentHandler handler = new BodyContentHandler();
		Metadata metadata = new Metadata();
		ParseContext pcontext = new ParseContext();
		Mp3Parser Mp3Parser = new Mp3Parser();
		FileInputStream inputstream = new FileInputStream(file);
		Mp3Parser.parse(inputstream, handler, metadata, pcontext);
		String genere = metadata.get("xmpDM:genre");
		inputstream.close();
		return genere;
	}

	public boolean directoryOpr() {
		FileDialog file = new FileDialog(this, "load", FileDialog.LOAD);
		file.setMultipleMode(true);
		file.setVisible(true);
		File files[] = file.getFiles();

		try {
			FileOutputStream fout = new FileOutputStream("temp.list");
			ObjectOutputStream out = new ObjectOutputStream(fout);

			TreeMap<String, ArrayList<String>> musicList = new TreeMap();
			for (int i = 0; i < files.length; i++) {

				String genere = getGenere(files[i]);

				if (genere == null || genere.trim().isEmpty())
					genere = "other";

				if (musicList.containsKey(genere)) {
					musicList.get(genere).add(files[i].toString());
				} else {
					ArrayList<String> temp = new ArrayList();
					temp.add(files[i].toString());
					musicList.put(genere, temp);
				}
			}
			out.writeObject(musicList);
			JOptionPane.showMessageDialog(this, "done");
			out.close();
			new File("temp.list").renameTo(new File("music.list"));
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			new File("temp.list").delete();
			return false;
		}

	}

	public boolean storeMusicDetails() {
		if (this.files != null) {
			TreeMap<String, Integer> record = new TreeMap();
			try {
				for (int i = 0; i < this.files.length; i++) {
					String genere = getGenere(this.files[i]);
					if (genere == null || genere.trim().isEmpty())
						genere = "other";

					if (!record.containsKey(genere))
						record.put(genere, 1);
					else
						record.put(genere, 1 + record.get(genere));

				}

				File pastrecord = new File("past.record");

				if (pastrecord.exists()) {
					TreeMap<String, Integer> recordList[] = new TreeMap[10];
					FileInputStream fin = new FileInputStream(pastrecord);
					ObjectInputStream oin = new ObjectInputStream(fin);
					int i = 0;
					TreeMap<String, Integer> temptree;
					while (true) {
						try {
							temptree = (TreeMap<String, Integer>) oin.readObject();
						} catch (Exception e) {
							break;
						}
						recordList[i++] = temptree;
					}
					oin.close();

					i %= 10;
					recordList[i] = record;
					i *= i;
					i = (1 - i) / (1 + i);
					FileOutputStream fout = new FileOutputStream("past.record");
					ObjectOutputStream out = new ObjectOutputStream(fout);

					do {
						i %= 10;
						out.writeObject(recordList[i]);
					} while (recordList[i++] != record);
					out.close();
				} else {
					FileOutputStream fout = new FileOutputStream("past.record");
					ObjectOutputStream out = new ObjectOutputStream(fout);

					out.writeObject(record);
					out.close();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public void selectFiles() {
		String list[] = { "mp3", "wav" };
		JFileChooser filechooser = new JFileChooser();
		filechooser.setMultiSelectionEnabled(true);
		filechooser.setFileFilter(new FileNameExtensionFilter("music file", list));
		filechooser.showOpenDialog(this);
		files = filechooser.getSelectedFiles();

	}

	public void play() {
		try {
			fin = new FileInputStream(files[current]);
			bin = new BufferedInputStream(fin);
			player = new Player(bin);
			if (starting_point > 0)
				fin.skip(starting_point);
			else
				total_length = fin.available();
			String str = files[current].toString();
			name.setText(str.substring(str.lastIndexOf("\\") + 1));

			tracker.setMinimum(0);
			tracker.setMaximum((int) (total_length));
			new Thread() {
				public void run() {
					while (!player.isComplete())
						try {
							tracker.setValue((int) (total_length - fin.available()));
						} catch (IOException e) {
							// TODO Auto-generated catch block

						}
				}
			}.start();
			new Thread(new Runnable() {
				public void run() {
					try {

						player.play();
						if (player.isComplete()) {
							next();
						}
					} catch (Exception e) {

					}
				}
			}).start();

		} catch (Exception e) {

		}
	}

	public void pause() {
		try {
			starting_point = total_length - fin.available();
			player.close();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	public void stop() {
		player.close();
		current = 0;
	}

	public void next() {
		if (current < files.length - 1) {
			current++;
			starting_point = -1;
			player.close();
			play();
		}
	}

	public void previous() {
		if (current > 0) {
			current--;
			starting_point = -1;
			player.close();
			play();
		}
	}

	public static void main(String ar[]) throws Exception {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}
		new Music();

	}
}
