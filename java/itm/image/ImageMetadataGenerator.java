package itm.image;

/*******************************************************************************
    This file is part of the ITM course 2017
    (c) University of Vienna 2009-2017
*******************************************************************************/

import itm.model.ImageMedia;
import itm.model.MediaFactory;
import jdk.jfr.events.FileWriteEvent;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
    This class reads images of various formats and stores some basic image meta data to text files.
    It can be called with 3 parameters, an input filename/directory, an output directory and an "overwrite" flag.
    It will read the input image(s), retrieve some meta data and write it to a text file in the output directory.
    The overwrite flag indicates whether the resulting output file should be overwritten or not.
    
    If the input file or the output directory do not exist, an exception is thrown.
*/
public class ImageMetadataGenerator 
{

    /**
        Constructor.
    */
    public ImageMetadataGenerator()
    {
    }
   

    /**
        Processes an image directory in a batch process.
        @param input a reference to the input image directory
        @param output a reference to the output directory
        @param overwrite indicates whether existing metadata files should be overwritten or not
        @return a list of the created media objects (images)
    */
    public ArrayList<ImageMedia> batchProcessImages( File input, File output, boolean overwrite ) throws IOException
    {
        if ( ! input.exists() ) {
            throw new IOException( "Input file " + input + " was not found!" );
        }
        if ( ! output.exists() ) {
            throw new IOException( "Output directory " + output + " not found!" );
        }
        if ( ! output.isDirectory() ) {
            throw new IOException( output + " is not a directory!" );
        }

        ArrayList<ImageMedia> ret = new ArrayList<ImageMedia>();

        if ( input.isDirectory() ) {
            File[] files = input.listFiles();
            for ( File f : files ) {
                try {
                    ImageMedia result = processImage( f, output, overwrite );
                    System.out.println( "converted " + f + " to " + output );
                    ret.add( result );
                } catch ( Exception e0 ) {
                    System.err.println( "Error converting " + input + " : " + e0.toString() );
                }
            }
        } else {
                try {
                    ImageMedia result = processImage( input, output, overwrite );
                    System.out.println( "converted " + input + " to " + output );
                    ret.add( result );
                } catch ( Exception e0 ) {
                    System.err.println( "Error converting " + input + " : " + e0.toString() );
                }
        }
        return ret;
    }    
    
    /**
        Processes the passed input image and stores the extracted metadata to a textfile in the output directory.
        @param input a reference to the input image
        @param output a reference to the output directory
        @param overwrite indicates whether existing metadata files should be overwritten or not
        @return the created image media object
    */
    protected ImageMedia processImage( File input, File output, boolean overwrite ) throws IOException, IllegalArgumentException
    {
        if ( ! input.exists() ) {
            throw new IOException( "Input file " + input + " was not found!" );
        }
        if ( input.isDirectory() ) {
            throw new IOException( "Input file " + input + " is a directory!" );
        }
        if ( ! output.exists() ) {
            throw new IOException( "Output directory " + output + " not found!" );
        }
        if ( ! output.isDirectory() ) {
            throw new IOException( output + " is not a directory!" );
        }

        // create outputfilename and check whether thumb already exists. All image 
        // metadata files have to start with "img_" -  this is used by the mediafactory!
        File outputFile = new File( output, "img_" + input.getName() + ".txt" );
        if ( outputFile.exists() ) {
            if ( ! overwrite ) {
                // load from file
                ImageMedia media = new ImageMedia();
                media.readFromFile( outputFile );
                return media;
                }
        }


        // get metadata and store it to media object
        ImageMedia media = (ImageMedia) MediaFactory.createMedia( input );

        // ***************************************************************
        //  Fill in your code here!
        // ***************************************************************
        
        // load the input image
        BufferedImage img = null;
        try {
            img = ImageIO.read(input);
        } catch (IOException e) {
        }

        // set width and height of the image  
        media.setWidth(img.getWidth());
        media.setHeight(img.getHeight());
        // add a tag "image" to the media
        media.addTag("image");
        //media.addTag("hallooo");
        
        // add a tag corresponding to the filename extension of the file to the media 
        String inputName = input.getName();
        String extensionTag = inputName.substring(inputName.lastIndexOf(".") + 1);
        media.addTag(extensionTag);
        //getDominantColors und mit for Schleife addTag

        ArrayList<String> tags = getDominantColors(img);
        for (String tag : tags) {
            media.addTag(tag);
        }
        // set orientation
        if(img.getHeight() < img.getWidth()){
            media.setOrientation(0);
        } else {
            media.setOrientation(1);
        }
        // if there is a colormodel:
        // set color space type
        // set pixel size
        // set transparency
        // set number of (color) components   
        if(img.getColorModel() != null) {

            media.setNrOfImageComp(img.getColorModel().getNumComponents());
            media.setNrOfImageColComp(img.getColorModel().getNumColorComponents());
            media.setTransparency(img.getTransparency());
            media.setPixelSize(img.getColorModel().getPixelSize());
            media.setColorSpaceType(img.getColorModel().getColorSpace().getType());
        }

        // store meta data
        StringBuffer stringBuff = media.serializeObject();
        BufferedWriter buffWriter = new BufferedWriter(new FileWriter(outputFile));
        buffWriter.write(stringBuff.toString());
        buffWriter.flush();
        buffWriter.close();


        return media;
    }

    public ArrayList<String> getDominantColors(BufferedImage img) {
        int imgHeight = img.getHeight();
        int imgWidth = img.getWidth();
        int redCounter = 0;
        int greenCounter = 0;
        int blueCounter = 0;
        Color color;

        for (int i = 0; i < imgWidth; i++)
        {
            for (int j = 0; j < imgHeight; j++)
            {
                color = new Color(img.getRGB(i, j));//Get the current pixel.

                if (!(color.getRed() == color.getGreen() && color.getRed() == color.getBlue())) {//so dass graue, schwarz und weiße Pixel ignoriert werden
                    if (color.getRed() > color.getGreen() && color.getRed() > color.getBlue())//Checking pixel
                    {
                        redCounter++;//increase counter

                    } else if (color.getGreen() > color.getBlue() && color.getGreen() > color.getRed())//Checking pixel
                    {
                        greenCounter++;//increase counter

                    } else if (color.getBlue() > color.getRed() && color.getBlue() > color.getGreen())//Checking pixel
                    {
                        blueCounter++;//increase counter
                    }
                }
            }
        }
        //System.out.println("Colours red: " + redCounter + "\n green: " + greenCounter + "\n blue: " + blueCounter);
        int sumColours = redCounter + blueCounter + greenCounter;
        int max = Math.max(redCounter, greenCounter);
        max = Math.max(max, blueCounter);

        ArrayList<String> tags = new ArrayList<String>();
        int biggestDiff = 150;

        if (max != 0) {
            if (max == redCounter || isColourDominant(max, redCounter, biggestDiff)) {
                tags.add("red");
            }
            if (max == greenCounter || isColourDominant(max, greenCounter, biggestDiff)) {
                tags.add("green");
            }
            if (max == blueCounter || isColourDominant(max, blueCounter, biggestDiff)) {
                tags.add("blue");
            }
        }

        return  tags;
    }

    public boolean isColourDominant(int max, int counter, int biggestDiff) {
        int diff = max - counter;
        if(Math.abs(diff) <= biggestDiff) {
            return true;
        } else {
            return false;
        }
    }
    
        
    /**
        Main method. Parses the commandline parameters and prints usage information if required.
    */
    public static void main( String[] args ) throws Exception
    {
        if ( args.length < 2 ) {
            System.out.println( "usage: java itm.image.ImageMetadataGenerator <input-image> <output-directory>" );
            System.out.println( "usage: java itm.image.ImageMetadataGenerator <input-directory> <output-directory>" );
            System.exit( 1 );
        }
        File fi = new File( args[0] );
        File fo = new File( args[1] );
        ImageMetadataGenerator img = new ImageMetadataGenerator();
        img.batchProcessImages( fi, fo, true );        
    }    
}