package LeetCodeJava.HashTable;

// https://leetcode.com/problems/maximum-equal-adjacent-pairs-after-at-most-one-replacement/description/

import java.util.*;

/**
 * 4066. Maximum Equal Adjacent Pairs After at Most One Replacement
 * Medium
 *
 * You are given a 1-indexed integer array nums.
 *
 * You can choose two distinct values x and y and perform the following
 * operation at most once:
 *
 *     Replace every occurrence of x in nums with y.
 *
 * Return the maximum possible number of pairs of adjacent elements that
 * are equal after performing the operation.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,3,2]
 * Output: 2
 * Explanation:
 * One optimal solution is to choose x = 3 and y = 2.
 * The resulting array is [1, 2, 2, 2].
 * There are 2 pairs of adjacent elements that are equal:
 * (nums[2], nums[3]) and (nums[3], nums[4]).
 * Therefore, the answer is 2.
 *
 * Example 2:
 *
 * Input: nums = [1,2,1,2,1]
 * Output: 4
 * Explanation:
 * One optimal solution is to choose x = 1 and y = 2.
 * The resulting array is [2, 2, 2, 2, 2].
 * There are 4 pairs of adjacent elements that are equal:
 * (nums[1], nums[2]), (nums[2], nums[3]), (nums[3], nums[4]), and (nums[4], nums[5]).
 * Therefore, the answer is 4.
 *
 * Example 3:
 *
 * Input: nums = [1,1,1]
 * Output: 2
 * Explanation:
 * One optimal solution is to perform no operation.
 * Thus, the resulting array is [1, 1, 1].
 * There are 2 pairs of adjacent elements that are equal:
 * (nums[1], nums[2]) and (nums[2], nums[3]).
 * Therefore, the answer is 2.
 *
 *
 * Constraints:
 *
 * 2 <= nums.length <= 10^5
 * 1 <= nums[i] <= 10^9
 *
 */
public class MaximumEqualAdjacentPairsAfterAtMostOneReplacement {

    // V0
    // IDEA: HASH MAP {(min, max) : how many times x and y sit next to each other}
    //
    //   what does ONE replacement "x -> y" change?
    //     - a pair (x, y) or (y, x)   -> becomes (y, y) : +1 equal pair
    //     - a pair (x, x)             -> becomes (y, y) : was equal, still equal
    //     - a pair (x, a), a != y     -> becomes (y, a) : was unequal, still unequal
    //     - anything else             -> untouched
    //
    //   so NO equal pair is ever lost, and the gain is EXACTLY the number of
    //   adjacent positions holding one x and one y:
    //
    //     answer = (pairs already equal) + max over {x, y} of (adjacent {x, y} pairs)
    //
    //   brute force (try every (x, y), replace, re-count) is O(k^2 * n) and times out.
    //
    //   e.g. nums = [1,2,3,2] -> already equal = 0, {1,2} x1, {2,3} x2 -> 0 + 2 = 2
    /**
     * time = O(N)
     * space = O(N)
     */
    public int maxEqualAdjacentPairs(int[] nums) {
        // edge case: fewer than 2 elements -> no adjacent pair at all
        if (nums == null || nums.length < 2) {
            return 0;
        }

        // pairs that are equal before any operation (never lost, see IDEA)
        int alreadyEqual = 0;

        // [smaller, larger] -> how many adjacent positions hold exactly these two values
        Map<List<Integer>, Integer> pairCount = new HashMap<>();

        for (int i = 0; i < nums.length - 1; i++) {
            int left = nums[i];
            int right = nums[i + 1];

            if (left == right) {
                alreadyEqual++;
                continue;
            }

            // NOTE !!! normalise the order so (2,3) and (3,2) are counted together:
            //          replacing either value with the other fixes BOTH orientations
            List<Integer> pairKey;
            if (left < right) {
                pairKey = Arrays.asList(left, right);
            } else {
                pairKey = Arrays.asList(right, left);
            }
            pairCount.put(pairKey, pairCount.getOrDefault(pairKey, 0) + 1);
        }

        // the best replacement gains one equal pair per adjacent {x, y};
        // 0 when every adjacent pair is already equal (do no operation)
        int bestGain = 0;
        for (int count : pairCount.values()) {
            bestGain = Math.max(bestGain, count);
        }

        return alreadyEqual + bestGain;
    }
}
