/*
 * DateTime.java
 *
 * Created on 17 August 2001, 10:03
 */

package com.argus.util;

/**
 *
 * @author  valeri chibaev
 * @version 
 */


public class DateTimeUtils {

    
    public final static String sMAX_DATE = "01/01/2200";
    public final static java.util.Date MAX_DATE = getDate( sMAX_DATE );
    
    public final static String sMIN_MSSQL_DATE = "01/01/1753";
    public final static java.util.Date MIN_MSSQL_DATE = getDate( sMIN_MSSQL_DATE );
    

    public static final int MONTHS_PER_YEAR = 12;
    public static final int SECONDS_PER_DAY = 24 * 60 * 60;
    public static final int MILLIS_PER_DAY  = SECONDS_PER_DAY * 1000;    
    public static final double MILLIS_PER_YEAR = MILLIS_PER_DAY  * 365.0; // 365.2

    // set some constants that allow pretty accurate estimates over
    //  about 2,000 years before or after the present.  These are
    //  slight overestimates since that's what we want (and need!)
    private static final double DAY_MILLIS = 1000*60*60 * 24.0015;
    private static final double WEEK_MILLIS = DAY_MILLIS * 7;
    private static final double MONTH_MILLIS = DAY_MILLIS * 30.43675;
    private static final double YEAR_MILLIS = WEEK_MILLIS * 52.2;

    public static final int WEEKS_PER_YEAR = 52;
    public static final int FORTNIGHTS_PER_YEAR = WEEKS_PER_YEAR / 2;
    
    public static final int DAYS_PER_YEAR = 365;

    // for JTextEdit
    public final static String DEFAULT_INPUT = "dd/MM/yyyy";
    
    // Oracle/SQL-Server Date is in yyyy-MM-dd
    public final static String DEFAULT_JDBC = "yyyy-MM-dd";
    
    
    public static java.text.DateFormat localeDateFormatter;
    
    public static java.text.DateFormat getLocaleDateFormatter() {
        if ( localeDateFormatter == null )
            localeDateFormatter = new java.text.SimpleDateFormat( DEFAULT_INPUT ); 
            //localeDateFormatter = java.text.DateFormat.getDateInstance( java.text.DateFormat.MEDIUM, java.util.Locale.ENGLISH );
            
        return localeDateFormatter;
    }
    
    /**
     *
     */
    public static String getCurentDate() {
        //java.util.Calendar calendar = java.util.Calendar.getInstance();
        //return "" + calendar.get(java.util.Calendar.YEAR) + ( calendar.get(java.util.Calendar.MONTH) + 1 ) + calendar.get(java.util.Calendar.DAY_OF_MONTH);
        return java.text.MessageFormat.format(
            "{0,date,yyyymmdd}",
            new Object [] { new java.util.Date() }
        );
    }
    
    public static String getCurentTime() {
        //java.util.Calendar calendar = java.util.Calendar.getInstance();
        //return "" + calendar.get(java.util.Calendar.YEAR) + ( calendar.get(java.util.Calendar.MONTH) + 1 ) + calendar.get(java.util.Calendar.DAY_OF_MONTH);
        return java.text.MessageFormat.format(
            "{0,time,hhmmss}",
            new Object [] { new java.util.Date() }
        );
    }
    
    public static String getCurentDateTime() {
        return getCurentDate() + "_" + getCurentTime();
    }
    
    public static int getCurrentYear() {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        return calendar.get(java.util.Calendar.YEAR);
    }
    
    public static int getFinancialYearEnd() {
        return getFinancialYearEnd( new java.util.Date() );
    }
    public static int getFinancialYearEnd( java.util.Date date ) {
        
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime( date );
        int year = calendar.get(java.util.Calendar.YEAR);
        int month = calendar.get(java.util.Calendar.MONTH);
        
        if ( month >= java.util.Calendar.JULY && month <= java.util.Calendar.DECEMBER )
            year++;
        
        return year;
        
    }
    
    public static int getYear( java.util.Date date ) {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime( date );
        return calendar.get(java.util.Calendar.YEAR);
    }
    
    public static int getTotalDays( int year ) {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.set( year, 12, 31 );
        return calendar.get(java.util.Calendar.DAY_OF_YEAR);
    }
    public static int getTotalDays( java.util.Date date ) {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime( date );
        return getTotalDays( calendar.get(java.util.Calendar.YEAR) );
    }
    
    public static long getTimeInMillis( java.util.Calendar calendar ) {
        if ( calendar == null ) return 0;

        /**
         *      !!!!!!!!!!!!!!!! JDK 1.4 end higher !!!!!!!!!!!!!!!!!!!!!!!
         */
        //if ( SwingUtils.JDK_1_4 )
        //    return calendar.getTimeInMillis();
        
        /**
         *      !!!!!!!!!!!!!!!! JDK 1.3 end less !!!!!!!!!!!!!!!!!!!!!!!
         */
        return calendar.getTime().getTime();
    }
   
    public static long getTimeInMillis( java.util.Date date ) {
        if ( date == null ) return 0;

        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime( date );
        return getTimeInMillis( calendar );
    }
   
    public static long getTimeInMillis( java.util.Date fromDate, java.util.Date toDate ) {
        if ( fromDate == null || toDate == null ) return 0;

        java.util.Calendar calendar = java.util.Calendar.getInstance();
        
        calendar.setTime( toDate );
        long diff = getTimeInMillis( calendar );

        calendar.setTime( fromDate );
        return diff - getTimeInMillis( calendar );
    }
   
    public static int getTimeInDays( java.util.Date fromDate, java.util.Date toDate ) {
        return (int) ( getTimeInMillis( fromDate, toDate ) / MILLIS_PER_DAY );
    }

    public static int getTimeInYears( java.util.Date fromDate, java.util.Date toDate ) {
        return (int) ( getTimeInMillis( fromDate, toDate ) / MILLIS_PER_YEAR );
    }

    public static double getYears( java.util.Date fromDate, java.util.Date toDate ) {
        return getTimeInMillis( fromDate, toDate ) / MILLIS_PER_YEAR;
    }

    public static double getYears( java.util.Date fromDate ) {
        return getTimeInMillis( fromDate, new java.util.Date() ) / MILLIS_PER_YEAR;
    }
    
    
    /**
     *
     */
    public static Double getAgeDouble( java.util.Date dateOfBirth ) {
        if ( dateOfBirth == null ) 
            return null;

        java.util.Calendar calendar = java.util.Calendar.getInstance();
        int years = calendar.get(java.util.Calendar.YEAR);
        int months = calendar.get(java.util.Calendar.MONTH);
        int days = calendar.get(java.util.Calendar.DAY_OF_MONTH);

        calendar.setTime( dateOfBirth );
        years -= calendar.get(java.util.Calendar.YEAR);
        months -= calendar.get(java.util.Calendar.MONTH);
        days -= calendar.get(java.util.Calendar.DAY_OF_MONTH);

        if ( years <= 0 ) return null;

        if ( months > 0 )
            return new Double( years );
        if ( months < 0 )
            return new Double( --years );

        // months == 0
        if ( days < 0 ) 
            return new Double( --years );
        return new Double( years );
        
    }
   
    public static Double getAgeDouble( java.util.Date laterDate, java.util.Date earlierDate ) {
        if ( laterDate == null || earlierDate == null ) 
            return null;

        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime( laterDate );
        
        int years = calendar.get(java.util.Calendar.YEAR);
        int months = calendar.get(java.util.Calendar.MONTH);
        int days = calendar.get(java.util.Calendar.DAY_OF_MONTH);

        calendar.setTime( earlierDate );
        years -= calendar.get(java.util.Calendar.YEAR);
        months -= calendar.get(java.util.Calendar.MONTH);
        days -= calendar.get(java.util.Calendar.DAY_OF_MONTH);

        if ( years <= 0 ) return null;

        if ( months > 0 )
            return new Double( years );
        if ( months < 0 )
            return new Double( --years );

        // months == 0
        if ( days < 0 ) 
            return new Double( --years );
        return new Double( years );
        
    }
    
   public int getDateDiff( int calUnit, java.util.Date d1, java.util.Date d2 ) {
      // swap if d1 later than d2
      boolean neg = false;
      if( d1.after(d2) ) {
         java.util.Date temp = d1;
         d1 = d2;
         d2 = temp;
         neg = true;
      }

      // estimate the diff.  d1 is now guaranteed <= d2
      int estimate = (int)getEstDiff( calUnit, d1, d2 );

      // convert the Dates to GregorianCalendars
      java.util.GregorianCalendar c1 = new java.util.GregorianCalendar();
      c1.setTime(d1);
      java.util.GregorianCalendar c2 = new java.util.GregorianCalendar();
      c2.setTime(d2);

      // add 2 units less than the estimate to 1st date,
      //  then serially add units till we exceed 2nd date
      c1.add( calUnit, (int)estimate - 2 );
      for( int i=estimate-1; ; i++ ) {         
         c1.add( calUnit, 1 );
         if( c1.after(c2) )
            return neg ? 1-i : i-1;
      }
   }

   private int getEstDiff( int calUnit, java.util.Date d1, java.util.Date d2 ) {
      long diff = d2.getTime() - d1.getTime();
      switch (calUnit) {
      case java.util.Calendar.DAY_OF_WEEK_IN_MONTH :
      case java.util.Calendar.DAY_OF_MONTH :
//      case Calendar.DATE :      // codes to same int as DAY_OF_MONTH
         return (int) (diff / DAY_MILLIS + .5);  
      case java.util.Calendar.WEEK_OF_YEAR :
         return (int) (diff / WEEK_MILLIS + .5);
      case java.util.Calendar.MONTH :
         return (int) (diff / MONTH_MILLIS + .5);
      case java.util.Calendar.YEAR :
         return (int) (diff / YEAR_MILLIS + .5);
      default:
         return 0;
      } /* endswitch */
   }

            
    public static Double getAgeDouble( String dateOfBirth ) {
        return getAgeDouble( getSqlDate( dateOfBirth ) );
    }
    
    
    /**
     * Financial Year days 1/7/NNNN to 30/6/NNNN + 1(day)
     */
    public static double getFinancialYearFraction( java.util.Date calcDate, java.util.Date startDate, java.util.Date endDate ) {
        if ( startDate == null && endDate == null ) return 1.;
        return getFinancialYearFraction( getFinancialYearEnd( calcDate ), startDate, endDate );
    }
    
    public static double getFinancialYearFraction( int financialYearEnd, java.util.Date startDate, java.util.Date endDate ) {
        if ( startDate == null && endDate == null ) return 1.;
        return getFinancialYearFraction( startDate, endDate, financialYearEnd, financialYearEnd );
    }
    public static double getFinancialYearFraction( java.util.Date startDate, java.util.Date endDate, int financialYearEnd1, int financialYearEnd2 ) {
        
        if ( startDate == null && endDate == null ) return 1.;
        
        // financial year limits
        final long START = getDate( "1/7/" + ( financialYearEnd1 - 1 ) ).getTime(); // 00:00
        final long END   = getDate( "1/7/" + financialYearEnd2 ).getTime(); // 30/6/YYYY 00:00 + 1 day
        long fullYearLength = END - START;
        
        long start = startDate == null ? START : startDate.getTime();
        if ( start < START ) start = START;
        
        long end   = endDate   == null ? END   : endDate.getTime() + MILLIS_PER_DAY; // dd/mm/yyyy 00:00 + 1 day
        if ( end > END ) end = END;
        
        long yearLength = end - start;
        if ( yearLength <= 0 ) return 0.; // equals or end before start
        
        return (double) yearLength / (double) fullYearLength;

    }
    
    public static double getFinancialYearFraction( java.util.Date calcDate, boolean left ) {

        int year = getFinancialYearEnd( calcDate );
        

        if ( left ) {

            java.util.Date endDate = getDate( "30/6/" + year );
            
            /* OLD VERSION: 
             * return
                (double)( date.getTime() - calcDate.getTime() ) / 
                (double)( date.getTime() - getDate( "1/7/" + --year ).getTime() );
             */

            // BEGIN: BUGFIX NO. 600 - 03/07/2002
            //        by shibaevv
            /* 
             * need to add one day, because starting day has to be included
             *       _______________________________
             *     /                                \
             *     |       + x. days                \|/
             *     |                                 "
             *  ---*---------------------------------*--------->>>
             *  calcDate                            30/6
             *
             *  calcDate not included in x days!!!
             */
            double helper_1 = (double)( endDate.getTime() - calcDate.getTime() ) + MILLIS_PER_DAY;
            double helper_2 = MILLIS_PER_YEAR;            
            
            return helper_1/helper_2;              
            // END: BUGFIX NO. 600 - 03/07/2002
            
        } else {
             java.util.Date startDate = getDate( "1/7/" + ( year - 1 ) );
             return (double)( calcDate.getTime() - startDate.getTime() ) / (double)( getDate( "30/6/" + year ).getTime() - startDate.getTime() );
            
        }
        
    }
    
    
    //public static int getNumberOfMonthlyPeriods( java.util.Date calcDate, boolean left ) {
    //    return getNumberOfPeriods( calcDate, left, FrequencyCode.MONTHLY, 15 );
    //}
    public static int getNumberOfPeriods( java.util.Date calcDate, Integer frequencyCodeID, boolean left ) {
        return getNumberOfPeriods( calcDate, left, frequencyCodeID, 0 );
    }
    public static int getNumberOfPeriods( 
            java.util.Date calcDate, boolean left, Integer frequencyCodeID, int periodStartDay ) {
        
        double fraction = getFinancialYearFraction( calcDate, left );
        double numberOfPeriods = frequencyCodeID == null ? 0 : IFrequencyCode.NUMBER_OF_PERIODS[frequencyCodeID.intValue()];
        int num = (int) ( fraction * numberOfPeriods );
        
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime( calcDate );
        
        int day;
        int month = calendar.get(java.util.Calendar.MONTH);
        switch ( frequencyCodeID.intValue() ) {
            //case 1: return 1; // ONLY_ONCE
            //case 2: return 365; // DAILY 
            case IFrequencyCode.iWEEKLY: // WEEKLY
                if ( periodStartDay == 0 ) periodStartDay = 4;
                day = calendar.get(java.util.Calendar.DAY_OF_WEEK); 
                if ( day > periodStartDay ) num--;
                break;
            //case 4: return 26; // FORTNIGHTLY
            //case 5: return 24; // TWICE_MONTHLY
            case IFrequencyCode.iMONTHLY: // MONTHLY
                if ( periodStartDay == 0 ) periodStartDay = 15;
                day = calendar.get(java.util.Calendar.DAY_OF_MONTH); 
                if ( day > periodStartDay ) num--;
                break;
            case IFrequencyCode.iEVERY_OTHER_MONTH: // EVERY_OTHER_MONTH
                break;
            case IFrequencyCode.iEVERY_THREE_MONTHS:  // EVERY_THREE_MONTHS
                if ( periodStartDay == 0 ) periodStartDay = 15;
                day = calendar.get(java.util.Calendar.DAY_OF_MONTH);
                if ( ( day > periodStartDay ) && 
                     ( month == java.util.Calendar.MARCH || month == java.util.Calendar.JUNE ||
                       month == java.util.Calendar.SEPTEMBER || month == java.util.Calendar.DECEMBER ) ) num--;
                break;
            case IFrequencyCode.iEVERY_FOUR_MONTHS: // EVERY_FOUR_MONTHS
                break;
            case IFrequencyCode.iHALF_YEARLY: // HALF_YEARLY
                if ( periodStartDay == 0 ) periodStartDay = 15;
                day = calendar.get(java.util.Calendar.DAY_OF_MONTH);
                if ( ( day > periodStartDay ) && 
                     ( month == java.util.Calendar.JUNE || month == java.util.Calendar.DECEMBER ) ) num--;
                break;
            case IFrequencyCode.iYEARLY: // YEARLY
                if ( periodStartDay == 0 ) periodStartDay = 15;
                day = calendar.get(java.util.Calendar.DAY_OF_MONTH); 
                if ( day > periodStartDay && 
                     month == java.util.Calendar.JUNE ) num--;
                break;
            //case 12: return .5; // EVERY_OTHER_YEAR
            default: day = 0;//smoothlogic.fps.util.BadArgumentException.BAD_DOUBLE_VALUE;
        }
        
        
        return num;
    }


    /**
     *
     */
    public static boolean isValidDate( String date ) {
        
        if ( ( date == null ) || ( ( date = date.trim() ).length() == 0 ) ) return true;
        
        java.util.StringTokenizer st = new java.util.StringTokenizer( date, "/" );
        if ( st.countTokens() != 3 ) return false;
        
        int day;
        int month;
        int year;
        try {
            day = Integer.parseInt( st.nextToken() );
            month = Integer.parseInt( st.nextToken() );
            year = Integer.parseInt( st.nextToken() );
        
            if ( day <= 0 || day > 31 ) return false;
            if ( month <= 0 || month > 12 ) return false;
            if ( year < 1000 || year > 9999 ) return false; // 4 digit year, 1/1/2000
        
            getLocaleDateFormatter().parse( date );
            return true; 
        } catch ( NumberFormatException nfe ) {
            String msg = "Correct format is dd/mm/yyyy";
            javax.swing.JOptionPane.showMessageDialog( null, msg, "Format is wrong!", javax.swing.JOptionPane.ERROR_MESSAGE );
            return false;
        } catch ( java.text.ParseException e ) { 
            
            System.err.println( e.getMessage() );
            return false; 
        }
        
    }

    
    //public static String asString( java.sql.Date date, String format ) {
    public static String asString( java.util.Date date ) {
        return asString( date, null );
    }
    
    public static String asString( java.util.Date date, String format ) {
        if ( date == null ) return null;
        
        if ( format == null ) format = DEFAULT_INPUT; 
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat( format );
        
        return sdf.format( date );
    }
 
    //public static String getJdbcDate( java.sql.Date date ) {
    public static String getJdbcDate( java.util.Date date ) {
        return asString( date, DEFAULT_JDBC );
    }
    
    // in 2002/2003 financial year, financialYear=2002
    public static java.util.Date getDate( java.util.Date date, int financialYear ) {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime( date );
        
        int amount = financialYear - calendar.get(java.util.Calendar.YEAR);
        if ( calendar.get(java.util.Calendar.MONTH) < java.util.Calendar.JULY )
            amount++;
        calendar.roll( java.util.Calendar.YEAR, amount );
        
        return calendar.getTime();
    }
    
    public static java.util.Date getDate( String date ) {
        return getDate( date, null );
    }
    
    public static java.util.Date getDate( String date, String format ) {
        if ( format == null ) format = DEFAULT_INPUT; 
        
        try { return new java.text.SimpleDateFormat( format ).parse( date ); } 
        catch ( java.text.ParseException pe ) { return null; }
    }
    
    public static String getJdbcDate( String date ) {
        if (date == null) return null;
        
        java.util.Date d;
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat( DEFAULT_INPUT );
        
        try { d = sdf.parse( date ); } 
        catch ( java.text.ParseException pe ) { return null; }
        
        // Oracle Date is in yyyy-MM-dd
        sdf = new java.text.SimpleDateFormat( DEFAULT_JDBC );
        
        return sdf.format( d );
    }
    
    
    public static java.sql.Date getSqlDate( String date ) {
        String d = getJdbcDate(date);
        return (d == null) ? null : java.sql.Date.valueOf(d);
    }
    
    
    public static String formatAsSHORT( java.util.Date date ) {
        return format( date, java.text.DateFormat.SHORT );
    }
    public static String formatAsSHORT( String jdbcDate ) {
        return jdbcDate == null ? null : formatAsSHORT( getDate( jdbcDate, DEFAULT_JDBC ) );
    }

    public static String formatAsMEDIUM( java.util.Date date ) {
        return format( date, java.text.DateFormat.MEDIUM );
    }
    public static String formatAsMEDIUM( String jdbcDate ) {
        return jdbcDate == null ? null : formatAsMEDIUM( getDate( jdbcDate, DEFAULT_JDBC ) );
    }
    
    public static String formatAsLONG( java.util.Date date ) {
        return format( date, java.text.DateFormat.LONG );
    }
    public static String formatAsLONG( String jdbcDate ) {
        return jdbcDate == null ? null : formatAsLONG( getDate( jdbcDate, DEFAULT_JDBC ) );
    }

    public static String format( java.util.Date date, int style ) {
        if ( date == null ) return null;
        return java.text.DateFormat.getDateInstance( style ).format( date );
    }


    /**
     * Calaculates the number of days from a given date to the end of the financial year.
     * ATTENTION: The starting day of the given date is NOT included!!!
     *
     * Example: 
     * given date = 13/05/2001
     * end of financial year = 30/06/2001
     * ==> 
     * number of days = 48 days!!!
     *
     * @param calcDate - the starting date
     */
    public static int getDaysToEndOfFinancialYear( java.util.Date calcDate ) {
        int number_of_days          = 0;
        int calcDate_month          = 0;
        int calcDate_year           = 0;
        int endOfFinancialYear_year = 0;
        
        if ( calcDate != null ) {        
            // create a new calendar object with the calcDate
            java.util.Calendar calcDate_calendar = java.util.GregorianCalendar.getInstance();
            calcDate_calendar.setTime( calcDate );
            calcDate_year  = calcDate_calendar.get(java.util.Calendar.YEAR);
            calcDate_month = calcDate_calendar.get(java.util.Calendar.MONTH);
                        
            // check if we need to use the same year for the end of financial year or not
            if ( calcDate_month >= java.util.Calendar.JULY && calcDate_month <= java.util.Calendar.DECEMBER ) {
                endOfFinancialYear_year = calcDate_year + 1;
            } else {
                endOfFinancialYear_year = calcDate_year;
            }
            
            // set date for the end of financial year
            java.util.Calendar endOfFinancialYear_calendar = java.util.GregorianCalendar.getInstance();
            endOfFinancialYear_calendar.setTime( getDate( "30/6/" + endOfFinancialYear_year ) );
            endOfFinancialYear_year = endOfFinancialYear_calendar.get(java.util.Calendar.YEAR);           

            // calcDate and end of financial year in the same year???
            if( calcDate_year == endOfFinancialYear_year ) {
                // yes
                number_of_days = endOfFinancialYear_calendar.get(java.util.Calendar.DAY_OF_YEAR) - calcDate_calendar.get(java.util.Calendar.DAY_OF_YEAR);
            } else {
                // no
                int help_1 = DAYS_PER_YEAR - endOfFinancialYear_calendar.get(java.util.Calendar.DAY_OF_YEAR) - 1;
                int help_2 = DAYS_PER_YEAR - calcDate_calendar.get(java.util.Calendar.DAY_OF_YEAR) - 1;

                number_of_days = help_1 + help_2;

            }
            
        }
        return number_of_days;
    }

    public static java.util.Date getStartOfFinancialYearDate( java.util.Date calcDate ) {    
        int calcDate_month          = 0;
        int calcDate_year           = 0;
        int endOfFinancialYear_year = 0;
        
        if ( calcDate != null ) {        
            // create a new calendar object with the calcDate
            java.util.Calendar calcDate_calendar = java.util.GregorianCalendar.getInstance();
            calcDate_calendar.setTime( calcDate );
            calcDate_year  = calcDate_calendar.get(java.util.Calendar.YEAR);
            calcDate_month = calcDate_calendar.get(java.util.Calendar.MONTH);
                        
            // check if we need to use the same year for the end of financial year or not
            if ( calcDate_month >= java.util.Calendar.JULY && calcDate_month <= java.util.Calendar.DECEMBER ) {
                endOfFinancialYear_year = calcDate_year + 1;
            } else {
                endOfFinancialYear_year = calcDate_year;
            }
            
            // set date for the end of financial year
            java.util.Calendar endOfFinancialYear_calendar = java.util.GregorianCalendar.getInstance();
            endOfFinancialYear_calendar.setTime( getDate( "01/7/" + endOfFinancialYear_year ) );
            return endOfFinancialYear_calendar.getTime();
        }
        return null;
    }
    
    public static java.util.Date getEndOfFinancialYearDate( java.util.Date calcDate ) {    
        
        if ( calcDate == null ) return null;
        
        int calcDate_month          = 0;
        int calcDate_year           = 0;
        int endOfFinancialYear_year = 0;
        
        // create a new calendar object with the calcDate
        java.util.Calendar calcDate_calendar = java.util.GregorianCalendar.getInstance();
        calcDate_calendar.setTime( calcDate );
        calcDate_year  = calcDate_calendar.get(java.util.Calendar.YEAR);
        calcDate_month = calcDate_calendar.get(java.util.Calendar.MONTH);

        // check if we need to use the same year for the end of financial year or not
        if ( calcDate_month >= java.util.Calendar.JULY && calcDate_month <= java.util.Calendar.DECEMBER ) {
            endOfFinancialYear_year = calcDate_year + 1;
        } else {
            endOfFinancialYear_year = calcDate_year;
        }

        // set date for the end of financial year
        java.util.Calendar endOfFinancialYear_calendar = java.util.GregorianCalendar.getInstance();
        endOfFinancialYear_calendar.setTime( getDate( "30/6/" + endOfFinancialYear_year ) );
        return endOfFinancialYear_calendar.getTime();
    }
    
    public static boolean check( java.util.Date start, java.util.Date end, int year ) {
        
        final java.util.Date START = getDate( "1/7/" + year ); // 00:00 ???
        final java.util.Date END = getDate( "30/6/" + ( year + 1 ) );
        
        return between( start, end, START, END );
    }

    public static boolean between( java.util.Date start, java.util.Date end, int year ) { // 0,1,...
        
        if (start == null && end == null) return true ;
        
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime( new java.util.Date() ); // now
        
        calendar.roll( java.util.Calendar.YEAR, year );
        java.util.Date d = calendar.getTime();
        
        if (start == null) return end.after(d) ;
        if (end  == null)  return start.before(d) ;
        
        return end.after(d) && start.before(d) ;
    }

    public static boolean betweenYears( java.util.Date start, java.util.Date end, int year ) { // 0,1,...
        
        if (start == null && end == null) return true ;
        
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime( new java.util.Date() ); // now
        
        calendar.roll( java.util.Calendar.YEAR, year );
        int yearToCompare = calendar.get(java.util.Calendar.YEAR);
        
        int startYear = Integer.MIN_VALUE;
        if ( start != null ) {
            calendar.setTime( start ) ;
            startYear = calendar.get(java.util.Calendar.YEAR);
        }
        
        int endYear = Integer.MAX_VALUE;
        if ( end != null ) {
            calendar.setTime( end ) ;
            endYear = calendar.get(java.util.Calendar.YEAR);
        }
        
        return startYear <= yearToCompare && yearToCompare <= endYear;
        
    }
    
    private static boolean between( 
        java.util.Date start, java.util.Date end, 
        final java.util.Date START, final java.util.Date END ) 
    {
        if ( start != null && start.before( START ) )
            start = null; // ignore
        if ( end != null && end.after( END ) )
            end = null; // ignore;
        
        if ( start == null && end == null )
            return true;
        if ( start == null )
            return end.after( START );
        if ( end == null )
            return start.before( END );
        return true;// ( start.after( START ) && end.before( END ) );
    }
    
    /*
    public static void main( String args[] ) {
        
        System.out.println(
            formatAsMEDIUM( "2002-09-22" )
        );
        
    }
    */
    
}
