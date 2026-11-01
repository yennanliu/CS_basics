package LeetCodeJava.String;

// https://leetcode.com/problems/reformat-date/description/

/**
 * 1507. Reformat Date
 * Easy
 *
 * Given a date string in the form Day Month Year, where:
 *
 * Day is in the set {"1st", "2nd", "3rd", "4th", ..., "30th", "31st"}.
 * Month is in the set {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"}.
 * Year is in the range [1900, 2100].
 *
 * Convert the date string to the format YYYY-MM-DD, where:
 *
 * YYYY denotes the 4 digit year.
 * MM denotes the 2 digit month.
 * DD denotes the 2 digit day.
 *
 *
 * Example 1:
 *
 * Input: date = "20th Oct 2052"
 * Output: "2052-10-20"
 *
 * Example 2:
 *
 * Input: date = "6th Jun 1933"
 * Output: "1933-06-06"
 *
 * Example 3:
 *
 * Input: date = "26th May 1960"
 * Output: "1960-05-26"
 *
 *
 * Constraints:
 *
 * The given dates are guaranteed to be valid, so no error handling is necessary.
 *
 */
public class ReformatDate {

    // V0
    // IDEA: STRING PARSING - SPLIT INTO DAY / MONTH / YEAR AND NORMALISE EACH
    //       day   : drop the 2-char ordinal suffix ("st"/"nd"/"rd"/"th"), zero-pad to 2
    //       month : position in the month table, 1..12, zero-pad to 2
    //       year  : already 4 digits
    /**
     * time = O(1)
     * space = O(1)
     */
    public String reformatDate(String date) {
        final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

        String[] parts = date.split(" ");
        int day = Integer.parseInt(parts[0].substring(0, parts[0].length() - 2));

        int month = 0;
        for (int i = 0; i < MONTHS.length; i++) {
            if (MONTHS[i].equals(parts[1])) {
                month = i + 1;
                break;
            }
        }
        String year = parts[2];

        // %02d zero-pads: "6th" must become "06", not "6"
        return String.format("%s-%02d-%02d", year, month, day);
    }
}
