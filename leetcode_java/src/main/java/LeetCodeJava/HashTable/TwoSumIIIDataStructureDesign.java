package LeetCodeJava.HashTable;

// https://leetcode.com/problems/two-sum-iii-data-structure-design/

import java.util.*;

/**
 *  170. Two Sum III - Data structure design
 *  Easy
 *
 *  Design a data structure that accepts a stream of integers and checks if it
 *  has a pair of integers that sum up to a particular value.
 *
 *  Implement the TwoSum class:
 *   - TwoSum() Initializes the TwoSum object, with an empty array initially.
 *   - void add(int number) Adds number to the data structure.
 *   - boolean find(int value) Returns true if there exists any pair of numbers
 *     whose sum is equal to value, otherwise returns false.
 *
 *  Example 1:
 *  Input
 *  ["TwoSum", "add", "add", "add", "find", "find"]
 *  [[], [1], [3], [5], [4], [7]]
 *  Output
 *  [null, null, null, null, true, false]
 *
 *  Constraints:
 *   - -10^5 <= number <= 10^5
 *   - -2^31 <= value <= 2^31 - 1
 *   - At most 10^4 calls will be made to add and find.
 */
public class TwoSumIIIDataStructureDesign {

    // V0
    // IDEA: HASHMAP (val -> count) : O(1) add, O(n) find
    public static class TwoSum {

        private final Map<Integer, Integer> cntMap;

        public TwoSum() {
            this.cntMap = new HashMap<>();
        }

        /**
         * time = O(1)
         * space = O(n)
         */
        public void add(int number) {
            int cnt = this.cntMap.getOrDefault(number, 0);
            this.cntMap.put(number, cnt + 1);
        }

        /**
         * time = O(n)   # n = number of distinct values
         * space = O(1)
         */
        public boolean find(int value) {
            for (Map.Entry<Integer, Integer> entry : this.cntMap.entrySet()) {
                int num = entry.getKey();
                /**
                 *  NOTE !!!
                 *
                 *  use `long` to avoid int overflow,
                 *  since value can be as small as -2^31
                 */
                long complement = (long) value - num;

                if (complement == num) {
                    // need the SAME number to appear at least twice
                    if (entry.getValue() > 1) {
                        return true;
                    }
                } else if (complement >= Integer.MIN_VALUE && complement <= Integer.MAX_VALUE
                        && this.cntMap.containsKey((int) complement)) {
                    return true;
                }
            }
            return false;
        }
    }

    // V1
    // IDEA: SORTED LIST + 2 POINTERS : O(1) add, O(n log n) find (sort only when dirty)
    public static class TwoSum2 {

        private final List<Integer> nums;
        private boolean isSorted;

        public TwoSum2() {
            this.nums = new ArrayList<>();
            this.isSorted = false;
        }

        /**
         * time = O(1) amortized
         * space = O(n)
         */
        public void add(int number) {
            this.nums.add(number);
            this.isSorted = false;
        }

        /**
         * time = O(n log n) when re-sorting, O(n) otherwise
         * space = O(1)
         */
        public boolean find(int value) {
            if (!this.isSorted) {
                Collections.sort(this.nums);
                this.isSorted = true;
            }

            int lo = 0;
            int hi = this.nums.size() - 1;

            while (lo < hi) {
                long sum = (long) this.nums.get(lo) + this.nums.get(hi);
                if (sum < value) {
                    lo++;
                } else if (sum > value) {
                    hi--;
                } else {
                    return true;
                }
            }

            return false;
        }
    }
}
