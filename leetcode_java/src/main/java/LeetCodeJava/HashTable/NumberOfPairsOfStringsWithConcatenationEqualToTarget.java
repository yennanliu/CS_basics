package LeetCodeJava.HashTable;

// https://leetcode.com/problems/number-of-pairs-of-strings-with-concatenation-equal-to-target/description/

import java.util.*;

/**
 * 2023. Number of Pairs of Strings With Concatenation Equal to Target
 * Medium
 *
 * Given an array of digit strings nums and a digit string target, return the number of pairs of
 * indices (i, j) (where i != j) such that the concatenation of nums[i] + nums[j] equals target.
 *
 *
 * Example 1:
 *
 * Input: nums = ["777","7","77","77"], target = "7777"
 * Output: 4
 * Explanation: Valid pairs are:
 * - (0, 1): "777" + "7"
 * - (1, 0): "7" + "777"
 * - (2, 3): "77" + "77"
 * - (3, 2): "77" + "77"
 *
 * Example 2:
 *
 * Input: nums = ["123","4","12","34"], target = "1234"
 * Output: 2
 * Explanation: Valid pairs are:
 * - (0, 1): "123" + "4"
 * - (2, 3): "12" + "34"
 *
 * Example 3:
 *
 * Input: nums = ["1","1","1"], target = "11"
 * Output: 6
 *
 *
 * Constraints:
 *
 * 2 <= nums.length <= 100
 * 1 <= nums[i].length <= 100
 * 2 <= target.length <= 100
 * nums[i] and target consist of digits.
 * nums[i] and target do not have leading zeros.
 *
 */
public class NumberOfPairsOfStringsWithConcatenationEqualToTarget {

    // V0
    // IDEA: ONE PASS, COUNT EARLIER PREFIXES / SUFFIXES OF TARGET
    //       for the current word w (the later index of a pair):
    //       - w is a prefix  -> pairs with every earlier word equal to the rest of target (w first)
    //       - w is a suffix  -> pairs with every earlier word equal to the front of target (w second)
    /**
     * time = O(N * L)
     * space = O(N * L)
     */
    public int numOfPairs(String[] nums, String target) {
        // earlier words that are a prefix / a suffix of target
        Map<String, Integer> prefixCount = new HashMap<>();
        Map<String, Integer> suffixCount = new HashMap<>();
        int pairs = 0;

        for (String word : nums) {
            boolean isPrefix = target.startsWith(word);
            boolean isSuffix = target.endsWith(word);

            // a word as long as target leaves nothing for its partner
            if (word.length() < target.length()) {
                if (isPrefix) {
                    String rest = target.substring(word.length());
                    pairs += suffixCount.getOrDefault(rest, 0);
                }
                if (isSuffix) {
                    String front = target.substring(0, target.length() - word.length());
                    pairs += prefixCount.getOrDefault(front, 0);
                }
            }

            if (isPrefix) {
                prefixCount.put(word, prefixCount.getOrDefault(word, 0) + 1);
            }
            if (isSuffix) {
                suffixCount.put(word, suffixCount.getOrDefault(word, 0) + 1);
            }
        }
        return pairs;
    }

    // V1
    // IDEA: HASH TABLE + TRY EVERY SPLIT POINT OF TARGET
    //       for each split target = front + back, pairs = count[front] * count[back];
    //       when front equals back, an index cannot pair with itself: count * (count - 1)
    /**
     * time = O(N * L + L^2)
     * space = O(N * L)
     */
    public int numOfPairs_1(String[] nums, String target) {
        Map<String, Integer> count = new HashMap<>();
        for (String word : nums) {
            count.put(word, count.getOrDefault(word, 0) + 1);
        }

        int pairs = 0;
        for (int split = 1; split < target.length(); split++) {
            String front = target.substring(0, split);
            String back = target.substring(split);
            int frontCount = count.getOrDefault(front, 0);
            int backCount = count.getOrDefault(back, 0);
            if (front.equals(back)) {
                pairs += frontCount * (frontCount - 1);
            } else {
                pairs += frontCount * backCount;
            }
        }
        return pairs;
    }
}
