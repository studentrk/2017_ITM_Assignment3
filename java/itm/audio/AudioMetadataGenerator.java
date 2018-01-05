package itm.audio;

/*******************************************************************************
 This file is part of the ITM course 2017
 (c) University of Vienna 2009-2017
 *******************************************************************************/

import itm.model.AudioMedia;
import itm.model.MediaFactory;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * This class reads audio files of various formats and stores some basic audio
 * metadata to text files. It can be called with 3 parameters, an input
 * filename/directory, an output directory and an "overwrite" flag. It will read
 * the input audio file(s), retrieve some metadata and write it to a text file
 * in the output directory. The overwrite flag indicates whether the resulting
 * output file should be overwritten or not.
 * 
 * If the input file or the output directory do not exist, an exception is
 * thrown.
 */
public class AudioMetadataGenerator {

	/**
	 * Constructor.
	 */
	public AudioMetadataGenerator() {
	}

	/**
	 * Processes an audio file directory in a batch process.
	 * 
	 * @param input
	 *            a reference to the audio file directory
	 * @param output
	 *            a reference to the output directory
	 * @param overwrite
	 *            indicates whether existing metadata files should be
	 *            overwritten or not
	 * @return a list of the created media objects (images)
	 */
	public ArrayList<AudioMedia> batchProcessAudio(File input, File output, boolean overwrite) throws IOException {
		if (!input.exists())
			throw new IOException("Input file " + input + " was not found!");
		if (!output.exists())
			throw new IOException("Output directory " + output + " not found!");
		if (!output.isDirectory())
			throw new IOException(output + " is not a directory!");

		ArrayList<AudioMedia> ret = new ArrayList<AudioMedia>();

		if (input.isDirectory()) {
			File[] files = input.listFiles();
			for (File f : files) {

				String ext = f.getName().substring(
						f.getName().lastIndexOf(".") + 1).toLowerCase();
				if (ext.equals("wav") || ext.equals("mp3") || ext.equals("ogg")) {
					try {
						AudioMedia result = processAudio(f, output, overwrite);
						System.out.println("created metadata for file " + f + " in " + output);
						ret.add(result);
					} catch (Exception e0) {
						System.err.println("Error when creating metadata from file " + input + " : " + e0.toString());
					}

				}

			}
		} else {

			String ext = input.getName().substring(
					input.getName().lastIndexOf(".") + 1).toLowerCase();
			if (ext.equals("wav") || ext.equals("mp3") || ext.equals("ogg")) {
				try {
					AudioMedia result = processAudio(input, output, overwrite);
					System.out.println("created metadata for file " + input + " in " + output);
					ret.add(result);
				} catch (Exception e0) {
					System.err.println("Error when creating metadata from file " + input + " : " + e0.toString());
				}
			}
		}
		return ret;
	}

	/**
	 * Processes the passed input audio file and stores the extracted metadata
	 * to a textfile in the output directory.
	 * 
	 * @param input
	 *            a reference to the input audio file
	 * @param output
	 *            a reference to the output directory
	 * @param overwrite
	 *            indicates whether existing metadata files should be
	 *            overwritten or not
	 * @return the created image media object
	 */
	protected AudioMedia processAudio(File input, File output, boolean overwrite) throws IOException, IllegalArgumentException, UnsupportedAudioFileException
    {
        if (!input.exists())
            throw new IOException("Input file " + input + " was not found!");
        if (input.isDirectory())
            throw new IOException("Input file " + input + " is a directory!");
        if (!output.exists())
            throw new IOException("Output directory " + output + " not found!");
        if (!output.isDirectory())
            throw new IOException(output + " is not a directory!");

        // create outputfilename and check whether thumb already exists. All
        // image metadata files have to start with "aud_" - this is used by the
        // mediafactory!
        File outputFile = new File(output, "aud_" + input.getName() + ".txt");
        if (outputFile.exists())
            if (!overwrite)
            {
                // load from file
                AudioMedia media = new AudioMedia();
                media.readFromFile(outputFile);
                return media;
            }

        // ***************************************************************
        // Fill in your code here!
        // ***************************************************************

        AudioInputStream audioInput = null;
        AudioFormat audioFormat = null;

        // create an audio metadata object
        AudioMedia media = (AudioMedia) MediaFactory.createMedia(input);

        // load the input audio file, do not decode
        try
        {
            audioInput = AudioSystem.getAudioInputStream(input);
            System.out.println("Audio loaded: " + input.getName());
        }
        catch (IOException e)
        {
            System.out.printf("Error reading the audio file. " + e);
        }

        // read AudioFormat properties
        audioFormat = audioInput.getFormat();
        AudioFileFormat audioFileFormat = AudioSystem.getAudioFileFormat(input);
        Map properties = audioFileFormat.properties();

//        double bitRate = audioFormat.getSampleRate() * audioFormat.getSampleSizeInBits() + audioFormat.getChannels();

        media.setEncoding(audioFormat.getEncoding().toString());
        media.setChannels(audioFormat.getChannels());

        // read file-type specific properties
        // if the file is wav, set properties to ""
		// AudioFileFormat to read the audio file properties(author, genre..)
        // you might have to distinguish what properties are available for what audio format
        // if the file is mp3
        if (audioFormat.toString().toLowerCase().contains("mpeg"))
        {
            long microsecond = (long) properties.get("duration");
            double second = ((microsecond / 1000000) % 60) / 100.0;
            int min = (int)(microsecond / 1000000) / 60;
            double duration = min + second;

            media.setDuration(duration);
			media.setAuthor(properties.get("author").toString());
		    media.setTitle(properties.get("title").toString());
		    media.setDate(properties.get("date").toString());
		    media.setComment(properties.get("comment").toString());
		    media.setAlbum(properties.get("album").toString());
            media.setTrack((String)audioFileFormat.properties().get("mp3.id3tag.track"));
            media.setComposer((String)properties.get("mp3.id3tag.composer"));
            media.setGenre(properties.get("mp3.id3tag.genre").toString());
			media.setFrequency(Integer.parseInt(properties.get("mp3.frequency.hz").toString()));
            media.setBitRate((Integer.parseInt(properties.get("mp3.bitrate.nominal.bps").toString()))/ 1000);
        }
        else
        {
            double durationInSeconds = (int) (input.length() / audioFormat.getSampleRate() / (audioFormat.getSampleSizeInBits() / 8.0) / audioFormat.getChannels());
            double duration = durationInSeconds / 100;

            media.setDuration(duration);
            media.setEncoding(audioFormat.getEncoding().toString());
            media.setAuthor("");
            media.setTitle("");
            media.setTitle("");
            media.setDate("");
            media.setComment("");
            media.setAlbum("");
            media.setTrack("");
            media.setComposer("");
            media.setGenre("");
			media.setFrequency((int)audioFormat.getSampleRate());
            media.setBitRate(-1);
        }

		// add a "audio" tag
		media.addTag("audio");

		// close the audio and write the md file.
        audioInput.close();
		media.writeToFile(outputFile);

		return media;
	}

	/**
	 * Main method. Parses the commandline parameters and prints usage
	 * information if required.
	 */
	public static void main(String[] args) throws Exception {

		//args = new String[] { "./media/audio", "./media/md" };
		
		if (args.length < 2) {
			System.out
					.println("usage: java itm.image.AudioMetadataGenerator <input-image> <output-directory>");
			System.out
					.println("usage: java itm.image.AudioMetadataGenerator <input-directory> <output-directory>");
			System.exit(1);
		}

		File fi = new File(args[0]);
		File fo = new File(args[1]);
		AudioMetadataGenerator audioMd = new AudioMetadataGenerator();
		audioMd.batchProcessAudio(fi, fo, true);
	}
}
