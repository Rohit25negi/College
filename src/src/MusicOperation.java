package src;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JOptionPane;

import javazoom.jl.player.Player;

public class MusicOperation {
	public static List<File> getFiles() {
		return files;
	}


	public static List<File> files;
	public static int current = -1;
	public static FileInputStream fin;
	public static BufferedInputStream bin;
	 static public long starting_point = -1;
	 static public long total_length;
	public static Player player;

	public static boolean directoryOpr(File file) { // to store the details of
													// the songs present
		// in the music directory

		File files[] = file.listFiles();

		try {
			/*
			 * temp.list is just a temporary file to store the temporary music
			 * list
			 */
			FileOutputStream fout = new FileOutputStream("temp.list");

			ObjectOutputStream out = new ObjectOutputStream(fout);

			/* Treemap is used to maintain the key value pair */
			TreeMap<String, ArrayList<String>> musicList = new TreeMap();

			for (int i = 0; i < files.length; i++) {

				String genere = new Music(files[i]).getGenere(); // get the
																	// genre of
																	// song

//				System.out.println(i);

				if (musicList.containsKey(genere)) {
					musicList.get(genere).add(files[i].toString());
				} else {
					ArrayList<String> temp = new ArrayList();
					temp.add(files[i].toString());
					musicList.put(genere, temp);
				}
			}
			
			out.writeObject(musicList);
		
			JOptionPane.showMessageDialog(null, "done");
			out.close();
			/*
			 * if successfully stored the records then replace the file with new
			 * record
			 */
			new File("temp.list").renameTo(new File("music.list"));
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			new File("temp.list").delete(); // if not successfully created the
			// record delete the temprory file
			return false;
		}

	}

	public static boolean storeMusicDetails() { // Storing music record as
												// history in
		// past.record file
		if (files != null) {
			TreeMap<String, Integer> record = new TreeMap();
			try {
				for (int i = 0; i < files.size(); i++) {

					String genere = new Music(files.get(i)).getGenere();
					if (genere == null || genere.trim().isEmpty())
						genere = "other";

					if (!record.containsKey(genere))
						record.put(genere, 1);
					else
						record.put(genere, 1 + record.get(genere));

				}

				File pastrecord = new File("past.record");

				if (pastrecord.exists()) {

					/*
					 * since only past 10 records are to be maintained therefore
					 * the size of array if 10
					 */
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

	public static void createPlayList(int size) {
		try {

			/* past.record file contains the user's music listening history */
			FileInputStream fin = new FileInputStream(new File("past.record"));

			ObjectInputStream oin = new ObjectInputStream(fin);

			/*
			 * Tree Map is used to maintain the key:value pair and it is also
			 * fast in searching
			 */
			TreeMap<String, Integer> percent = new TreeMap();

			TreeMap<String, Integer> x;
			int total = 0;
			
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

			fin = new FileInputStream("music.list"); // music.list file contains
														// the information about
														// the music files
			oin = new ObjectInputStream(fin);
			TreeMap<String, ArrayList<String>> musicList;
			musicList = (TreeMap<String, ArrayList<String>>) oin.readObject();

			Set<String> keys = percent.keySet();
			ArrayList<File> tempFiles = new ArrayList();
			ArrayList<String> songs = new ArrayList();
			int count;
			Random random = new Random();

			for (String key : keys) {
				/*
				 * count of each type of song which is to be inserted into the
				 * list
				 */
				count = (int) ((percent.get(key) / (float) total) * size);

				songs = musicList.get(key);

//				System.out.println(key + ":" + percent.get(key) + ":" + total + ":" + count);

				/* storing the 'count' number of songs of this type */
				while (count-- != 0 && !songs.isEmpty())
					tempFiles.add(new File(songs.remove(random.nextInt(songs.size()))));

			}
			int cursize = tempFiles.size();

			keys = percent.keySet();
			Collection<Integer> values = percent.values();
			String keyList[] = new String[keys.size()];
			int valueList[] = new int[keys.size()];

			int i = 0;
			for (String xx : keys) {
				keyList[i++] = xx;
			}
			i = 0;
			for (int xx : values) {
				valueList[i++] = xx;
			}

			for (int j = 0; j < keyList.length - 1; j++) {
				int pos = j;
				int max = valueList[j];
				for (int k = j + 1; k < keyList.length; k++) {
					if (valueList[k] > max) {
						pos = k;
						max = valueList[k];
					}

				}
				valueList[pos] = valueList[j];
				valueList[j] = max;

				String temp = keyList[pos];
				keyList[pos] = keyList[j];
				keyList[j] = temp;
			}

			OUTER: for (i = 0; i < keyList.length; i++) {
				songs = musicList.get(keyList[i]);

				while (!songs.isEmpty()) {
					if (++cursize > size)
						break OUTER;
					/* filling the remaining playlist */
					tempFiles.add(new File(songs.remove(random.nextInt(songs.size()))));

				}
			}

			MusicOperation.files = tempFiles;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void selectFiles(List<File> files) {
		// restricting the filetype

		MusicOperation.files = files;

	}
}