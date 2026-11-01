package LeetCodeJava.Array;

// https://leetcode.com/problems/plates-between-candles/description/

/**
 * 2055. Plates Between Candles
 * Medium
 *
 * There is a long table with a line of plates and candles arranged on top of it. You are given a 0-indexed string s consisting of characters '*' and '|' only, where a '*' represents a plate and a '|' represents a candle.
 *
 * You are also given a 0-indexed 2D integer array queries where queries[i] = [left_i, right_i] denotes the substring s[left_i...right_i] (inclusive). For each query, you need to find the number of plates between candles that are in the substring. A plate is considered between candles if there is at least one candle to its left and at least one candle to its right in the substring.
 *
 * For example, s = "||**||**|*", and a query [3, 8] denotes the substring "*||**|". The number of plates between candles in this substring is 2, as each of the two plates has at least one candle in the substring to its left and right.
 *
 * Return an integer array answer where answer[i] is the answer to the ith query.
 *
 *
 * Example 1:
 *
 * Input: s = "**|**|***|", queries = [[2,5],[5,9]]
 * Output: [2,3]
 * Explanation:
 * - queries[0] has two plates between candles.
 * - queries[1] has three plates between candles.
 *
 * Example 2:
 *
 * Input: s = "***|**|*****|**||**|*", queries = [[1,17],[4,5],[14,17],[5,11],[15,16]]
 * Output: [9,0,0,0,0]
 * Explanation:
 * - queries[0] has nine plates between candles.
 * - The other queries have zero plates between candles.
 *
 *
 * Constraints:
 *
 * 3 <= s.length <= 10^5
 * s consists of '*' and '|' characters.
 * 1 <= queries.length <= 10^5
 * queries[i].length == 2
 * 0 <= left_i <= right_i < s.length
 *
 */
public class PlatesBetweenCandles {

    // V0
    // IDEA: PREFIX SUM OF PLATES + NEAREST-CANDLE ARRAYS (O(1) PER QUERY)
    /**
     *  platesBefore[i] = number of '*' in s[0 .. i-1]
     *  nextCandle[i]   = index of the nearest candle at or AFTER i  (-1 if none)
     *  prevCandle[i]   = index of the nearest candle at or BEFORE i (-1 if none)
     *
     *  For a query [l, r] only the plates between the first candle inside
     *  (nextCandle[l]) and the last candle inside (prevCandle[r]) count.
     *
     * time = O(N + Q)
     * space = O(N)
     */
    public int[] platesBetweenCandles(String s, int[][] queries) {
        int n = s.length();

        int[] platesBefore = new int[n + 1];
        for (int i = 0; i < n; i++) {
            int isPlate = s.charAt(i) == '*' ? 1 : 0;
            platesBefore[i + 1] = platesBefore[i] + isPlate;
        }

        int[] prevCandle = new int[n];
        int lastCandle = -1;
        for (int i = 0; i < n; i++) {
            if (s.charAt(i) == '|') {
                lastCandle = i;
            }
            prevCandle[i] = lastCandle;
        }

        int[] nextCandle = new int[n];
        lastCandle = -1;
        for (int i = n - 1; i >= 0; i--) {
            if (s.charAt(i) == '|') {
                lastCandle = i;
            }
            nextCandle[i] = lastCandle;
        }

        int[] answer = new int[queries.length];
        for (int q = 0; q < queries.length; q++) {
            int leftCandle = nextCandle[queries[q][0]];
            int rightCandle = prevCandle[queries[q][1]];
            // both candles must exist inside the range and be in order
            if (leftCandle != -1 && rightCandle != -1 && leftCandle < rightCandle) {
                // plates in (leftCandle, rightCandle)
                answer[q] = platesBefore[rightCandle] - platesBefore[leftCandle + 1];
            }
        }
        return answer;
    }
}
