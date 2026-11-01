package LeetCodeJava.Array;

// https://leetcode.com/problems/guess-the-majority-in-a-hidden-array/description/

/**
 * 1538. Guess the Majority in a Hidden Array
 * Medium
 *
 * We have an integer array nums, where all the integers in nums are 0 or 1. You will not be
 * given direct access to the array, instead, you will have an API ArrayReader which have
 * the following functions:
 *
 * int query(int a, int b, int c, int d): where 0 <= a < b < c < d < ArrayReader.length().
 * The function returns the distribution of the value of the 4 elements and returns:
 *   4 : if the values of the 4 elements are the same (0 or 1).
 *   2 : if three elements have a value equal to 0 and one element has value equal to 1 or
 *       vice versa.
 *   0 : if two element have a value equal to 0 and two elements have a value equal to 1.
 * int length(): Returns the size of the array.
 *
 * You are allowed to call query() 2 * n times at most where n is equal to
 * ArrayReader.length().
 *
 * Return any index of the most frequent value in nums, in case of tie, return -1.
 *
 *
 * Example 1:
 *
 * Input: nums = [0,0,1,0,1,1,1,1]
 * Output: 5
 * Explanation: reader.query(0,1,2,3) returns 2, reader.query(4,5,6,7) returns 4, so the
 * most frequent value is found in the last 4 elements. Index 2, 4, 6, 7 is also a correct
 * answer.
 *
 * Example 2:
 *
 * Input: nums = [0,0,1,1,0]
 * Output: 0
 *
 * Example 3:
 *
 * Input: nums = [1,0,1,0,1,0,1,0]
 * Output: -1
 *
 *
 * Constraints:
 *
 * 5 <= nums.length <= 10^5
 * 0 <= nums[i] <= 1
 *
 *
 * Follow up: What is the minimum number of calls needed to find the majority element?
 *
 */
public class GuessTheMajorityInAHiddenArray {

    /** This is the ArrayReader's API interface (provided by LC, not implemented here). */
    public interface ArrayReader {
        /** Compares 4 different elements; returns 4, 2 or 0 as described above. */
        int query(int a, int b, int c, int d);

        /** Returns the length of the array. */
        int length();
    }

    // V0
    // IDEA: COMPARE EVERY INDEX AGAINST A FIXED REFERENCE INDEX (index 3)
    //       With three query slots held fixed, the answer differs for a 0 vs a 1 in the
    //       free slot, so query(a,b,c,i) == query(a,b,c,j) <=> nums[i] == nums[j].
    //       - indices >= 4: compare against index 3 using the fixed trio (0, 1, 2)
    //       - indices 0, 1, 2: swap each one against index 3, holding the other two of
    //         {0, 1, 2} plus index 4 fixed
    //       Total calls = (n - 4) + 5 = n + 1, inside the 2n budget.
    /**
     * time = O(N)   (query calls)
     * space = O(1)
     */
    public int guessMajority(ArrayReader reader) {
        int n = reader.length();

        int sameAsRef = 1;          // index 3 itself matches nums[3]
        int differentFromRef = 0;
        int differentIndex = -1;    // any one index whose value differs from nums[3]

        // indices 4 .. n-1, each compared against index 3 through the trio (0, 1, 2)
        int referenceAnswer = reader.query(0, 1, 2, 3);
        for (int i = 4; i < n; i++) {
            if (reader.query(0, 1, 2, i) == referenceAnswer) {
                sameAsRef++;
            } else {
                differentFromRef++;
                differentIndex = i;
            }
        }

        // indices 0, 1, 2: base quadruple is {0, 1, 2, 4}; replacing index j with 3
        // keeps the other three slots fixed, so equal answers <=> nums[j] == nums[3]
        int baseAnswer = reader.query(0, 1, 2, 4);
        int[][] swapped = {
                {1, 2, 3, 4},   // index 0 replaced by 3
                {0, 2, 3, 4},   // index 1 replaced by 3
                {0, 1, 3, 4}    // index 2 replaced by 3
        };
        for (int j = 0; j < 3; j++) {
            int[] quad = swapped[j];
            if (reader.query(quad[0], quad[1], quad[2], quad[3]) == baseAnswer) {
                sameAsRef++;
            } else {
                differentFromRef++;
                differentIndex = j;
            }
        }

        if (sameAsRef == differentFromRef) {
            return -1;
        }
        if (sameAsRef > differentFromRef) {
            return 3;
        }
        return differentIndex;
    }
}
