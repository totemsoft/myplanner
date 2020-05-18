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

package com.argus.io;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Argus Software Pty Ltd</p>
 * @author Valeri SHIBAEV
 * @author Last Updated By:   $Author: Valera $
 * @version                   $Revision: 1.4 $
 */

/*
 *	jaxp.properties
 *	#javax.xml.transform.TransformerFactory=org.apache.xalan.processor.TransformerFactoryImpl
 *	#javax.xml.parsers.SAXParserFactory=org.apache.xerces.jaxp.SAXParserFactoryImpl
 *	#javax.xml.parsers.DocumentBuilderFactory=org.apache.xerces.jaxp.DocumentBuilderFactoryImpl
 *	
 * 	Setup for the Crimson xml parsers
 *	# Add the XML parser jars and set the JAXP factory names
 *	# Crimson parser JAXP setup(default)
 *	CLASSPATH=$CLASSPATH:../lib/crimson.jar
 *	JAXP=-Djavax.xml.parsers.DocumentBuilderFactory=org.apache.crimson.jaxp.DocumentBuilderFactoryImpl
 *	JAXP="$JAXP -Djavax.xml.parsers.SAXParserFactory=org.apache.crimson.jaxp.SAXParserFactoryImpl"
 *
 *	Setup for the Xerces xml parsers
 *	# Add the XML parser jars and set the JAXP factory names
 *	# Xerces parser JAXP setup
 *	CLASSPATH=$CLASSPATH:../lib/xerces.jar
 *	JAXP=-Djavax.xml.parsers.DocumentBuilderFactory=org.apache.xerces.jaxp.DocumentBuilderFactoryImpl
 *	JAXP="$JAXP -Djavax.xml.parsers.SAXParserFactory=org.apache.xerces.jaxp.SAXParserFactoryImpl"
 */

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public final class XMLUtils {

	private static final Logger LOG = Logger.getLogger(XMLUtils.class.getName());
	
	public static final String TRANSFORMER_FACTORY = "javax.xml.transform.TransformerFactory";
	public static final String DOCUMENT_BUILDER_FACTORY = "javax.xml.parsers.DocumentBuilderFactory";
	public static final String SAX_PARSER_FACTORY = "javax.xml.parsers.SAXParserFactory";

	public static void clearJAXP() {
		Properties props = System.getProperties();
		props.remove( TRANSFORMER_FACTORY );
		props.remove( SAX_PARSER_FACTORY );
		props.remove( DOCUMENT_BUILDER_FACTORY );
		System.setProperties( props );
	}

	public static Properties setOracleJAXP() {
		Properties oldProperties = getJAXPSettings();
		System.setProperty( TRANSFORMER_FACTORY, "oracle.xml.jaxp.JXSAXTransformerFactory" );
		System.setProperty( SAX_PARSER_FACTORY, "oracle.xml.jaxp.JXSAXParserFactory" );
		System.setProperty( DOCUMENT_BUILDER_FACTORY, "oracle.xml.jaxp.JXDocumentBuilderFactory" );
		return oldProperties;
	}

	public static Properties setXercesJAXP() {
		Properties oldProperties = getJAXPSettings();
		System.setProperty( TRANSFORMER_FACTORY, "org.apache.xalan.processor.TransformerFactoryImpl" );
		//System.setProperty( TRANSFORMER_FACTORY, "org.apache.xalan.xsltc.trax.TransformerFactoryImpl" );
		System.setProperty( SAX_PARSER_FACTORY, "org.apache.xerces.jaxp.SAXParserFactoryImpl" );
		System.setProperty( DOCUMENT_BUILDER_FACTORY, "org.apache.xerces.jaxp.DocumentBuilderFactoryImpl" );
		return oldProperties;
	}

	/**
	 * TODO: set "javax.xml.transform.TransformerFactory"="org.apache.crimson.jaxp.???"
	 * @return
	 */
	public static Properties setCrimsonJAXP() {
		Properties oldProperties = getJAXPSettings();
		System.setProperty( TRANSFORMER_FACTORY, "org.apache.crimson.jaxp." );
		System.setProperty( SAX_PARSER_FACTORY, "org.apache.crimson.jaxp.SAXParserFactoryImpl" );
		System.setProperty( DOCUMENT_BUILDER_FACTORY, "org.apache.crimson.jaxp.DocumentBuilderFactoryImpl" );
		return oldProperties;
	}

	public static Properties getJAXPSettings() {
		Properties properties = new Properties();
		
		String s = System.getProperty(TRANSFORMER_FACTORY);
		if (s != null) properties.setProperty( TRANSFORMER_FACTORY, s );
		
		s = System.getProperty(DOCUMENT_BUILDER_FACTORY);
		if (s != null) properties.setProperty( DOCUMENT_BUILDER_FACTORY, s );
		
		s = System.getProperty(SAX_PARSER_FACTORY);
		if (s != null) properties.setProperty( SAX_PARSER_FACTORY, s );

		return properties;
	}
	
	public static Properties setJAXPSettings(Properties properties) {
		Properties oldProperties = getJAXPSettings();

		String s = properties.getProperty(TRANSFORMER_FACTORY);
		if (s == null) System.getProperties().remove(TRANSFORMER_FACTORY);
		else System.setProperty( TRANSFORMER_FACTORY, s );
		
		s = properties.getProperty(DOCUMENT_BUILDER_FACTORY);
		if (s == null) System.getProperties().remove(DOCUMENT_BUILDER_FACTORY);
		else System.setProperty( DOCUMENT_BUILDER_FACTORY, s );
		
		s = properties.getProperty(SAX_PARSER_FACTORY);
		if (s == null) System.getProperties().remove(SAX_PARSER_FACTORY);
		else System.setProperty( SAX_PARSER_FACTORY, s );

		return oldProperties;
	}
	
	public static void printJAXPSettings() {
		System.out.println( TRANSFORMER_FACTORY + "=" + System.getProperty(TRANSFORMER_FACTORY) );
		System.out.println( DOCUMENT_BUILDER_FACTORY + "=" + System.getProperty(DOCUMENT_BUILDER_FACTORY) );
		System.out.println( SAX_PARSER_FACTORY + "=" + System.getProperty(SAX_PARSER_FACTORY) );
		//System.out.println( System.getProperties() );
	}


	// hide ctor
	private XMLUtils() {}

	/**
	 * create DOM document
	 * @return		XML document (org.w3c.dom.Document)
	 */
	public static Document newDocument() 
		throws ParserConfigurationException 
	{
		return newDocument(null, null);
	}
	public static Document newDocument(DocumentBuilderFactory factory, Properties jaxpAttributes) 
		throws ParserConfigurationException 
	{
		if (factory == null) {
			factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setValidating(true);
        }

		if (jaxpAttributes != null && !jaxpAttributes.isEmpty()) {
			
			Set entries = jaxpAttributes.entrySet();
			Iterator iter = entries.iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				factory.setAttribute( (String) entry.getKey(), entry.getValue() );
			}
			
		}
	
		DocumentBuilder docBuilder = factory.newDocumentBuilder();

		return docBuilder.newDocument();

	}
	

	/**
	 *
	 * @param	source		[in]	Stream of XML document (java.io.InputStream)
	 * @return	destination	[out]	XML document (org.w3c.dom.Document)
	 */
	public static Document parse(InputStream source) 
		throws ParserConfigurationException, IOException, SAXException
	{
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = factory.newDocumentBuilder();
			return docBuilder.parse(source);
			
		} finally {
			try { source.close(); }	catch (IOException ex) {}
		}
		
	}

	/**
	 * 
	 * @param source
	 * @return
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 */
	public static Document parse(File source) 
		throws ParserConfigurationException, IOException, SAXException
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = factory.newDocumentBuilder();
		return docBuilder.parse(source);
			
	}
	
	/**
	 *
	 * @param	source		[in]	Stream of XML document (java.io.InputStream)
	 * @param	destination	[out]	XML document (org.w3c.dom.Document)
	 */
	public static void transform( InputStream source, OutputStream result ) 
		throws TransformerException
	{
		try {
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.transform(new StreamSource(source), new StreamResult(result));
		} finally {
			try {source.close(); } catch (IOException ex) {}
			try {result.close(); } catch (IOException ex) {}
		}
	}
	
	public static void transform( Source source, OutputStream result ) 
		throws TransformerException
	{
		try {
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.transform(source, new StreamResult(result));
		} finally {
			try {result.close(); } catch (IOException ex) {}
		}
	}

	public static void transform( Source source, Result result ) 
		throws TransformerException
	{
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.transform(source, result);
	}

	/**
	 * @param	source			[in]	XML document (org.w3c.dom.Document)
	 * @param	destination		[in]	XML document (org.w3c.dom.Document)
	 * @param	transformation	[in]	Stream of XSL transformation (java.io.OutputStream)
	 * @param	errorListener	[in]	ErrorListener
	 * @param	uriResolver		[in]	URIResolver
	 * @return  List of messages generated in xslt
	 */
	public static List transform(
		Source source,
		Result result,
		Source transformation,
		Properties parameters,
		ErrorListener errorListener,
		URIResolver uriResolver) 
		throws TransformerException
	{	
		//try {System.out.println("Source [" + newDocument().getClass().getName() + "]");} catch (Exception ex) {}
		
		// template can be used for optimization
		TransformerFactory tf = TransformerFactory.newInstance();
		
		if (errorListener != null) 
			tf.setErrorListener(errorListener);
		if (uriResolver != null) 
			tf.setURIResolver(uriResolver);
		
		//Templates templates = tf.newTemplates( transformation );
		//Transformer transformer = templates.newTransformer();
		Transformer transformer = tf.newTransformer( transformation );
		if (errorListener != null) 
			transformer.setErrorListener(errorListener);
		
		if (parameters != null && !parameters.isEmpty()) {
			Set entrySet = parameters.entrySet();
			Iterator iter = entrySet.iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				String name  = (String) entry.getKey();
				String value = (String) entry.getValue();
				transformer.setParameter( name, value );
			}
		}

		//return transform(transformer, source, result);
		transformer.transform(source, result);

		//System.out.println( "xsl:version=" + transformer.getOutputProperty("xsl-version") );

		return Collections.EMPTY_LIST;
		
	}


	/**
	 * 
	 * @param data
	 * @return
	 */
	public static String formatForXML(String data) {
        return data
            .replaceAll( "&",  "&amp;" )
    		.replaceAll( "<",  "&lt;" )
    		.replaceAll( ">",  "&gt;" )
    		.replaceAll( "\'", "&apos;" )
    		.replaceAll( "\"", "&quot;" );
	}
	
//	private static String replaceAll(String data, String regex, String replacement) {
//		//return data.replaceAll( regex, replacement); // JDK 1.4
//		int index = 0;
//		while ( ( index = data.indexOf(regex, index) ) >= 0 ) {
//			data = data.substring(0, index) + replacement + data.substring(index + 1);
//			index++;
//		}
//		return data;
//	}

	/**
	 * 
	 * @param node
	 * @param parentName
	 * @return
	 */
	public static Node getParent(Node node, String parentName) {

		Node parent = node;
		while ( ( parent = parent.getParentNode() ) != null ) {
			if ( parentName.equals( parent.getNodeName() ) )
				return parent;
		}
		return null;
			
	}

	/**
	 * 
	 * @param parent
	 * @param childName
	 * @param indexName
	 * @param indexValue
	 * @return
	 */
	public static Node getChildNode( Node parent, String childName, String indexName, String indexValue ) {
		
		Node lastChild = parent.getLastChild();
		//Node lastChild = parent.getFirstChild();
		while ( lastChild != null ) {
			if ( childName.equals( lastChild.getNodeName() ) ) {
				if (indexValue == null)
					return lastChild;

				Node attrNode = lastChild.getAttributes().getNamedItem(indexName);
				if (attrNode == null)
					return lastChild;

				if (attrNode.getNodeValue().equals(indexValue))
					return lastChild;
			}
			lastChild = lastChild.getPreviousSibling();
			//lastChild = lastChild.getNextSibling();
		}
		return null;
	}

	/**
	 * 
	 * @param node
	 * @param attributeName
	 * @return
	 */
	public static String getAttributeValue(Node node, String attributeName) {
		if (attributeName == null) return null;
		NamedNodeMap attributes = node.getAttributes();
		Node attrNode = attributes == null ? null : attributes.getNamedItem(attributeName);
		return attrNode == null ? null : attrNode.getNodeValue();
	}

	/**
	 * 
	 * @param node
	 * @return
	 */
	public static String getXPATH(Node node) {
		return getXPATH(node, null);		
	}
	/**
	 * Work on all nodes, except Attr, Document, DocumentFragment, Entity, and Notation.
	 * @param node
	 * @param indexName
	 * @return
	 */
	public static String getXPATH(Node node, String indexName) {
		// break condition
		if (node == null || node instanceof Document) 
			return "";
		
		// recursive call
		String indexValue = getAttributeValue(node, indexName);
        //All nodes, except Attr, Document, DocumentFragment, Entity, and Notation may have a parent.
        //However, if a node has just been created and not yet added to the tree,
        //or if it has been removed from the tree, this is null. 
		String xpath = getXPATH(node.getParentNode(), indexName) + "/" + 
			(node.getNodeType() == Node.ATTRIBUTE_NODE ? "@" : "") +
			node.getNodeName() + 
			(indexValue == null ? "" : "[" + indexValue + "]");
		//System.out.println("\t" + xpath);
		return xpath;
		
	}
	
	/**
	 * 
	 * @param node
	 * @param indexName
	 */
	public static void removeAttribute( Element node, String indexName ) {
		
		NodeList nodes = node.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node child = nodes.item(i);
			if (child.getNodeType() != Node.ELEMENT_NODE) continue;
			
			Element childElement = (Element) child;
			if (childElement.hasAttribute(indexName)) {
				//logger.info("\tremoveAttribute on node: " + node.getNodeName() + ", child: " + child.getNodeName());
				childElement.removeAttribute(indexName);
			}
			
			removeAttribute(childElement, indexName);
			
		}
		
	}

    /**
     * Transfer data from xml document to Map.
     * Data format: Map[xpath, value]
     * @param source The xml document.
     * @param addRootNodeName Control whether Root Node Name will be added to generated xpath.
     * @return Map
     */
    public static Map transfer(
        Document source, boolean addRootNodeName)
    {
        if (source == null)
            return null;

        Element root = source.getDocumentElement();
        
        Map result = new Hashtable();
        doTransfer(root, root.getNodeName(), result, addRootNodeName);
        return result;

    }
        
    private static void doTransfer(
        Element source, String xpath, Map result, boolean addParentNodeName)
    {
        NamedNodeMap attributes = source.getAttributes();
        for (int i = 0; attributes != null && i < attributes.getLength(); i++) {
            Node node = attributes.item(i);
            String nodeName = node.getNodeName();
            // construct this node xpath
            String nodeXPATH = addParentNodeName ? xpath + "/" + nodeName : nodeName;
            result.put(nodeXPATH, node.getNodeValue());
            LOG.finest("Attribute: " + nodeXPATH + "=" + node.getNodeValue());
        }

        NodeList nodes = source.getChildNodes();
        int index = 0;
        boolean addIndex = isIdenticalChildren(source);
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            String nodeName = node.getNodeName();
            short nodeType = node.getNodeType();
            switch (nodeType) {
                case (Node.TEXT_NODE) : 
                    break;
                case (Node.ELEMENT_NODE) :
                    // construct this node xpath and position (if nessesary)
                    String nodeXPATH = addParentNodeName ? xpath + "/" + nodeName : nodeName;
                
                    // check if this element has value
                    Node firstChild = node.getFirstChild();
                    String firstChildValue = firstChild == null ? null : firstChild.getNodeValue();
                    if (firstChildValue != null && firstChildValue.trim().length() > 0) {
                        result.put(nodeXPATH, firstChild.getNodeValue());
                        LOG.finest("Element: " + nodeXPATH + "=" + firstChild.getNodeValue());
                    }

                    // recursively transfer children
                    doTransfer(
                        (Element) node, 
                        nodeXPATH + (addIndex ? "[" + ++index + "]" : ""), 
                        result, 
                        true);
                    
                    break;
                default :
                    System.err.println("XMLTransferer::doTransfer() - Unhandled nodeType=" + nodeType + ".");
            }
            
        }
        
    }
    
    private static boolean isIdenticalChildren(Node source)
    {
        NodeList nodes = source.getChildNodes();
        String prevNodeName = null;
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            String nodeName = node.getNodeName();
            short nodeType = node.getNodeType();
            switch (nodeType) {
                case (Node.TEXT_NODE) : 
                    break;
                case (Node.ELEMENT_NODE) :
                    // check if this node has same name as previous
                    if (prevNodeName != null && !nodeName.equals(prevNodeName))
                        return false;
                    prevNodeName = nodeName;
                    break;
                default :
                    System.err.println("XMLTransferer::doTransfer() - Unhandled nodeType=" + nodeType + ".");
            }
            
        }
        return true;
    }
        
//    /**
//     * Retrieves a xml-fo document byte array
//     * for the RTF document template.
//     *
//     * @param templateFile templateFile.
//     * @return byte[] of the xml-fo file.
//     * @throws IOException an IOException.
//     * @throws TransformerException a TransformerException.
//     * @throws ParserConfigurationException a ParserConfigurationException.
//     */
//    public static byte [] createDocumentTemplateFo(File templateFile)
//        throws IOException, TransformerException, ParserConfigurationException
//    {
//        InputStream is = new FileInputStream(templateFile);
//        OutputStream os = new ByteArrayOutputStream();
//        try
//        {
//            RTF2FOContext context = RTF2FOContext.prepareContext(null);
//            context.setOption("clip-headers", "true");
//            CommonLogger logger = context.newLogger("result", NSDCContext.NORMAL_LOG);
//
//            Document [] documents = RTF2FO.convert(
//                is, context, logger, null, 
//                DocumentBuilderFactory.newInstance().newDocumentBuilder().getDOMImplementation());
//
//            //TransformerFactory tf = TransformerFactory.newInstance();
//            //Transformer transformer = tf.newTransformer();
//            //transformer.
//            transform(new DOMSource(documents[0]), new StreamResult(os));
//
//            return os.toByteArray();
//
//        }
//        finally
//        {
//            is.close();
//            os.close();
//        }
//    }

}
