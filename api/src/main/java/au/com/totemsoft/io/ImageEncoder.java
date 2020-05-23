/*
 * Encoder.java Created on 12 March 2003, 12:21
 */

package au.com.totemsoft.io;

/**
 *
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */

public interface ImageEncoder {

    public void encodeAsJPEG(java.io.File file) throws Exception;

    public void encodeAsJPEG(java.io.File file, javax.swing.JComponent comp) throws Exception;

}
