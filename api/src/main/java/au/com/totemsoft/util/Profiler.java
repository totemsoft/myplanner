/*
 *              Argus Software Pty Ltd License Notice
 * 
 * The contents of this file are subject to the Argus Software Pty Ltd
 * License Version 1.0 (the "License"). You may not use this file except
 * in compliance with the License. A copy of the License is available at
 * http://www.argussoftware.net/license/license_agreement.html
 * 
 * The Original Code is argus. The Initial Developer of the Original
 * Code is Argus Software Pty Ltd, All Rights Reserved.
 */

package au.com.totemsoft.util;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Argus Software Pty Ltd</p>
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version                   $Revision: 1.1.1.1 $
 */

import java.util.logging.Logger;

public class Profiler {

	private static final Logger _logger = Logger.getLogger( Profiler.class.getName() );
	
	
	private long ms;
	private String processName;
	private Logger logger;
	
	/**
	 * 
	 */
	public Profiler() {
		this(_logger);
	}

	public Profiler(Logger logger) {
		this.logger = logger;
	}

	public void start() {
		this.ms = System.currentTimeMillis();
		this.processName = "";
	}
	public void start(String processName) {
		this.ms = System.currentTimeMillis();
		this.processName = processName;
		logger.finest(processName + " starts..."); 
	}

	public void stop() {
		stop(processName);
	}

	public void stop(String processName) {
		logger.finest(processName + " stopped in " + ( System.currentTimeMillis() - ms ) / 1000. + " seconds.");
	}

	public void println(String s) {
		logger.info(s);
	}

}
