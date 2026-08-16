package LeetCodeJava.Design;

// https://leetcode.com/problems/design-compressed-string-iterator/description/

import java.util.ArrayDeque;
import java.util.Deque;
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


    // V1
    // IDEA: LAZY PARSING -- decode straight off the compressed string
    /**
     *  V0 parses everything in the constructor. Here the constructor is O(1): we
     *  keep an index into `compressedString` and decode the next (letter, count)
     *  group only when it is needed.
     *
     *  Matters when the iterator is often abandoned after a few next() calls, or
     *  when the compressed string is huge.
     *
     *  time  = O(1) constructor, O(1) amortised next / hasNext
     *  space = O(1)
     */
    class StringIterator_1 {

        private String src;
        private int pos;      // index of the next group's LETTER
        private char cur;
        private int remaining;

        public StringIterator_1(String compressedString) {
            this.src = compressedString;
            this.pos = 0;
            this.remaining = 0;
        }

        public char next() {
            if (!hasNext()) {
                return ' ';
            }
            remaining -= 1;
            return cur;
        }

        public boolean hasNext() {
            if (remaining > 0) {
                return true;
            }
            if (pos >= src.length()) {
                return false;
            }
            // decode exactly one group, right now
            cur = src.charAt(pos++);
            int start = pos;
            while (pos < src.length() && Character.isDigit(src.charAt(pos))) {
                pos += 1;
            }
            remaining = Integer.parseInt(src.substring(start, pos));
            return remaining > 0;
        }
    }

    // V2
    // IDEA: PREFIX COUNTS + BINARY SEARCH (random access, not just sequential)
    /**
     *  Store the cumulative character count per group. A pointer `idx` then names
     *  the position in the UNCOMPRESSED string, and the character at any position
     *  is found by binary searching the prefix array.
     *
     *  -> the iterator gains a `charAt(i)` capability that the sequential versions
     *     cannot offer, at the price of O(log g) per lookup.
     *
     *  NOTE !!! the counts reach 10^9 each, so the prefix array must be `long`.
     *
     *  time  = O(n) constructor, O(log g) per next
     *  space = O(g), g = number of groups
     */
    class StringIterator_2 {

        private char[] letters;
        private long[] prefix; // prefix[i] = total chars through group i-1
        private long idx;      // position in the uncompressed string

        public StringIterator_2(String compressedString) {
            List<Character> ls = new ArrayList<>();
            List<Long> cs = new ArrayList<>();

            int i = 0;
            int n = compressedString.length();
            while (i < n) {
                char c = compressedString.charAt(i++);
                int start = i;
                while (i < n && Character.isDigit(compressedString.charAt(i))) {
                    i += 1;
                }
                ls.add(c);
                cs.add(Long.parseLong(compressedString.substring(start, i)));
            }

            this.letters = new char[ls.size()];
            this.prefix = new long[ls.size() + 1];
            for (int t = 0; t < ls.size(); t++) {
                letters[t] = ls.get(t);
                prefix[t + 1] = prefix[t] + cs.get(t);
            }
            this.idx = 0;
        }

        public char next() {
            if (!hasNext()) {
                return ' ';
            }
            return charAt(idx++);
        }

        public boolean hasNext() {
            return idx < prefix[prefix.length - 1];
        }

        /** the character at uncompressed position p */
        private char charAt(long p) {
            int lo = 0;
            int hi = letters.length - 1;
            while (lo < hi) {
                int mid = lo + (hi - lo) / 2;
                if (prefix[mid + 1] <= p) {
                    lo = mid + 1;
                } else {
                    hi = mid;
                }
            }
            return letters[lo];
        }
    }

    // V3
    // IDEA: QUEUE OF (letter, count) GROUPS
    /**
     *  Keep the groups in a Queue and simply poll the head once its count hits
     *  zero.
     *
     *  There is no index to maintain at all -- `hasNext` is `queue is not empty`,
     *  which removes the whole class of pointer bugs the array versions can have.
     *
     *  time  = O(n) constructor, O(1) next / hasNext
     *  space = O(g)
     */
    class StringIterator_3 {

        private Deque<Object[]> groups; // {Character letter, Integer remaining}

        public StringIterator_3(String compressedString) {
            this.groups = new ArrayDeque<>();
            int i = 0;
            int n = compressedString.length();
            while (i < n) {
                char c = compressedString.charAt(i++);
                int start = i;
                while (i < n && Character.isDigit(compressedString.charAt(i))) {
                    i += 1;
                }
                groups.offer(new Object[] {
                        c, Integer.parseInt(compressedString.substring(start, i)) });
            }
        }

        public char next() {
            if (!hasNext()) {
                return ' ';
            }
            Object[] head = groups.peek();
            char c = (Character) head[0];
            int left = (Integer) head[1] - 1;
            if (left == 0) {
                groups.poll();
            } else {
                head[1] = left;
            }
            return c;
        }

        public boolean hasNext() {
            return !groups.isEmpty();
        }
    }

}
