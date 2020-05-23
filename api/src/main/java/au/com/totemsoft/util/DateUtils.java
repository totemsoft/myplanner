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

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {

	public static final long SECONDS_PER_MINUTE = 60;
	public static final long MILLIS_PER_MINUTE = SECONDS_PER_MINUTE * 1000L;
	
	public static final long SECONDS_PER_HOUR = 60 * 60;
	public static final long MILLIS_PER_HOUR = SECONDS_PER_HOUR * 1000L;
	
	public static final long SECONDS_PER_DAY = 24 * 60 * 60;
	public static final long MILLIS_PER_DAY = SECONDS_PER_DAY * 1000L;

	private static final Locale locale = Locale.getDefault();
	private static final TimeZone defaultTimeZone = TimeZone.getDefault();
	
	private static final double EXCEL_DATE_29_02_1900 = 60.;
	
	private static final String sEXCEL_ZERO_DATE   = "01/01/1900"; // 0=00/01/1900 in Excel
	private static final String sVARIANT_ZERO_DATE = "30/12/1899";
	private static final String sMSSQL_ZERO_DATE   = "01/01/1753";
	private static final String sJAVA_ZERO_DATE    = "01/01/1970";

    public static final Date EXCEL_ZERO_DATE;
    public static final Date MSSQL_ZERO_DATE;
    public static final Date JAVA_ZERO_DATE;

	private static final Calendar calendar = Calendar.getInstance();

	static {
		calendar.set( Calendar.HOUR_OF_DAY, 0 );
		calendar.set( Calendar.MINUTE, 0 );
		calendar.set( Calendar.SECOND, 0 );
		calendar.set( Calendar.MILLISECOND, 0 );

		calendar.set( Calendar.YEAR, 1900 );
		calendar.set( Calendar.MONTH, 0 );
		calendar.set( Calendar.DAY_OF_MONTH, 1 );
		EXCEL_ZERO_DATE = calendar.getTime();

		calendar.set( Calendar.YEAR, 1753 );
		calendar.set( Calendar.MONTH, 0 );
		calendar.set( Calendar.DAY_OF_MONTH, 1 );
		MSSQL_ZERO_DATE = calendar.getTime();

		calendar.set( Calendar.YEAR, 1970 );
		calendar.set( Calendar.MONTH, 0 );
		calendar.set( Calendar.DAY_OF_MONTH, 1 );
		JAVA_ZERO_DATE = calendar.getTime();
	}
	
	public static long JAVA_EXCEL_TIME_DIFF   = JAVA_ZERO_DATE.getTime() - EXCEL_ZERO_DATE.getTime();
	public static long JAVA_EXCEL_DAY_DIFF    = JAVA_EXCEL_TIME_DIFF / MILLIS_PER_DAY;

	// hide ctor
    private DateUtils()
    {
        super();
    }

    /**
     *
     * @return
     */
    public static Date getCurrentDate()
    {
        return getStartOfDay(getCurrentDateTime());
    }

    /**
     *
     * @return
     */
    public static Date getCurrentDateTime()
    {
        return new Date();
    }

    /**
     * Return the start of the day for the specified date.
     *
     * @param date a date
     * @return start of the day of the day for the specified date
     */
    public static Date getStartOfDay(Date date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

	public static Date convertExcelDate2JavaDate(double excelDate) { 
		
		// Excel 2000 incorrectly assumes that the year 1900 is a leap year
		// http://support.microsoft.com/default.aspx?scid=KB;EN-US;Q214326&ID=KB;EN-US;Q214326 
		if ( (int) excelDate >= EXCEL_DATE_29_02_1900)
			excelDate -= 1.;
			
		// 01/01/1900 has numeric value of 1 (not a zero based)
		excelDate -= 1.;

		long ms = (long) ( excelDate * MILLIS_PER_DAY ) - JAVA_EXCEL_TIME_DIFF;
		calendar.setTimeInMillis(ms);
		long ZONE_OFFSET = calendar.get(Calendar.ZONE_OFFSET);
		long DST_OFFSET = calendar.get(Calendar.DST_OFFSET);
		long dtd = ZONE_OFFSET + DST_OFFSET;
		return new Date( ms - dtd );
    }
	public static double convertJavaDate2ExcelDate(Date date) { 
		calendar.setTime(date);
		long ZONE_OFFSET = calendar.get(Calendar.ZONE_OFFSET);
		long DST_OFFSET = calendar.get(Calendar.DST_OFFSET);
		long dtd = ZONE_OFFSET + DST_OFFSET;
		return (double) ( date.getTime() + JAVA_EXCEL_TIME_DIFF + dtd ) / MILLIS_PER_DAY;
    }
	
	
    public static String formatAsSHORT( Date date ) {
        return format( date, java.text.DateFormat.SHORT );
    }
    public static String formatAsMEDIUM( Date date ) {
        return format( date, java.text.DateFormat.MEDIUM );
    }
    public static String formatAsLONG( Date date ) {
        return format( date, java.text.DateFormat.LONG );
    }
    public static String format( Date date, int style ) {
        if ( date == null ) return null;
        return java.text.DateFormat.getDateInstance( style ).format( date );
    }

}
