/*
 *              Argus Software Pty Ltd License Notice
 * 
 * The contents of this file are subject to the Argus Software Pty Ltd
 * License Version 1.0 (the "License"). 
 * You may not use this file except in compliance with the License.
 * A updated copy of the License is available at
 * http://www.argussoftware.net/license/
 * 
 * The Original Code is argus. The Initial Developer of the Original
 * Code is Argus Software Pty Ltd, All Rights Reserved.
 */

package au.com.totemsoft.util;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Argus Software Pty Ltd</p>
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version                   $Revision: 1.1.1.1 $
 */

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.jar.JarFile;

public class Builder {

    private String jarFileName;
    private String classDir;
    
	/**
	 * 
	 */
	public Builder(String jarFileName) {
	    this.jarFileName = jarFileName;
	}

    /**
     * @return Returns the jarFileName.
     */
    public String getJarFileName() {
        return jarFileName;
    }

    /**
     * @return Returns the classDir.
     */
    public String getClassDir() {
        return classDir;
    }
    /**
     * @param classDir The classDir to set.
     */
    public void setClassDir(String classDir) {
        this.classDir = classDir;
    }

	/**
	 * 
	 *
	 */
	public void run() {
	    
	    try {
	        File file = new File(jarFileName);
	        if (!file.exists() && !file.createNewFile())
                System.err.println("FAILED to create: " + file);
	        
            JarFile jarFile = new JarFile(jarFileName);
            
            File classDir = new File(this.classDir);
            File [] javaFiles = classDir.listFiles( new FileFilter() {
                public boolean accept(File pathname) {
                    return pathname.getName().indexOf(".java") > 0;
                }                
            });
            
            for (int i = 0; i < javaFiles.length; i++) {
                File javaFile = javaFiles[i];
                boolean result = Compiler.compileClasses(javaFile.getCanonicalPath());
                if (!result)
                    System.err.println("FAILED to compile: " + javaFile);
            }
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
	    
	}

}
