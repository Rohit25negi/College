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
public String tagText(String untaggedString)	//to perform the POS Tagging on the input text using Stanford tagger
{	String sentences; 
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
{	String sentences=this.TaggedText;
	String x[]=sentences.split(" ");	// splitting by space to get each word
	for(int i=0;i<x.length;i++)
	{
		String url = "http://www.google.com/search?q="+x[i];		

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();	

		// optional default is GET
		con.setRequestMethod("GET");	//sending get request to google

		//add request header
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
	

		BufferedReader in=new BufferedReader(new InputStreamReader(con.getInputStream()));	//getting inputStream from the connection
		StringBuffer str=new StringBuffer("");
		String xx;
		while((xx=in.readLine())!=null)
			str.append(xx);
		/*
		 * Trying to fetch the meaning from html body*/;
		System.out.println(str);
		Document doc=Jsoup.parse(str.toString());
		Elements elem=doc.select("table");
		System.out.println(str);
		
	}
}
}
