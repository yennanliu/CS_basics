package LeetCodeJava.TwoPointer;

// https://leetcode.ca/2018-10-20-1055-Shortest-Way-to-Form-String/
// https://leetcode.com/problems/shortest-way-to-form-string/description/

/**
 *  1055. Shortest Way to Form String
 * Description
 * A subsequence of a string is a new string that is formed from the original string by deleting some (can be none) of the characters without disturbing the relative positions of the remaining characters. (i.e., "ace" is a subsequence of "abcde" while "aec" is not).
 *
 * Given two strings source and target, return the minimum number of subsequences of source such that their concatenation equals target. If the task is impossible, return -1.
 *
 *
 *
 * Example 1:
 *
 * Input: source = "abc", target = "abcbc"
 * Output: 2
 * Explanation: The target "abcbc" can be formed by "abc" and "bc", which are subsequences of source "abc".
 * Example 2:
 *
 * Input: source = "abc", target = "acdbc"
 * Output: -1
 * Explanation: The target string cannot be constructed from the subsequences of source string due to the character "d" in target string.
 * Example 3:
 *
 * Input: source = "xyz", target = "xzyxz"
 * Output: 3
 * Explanation: The target string can be constructed as follows "xz" + "y" + "xz".
 *
 *
 * Constraints:
 *
 * 1 <= source.length, target.length <= 1000
 * source and target consist of lowercase English letters.
 *
 * 
 */
public class ShortestWayToFormString {

    // V0
    // IDEA : 2 POINTER
    // TODO : implement
    // TODO : validate below (modified by gpt)
//    public int shortestWay(String source, String target) {
//        int sourceLength = source.length();
//        int targetLength = target.length();
//
//        int j = 0; // pointer for target
//        int cnt = 0; // count of subsequences used
//
//        // Loop over the target string
//        while (j < targetLength) {
//            int startJ = j; // Track the initial position of j for this subsequence
//            // Loop through the source string and try to match the target
//            for (int i = 0; i < sourceLength; i++) {
//                // Check if the current character matches
//                if (j < targetLength && source.charAt(i) == target.charAt(j)) {
//                    j++;
//                }
//            }
//
//            // If no progress is made (i.e., no characters from target are matched)
//            if (startJ == j) {
//                return -1; // It's impossible to form target using subsequences of source
//            }
//
//            cnt++; // We've used one more subsequence of source
//        }
//
//        return cnt;
//    }


    // V1_1
    // IDEA : 2 POINTER (gpt)
    // TODO : validate below
    public int shortestWay_1(String source, String target) {
        // Step 1: Check if every character in target exists in source
        for (char x : target.toCharArray()) {
            if (source.indexOf(x) == -1) {
                return -1;
            }
        }

        int sourceLen = source.length();
        int targetLen = target.length();
        int sourceIdx = 0;
        int targetIdx = 0;
        int res = 0;

        // Step 2: Iterate through the target string
        while (targetIdx < targetLen) {
            int currentIdx = targetIdx;

            // Step 3: Match as many characters as possible from source with target
            /** NOTE !!! below logic
             *
             *   while src idx < src len && target idx < target len.
             *   keep comparing src and target val
             */
            while (sourceIdx < sourceLen && targetIdx < targetLen) {
                if (source.charAt(sourceIdx) == target.charAt(targetIdx)) {
                    targetIdx++;
                }
                sourceIdx++;
            }

            // Step 4: If no progress was made in this pass, it means the target cannot be formed
            if (targetIdx == currentIdx) {
                return -1;
            }

            // Step 5: Reset source index and increment the subsequence count
            /** NOTE !!! reset src idx after src idx reach src len */
            sourceIdx = 0;
            res++;
        }

        return res;
    }


    // V2_1
    // https://leetcode.ca/2018-10-20-1055-Shortest-Way-to-Form-String/
    // IDEA : 2 POINTER
    public int shortestWay_2_1(String source, String target) {
        int m = source.length(), n = target.length();
        int ans = 0, j = 0;
        while (j < n) {
            int i = 0;
            boolean ok = false;
            while (i < m && j < n) {
                if (source.charAt(i) == target.charAt(j)) {
                    ok = true;
                    ++j;
                }
                ++i;
            }
            if (!ok) {
                return -1;
            }
            ++ans;
        }
        return ans;
    }

    // V2

}
