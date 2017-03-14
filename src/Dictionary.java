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
public String simplify() throws Exception
{	
	
	String A[]=this.TaggedText.split(" "),p,finalproduct="";	//Tagging the text
	Wordnet wordnet=new Wordnet();
	
	for(int j=0;j<A.length;j++)
	{	String x=A[j].split("/")[1];
		System.out.println(A[j]);
		p=A[j].split("/")[0];
		if(x.equals("NN")||x.equals("NNP")||x.equals("VB")||x.equals("JJ"))
		{
			p=wordnet.simplestOf(p, x);
		}
		finalproduct+=p;
		if(j<A.length-1)
		{
			char c=A[j+1].charAt(0);
			if(c>='A'&&c<='Z'||c>='a'&&c<='z')
				finalproduct+=" ";
		}
	}
	SimplifiedText=finalproduct;
	return SimplifiedText;
}
static void addWord()
{
	
}
}
