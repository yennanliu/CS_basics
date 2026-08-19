package LeetCodeJava.Tree;

// https://leetcode.com/problems/delete-duplicate-folders-in-system/

import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *  1948. Delete Duplicate Folders in System
 *  Hard
 *
 *  Due to a bug, there are many duplicate folders in a file system. You are given
 *  a 2D array paths, where paths[i] is an array representing an absolute path to
 *  the ith folder in the file system. For example, ["one","two","three"]
 *  represents the path "/one/two/three".
 *
 *  Two folders (not necessarily on the same level) are identical if they contain
 *  the same non-empty set of identical subfolders and underlying subfolder
 *  structure. If two or more folders are identical, then mark the folders as well
 *  as all their subfolders.
 *
 *  Once all the identical folders and their subfolders have been marked, the file
 *  system will delete all of them. The file system only runs the deletion once,
 *  so any folders that become identical after the initial deletion are not
 *  deleted.
 *
 *  Return the 2D array ans containing the paths of the remaining folders after
 *  deleting all the marked folders. The paths may be returned in any order.
 *
 *  Example 1:
 *    Input: paths = [["a"],["c"],["d"],["a","b"],["c","b"],["d","a"]]
 *    Output: [["d"],["d","a"]]
 *    Explanation: "/a" and "/c" both contain an empty folder named "b".
 *
 *  Example 2:
 *    Input: paths = [["a"],["c"],["a","b"],["c","b"],["a","b","x"],
 *                    ["a","b","x","y"],["w"],["w","y"]]
 *    Output: [["c"],["c","b"],["a"],["a","b"]]
 *
 *  Constraints:
 *    1 <= paths.length <= 2 * 10^4
 *    1 <= paths[i].length <= 500
 *    1 <= paths[i][j].length <= 10
 *    1 <= sum(paths[i][j].length) <= 2 * 10^5
 *    paths[i][j] consists of lowercase English letters.
 *    No two paths lead to the same folder.
 */
public class DeleteDuplicateFoldersInSystem {

    // V0
    // IDEA: TRIE + SUBTREE SERIALIZATION (a canonical string per folder = its identity)
    //       1) insert every path into a trie, so one trie node == one folder.
    //       2) post-order, give each node a canonical signature:
    //            sig(node) = concat of sorted( name + "(" + sig(child) + ")" )
    //          sorting makes the signature independent of insertion order, and a
    //          LEAF gets the EMPTY signature -- which is exactly why empty
    //          folders are never duplicates of each other.
    //       3) two nodes with the same NON-EMPTY signature are identical folders
    //          -> mark BOTH deleted (the first is remembered in a map).
    //       4) walk the trie again, skipping whole deleted subtrees, emit paths.
    //       NOTE: marking happens on the ORIGINAL tree, all at once -- that is
    //             what "the deletion only runs once" means.
    //       NOTE: paths can be 500 deep -> both traversals are ITERATIVE.
    /**
     * time = O(total signature length)
     * space = O(total signature length)
     */
    public List<List<String>> deleteDuplicateFolder(List<List<String>> paths) {
        // node id -> (name -> child id); TreeMap keeps names sorted for free
        List<TreeMap<String, Integer>> children = new ArrayList<>();
        List<Boolean> deleted = new ArrayList<>();
        children.add(new TreeMap<String, Integer>());
        deleted.add(Boolean.FALSE);

        for (List<String> path : paths) {
            int cur = 0;
            for (String name : path) {
                Integer next = children.get(cur).get(name);
                if (next == null) {
                    children.add(new TreeMap<String, Integer>());
                    deleted.add(Boolean.FALSE);
                    next = children.size() - 1;
                    children.get(cur).put(name, next);
                }
                cur = next;
            }
        }

        // pre-order list, reversed -> a valid post-order (children before parent)
        List<Integer> order = new ArrayList<>();
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(0);
        while (!stack.isEmpty()) {
            int u = stack.pop();
            order.add(u);
            for (int v : children.get(u).values()) {
                stack.push(v);
            }
        }

        String[] sig = new String[children.size()];
        Map<String, Integer> seen = new HashMap<>();
        for (int i = order.size() - 1; i >= 0; i--) {
            int u = order.get(i);
            if (children.get(u).isEmpty()) {
                sig[u] = ""; // leaf -> empty signature, never a dup
                continue;
            }
            List<String> parts = new ArrayList<>();
            for (Map.Entry<String, Integer> e : children.get(u).entrySet()) {
                parts.add(e.getKey() + "(" + sig[e.getValue()] + ")");
            }
            Collections.sort(parts);
            StringBuilder sb = new StringBuilder();
            for (String p : parts) {
                sb.append(p);
            }
            String s = sb.toString();
            sig[u] = s;
            Integer prev = seen.get(s);
            if (prev != null) {
                deleted.set(u, Boolean.TRUE);
                deleted.set(prev, Boolean.TRUE);
            } else {
                seen.put(s, u);
            }
        }

        List<List<String>> res = new ArrayList<>();
        Deque<Integer> nodeStack = new ArrayDeque<>();
        Deque<List<String>> pathStack = new ArrayDeque<>();
        nodeStack.push(0);
        pathStack.push(new ArrayList<String>());
        while (!nodeStack.isEmpty()) {
            int u = nodeStack.pop();
            List<String> path = pathStack.pop();
            if (deleted.get(u)) {
                continue;
            }
            if (!path.isEmpty()) {
                res.add(path);
            }
            for (Map.Entry<String, Integer> e : children.get(u).entrySet()) {
                List<String> nextPath = new ArrayList<>(path);
                nextPath.add(e.getKey());
                nodeStack.push(e.getValue());
                pathStack.push(nextPath);
            }
        }
        return res;
    }
}
