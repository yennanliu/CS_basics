package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/maximum-number-of-groups-getting-fresh-donuts/description/

import java.util.*;

/**
 * 1815. Maximum Number of Groups Getting Fresh Donuts
 * Hard
 *
 * There is a donuts shop that bakes donuts in batches of batchSize. They have a
 * rule where they must serve all of the donuts of a batch before serving any
 * donuts of the next batch. You are given an integer batchSize and an integer
 * array groups, where groups[i] denotes that there is a group of groups[i]
 * customers that will visit the shop. Each customer will get exactly one donut.
 *
 * When a group visits the shop, all customers of the group must be served before
 * serving any of the following groups. A group will be happy if they all get
 * fresh donuts. That is, the first customer of the group does not receive a
 * donut that was left over from the previous group.
 *
 * You can freely rearrange the ordering of the groups. Return the maximum
 * possible number of happy groups after rearranging the groups.
 *
 *
 * Example 1:
 *
 * Input: batchSize = 3, groups = [1,2,3,4,5,6]
 * Output: 4
 * Explanation: You can arrange the groups as [6,2,4,5,1,3]. Then the 1st, 2nd,
 * 4th, and 6th groups will be happy.
 *
 * Example 2:
 *
 * Input: batchSize = 4, groups = [1,3,2,5,2,2,1,6]
 * Output: 4
 *
 *
 * Constraints:
 *
 * 1 <= batchSize <= 9
 * 1 <= groups.length <= 30
 * 1 <= groups[i] <= 10^9
 *
 */
public class MaximumNumberOfGroupsGettingFreshDonuts {

    // each remainder's count (<= 30) fits in a 5-bit slot of the packed state
    private static final int BITS_PER_SLOT = 5;
    private static final long SLOT_MASK = 31L;

    // V0
    // IDEA: MEMOIZED DFS OVER REMAINDER COUNTS (state = how many groups of each remainder are left)
    /**
     * time = O(B^2 * prod(cnt_i + 1)), B = batchSize
     * space = O(B * prod(cnt_i + 1))
     */
    public int maxHappyGroups(int batchSize, int[] groups) {
        int alwaysHappy = 0;
        long state = 0;

        for (int group : groups) {
            int remainder = group % batchSize;
            if (remainder == 0) {
                // never changes the leftover; served first, these groups are always happy
                alwaysHappy++;
            } else {
                // one more unserved group of this remainder
                state += 1L << (remainder * BITS_PER_SLOT);
            }
        }

        Map<Long, Integer> memo = new HashMap<>();
        return alwaysHappy + dfs(state, 0, batchSize, memo);
    }

    /**
     * Max happy groups obtainable from the unserved multiset `state` when `leftover`
     * donuts remain in the current batch.
     */
    private int dfs(long state, int leftover, int batchSize, Map<Long, Integer> memo) {
        if (state == 0) {
            return 0;
        }
        // leftover < batchSize <= 9, so 4 bits are enough to fold it into the key
        long key = (state << 4) | leftover;
        if (memo.containsKey(key)) {
            return memo.get(key);
        }

        // the next group served is happy exactly when no donuts are left over
        int gain = (leftover == 0) ? 1 : 0;
        int best = 0;
        for (int remainder = 1; remainder < batchSize; remainder++) {
            long countLeft = (state >> (remainder * BITS_PER_SLOT)) & SLOT_MASK;
            if (countLeft == 0) {
                continue;
            }
            // serve one group of this remainder next
            long nextState = state - (1L << (remainder * BITS_PER_SLOT));
            int nextLeftover = (leftover + remainder) % batchSize;
            best = Math.max(best, gain + dfs(nextState, nextLeftover, batchSize, memo));
        }

        memo.put(key, best);
        return best;
    }
}
