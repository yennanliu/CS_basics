package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/swap-adjacent-in-lr-string/

/**
 *  777. Swap Adjacent in LR String
 *  Medium
 *
 *  In a string composed of 'L', 'R', and 'X' characters, like "RXXLRXRXL", a
 *  move consists of either replacing one occurrence of "XL" with "LX", or
 *  replacing one occurrence of "RX" with "XR".
 *
 *  Given the starting string start and the ending string result, return True if
 *  there exists a sequence of moves to transform start to result.
 *
 *  Example 1:
 *    Input: start = "RXXLRXRXL", result = "XRLXXRRLX"
 *    Output: true
 *
 *  Example 2:
 *    Input: start = "X", result = "L"
 *    Output: false
 *
 *  Constraints:
 *    1 <= start.length <= 10^4
 *    start.length == result.length
 *    Both start and result will only consist of characters in 'L', 'R', and 'X'.
 */
public class SwapAdjacentInLRString {

    // V0
    // IDEA: 2 POINTERS - ignore 'X' and compare the L/R "skeletons"
    //       key invariants:
    //         - removing all 'X', the two strings must be identical
    //         - 'L' can only move LEFT  -> its index in start must be >= index in end
    //         - 'R' can only move RIGHT -> its index in start must be <= index in end
    /**
     * time = O(N)
     * space = O(1)
     */
    public boolean canTransform(String start, String end) {
        if (start == null || end == null || start.length() != end.length()) {
            return false;
        }

        int n = start.length();
        int i = 0;
        int j = 0;

        while (i < n || j < n) {
            // skip 'X'
            while (i < n && start.charAt(i) == 'X') {
                i++;
            }
            while (j < n && end.charAt(j) == 'X') {
                j++;
            }

            // one ran out of non-X chars : both must be exhausted
            if (i == n || j == n) {
                return i == n && j == n;
            }

            // the L/R skeleton must match
            if (start.charAt(i) != end.charAt(j)) {
                return false;
            }

            char c = start.charAt(i);
            if (c == 'L' && i < j) {
                return false;   // 'L' cannot move right
            }
            if (c == 'R' && i > j) {
                return false;   // 'R' cannot move left
            }

            i++;
            j++;
        }
        return true;
    }
}
