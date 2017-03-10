/**
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
private String SimpleText,TaggedText,SimplifiedText;

public Dictionary(String st)
{
	SimpleText=st;
}
public String tagText()	//to perform the POS Tagging on the input text using Stanford tagger
{	String sentences;
	String untaggedString=SimpleText;
	if(!untaggedString.isEmpty())
	{	try{
		
	
		MaxentTagger tagger = new MaxentTagger("models/left3words-wsj-0-18.tagger");	//Loading the tagger
		sentences = tagger.tagString(untaggedString); //taggin the text
		TaggedText=sentences;
		
	}catch(Exception ex)
	{
		ex.printStackTrace();
		return null;
	}
	return sentences;
	}
	else return null;
}
public void simplify() throws Exception
{	
}
static void addWord()
{
	
}
}
