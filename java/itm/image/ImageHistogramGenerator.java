package itm.image;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;

/*******************************************************************************
    This file is part of the ITM course 2017
    (c) University of Vienna 2009-2017
*******************************************************************************/


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import itm.util.Histogram;

/**
    This class creates color and grayscale histograms for various images.
    It can be called with 3 parameters, an input filename/directory, an output directory and a various bin/interval size.
    It will read the input image(s), count distinct pixel values and then plot the histogram.

    If the input file or the output directory do not exist, an exception is thrown.
*/
public class ImageHistogramGenerator 
{

    /**
        Constructor.
    */
    public ImageHistogramGenerator() 
    {
    }

    /**
        Processes an image directory in a batch process.
        @param input a reference to the input image file
        @param output a reference to the output directory
        @param bins the histogram interval
        @return a list of the created files
    */
    public ArrayList<File> batchProcessImages( File input, File output, int bins) throws IOException
    {
        if ( ! input.exists() ) 
            throw new IOException( "Input file " + input + " was not found!" );
        if ( ! output.exists() ) 
            throw new IOException( "Output directory " + output + " not found!" );
        if ( ! output.isDirectory() ) 
            throw new IOException( output + " is not a directory!" );

        ArrayList<File> ret = new ArrayList<File>();
        
        if ( input.isDirectory() ) {
            File[] files = input.listFiles();
            for ( File f : files ) {
                try {
                    File result = processImage( f, output, bins );
                    System.out.println( "converted " + f + " to " + result );
                    ret.add( result );
                } catch ( Exception e0 ) {
                    System.err.println( "Error converting " + input + " : " + e0.toString() );
                    }
                 }
            } else {
            try {
                File result = processImage( input, output, bins );
                System.out.println( "created " + input + " for " + result );
                ret.add( result );
            } catch ( Exception e0 ) { System.err.println( "Error creating histogram from " + input + " : " + e0.toString() ); }
            } 
        return ret;
    }  
    
    /**
        Processes the passed input image and stores it to the output directory.
        @param input a reference to the input image file
        @param output a reference to the output directory
        @param bins the histogram interval
        already existing files are overwritten automatically
    */   
	protected File processImage( File input, File output, int bins ) throws IOException, IllegalArgumentException
    {
		if ( ! input.exists() ) 
            throw new IOException( "Input file " + input + " was not found!" );
        if ( input.isDirectory() ) 
            throw new IOException( "Input file " + input + " is a directory!" );
        if ( ! output.exists() ) 
            throw new IOException( "Output directory " + output + " not found!" );
        if ( ! output.isDirectory() ) 
            throw new IOException( output + " is not a directory!" );


		// compose the output file name from the absolute path, a path separator and the original filename
		String outputFileName = "";
		outputFileName += output.toString() + File.separator + input.getName().toString();
		File outputFile = new File( output, input.getName() + ".hist.png" );
		
       
        // ***************************************************************
        //  Fill in your code here!
        // ***************************************************************

        // load the input image
        BufferedImage bi = null;
        try {
            bi = ImageIO.read(input);
        } catch (Exception e) {
            //TODO: handle exception
        }
        // get the color model of the image and the amount of color components
        int[] numOfColorComp = bi.getColorModel().getComponentSize();
        // initiate a Histogram[color components] [bins]
        Histogram histogram = new Histogram(numOfColorComp.length, bins);

        // create a histogram array histArray[color components][bins]
        int[][] histArray = new int[numOfColorComp.length][bins];
        int width = bi.getWidth();
        int height = bi.getHeight();
        
        
        // read the pixel values and extract the color information
        for (int row = 0; row < width; row++) {
           for (int col = 0; col < height; col++) {
            
                Color pixelColor = new Color(bi.getRGB(row, col));
                double red = (double) pixelColor.getRed()/ ( 256.0 / (double) bins );
                double green = (double) pixelColor.getGreen()/ ( 256.0 / (double) bins );
                double blue = (double) pixelColor.getBlue()/ ( 256.0 / (double) bins );
                int r = (int) red;
                int g = (int) green;
                int b = (int) blue;
                histArray[0][r]++;
                histArray[1][g]++;
                histArray[2][b]++;
               
           }
        }
        // fill the array setHistogram(histArray)
        histogram.setHistogram(histArray);

        // plot the histogram, try different dimensions for better visualization
        // encode and save the image as png 900, 600
        ImageIO.write( histogram.plotHistogram( 1080, 720 ), "png", outputFile ); 

        return outputFile;
    }
    
        
    /**
        Main method. Parses the commandline parameters and prints usage information if required.
    */
    public static void main( String[] args ) throws Exception
    {
        if ( args.length < 3 ) {
            System.out.println( "usage: java itm.image.ImageHistogramGenerator <input-image> <output-directory> <bins>" );
            System.out.println( "usage: java itm.image.ImageHistogramGenerator <input-directory> <output-directory> <bins>" );
            System.out.println( "");
            System.out.println( "bins:default 256" );
            System.exit( 1 );
        }
        // read params
        File fi = new File( args[0] );
        File fo = new File( args[1] );
        int bins = Integer.parseInt(args[2]);
        ImageHistogramGenerator histogramGenerator = new ImageHistogramGenerator();
        histogramGenerator.batchProcessImages( fi, fo, bins );        
    }    
}