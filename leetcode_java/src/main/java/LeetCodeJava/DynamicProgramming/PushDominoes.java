package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/push-dominoes/

/**
 *  838. Push Dominoes
 *  Medium
 *
 *  There are n dominoes in a line, and we place each domino vertically upright.
 *  In the beginning, we simultaneously push some of the dominoes either to the
 *  left or to the right.
 *
 *  After each second, each domino that is falling to the left pushes the
 *  adjacent domino on the left. Similarly, the dominoes falling to the right
 *  push their adjacent dominoes standing on the right.
 *
 *  When a vertical domino has dominoes falling on it from both sides, it stays
 *  still due to the balance of the forces.
 *
 *  You are given a string dominoes representing the initial state where:
 *    - dominoes[i] = 'L', if the i-th domino has been pushed to the left,
 *    - dominoes[i] = 'R', if the i-th domino has been pushed to the right, and
 *    - dominoes[i] = '.', if the i-th domino has not been pushed.
 *
 *  Return a string representing the final state.
 *
 *  Example 1:
 *    Input: dominoes = "RR.L"
 *    Output: "RR.L"
 *
 *  Example 2:
 *    Input: dominoes = ".L.R...LR..L.."
 *    Output: "LL.RR.LLRRLL.."
 *
 *  Constraints:
 *    - n == dominoes.length
 *    - 1 <= n <= 10^5
 *    - dominoes[i] is either 'L', 'R', or '.'.
 */
public class PushDominoes {

    // V0
    // IDEA: pad with sentinels "L" ... "R", then resolve each segment between 2 anchors
    /**
     * time = O(N)
     * space = O(N)
     */
    public String pushDominoes(String dominoes) {
        String s = "L" + dominoes + "R";
        StringBuilder sb = new StringBuilder();
        int prev = 0;

        for (int i = 1; i < s.length(); i++) {
            char cur = s.charAt(i);
            if (cur == '.') {
                continue;
            }
            char left = s.charAt(prev);
            int gap = i - prev - 1;

            if (prev > 0) {
                sb.append(left);          // the anchor itself (skip the added sentinel)
            }

            if (left == cur) {
                // "L...L" or "R...R" -> everything falls the same way
                for (int j = 0; j < gap; j++) {
                    sb.append(cur);
                }
            } else if (left == 'L' && cur == 'R') {
                // "L...R" -> nothing in between moves
                for (int j = 0; j < gap; j++) {
                    sb.append('.');
                }
            } else {
                // "R...L" -> both sides collapse inward, middle one stays if gap is odd
                for (int j = 0; j < gap / 2; j++) {
                    sb.append('R');
                }
                if (gap % 2 == 1) {
                    sb.append('.');
                }
                for (int j = 0; j < gap / 2; j++) {
                    sb.append('L');
                }
            }
            prev = i;
        }
        return sb.toString();
    }

    // V1
    // IDEA: FORCE ARRAY - accumulate a decaying rightward force, then a leftward one
    /**
     * time = O(N)
     * space = O(N)
     */
    public String pushDominoes_1(String dominoes) {
        int n = dominoes.length();
        int[] force = new int[n];

        int f = 0;
        for (int i = 0; i < n; i++) {
            char c = dominoes.charAt(i);
            if (c == 'R') {
                f = n;
            } else if (c == 'L') {
                f = 0;
            } else {
                f = Math.max(f - 1, 0);
            }
            force[i] += f;
        }

        f = 0;
        for (int i = n - 1; i >= 0; i--) {
            char c = dominoes.charAt(i);
            if (c == 'L') {
                f = n;
            } else if (c == 'R') {
                f = 0;
            } else {
                f = Math.max(f - 1, 0);
            }
            force[i] -= f;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(force[i] > 0 ? 'R' : (force[i] < 0 ? 'L' : '.'));
        }
        return sb.toString();
    }

    // V2
    // IDEA: brute force simultaneous simulation - one "second" per round, applying the
    //       physics literally until nothing changes; kept as a readable correctness
    //       reference (O(N^2) worst case)
    /**
     * time = O(N^2)
     * space = O(N)
     */
    public String pushDominoes_2(String dominoes) {
        char[] cur = dominoes.toCharArray();
        int n = cur.length;

        while (true) {
            char[] next = cur.clone();
            boolean changed = false;
            for (int i = 0; i < n; i++) {
                if (cur[i] != '.') {
                    continue;
                }
                boolean pushedRight = (i > 0) && cur[i - 1] == 'R';
                boolean pushedLeft = (i < n - 1) && cur[i + 1] == 'L';
                if (pushedRight && !pushedLeft) {
                    next[i] = 'R';
                    changed = true;
                } else if (pushedLeft && !pushedRight) {
                    next[i] = 'L';
                    changed = true;
                }
                // pushed from both sides -> stays upright
            }
            if (!changed) {
                break;
            }
            cur = next;
        }
        return new String(cur);
    }
}
