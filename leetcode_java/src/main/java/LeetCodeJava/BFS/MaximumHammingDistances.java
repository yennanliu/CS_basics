package LeetCodeJava.BFS;

// https://leetcode.com/problems/maximum-hamming-distances/description/
// https://leetcode.ca/2024-05-25-3141-Maximum-Hamming-Distances/

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

/**
 * 3141. Maximum Hamming Distances
 * Hard
 *
 * You are given an array nums and an integer m. The Hamming distance between two integers is the number of positions at which the corresponding bits are different.
 *
 * Return an array answer of the same length as nums, where answer[i] is the maximum Hamming distance between nums[i] and any element in nums, where every integer is treated as an m-bit number.
 *
 *
 * Example 1:
 *
 * Input: nums = [9,12,9,11], m = 4
 * Output: [2,3,2,3]
 * Explanation:
 * The binary representations are 9 = 1001, 12 = 1100, 11 = 1011.
 * - For nums[0] = 9, the farthest element is 12 at a distance of 2.
 * - For nums[1] = 12, the farthest element is 11 at a distance of 3.
 * - For nums[2] = 9, the farthest element is 12 at a distance of 2.
 * - For nums[3] = 11, the farthest element is 12 at a distance of 3.
 *
 * Example 2:
 *
 * Input: nums = [3,4,6,10], m = 4
 * Output: [3,3,2,3]
 * Explanation:
 * The binary representations are 3 = 0011, 4 = 0100, 6 = 0110, 10 = 1010.
 * - For nums[0] = 3, the farthest element is 4 at a distance of 3.
 * - For nums[1] = 4, the farthest element is 3 at a distance of 3.
 * - For nums[2] = 6, the farthest elements are 9 and 10 at a distance of 2.
 * - For nums[3] = 10, the farthest element is 4 at a distance of 3.
 *
 *
 * Constraints:
 *
 * 1 <= m <= 17
 * 1 <= nums.length <= 2^m
 * 0 <= nums[i] < 2^m
 *
 */
public class MaximumHammingDistances {

    // V0
    // IDEA: MULTI-SOURCE BFS ON THE m-BIT HYPERCUBE -- FARTHEST FROM x IS NEAREST TO ~x
    //   over m bits, hamming(x, y) = m - hamming(~x, y), so the farthest element
    //   from x is the nearest element to x's complement.
    //   Nearest-element distances: BFS from every value in nums at distance 0,
    //   each edge flips one bit. answer[i] = m - dist[complement of nums[i]].
    //   2^m nodes * m edges (about 2.2 million at m = 17) beats comparing all pairs.
    /**
     * time = O(m * 2^m)
     * space = O(2^m)
     */
    public int[] maxHammingDistances(int[] nums, int m) {
        int size = 1 << m;
        // mask = m one-bits; x ^ mask flips every bit of x (its m-bit complement)
        int mask = size - 1;

        // distance[v] = bits separating v from the closest value in nums
        int[] distance = new int[size];
        Arrays.fill(distance, -1);
        Queue<Integer> queue = new ArrayDeque<>();
        for (int num : nums) {
            if (distance[num] == -1) {
                distance[num] = 0;
                queue.offer(num);
            }
        }

        while (!queue.isEmpty()) {
            int value = queue.poll();
            for (int bit = 0; bit < m; bit++) {
                // flip one bit: a neighbour on the hypercube
                int next = value ^ (1 << bit);
                if (distance[next] == -1) {
                    distance[next] = distance[value] + 1;
                    queue.offer(next);
                }
            }
        }

        int[] answer = new int[nums.length];
        for (int i = 0; i < nums.length; i++) {
            answer[i] = m - distance[nums[i] ^ mask];
        }
        return answer;
    }
}
