package LeetCodeJava.Design;

// https://leetcode.com/problems/design-file-system/

import java.util.HashMap;
import java.util.Map;

/**
 *  1166. Design File System
 *  Medium
 *
 *  You are asked to design a file system that allows you to create new paths and
 *  associate them with different values.
 *
 *  The format of a path is one or more concatenated strings of the form: '/' followed
 *  by one or more lowercase English letters. For example, "/leetcode" and
 *  "/leetcode/problems" are valid paths while an empty string "" and "/" are not.
 *
 *  Implement the FileSystem class:
 *   - boolean createPath(String path, int value) Creates a new path and associates a
 *     value to it if possible and returns true. Returns false if the path already
 *     exists or its parent path doesn't exist.
 *   - int get(String path) Returns the value associated with path, or -1 if the path
 *     doesn't exist.
 *
 *  Example 2:
 *    Input:
 *      ["FileSystem","createPath","createPath","get","createPath","get"]
 *      [[],["/leet",1],["/leet/code",2],["/leet/code"],["/c/d",1],["/c"]]
 *    Output:
 *      [null,true,true,2,false,-1]
 *
 *  Constraints:
 *    The number of calls to the two functions is <= 10^4 in total.
 *    2 <= path.length <= 100
 *    1 <= value <= 10^9
 */
public class DesignFileSystem {

    // V0
    // IDEA: a flat HashMap keyed by the FULL path string - no tree needed.
    //       createPath only has to check (a) the path is absent and (b) its parent
    //       (the prefix up to the last '/') is either the root or already present.
    /**
     * time = O(L) per op, L = path length
     * space = O(total path length stored)
     */
    private final Map<String, Integer> paths;

    public DesignFileSystem() {
        this.paths = new HashMap<>();
    }

    /**
     * time = O(L)
     * space = O(L)
     */
    public boolean createPath(String path, int value) {

        if (path == null || path.length() <= 1 || !path.startsWith("/")) {
            return false;
        }
        if (this.paths.containsKey(path)) {
            return false;
        }

        int idx = path.lastIndexOf('/');
        String parent = path.substring(0, idx);

        // idx == 0 -> parent is the root, which always exists
        if (idx == 0 || this.paths.containsKey(parent)) {
            this.paths.put(path, value);
            return true;
        }

        return false;
    }

    /**
     * time = O(L)
     * space = O(1)
     */
    public int get(String path) {
        Integer val = this.paths.get(path);
        return (val == null) ? -1 : val;
    }
}
