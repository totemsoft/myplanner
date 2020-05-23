/*
 * TableDisplayMode.java
 *
 * Created on 27 December 2002, 10:48
 */

package au.com.totemsoft.myplanner.code;

/**
 * 
 * @version
 */
public class TableDisplayMode {
    // Display modes
    public static final int EVERY_YEAR = 0;

    public static final int EVERY_SECOND_YEAR = 1;

    public static final int FIRST_FIVE_YEARS_EVERY_SECOND_AFTER = 2;

    public static final int FIRST_FIVE_YEARS_EVERY_FIFTH_AFTER = 3;

    public static String[] modes = { "Every year", "Every second year",
            "First five years and every second year after",
            "First five years and every fifth year after" };

    /** Creates new TableDisplayMode */
    public TableDisplayMode() {
    }

    public static String[] getDisplayModes() {
        return modes;
    }

    public static int toAnnualIndex(int mode, int index) {

        switch (mode) {
        case EVERY_YEAR:
            return index;
        case EVERY_SECOND_YEAR:
            return index * 2;
        case FIRST_FIVE_YEARS_EVERY_SECOND_AFTER:
            if (index < 5)
                return index;
            return 6 + (index - 5) * 2;
        case FIRST_FIVE_YEARS_EVERY_FIFTH_AFTER:
            if (index < 5)
                return index;
            return 9 + (index - 5) * 5;
        default:
            return -1;
        }

    }

    public static int toModeIndex(int mode, int index) {

        switch (mode) {
        case EVERY_YEAR:
            return index;
        case EVERY_SECOND_YEAR:
            return index / 2;
        case FIRST_FIVE_YEARS_EVERY_SECOND_AFTER:
            if (index < 5)
                return index;
            return 5 + (index - 5) / 2;
        case FIRST_FIVE_YEARS_EVERY_FIFTH_AFTER:
            if (index < 5)
                return index;
            return 5 + (index - 5) / 5;
        default:
            return -1;
        }

    }

    public static int[] getDisplayFrequency(int mode, int yearStart, int yearEnd) {

        if (yearStart == yearEnd)
            return new int[] { yearStart };

        if (yearStart > yearEnd)
            return new int[0];

        int length;
        switch (mode) {
        case EVERY_YEAR:
            length = yearEnd - yearStart + 1;
            break;
        case EVERY_SECOND_YEAR:
            length = (yearEnd - yearStart) / 2 + 1;
            break;
        case FIRST_FIVE_YEARS_EVERY_SECOND_AFTER:
            // Calculate size of array : 5 years or less plus every second year
            length = yearEnd - yearStart > 5 ? 5 + ((yearEnd - yearStart - 5) / 2 + 1)
                    : yearEnd - yearStart + 1;
            break;
        case FIRST_FIVE_YEARS_EVERY_FIFTH_AFTER:
            // Calculate size of array : 5 years or less plus every fifth year
            length = yearEnd - yearStart > 5 ? 5 + ((yearEnd - yearStart - 5) / 5 + 1)
                    : yearEnd - yearStart + 1;
            break;
        default:
            return new int[0];
        }

        int[] frequency = new int[length];
        for (int i = 0; i < length; i++)
            frequency[i] = toAnnualIndex(mode, i) + yearStart;

        // adjust last year
        if (frequency[length - 1] > yearEnd)
            frequency[length - 1] = yearEnd;

        return frequency;

    }

}
