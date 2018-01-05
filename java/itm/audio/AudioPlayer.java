package itm.audio;

/*******************************************************************************
 This file is part of the ITM course 2017
 (c) University of Vienna 2009-2017
 *******************************************************************************/

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.*;

/**
 * Plays an audio file using the system's default sound output device
 * 
 */
public class AudioPlayer {

	/**
	 * Constructor
	 */
	public AudioPlayer() {

	}

	/**
	 * Plays audio data from a given input file to the system's default sound
	 * output device
	 * 
	 * @param input
	 *            the audio file
	 * @throws IOException
	 *             general error when accessing audio file
	 */
	protected void playAudio(File input) throws IOException, LineUnavailableException
    {

		if (!input.exists())
			throw new IOException("Input file " + input + " was not found!");

		AudioInputStream audio = null;
		try {
			audio = openAudioInputStream(input);
		} catch (UnsupportedAudioFileException e) {
			throw new IOException("could not open audio file " + input
					+ ". Encoding / file format not supported");
		}

		try {
			rawplay(audio);
		} catch (LineUnavailableException e) {
			throw new IOException("Error when playing sound from file "
					+ input.getName() + ". Sound output device unavailable");
		}

		audio.close();
	}

	/**
	 * Decodes an encoded audio file and returns a PCM input stream
	 * 
	 * Supported encodings: MP3, OGG (requires SPIs to be in the classpath)
	 * 
	 * @param input
	 *            a reference to the input audio file
	 * @return a PCM AudioInputStream
	 * @throws UnsupportedAudioFileException
	 *             an audio file's encoding is not supported
	 * @throws IOException
	 *             general error when accessing audio file
	 */
	private AudioInputStream openAudioInputStream(File input) throws UnsupportedAudioFileException, IOException, LineUnavailableException
    {
		// ***************************************************************
		// Fill in your code here!
		// ***************************************************************

        AudioInputStream din = null;
        AudioInputStream inputStream = null;
        AudioFormat originalFormat = null;
		try
        {
            // open audio stream
            inputStream = AudioSystem.getAudioInputStream(input);

            // get format
            originalFormat = inputStream.getFormat();
            System.out.println("The Sample Rate is: " + originalFormat.getSampleRate());
        }
        catch (IOException e)
		{
			System.out.println("Error reading the the audio file: " + e);
		}

		// get decoded format
		AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, originalFormat.getSampleRate(), 16, originalFormat.getChannels(), originalFormat.getChannels() * 2, originalFormat.getSampleRate(), false);

		// get decoded audio input stream
		din = AudioSystem.getAudioInputStream(decodedFormat, inputStream);

		return din;
	}

	/**
	 * Writes audio data from an AudioInputStream to a SourceDataline
	 * 
	 * @param audio
	 *            the audio data
	 * @throws IOException
	 *             error when writing audio data to source data line
	 * @throws LineUnavailableException
	 *             system's default source data line is not available
	 */

    private void rawplay(AudioInputStream audio) throws IOException, LineUnavailableException {

        // ***************************************************************
		// Fill in your code here!
		// ***************************************************************

        AudioFormat format = null;
        SourceDataLine sourceDataLine = null;
        DataLine.Info info = null;

        // get audio format
        format = audio.getFormat();

		// get a source data line
        info = new DataLine.Info(SourceDataLine.class, format);
        sourceDataLine = (SourceDataLine) AudioSystem.getLine(info);

        // Open the Audio stream and write to it
        sourceDataLine.open(format);

        // read samples from audio and write them to the data line
        byte[] data = new byte[4096];

        if(sourceDataLine != null)
        {
            sourceDataLine.start();

            int bytesRead = 0, bytesWritten = 0;
            while (bytesRead != -1)
            {
                bytesRead = audio.read(data, 0, data.length);
                if (bytesRead != -1)
                    bytesWritten = sourceDataLine.write(data, 0, bytesRead);
            }
        }

		// properly close the line!
        sourceDataLine.stop();
        sourceDataLine.close();
        audio.close();
	}

	/**
	 * Main method. Parses the commandline parameters and prints usage
	 * information if required.
	 */
	public static void main(String[] args) throws Exception {

		if (args.length < 1) {
			System.out.println("usage: java itm.audio.AudioPlayer <input-audioFile>");
			System.exit(1);
		}
		File fi = new File(args[0]);
		AudioPlayer player = new AudioPlayer();
		player.playAudio(fi);
		System.exit(0);
	}
}
