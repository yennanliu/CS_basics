package LeetCodeJava.Sort;

// https://leetcode.com/problems/reorder-data-in-log-files/description/

import java.util.Arrays;

/**
 * 937. Reorder Data in Log Files
 * Easy
 *
 * You are given an array of logs. Each log is a space-delimited string of words,
 * where the first word is the identifier.
 *
 * There are two types of logs:
 *
 * Letter-logs: All words (except the identifier) consist of lowercase English letters.
 * Digit-logs: All words (except the identifier) consist of digits.
 *
 * Reorder these logs so that:
 *
 * 1. The letter-logs come before all digit-logs.
 * 2. The letter-logs are sorted lexicographically by their contents. If their contents
 *    are the same, then sort them lexicographically by their identifiers.
 * 3. The digit-logs maintain their relative ordering.
 *
 * Return the final order of the logs.
 *
 *
 * Example 1:
 *
 * Input: logs = ["dig1 8 1 5 1","let1 art can","dig2 3 6","let2 own kit dig",
 *                "let3 art zero"]
 * Output: ["let1 art can","let3 art zero","let2 own kit dig","dig1 8 1 5 1","dig2 3 6"]
 * Explanation:
 * The letter-log contents are all different, so their ordering is "art can",
 * "art zero", "own kit dig".
 * The digit-logs have a relative order of "dig1 8 1 5 1", "dig2 3 6".
 *
 * Example 2:
 *
 * Input: logs = ["a1 9 2 3 1","g1 act car","zo4 4 7","ab1 off key dog","a8 act zoo"]
 * Output: ["g1 act car","a8 act zoo","ab1 off key dog","a1 9 2 3 1","zo4 4 7"]
 *
 *
 * Constraints:
 *
 * 1 <= logs.length <= 100
 * 3 <= logs[i].length <= 100
 * All the tokens of logs[i] are separated by a single space.
 * logs[i] is guaranteed to have an identifier and at least one word after the identifier.
 *
 */
public class ReorderDataInLogFiles {

    // V0
    // IDEA: CUSTOM COMPARATOR + STABLE SORT
    /**
     *  Compare logs as if each had the key:
     *
     *     letter-log -> (0, content, identifier)  // letters first, then content, then id
     *     digit-log  -> (1, "", "")               // all digit-logs compare EQUAL
     *
     *  NOTE !!! `Arrays.sort` on OBJECTS is STABLE in java (TimSort), so all digit-logs
     *           (which compare equal) keep their ORIGINAL relative order for free.
     *           This would NOT hold for the primitive `Arrays.sort` overloads.
     *
     *  time  = O(n * L * log(n)), L = max log length
     *  space = O(n * L)
     */
    public String[] reorderLogFiles(String[] logs) {
        String[] res = logs.clone();

        Arrays.sort(res, (a, b) -> {
            // split into identifier + content, on the FIRST space only
            int ia = a.indexOf(' ');
            int ib = b.indexOf(' ');
            String idA = a.substring(0, ia);
            String idB = b.substring(0, ib);
            String contentA = a.substring(ia + 1);
            String contentB = b.substring(ib + 1);

            /** NOTE !!!
             *
             *  only the FIRST char of the content needs testing:
             *  a log is either all-letters or all-digits after the identifier
             */
            boolean letterA = Character.isLetter(contentA.charAt(0));
            boolean letterB = Character.isLetter(contentB.charAt(0));

            if (letterA && letterB) {
                int cmp = contentA.compareTo(contentB);
                return cmp != 0 ? cmp : idA.compareTo(idB);
            }
            if (letterA) {
                return -1; // letter-logs come FIRST
            }
            if (letterB) {
                return 1;
            }
            return 0; // both digit-logs -> equal -> stable sort keeps their order
        });

        return res;
    }

}
