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
public class Wordnet {
JSONObject wordnet;
Wordnet()
{
	try
	{	File file=new File("Wordnet.json");
		if(!file.exists())
		file.createNewFile();
		
		JSONParser parser=new JSONParser();
		JSONObject jObject=(JSONObject)parser.parse(new FileReader("Wordnet.json"));
		this.wordnet=jObject;
		
	}catch(Exception e )
	{
		e.printStackTrace();
	}
}
public static void main(String arg[])
{
	Wordnet x=new Wordnet();
	Scanner in=new Scanner(System.in);
	int choice;
	do
	{
		System.out.println("1.insert the word\n2.extract the simplest word of a word\n3.remove a word\n4. display the wordnet");
		System.out.println("5.exit");
		System.out.println("enter you choice");
		choice=in.nextInt();
		switch(choice)
		{
		case 1:break;
		case 2: break;
		case 3: break;
		case 4: System.out.println(x.wordnet);break;
		case 5: System.out.println("bye bye");
				break;
		default: System.out.println("wrong option");
		}
	}while(choice!=5);
	
}
}
