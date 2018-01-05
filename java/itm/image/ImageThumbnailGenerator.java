                     package itm.image;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/*******************************************************************************
    This file is part of the ITM course 2017
    (c) University of Vienna 2009-2017
*******************************************************************************/

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.sun.prism.Graphics;

/**
    This class converts images of various formats to PNG thumbnails files.
    It can be called with 3 parameters, an input filename/directory, an output directory and a compression quality parameter.
    It will read the input image(s), grayscale and scale it/them and convert it/them to a PNG file(s) that is/are written to the output directory.

    If the input file or the output directory do not exist, an exception is thrown.
*/
public class ImageThumbnailGenerator 
{

    /**
        Constructor.
    */
    public ImageThumbnailGenerator()
    {
    }

    /**
        Processes an image directory in a batch process.
        @param input a reference to the input image file
        @param output a reference to the output directory
        @param overwrite indicates whether existing thumbnails should be overwritten or not
        @return a list of the created files
    */
    public ArrayList<File> batchProcessImages( File input, File output, boolean overwrite ) throws IOException
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

        ArrayList<File> ret = new ArrayList<File>();

        if ( input.isDirectory() ) {
            File[] files = input.listFiles();
            for ( File f : files ) {
                try {
                    File result = processImage( f, output, overwrite );
                    System.out.println( "converted " + f + " to " + result );
                    ret.add( result );
                } catch ( Exception e0 ) {
                    System.err.println( "Error converting " + input + " : " + e0.toString() );
                }
            }
        } else {
            try {
                File result = processImage( input, output, overwrite );
                System.out.println( "converted " + input + " to " + result );
                ret.add( result );
            } catch ( Exception e0 ) {
                System.err.println( "Error converting " + input + " : " + e0.toString() );
            }
        } 
        return ret;
    }  

    /**
        Processes the passed input image and stores it to the output directory.
        This function should not do anything if the outputfile already exists and if the overwrite flag is set to false.
        @param input a reference to the input image file
        @param output a reference to the output directory
        @param dimx the width of the resulting thumbnail
        @param dimy the height of the resulting thumbnail
        @param overwrite indicates whether existing thumbnails should be overwritten or not
    */
    protected File processImage( File input, File output, boolean overwrite ) throws IOException, IllegalArgumentException
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

        // create outputfilename and check whether thumb already exists
        File outputFile = new File( output, input.getName() + ".thumb.png" );
        if ( outputFile.exists() ) {
            if ( ! overwrite ) {
                return outputFile;
            }
        }

        // ***************************************************************
        //  Fill in your code here!
        // ***************************************************************
        
        // load the input image
        BufferedImage bi = null;
        try {
            bi = ImageIO.read(input);
        } catch (Exception e) {
            throw new IllegalArgumentException("Die Datei konnte nicht eingelesen werden");
        }

        Graphics2D graphics;
        int biType = bi.getType();
        biType = BufferedImage.TYPE_INT_RGB;
        
        int biOriginHeight = bi.getHeight();
        int biOriginWidth = bi.getWidth();

        // rotate if needed, when the image is potrait-oriented
        BufferedImage newImage;
        if(biOriginWidth < biOriginHeight) {
            newImage =new BufferedImage(biOriginHeight, biOriginWidth, bi.getType());
            // create graphics object and add original image to it
            graphics = newImage.createGraphics();
            double drehwinkel = Math.toRadians (90);
            AffineTransform affineTransform = AffineTransform.getRotateInstance(drehwinkel, newImage.getWidth() / 2, newImage.getWidth()/ 2);
            AffineTransformOp affineTransformOp = new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_BILINEAR);
            graphics.drawImage(affineTransformOp.filter(bi, null), 0, 0, null);
        } else {
            newImage = new BufferedImage(bi.getWidth(), bi.getHeight(), biType);
            graphics = newImage.createGraphics();
            graphics.drawImage(bi, 0, 0, null);
        }

        // add a watermark of your choice and paste it to the image
        // e.g. text or a graphic
        graphics.setFont(new Font("Arial", Font.BOLD, 10 * newImage.getWidth() /100));
        String watermark = "My pictures";
        graphics.drawString(watermark, newImage.getWidth()/2, newImage.getHeight()/2);
        
        // scale the image to a maximum of [ 200 w X 100 h ] pixels - do not distort!
        // if the image is smaller than [ 200 w X 100 h ] - print it on a [ dim X dim ] canvas!
        double verhaeltnis = (double) newImage.getWidth() / (double) newImage.getHeight();
        BufferedImage newImage2 = new BufferedImage(200, (int)(200/verhaeltnis), biType);
        graphics = newImage2.createGraphics();
        if (newImage.getWidth() < 200) {
            int x = ( newImage2.getWidth() / 2 ) - ( newImage.getWidth() / 2 );
	    	int y = ( newImage2.getHeight() / 2 ) - ( newImage.getHeight() / 2 );
            graphics.drawImage( newImage, x, y, newImage.getWidth(), (int)(newImage.getWidth() / verhaeltnis), new Color( 0, 0, 0 ), null);
        } else {
            graphics.drawImage(newImage, 0, 0, 200, (int)(200 / verhaeltnis), new Color( 0, 0, 0 ), null);
        }

        // encode and save the image
        String imgFormat = "png";
        outputFile = new File( output.getAbsolutePath() + "/" + input.getName() + ".thumb." + imgFormat );
        ImageIO.write( newImage2, imgFormat, outputFile );
        

        return outputFile;
    }

    /**
        Main method. Parses the commandline parameters and prints usage information if required.
    */
    public static void main( String[] args ) throws Exception
    {
        if ( args.length < 2 ) {
            System.out.println( "usage: java itm.image.ImageThumbnailGenerator <input-image> <output-directory>" );
            System.out.println( "usage: java itm.image.ImageThumbnailGenerator <input-directory> <output-directory>" );
            System.exit( 1 );
        }
        File fi = new File( args[0] );
        File fo = new File( args[1] );

        ImageThumbnailGenerator itg = new ImageThumbnailGenerator();
        itg.batchProcessImages( fi, fo, true );
    }    
}