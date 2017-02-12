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
import java.io.PrintStream;
import java.util.TreeMap;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.*;

public class Music extends JFrame implements ActionListener {
	JButton play, pause, next, previous, stop, selectList;
	JLabel name;
	JSlider tracker;
	File files[];
	int current=-1;
	FileInputStream fin;
	BufferedInputStream bin;
	public long starting_point=-1;
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
		this.add(play);
		this.add(pause);
		add(next);
		add(previous);
		add(stop);
		add(selectList);
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
			if(files!=null)
			{
				current=0;
				play();
			}
			

		} else if (obj == next) {
			next();
		} else if (obj == previous) {
			previous();
		}

	}
	public boolean storeMusicDetails()
	{
		
		return true;
	}
	public void selectFiles() {
		String list[] = { "mp3", "wav"};
		JFileChooser filechooser = new JFileChooser();
		filechooser.setMultiSelectionEnabled(true);
		filechooser.setFileFilter(new FileNameExtensionFilter("music file", list));
		filechooser.showOpenDialog(this);
		files = filechooser.getSelectedFiles();
		File f=files[0];
		try{
			FileInputStream fin=new FileInputStream(f);
			byte A[]=new byte[128];

		}catch(Exception e)
		{

		}
	}

	public void play(){
		try
		{
			fin=new FileInputStream(files[current]);
			bin=new BufferedInputStream(fin);
			player =new Player(bin);
			if(starting_point>0)
			fin.skip(starting_point);
			else total_length=fin.available();
			String str=files[current].toString();
			name.setText(str.substring(str.lastIndexOf("\\")+1));
			
			tracker.setMinimum(0);
			tracker.setMaximum((int)(total_length));
			new Thread(){
				public void run()
				{
					while(!player.isComplete())
						try {
							tracker.setValue((int)(total_length-fin.available()));
						} catch (IOException e) {
							// TODO Auto-generated catch block
						
						}
				}
			}.start();
			new Thread(new Runnable(){
				public void run()
				{	try{
					
				
					player.play();
					if(player.isComplete())
					{
						next();
					}
				}catch(Exception e)
				{
					
				}
				}
			}).start();
			
		}catch(Exception e)
		{
			
		}
	}
	public void pause()
	{
		try {
			starting_point=total_length-fin.available();
			player.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
	public void stop()
	{
		player.close();
		current=0;
	}
	public void next()
	{
		if(current<files.length-1)
		{
			current++;
			starting_point=-1;
			player.close();
			play();
		}
	}
	public void previous()
	{
		if(current>0)
		{
			current--;
			starting_point=-1;
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

class MusicDetails {

	TreeMap<Integer, String> sad, romantic, rock, other;
	String directory;

	MusicDetails(String dir) {
		sad = new TreeMap();
		romantic = new TreeMap();
		rock = new TreeMap();
		other = new TreeMap();
		directory = dir;
	}

	String determineType(String name) {
		String SAD[] = { "sad", "maut", "alone", "judai", "dard", "dukh", "bina", "bin", "sorry", "yaad", "intehaan" };
		String ROMANTIC[] = { "dil", "pyar", "love", "ishq", "jeena", "ashiqui", "chaha", "deewana", "rishta",
				"mohabbat", "Rastaa" };
		String ROCK[] = { "party", "dance", "nach", "club", "bhangra", "bandhu", "rab", "daru", "desi", "button",
				"pagalpan", "rock" };
		for (int i = 0; i < SAD.length; i++) {
			if (name.toLowerCase().contains(SAD[i])) {
				sad.put(sad.isEmpty() ? 1 : sad.lastKey() + 1, name);
				return "SAD";
			}
		}
		for (int i = 0; i < ROMANTIC.length; i++) {
			if (name.toLowerCase().contains(ROMANTIC[i])) {
				romantic.put(romantic.isEmpty() ? 1 : romantic.lastKey() + 1, name);
				return "ROMANTIC";
			}
		}
		for (int i = 0; i < ROCK.length; i++) {
			if (name.toLowerCase().contains(ROCK[i])) {
				rock.put(rock.isEmpty() ? 1 : rock.lastKey() + 1, name);
				return "ROCK";
			}
		}

		other.put(other.isEmpty() ? 1 : other.lastKey() + 1, name);
		return "OTHER";

	}

	boolean storeDetails(String[] A) {
		try {

			FileInputStream fin = new FileInputStream("record.txt");
			BufferedReader in = new BufferedReader(new InputStreamReader(fin));
			String type;
			int sad = 0, romantic = 0, rock = 0, other = 0;
			while ((type = in.readLine()) != null) {
				if (type.split(" ")[0].equals("sad"))
					sad = Integer.parseInt(type.split(" ")[1]);
				else if (type.split(" ")[0].equals("romantic"))
					romantic = Integer.parseInt(type.split(" ")[1]);
				else if (type.split(" ")[0].equals("rock"))
					rock = Integer.parseInt(type.split(" ")[1]);
				else
					other = Integer.parseInt(type.split(" ")[1]);
			}
			in.close();
			fin.close();
			for (int i = 0; i < A.length; i++) {
				switch (A[i]) {
				case "sad":
					sad++;
					break;
				case "romantic":
					romantic++;
					break;
				case "rock":
					rock++;
					break;
				default:
					other++;
				}
			}
			FileOutputStream fout = new FileOutputStream("record.txt");
			PrintStream out = new PrintStream(fout);
			out.println("sad " + sad);
			out.println("romantic " + romantic);
			out.println("rock " + rock);
			out.print("other " + other);
			out.close();
			fout.close();
		}

		catch (Exception e) {
			return false;
		}
		return true;

	}

	String[] createPlaylist(int n) {
		
		return null;
	}

}
