package LeetCodeJava.Tree;

// https://leetcode.com/problems/smallest-missing-genetic-value-in-each-subtree/

import java.util.Arrays;

/**
 *  2003. Smallest Missing Genetic Value in Each Subtree
 *  Hard
 *
 *  There is a family tree rooted at 0 consisting of n nodes numbered 0 to
 *  n - 1. You are given a 0-indexed integer array parents, where parents[i] is
 *  the parent for node i. Since node 0 is the root, parents[0] == -1.
 *
 *  There are 10^5 genetic values, each represented by an integer in the
 *  inclusive range [1, 10^5]. You are given a 0-indexed integer array nums,
 *  where nums[i] is a distinct genetic value for node i.
 *
 *  Return an array ans of length n where ans[i] is the smallest genetic value
 *  that is missing from the subtree rooted at node i.
 *
 *  Example 1:
 *    Input: parents = [-1,0,0,2], nums = [1,2,3,4]
 *    Output: [5,1,1,1]
 *    Explanation: subtree of 0 holds {1,2,3,4} -> 5 missing;
 *                 the other subtrees all miss 1.
 *
 *  Example 2:
 *    Input: parents = [-1,0,1,0,3,3], nums = [5,4,6,2,1,3]
 *    Output: [7,1,1,4,2,1]
 *
 *  Constraints:
 *    n == parents.length == nums.length
 *    2 <= n <= 10^5
 *    parents[0] == -1, parents represents a valid tree.
 *    1 <= nums[i] <= 10^5, each nums[i] is distinct.
 */
public class SmallestMissingGeneticValueInEachSubtree {

    // V0
    // IDEA: WALK UP THE ROOT-PATH OF THE NODE HOLDING VALUE 1 (incremental DFS)
    //       any subtree that does NOT contain the value 1 answers 1. so only
    //       the nodes on the root-path of the node holding 1 can answer > 1,
    //       and those subtrees are NESTED (each contains the previous one).
    //       -> start at the node with value 1 and climb to the root; at every
    //          step add the newly reachable nodes with a DFS that never
    //          revisits a marked node, keeping a "seen value" table plus a
    //          pointer `miss` that only ever moves forward.
    //       each node is visited once overall and `miss` moves at most n times.
    //       NOTE: n can be 10^5, so the DFS uses an explicit stack.
    /**
     * time = O(N)
     * space = O(N)
     */
    public int[] smallestMissingValueSubtree(int[] parents, int[] nums) {
        int n = nums.length;
        int[] res = new int[n];
        Arrays.fill(res, 1);

        // children adjacency via head/next arrays
        int[] head = new int[n];
        int[] nxt = new int[n];
        Arrays.fill(head, -1);
        for (int i = 1; i < n; i++) {
            nxt[i] = head[parents[i]];
            head[parents[i]] = i;
        }

        int start = -1;
        for (int i = 0; i < n; i++) {
            if (nums[i] == 1) {
                start = i;
                break;
            }
        }
        // no node holds value 1 -> every subtree misses 1
        if (start == -1) {
            return res;
        }

        boolean[] seen = new boolean[n + 2];
        boolean[] visited = new boolean[n];
        int[] stack = new int[n];
        int miss = 2;

        int node = start;
        while (node != -1) {
            // add every not-yet-visited node of this subtree
            int sp = 0;
            stack[sp++] = node;
            while (sp > 0) {
                int cur = stack[--sp];
                if (visited[cur]) {
                    continue;
                }
                visited[cur] = true;
                if (nums[cur] <= n + 1) {
                    seen[nums[cur]] = true;
                }
                for (int c = head[cur]; c != -1; c = nxt[c]) {
                    if (!visited[c]) {
                        stack[sp++] = c;
                    }
                }
            }

            while (miss <= n + 1 && seen[miss]) {
                miss++;
            }
            res[node] = miss;
            node = parents[node];
        }
        return res;
    }
}
