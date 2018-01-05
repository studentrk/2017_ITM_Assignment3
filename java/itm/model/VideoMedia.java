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

public class VideoMedia extends AbstractMedia {

	// ***************************************************************
	// Fill in your code here!
	// ***************************************************************

    /* video format metadata */
    private String videoCodec;
    private String videoCodecID;
    private double videoFrameRate;
    private Long videoLength;
    private int videoHeight;
    private int videoWidth;

    /* audio format metadata */

    private String audioCodec;
    private String audioCodecID;
    private int audioChannels;
    private int audioSampleRate;
    private int audioBitRate;

    public String getVideoCodec()
    {
        return videoCodec;
    }

    public void setVideoCodec(String videoCodec)
    {
        this.videoCodec = videoCodec;
    }

    public String getVideoCodecID()
    {
        return videoCodecID;
    }

    public void setVideoCodecID(String videoCodecID)
    {
        this.videoCodecID = videoCodecID;
    }

    public double getVideoFrameRate()
    {
        return videoFrameRate;
    }

    public void setVideoFrameRate(double videoFrameRate)
    {
        this.videoFrameRate = videoFrameRate;
    }

    public double getVideoLength()
    {
        return videoLength;
    }

    public void setVideoLength(Long videoLength)
    {
        this.videoLength = videoLength;
    }

    public int getVideoHeight()
    {
        return videoHeight;
    }

    public void setVideoHeight(int videoHeight)
    {
        this.videoHeight = videoHeight;
    }

    public int getVideoWidth()
    {
        return videoWidth;
    }

    public void setVideoWidth(int videoWidth)
    {
        this.videoWidth = videoWidth;
    }

    public String getAudioCodec()
    {
        return audioCodec;
    }

    public void setAudioCodec(String audioCodec)
    {
        this.audioCodec = audioCodec;
    }

    public String getAudioCodecID()
    {
        return audioCodecID;
    }

    public void setAudioCodecID(String audioCodecID)
    {
        this.audioCodecID = audioCodecID;
    }

    public int getAudioChannels()
    {
        return audioChannels;
    }

    public void setAudioChannels(int audioChannels)
    {
        this.audioChannels = audioChannels;
    }

    public int getAudioSampleRate()
    {
        return audioSampleRate;
    }

    public void setAudioSampleRate(int audioSampleRate)
    {
        this.audioSampleRate = audioSampleRate;
    }

    public int getAudioBitRate()
    {
        return audioBitRate;
    }

    public void setAudioBitRate(int audioBitRate)
    {
        this.audioBitRate = audioBitRate;
    }

	/**
	 * Constructor.
	 */
	public VideoMedia() {
		super();
	}

	/**
	 * Constructor.
	 */
	public VideoMedia(File instance) {
		super(instance);
	}

	/* GET / SET methods */

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
		out.println("type: video");
		StringBuffer sup = super.serializeObject();
		out.print(sup);

		/* video fields */

		// ***************************************************************
		// Fill in your code here!
		// ***************************************************************

        out.println("videoCodec: " + videoCodec);
        out.println("videoCodecID: " + videoCodecID);
        out.println("videoFrameRate: " + videoFrameRate);
        out.println("videoLength: " + videoLength);
        out.println("videoHeight: " + videoHeight);
        out.println("videoWidth: " + videoWidth);


        out.println("audioCodec: " + audioCodec);
        out.println("audioCodecID: " + audioCodecID);
        out.println("audioChannels: " + audioChannels);
        out.println("audioSampleRate: " + audioSampleRate);
        out.println("audioBitRate: " + audioBitRate);


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

			/* video fields */
			// ***************************************************************
			// Fill in your code here!
			// ***************************************************************

            if(line.startsWith("videoCodec:"))
                setVideoCodec(line);

            if(line.startsWith("videoCodecID:"))
                setVideoCodecID(line);

            if(line.startsWith("videoFrameRate:"))
                setVideoFrameRate(Double.parseDouble(line));

            if(line.startsWith("videoLength:"))
                setVideoLength(Long.parseLong(line));

            if(line.startsWith("videoHeight:"))
                setVideoHeight(Integer.parseInt(line));

            if(line.startsWith("videoWidth:"))
                setVideoWidth(Integer.parseInt(line));

            if(line.startsWith("audioCodec:"))
                setAudioCodec(line);

            if(line.startsWith("audioCodecID"))
                setAudioCodecID(line);

            if(line.startsWith("audioChannels"))
                setAudioChannels(Integer.parseInt(line));

            if(line.startsWith("audioSampleRate"))
                setAudioSampleRate(Integer.parseInt(line));

            if(line.startsWith("audioBitRate"))
                setAudioBitRate(Integer.parseInt(line));
		}
	}

}
