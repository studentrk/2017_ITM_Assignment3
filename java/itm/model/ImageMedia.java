package itm.model;

/*******************************************************************************
    This file is part of the ITM course 2017
    (c) University of Vienna 2009-2017
*******************************************************************************/

import java.awt.color.ColorSpace;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

/**
    This class describes an image. 
*/
public class ImageMedia extends AbstractMedia 
{

    public final static int ORIENTATION_LANDSCAPE = 0;
    public final static int ORIENTATION_PORTRAIT = 1;

    // ***************************************************************
    //  Fill in your code here!
    // ***************************************************************

    // add required properties (scope: protected!)

    protected int width;
    protected int height;
    protected int nrOfImageComp;
    protected int nrOfImageColComp;
    protected int transparency;
    protected int pixelSize;
    protected int colorSpaceType;
    protected int orientation;


    /**
        Constructor.
    */
    public ImageMedia()
    {
        super();
    }

    /**
        Constructor.
    */
    public ImageMedia( File instance )
    {
        super( instance );
    }


    

    /**
        Converts a color space type to a human readable string
        @return a string describing the passed colorspace
    */
    protected String serializeCSType( int cstype )
    {
        switch ( cstype ) {
            case ColorSpace.CS_CIEXYZ: return "CS_CIEXYZ"; 
            case ColorSpace.CS_GRAY: return "CS_GRAY"; 
            case ColorSpace.CS_LINEAR_RGB: return "CS_LINEAR_RGB"; 
            case ColorSpace.CS_PYCC: return "CS_PYCC"; 
            case ColorSpace.CS_sRGB: return "CS_sRGB"; 
            case ColorSpace.TYPE_CMY: return "TYPE_CMY"; 
            case ColorSpace.TYPE_CMYK: return "TYPE_CMYK"; 
            case ColorSpace.TYPE_GRAY: return "TYPE_GRAY"; 
            case ColorSpace.TYPE_RGB: return "TYPE_RGB"; 
            case ColorSpace.TYPE_HLS: return "TYPE_HLS"; 
            default: return ""+cstype; 
        }
    }

    /**
        Converts a human readable string string to a color space type
        @return the colorspace corresponding to the passed string
    */
    protected int deserializeCSType( String cstype )
    {
        if ( cstype.equals( "CS_CIEXYZ" ) ) {
            return ColorSpace.CS_CIEXYZ;
        }
        if ( cstype.equals( "CS_GRAY" ) ) {
            return ColorSpace.CS_GRAY;
        }
        if ( cstype.equals( "CS_LINEAR_RGB" ) ) {
            return ColorSpace.CS_LINEAR_RGB;
        }
        if ( cstype.equals( "CS_PYCC" ) ) {
            return ColorSpace.CS_PYCC;
        }
        if ( cstype.equals( "CS_sRGB" ) ) {
            return ColorSpace.CS_sRGB;
        }
        if ( cstype.equals( "TYPE_CMY" ) ) { 
            return ColorSpace.TYPE_CMY;
        }
        if ( cstype.equals( "TYPE_CMYK" ) ) {
            return ColorSpace.TYPE_CMYK;
        }
        if ( cstype.equals( "TYPE_GRAY" ) ) {
            return ColorSpace.TYPE_GRAY;
        }
        if ( cstype.equals( "TYPE_RGB" ) ) {
            return ColorSpace.TYPE_RGB;
        }
        if ( cstype.equals( "TYPE_HLS" ) ) {
            return ColorSpace.TYPE_HLS;
        }

        return Integer.parseInt( cstype );
    }
        
        
    /**
        Serializes this object to a string buffer.
        @return a StringBuffer containing a serialized version of this object.
    */
    @Override
    public StringBuffer serializeObject() throws IOException
    {
        StringWriter data = new StringWriter();
        // print writer for creating the output
        PrintWriter out = new PrintWriter( data );
        // print type
        out.println( "type: image" );
        StringBuffer sup = super.serializeObject();
        // print the serialization of the superclass (AbstractMedia)
        out.print( sup );
        // ***************************************************************
        //  Fill in your code here!
        // ***************************************************************

        // print properties
        out.println("width: " + getWidth());
        out.println("height: " + getHeight());
        out.println("nrOfImageComp: " + getNrOfImageComp());
        out.println("nrOfImageColComp: " + getNrOfImageColComp());
        out.println("transparency: " + getTransparency());
        out.println("pixelSize: " + getPixelSize());
        out.println("orientation: " + getOrientation());
        out.println("colorSpaceType: " + serializeCSType(getColorSpaceType()));

        return data.getBuffer();
    }



    /**
        Deserializes this object from the passed string buffer.
    */
    @Override
    public void deserializeObject( String data ) throws IOException
    {
        super.deserializeObject( data );
        
        StringReader sr = new StringReader( data );
        BufferedReader br = new BufferedReader( sr );
        String line = null;
        while ( ( line = br.readLine() ) != null ) {

            // ***************************************************************
            //  Fill in your code here!
            // ***************************************************************
            
            // read and set properties
            if(line.startsWith("width: ")){

                setWidth(Integer.parseInt(line.substring("width: ".length())));
            
            } else if(line.startsWith("height: ")){

                setHeight(Integer.parseInt(line.substring("height: ".length())));
                            
            } else if(line.startsWith("nrOfImageComp: ")){
                
                setNrOfImageComp(Integer.parseInt(line.substring("nrOfImageComp: ".length())));
                            
            } else if(line.startsWith("nrOfImageColComp: ")){
                
                setNrOfImageColComp(Integer.parseInt(line.substring("nrOfImageColComp: ".length())));
                            
            } else if(line.startsWith("transparency: ")){
                
                setTransparency(Integer.parseInt(line.substring("transparency: ".length())));
                            
            }else if(line.startsWith("pixelSize: ")){
                
                setPixelSize(Integer.parseInt(line.substring("pixelSize: ".length())));
                            
            } else if(line.startsWith("orientation: ")){
                
                setOrientation(Integer.parseInt(line.substring("orientation: ".length())));
                            
            } else if(line.startsWith("colorSpaceType: ")){
                
                setColorSpaceType(deserializeCSType(line.substring("colorSpaceType: ".length())));
                            
            } 


        }
    }

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * @return the nrOfImageComp
	 */
	public int getNrOfImageComp() {
		return nrOfImageComp;
	}

	/**
	 * @param nrOfImageComp the nrOfImageComp to set
	 */
	public void setNrOfImageComp(int nrOfImageComp) {
		this.nrOfImageComp = nrOfImageComp;
	}

	/**
	 * @return the nrOfImageColComp
	 */
	public int getNrOfImageColComp() {
		return nrOfImageColComp;
	}

	/**
	 * @param nrOfImageColComp the nrOfImageColComp to set
	 */
	public void setNrOfImageColComp(int nrOfImageColComp) {
		this.nrOfImageColComp = nrOfImageColComp;
	}

	/**
	 * @return the transparency
	 */
	public int getTransparency() {
		return transparency;
	}

	/**
	 * @param transparency the transparency to set
	 */
	public void setTransparency(int transparency) {
		this.transparency = transparency;
	}

	/**
	 * @return the pixelSize
	 */
	public int getPixelSize() {
		return pixelSize;
	}

	/**
	 * @param pixelSize the pixelSize to set
	 */
	public void setPixelSize(int pixelSize) {
		this.pixelSize = pixelSize;
	}

	/**
	 * @return the colorSpaceType
	 */
	public int getColorSpaceType() {
		return colorSpaceType;
	}

	/**
	 * @param colorSpaceType the colorSpaceType to set
	 */
	public void setColorSpaceType(int colorSpaceType) {
		this.colorSpaceType = colorSpaceType;
	}

	/**
	 * @return the orientation
	 */
	public int getOrientation() {
		return orientation;
	}

	/**
	 * @param orientation the orientation to set
	 */
	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}
}


