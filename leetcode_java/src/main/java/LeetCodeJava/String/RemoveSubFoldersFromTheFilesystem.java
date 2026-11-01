package LeetCodeJava.String;

// https://leetcode.com/problems/remove-sub-folders-from-the-filesystem/description/

import java.util.*;

/**
 * 1233. Remove Sub-Folders from the Filesystem
 * Medium
 *
 * Given a list of folders folder, return the folders after removing all sub-folders in
 * those folders. You may return the answer in any order.
 *
 * If a folder[i] is located within another folder[j], it is called a sub-folder of it.
 * A sub-folder of folder[j] must start with folder[j], followed by a "/".
 * For example, "/a/b" is a sub-folder of "/a", but "/b" is not a sub-folder of "/a/b/c".
 *
 * The format of a path is one or more concatenated strings of the form: '/' followed by one
 * or more lowercase English letters.
 *
 * For example, "/leetcode" and "/leetcode/problems" are valid paths while an empty string
 * and "/" are not.
 *
 * Example 1:
 *
 * Input: folder = ["/a","/a/b","/c/d","/c/d/e","/c/f"]
 * Output: ["/a","/c/d","/c/f"]
 * Explanation: Folders "/a/b" is a subfolder of "/a" and "/c/d/e" is inside of folder
 * "/c/d" in our filesystem.
 *
 * Example 2:
 *
 * Input: folder = ["/a","/a/b/c","/a/b/d"]
 * Output: ["/a"]
 * Explanation: Folders "/a/b/c" and "/a/b/d" will be removed because they are subfolders
 * of "/a".
 *
 * Example 3:
 *
 * Input: folder = ["/a/b/c","/a/b/ca","/a/b/d"]
 * Output: ["/a/b/c","/a/b/ca","/a/b/d"]
 *
 *
 * Constraints:
 *
 * 1 <= folder.length <= 4 * 10^4
 * 2 <= folder[i].length <= 100
 * folder[i] contains only lowercase letters and '/'.
 * folder[i] always starts with the character '/'.
 * Each folder name is unique.
 *
 */
public class RemoveSubFoldersFromTheFilesystem {

    // V0
    // IDEA: SORT + PREFIX CHECK AGAINST THE LAST KEPT FOLDER
    //       after a lexicographic sort a parent comes right before all of its sub-folders,
    //       so each path only has to be compared with the last folder that was kept.
    //       NOTE !!! the + "/" guard stops "/a/b/ca" counting as a sub-folder of "/a/b/c"
    /**
     * time = O(N * L * log N), L = max path length
     * space = O(N)
     */
    public List<String> removeSubfolders(String[] folder) {
        String[] sorted = folder.clone();
        Arrays.sort(sorted);

        List<String> result = new ArrayList<>();
        for (String path : sorted) {
            boolean isSubFolder = !result.isEmpty()
                    && path.startsWith(result.get(result.size() - 1) + "/");
            if (!isSubFolder) {
                result.add(path);
            }
        }
        return result;
    }

    // V1
    // IDEA: TRIE OVER PATH COMPONENTS
    //       insert every folder split by "/"; walking the trie depth-first, the first node
    //       marked as a folder end is a top-level folder and everything below it is pruned.
    /**
     * time = O(N * L)
     * space = O(N * L)
     */
    public List<String> removeSubfolders_1(String[] folder) {
        TrieNode root = new TrieNode();
        for (String path : folder) {
            TrieNode node = root;
            // "/a/b".split("/") = ["", "a", "b"] -> skip the empty first part
            String[] parts = path.split("/");
            for (int i = 1; i < parts.length; i++) {
                node = node.children.computeIfAbsent(parts[i], x -> new TrieNode());
            }
            node.folder = path;   // mark the end of a real folder
        }

        List<String> result = new ArrayList<>();
        collectTopFolders(root, result);
        return result;
    }

    /** Adds the shallowest folder end on every path below node. */
    private void collectTopFolders(TrieNode node, List<String> result) {
        if (node.folder != null) {
            result.add(node.folder);
            return;   // everything deeper is a sub-folder -> prune
        }
        for (TrieNode child : node.children.values()) {
            collectTopFolders(child, result);
        }
    }

    /** One path component; folder is set when a whole input path ends here. */
    private static class TrieNode {
        Map<String, TrieNode> children = new HashMap<>();
        String folder = null;
    }
}
