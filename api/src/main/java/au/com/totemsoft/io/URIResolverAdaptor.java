/*
 *              Argus Software Pty Ltd License Notice
 * 
 * The contents of this file are subject to the Argus Software Pty Ltd
 * License Version 1.0 (the "License"). 
 * You may not use this file except in compliance with the License.
 * A updated copy of the License is available at
 * http://www.argussoftware.net/license/license_agreement.html
 * 
 * The Original Code is argus. The Initial Developer of the Original
 * Code is Argus Software Pty Ltd, All Rights Reserved.
 */

package au.com.totemsoft.io;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Argus Software Pty Ltd</p>
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version                   $Revision: 1.1.1.1 $
 */

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;

public class URIResolverAdaptor implements URIResolver {

	// xslt resolver map
	private Map urlMap;
	
	
	/**
	 * 
	 */
	public URIResolverAdaptor(Map urlMap) {
		this.urlMap = urlMap;
	}

	public Source resolve(String href, String base) 
		throws TransformerException
	{
		// check our urlMap
		Object obj = urlMap.get( href );
		System.out.println("URIResolverAdaptor::resolving: " + 
			href + ", base: " + base + " [" + obj + "]");

		URL url = null;
		try {
			if (obj instanceof String)
				url = IOUtils.getResource( (String) obj );
			else if (obj instanceof URL)
				url = (URL) obj;

			// href could be valid url
			//url = new URL("" + obj);
			
		} catch (IOException ex) {
			throw new TransformerException("No mapping exists for: " + 
				href + ", base: " + base + ".\n" + ex.getMessage());
						
		}
		
		try {
			//System.out.println("URIResolverAdaptor::resolved to: " + url);
			return new StreamSource( url.toExternalForm() );
		} catch (Exception ex) {
			throw new TransformerException(ex);
		}
		
	}
	
}

