/*
 * ImageUtils.java
 *
 * Created on 17 January 2002, 16:05
 */

package com.argus.io;

/**
 *
 * @author  valeri chibaev
 * @version 
 */

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import com.klg.jclass.util.swing.encode.EncoderException;
import com.klg.jclass.util.swing.encode.JCEncodeComponent;

public class ImageUtils {

    /** Creates new ImageUtils */
    private ImageUtils() {}

//
//    /**
//Question:   How do I save a picture drawn on a Canvas object, into a Jpeg file? 
//
//I searched and found a Jpeg encoder that accepts an Image as the argument. 
//     But to use it, I have to convert my Canvas (or picture on the Canvas) to an Image object. 
//     Can someone tell me how to do that? 
//     Or if you can suggest an easier way to save the picture into Jpeg, that would be great as well. 
//     *
//     * First you need to use the createImage method with Canvas. 
//     * (Try createImage(int, int) method of the Component class). 
//     * This gives you the image. 
//     * Then you need to paint your canvas onto the new image. 
//     * Try something like this: 
//     */
//    public static void encodeAsJPEG( BufferedImage source, OutputStream dest ) throws IOException {
//
//        if ( source == null || dest == null ) return;
//        
//        // This creates an instance of a JPEGImageEncoder 
//        // that can be used to encode image data as JPEG Data streams.
//        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder( dest );
//
//        try {
//            // Encode a BufferedImage as a JPEG data stream. 
//            // Note, some color conversions may takes place. 
//            // The current JPEGEncodeParam's encoded COLOR_ID should match 
//            // the value returned by getDefaultColorID when given the BufferedImage's ColorModel.
//            // If no JPEGEncodeParam object has been provided yet a default one 
//            // will be created by calling getDefaultJPEGEncodeParam with image.
//            encoder.encode( source );
//        } catch (Exception e ) {
//            throw new IOException( e.getMessage() );
//        }
//        
//    }
//    
//    public static void encodeAsJPEG( BufferedImage source, File file ) throws IOException {
//        encodeAsJPEG( source, new java.io.FileOutputStream( file ) );
//    }
    
    
    public static String encodeAsJPEG(ImageEncoder encoder) throws Exception {
        
        java.io.File file = java.io.File.createTempFile( "temp", JPEGFileFilter.DOT_JPG );
        file.deleteOnExit();

        encoder.encodeAsJPEG( file );
        return file.getCanonicalPath();
    
    }
    
    
    /**
     *  GIF  - Only enabled if user has a GIF output license from Unisys. 
     *  PNG  - Portable Network Graphics format. 
     *  JPEG - Joint Photographic Experts Group format. 
     *  EPS  - Encapsulated PostScript format. Requires JClass PageLayout. 
     *  PDF  - Acrobat Portable Document format. Requires JClass PageLayout. 
     *  PS   - PostScript Level 2 format. Requires JClass PageLayout. 
     *  PCL  - Hewlett-Packard Page Control Language version 5. Requires JClass PageLayout. 
     */
    public static void encode( JCEncodeComponent.Encoding encoding,
        Component component, OutputStream output)
            throws IOException, EncoderException {
        JCEncodeComponent.encode( encoding, component, output );
    }
    public static void encodeAsJPEG( 
        Component component, OutputStream output)
            throws IOException, EncoderException {
        encode( JCEncodeComponent.JPEG, component, output );
    }
    public static void encodeAsPNG( 
        Component component, OutputStream output)
            throws IOException, EncoderException {
        encode( JCEncodeComponent.PNG, component, output );
    }
    
    public static void encode( JCEncodeComponent.Encoding encoding,
        Component component, File file )
            throws IOException, EncoderException {
        JCEncodeComponent.encode( encoding, component, file );
    }
    public static void encodeAsJPEG( 
        Component component, File file )
            throws IOException, EncoderException {
        encode( JCEncodeComponent.JPEG, component, file );
    }
    public static void encodeAsPNG( 
        Component component, File file )
            throws IOException, EncoderException {
        encode( JCEncodeComponent.PNG, component, file );
    }
        
}
