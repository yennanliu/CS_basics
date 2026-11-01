package LeetCodeJava.BFS;

// https://leetcode.com/problems/minimum-jumps-to-reach-end-via-prime-teleportation/description/

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * 3629. Minimum Jumps to Reach End via Prime Teleportation
 * Medium
 *
 * You are given an integer array nums of length n.
 *
 * You start at index 0, and your goal is to reach index n - 1.
 *
 * From any index i, you may perform one of the following operations:
 *
 * Adjacent Step: Jump to index i + 1 or i - 1, if the index is within bounds.
 *
 * Prime Teleportation: If nums[i] is a prime number p, you may instantly jump
 * to any index j != i such that nums[j] % p == 0.
 *
 * Return the minimum number of jumps required to reach index n - 1.
 *
 * Example 1:
 *
 * Input: nums = [1,2,4,6]
 * Output: 2
 * Explanation:
 * One optimal sequence of jumps is:
 * Start at index i = 0. Take an adjacent step to index 1.
 * At index i = 1, nums[1] = 2 is a prime number. Therefore, we teleport to
 * index i = 3 as nums[3] = 6 is divisible by 2.
 * Thus, the answer is 2.
 *
 * Example 2:
 *
 * Input: nums = [2,3,4,7,9]
 * Output: 2
 * Explanation:
 * One optimal sequence of jumps is:
 * Start at index i = 0. Take an adjacent step to index i = 1.
 * At index i = 1, nums[1] = 3 is a prime number. Therefore, we teleport to
 * index i = 4 since nums[4] = 9 is divisible by 3.
 * Thus, the answer is 2.
 *
 * Example 3:
 *
 * Input: nums = [4,6,5,8]
 * Output: 3
 * Explanation:
 * Since no teleportation is possible, we move through 0 → 1 → 2 → 3. Thus, the
 * answer is 3.
 *
 * Constraints:
 *
 * 1 <= n == nums.length <= 10^5
 * 1 <= nums[i] <= 10^6
 *
 */
public class MinimumJumpsToReachEndViaPrimeTeleportation {

    // V0
    // IDEA: BFS + SMALLEST-PRIME-FACTOR SIEVE; EACH PRIME'S BUCKET IS EXPANDED ONCE
    //   moves are unweighted, so BFS gives the fewest jumps. The teleport edge
    //   (prime p -> every index whose value p divides) is quadratic if expanded
    //   naively, but the first time BFS stands on any index holding prime p,
    //   every index in p's bucket gets its final distance -- so the bucket is
    //   emptied and never expanded again. Buckets come from an SPF sieve.
    /**
     * time = O(MAX log log MAX + N log MAX)
     * space = O(MAX + N log MAX)
     */
    public int minJumps(int[] nums) {
        int n = nums.length;
        if (n == 1) {
            return 0;
        }
        int maxValue = 0;
        for (int num : nums) {
            maxValue = Math.max(maxValue, num);
        }

        // smallestPrime[v] = smallest prime factor of v (v itself when v is prime)
        int[] smallestPrime = new int[maxValue + 1];
        for (int v = 0; v <= maxValue; v++) {
            smallestPrime[v] = v;
        }
        for (int i = 2; (long) i * i <= maxValue; i++) {
            if (smallestPrime[i] == i) {
                for (int j = i * i; j <= maxValue; j += i) {
                    if (smallestPrime[j] == j) {
                        smallestPrime[j] = i;
                    }
                }
            }
        }

        // bucket.get(p) = indices whose value is divisible by prime p
        Map<Integer, List<Integer>> bucket = new HashMap<>();
        for (int idx = 0; idx < n; idx++) {
            int value = nums[idx];
            while (value > 1) {
                int prime = smallestPrime[value];
                bucket.computeIfAbsent(prime, x -> new ArrayList<>()).add(idx);
                // strip every copy of this prime so it is filed once
                while (value % prime == 0) {
                    value /= prime;
                }
            }
        }

        int[] distance = new int[n];
        Arrays.fill(distance, -1);
        distance[0] = 0;
        Queue<Integer> queue = new ArrayDeque<>();
        queue.offer(0);
        while (!queue.isEmpty()) {
            int i = queue.poll();
            if (i == n - 1) {
                return distance[i];
            }
            // adjacent steps
            int[] neighbours = { i - 1, i + 1 };
            for (int j : neighbours) {
                if (j >= 0 && j < n && distance[j] == -1) {
                    distance[j] = distance[i] + 1;
                    queue.offer(j);
                }
            }
            // teleport: only from a prime value, and only the first time that prime is seen
            int value = nums[i];
            boolean isPrime = value > 1 && smallestPrime[value] == value;
            if (isPrime && bucket.containsKey(value)) {
                for (int j : bucket.remove(value)) {
                    if (distance[j] == -1) {
                        distance[j] = distance[i] + 1;
                        queue.offer(j);
                    }
                }
            }
        }
        return distance[n - 1];
    }
}
