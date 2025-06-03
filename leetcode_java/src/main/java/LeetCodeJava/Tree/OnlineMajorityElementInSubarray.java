package LeetCodeJava.Tree;

// https://leetcode.com/problems/online-majority-element-in-subarray/description/

import java.util.*;

/**
 * 1157. Online Majority Element In Subarray
 * Hard
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * Design a data structure that efficiently finds the majority element of a given subarray.
 *
 * The majority element of a subarray is an element that occurs threshold times or more in the subarray.
 *
 * Implementing the MajorityChecker class:
 *
 * MajorityChecker(int[] arr) Initializes the instance of the class with the given array arr.
 * int query(int left, int right, int threshold) returns the element in the subarray arr[left...right] that occurs at least threshold times, or -1 if no such element exists.
 *
 *
 * Example 1:
 *
 * Input
 * ["MajorityChecker", "query", "query", "query"]
 * [[[1, 1, 2, 2, 1, 1]], [0, 5, 4], [0, 3, 3], [2, 3, 2]]
 * Output
 * [null, 1, -1, 2]
 *
 * Explanation
 * MajorityChecker majorityChecker = new MajorityChecker([1, 1, 2, 2, 1, 1]);
 * majorityChecker.query(0, 5, 4); // return 1
 * majorityChecker.query(0, 3, 3); // return -1
 * majorityChecker.query(2, 3, 2); // return 2
 *
 *
 * Constraints:
 *
 * 1 <= arr.length <= 2 * 104
 * 1 <= arr[i] <= 2 * 104
 * 0 <= left <= right < arr.length
 * threshold <= right - left + 1
 * 2 * threshold > right - left + 1
 * At most 104 calls will be made to query.
 *
 *
 */
public class OnlineMajorityElementInSubarray {

    // V0
//    class MajorityChecker {
//
//        public MajorityChecker(int[] arr) {
//
//        }
//
//        public int query(int left, int right, int threshold) {
//
//        }
//    }

    // V0-0-1
    // IDEA: HASHMAP + 2 POINTERS (fixed by gpt) (TLE)
    class MajorityChecker_0_0_1 {

        int[] arr;

        public MajorityChecker_0_0_1(int[] arr) {
            this.arr = arr;
        }

        public int query(int left, int right, int threshold) {
            // Count frequency in [left, right]
            Map<Integer, Integer> freq = new HashMap<>();

            for (int i = left; i <= right; i++) {
                int val = arr[i];
                freq.put(val, freq.getOrDefault(val, 0) + 1);

                // If frequency reaches threshold, return immediately
                if (freq.get(val) >= threshold) {
                    return val;
                }
            }

            return -1;
        }
    }

    // V0-1
    // IDEA: HASHMAP + LIST (fixed by gpt)
    class MajorityChecker_0_1 {
        private int[] arr;
        private Map<Integer, List<Integer>> idxMap;
        private Random rand;

        public MajorityChecker_0_1(int[] arr) {
            this.arr = arr;
            this.idxMap = new HashMap<>();
            this.rand = new Random();

            // Build the index map: number -> list of positions
            for (int i = 0; i < arr.length; i++) {
                idxMap.computeIfAbsent(arr[i], x -> new ArrayList<>()).add(i);
            }
        }

        public int query(int left, int right, int threshold) {
            int length = right - left + 1;

            // Try up to 20 random samples (statistically enough)
            for (int i = 0; i < 20; i++) {
                int randIdx = left + rand.nextInt(length);
                int candidate = arr[randIdx];

                List<Integer> indices = idxMap.get(candidate);
                int freq = countInRange(indices, left, right);

                if (freq >= threshold)
                    return candidate;
            }

            return -1;
        }

        // Binary search for lower and upper bounds in the list of positions
        private int countInRange(List<Integer> indices, int left, int right) {
            int start = lowerBound(indices, left);
            int end = upperBound(indices, right);
            return end - start;
        }

        private int lowerBound(List<Integer> list, int target) {
            int lo = 0, hi = list.size();
            while (lo < hi) {
                int mid = (lo + hi) / 2;
                if (list.get(mid) >= target)
                    hi = mid;
                else
                    lo = mid + 1;
            }
            return lo;
        }

        private int upperBound(List<Integer> list, int target) {
            int lo = 0, hi = list.size();
            while (lo < hi) {
                int mid = (lo + hi) / 2;
                if (list.get(mid) > target)
                    hi = mid;
                else
                    lo = mid + 1;
            }
            return lo;
        }
    }

    // V1
    // https://leetcode.com/problems/online-majority-element-in-subarray/solutions/617295/java-ologn-for-each-query-with-bit-manip-bpuu/
    class MajorityChecker_1 {

        private final int digits = 15;
        private int[][] presum;
        private ArrayList<Integer>[] pos;

        public MajorityChecker_1(int[] arr) {
            int len = arr.length;
            presum = new int[len + 1][digits];
            pos = new ArrayList[20001];

            for (int i = 0; i < len; i++) {
                int n = arr[i];
                if (pos[n] == null)
                    pos[n] = new ArrayList();
                pos[n].add(i);

                for (int j = 0; j < digits; j++) {
                    presum[i + 1][j] = presum[i][j] + (n & 1);
                    n >>= 1;
                }
            }
        }

        public int query(int left, int right, int threshold) {
            int ans = 0;
            for (int i = digits - 1; i >= 0; i--) {
                int cnt = presum[right + 1][i] - presum[left][i];
                int b = 1;
                if (cnt >= threshold)
                    b = 1;
                else if (right - left + 1 - cnt >= threshold)
                    b = 0;
                else
                    return -1;
                ans = (ans << 1) + b;
            }

            // check
            ArrayList<Integer> list = pos[ans];
            if (list == null)
                return -1;
            int L = floor(list, left - 1);
            int R = floor(list, right);
            if (R - L >= threshold)
                return ans;
            return -1;
        }

        private int floor(ArrayList<Integer> list, int n) {
            int left = 0, right = list.size() - 1, mid;
            while (left <= right) {
                mid = left + (right - left) / 2;
                int index = list.get(mid);
                if (index == n)
                    return mid;
                else if (index < n)
                    left = mid + 1;
                else
                    right = mid - 1;
            }
            return right;
        }

    }

    // V2
    // https://leetcode.com/problems/online-majority-element-in-subarray/solutions/355908/bias-against-python-again-by-leetcoder28-gxa7/
    class MajorityChecker_2 {
        int[] arr;

        public MajorityChecker_2(int[] arr) {
            this.arr = arr;
        }

        public int query(int left, int right, int threshold) {
            int candidate = -1;
            int count = 0;

            for (int i = left; i <= right; i++) {
                if (count == 0) {
                    candidate = arr[i];
                    count = 1;
                } else if (candidate == arr[i]) {
                    count++;
                } else {
                    count--;
                }
            }

            count = 0;
            for (int i = left; i <= right; i++) {
                if (arr[i] == candidate) {
                    count++;
                }
            }
            if (count >= threshold) {
                return candidate;
            } else {
                return -1;
            }
        }
    }

    // V3

    // V4

}
