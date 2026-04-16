package LeetCodeJava.Math;

// https://leetcode.com/problems/the-kth-factor-of-n/description/

import java.util.ArrayList;
import java.util.List;

/**
 *  1492. The kth Factor of n
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You are given two positive integers n and k. A factor of an integer n is defined as an integer i where n % i == 0.
 *
 * Consider a list of all factors of n sorted in ascending order, return the kth factor in this list or return -1 if n has less than k factors.
 *
 *
 *
 * Example 1:
 *
 * Input: n = 12, k = 3
 * Output: 3
 * Explanation: Factors list is [1, 2, 3, 4, 6, 12], the 3rd factor is 3.
 * Example 2:
 *
 * Input: n = 7, k = 2
 * Output: 7
 * Explanation: Factors list is [1, 7], the 2nd factor is 7.
 * Example 3:
 *
 * Input: n = 4, k = 4
 * Output: -1
 * Explanation: Factors list is [1, 2, 4], there is only 3 factors. We should return -1.
 *
 *
 * Constraints:
 *
 * 1 <= k <= n <= 1000
 *
 *
 * Follow up:
 *
 * Could you solve this problem in less than O(n) complexity?
 *
 */
public class TheKthFactorOfN {

    // V0
//    public int kthFactor(int n, int k) {
//
//    }

    // V1
    // IDEA: MATH
    // https://leetcode.com/problems/the-kth-factor-of-n/solutions/7732631/easy-approach-using-java-for-beginners-b-5k1d/
    public int kthFactor_1(int n, int k) {
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            if (n % i == 0) {
                list.add(i);
            }
            // if(n/i!=i){
            //     list.add(n/i);
            // }
        }
        // Collections.sort(list);
        if (list.size() < k) {
            return -1;
        }
        return list.get(k - 1);

    }


    // V2
    // IDEA: MATH
    // https://leetcode.com/problems/the-kth-factor-of-n/solutions/7631774/on-approach-to-find-kth-factor-by-vyankt-2jc9/
    public int kthFactor_2(int n, int k) {
        int count = 0;

        for (int i = 1; i <= n; i++) {
            if (n % i == 0 && ++count == k)
                return i;
        }

        return -1;
    }





}
