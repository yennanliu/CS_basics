package LeetCodeJava.Math;

// https://leetcode.com/problems/split-message-based-on-limit/description/

/**
 * 2468. Split Message Based on Limit
 * Hard
 *
 * You are given a string, message, and a positive integer, limit.
 *
 * You must split message into one or more parts based on limit. Each resulting part should have the suffix "<a/b>", where "b" is to be replaced with the total number of parts and "a" is to be replaced with the index of the part, starting from 1 and going up to b. Additionally, the length of each resulting part (including its suffix) should be equal to limit, except for the last part whose length can be at most limit.
 *
 * The resulting parts should be formed such that when their suffixes are removed and they are all concatenated in order, they should be equal to message. Also, the result should contain as few parts as possible.
 *
 * Return the parts message would be split into as an array of strings. If it is impossible to split message as required, return an empty array.
 *
 *
 * Example 1:
 *
 * Input: message = "this is really a very awesome message", limit = 9
 * Output: ["thi<1/14>","s i<2/14>","s r<3/14>","eal<4/14>","ly <5/14>","a v<6/14>","ery<7/14>"," aw<8/14>","eso<9/14>","me<10/14>"," m<11/14>","es<12/14>","sa<13/14>","ge<14/14>"]
 * Explanation:
 * The first 9 parts take 3 characters each from the beginning of message.
 * The next 5 parts take 2 characters each from the remaining message.
 * In this example, each part, including the last, has length 9.
 * It can be shown it is not possible to split message into less than 14 parts.
 *
 * Example 2:
 *
 * Input: message = "short message", limit = 15
 * Output: ["short mess<1/2>","age<2/2>"]
 * Explanation:
 * Under the given constraints, the string can be split into two parts:
 * - The first part comprises of the first 10 characters, and has a length 15.
 * - The next part comprises of the last 3 characters, and has a length 8.
 *
 *
 * Constraints:
 *
 * 1 <= message.length <= 10^4
 * message consists of only lowercase English letters and ' '.
 * 1 <= limit <= 10^4
 *
 */
public class SplitMessageBasedOnLimit {

    // V0
    // IDEA: TRY EACH PART COUNT k IN INCREASING ORDER AND CHECK THE TOTAL CAPACITY
    //   part i carries the suffix "<i/k>" of length digits(i) + digits(k) + 3, so the
    //   total payload room over k parts is k * (limit - 3 - digits(k)) - sum of digits(i), i = 1..k.
    //   the first k whose room covers the message gives the fewest parts.
    /**
     * time = O(n log n)
     * space = O(n)
     */
    public String[] splitMessage(String message, int limit) {
        int n = message.length();

        for (int parts = 1; parts <= n; parts++) {
            int partsWidth = digitCount(parts);
            // the widest suffix belongs to the last part; it must still leave room for a character
            if (limit - 3 - partsWidth - partsWidth <= 0) {
                continue;
            }
            long capacity = (long) parts * (limit - 3 - partsWidth) - indexWidthTotal(parts);
            if (capacity < n) {
                continue;
            }

            // build the parts: each takes as many characters as its suffix leaves room for
            String[] result = new String[parts];
            int position = 0;
            for (int i = 1; i <= parts; i++) {
                int room = limit - 3 - partsWidth - digitCount(i);
                int end = Math.min(n, position + room);
                result[i - 1] = message.substring(position, end) + "<" + i + "/" + parts + ">";
                position = end;
            }
            return result;
        }
        return new String[0];
    }

    /** Number of decimal digits in x (x >= 1). */
    private int digitCount(int x) {
        int count = 0;
        while (x > 0) {
            count++;
            x /= 10;
        }
        return count;
    }

    /** Sum of digitCount(i) for i = 1..k, grouped by digit length so it costs O(log k). */
    private long indexWidthTotal(int k) {
        long total = 0;
        int width = 1;
        long start = 1;
        while (start <= k) {
            // all indices in [start, end] have the same number of digits
            long end = Math.min(k, start * 10 - 1);
            total += (end - start + 1) * width;
            width++;
            start *= 10;
        }
        return total;
    }
}
