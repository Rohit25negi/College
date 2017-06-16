package src;
/**
 * Author: Rohit Negi CSE-B 4th year. Roll No: 130101144
 * email: rohit25.negi@gmail.com
 * This is file is part of the module called automatic playlist generator. It will be able to create
 * the playlist depending on the user's listening habits
 * 
 * 
 * JZoom Library is used to make the music player
 */

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
import java.util.Collection;
import java.util.List;
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

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Random;



public class Music extends RecursiveTreeObject<Music>{

	File file;
	private StringProperty sTitle, sartist, slength, sgenre;

	public File getFile() {
		return file;
	}

	public StringProperty getsTitle() {
		return sTitle;
	}

	public StringProperty getSartist() {
		return sartist;
	}

	public StringProperty getSlength() {
		return slength;
	}

	public StringProperty getSgenre() {
		return sgenre;
	}

	/* Initializing the UI */
	public Music(File file) {
		this.file = file;
		try {
			sgenre = new SimpleStringProperty(this.getGenere());
			sTitle = new SimpleStringProperty(file.getName());
			slength = new SimpleStringProperty(this.getLength());
			sartist = new SimpleStringProperty(this.getArtist());

		} catch (Exception e) {

		}
	}

	/*
	 * This function will automatically create the playList depending on the
	 * past history of the user
	 */

	/* this function is used to find the music genre */
	public String getGenere() throws Exception {
		BodyContentHandler handler = new BodyContentHandler();
		Metadata metadata = new Metadata();
		ParseContext pcontext = new ParseContext();
		Mp3Parser Mp3Parser = new Mp3Parser();
		FileInputStream inputstream = new FileInputStream(file);

		/* extracting the metadata from the file */
		Mp3Parser.parse(inputstream, handler, metadata, pcontext);
		String genre = metadata.get("xmpDM:genre"); // extracting only the
													// genre
		if (genre == null)
			genre = "Other";
		inputstream.close();
		return genre;
	}

	public String getLength() throws Exception {
		BodyContentHandler handler = new BodyContentHandler();
		Metadata metadata = new Metadata();
		ParseContext pcontext = new ParseContext();
		Mp3Parser Mp3Parser = new Mp3Parser();
		FileInputStream inputstream = new FileInputStream(file);

		/* extracting the metadata from the file */
		Mp3Parser.parse(inputstream, handler, metadata, pcontext);
		String duration = metadata.get("xmpDM:duration"); // extracting only the
															// genre
		if (duration == null)
			duration = "0";
		inputstream.close();
		return duration;
	}

	public String getArtist() throws Exception {
		BodyContentHandler handler = new BodyContentHandler();
		Metadata metadata = new Metadata();
		ParseContext pcontext = new ParseContext();
		Mp3Parser Mp3Parser = new Mp3Parser();
		FileInputStream inputstream = new FileInputStream(file);

		/* extracting the metadata from the file */
		Mp3Parser.parse(inputstream, handler, metadata, pcontext);
		String artist = metadata.get("xmpDM:artist"); // extracting only the
														// genre
		if (artist == null)
			artist = "None";
		inputstream.close();
		return artist;
	}

	/* selecting the files which the user wants to play */

}
