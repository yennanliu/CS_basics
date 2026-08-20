package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/number-of-unique-xor-triplets-i/

/**
 *  3513. Number of Unique XOR Triplets I
 *  Medium
 *
 *  You are given an integer array nums of length n, where nums is a permutation
 *  of the numbers in the range [1, n].
 *
 *  A XOR triplet is defined as the XOR of three elements
 *  nums[i] XOR nums[j] XOR nums[k] where i <= j <= k.
 *
 *  Return the number of unique XOR triplet values from all possible triplets
 *  (i, j, k).
 *
 *  Example 1:
 *    Input: nums = [1,2]
 *    Output: 2
 *    Explanation: the reachable values are {1, 2}.
 *
 *  Example 2:
 *    Input: nums = [3,1,2]
 *    Output: 4
 *    Explanation: the reachable values are {0, 1, 2, 3}.
 *
 *  Constraints:
 *    1 <= n == nums.length <= 10^5
 *    1 <= nums[i] <= n
 *    nums is a permutation of integers from 1 to n.
 */
public class NumberOfUniqueXorTripletsI {

    // V0
    // IDEA: CLOSURE OF {1..n} UNDER XOR IS THE FULL POWER-OF-TWO BLOCK
    //       i <= j <= k allows repeats, so taking i == j cancels a pair and leaves
    //       just nums[k] -> every element 1..n is reachable on its own.
    //       for n >= 3 the three-distinct case fills in everything else: {1..n}
    //       spans all bit positions below 2^b with b = bitLength(n), and xoring
    //       triples of such numbers generates that whole block, 0 included
    //       (e.g. 1 ^ 2 ^ 3) -> the answer is 2^b.
    //       n = 1 / n = 2 are special: not enough distinct elements to form a
    //       genuine triple, so only the n singletons are reachable.
    /**
     * time = O(1)   // after reading n
     * space = O(1)
     */
    public int uniqueXorTriplets(int[] nums) {
        int n = nums.length;
        if (n < 3) {
            return n;
        }
        int bitLength = 32 - Integer.numberOfLeadingZeros(n);
        return 1 << bitLength;
    }
}
