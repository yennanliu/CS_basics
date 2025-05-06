package LeetCodeJava.Graph;

// https://leetcode.com/problems/greatest-common-divisor-traversal/description/
/**
 * 2709. Greatest Common Divisor Traversal
 * Hard
 * Topics
 * Companies
 * Hint
 * You are given a 0-indexed integer array nums, and you are allowed to traverse between its indices. You can traverse between index i and index j, i != j, if and only if gcd(nums[i], nums[j]) > 1, where gcd is the greatest common divisor.
 *
 * Your task is to determine if for every pair of indices i and j in nums, where i < j, there exists a sequence of traversals that can take us from i to j.
 *
 * Return true if it is possible to traverse between all such pairs of indices, or false otherwise.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [2,3,6]
 * Output: true
 * Explanation: In this example, there are 3 possible pairs of indices: (0, 1), (0, 2), and (1, 2).
 * To go from index 0 to index 1, we can use the sequence of traversals 0 -> 2 -> 1, where we move from index 0 to index 2 because gcd(nums[0], nums[2]) = gcd(2, 6) = 2 > 1, and then move from index 2 to index 1 because gcd(nums[2], nums[1]) = gcd(6, 3) = 3 > 1.
 * To go from index 0 to index 2, we can just go directly because gcd(nums[0], nums[2]) = gcd(2, 6) = 2 > 1. Likewise, to go from index 1 to index 2, we can just go directly because gcd(nums[1], nums[2]) = gcd(3, 6) = 3 > 1.
 * Example 2:
 *
 * Input: nums = [3,9,5]
 * Output: false
 * Explanation: No sequence of traversals can take us from index 0 to index 2 in this example. So, we return false.
 * Example 3:
 *
 * Input: nums = [4,3,12,8]
 * Output: true
 * Explanation: There are 6 possible pairs of indices to traverse between: (0, 1), (0, 2), (0, 3), (1, 2), (1, 3), and (2, 3). A valid sequence of traversals exists for each pair, so we return true.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 105
 * 1 <= nums[i] <= 105
 *
 *
 *
 */
public class GreatestCommonDivisorTraversal {

    // V0
//    public boolean canTraverseAllPairs(int[] nums) {
//
//    }

    // V1
    // https://www.youtube.com/watch?v=jZ-RVp5CVYY


    // V2
    // https://leetcode.com/problems/greatest-common-divisor-traversal/editorial/
    public boolean canTraverseAllPairs_2(int[] nums) {
        int MAX = 100000;
        int N = nums.length;
        boolean[] has = new boolean[MAX + 1];
        for (int x : nums) {
            has[x] = true;
        }

        // edge cases
        if (N == 1) {
            return true;
        }
        if (has[1]) {
            return false;
        }

        // the general solution
        int[] sieve = new int[MAX + 1];
        for (int d = 2; d <= MAX; d++) {
            if (sieve[d] == 0) {
                for (int v = d; v <= MAX; v += d) {
                    sieve[v] = d;
                }
            }
        }

        DSU union = new DSU(2 * MAX + 1);
        for (int x : nums) {
            int val = x;
            while (val > 1) {
                int prime = sieve[val];
                int root = prime + MAX;
                if (union.find(root) != union.find(x)) {
                    union.merge(root, x);
                }
                while (val % prime == 0) {
                    val /= prime;
                }
            }
        }

        int cnt = 0;
        for (int i = 2; i <= MAX; i++) {
            if (has[i] && union.find(i) == i) {
                cnt++;
            }
        }
        return cnt == 1;
    }
}

class DSU {

    public int[] dsu;
    public int[] size;

    public DSU(int N) {
        dsu = new int[N + 1];
        size = new int[N + 1];
        for (int i = 0; i <= N; i++) {
            dsu[i] = i;
            size[i] = 1;
        }
    }

    public int find(int x) {
        return dsu[x] == x ? x : (dsu[x] = find(dsu[x]));
    }

    public void merge(int x, int y) {
        int fx = find(x);
        int fy = find(y);
        if (fx == fy) {
            return;
        }
        if (size[fx] > size[fy]) {
            int temp = fx;
            fx = fy;
            fy = temp;
        }
        dsu[fx] = fy;
        size[fy] += size[fx];
    }

}
