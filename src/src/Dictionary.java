package src;
/**
Author: 
Name - Rohit Negi
Roll No - 130101144
Sys Id - 2013014812

Dictionary.java contains the code for the text simplifier module.
Objective of text simplifier is to take the input paragraph and find the
simpler meaning of difficult words and output the paragraph after replace the words
with simpler words.

**/
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class Dictionary {
	private String SimpleText, TaggedText, SimplifiedText;

	public Dictionary(String st) {
		SimpleText = st;
	}

	// to perform the POS Tagging on the input text using Stanford tagger
	public String tagText() {
		String sentences;
		String untaggedString = SimpleText;
		if (!untaggedString.isEmpty()) {
			try {
				/*loading the tagger*/
				MaxentTagger tagger = new MaxentTagger("models/left3words-wsj-0-18.tagger");
				
				/*taggint the text*/
				sentences = tagger.tagString(untaggedString); 
				
				TaggedText = sentences;
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}
			return sentences;
		} else
			return null;
	}

	/* simplifying the tagged text with the help of wordnet */
	public String simplify() throws Exception {
		
		/*tagging the text*/
		String A[] = this.TaggedText.split(" "), p, finalproduct = ""; 
		
		/* Loading the wordnet */
		Wordnet wordnet = new Wordnet();

		/* traversing through all the tokens */
		for (int j = 0; j < A.length; j++) {
			String x = A[j].split("/")[1];
			System.out.println(A[j]);
			p = A[j].split("/")[0];

			/*
			 * if the word is noun, proper noun, verb, adjective then simplify
			 * it to its simplest form
			 */
			if (x.equals("NN") || x.equals("NNP") || x.equals("VB") || x.equals("JJ")) {
				p = wordnet.simplestOf(p, x);
			}
			
			finalproduct += p;
			if (j < A.length - 1) {
				char c = A[j + 1].charAt(0);
				if (c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z')
					finalproduct += " ";
			}
		}
		SimplifiedText = finalproduct;
		return SimplifiedText;
	}

	static void addWord() {

	}
}
