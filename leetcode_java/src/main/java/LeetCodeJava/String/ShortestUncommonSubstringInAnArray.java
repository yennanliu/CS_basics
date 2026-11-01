package LeetCodeJava.String;

// https://leetcode.com/problems/shortest-uncommon-substring-in-an-array/description/

/**
 * 3076. Shortest Uncommon Substring in an Array
 * Medium
 *
 * You are given an array arr of size n consisting of non-empty strings.
 *
 * Find a string array answer of size n such that:
 *
 * answer[i] is the shortest substring of arr[i] that does not occur as a substring in any other string in arr. If multiple such substrings exist, answer[i] should be the lexicographically smallest. And if no such substring exists, answer[i] should be an empty string.
 *
 * Return the array answer.
 *
 *
 * Example 1:
 *
 * Input: arr = ["cab","ad","bad","c"]
 * Output: ["ab","","ba",""]
 * Explanation: We have the following:
 * - For the string "cab", the shortest substring that does not occur in any other string is either "ca" or "ab", we choose the lexicographically smaller substring, which is "ab".
 * - For the string "ad", there is no substring that does not occur in any other string.
 * - For the string "bad", the shortest substring that does not occur in any other string is "ba".
 * - For the string "c", there is no substring that does not occur in any other string.
 *
 * Example 2:
 *
 * Input: arr = ["abc","bcd","abcd"]
 * Output: ["","","abcd"]
 * Explanation: We have the following:
 * - For the string "abc", there is no substring that does not occur in any other string.
 * - For the string "bcd", there is no substring that does not occur in any other string.
 * - For the string "abcd", the shortest substring that does not occur in any other string is "abcd".
 *
 *
 * Constraints:
 *
 * n == arr.length
 * 2 <= n <= 100
 * 1 <= arr[i].length <= 20
 * arr[i] consists only of lowercase English letters.
 *
 */
public class ShortestUncommonSubstringInAnArray {

    // V0
    // IDEA: BRUTE FORCE — ENUMERATE SUBSTRINGS SHORTEST FIRST
    //       n <= 100 strings of length <= 20, so at most 210 substrings each.
    //       For each length from 1 up, keep the lexicographically smallest
    //       substring that occurs in no other string; the first length with a
    //       survivor is the answer.
    /**
     * time = O(N^2 * L^3)
     * space = O(L)
     */
    public String[] shortestSubstrings(String[] arr) {
        int n = arr.length;
        String[] answer = new String[n];

        for (int i = 0; i < n; i++) {
            String current = arr[i];
            String best = "";
            for (int length = 1; length <= current.length() && best.isEmpty(); length++) {
                for (int start = 0; start + length <= current.length(); start++) {
                    String candidate = current.substring(start, start + length);
                    if (!occursElsewhere(arr, i, candidate)
                            && (best.isEmpty() || candidate.compareTo(best) < 0)) {
                        best = candidate;
                    }
                }
            }
            answer[i] = best;
        }
        return answer;
    }

    /** True iff candidate is a substring of some arr[j] with j != skip. */
    private boolean occursElsewhere(String[] arr, int skip, String candidate) {
        for (int j = 0; j < arr.length; j++) {
            if (j != skip && arr[j].contains(candidate)) {
                return true;
            }
        }
        return false;
    }
}
