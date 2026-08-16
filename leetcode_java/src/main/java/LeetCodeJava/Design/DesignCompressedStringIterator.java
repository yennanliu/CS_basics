package LeetCodeJava.Design;

// https://leetcode.com/problems/design-compressed-string-iterator/description/

import java.util.ArrayList;
import java.util.List;

/**
 * 604. Design Compressed String Iterator
 * Easy
 * Lock: Prime
 *
 * Design and implement a data structure for a compressed string iterator.
 * The given compressed string will be in the form of each letter followed by a positive
 * integer representing the number of this letter existing in the original uncompressed
 * string.
 *
 * Implement the StringIterator class:
 *
 * - next() Returns the next character if the original string still has uncompressed
 *   characters, otherwise returns a white space.
 * - hasNext() Returns true if there is any letter needs to be uncompressed in the
 *   original string, otherwise returns false.
 *
 * Example 1:
 *
 * Input
 * ["StringIterator", "next", "next", "next", "next", "next", "next", "hasNext", "next",
 *  "hasNext"]
 * [["L1e2t1C1o1d1e1"], [], [], [], [], [], [], [], [], []]
 * Output
 * [null, "L", "e", "e", "t", "C", "o", true, "d", true]
 *
 * Explanation
 * StringIterator stringIterator = new StringIterator("L1e2t1C1o1d1e1");
 * stringIterator.next();    // return "L"
 * stringIterator.next();    // return "e"
 * stringIterator.next();    // return "e"
 * stringIterator.next();    // return "t"
 * stringIterator.next();    // return "C"
 * stringIterator.next();    // return "o"
 * stringIterator.hasNext(); // return True
 * stringIterator.next();    // return "d"
 * stringIterator.hasNext(); // return True
 *
 * Constraints:
 *
 * 1 <= compressedString.length <= 1000
 * compressedString consists of lower-case an upper-case English letters and digits.
 * The number of a single character repetitions in compressedString is in the range
 * [1, 10^9]
 * At most 100 calls will be made to next and hasNext.
 *
 */
public class DesignCompressedStringIterator {

    /**
     * Your StringIterator object will be instantiated and called as such:
     * StringIterator obj = new StringIterator(compressedString);
     * char param_1 = obj.next();
     * boolean param_2 = obj.hasNext();
     */

    // V0
    // IDEA: PARSE ONCE INTO (char, count) PAIRS + POINTER
    /**
     *   NEVER expand the string -- a count can be up to 10^9.
     *   Keep a pointer `p` to the current (char, count) pair and DECREMENT the
     *   count LAZILY on each next() call.
     *
     *   NOTE !!! the digit run must be parsed as a whole (`e12` is 12, not 1 then 2),
     *            and the count reaches 10^9 so it must not be a `char`/`short`.
     *
     *   time  = O(n) for the constructor (n = compressedString.length),
     *           O(1) for next / hasNext
     *   space = O(n)
     */
    class StringIterator {

        private List<char[]> chars;  // the letter of each group
        private List<Integer> count; // remaining count of each group
        private int p;

        public StringIterator(String compressedString) {
            this.chars = new ArrayList<>();
            this.count = new ArrayList<>();
            this.p = 0;

            int n = compressedString.length();
            int i = 0;
            while (i < n) {
                char c = compressedString.charAt(i);
                i += 1;

                // collect the WHOLE digit run following the letter
                int j = i;
                while (j < n && Character.isDigit(compressedString.charAt(j))) {
                    j += 1;
                }

                chars.add(new char[] { c });
                count.add(Integer.parseInt(compressedString.substring(i, j)));
                i = j;
            }
        }

        public char next() {
            if (!hasNext()) {
                return ' ';
            }
            char c = chars.get(p)[0];
            count.set(p, count.get(p) - 1);

            // current group EXHAUSTED -> move to the next group
            if (count.get(p) == 0) {
                p += 1;
            }
            return c;
        }

        public boolean hasNext() {
            return p < chars.size();
        }
    }

}
