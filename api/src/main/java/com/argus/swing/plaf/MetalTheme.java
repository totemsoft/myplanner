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

package com.argus.swing.plaf;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Argus Software Pty Ltd</p>
 * @author Valeri SHIBAEV
 * @author Last Updated By:   $Author: Valera $
 * @version                   $Revision: 1.2 $
 */

import java.awt.Font;

import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;

/**
 * This class describes a theme using "Argus Software" colors.
 */
public class MetalTheme extends DefaultMetalTheme {

    public String getName() { return "Argus Software"; }

    private final ColorUIResource primary1 = new ColorUIResource( 1,  94,  143);
    private final ColorUIResource primary2 = new ColorUIResource(128, 170, 213);
    private final ColorUIResource primary3 = new ColorUIResource(201, 221, 238);

    private final ColorUIResource secondary1 = new ColorUIResource(111,  111,  111);
    private final ColorUIResource secondary2 = new ColorUIResource(130, 130, 130);
    private final ColorUIResource secondary3 = new ColorUIResource(230, 230, 230);

    protected ColorUIResource getPrimary1() { return primary1; }
    protected ColorUIResource getPrimary2() { return primary2; }
    protected ColorUIResource getPrimary3() { return primary3; }

    protected ColorUIResource getSecondary1() { return secondary1; }
    protected ColorUIResource getSecondary2() { return secondary2; }
    protected ColorUIResource getSecondary3() { return secondary3; }

    private final ColorUIResource userText = new ColorUIResource(0, 0,0);

    public ColorUIResource getSystemTextColor()  { return userText; }

    private final FontUIResource controlFont = new FontUIResource("Arial", Font.PLAIN, 12);
    private final FontUIResource systemFont = new FontUIResource("Arial", Font.PLAIN, 12);
    private final FontUIResource windowTitleFont = new FontUIResource("Arial", Font.PLAIN, 12);
    private final FontUIResource userFont = new FontUIResource("Arial", Font.PLAIN, 12);
    private final FontUIResource smallFont = new FontUIResource("Arial", Font.PLAIN, 12);

    public FontUIResource getControlTextFont() { return controlFont;}
    public FontUIResource getSystemTextFont() { return systemFont;}
    public FontUIResource getUserTextFont() { return userFont;}
    public FontUIResource getMenuTextFont() { return controlFont;}
    public FontUIResource getWindowTitleFont() { return windowTitleFont;}
    public FontUIResource getSubTextFont() { return smallFont;}

}
