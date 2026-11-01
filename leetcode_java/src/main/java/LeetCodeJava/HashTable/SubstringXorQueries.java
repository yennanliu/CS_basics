package LeetCodeJava.HashTable;

// https://leetcode.com/problems/substring-xor-queries/description/

import java.util.*;

/**
 * 2564. Substring XOR Queries
 * Medium
 *
 * You are given a binary string s, and a 2D integer array queries where queries[i] = [firsti, secondi].
 *
 * For the ith query, find the shortest substring of s whose decimal value, val, yields secondi when bitwise XORed with firsti. In other words, val ^ firsti == secondi.
 *
 * The answer to the ith query is the endpoints (0-indexed) of the substring [lefti, righti] or [-1, -1] if no such substring exists. If there are multiple answers, choose the one with the minimum lefti.
 *
 * Return an array ans where ans[i] = [lefti, righti] is the answer to the ith query.
 *
 * A substring is a contiguous non-empty sequence of characters within a string.
 *
 *
 * Example 1:
 *
 * Input: s = "101101", queries = [[0,5],[1,2]]
 * Output: [[0,2],[2,3]]
 * Explanation: For the first query the substring in range [0,2] is "101" which has a decimal value of 5, and 5 ^ 0 = 5, hence the answer to the first query is [0,2]. In the second query, the substring in range [2,3] is "11", and has a decimal value of 3, and 3 ^ 1 = 2. So, [2,3] is returned for the second query.
 *
 * Example 2:
 *
 * Input: s = "0101", queries = [[12,8]]
 * Output: [[-1,-1]]
 * Explanation: In this example there is no substring that answers the query, hence [-1,-1] is returned.
 *
 * Example 3:
 *
 * Input: s = "1", queries = [[4,5]]
 * Output: [[0,0]]
 * Explanation: For this example, the substring in range [0,0] has a decimal value of 1, and 1 ^ 4 = 5. So, the answer is [0,0].
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 10^4
 * s[i] is either '0' or '1'.
 * 1 <= queries.length <= 10^5
 * 0 <= firsti, secondi <= 10^9
 *
 */
public class SubstringXorQueries {

    // V0
    // IDEA: XOR IDENTITY (val = first ^ second) + HASH MAP OF FIRST POSITION OF EVERY SHORT SUBSTRING VALUE
    /**
     * time = O(N * 32 + Q)
     * space = O(N * 32)
     */
    public int[][] substringXorQueries(String s, int[][] queries) {
        int n = s.length();
        // value -> [left, right] of its shortest, leftmost occurrence
        Map<Long, int[]> firstPosition = new HashMap<>();

        for (int left = 0; left < n; left++) {
            long value = 0;
            // targets are below 2^30, so a useful substring is at most 32 chars long
            for (int right = left; right < Math.min(n, left + 32); right++) {
                // append the next bit
                value = value * 2 + (s.charAt(right) - '0');
                // scanning left ascending, the first record per value is the leftmost one
                if (!firstPosition.containsKey(value)) {
                    firstPosition.put(value, new int[] {left, right});
                }
                // a substring starting with '0' is only shortest as the single "0"
                if (value == 0) {
                    break;
                }
            }
        }

        int[][] answer = new int[queries.length][];
        for (int i = 0; i < queries.length; i++) {
            // val ^ first == second  <=>  val == first ^ second
            long target = queries[i][0] ^ queries[i][1];
            int[] position = firstPosition.get(target);
            if (position == null) {
                answer[i] = new int[] {-1, -1};
            } else {
                answer[i] = new int[] {position[0], position[1]};
            }
        }
        return answer;
    }
}
