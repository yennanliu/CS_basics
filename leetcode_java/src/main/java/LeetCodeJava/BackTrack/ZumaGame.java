package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/zuma-game/description/

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 488. Zuma Game
 * Hard
 *
 * You are playing a variation of the game Zuma.
 *
 * In this variation of Zuma, there is a single row of colored balls on a board, where each ball
 * can be colored red 'R', yellow 'Y', blue 'B', green 'G', or white 'W'.
 * You also have several colored balls in your hand.
 *
 * Your goal is to clear all of the balls from the board. On each turn:
 *
 * - Pick any ball from your hand and insert it in between two balls in the row or on either end
 *   of the row.
 * - If there is a group of three or more consecutive balls of the same color, remove the group of
 *   balls from the board.
 *   - If this removal causes more groups of three or more of the same color to form, then continue
 *     removing each group until there are none left.
 * - If there are no more balls on the board, then you win the game.
 * - Repeat this process until you either win or do not have any more balls in your hand.
 *
 * Given a string board, representing the row of balls on the board, and a string hand, representing
 * the balls in your hand, return the minimum number of balls you have to insert to clear all the
 * balls from the board. If you cannot clear all the balls from the board using the balls in your
 * hand, return -1.
 *
 * Example 1:
 *
 * Input: board = "WRRBBW", hand = "RB"
 * Output: -1
 * Explanation: It is impossible to clear all the balls. The best you can do is:
 * - Insert 'R' so the board becomes WRRRBBW. WRRRBBW -> WBBW.
 * - Insert 'B' so the board becomes WBBBW. WBBBW -> WW.
 * There are still balls remaining on the board, and you are out of balls to insert.
 *
 * Example 2:
 *
 * Input: board = "WWRRBBWW", hand = "WRBRW"
 * Output: 2
 * Explanation: To make the board empty:
 * - Insert 'R' so the board becomes WWRRRBBWW. WWRRRBBWW -> WWBBWW.
 * - Insert 'B' so the board becomes WWBBBWW. WWBBBWW -> WWWW -> empty.
 * 2 balls from your hand were needed to clear the board.
 *
 * Example 3:
 *
 * Input: board = "G", hand = "GGGGG"
 * Output: 2
 * Explanation: To make the board empty:
 * - Insert 'G' so the board becomes GG.
 * - Insert 'G' so the board becomes GGG. GGG -> empty.
 * 2 balls from your hand were needed to clear the board.
 *
 *
 * Constraints:
 *
 * 1 <= board.length <= 16
 * 1 <= hand.length <= 5
 * board and hand consist of the characters 'R', 'Y', 'B', 'G', and 'W'.
 * The initial row of balls on the board will not have any groups of three or more consecutive
 * balls of the same color.
 *
 */
public class ZumaGame {

    // V0
    // IDEA: DFS (backtracking) + MEMOIZATION on (board, hand) state
    /**
     *   PRUNING (this is what makes the search feasible):
     *
     *    1) hand is SORTED, so identical balls in hand are tried only once
     *
     *    2) inserting anywhere INSIDE a run of identical balls gives the SAME board,
     *       so only the first position of such a run is tried
     *
     *    3) an inserted ball is only useful when it either
     *         (a) TOUCHES a same colored ball (it can grow into a group of 3), or
     *         (b) SPLITS a pair of identical balls (board[j-1] == board[j] != ball)
     *       inserting 3 same colored balls into a `no same color neighbor` gap
     *       removes them immediately -> board unchanged -> never helps.
     *
     *   time  = O(states * hand.length * board.length * board.length),
     *           bounded by the tiny input limits
     *   space = O(states * (board.length + hand.length))
     */

    private Map<String, Integer> memo;

    public int findMinStep(String board, String hand) {
        this.memo = new HashMap<>();

        // sort the hand -> enables pruning 1)
        char[] h = hand.toCharArray();
        Arrays.sort(h);

        return dfs(board, new String(h));
    }

    private int dfs(String board, String hand) {
        if (board.isEmpty()) {
            return 0;
        }
        if (hand.isEmpty()) {
            return -1;
        }

        String key = board + "#" + hand;
        if (memo.containsKey(key)) {
            return memo.get(key);
        }

        int res = -1;

        for (int i = 0; i < hand.length(); i++) {

            // pruning 1) : skip duplicated ball in hand (hand is sorted)
            if (i > 0 && hand.charAt(i) == hand.charAt(i - 1)) {
                continue;
            }
            char ball = hand.charAt(i);

            for (int j = 0; j <= board.length(); j++) {

                // pruning 2) : same insertion inside a run of `ball` colored balls
                if (j > 0 && board.charAt(j - 1) == ball) {
                    continue;
                }

                /** NOTE !!! pruning 3)
                 *
                 *  (a) grow  : the ball extends a same colored group
                 *  (b) split : the ball breaks up a pair of identical balls
                 *
                 *  any other insertion CANNOT help -> skip it
                 */
                boolean grow = j < board.length() && board.charAt(j) == ball;
                boolean split = j > 0 && j < board.length()
                        && board.charAt(j - 1) == board.charAt(j);
                if (!grow && !split) {
                    continue;
                }

                String nxtBoard = shrink(board.substring(0, j) + ball + board.substring(j));
                String nxtHand = hand.substring(0, i) + hand.substring(i + 1);

                int sub = dfs(nxtBoard, nxtHand);
                if (sub != -1 && (res == -1 || sub + 1 < res)) {
                    res = sub + 1;
                }
            }
        }

        memo.put(key, res);
        return res;
    }

    /** remove groups of >= 3 same consecutive balls, repeatedly (CASCADE) */
    private String shrink(String s) {
        int i = 0;
        while (i < s.length()) {
            int j = i;
            while (j < s.length() && s.charAt(j) == s.charAt(i)) {
                j += 1;
            }
            if (j - i >= 3) {
                /** NOTE !!!
                 *
                 *  a removal may create a NEW group
                 *  -> re-run on the shorter string (recursion handles the cascade)
                 */
                return shrink(s.substring(0, i) + s.substring(j));
            }
            i = j;
        }
        return s;
    }


    // V1
    // IDEA: BFS over (board, hand) states
    /**
     *  Every insertion costs exactly 1, so a BFS layer IS the number of balls used
     *  and the first time an empty board is dequeued that layer is the answer.
     *
     *  No memo map and no recursion depth to worry about; the visited set replaces
     *  both. It also gives the shortest solution WITHOUT trusting the min() folding.
     *
     *  time  = O(states * board * hand)
     *  space = O(states)
     */
    public int findMinStep_1(String board, String hand) {
        char[] h = hand.toCharArray();
        Arrays.sort(h);

        Deque<String[]> q = new ArrayDeque<>();      // {board, hand}
        q.offer(new String[] { board, new String(h) });
        Set<String> seen = new HashSet<>();
        seen.add(board + "#" + new String(h));

        int steps = 0;
        while (!q.isEmpty()) {
            int levelSize = q.size();
            for (int t = 0; t < levelSize; t++) {
                String[] cur = q.poll();
                String b = cur[0];
                String hd = cur[1];

                if (b.isEmpty()) {
                    return steps;
                }
                if (hd.isEmpty()) {
                    continue;
                }

                for (int i = 0; i < hd.length(); i++) {
                    if (i > 0 && hd.charAt(i) == hd.charAt(i - 1)) {
                        continue;
                    }
                    char ball = hd.charAt(i);
                    for (int j = 0; j <= b.length(); j++) {
                        if (j > 0 && b.charAt(j - 1) == ball) {
                            continue;
                        }
                        boolean grow = j < b.length() && b.charAt(j) == ball;
                        boolean split = j > 0 && j < b.length() && b.charAt(j - 1) == b.charAt(j);
                        if (!grow && !split) {
                            continue;
                        }
                        String nb = shrinkBoard(b.substring(0, j) + ball + b.substring(j));
                        String nh = hd.substring(0, i) + hd.substring(i + 1);
                        String key = nb + "#" + nh;
                        if (seen.add(key)) {
                            q.offer(new String[] { nb, nh });
                        }
                    }
                }
            }
            steps += 1;
        }

        return -1;
    }

    /** iterative cascade removal of runs >= 3 (no recursion) */
    private String shrinkBoard(String s) {
        StringBuilder cur = new StringBuilder(s);
        boolean changed = true;
        while (changed) {
            changed = false;
            int i = 0;
            while (i < cur.length()) {
                int j = i;
                while (j < cur.length() && cur.charAt(j) == cur.charAt(i)) {
                    j += 1;
                }
                if (j - i >= 3) {
                    cur.delete(i, j);
                    changed = true;
                    i = 0; // a removal can create a new run to the left
                } else {
                    i = j;
                }
            }
        }
        return cur.toString();
    }

    // V2
    // IDEA: HAND AS A 5-SLOT COUNT ARRAY (colour histogram state)
    /**
     *  The hand is a MULTISET, so encoding it as counts of R/Y/B/G/W removes the
     *  need to sort it and makes the `skip duplicate ball` pruning automatic --
     *  we simply loop over the 5 colours.
     *
     *  The memo key becomes board + the 5 digits, which collides less than the
     *  string-slicing version.
     *
     *  time  = O(states * 5 * board^2)
     *  space = O(states)
     */
    private Map<String, Integer> memo2;
    private static final String COLORS = "RYBGW";

    public int findMinStep_2(String board, String hand) {
        int[] cnt = new int[5];
        for (char c : hand.toCharArray()) {
            cnt[COLORS.indexOf(c)] += 1;
        }
        this.memo2 = new HashMap<>();
        int res = dfsCount(board, cnt);
        return res >= Integer.MAX_VALUE / 2 ? -1 : res;
    }

    private int dfsCount(String board, int[] cnt) {
        if (board.isEmpty()) {
            return 0;
        }
        String key = board + "#" + Arrays.toString(cnt);
        Integer cached = memo2.get(key);
        if (cached != null) {
            return cached;
        }

        int res = Integer.MAX_VALUE / 2;
        for (int c = 0; c < 5; c++) {
            if (cnt[c] == 0) {
                continue;
            }
            char ball = COLORS.charAt(c);
            for (int j = 0; j <= board.length(); j++) {
                if (j > 0 && board.charAt(j - 1) == ball) {
                    continue;
                }
                boolean grow = j < board.length() && board.charAt(j) == ball;
                boolean split = j > 0 && j < board.length()
                        && board.charAt(j - 1) == board.charAt(j);
                if (!grow && !split) {
                    continue;
                }
                cnt[c] -= 1;
                int sub = dfsCount(shrinkBoard(board.substring(0, j) + ball + board.substring(j)), cnt);
                cnt[c] += 1;
                res = Math.min(res, sub + 1);
            }
        }

        memo2.put(key, res);
        return res;
    }

    // V3
    // IDEA: EXHAUSTIVE DFS (no prunings) -- the reference implementation
    /**
     *  Try EVERY ball at EVERY position, with only memoisation to keep it finite.
     *
     *  Much slower than V0, but it makes no assumptions about which insertions can
     *  help -- which is exactly what makes it useful for checking that V0's three
     *  prunings never discard a winning line.
     *
     *  time  = O(states * hand * board)
     *  space = O(states)
     */
    private Map<String, Integer> memo3;

    public int findMinStep_3(String board, String hand) {
        char[] h = hand.toCharArray();
        Arrays.sort(h);
        this.memo3 = new HashMap<>();
        int res = dfsNaive(board, new String(h));
        return res >= Integer.MAX_VALUE / 2 ? -1 : res;
    }

    private int dfsNaive(String board, String hand) {
        if (board.isEmpty()) {
            return 0;
        }
        if (hand.isEmpty()) {
            return Integer.MAX_VALUE / 2;
        }
        String key = board + "#" + hand;
        Integer cached = memo3.get(key);
        if (cached != null) {
            return cached;
        }

        int res = Integer.MAX_VALUE / 2;
        for (int i = 0; i < hand.length(); i++) {
            for (int j = 0; j <= board.length(); j++) {
                String nb = shrinkBoard(board.substring(0, j) + hand.charAt(i) + board.substring(j));
                String nh = hand.substring(0, i) + hand.substring(i + 1);
                res = Math.min(res, 1 + dfsNaive(nb, nh));
            }
        }

        memo3.put(key, res);
        return res;
    }

}
