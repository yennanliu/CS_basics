package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/stickers-to-spell-word/description/

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;
import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 691. Stickers to Spell Word
 * Hard
 *
 * We are given n different types of stickers. Each sticker has a lowercase English word
 * on it.
 *
 * You would like to spell out the given string target by cutting individual letters from
 * your collection of stickers and rearranging them. You can use each sticker more than
 * once if you want, and you have infinite quantities of each sticker.
 *
 * Return the minimum number of stickers that you need to spell out target. If the task
 * is impossible, return -1.
 *
 * Note: In all test cases, all words were chosen randomly from the 1000 most common US
 * English words, and target was chosen as a concatenation of two random words.
 *
 * Example 1:
 *
 * Input: stickers = ["with","example","science"], target = "thehat"
 * Output: 3
 * Explanation:
 * We can use 2 "with" stickers, and 1 "example" sticker.
 * After cutting and rearrange the letters of those stickers, we can form the target
 * "thehat".
 * Also, this is the minimum number of stickers necessary to form the target string.
 *
 * Example 2:
 *
 * Input: stickers = ["notice","possible"], target = "basicbasic"
 * Output: -1
 * Explanation:
 * We cannot form the target "basicbasic" from cutting letters from the given stickers.
 *
 * Constraints:
 *
 * n == stickers.length
 * 1 <= n <= 50
 * 1 <= stickers[i].length <= 10
 * 1 <= target.length <= 15
 * stickers[i] and target consist of lowercase English letters.
 *
 */
public class StickersToSpellWord {

    // V0
    // IDEA: BITMASK DP over the POSITIONS of target
    /**
     *  target is at most 15 chars, so the set of positions already covered fits in an
     *  int BITMASK (bit i set == target[i] has been supplied by some sticker).
     *
     *  DP def:
     *    - dp[mask] = minimum number of stickers needed to cover EXACTLY the
     *                 positions in `mask`
     *
     *  DP eq:
     *    - from every reachable mask, try each sticker: hand out its letters to the
     *      still-uncovered positions, LOWEST-INDEX-FIRST, producing `nxt`
     *        dp[nxt] = min(dp[nxt], dp[mask] + 1)
     *
     *  NOTE !!! why filling uncovered positions in left-to-right index order is SAFE:
     *           within a single letter, all positions needing that letter are
     *           INTERCHANGEABLE, so WHICH ones a given sticker fills does not matter
     *           -- only HOW MANY.
     *
     *  NOTE !!! masks are processed in INCREASING order, and a sticker never CLEARS a
     *           bit, so dp[mask] is final by the time it is read.
     *
     *  time  = O(2^L * n * L), L = target.length, n = stickers.length
     *  space = O(2^L)
     */
    public int minStickers(String[] stickers, String target) {
        int L = target.length();
        int full = 1 << L;
        final int INF = Integer.MAX_VALUE / 2;

        // letter multiset of each sticker; ONLY letters appearing in target matter
        Set<Character> needed = new HashSet<>();
        for (int i = 0; i < L; i++) {
            needed.add(target.charAt(i));
        }

        List<int[]> stickerCounts = new ArrayList<>();
        for (String s : stickers) {
            int[] c = new int[26];
            boolean any = false;
            for (int i = 0; i < s.length(); i++) {
                char ch = s.charAt(i);
                if (needed.contains(ch)) {
                    c[ch - 'a'] += 1;
                    any = true;
                }
            }
            if (any) {
                stickerCounts.add(c);
            }
        }

        int[] dp = new int[full];
        Arrays.fill(dp, INF);
        dp[0] = 0;

        for (int mask = 0; mask < full; mask++) {
            if (dp[mask] == INF) {
                continue;
            }
            for (int[] count : stickerCounts) {
                int nxt = mask;
                int[] remain = count.clone();

                for (int i = 0; i < L; i++) {
                    if (((nxt >> i) & 1) == 0) {
                        int ch = target.charAt(i) - 'a';
                        if (remain[ch] > 0) {
                            remain[ch] -= 1;
                            nxt |= 1 << i;
                        }
                    }
                }

                if (dp[nxt] > dp[mask] + 1) {
                    dp[nxt] = dp[mask] + 1;
                }
            }
        }

        return dp[full - 1] == INF ? -1 : dp[full - 1];
    }


    // V1
    // IDEA: BFS OVER THE MASKS (shortest path in an unweighted graph)
    /**
     *  Every sticker use costs exactly 1, so the mask graph is unweighted and BFS
     *  finds the minimum directly -- the first time the full mask is dequeued.
     *
     *  No relaxation and no `is this dp entry final?` reasoning: the BFS layer IS
     *  the sticker count.
     *
     *  time  = O(2^L * n * L)
     *  space = O(2^L)
     */
    public int minStickers_1(String[] stickers, String target) {
        int L = target.length();
        int full = (1 << L) - 1;

        List<int[]> counts = buildCounts(stickers, target);

        boolean[] seen = new boolean[1 << L];
        Deque<Integer> q = new ArrayDeque<>();
        q.offer(0);
        seen[0] = true;
        int steps = 0;

        while (!q.isEmpty()) {
            int levelSize = q.size();
            for (int t = 0; t < levelSize; t++) {
                int mask = q.poll();
                if (mask == full) {
                    return steps;
                }
                for (int[] c : counts) {
                    int nxt = applySticker(mask, c, target, L);
                    if (!seen[nxt]) {
                        seen[nxt] = true;
                        q.offer(nxt);
                    }
                }
            }
            steps += 1;
        }
        return -1;
    }

    /** letter histograms of the stickers, restricted to letters target needs */
    private List<int[]> buildCounts(String[] stickers, String target) {
        boolean[] needed = new boolean[26];
        for (int i = 0; i < target.length(); i++) {
            needed[target.charAt(i) - 'a'] = true;
        }
        List<int[]> counts = new ArrayList<>();
        for (String s : stickers) {
            int[] c = new int[26];
            boolean any = false;
            for (int i = 0; i < s.length(); i++) {
                if (needed[s.charAt(i) - 'a']) {
                    c[s.charAt(i) - 'a'] += 1;
                    any = true;
                }
            }
            if (any) {
                counts.add(c);
            }
        }
        return counts;
    }

    /** hand the sticker's letters to the lowest still-uncovered positions */
    private int applySticker(int mask, int[] count, String target, int L) {
        int[] remain = count.clone();
        int nxt = mask;
        for (int i = 0; i < L; i++) {
            if (((nxt >> i) & 1) == 0) {
                int ch = target.charAt(i) - 'a';
                if (remain[ch] > 0) {
                    remain[ch] -= 1;
                    nxt |= 1 << i;
                }
            }
        }
        return nxt;
    }

    // V2
    // IDEA: TOP-DOWN MEMOISED DFS, always filling the LOWEST uncovered bit
    /**
     *  From a mask, find the first uncovered position and only try the stickers
     *  that actually CONTAIN that letter -- every other sticker is a wasted branch
     *  from this state.
     *
     *  That single restriction prunes the branching factor hard, and it is what
     *  makes the top-down version competitive with the full sweep.
     *
     *  time  = O(2^L * n * L)
     *  space = O(2^L)
     */
    private Integer[] memoSt;

    public int minStickers_2(String[] stickers, String target) {
        int L = target.length();
        memoSt = new Integer[1 << L];
        List<int[]> counts = buildCounts(stickers, target);
        int res = dfsSticker(0, counts, target, L);
        return res >= Integer.MAX_VALUE / 2 ? -1 : res;
    }

    private int dfsSticker(int mask, List<int[]> counts, String target, int L) {
        int full = (1 << L) - 1;
        if (mask == full) {
            return 0;
        }
        if (memoSt[mask] != null) {
            return memoSt[mask];
        }

        int first = 0;
        while (((mask >> first) & 1) == 1) {
            first += 1;
        }
        int needChar = target.charAt(first) - 'a';

        int best = Integer.MAX_VALUE / 2;
        for (int[] c : counts) {
            if (c[needChar] == 0) {
                continue;   // cannot help with the lowest uncovered position
            }
            int nxt = applySticker(mask, c, target, L);
            best = Math.min(best, 1 + dfsSticker(nxt, counts, target, L));
        }

        memoSt[mask] = best;
        return best;
    }

    // V3
    // IDEA: DIJKSTRA-STYLE SWEEP with an explicit `finalised` set
    /**
     *  Equivalent to the BFS but processed with a priority queue keyed by the
     *  sticker count.
     *
     *  Pointless while all edges cost 1 -- but it is the version that survives the
     *  natural follow-up where stickers have DIFFERENT prices, which neither the
     *  BFS nor the forward sweep would handle.
     *
     *  time  = O(2^L * n * L * log)
     *  space = O(2^L)
     */
    public int minStickers_3(String[] stickers, String target) {
        int L = target.length();
        int full = (1 << L) - 1;
        List<int[]> counts = buildCounts(stickers, target);

        int[] dist = new int[1 << L];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[0] = 0;

        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(x -> x[0]));
        pq.add(new int[] { 0, 0 });

        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            int cost = cur[0];
            int mask = cur[1];
            if (cost > dist[mask]) {
                continue;
            }
            if (mask == full) {
                return cost;
            }
            for (int[] c : counts) {
                int nxt = applySticker(mask, c, target, L);
                if (cost + 1 < dist[nxt]) {
                    dist[nxt] = cost + 1;
                    pq.add(new int[] { cost + 1, nxt });
                }
            }
        }
        return -1;
    }

}
