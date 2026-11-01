package LeetCodeJava.Graph;

// https://leetcode.com/problems/number-of-ways-to-reconstruct-a-tree/description/

import java.util.*;

/**
 * 1719. Number Of Ways To Reconstruct A Tree
 * Hard
 *
 * You are given an array pairs, where pairs[i] = [xi, yi], and:
 *
 * There are no duplicates.
 * xi < yi
 *
 * Let ways be the number of rooted trees that satisfy the following conditions:
 *
 * The tree consists of nodes whose values appeared in pairs.
 * A pair [xi, yi] exists in pairs if and only if xi is an ancestor of yi or yi is an ancestor of xi.
 * Note: the tree does not have to be a binary tree.
 *
 * Two ways are considered to be different if there is at least one node that has different parents in both ways.
 *
 * Return:
 *
 * 0 if ways == 0
 * 1 if ways == 1
 * 2 if ways > 1
 *
 * A rooted tree is a tree that has a single root node, and all edges are oriented to be outgoing from the root.
 *
 * An ancestor of a node is any node on the path from the root to that node (excluding the node itself).
 * The root has no ancestors.
 *
 *
 * Example 1:
 *
 * Input: pairs = [[1,2],[2,3]]
 * Output: 1
 *
 * Example 2:
 *
 * Input: pairs = [[1,2],[2,3],[1,3]]
 * Output: 2
 *
 * Example 3:
 *
 * Input: pairs = [[1,2],[2,3],[2,4],[1,5]]
 * Output: 0
 *
 *
 * Constraints:
 *
 * 1 <= pairs.length <= 10^5
 * 1 <= xi < yi <= 500
 * The elements in pairs are unique.
 *
 */
public class NumberOfWaysToReconstructATree {

    // V0
    // IDEA: DEGREE ORDERING + ADJACENCY SUBSET CHECK
    // adj(x) (with x itself included) = x's ancestors + x's descendants + x. So:
    //   1) if y is an ancestor of x, adj(x) is a subset of adj(y);
    //   2) an ancestor's degree is >= its descendant's.
    // Sort nodes by degree ascending; x's parent must be its first neighbour later in that
    // order. If adj(x) is not a subset of adj(parent) -> 0. A node with no later neighbour is
    // a root; more than one root -> 0. Equal degrees mean x and parent can swap -> 2.
    /**
     * time = O(V^2 + E), V <= 500
     * space = O(V + E)
     */
    public int checkWays(int[][] pairs) {
        // adjacency sets, each node containing itself so the subset test is clean
        Map<Integer, Set<Integer>> adjacency = new HashMap<>();
        for (int[] pair : pairs) {
            for (int node : pair) {
                if (!adjacency.containsKey(node)) {
                    Set<Integer> self = new HashSet<>();
                    self.add(node);
                    adjacency.put(node, self);
                }
            }
            adjacency.get(pair[0]).add(pair[1]);
            adjacency.get(pair[1]).add(pair[0]);
        }

        List<Integer> nodes = new ArrayList<>(adjacency.keySet());
        nodes.sort((a, b) -> Integer.compare(adjacency.get(a).size(), adjacency.get(b).size()));

        int roots = 0;
        boolean multiple = false;
        for (int i = 0; i < nodes.size(); i++) {
            Set<Integer> childSet = adjacency.get(nodes.get(i));

            // the first later neighbour (degree >= ours) is the only possible parent
            Integer parent = null;
            for (int j = i + 1; j < nodes.size(); j++) {
                if (childSet.contains(nodes.get(j))) {
                    parent = nodes.get(j);
                    break;
                }
            }

            if (parent == null) {
                roots++;
                continue;
            }

            Set<Integer> parentSet = adjacency.get(parent);
            if (!parentSet.containsAll(childSet)) {
                return 0;
            }
            // same neighbourhood: parent and child could trade places
            if (childSet.size() == parentSet.size()) {
                multiple = true;
            }
        }

        if (roots > 1) {
            return 0;
        }
        if (multiple) {
            return 2;
        }
        return 1;
    }
}
