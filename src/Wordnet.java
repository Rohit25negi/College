
/**
 * AUTHOR: ROHIT NEGI, CSE-B, Roll No- 130101144
 * 
 */
import org.json.simple.JSONObject;
import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;
import java.io.*;
import java.util.ArrayList;

public class Wordnet {
	private JSONObject wordnet;

	Wordnet() {
		try {
			File file = new File("Wordnet.json");
			if (!file.exists())
				file.createNewFile();

			JSONParser parser = new JSONParser();
			JSONObject jObject = (JSONObject) parser.parse(new FileReader("Wordnet.json"));
			this.wordnet = jObject;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public JSONObject getWordnet() {
		return wordnet;
	}

	public boolean insertWord(String word, String pos, String... metadata) {
		String parent = null, child = null;
		ArrayList<String> parents = new ArrayList();
		ArrayList<String> children = new ArrayList();

		if (metadata.length > 0) {
			parent = metadata[0];
			if (parent != null)
				parents.add(parent);

			if (metadata.length > 1) {
				child = metadata[1];

				if (child != null)
					children.add(child);
			}
		}
		String previousChild = null;

		OUTER: for (int i = 0; i < parents.size(); i++) {
			ArrayList<String> chill = ((ArrayList<String>) ((JSONObject) this.wordnet.get(parents.get(i)))
					.get("children"));
			for (int j = 0; j < chill.size(); j++) {
				if (((String) ((JSONObject) this.wordnet.get(chill.get(j))).get("pos")).equals(pos)) {
					previousChild = chill.get(j);
					break OUTER;
				}
			}
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("pos", pos);
		jsonObject.put("parents", parents);
		if (previousChild == null) {

			jsonObject.put("children", children);

			if (parent != null)
				((ArrayList<String>) ((JSONObject) this.wordnet.get(parent)).get("children")).add(word);
			if (child != null)
				((ArrayList<String>) ((JSONObject) this.wordnet.get(child)).get("parents")).add(word);

		} else {
			((ArrayList<String>) ((JSONObject) this.wordnet.get(parent)).get("children")).remove(previousChild);
			((ArrayList<String>) ((JSONObject) this.wordnet.get(parent)).get("children")).add(word);
			((ArrayList<String>) ((JSONObject) this.wordnet.get(previousChild)).get("parents")).remove(parent);
			((ArrayList<String>) ((JSONObject) this.wordnet.get(previousChild)).get("parents")).add(word);
			if (child != null) {
				String pos1 = ((String) ((JSONObject) this.wordnet.get(child)).get("pos"));
				String pos2 = ((String) ((JSONObject) this.wordnet.get(previousChild)).get("pos"));
				if (pos1.equals(pos2))
					children.clear();
				else
					((ArrayList<String>) ((JSONObject) this.wordnet.get(child)).get("parents")).add(word);
			}
			children.add(previousChild);
			jsonObject.put("children", children);

		}
		this.wordnet.put(word, jsonObject);
		try {
			FileOutputStream fout = new FileOutputStream("Wordnet.json");
			PrintStream out = new PrintStream(fout);
			out.print(this.wordnet.toJSONString());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	public String simplestOf(String word, String pos) {
		String simplestword = word;
		while (this.wordnet.get(simplestword) != null
				&& !((ArrayList) ((JSONObject) this.wordnet.get(simplestword)).get("children")).isEmpty()) {
			ArrayList<String> childrenOfThis = ((ArrayList) ((JSONObject) this.wordnet.get(simplestword)).get("children"));
			int i;
			for (i = 0; i < childrenOfThis.size(); i++) {
				if (((String) ((JSONObject) this.wordnet.get((String) childrenOfThis.get(i))).get("pos")).equals(pos)) {
					simplestword = childrenOfThis.get(i);
					break;
				}
			}
			if(i==childrenOfThis.size())break;
		}
		
		
		return simplestword;
	}

	public static void main(String arg[]) {
		Wordnet x = new Wordnet();
		Scanner in = new Scanner(System.in);
		int choice;
		do {
			System.out.println(
					"1.insert the word\n2.extract the simplest word of a word\n3.remove a word\n4. display the wordnet");
			System.out.println("5.exit");
			System.out.println("enter you choice");
			choice = in.nextInt();
			switch (choice) {
			case 1:
				x.insertWord(in.next(), in.next(), in.next(), in.next());
				break;
			case 2:
				System.out.println("enter the word");
				String simplestword = in.next();
				System.out.println("enter the POS type");
				while (!((ArrayList) ((JSONObject) x.wordnet.get(simplestword)).get("children")).isEmpty()) {
					simplestword = (String) ((ArrayList) ((JSONObject) x.wordnet.get(simplestword)).get("children"))
							.get(0);

				}
				System.out.println(simplestword);
				break;
			case 3:
				break;
			case 4:
				System.out.println(x.wordnet);
				break;
			case 5:
				System.out.println("bye bye");
				break;
			default:
				System.out.println("wrong option");
			}
		} while (choice != 5);

	}
}
