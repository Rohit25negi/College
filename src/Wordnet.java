
/**
 * AUTHOR: ROHIT NEGI, CSE-B, Roll No- 130101144, Sys id- 2013014812
 * 
 * This File contains the code for manipulation of the network of English words and  simplifying the given words.
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
			// Wordnet.json stores the web of stored/defined english words
			File file = new File("Wordnet.json");
			if (!file.exists())
				file.createNewFile();

			JSONParser parser = new JSONParser();
			// parsing the json string to json object
			JSONObject jObject = (JSONObject) parser.parse(new FileReader("Wordnet.json"));
			this.wordnet = jObject;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* return the wordnet which is the object of JSONObject class */
	public JSONObject getWordnet() {
		return wordnet;
	}

	/*
	 * to insert new word into web of words. pos is the POS from which the word
	 * is belong to. metadata includes the parent and child node information
	 */
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

		/*
		 * checking is the parent already have the child with the same pos tag
		 */
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
			/* if parent doesn't have child with same pos */
			/* simply make the new word child of given parents */

			jsonObject.put("children", children);

			if (parent != null)
				((ArrayList<String>) ((JSONObject) this.wordnet.get(parent)).get("children")).add(word);
			if (child != null)
				((ArrayList<String>) ((JSONObject) this.wordnet.get(child)).get("parents")).add(word);

		} else {
			/* if parent has child with same pos */

			/*
			 * replacing the current child of parent with this word. making the
			 * current child of parent, the child of this word.
			 */
			((ArrayList<String>) ((JSONObject) this.wordnet.get(parent)).get("children")).remove(previousChild);
			((ArrayList<String>) ((JSONObject) this.wordnet.get(parent)).get("children")).add(word);
			((ArrayList<String>) ((JSONObject) this.wordnet.get(previousChild)).get("parents")).remove(parent);
			((ArrayList<String>) ((JSONObject) this.wordnet.get(previousChild)).get("parents")).add(word);

			if (child != null) {
				String pos1 = ((String) ((JSONObject) this.wordnet.get(child)).get("pos"));
				String pos2 = ((String) ((JSONObject) this.wordnet.get(previousChild)).get("pos"));

				/*
				 * if the child of this word and current child of given parent
				 * have same pos then only one link is maintained
				 */
				if (pos1.equals(pos2))
					children.clear();
				else
					((ArrayList<String>) ((JSONObject) this.wordnet.get(child)).get("parents")).add(word);
			}
			children.add(previousChild);
			jsonObject.put("children", children);

		}
		/* insert the new word into the wordnet */
		this.wordnet.put(word, jsonObject);

		try {
			/* writing the wordnet into the file */
			FileOutputStream fout = new FileOutputStream("Wordnet.json");
			PrintStream out = new PrintStream(fout);
			out.print(this.wordnet.toJSONString());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	/* to find the simplest form of the given word with same pos */
	public String simplestOf(String word, String pos) {
		/* converting it to lowercase */
		String simplestword = word.toLowerCase();

		/* traverse the tree until you reach to the leaf node */
		while (this.wordnet.get(simplestword) != null
				&& !((ArrayList) ((JSONObject) this.wordnet.get(simplestword)).get("children")).isEmpty()) {

			/* getting all the children nodes of current node */
			ArrayList<String> childrenOfThis = ((ArrayList) ((JSONObject) this.wordnet.get(simplestword))
					.get("children"));
			int i;
			/* traversing each any every child of this node */
			for (i = 0; i < childrenOfThis.size(); i++) {
				if (((String) ((JSONObject) this.wordnet.get((String) childrenOfThis.get(i))).get("pos")).equals(pos)) {
					simplestword = childrenOfThis.get(i);
					break;
				}
			}
			
			/*if no child has the pos same as input word, then break the loop*/
			if (i == childrenOfThis.size())
				break;
		}
		char c = word.charAt(0);
		
		/*changing the case of outputword same as input word*/
		if (c >= 'A' && c <= 'Z')
			simplestword = simplestword.charAt(0) + simplestword.substring(1);
		return simplestword;
	}

}
