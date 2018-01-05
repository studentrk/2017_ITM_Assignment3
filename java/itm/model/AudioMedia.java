package itm.model;

/*******************************************************************************
 This file is part of the ITM course 2017
 (c) University of Vienna 2009-2017
 *******************************************************************************/

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

public class AudioMedia extends AbstractMedia {

	// ***************************************************************
	// Fill in your code here!
	// ***************************************************************

    private String encoding;
	private double duration;
	private String author;
	private String title;
	private String date;
	private String comment;
	private String album;
	private String track;
	private String composer;
	private String genre;
	private int frequency;
	private int bitRate;
	private int channels;

	/**
	 * Constructor.
	 */
	public AudioMedia() {
		super();
	}

	/**
	 * Constructor.
	 */
	public AudioMedia(File instance) {
		super(instance);
	}

	/* GET / SET methods */
    public String getEncoding()
    {
        return encoding;
    }

    public void setEncoding(String encoding)
    {
        this.encoding = encoding;
    }

    public double getDuration()
    {
        return duration;
    }

    public void setDuration(double duration)
    {
        this.duration = duration;
    }

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public String getAlbum()
    {
        return album;
    }

    public void setAlbum(String album)
    {
        this.album = album;
    }

    public String getTrack()
    {
        return track;
    }

    public void setTrack(String track)
    {
        this.track = track;
    }

    public String getComposer()
    {
        return composer;
    }

    public void setComposer(String composer)
    {
        this.composer = composer;
    }

    public String getGenre()
    {
        return genre;
    }

    public void setGenre(String genre)
    {
        this.genre = genre;
    }

    public int getFrequency()
    {
        return frequency;
    }

    public void setFrequency(int frequency)
    {
        this.frequency = frequency;
    }

    public int getBitRate()
    {
        return bitRate;
    }

    public void setBitRate(int bitRate)
    {
        this.bitRate = bitRate;
    }

    public int getChannels()
    {
        return channels;
    }

    public void setChannels(int channels)
    {
        this.channels = channels;
    }

	// ***************************************************************
	// Fill in your code here!
	// ***************************************************************

	/* (de-)serialization */

	/**
	 * Serializes this object to the passed file.
	 * 
	 */
	@Override
	public StringBuffer serializeObject() throws IOException {
		StringWriter data = new StringWriter();
		PrintWriter out = new PrintWriter(data);
		out.println("type: audio");
		StringBuffer sup = super.serializeObject();
		out.print(sup);

		// ***************************************************************
		// Fill in your code here!
		// ***************************************************************

        out.println("encoding: " + encoding );
        out.println("duration: " +  duration);
        out.println("author: " + author);
        out.println("title: " + title);
        out.println("date: " + date);
        out.println("comment: " + comment );
        out.println("album: " + album );
        out.println("track: " + track);
        out.println("composer: " + composer);
        out.println("genre: " + genre);
        out.println("frequency: " + frequency);
        out.println("bitrate: " + bitRate);
        out.println("channels: " + channels);

		return data.getBuffer();
	}

	/**
	 * Deserializes this object from the passed string buffer.
	 */
	@Override
	public void deserializeObject(String data) throws IOException {
		super.deserializeObject(data);

		StringReader sr = new StringReader(data);
		BufferedReader br = new BufferedReader(sr);
		String line = null;
		while ((line = br.readLine()) != null) {

			// ***************************************************************
			// Fill in your code here!
			// ***************************************************************

            // read and set properties
            if(line.startsWith("encoding:"))
            {
                System.out.println("The line for encoding is: " + line);
                setEncoding(line);

            }


            if(line.startsWith("duration:"))
                setDuration(Integer.parseInt(line));

            if(line.startsWith("author:"))
                setAuthor(line);

            if(line.startsWith("title:"))
                setTitle(line);

            if(line.startsWith("date:"))
                setDate(line);

            if(line.startsWith("comment:"))
                setComment(line);

            if(line.startsWith("album:"))
                setAlbum(line);

            if(line.startsWith("track:"))
                setTrack(line);

            if(line.startsWith("composer:"))
                setComposer(line);

            if(line.startsWith("genre:"))
                setGenre(line);

            if(line.startsWith("frequency:"))
                setFrequency(Integer.parseInt(line));

            if(line.startsWith("bitrate:"))
                setBitRate(Integer.parseInt(line));

            if(line.startsWith("channels:"))
                setChannels(Integer.parseInt(line));
		}
	}

}
