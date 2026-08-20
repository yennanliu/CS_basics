package LeetCodeJava.DFS;

// https://leetcode.com/problems/find-subtree-sizes-after-changes/

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 *  3331. Find Subtree Sizes After Changes
 *  Medium
 *
 *  You are given a tree rooted at node 0 that consists of n nodes numbered from 0 to
 *  n - 1. The tree is represented by an array parent of size n, where parent[i] is the
 *  parent of node i. Since node 0 is the root, parent[0] == -1.
 *
 *  You are also given a string s of length n, where s[i] is the character assigned to
 *  node i.
 *
 *  We make the following changes on the tree one time simultaneously for all nodes x
 *  from 1 to n - 1:
 *    Find the closest node y to node x such that y is an ancestor of x, and s[x] == s[y].
 *    If node y does not exist, do nothing.
 *    Otherwise, remove the edge between x and its current parent and make node y the
 *    new parent of x by adding an edge between them.
 *
 *  Return an array answer of size n where answer[i] is the size of the subtree of node
 *  i in the final tree.
 *
 *  Example 1:
 *    Input: parent = [-1,0,0,1,1,1], s = "abaabc"
 *    Output: [6,3,1,1,1,1]
 *    Explanation: the parent of node 3 changes from node 1 to node 0.
 *
 *  Example 2:
 *    Input: parent = [-1,0,4,0,1], s = "abbba"
 *    Output: [5,2,1,1,1]
 *
 *  Constraints:
 *    n == parent.length == s.length
 *    1 <= n <= 10^5
 *    0 <= parent[i] <= n - 1 for all i >= 1
 *    parent[0] == -1
 *    parent represents a valid tree.
 *    s consists only of lowercase English letters.
 */
public class FindSubtreeSizesAfterChanges {

    // V0
    // IDEA: ONE DFS CARRYING A STACK OF ANCESTORS PER LETTER
    //       "the closest ancestor sharing x's character" is exactly the top of a stack
    //       holding, for each letter, the ancestors currently on the root -> x path.
    //       So a single traversal decides every node's new parent: push a node's letter
    //       on entry, pop it on exit.
    //       the changes are described as simultaneous, but the new parent is always an
    //       ANCESTOR in the ORIGINAL tree, so reading the original ancestor stack gives
    //       the right answer with no ordering subtlety.
    //       with the new parents in hand, subtree sizes come from a second bottom-up
    //       pass over the same (pre-order) traversal order - an ancestor always appears
    //       before its descendants, so walking it BACKWARDS settles children first.
    //       both passes are ITERATIVE - the tree may be a 10^5-node path.
    /**
     * time = O(n)
     * space = O(n)
     */
    public int[] findSubtreeSizes(int[] parent, String s) {

        int n = parent.length;

        List<List<Integer>> children = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            children.add(new ArrayList<Integer>());
        }
        for (int i = 1; i < n; i++) {
            children.get(parent[i]).add(i);
        }

        int[] newParent = new int[n];
        for (int i = 0; i < n; i++) {
            newParent[i] = parent[i];
        }

        // one live ancestor stack per letter
        List<List<Integer>> stacks = new ArrayList<>();
        for (int i = 0; i < 26; i++) {
            stacks.add(new ArrayList<Integer>());
        }

        int[] order = new int[n];
        int orderSize = 0;

        // entries : {node, done}
        Deque<int[]> st = new ArrayDeque<>();
        st.push(new int[]{0, 0});

        while (!st.isEmpty()) {
            int[] cur = st.pop();
            int x = cur[0];
            int c = s.charAt(x) - 'a';

            if (cur[1] == 1) {
                List<Integer> lst = stacks.get(c);
                lst.remove(lst.size() - 1); // leaving x -> pop its letter
                continue;
            }

            List<Integer> lst = stacks.get(c);
            if (!lst.isEmpty()) {
                newParent[x] = lst.get(lst.size() - 1); // closest same-letter ancestor
            }
            lst.add(x);
            order[orderSize++] = x;

            st.push(new int[]{x, 1});
            for (Integer y : children.get(x)) {
                st.push(new int[]{y, 0});
            }
        }

        int[] size = new int[n];
        for (int i = 0; i < n; i++) {
            size[i] = 1;
        }
        for (int i = orderSize - 1; i >= 1; i--) {
            int x = order[i];
            int p = newParent[x];
            if (p != -1 && x != 0) {
                size[p] += size[x];
            }
        }

        return size;
    }
}
