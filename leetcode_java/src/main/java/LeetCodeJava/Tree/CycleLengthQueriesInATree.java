package LeetCodeJava.Tree;

// https://leetcode.com/problems/cycle-length-queries-in-a-tree/description/
// https://leetcode.cn/problems/cycle-length-queries-in-a-tree/solutions/
/**
 * 2509. Cycle Length Queries in a Tree
 * Hard
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You are given an integer n. There is a complete binary tree with 2n - 1 nodes. The root of that tree is the node with the value 1, and every node with a value val in the range [1, 2n - 1 - 1] has two children where:
 *
 * The left node has the value 2 * val, and
 * The right node has the value 2 * val + 1.
 * You are also given a 2D integer array queries of length m, where queries[i] = [ai, bi]. For each query, solve the following problem:
 *
 * Add an edge between the nodes with values ai and bi.
 * Find the length of the cycle in the graph.
 * Remove the added edge between nodes with values ai and bi.
 * Note that:
 *
 * A cycle is a path that starts and ends at the same node, and each edge in the path is visited only once.
 * The length of a cycle is the number of edges visited in the cycle.
 * There could be multiple edges between two nodes in the tree after adding the edge of the query.
 * Return an array answer of length m where answer[i] is the answer to the ith query.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: n = 3, queries = [[5,3],[4,7],[2,3]]
 * Output: [4,5,3]
 * Explanation: The diagrams above show the tree of 23 - 1 nodes. Nodes colored in red describe the nodes in the cycle after adding the edge.
 * - After adding the edge between nodes 3 and 5, the graph contains a cycle of nodes [5,2,1,3]. Thus answer to the first query is 4. We delete the added edge and process the next query.
 * - After adding the edge between nodes 4 and 7, the graph contains a cycle of nodes [4,2,1,3,7]. Thus answer to the second query is 5. We delete the added edge and process the next query.
 * - After adding the edge between nodes 2 and 3, the graph contains a cycle of nodes [2,1,3]. Thus answer to the third query is 3. We delete the added edge.
 * Example 2:
 *
 *
 * Input: n = 2, queries = [[1,2]]
 * Output: [2]
 * Explanation: The diagram above shows the tree of 22 - 1 nodes. Nodes colored in red describe the nodes in the cycle after adding the edge.
 * - After adding the edge between nodes 1 and 2, the graph contains a cycle of nodes [2,1]. Thus answer for the first query is 2. We delete the added edge.
 *
 *
 * Constraints:
 *
 * 2 <= n <= 30
 * m == queries.length
 * 1 <= m <= 105
 * queries[i].length == 2
 * 1 <= ai, bi <= 2n - 1
 * ai != bi
 *
 *
 */
public class CycleLengthQueriesInATree {

    // V0
//    public int[] cycleLengthQueries(int n, int[][] queries) {
//
//    }

    // V0-1
    // IDEA: LCA (gpt)
    /**  NOTE !!!
     *
     * A cycle is a path that `starts and ends at the same node,`
     * and each edge in the path is visited only once.
     *
     */
    /** IDEA:
     *
     *  Key idea (short)
     *
     * The tree is a perfect binary tree with nodes labeled 1..(2^n - 1).
     * The parent of node x is x/2 (integer division).
     * If we add an edge between a and b, the cycle length equals the number
     * of edges on the path between a and b in the tree plus the new edge.
     * So we need distance(a,b) + 1. The LCA can be found by repeatedly moving
     * the deeper/higher labeled node up via x /= 2 until a == b.
     * The number of moves is distance(a,b).
     *
     */
    public int[] cycleLengthQueries_0_1(int n, int[][] queries) {
        int m = queries.length;
        int[] ans = new int[m];

        for (int i = 0; i < m; ++i) {
            int a = queries[i][0];
            int b = queries[i][1];

            // start with 1 to account for the extra edge added between a and b
            int length = 1;

            // climb the deeper node until both nodes meet at LCA
            while (a != b) {
                if (a > b) {
                    a /= 2; // move a to its parent
                } else {
                    b /= 2; // move b to its parent
                }
                ++length; // each move is one tree-edge on the path a<->b
            }
            ans[i] = length; // distance(a,b) + 1
        }
        return ans;
    }

    // V0-2
    // IDEA:   Lowest Common Ancestor (LCA)  (gemini)
    /**
     *  IDEA:
     *
     * This problem, LeetCode 2509, "Cycle Length Queries in a Tree," involves finding the length of the cycle formed by adding an edge between two nodes, a and b, in a complete binary tree.
     *
     * Since the tree is a complete binary tree where nodes are indexed from 1, the parent-child relationship is defined by:
     *
     * Parent of node i (for i>1) is ⌊i/2⌋.
     *
     * Children of node i are 2i and 2i+1.
     *
     * The length of the cycle formed by adding the edge (a,b) is given by the length of the path from a to b through the tree, plus the new edge (a,b) itself.
     *
     * Cycle Length=Distance(a,b)+1
     * The distance between two nodes in a tree is:
     *
     * Distance(a,b)=Depth(a)+Depth(b)−2×Depth(LCA(a,b))
     * The Depth (or level) of a node i is related to ⌊log
     * 2
     * ​
     *  i⌋+1. Since the path length is what matters, we can use the height (depth starting from 0) which is simply ⌊log
     * 2
     * ​
     *  i⌋.
     *
     * Algorithm Steps
     * Find the Lowest Common Ancestor (LCA) of a and b. The LCA is the deepest node that is an ancestor of both a and b. In a complete binary tree, we can find the LCA by iteratively moving a and b up to their parents until they become equal.
     *
     * Calculate the distance from a to LCA(a,b) and the distance from b to LCA(a,b).
     *
     * Total Path Length a→LCA→b is the sum of these two distances.
     *
     * Cycle Length is the total path length +1.
     *
     * In the provided Java solution, the distance is found by counting the steps (edges) taken to move a and b up to the LCA.
     */
    public int[] cycleLengthQueries_0_2(int n, int[][] queries) {
        // The problem is about finding the length of the cycle created by adding an edge
        // between two nodes (a, b) in a complete binary tree.
        // Cycle Length = Distance(a, b) + 1
        // Distance(a, b) = steps from a to LCA + steps from b to LCA

        int m = queries.length; // 'm' is the number of queries.
        int[] results = new int[m]; // Array to store the cycle length for each query.

        // Loop through each query [a, b]
        for (int i = 0; i < m; i++) {
            int a = queries[i][0]; // Starting node of the new edge
            int b = queries[i][1]; // Ending node of the new edge

            int stepsA = 0; // Steps (distance) from 'a' up to the LCA
            int stepsB = 0; // Steps (distance) from 'b' up to the LCA

            // This while loop finds the LCA of 'a' and 'b' and counts the steps.
            // The LCA is found when 'a' and 'b' become equal.
            while (a != b) {
                // Determine which node is deeper (larger index means potentially deeper/lower in the tree)
                if (a > b) {
                    // 'a' is lower, move 'a' up to its parent: parent = floor(a/2)
                    a /= 2;
                    stepsA++; // Count one step (edge)
                } else {
                    // 'b' is lower or they are at the same level, move 'b' up
                    b /= 2;
                    stepsB++; // Count one step (edge)
                }
            }

            // At this point, a == b, which is the LCA.

            // Total path length in the tree: stepsA + stepsB
            // Cycle length = path length + 1 (for the new edge (a, b))
            results[i] = stepsA + stepsB + 1;
        }

        return results; // Return the array of cycle lengths.
    }

    // V1
    // https://leetcode.com/problems/cycle-length-queries-in-a-tree/solutions/2923489/javacpython-lowest-common-ancestor-by-le-p3ft/
    public int[] cycleLengthQueries_1(int n, int[][] queries) {
        int m = queries.length, res[] = new int[m];
        for (int i = 0; i < m; ++i) {
            res[i]++;
            int x = queries[i][0], y = queries[i][1];
            while (x != y) {
                if (x > y)
                    x /= 2;
                else
                    y /= 2;
                res[i]++;
            }
        }
        return res;
    }

    // V2
    // https://leetcode.com/problems/cycle-length-queries-in-a-tree/solutions/7118068/cycle-length-queries-in-a-tree-2509q-by-5h8cf/
    public int[] cycleLengthQueries_2(int n, int[][] queries) {
        int m = queries.length;
        int[] result = new int[m];

        for (int i = 0; i < m; i++) {
            int a = queries[i][0], b = queries[i][1];
            int t = 1; // extra edge
            while (a != b) {
                if (a > b) {
                    a /= 2;
                } else {
                    b /= 2;
                }
                t++;
            }
            result[i] = t;
        }
        return result;
    }


}
