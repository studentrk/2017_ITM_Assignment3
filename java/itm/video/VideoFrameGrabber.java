package itm.video;

import com.sun.deploy.uitoolkit.impl.fx.Utils;
import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.demos.DecodeAndCaptureFrames;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IPacket;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.IVideoResampler;
import java.awt.image.BufferedImage;

import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.video.ConverterFactory;
import com.xuggle.xuggler.video.IConverter;

/*******************************************************************************
 This file is part of the ITM course 2017
 (c) University of Vienna 2009-2017
 *******************************************************************************/

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 * 
 * This class creates JPEG thumbnails from from video frames grabbed from the
 * middle of a video stream It can be called with 2 parameters, an input
 * filename/directory and an output directory.
 * 
 * If the input file or the output directory do not exist, an exception is
 * thrown.
 */

public class VideoFrameGrabber {

	/**
	 * Constructor.
	 */
	public VideoFrameGrabber() {
	}

	/**
	 * Processes the passed input video file / video file directory and stores
	 * the processed files in the output directory.
	 * 
	 * @param input
	 *            a reference to the input video file / input directory
	 * @param output
	 *            a reference to the output directory
	 */
	public ArrayList<File> batchProcessVideoFiles(File input, File output) throws IOException {
		if (!input.exists())
			throw new IOException("Input file " + input + " was not found!");
		if (!output.exists())
			throw new IOException("Output directory " + output + " not found!");
		if (!output.isDirectory())
			throw new IOException(output + " is not a directory!");

		ArrayList<File> ret = new ArrayList<File>();

		if (input.isDirectory()) {
			File[] files = input.listFiles();
			for (File f : files) {
				if (f.isDirectory())
					continue;

				String ext = f.getName().substring(f.getName().lastIndexOf(".") + 1).toLowerCase();
				if (ext.equals("avi") || ext.equals("swf") || ext.equals("asf") || ext.equals("flv")
						|| ext.equals("mp4")) {
					File result = processVideo(f, output);
					System.out.println("converted " + f + " to " + result);
					ret.add(result);
				}

			}

		} else {
			String ext = input.getName().substring(input.getName().lastIndexOf(".") + 1).toLowerCase();
			if (ext.equals("avi") || ext.equals("swf") || ext.equals("asf") || ext.equals("flv") || ext.equals("mp4")) {
				File result = processVideo(input, output);
				System.out.println("converted " + input + " to " + result);
				ret.add(result);
			}
		}
		return ret;
	}

	/**
	 * Processes the passed audio file and stores the processed file to the
	 * output directory.
	 * 
	 * @param input
	 *            a reference to the input audio File
	 * @param output
	 *            a reference to the output directory
	 */
	protected File processVideo(File input, File output) throws IOException, IllegalArgumentException {
		if (!input.exists())
			throw new IOException("Input file " + input + " was not found!");
		if (input.isDirectory())
			throw new IOException("Input file " + input + " is a directory!");
		if (!output.exists())
			throw new IOException("Output directory " + output + " not found!");
		if (!output.isDirectory())
			throw new IOException(output + " is not a directory!");

		File outputFile = new File(output, input.getName() + "_thumb.jpg");
		// load the input video file

		String filename = input.getAbsolutePath();
		BufferedImage frame = null; // the thumbnail
		IContainer container = IContainer.make(); // media container (all streams)
		IStream videoStream = null; // represents the video stream
		IStreamCoder videoCoder = null; // video stream coder

		// try to open the container
		if (container.open(input.getAbsolutePath(), IContainer.Type.READ, null) < 0)
			throw new RuntimeException("Opening file didn't work");

		int videoStreamId = -1;
		int numStreams = container.getNumStreams();
		for (int i = 0; i < numStreams; i++) {
			// find the stream object

			videoStream = container.getStream(i);

			// get the pre-configured decoder that can decode this stream;
			IStreamCoder coder = videoStream.getStreamCoder();

			if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO) {
				videoStreamId = i;
				videoCoder = coder;
				break;
			}
		}

		if (videoStreamId == -1)
			throw new RuntimeException("could not find video stream in container: " + filename);

		long mittelFrame = Math.round(((videoStream.getDuration() * videoCoder.getTimeBase().getNumerator())
				/ videoCoder.getTimeBase().getDenominator()) * videoCoder.getFrameRate().getDouble() / 2);

		if (videoCoder.open() < 0)
			throw new RuntimeException("could not open video decoder for container: " + filename);

		IVideoResampler resampler = IVideoResampler.make(
			videoCoder.getWidth(), videoCoder.getHeight(), IPixelFormat.Type.BGR24, 
videoCoder.getWidth(), videoCoder.getHeight(), videoCoder.getPixelType());

		IPacket packet = IPacket.make();
		// prepare a picture to read decoded stuff into
		IVideoPicture picture = IVideoPicture.make(videoCoder.getPixelType(), videoCoder.getWidth(),
				videoCoder.getHeight());



		container.seekKeyFrame(videoStreamId, -1, 0);


		container.seekKeyFrame(
			videoStreamId, 	// on which stream to look
			0, 				// min timestamp (for short videos)
			(long) (mittelFrame * videoCoder.getTimeBase().getDenominator() / (videoCoder.getFrameRate().getDouble()*videoCoder.getTimeBase().getNumerator())), // target
			container.getDuration(),	// max timestamp
			0		// seek flags
			);
		

		while (container.readNextPacket(packet) >= 0) {
			if (packet.getStreamIndex() != videoStreamId) continue; // wenn es nicht Teil des Video Streams ist, dann ignoriere es
			if (picture.isComplete()) break; // stop the loop when picture is complete

			
			
			int progress = 0; // how many bytes are decoded
			while(progress < packet.getSize()) {
				progress += videoCoder.decodeVideo(picture, packet, progress); // write progress into pic
				if (picture.isComplete()) {
					
					IVideoPicture resampled = null;
					
					if (videoCoder.getPixelType() != IPixelFormat.Type.BGR24) { // we must resample
						resampled = IVideoPicture.make(
								resampler.getOutputPixelFormat(), videoCoder.getWidth(), videoCoder.getHeight());
						if (resampler.resample(resampled, picture) <0) throw new RuntimeException("Resampling failed.");
						
						frame = new BufferedImage(videoCoder.getWidth(), videoCoder.getHeight(),BufferedImage.TYPE_3BYTE_BGR);
						IConverter conv = ConverterFactory.createConverter(frame, IPixelFormat.Type.BGR24);
						frame = conv.toImage(resampled);
					} else {
						frame = new BufferedImage(videoCoder.getWidth(), videoCoder.getHeight(),BufferedImage.TYPE_3BYTE_BGR);
						IConverter conv = ConverterFactory.createConverter(frame, IPixelFormat.Type.BGR24);
						frame = conv.toImage(picture);
					}
					break;
				}

			}
		}

		if (frame == null)
			throw new RuntimeException("Frame could not be extracted");
		ImageIO.write(frame, "jpeg", outputFile);

		// resource cleanup
		videoCoder.close();
		container.close();
		// ***************************************************************
		// Fill in your code here!
		// ***************************************************************

		return outputFile;

	}

	/**
	 * Main method. Parses the commandline parameters and prints usage
	 * information if required.
	 */
	public static void main(String[] args) throws Exception {

		// args = new String[] { "./media/video", "./test" };

		if (args.length < 2) {
			System.out.println("usage: java itm.video.VideoFrameGrabber <input-videoFile> <output-directory>");
			System.out.println("usage: java itm.video.VideoFrameGrabber <input-directory> <output-directory>");
			System.exit(1);
		}
		File fi = new File(args[0]);
		File fo = new File(args[1]);
		VideoFrameGrabber grabber = new VideoFrameGrabber();
		grabber.batchProcessVideoFiles(fi, fo);
	}

}
