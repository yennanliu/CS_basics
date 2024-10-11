package LeetCodeJava.Math;

// https://leetcode.com/problems/random-pick-with-weight/description/

import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;

/**
 * 528. Random Pick with Weight
 * Medium
 * Topics
 * Companies
 * You are given a 0-indexed array of positive integers w where w[i] describes the weight of the ith index.
 * <p>
 * You need to implement the function pickIndex(), which randomly picks an index in the range [0, w.length - 1] (inclusive) and returns it. The probability of picking an index i is w[i] / sum(w).
 * <p>
 * For example, if w = [1, 3], the probability of picking index 0 is 1 / (1 + 3) = 0.25 (i.e., 25%), and the probability of picking index 1 is 3 / (1 + 3) = 0.75 (i.e., 75%).
 * <p>
 * <p>
 * Example 1:
 * <p>
 * Input
 * ["Solution","pickIndex"]
 * [[[1]],[]]
 * Output
 * [null,0]
 * <p>
 * Explanation
 * Solution solution = new Solution([1]);
 * solution.pickIndex(); // return 0. The only option is to return 0 since there is only one element in w.
 * Example 2:
 * <p>
 * Input
 * ["Solution","pickIndex","pickIndex","pickIndex","pickIndex","pickIndex"]
 * [[[1,3]],[],[],[],[],[]]
 * Output
 * [null,1,1,1,1,0]
 * <p>
 * Explanation
 * Solution solution = new Solution([1, 3]);
 * solution.pickIndex(); // return 1. It is returning the second element (index = 1) that has a probability of 3/4.
 * solution.pickIndex(); // return 1
 * solution.pickIndex(); // return 1
 * solution.pickIndex(); // return 1
 * solution.pickIndex(); // return 0. It is returning the first element (index = 0) that has a probability of 1/4.
 * <p>
 * Since this is a randomization problem, multiple answers are allowed.
 * All of the following outputs can be considered correct:
 * [null,1,1,1,1,0]
 * [null,1,1,1,1,1]
 * [null,1,1,1,0,0]
 * [null,1,1,1,0,1]
 * [null,1,0,1,0,0]
 * ......
 * and so on.
 * <p>
 * <p>
 * Constraints:
 * <p>
 * 1 <= w.length <= 104
 * 1 <= w[i] <= 105
 * pickIndex will be called at most 104 times.
 */
public class RandomPickWithWeight {

    // V0
    // IDEA : prefixSums + BINARY SEARCH (fixed by gpt)
    /**
     * 	1.	Prefix Sums Array:
     * 	        The array prefixSums stores cumulative sums of the weights.
     * 	        For example, if w = [1, 3, 2], then prefixSums = [1, 4, 6].
     * 	        This allows us to treat the weights as a range and pick indices
     * 	        proportionally to their weight.
     *
     * 	2.	Random Number:
     * 	        We generate a random number between 1
     * 	        and the total sum of the weights (totalSum).
     * 	        This number represents a “position”
     * 	        within the cumulative range.
     *
     * 	3.	Binary Search:
     * 	        We perform a binary search on prefixSums to
     * 	        find the index where the random number fits,
     * 	        ensuring that indices with higher weights
     * 	        have a proportionally higher chance of being selected.
     *
     */
    class Solution {
        int[] prefixSums;
        int totalSum;
        Random random;

        public Solution(int[] w) {
            this.prefixSums = new int[w.length];
            this.totalSum = 0;
            this.random = new Random();

            // Build the prefix sum array
            /**
             *  NOTE !!!
             *
             *  we use "prefix sum" to avoid double loop
             *
             *
             *  e.g. :
             *
             *  Given w = [1, 3, 2], the prefixSums array becomes [1, 4, 6]. The values in the prefixSums array represent cumulative weights, and the total sum of weights is 6 (totalSum = 6).
             *
             * The probability of picking each index is proportional to its corresponding weight in w. Here’s how we can interpret this:
             *
             * 	1.	Index 0 (weight = 1): The range for index 0 in the prefixSums array is from 1 to 1 (since prefixSums[0] = 1), which gives a probability of:
             *
             *      -> Probability for index 0 =  1/ 6
             *
             * 	2.	Index 1 (weight = 3): The range for index 1 is from 2 to 4 (since prefixSums[1] = 4), which gives a probability of:
             *
             *      -> Probability for index 1 = (4-1) / 6
             *
             * 	3.	Index 2 (weight = 2): The range for index 2 is from 5 to 6 (since prefixSums[2] = 6), which gives a probability of:
             *
             *      -> Probability for index 2 =  (6-4) / 6
             *
             *
             */
            for (int i = 0; i < w.length; i++) {
                totalSum += w[i];
                prefixSums[i] = totalSum;
            }
        }

        public int pickIndex() {
            // Get a random value in the range [1, totalSum]
            int target = random.nextInt(totalSum) + 1;

            // Binary search to find the right index
            int low = 0, high = prefixSums.length - 1;
            while (low < high) {
                int mid = low + (high - low) / 2;
                if (prefixSums[mid] < target) {
                    low = mid + 1;
                } else {
                    high = mid;
                }
            }

            return low;
        }
    }

    // V1
    // https://leetcode.com/problems/random-pick-with-weight/solutions/671874/java-three-solutions-easiest-to-hardest-difficulty/
    class Solution_1 {

        private ArrayList<Integer> nums;
        private Random rand;

        public Solution_1(int[] w) {
            this.nums = new ArrayList<>();
            this.rand = new Random();

            for (int i = 0; i < w.length; i++)
                for (int j = 0; j < w[i]; j++)
                    this.nums.add(i);
        }

        public int pickIndex() {
            int n = this.rand.nextInt(nums.size());
            return nums.get(n);
        }
    }

    // V2
    TreeMap<Integer, Integer> map;
    Random random = new Random(System.currentTimeMillis());
    private int sum = 0;

    class Solution_2 {

        public Solution_2(int[] w) {
            map = new TreeMap<>();
            for (int i = 0; i < w.length; i++) {
                sum += w[i];
                map.put(sum, i);
            }
        }

        public int pickIndex() {
            return map.get(map.higherKey(random.nextInt(sum)));
        }

    }

}
